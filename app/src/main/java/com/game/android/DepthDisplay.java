package com.game.android;

import com.game.engine.DisplayAdapter;
import com.game.engine.Sprite;
import com.game.engine.Transform;
import com.game.tama.core.world.WorldObject;

import java.util.Comparator;
import java.util.PriorityQueue;

public class DepthDisplay implements DisplayAdapter
{

    public DisplayAdapter display;
    private final PriorityQueue<WorldObject> draws;
    boolean check = true;

    public DepthDisplay()
    {
        draws = new PriorityQueue<>(200, new DepthComp());
    }

    @Override
    public void draw(final WorldObject worldObject)
    {
        draws.add(worldObject);
    }

    @Override
    public void draw(final Sprite d, final float x, final float y, final float z)
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void drawSprite(final Sprite sprite, final float x, final float y)
    {

    }

    @Override
    public void drawSprite(final Sprite sprite)
    {
        throw new NoSuchMethodError();
    }

    @Override
    public void drawLine(final float x1, final float y1, final float x2, final float y2)
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void clearRect(final float x, final float y, final float width, final float height)
    {
        throw new RuntimeException("Operation not supported");
    }

    public void drawQ()
    {
        if (display != null)
        {
            WorldObject b;
            while (!draws.isEmpty())
            {
                b = draws.poll();
                display.draw(b);
            }
        }
        check = false;
    }

    public void clearQ()
    {
        draws.clear();
    }

    class DepthComp implements Comparator<WorldObject>
    {
        @Override
        public int compare(final WorldObject a, final WorldObject b)
        {
            if (b.isFlat && a.isFlat)
            {
                return 0;
            }
            if (a.isFlat)
            {
                return -1;
            }
            if (b.isFlat)
            {
                return 1;
            }
            return (a.y + a.yoff / 100f) > (b.y + b.yoff / 100f) ? 1 : -1;
        }
    }

    @Override
    public void setTransform(final Transform mat)
    {
        display.setTransform(mat);
    }

    @Override
    public Transform getTransform()
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void translate(final float x, final float y)
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void push()
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void pop()
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void preConcat(final Transform mat)
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void drawUi(final Sprite sprite, final float x, final float y)
    {

    }
}
