package com.game.tama.ui;

import com.game.engine.DisplayAdapter;
import com.game.engine.Sprite;
import com.game.engine.gesture.gestureEvent.Down;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.tama.util.Bounds;
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
    public SimpleButtonLeaf(final float xPos,
                            final float yPos,
                            final float width,
                            final float height,
                            final Sprite sprite,
                            final Runnable action)
    {
        this.pos = new Vec2<>(xPos, yPos);
        this.size = new Vec2<>(width, height);
        this.action = action;
        this.sprite = sprite;
    }

    @Override
    public void draw(final DisplayAdapter display)
    {
        display.drawSprite(sprite, pos.x, pos.y);
    }

    public void activate()
    {
        action.run();
    }

    @Override
    public boolean handleEvent(final GestureEvent e)
    {
        if (e instanceof Down && isInside(e.x, e.y))
        {
            activate();
            return true;
        }
        return false;
    }

    @Override
    public boolean isInside(final float x, final float y)
    {
        return Bounds.isInside(x, y, pos.x, pos.y, size.x, size.y);
    }
}
