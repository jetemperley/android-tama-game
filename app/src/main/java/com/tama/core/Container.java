package com.tama.core;

import android.graphics.Matrix;

import com.tama.gesture.DoubleTapDragEnd;
import com.tama.gesture.DoubleTapDragStart;
import com.tama.gesture.GestureEvent;
import com.tama.gesture.GestureEventHandler;
import com.tama.gesture.Scroll;
import com.tama.thing.Bush;
import com.tama.thing.Thing;
import com.tama.util.Bounds;
import com.tama.util.Log;
import com.tama.util.MatrixUtil;
import com.tama.util.Vec2;

import java.util.ArrayList;
import java.util.List;

public class Container extends Interactive implements GestureEventHandler
{
    private World world;
    public PetGame parent;

    List<Button> buttons = new ArrayList<>();

    Matrix worldMat;
    Matrix backapackMat;
    Vec2<Float> containerTranslation = new Vec2<Float>(30f, 30f);
    Vec2<Float> inventoryOffset = new Vec2<Float>(0f, 16f);

    public Container(PetGame parent, Matrix worldMat, int size)
    {
        this.parent = parent;
        this.worldMat = worldMat;
        backapackMat = new Matrix();
        backapackMat.set(worldMat);
        MatrixUtil.clearTranslate(backapackMat);

        world = WorldFactory.makeBackpack(2, 2);
        world.add(new Bush(), 0, 0);
        buttons.add(new Button(0, 0, null)
        {
            @Override
            void activate()
            {

            }
        });
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        display.setMatrix(backapackMat);

        display.drawRect(
            0,
            0,
            world.celln * 16,
            (world.celln + 1) * 16);

        world.draw(display);
        display.setMatrix(worldMat);
    }

    @Override
    public void update()
    {
        float scale = MatrixUtil.getScale(worldMat);
        MatrixUtil.setScale(backapackMat, scale);
    }

    @Override
    public void doubleTapDragStart(float startX, float startY, float currX, float currY)
    {
        float[] f = MatrixUtil.convertScreenToWorldArray(backapackMat, startX, startY);
        Thing t = world.checkCollision(f[0], f[1]);
        if (t != null)
        {
            world.pickupThing(t.loc.x, t.loc.y);
            parent.transferFromContainer(t, backapackMat);
        }
    }

    @Override
    public void doubleTapDragEnd(float x, float y)
    {
        float[] loc = MatrixUtil.convertScreenToWorldArray(backapackMat, x, y);
        if (world.addOrClosest(parent.held, (int) loc[0], (int) loc[1]))
        {
            parent.held = null;
            return;
        }
        parent.drop(x, y);
    }

    @Override
    public void scroll(Vec2<Float> prev, Vec2<Float> next)
    {
        backapackMat.postTranslate(next.x - prev.x, next.y - prev.y);
    }

    Matrix temp = new Matrix();

    /**
     * @param x Screen space x
     * @param y Screen space y
     * @return
     */
    public boolean isTouchInside(float x, float y)
    {
        float[] f = MatrixUtil.convertScreenToWorldBits(backapackMat, x, y);
        return Bounds.isInside(f[0], f[1],
            0,
            0,
            32,
            48);
    }

    @Override
    public boolean handleEvent(GestureEvent e)
    {
        if (!isTouchInside(e.x, e.y))
        {
            return false;
        }
        if (e.getClass() == Scroll.class ||
            e.getClass() == DoubleTapDragEnd.class ||
            e.getClass() == DoubleTapDragStart.class)
        {
            e.callEvent(this);
            return true;
        }
        return false;
    }
}
