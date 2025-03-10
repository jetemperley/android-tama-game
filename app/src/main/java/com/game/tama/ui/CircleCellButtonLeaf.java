package com.game.tama.ui;

import com.game.engine.DisplayAdapter;
import com.game.android.Asset;
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
     * @param xPos pixel x position of the button
     * @param yPos pixel y position of the button
     */
    public CircleCellButtonLeaf(float xPos,
                                float yPos,
                                Sprite sprite,
                                Size size)
    {
        this(xPos, yPos, sprite, () ->
        {}, size);
    }

    public CircleCellButtonLeaf(float xPos,
                                float yPos,
                                Sprite sprite,
                                Runnable activate,
                                Size size)
    {
        super(xPos, yPos, 1, 1, Asset.getStaticSprite(size.asset), activate);
        this.sprite = sprite;
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        super.draw(display);
        display.drawSprite(sprite, pos.x, pos.y);
    }

    @Override
    public boolean isInside(float x, float y)
    {
        float xdiff = pos.x + size.x - x;
        float ydiff = pos.y + size.x - y;
        float distSq = xdiff * xdiff + ydiff * ydiff;
        return distSq <= size.x * size.x;
    }
}
