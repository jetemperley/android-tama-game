package com.game.android;

import android.graphics.Matrix;

import com.game.tama.core.Sprite;
import com.game.tama.core.WorldObject;

import java.util.Comparator;
import java.util.PriorityQueue;

public class DepthDisplay implements DisplayAdapter
{

    public DisplayAdapter display;
    private PriorityQueue<WorldObject> draws;
    boolean check = true;

    public DepthDisplay()
    {
        draws = new PriorityQueue<>(200, new DepthComp());
    }

    public void drawArr(WorldObject t)
    {
        draws.add(t);
    }

    @Override
    public void drawArr(Sprite d, float x, float y)
    {
        throw new RuntimeException("Operation not supported");
    }

    public void drawSprite(Sprite d, float x, float y)
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2)
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void drawRect(float x, float y, float width, float height)
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
                display.drawArr(b);
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
        public int compare(WorldObject a, WorldObject b)
        {
            if (b.flat && a.flat)
            {
                return 0;
            }
            if (a.flat)
            {
                return -1;
            }
            if (b.flat)
            {
                return 1;
            }
            return (a.y + a.yoff / 100f) > (b.y + b.yoff / 100f) ? 1 : -1;
        }
    }

    @Override
    public void setMatrix(Matrix mat)
    {
        display.setMatrix(mat);
    }

    @Override
    public Matrix getMatrix()
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void translate(float x, float y)
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
    public void preConcat(Matrix mat)
    {
        throw new RuntimeException("Operation not supported");
    }
}
