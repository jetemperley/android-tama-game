package com.game.tama.core;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.tama.thing.Thing;
import com.game.tama.util.Bounds;
import com.game.tama.util.MatrixUtil;
import com.game.tama.util.Vec2;
import com.game.android.gesture.DoubleTapDragEnd;
import com.game.android.gesture.DoubleTapDragStart;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.GestureEventHandler;
import com.game.tama.util.Log;

public class Container extends Thing implements GestureEventHandler
{
    private World world;
    private PetGame parent;

    Matrix mat;

    public Container(PetGame parent, int size)
    {
        this.mat = new Matrix();
        this.parent = parent;
        world = WorldFactory.makeBackpack(size, size);
        asset = Assets.Names.static_backpack.name();
        load();
    }

    public void drawWorld(DisplayAdapter display)
    {
        display.push();
        Vec2<Float> pos = loc.getWorldBitPos();
        mat.setTranslate(pos.x, pos.y+16);
        display.preConcat(mat);
        display.drawRect(
            0,
            0,
            world.celln * 16,
            world.celln * 16);

        world.draw(display);
        display.pop();
    }

    @Override
    public void poke()
    {
        ContainerManager manager = parent.getBehaviour(ContainerManager.class);

        if (manager.containers.contains(this))
        {
            manager.containers.remove(this);
        }
        else
        {
            manager.containers.add(this);
        }
    }

    public void doubleTapDragStart(float startX,
                                   float startY,
                                   float currX,
                                   float currY)
    {
        float[] f = MatrixUtil.convertScreenToWorldArray(parent.worldMat, startX, startY);
        f[0] -= loc.x;
        f[1] -= loc.y +1;
        Thing t = world.checkCollision(f[0], f[1]);
        if (t != null)
        {
            world.pickupThing(t.loc.x, t.loc.y);
            parent.transferFromContainer(t, mat);
        }
    }

    public void doubleTapDragEnd(float x, float y)
    {
        float[] arrPos = MatrixUtil.convertScreenToWorldArray(parent.worldMat, x, y);
        arrPos[0] -= loc.x;
        arrPos[1] -= loc.y +1;
        Log.log(this, "doubleTapDragEnd drop loc " + arrPos[0] + " " + arrPos[1]);
        if (world.addOrClosest(parent.held, (int) arrPos[0], (int) arrPos[1]))
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
        mat.setTranslate(0, 16);
        mat.preConcat(parent.worldMat);
        float[] f = MatrixUtil.convertScreenToWorldBits(mat, x, y);

        Vec2<Float> pos = loc.getWorldBitPos();
        return Bounds.isInside(f[0], f[1],
            pos.x,
            pos.y + 16,
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
        Log.log(this, "Touch was inside.");
        Class clazz = e.getClass();
        if (clazz == DoubleTapDragEnd.class)
        {
            doubleTapDragEnd(e.x, e.y);
            return true;
        }
        if (clazz == DoubleTapDragStart.class)
        {
            DoubleTapDragStart es = (DoubleTapDragStart) e;
            doubleTapDragStart(es.startX, es.startY, es.currentX, es.currentY);
            return true;
        }
        return e.type() == GestureEvent.Type.press;
    }
}
