package com.game.android;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.game.tama.core.AssetName;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Square
{

    public final FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
        -0.5f,
        -0.5f,
        0.0f,
        // bot left
        0.5f,
        -0.5f,
        0.0f,
        // bot right
        -0.5f,
        0.5f,
        0.0f,
        // top left
        0.5f,
        0.5f,
        0.0f,
        // top right
    };

    public final int[] textureHandle = new int[1];

    public final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    public final int vertexStride = COORDS_PER_VERTEX * 4;// 4 bytes per vertex

    public Square()
    {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
            // (number of coordinate values * 4 bytes per float)
            triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);


    }




}