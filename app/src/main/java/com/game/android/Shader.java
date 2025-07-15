package com.game.android;

import android.opengl.GLES20;

public class Shader
{
    public final int shaderId;

    public Shader(final String vertexShaderCode, final String fragmentShaderCode)
    {
        shaderId = buildShader(vertexShaderCode, fragmentShaderCode);
    }

    public static int buildShader(final String vertexShaderCode,
                                  final String fragmentShaderCode)
    {
        final int vertexShader = GLDisplay.loadShader(
            GLES20.GL_VERTEX_SHADER,
            vertexShaderCode);
        final int fragmentShader = GLDisplay.loadShader(
            GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode);

        // create empty OpenGL ES Program
        final int program = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(program, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(program, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(program);

        return program;
    }


}
