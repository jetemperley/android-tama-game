package com.game.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.WorldObject;
import com.game.tama.util.Log;

import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Consumer;

public class GLRenderer implements GLSurfaceView.Renderer, DisplayAdapter {

    Square square;
    private Shader genericShader;

    public Consumer<DisplayAdapter> drawWorld = null;

    private Matrix currentMatrix = new Matrix();
    private Stack<float[]> matrixStack = new Stack<>();

    private HashMap<Bitmap, Integer> textures = new HashMap<>();

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        Log.log(this, "surface created");
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        square = new Square();
        // IntBuffer intBuffer = IntBuffer.allocate(1);
        // GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS, intBuffer);
        // Log.log(this, "Max textures = " + intBuffer.get(0));
        genericShader = new Shader(GLAssetName.vertex, GLAssetName.fragment);

        for (Field f : AssetName.class.getDeclaredFields())
        {
            if (f.getName().startsWith("sheet"))
            {

            }
        }

    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(genericShader.shaderId);
        drawWorld.accept(this);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public static int loadShader(int type, String shaderCode){

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

    }

    @Override
    public void drawArr(Sprite d, float ax, float ay)
    {

    }

    @Override
    public void drawSprite(Sprite d, float x, float y)
    {
        push();
        currentMatrix.preTranslate(x, y);

        square.draw();
        pop();
    }

    @Override
    public void setMatrix(Matrix mat)
    {
        currentMatrix.reset();
        currentMatrix.preConcat(mat);
    }

    @Override
    public Matrix getMatrix()
    {
        return currentMatrix;
    }

    @Override
    public void translate(float x, float y)
    {

    }

    @Override
    public void push()
    {
        float[] values = new float[9];
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

    }

    @Override
    public void drawRect(float x, float y, float width, float height)
    {

    }

    @Override
    public void preConcat(Matrix mat)
    {
        currentMatrix.preConcat(mat);
    }

    private void draw(Shader shader, Square square, Sprite sprite)
    {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(shader.shaderId);

        // get handle to vertex shader's vPosition member
        int positionHandle = GLES20.glGetAttribLocation(shader.shaderId, "vPosition");

        // Enable a handle to the triangle vertices
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

        int texUniform = GLES20.glGetUniformLocation(shader.shaderId, "u_Texture");

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glActiveTexture ( GLES20.GL_TEXTURE0 );
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, square.textureHandle[0]);

        // Tell the texture uniform sampler to use this texture in the shader
        // by binding to texture unit 0.
        GLES20.glUniform1i(texUniform, 0);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, square.vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
