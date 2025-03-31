package com.game.tama.command;

import android.util.Log;

import com.game.engine.DisplayAdapter;
import com.game.tama.core.World;
import com.game.tama.thing.pet.Pet;

import java.io.Serializable;
import java.util.function.BiConsumer;

public abstract class Command implements Serializable
{
    public static final BiConsumer<Pet, World> noop = (p, w) -> {};

    public BiConsumer<Pet, World> update = this::start;
    public CommandState state = CommandState.ready;

    protected Pet actor = null;

    protected void start(Pet pet, World world)
    {
        actor = pet;
        state = CommandState.doing;
        update = this::doing;
    }

    protected abstract void doing(Pet pet, World world);

    public boolean isReplaceable()
    {
        return state == CommandState.ready;
    }

    public void hardCancel()
    {
        state = CommandState.failed;
        update = noop;
        Log.d(getClass().getCanonicalName(), "canceled");
    }

    public void draw(DisplayAdapter display){}

    protected void fail()
    {
        state = CommandState.failed;
        update = noop;
        Log.d(getClass().getCanonicalName(), "failed");
    }

    protected void complete()
    {
        state = CommandState.complete;
        update = noop;
    }
}
