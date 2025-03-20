package com.game.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.game.engine.DisplayAdapter;
import com.game.engine.Transform;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.SpriteSheet;
import com.game.tama.core.StaticSprite;
import com.game.tama.core.WorldObject;
import com.game.tama.util.Log;
import com.game.tama.util.Vec;
import com.game.tama.util.Vec2;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Consumer;

public class GLRenderer implements GLSurfaceView.Renderer, DisplayAdapter
{

    Square square;
    private Shader genericShader;
    private final Vec<Float> color = new Vec<>(1f, 1f, 1f);
    private final Vec<Float> tempColor = new Vec<>(1f, 1f, 1f);

    public Consumer<DisplayAdapter> drawWorld = null;

    private Matrix4 currentMatrix = new Matrix4();
    private Stack<float[]> matrixStack = new Stack<>();

    private int[] textureHandles;
    private HashMap<Bitmap, Integer> textures = new HashMap<>();

    public void onSurfaceCreated(GL10 unused, EGLConfig config)
    {
        // Set the background frame color
        Log.log(this, "surface created");
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        square = new Square();
        // IntBuffer intBuffer = IntBuffer.allocate(1);
        // GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS, intBuffer);
        // Log.log(this, "Max textures = " + intBuffer.get(0));
        genericShader = new Shader(GLAssetName.generic_vertex, GLAssetName.generic_fragment);

        loadAllTextures();
    }

    public void onDrawFrame(GL10 unused)
    {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(genericShader.shaderId);
        drawWorld.accept(this);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);
    }

    public static int loadShader(int type, String shaderCode)
    {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void drawArr(WorldObject t)
    {
        Vec2<Float> pos = t.getWorldArrPos();
        drawSprite(t.sprite, pos.x, pos.y);
    }

    @Override
    public void drawArr(Sprite d, float ax, float ay)
    {
        drawSprite(d, ax, ay);
    }

    @Override
    public void drawSprite(Sprite sprite, float x, float y)
    {
        push();
        currentMatrix.preTranslate(x, y, 0);
        draw(genericShader, square, sprite);
        pop();
    }

    @Override
    public void drawSprite(Sprite sprite)
    {
        draw(genericShader, square, sprite);
    }

    @Override
    public void setTransform(Transform transform)
    {
        currentMatrix.setValues(transform);
    }

    @Override
    public Transform getTransform()
    {
        return currentMatrix;
    }

    @Override
    public void translate(float x, float y)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void push()
    {
        float[] values = new float[16];
        currentMatrix.getValues(values);
        matrixStack.push(values);
    }

    @Override
    public void pop()
    {
        currentMatrix.setValues(matrixStack.pop());
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2)
    {
        throw new NoSuchMethodError();
    }

    @Override
    public void drawRect(float x, float y, float width, float height)
    {
        push();
        currentMatrix.preTranslate(x, y, 0);
        currentMatrix.preScale(width, height, 1);
        tempColor.set(color);
        color.set(0f, 0f, 0f);
        draw(genericShader, square, Asset.getStaticSprite(AssetName.static_solid));
        color.set(tempColor);
        pop();
    }

    @Override
    public void preConcat(Transform mat)
    {
        currentMatrix.preMult(mat);
    }

    private void draw(Shader shader, Square square, Sprite sprite)
    {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(shader.shaderId);

        // matrix
        int matrixHandle =
            GLES20.glGetUniformLocation(shader.shaderId, "matrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, currentMatrix.getValues(), 0);

        // vertex pos
        int positionHandle =
            GLES20.glGetAttribLocation(shader.shaderId, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
            positionHandle, square.COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            square.vertexStride, square.vertexBuffer);

        // Set filtering
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_NEAREST);
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_NEAREST);

        int texUniform =
            GLES20.glGetUniformLocation(shader.shaderId, "u_Texture");

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures.get(sprite.getSprite()));

        // Tell the texture uniform sampler to use this texture in the shader
        // by binding to texture unit 0.
        GLES20.glUniform1i(texUniform, 0);

        int colorUniform =
            GLES20.glGetUniformLocation(shader.shaderId, "tintColor");
        GLES20.glUniform3f(colorUniform, color.r(), color.g(), color.b());

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, square.vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private void loadAllTextures()
    {
        textureHandles = new int[numTexturesRequired()];
        GLES20.glGenTextures(textureHandles.length, textureHandles, 0);

        int currentTex = 0;
        for (Field f : AssetName.class.getFields())
        {
            AssetName name = AssetName.valueOf(f.getName());
            if (name.name().startsWith("sheet"))
            {
                SpriteSheet sheet = Asset.getSpriteSheet(name);
                for (int row = 0; row < sheet.numRows(); row++)
                {
                    for (int col = 0; col < sheet.rowLength(row); col++)
                    {
                        Bitmap bitmap = sheet.get(row, col);
                        loadSingleTexture(
                            textureHandles[currentTex],
                            bitmap);
                        textures.put(bitmap, textureHandles[currentTex]);
                        currentTex++;
                    }
                }
            }
            else
            {
                StaticSprite sprite = Asset.getStaticSprite(name);
                if (sprite == null)
                {
                    Log.log(this, name.name());
                }
                loadSingleTexture(
                    textureHandles[currentTex],
                    sprite.getSprite());
                textures.put(sprite.getSprite(), textureHandles[currentTex]);
                currentTex++;
            }
        }
    }

    private void loadSingleTexture(int texHandle, Bitmap bitmap)
    {

        // do texture stuff
        // Bind to the texture in OpenGL
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texHandle);

        ByteBuffer byteBuffer =
            ByteBuffer.allocateDirect(bitmap.getByteCount());

        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.position(0);
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            bitmap.getWidth(),
            bitmap.getHeight(),
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            byteBuffer);
    }

    private int numTexturesRequired()
    {
        int count = 0;
        for (Field f : AssetName.class.getDeclaredFields())
        {
            if (f.getName().startsWith("sheet"))
            {
                count += Asset.getSpriteSheet(AssetName.valueOf(f.getName()))
                    .totalSprites();
            }
            else
            {
                count++;
            }
        }
        return count;
    }
}
