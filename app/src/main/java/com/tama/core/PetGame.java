package com.tama.core;

import com.tama.command.CommandFactory;
import com.tama.command.CommandQueue;
import com.tama.thing.Pet;
import com.tama.thing.Thing;
import com.tama.util.Log;
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
    public final static int gameSpeed = 25;

    PetGame()
    {
        Log.log(this, "starting game");
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
            d.displayWorld(
                    Assets.getSprite(Assets.static_inv),
                    selected.loc.getWorldPos().x,
                    selected.loc.getWorldPos().y);
            if (selected instanceof Pet)
            {
                ((Pet)selected).currentCommand.draw(d);
            }
        }
        if (held != null)
        {
            d.displayWorld(
                    held.loc.sprite,
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

    void setHeld(float ax, float ay)
    {
        Thing thing = world.checkCollision(ax, ay);
        thing = world.pickupThing(thing.loc.x, thing.loc.y);
        setHeld(thing);
    }

    void setHeld(Thing thing)
    {
        held = thing;
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
            held = world.pickupThing(t.loc.x, t.loc.y);
            heldPos.set(x, y);
            Vec2<Float> pos = held.loc.getWorldPos();
            heldOffset.x = x - pos.x;
            heldOffset.y = y - pos.y;
        }
    }

    void drop(float x, float y)
    {
        world.addOrClosest(held, (int) x, (int) y);
        held = null;
    }

    void select(float x, float y)
    {
        Thing t = world.checkCollision(x, y);
        if (selected == t)
        {
            selected = null;
        }
        else
        {
            selected = t;
        }
    }

    /**
     * poke the world position x y
     *
     * @param x coord relative to array position
     * @param y coord relative to array position
     */
    void poke(float x, float y)
    {
        Thing t = world.checkCollision(x, y);
        if (t != null)
        {
            t.poke();
        }
    }

    /**
     * Triggers the held object to target position x, y
     *
     * @param ax coord relative to array position
     * @param ay coord relative to array position
     */
    public void doubleSelect(float ax, float ay)
    {
        if (selected == null)
        {
            setHeld(ax, ay);
            return;
        }

        if (!(selected instanceof Pet))
        {
            return;
        }

        Pet pet = (Pet) selected;
        Thing thing = world.checkCollision(ax, ay);
        if (thing == pet)
        {
            pickup(ax, ay);
            Log.log(this, "double tapped the selected pet");
            return;
        }
        if (thing == null)
        {
            CommandQueue walk = CommandFactory.Companion.commandPathTo(
                            (int) ax,
                            (int) ay);
            pet.currentCommand.replace(walk);
            return;
        }
        pet.setActionTarget(thing);
    }
}
