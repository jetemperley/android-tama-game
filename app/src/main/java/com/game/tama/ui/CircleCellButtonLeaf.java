package com.game.tama.ui;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.android.Assets;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;

public class CircleCellButtonLeaf extends SimpleButtonLeaf
{
    public enum Size
    {
        p16(AssetName.static_circle_16),
        p14(AssetName.static_circle_14);

        public AssetName asset;

        Size(AssetName asset)
        {
            this.asset = asset;
        }
    }

    private Sprite sprite;

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     */
    public CircleCellButtonLeaf(float xPos, float yPos, Sprite sprite, Size size)
    {
        this(xPos, yPos, sprite, ()->{}, size);
    }

    public CircleCellButtonLeaf(float xPos, float yPos, Sprite sprite, Runnable activate, Size size)
    {
        super(xPos, yPos, 16, 16, Assets.getStaticSprite(size.asset), activate);
        this.sprite = sprite;
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        super.draw(display);
        display.drawSprite(sprite, pos.x, pos.y);
    }

    @Override
    public boolean isInside(float x, float y, Matrix matrix)
    {
        float[] posArr = new float[]{
            pos.x,
            pos.y};
        float[] sizeArr = new float[]{
            8,
            0};
        matrix.mapPoints(posArr);
        matrix.mapVectors(sizeArr);
        float xdiff = posArr[0] + sizeArr[0] - x;
        float ydiff = posArr[1] + sizeArr[0] - y;
        float distSq = xdiff*xdiff + ydiff*ydiff;
        return distSq <= sizeArr[0]*sizeArr[0];
    }

}
