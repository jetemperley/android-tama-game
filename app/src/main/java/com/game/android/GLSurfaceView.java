package com.game.android;

class GLSurfaceView extends android.opengl.GLSurfaceView
{

    public final GLDisplay renderer;

    public GLSurfaceView(final GameActivity context)
    {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new GLDisplay();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
        //        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
