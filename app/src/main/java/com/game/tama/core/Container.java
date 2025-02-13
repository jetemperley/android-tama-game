package com.game.tama.core;

import android.graphics.Matrix;

import com.game.engine.DisplayAdapter;
import com.game.android.gesture.Down;
import com.game.android.gesture.DragEnd;
import com.game.android.gesture.DragStart;
import com.game.android.gesture.LongPress;
import com.game.tama.engine.behaviour.GameManager;
import com.game.tama.thing.Thing;
import com.game.tama.util.Bounds;
import com.game.tama.util.MatrixUtil;
import com.game.tama.util.Vec2;
import com.game.android.gesture.GestureEvent;
import com.game.tama.util.Log;

import java.io.IOException;
import java.io.Serializable;

public class Container extends Thing implements Serializable
{
    private World world;
    private transient Matrix mat = new Matrix();

    public Container(int size)
    {
        world = WorldFactory.makeBackpack(size, size);
        asset = AssetName.static_backpack;
        load();
    }

    public void drawContainer(DisplayAdapter display)
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
        GameManager.INST.gameBehaviour.containerManager.toggle(this);
    }

    @Override
    public void use()
    {

    }

    public void dragStart(float startX,
                          float startY, Matrix mat)
    {
        float[] f = MatrixUtil.convertScreenToWorldArray(
            mat, startX, startY);
        float invTapX = f[0] - loc.x;
        float invTapY = f[1] - loc.y - 1;
        Thing t = world.checkCollision(invTapX, invTapY);
        if (t != null)
        {
            world.pickupThing(t.loc.x, t.loc.y);
            t.loc.setPos((int)f[0], (int)f[1]);
            GameManager.getHeld().setHeld(t, f[0], f[1]);
        }
    }

    public void dragEnd(float x, float y, Matrix mat)
    {
        float[] arrPos = MatrixUtil.convertScreenToWorldArray(mat, x, y);
        arrPos[0] -= loc.x;
        arrPos[1] -= loc.y +1;
        Log.log(this, "doubleTapDragEnd drop loc " + arrPos[0] + " " + arrPos[1]);
        Thing held = GameManager.getHeld().held;
        if (world.addOrClosest(held, (int) arrPos[0], (int) arrPos[1]))
        {
            GameManager.getHeld().held = null;
            return;
        }
        GameManager.getHeld().dropHeld(x, y);
    }

    /**
     * @param x Screen space x
     * @param y Screen space y
     * @return
     */
    public boolean isTouchInside(float x, float y, Matrix mat)
    {
        mat.setTranslate(0, 16);
        mat.preConcat(mat);
        Vec2<Float> locv = loc.getWorldBitPos();
        float[] pos = {locv.x, locv.y+16};
        float[] size = {world.celln*16, world.celln*16};
        mat.mapPoints(pos);
        mat.mapVectors(size);

        return Bounds.isInside(x, y,
            pos[0],
            pos[1],
            size[0],
            size[1]);
    }

    /**
     * Call when the containes inventory has been pressed
     * @param e
     * @param mat
     * @return
     */
    public boolean handleEvent(GestureEvent e, Matrix mat)
    {
        if (!isTouchInside(e.x, e.y, mat))
        {
            return false;
        }
        Log.log(this, "Touch was inside.");
        Class clazz = e.getClass();
        if (clazz == DragEnd.class)
        {
            dragEnd(e.x, e.y, mat);
            return true;
        }
        else if (clazz == DragStart.class)
        {
            dragStart(e.x, e.y, mat);
            return true;
        }
        return clazz == Down.class || clazz == LongPress.class;
    }

    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        mat = new Matrix();
        world.reLoadAllAssets();
    }
}
