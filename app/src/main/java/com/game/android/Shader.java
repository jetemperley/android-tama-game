package com.game.android;

import android.opengl.GLES20;

public class Shader
{
    public final int shaderId;

    public Shader(GLAssetName vertex, GLAssetName fragment)
    {
        shaderId = buildShader(vertex, fragment);
    }

    public static int buildShader (GLAssetName vertShaderName, GLAssetName fragShaderName)
    {
        String vertexShaderCode = Asset.getRawContent(vertShaderName);
        String fragmentShaderCode = Asset.getRawContent(fragShaderName);

        int vertexShader = GLDisplay.loadShader(
            GLES20.GL_VERTEX_SHADER,
            vertexShaderCode);
        int fragmentShader = GLDisplay.loadShader(
            GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode);

        // create empty OpenGL ES Program
        int program = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(program, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(program, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(program);

        return program;
    }



}
