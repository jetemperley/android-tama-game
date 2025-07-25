package com.game.engine;

import com.game.tama.core.Sprite;
import com.game.tama.core.WorldObject;

public interface DisplayAdapter
{
    float GROUND_LAYER = 0;
    float ABOVE_GROUND_LAYER = -1;
    float BELOW_GROUND_LAYER = 1;
    float UI_LAYER = -99;

    /**
     * Display the object based on its world position
     *
     * @param worldObject
     */
    void draw(WorldObject worldObject);

    /**
     * Dispay the sprite relative to the world array
     *
     * @param sprite the thing to display
     * @param x      world position x
     * @param y      world position y
     */
    void draw(Sprite sprite, float x, float y, float z);

    /**
     * Display at the provided pixel location, with respect to the current
     * matrix
     *
     * @param spriteIsprite to display
     * @param x             world x
     * @param y             world y
     */
    void drawSprite(Sprite sprite, float x, float y);

    /**
     * Draw the sprite with respect to the matrix
     *
     * @param spriteId sprite to display
     */
    void drawSprite(Sprite sprite);

    void setTransform(Transform mat);

    Transform getTransform();

    /**
     * Translate in world coordinates
     *
     * @param x
     * @param y
     */
    void translate(float x, float y);

    void push();

    void pop();

    void drawLine(float x1, float y1, float x2, float y2);

    /**
     * preConcat effects the matrix in its *local* frame of reference
     *
     * @param mat
     */
    void preConcat(Transform mat);

    /**
     * Draws the sprite with no transparency, at x, y and on top of everything else.
     *
     * @param sprite the sprite to draw (ignoring transparency)
     * @param x      world x position
     * @param y      world y position
     */
    void drawUi(Sprite sprite, final float x, final float y);

    void clearRect(float x, float y, float xSize, float ySize);

}

