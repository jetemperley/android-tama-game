package com.game.tama.core;

import com.game.engine.DisplayAdapter;
import com.game.engine.gesture.gestureEvent.Down;
import com.game.engine.gesture.gestureEvent.DragEnd;
import com.game.engine.gesture.gestureEvent.DragStart;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.engine.gesture.gestureEvent.LongPress;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.world.World;
import com.game.tama.core.world.WorldFactory;
import com.game.tama.engine.behaviour.GameManager;
import com.game.tama.util.Bounds;
import com.game.tama.util.Vec2;

import java.io.IOException;
import java.io.Serializable;

public class Container extends Thing implements Serializable
{
    private final World world;

    public Container(final int size)
    {
        world = WorldFactory.makeBackpack(size, size);
        asset = AssetName.static_backpack;
        load();
    }

    public void drawContainer(final DisplayAdapter display)
    {
        display.push();
        final Vec2<Float> pos = loc.getWorldBitPos();
        display.getTransform().preTranslate(pos.x, pos.y + 1, 0);
        display.clearRect(
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
     *
     * @param startX x position in parent world arr coords
     * @param startY y position in parent world arr coords
     */
    public void dragStart(final float startX,
                          final float startY)
    {
        final Thing t = world.checkCollision(startX - loc.x, startY - loc.y);
        if (t != null)
        {
            world.pickupThing(t.loc.x, t.loc.y);
            // t.loc.setPos((int)f[0], (int)f[1]);
            GameManager.getHeldBehaviour().setHeld(t, startX, startY);
        }
    }

    /**
     * End drag event
     *
     * @param x x position in parent world arr coords
     * @param y y position in parent world arr coords
     */
    public void dragEnd(final float x, final float y)
    {
        final Thing held = GameManager.getHeldBehaviour().held;
        if (world.addOrClosest(held, (int) (x - loc.x), (int) (y - loc.y)))
        {
            GameManager.getHeldBehaviour().held = null;
            return;
        }
        GameManager.getHeldBehaviour().dropHeld(x, y);
    }

    /**
     * @param x Screen space x
     * @param y Screen space y
     * @return
     */
    public boolean isTouchInside(final float x, final float y)
    {
        return Bounds.isInside(x, y,
                               loc.x, loc.y, world.width(), world.height());
    }

    /**
     * Call when the contains inventory has been pressed
     *
     * @param e event with transformed coords
     * @return
     */
    public boolean handleEvent(final GestureEvent e)
    {
        if (!isTouchInside(e.x, e.y))
        {
            return false;
        }
        final Class clazz = e.getClass();
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

    private void readObject(final java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        world.reLoadAllAssets();
    }
}
