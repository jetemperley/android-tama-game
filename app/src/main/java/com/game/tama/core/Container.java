package com.game.tama.core;

import android.graphics.Matrix;

import com.game.engine.DisplayAdapter;
import com.game.android.gesture.Down;
import com.game.android.gesture.DragEnd;
import com.game.android.gesture.DragStart;
import com.game.android.gesture.LongPress;
import com.game.engine.Transform;
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
        display.getTransform().preTranslate(pos.x, pos.y+1, 0);
        display.drawRect(
            0,
            0,
            world.celln,
            world.celln);

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

    /**
     * Start drag event
     * @param startX x position in parent world arr coords
     * @param startY y position in parent world arr coords
     */
    public void dragStart(float startX,
                          float startY)
    {
        Thing t = world.checkCollision(startX - loc.x, startY - loc.y);
        if (t != null)
        {
            world.pickupThing(t.loc.x, t.loc.y);
            // t.loc.setPos((int)f[0], (int)f[1]);
            GameManager.getHeld().setHeld(t, startX, startY);
        }
    }

    /**
     * End drag event
     * @param x x position in parent world arr coords
     * @param y y position in parent world arr coords
     */
    public void dragEnd(float x, float y)
    {
        Thing held = GameManager.getHeld().held;
        if (world.addOrClosest(held, (int) (x - loc.x), (int) (y - loc.y)))
        {
            GameManager.getHeld().held = null;
            return;
        }
        GameManager.getHeld().dropHeld(x, y);
    }

    /**
     * @param x Screen space x
     * @param y Screen space y
     * @param transform the transform of the parent world
     * @return
     */
    public boolean isTouchInside(float x, float y, Transform transform)
    {
        Vec2<Float> locv = loc.getWorldBitPos();

        // the position of this containers display element in the world
        float[] position = transform.mapPoint(locv.x, locv.y + 1, 0);
        // the size of this container is the size of its internal world
        float[] size = transform.mapPoint(world.celln, world.celln, 0);

        return Bounds.isInside(x, y,
            position[0],
            position[1],
            size[0],
            size[1]);
    }

    /**
     * Call when the contains inventory has been pressed
     * @param e
     * @param mat
     * @return
     */
    public boolean handleEvent(GestureEvent e, Transform mat)
    {
        if (!isTouchInside(e.x, e.y, mat))
        {
            return false;
        }
        Log.log(this, "Touch was inside.");
        Class clazz = e.getClass();
        if (clazz == DragEnd.class)
        {
            dragEnd(e.x, e.y);
            return true;
        }
        else if (clazz == DragStart.class)
        {
            dragStart(e.x, e.y);
            return true;
        }
        return clazz == Down.class || clazz == LongPress.class;
    }

    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        world.reLoadAllAssets();
    }
}
