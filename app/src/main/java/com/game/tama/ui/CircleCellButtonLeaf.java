package com.game.tama.ui;

import com.game.engine.DisplayAdapter;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;

public class CircleCellButtonLeaf extends SimpleButtonLeaf
{
    public enum Size
    {
        p16(AssetName.static_circle_16),
        p14(AssetName.static_circle_14);

        public AssetName asset;

        Size(final AssetName asset)
        {
            this.asset = asset;
        }
    }

    private final Sprite sprite;

    /**
     * @param xPos pixel x position of the button
     * @param yPos pixel y position of the button
     */
    public CircleCellButtonLeaf(final float xPos,
                                final float yPos,
                                final Sprite sprite,
                                final Size size)
    {
        this(xPos, yPos, sprite, () ->
        {
        }, size);
    }

    public CircleCellButtonLeaf(final float xPos,
                                final float yPos,
                                final Sprite sprite,
                                final Runnable activate,
                                final Size size)
    {
        super(xPos, yPos, 1, 1, Asset.sprites.get(size.asset), activate);
        this.sprite = sprite;
    }

    @Override
    public void draw(final DisplayAdapter display)
    {
        super.draw(display);
        display.drawSprite(sprite, pos.x, pos.y);
    }

    @Override
    public boolean isInside(final float x, final float y)
    {
        final float xdiff = pos.x + size.x - x;
        final float ydiff = pos.y + size.x - y;
        final float distSq = xdiff * xdiff + ydiff * ydiff;
        return distSq <= size.x * size.x;
    }
}
