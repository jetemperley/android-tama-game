package com.tama.core;

import android.graphics.Matrix;

import com.tama.gesture.DoubleTapDragEnd;
import com.tama.gesture.DoubleTapDragStart;
import com.tama.gesture.GestureEvent;
import com.tama.gesture.GestureEventHandler;
import com.tama.gesture.Scroll;
import com.tama.thing.Thing;
import com.tama.thing.Tree;
import com.tama.util.Bounds;
import com.tama.util.MatrixUtil;

public class Container extends Interactive implements GestureEventHandler
{
    private World world;
    private PetGame parent;

    Matrix mat;

    public Container(PetGame parent, Matrix matrix, int size)
    {
        this.mat = matrix;
        this.parent = parent;
        world = WorldFactory.makeBackpack(size, size);
        world.add(new Tree(Tree.GrowthLevel.large_3), 0, 0);
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        display.push();
        display.preConcat(mat);
        display.drawRect(
            0,
            0,
            world.celln * 16,
            (world.celln) * 16);

        world.draw(display);
        display.pop();
    }

    @Override
    public void update()
    {
    }

    @Override
    public void doubleTapDragStart(float startX,
                                   float startY,
                                   float currX,
                                   float currY)
    {
        float[] f = MatrixUtil.convertScreenToWorldArray(mat, startX, startY);
        Thing t = world.checkCollision(f[0], f[1]);
        if (t != null)
        {
            world.pickupThing(t.loc.x, t.loc.y);
            parent.transferFromContainer(t, mat);
        }
    }

    @Override
    public void doubleTapDragEnd(float x, float y)
    {
        float[] loc = MatrixUtil.convertScreenToWorldArray(mat, x, y);
        if (world.addOrClosest(parent.held, (int) loc[0], (int) loc[1]))
        {
            parent.held = null;
            return;
        }
        parent.drop(x, y);
    }

    /**
     * @param x Screen space x
     * @param y Screen space y
     * @return
     */
    public boolean isTouchInside(float x, float y)
    {
        float[] f = MatrixUtil.convertScreenToWorldBits(mat, x, y);
        return Bounds.isInside(f[0], f[1],
            0,
            0,
            world.celln*16,
            world.celln*16);
    }

    @Override
    public boolean handleEvent(GestureEvent e)
    {
        if (!isTouchInside(e.x, e.y))
        {
            return false;
        }
        Class clazz = e.getClass();
        if (clazz == DoubleTapDragEnd.class ||
            clazz == DoubleTapDragStart.class)
        {
            e.callEvent(this);
            return true;
        }
        return e.type() == GestureEvent.Type.press;
    }
}
