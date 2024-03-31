package com.game.android;

import android.graphics.Matrix;

import com.game.tama.core.Sprite;
import com.game.tama.core.WorldObject;

public interface DisplayAdapter
{
    /**
     * Display the object based on its x, y, and offsets
     *
     * @param t
     */
    public void displayArr(WorldObject t);

    /**
     * Dispay the sprite relative to the world array
     *
     * @param d  the thing to display
     * @param ax array position x
     * @param ay array position y
     */
    public void displayArr(Sprite d, float ax, float ay);

    /**
     * Display at the povided pixel location, with respect to the current
     * matrix
     *
     * @param d Thing to display
     * @param x Pixel x
     * @param y Pixel y
     */
    public void displayAt(Sprite d, float x, float y);

    public void setMatrix(Matrix mat);

    public Matrix getMatrix();

    public void translate(float x, float y);

    public void push();

    public void pop();

    public void drawLine(float x1, float y1, float x2, float y2);

    public void drawRect(float x, float y, float width, float height);

    /**
     * preConcat effects the matrix in its *local* frame of reference
     * @param mat
     */
    public void preConcat(Matrix mat);
}

