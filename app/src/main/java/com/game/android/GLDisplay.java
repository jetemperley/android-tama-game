package com.game.android;

import android.graphics.Bitmap;
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
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Consumer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLDisplay implements GLSurfaceView.Renderer, DisplayAdapter
{

    Square square;
    private Shader genericShader;
    private Shader uiShader;
    private final Vec<Float> color = new Vec<>(1f, 1f, 1f);
    private final Vec<Float> tempColor = new Vec<>(1f, 1f, 1f);

    public Consumer<DisplayAdapter> drawWorld = null;

    private final Matrix4 currentMatrix = new Matrix4();
    private final Stack<float[]> matrixStack = new Stack<>();

    private int[] textureHandles;
    private final HashMap<Bitmap, Integer> textures = new HashMap<>();

    public void onSurfaceCreated(final GL10 unused, final EGLConfig config)
    {
        // Set the background frame color
        Log.log(this, "surface created");
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        square = new Square();
        genericShader = new Shader(GLAssetName.generic_vertex, GLAssetName.generic_fragment);
        uiShader = new Shader(GLAssetName.generic_vertex, GLAssetName.ui_fragment);
        loadAllTextures();
    }

    public void onDrawFrame(final GL10 unused)
    {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        Log.log(this, "draw time " + LocalTime.now());
        drawWorld.accept(this);
    }

    public void onSurfaceChanged(final GL10 unused, final int width, final int height)
    {
        GLES20.glViewport(0, 0, width, height);
    }

    public static int loadShader(final int type, final String shaderCode)
    {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        final int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void draw(final WorldObject t)
    {
        final Vec2<Float> pos = t.getWorldArrPos();
        if (t.isFlat)
        {
            drawSprite(t.sprite, pos.x, pos.y, 0);
            return;
        }
        drawSprite(t.sprite, pos.x, pos.y, -1);
    }

    @Override
    public void draw(final Sprite d, final float x, final float y, final float z)
    {

        drawSprite(d, x, y, z);
    }

    @Override
    public void drawSprite(final Sprite sprite, final float x, final float y)
    {
        drawSprite(sprite, x, y, -99);
    }

    public void drawSprite(final Sprite sprite, final float x, final float y, final float z)
    {
        push();
        currentMatrix.preTranslate(x, y, z);
        draw(genericShader, square, sprite);
        pop();
    }

    @Override
    public void drawSprite(final Sprite sprite)
    {
        draw(genericShader, square, sprite);
    }

    @Override
    public void setTransform(final Transform transform)
    {
        currentMatrix.setValues(transform);
    }

    @Override
    public Transform getTransform()
    {
        return currentMatrix;
    }

    @Override
    public void translate(final float x, final float y)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void push()
    {
        final float[] values = new float[16];
        currentMatrix.getValues(values);
        matrixStack.push(values);
    }

    @Override
    public void pop()
    {
        currentMatrix.setValues(matrixStack.pop());
    }

    @Override
    public void drawLine(final float x1, final float y1, final float x2, final float y2)
    {
        throw new NoSuchMethodError();
    }

    @Override
    public void preConcat(final Transform mat)
    {
        currentMatrix.preMult(mat);
    }

    private void draw(final Shader shader, final Square square, final Sprite sprite)
    {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(shader.shaderId);

        // matrix
        final int matrixHandle =
            GLES20.glGetUniformLocation(shader.shaderId, "matrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, currentMatrix.getValues(), 0);

        // vertex pos
        final int positionHandle =
            GLES20.glGetAttribLocation(shader.shaderId, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
            positionHandle, Square.COORDS_PER_VERTEX,
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

        final int texUniform =
            GLES20.glGetUniformLocation(shader.shaderId, "u_Texture");

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        final int i = textures.get(sprite.getSprite());
        final Bitmap b =
            textures.entrySet().stream().filter(k -> k.getValue() == 37).findFirst().get().getKey();

        // Bind the texture to this unit.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i);

        // Tell the texture uniform sampler to use this texture in the shader
        // by binding to texture unit 0.
        GLES20.glUniform1i(texUniform, 0);

        final int colorUniform =
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
        for (final Field f : AssetName.class.getFields())
        {
            final AssetName name = AssetName.valueOf(f.getName());
            if (name.name().startsWith("sheet"))
            {
                final SpriteSheet sheet = Asset.getSpriteSheet(name);
                for (int row = 0; row < sheet.numRows(); row++)
                {
                    for (int col = 0; col < sheet.rowLength(row); col++)
                    {
                        final Bitmap bitmap = sheet.get(row, col);
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
                final StaticSprite sprite = Asset.getStaticSprite(name);
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

    private void loadSingleTexture(final int texHandle, final Bitmap bitmap)
    {

        // do texture stuff
        // Bind to the texture in OpenGL
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texHandle);

        final ByteBuffer byteBuffer =
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
        for (final Field f : AssetName.class.getDeclaredFields())
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

    public void drawUi(final Sprite sprite, final float x, final float y)
    {
        push();
        currentMatrix.preTranslate(x, y, -5);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        draw(genericShader, square, sprite);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        pop();
    }

    @Override
    public void clearRect(final float x, final float y, final float xSize, final float ySize)
    {
        // todo implement size
        drawUi(Asset.getStaticSprite(AssetName.static_solid), x, y);
    }

}
