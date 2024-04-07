package com.game.tama.ui;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.tama.core.Assets;
import com.game.tama.core.Sprite;
import com.game.tama.util.MatrixUtil;

public class CircleCellButtonLeaf extends SimpleButtonLeaf
{
    public enum Size
    {
        p16(Assets.Names.static_circle_16.toString()),
        p14(Assets.Names.static_circle_14.toString());

        public String asset;

        Size(String asset)
        {
            this.asset = asset;
        }
    }

    private String iconAsset;
    private transient Sprite icon;

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     */
    public CircleCellButtonLeaf(float xPos, float yPos, String asset, Size size)
    {
        this(xPos, yPos, asset, ()->{}, size);
    }

    public CircleCellButtonLeaf(float xPos, float yPos, String asset, Runnable activate, Size size)
    {
        super(xPos, yPos, 16, 16, size.asset, activate);
        iconAsset = asset;
        load();
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        super.draw(display);
        display.drawSprite(icon, pos.x, pos.y);
    }

    @Override
    public void load()
    {
        super.load();
        icon = Assets.getSprite(iconAsset);
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
