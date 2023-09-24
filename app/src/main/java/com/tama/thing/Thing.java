package com.tama.thing;

import com.tama.core.Assets;
import com.tama.core.DisplayAdapter;
import com.tama.core.Displayable;
import com.tama.core.Type;
import com.tama.core.World;
import com.tama.core.WorldObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Thing implements java.io.Serializable
{
    @NotNull
    final public WorldObject loc;
    protected String asset = Assets.static_poop;
    public final List<Thing> secondaryThings;

    public Thing()
    {
        loc = new WorldObject(Assets.getSprite(asset));
        loc.sprite = getAsset();
        secondaryThings = new ArrayList<>(1);
    }

    public void display(DisplayAdapter d)
    {
        d.displayWorld(loc);
    }

    Displayable getAsset()
    {
        return Assets.getSprite(asset);
    }

    public void loadAsset()
    {
        loc.sprite = getAsset();
    }

    public void update(World map)
    {

    }


    public boolean canSwim()
    {
        return false;
    }

    public boolean canWalk()
    {
        return true;
    }

    /**
     * Checks whether the point x,y is inside the bounding box for this object
     * @param x
     * @param y
     * @return
     */
    public boolean contains(float x, float y)
    {
        if (x > loc.x + (loc.xoff / 100f) && x < loc.x + (loc.xoff / 100f) + 1 &&
                y > loc.y + (loc.yoff / 100f) && y < loc.y + (loc.yoff / 100f) + 1)
        {
            return true;
        }
        return false;
    }

    Type type()
    {
        return Type.undefined;
    }

    Thing apply(World m, int x, int y)
    {
        return this;
    }

    public void poke()
    {
    }

    public String getDescription()
    {
        return "Loc: " + loc.x + ", " + loc.y + ". ";
    }

    public Thing pickup()
    {
        return this;
    }
}


