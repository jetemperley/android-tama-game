package com.game.tama.command

import android.util.Log
import com.game.android.DisplayAdapter
import com.game.tama.core.GameLoop
import com.game.tama.thing.pet.Pet
import com.game.tama.core.World
import com.game.tama.util.Vec2

public class CommandWait(private val waitTimeMs: Float) : Command()
{

    private var timeMs: Float = 0f;

    public override fun start(pet: Pet, world: World)
    {
        super.start(pet, world);
        Log.d(javaClass.canonicalName, "pet waiting $waitTimeMs");
        update(pet, world);
    }

    public override fun doing(pet: Pet, world: World)
    {
        timeMs += GameLoop.deltaTimeMs;
        if (timeMs >= waitTimeMs)
        {
            complete();
        }
    }

    override fun hardCancel()
    {
        super.hardCancel();
    }

    override fun draw(d: DisplayAdapter)
    {

    }
}