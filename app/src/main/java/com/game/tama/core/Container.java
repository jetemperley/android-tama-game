package com.game.tama.core;

import com.game.engine.DisplayAdapter;
import com.game.engine.gesture.gestureEvent.Down;
import com.game.engine.gesture.gestureEvent.DragEnd;
import com.game.engine.gesture.gestureEvent.DragStart;
import com.game.engine.gesture.gestureEvent.LongPress;
import com.game.tama.engine.behaviour.GameManager;
import com.game.tama.thing.Thing;
import com.game.tama.util.Bounds;
import com.game.tama.util.Vec2;
import com.game.engine.gesture.gestureEvent.GestureEvent;

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
     * @return
     */
    public boolean isTouchInside(float x, float y)
    {
        return Bounds.isInside(x, y,
            loc.x, loc.y, world.width(), world.height());
    }

    /**
     * Call when the contains inventory has been pressed
     * @param e event with transformed coords
     * @return
     */
    public boolean handleEvent(GestureEvent e)
    {
        if (!isTouchInside(e.x, e.y))
        {
            return false;
        }
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
