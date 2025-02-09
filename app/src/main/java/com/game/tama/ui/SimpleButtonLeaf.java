package com.game.tama.ui;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.Down;
import com.game.android.gesture.GestureEvent;
import com.game.tama.core.Sprite;
import com.game.tama.util.Vec2;

public class SimpleButtonLeaf extends UIComposite
{
    public Vec2<Float> pos;
    public Vec2<Float> size;

    protected Sprite sprite;
    protected Runnable action;

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     * @param width  width in pixels of the button
     * @param height height in pixels of the button
     * @param sprite
     * @param action
     */
    public SimpleButtonLeaf(float xPos,
                            float yPos,
                            float width,
                            float height,
                            Sprite sprite,
                            Runnable action)
    {
        this.pos = new Vec2<>(xPos, yPos);
        this.size = new Vec2<>(width, height);
        this.action = action;
        this.sprite = sprite;
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        display.drawSprite(sprite, pos.x, pos.y);
    }

    public void activate()
    {
        action.run();
    }

    @Override
    public boolean handleEvent(GestureEvent e, Matrix mat)
    {
        if (e instanceof Down && isInside(e.x, e.y, mat))
        {
            activate();
            return true;
        }
        return false;
    }

    /**
     * Returns whether point x y is inside this button
     *
     * @param x      point to check
     * @param y      point to check
     * @param matrix with respect to this matrix
     * @return
     */
    public boolean isInside(float x, float y, Matrix matrix)
    {
        float[] posArr = new float[]{
            pos.x,
            pos.y};
        float[] sizeArr = new float[]{
            size.x,
            size.y};
        matrix.mapPoints(posArr);
        matrix.mapVectors(sizeArr);
        return (
            x > posArr[0] &&
                y > posArr[1] &&
                x < posArr[0] + sizeArr[0] &&
                y < posArr[1] + sizeArr[1]);
    }
}
