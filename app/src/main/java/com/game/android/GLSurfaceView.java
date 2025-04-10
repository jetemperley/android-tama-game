package com.game.android;

import android.content.Context;

class GLSurfaceView extends android.opengl.GLSurfaceView
{

    public final GLRenderer renderer;

    public GLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new GLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
       // setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
