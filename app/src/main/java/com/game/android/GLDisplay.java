package com.game.android;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.game.engine.DisplayAdapter;
import com.game.engine.Sprite;
import com.game.engine.SpriteSheet;
import com.game.engine.StaticSprite;
import com.game.engine.Transform;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.world.WorldObject;
import com.game.tama.util.Log;
import com.game.tama.util.Vec2;
import com.game.tama.util.Vec4;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLDisplay implements GLSurfaceView.Renderer, DisplayAdapter
{

    Square square;
    private Shader genericShader;
    private final Vec4<Float> color = new Vec4<>(1f, 1f, 1f, 1f);
    private final Vec4<Float> tempColor = new Vec4<>(1f, 1f, 1f, 1f);

    public Consumer<DisplayAdapter> drawWorld = null;

    private final Matrix4 currentMatrix = new Matrix4();
    private final Stack<float[]> matrixStack = new Stack<>();

    private final HashMap<Bitmap, Integer> textures = new HashMap<>();

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config)
    {
        // Set the background frame color
        Log.log(this, "surface created");
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        square = new Square();
        final HashMap<GLAssetName, String> raw = AndroidAsset.loadGlsl();
        genericShader =
            new Shader(raw.get(GLAssetName.generic_vertex), raw.get(GLAssetName.generic_fragment));
        loadAllTextures();

        GameActivity.gameActivity.setupEngine();
    }

    @Override
    public void onDrawFrame(final GL10 unused)
    {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        drawWorld.accept(this);
    }

    @Override
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
    public void draw(final WorldObject worldObject)
    {
        final Vec2<Float> pos = worldObject.getWorldArrPos();
        if (worldObject.isFlat)
        {
            draw(worldObject.sprite, pos.x, pos.y, GROUND_LAYER);
            return;
        }
        draw(worldObject.sprite, pos.x, pos.y, ABOVE_GROUND_LAYER);
    }

    @Override
    public void draw(final Sprite sprite, final float x, final float y, final float z)
    {

        push();
        currentMatrix.preTranslate(x, y, z);
        draw(genericShader, square, sprite);
        pop();
    }

    @Override
    public void drawSprite(final Sprite sprite, final float x, final float y)
    {
        draw(sprite, x, y, UI_LAYER);
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

        final int texUniform =
            GLES20.glGetUniformLocation(shader.shaderId, "u_Texture");
        // Bind the texture to this unit.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sprite.getSpriteId());
        // Tell the texture uniform sampler to use this texture in the shader
        // by binding to texture unit 0.
        GLES20.glUniform1i(texUniform, 0);

        // Set filtering
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_NEAREST);
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_NEAREST);

        final int colorUniform =
            GLES20.glGetUniformLocation(shader.shaderId, "tintColor");
        GLES20.glUniform3f(colorUniform, color.x, color.y, color.z);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, square.vertexCount);
        final int error = GLES20.glGetError();
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private static void loadAllTextures()
    {
        final HashMap<AssetName, Bitmap> sprites = AndroidAsset.loadStaticSprites();
        final HashMap<AssetName, Bitmap[][]> sheets = AndroidAsset.loadSpriteSheets();

        final int numTextures = numTexturesRequired(sprites.values(), sheets.values());
        final int[] textureHandles = new int[numTextures];
        GLES20.glGenTextures(textureHandles.length, textureHandles, 0);

        int currentTex = 0;
        for (final Map.Entry<AssetName, Bitmap> spriteAsset : sprites.entrySet())
        {
            final int textureHandle = textureHandles[currentTex];
            glLoadTexture(textureHandle, spriteAsset.getValue());
            final StaticSprite sprite = new StaticSprite(textureHandles[currentTex]);
            Asset.sprites.put(spriteAsset.getKey(), sprite);
            currentTex++;
        }
        for (final Map.Entry<AssetName, Bitmap[][]> sheetAsset : sheets.entrySet())
        {
            final Bitmap[][] bitmaps = sheetAsset.getValue();
            final StaticSprite[][] spriteArr = new StaticSprite[bitmaps.length][bitmaps[0].length];
            for (int i = 0; i < bitmaps.length; i++)
            {
                for (int j = 0; j < bitmaps[0].length; j++)
                {
                    final int textureHandle = textureHandles[currentTex];
                    glLoadTexture(textureHandle, bitmaps[i][j]);
                    spriteArr[i][j] = new StaticSprite(textureHandle);
                    currentTex++;
                }
            }
            final SpriteSheet sheet = new SpriteSheet(spriteArr);
            Asset.sheets.put(sheetAsset.getKey(), sheet);
        }
    }

    private static void glLoadTexture(final int texHandle, final Bitmap bitmap)
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

    private static int numTexturesRequired(final Collection<Bitmap> sprites,
                                           final Collection<Bitmap[][]> sheets)
    {
        int count = sprites.size();
        for (final Bitmap[][] sheet : sheets)
        {
            count += sheet.length * sheet[0].length;
        }
        return count;
    }

    @Override
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
        drawUi(Asset.sprites.get(AssetName.static_solid), x, y);
    }

}
