package com.tama.thing;

import com.tama.command.CommandQueue;
import com.tama.command.State;
import com.tama.command.Wander;
import com.tama.core.Animator;
import com.tama.core.Assets;
import com.tama.core.Displayable;
import com.tama.core.Stats;
import com.tama.core.Type;
import com.tama.core.World;

public abstract class Pet extends Thing
{

    static final int down = 0, right = 1, up = 2, left = 3;
    public Stats stats;

    State state;
    String name;

    // Movement moves;
    CommandQueue commandQueue;
    // this pet animator is the same object as Displayable.sprite;
    public Animator anim;

    public int speed = 3;

    Pet()
    {
        super();
        state = new Wander();
        name = "";
        stats = new Stats();
        commandQueue = new CommandQueue();

        asset = Assets.sheet_16_blob;
        loadAsset();
    }

    Displayable getAsset()
    {
        if (anim == null)
        {
            anim = new Animator(Assets.getSheet(asset));
            anim.play();
            anim.repeat(true);
        }
        else
        {
            anim.sheet = Assets.getSheet(asset);
        }
        return anim;
    }

    public void update(World map)
    {
        stats.updateStats(this);
        commandQueue.getUpdate().invoke(this, map);
        state.update(commandQueue);
    }

    public boolean consume(Thing t)
    {
        //COMPLETE
        return true;
    }

    public Type type()
    {
        return Type.pet;
    }

    public void setDir(Direction dir)
    {
        anim.animID = dir.ordinal();
    }

    public String getDescription()
    {
        return super.getDescription() + "It's a Pet!";
    }

    public boolean canMoveOnto(Tile tile)
    {
        if (tile == null)
            return false;
        return tile.isEmpty();
    }

}

