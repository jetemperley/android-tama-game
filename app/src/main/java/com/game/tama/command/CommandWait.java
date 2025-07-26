package com.game.tama.command;

import android.util.Log;

import com.game.engine.Time;
import com.game.tama.core.World;
import com.game.tama.thing.pet.Pet;

public class CommandWait extends Command
{

    private final float waitTimeMs;
    private float timeMs = 0f;

    public CommandWait(final float waitTimeMs)
    {
        this.waitTimeMs = waitTimeMs;
    }

    @Override
    public void start(final Pet pet, final World world)
    {
        super.start(pet, world);
        Log.d(getClass().getCanonicalName(), "pet waiting $waitTimeMs");
        update.update(pet, world);
    }

    @Override
    public void doing(final Pet pet, final World world)
    {
        timeMs += Time.deltaTimeMs();
        if (timeMs >= waitTimeMs)
        {
            complete();
        }
    }
}