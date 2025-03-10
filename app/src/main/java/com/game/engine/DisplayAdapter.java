package com.game.engine;

import com.game.tama.core.Sprite;
import com.game.tama.core.WorldObject;

public interface DisplayAdapter
{
    /**
     * Display the object based on its x, y, and offsets
     *
     * @param t
     */
    public void drawArr(WorldObject t);

    /**
     * Dispay the sprite relative to the world array
     *
     * @param d  the thing to display
     * @param ax array position x
     * @param ay array position y
     */
    public void drawArr(Sprite d, float ax, float ay);

    /**
     * Display at the provided pixel location, with respect to the current
     * matrix
     *
     * @param sprite Thing to display
     * @param x Pixel x
     * @param y Pixel y
     */
    public void drawSprite(Sprite sprite, float x, float y);

    /**
     * Draw the sprite with respect to the matrix
     * @param sprite
     */
    public void drawSprite(Sprite sprite);

    public void setTransform(Transform mat);

    public Transform getTransform();

    /**
     * Translate in world coordinates
     * @param x
     * @param y
     */
    public void translate(float x, float y);

    public void push();

    public void pop();

    public void drawLine(float x1, float y1, float x2, float y2);

    public void drawRect(float x, float y, float width, float height);

    /**
     * preConcat effects the matrix in its *local* frame of reference
     * @param mat
     */
    public void preConcat(Transform mat);



}

