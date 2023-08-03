package com.tama.core;

import com.tama.thing.Pet;
import com.tama.thing.Thing;
import com.tama.util.Vec2;

public class PetGame implements java.io.Serializable
{

    private World world;
    private Thing held, selected;
    private Vec2<Float> heldPos, heldOffset;

    /**
     * The amount of ms this game has been running for
     */
    public static long time = 0;
    /**
     * The time that this game should aim to run at (ms)
     */
    public final static long gameSpeed = 25;

    PetGame()
    {
        world = WorldFactory.makeWorld();
        held = null;
        heldPos = new Vec2<>(0f, 0f);
        heldOffset = new Vec2<>(0f, 0f);
    }

    void update()
    {
        world.update();
        time += gameSpeed;
    }

    void drawEnv(DisplayAdapter d)
    {
        world.display(d);
    }

    void drawUI(DisplayAdapter d)
    {
        if (selected != null)
        {
            if (selected instanceof Pet)
            {
                d.uiMode();
                ((Pet)selected).commandQueue.draw(d, selected.loc.sprite);
                d.worldMode();
            }
        }
        if (held != null)
        {
            d.displayWorld(held.loc.sprite,
                           heldPos.x - heldOffset.x,
                           heldPos.y - heldOffset.y);
        }
    }

    void reLoadAllAssets()
    {
        world.reLoadAllAssets();
        if (held != null)
        {
            held.loadAsset();
        }
    }

    void setHeldPosition(float x, float y)
    {
        heldPos.set(x, y);
    }

    void setHeld(int x, int y)
    {
        held = world.takeThing(x, y);
    }

    void setSelected(int x, int y)
    {
        selected = world.getThing(x, y);
    }

    void setSelectedAsHeld()
    {
        if (selected != null)
        {
            setHeld(selected.loc.x, selected.loc.y);
        }
        else
        {
            held = null;
        }
    }

    void pickup(float x, float y)
    {
        if (held != null)
        {
            heldPos.set(x, y);
            return;
        }
        Thing t = world.checkCollision(x, y);
        if (t != null)
        {
            held = world.takeThing(t.loc.x, t.loc.y);
            heldPos.set(x, y);
            Vec2<Float> pos = held.loc.getWorldPos();
            heldOffset.x = x - pos.x;
            heldOffset.y = y - pos.y;
        }
    }

    void drop(float x, float y)
    {
        if (world.getThing((int) x, (int) y) == null)
        {
            world.add(held, (int) x, (int) y);
            held = null;
        }
    }

    void select(float x, float y)
    {
        Thing t = world.checkCollision(x, y);
        selected = t;
    }

    void poke(float x, float y)
    {
        Thing t = world.checkCollision(x, y);
        if (t != null)
        {
            t.poke();
        }
    }

}
