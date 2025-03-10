package com.game.tama.command;

import android.util.Log;
import com.game.engine.DisplayAdapter;
import com.game.engine.GameLoop;
import com.game.tama.thing.pet.Pet;
import com.game.tama.core.World;

public class CommandWait extends Command
{

    private float waitTimeMs;
    private float timeMs = 0f;

    public CommandWait(float waitTimeMs)
    {
        this.waitTimeMs = waitTimeMs;
    }

    @Override
    public void start(Pet pet, World world)
    {
        super.start(pet, world);
        Log.d(getClass().getCanonicalName(), "pet waiting $waitTimeMs");
        update.accept(pet, world);
    }

    @Override
    public void doing(Pet pet, World world)
    {
        timeMs += GameLoop.deltaTimeMs;
        if (timeMs >= waitTimeMs)
        {
            complete();
        }
    }
}