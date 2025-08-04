package com.game.tama.command;

import android.util.Log;

import com.game.engine.DisplayAdapter;
import com.game.tama.command.lambda.CommandUpdatable;
import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;

import java.io.Serializable;

public abstract class Command implements Serializable
{
    public static final CommandUpdatable noop = (p, w) -> {};

    public CommandUpdatable update = this::start;
    public CommandState state = CommandState.ready;

    protected Pet actor = null;

    protected void start(final Pet pet, final World world)
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

    public void draw(final DisplayAdapter display) {}

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
