package com.tama.command

import android.util.Log
import com.tama.component.Health
import com.tama.core.DisplayAdapter
import com.tama.thing.Direction
import com.tama.thing.Pet
import com.tama.core.World
import com.tama.core.WorldObject
import com.tama.thing.Thing

public class CommandAttack : Command
{
    val target: Thing;
    val targetHealth: Health?;

    constructor(target: Thing)
    {
        this.target = target;
        targetHealth = target.getComponent(Health::class.java);
    }

    public override fun start(pet: Pet, world: World)
    {
        super.start(pet, world)
        if (World.isAdjacent(pet.loc, target.loc))
        {
            fail()
        }
        // TODO start an animation taking into account duration/speed of action
        doing(pet, world)
    }

    public override fun doing(pet: Pet, world: World)
    {
        // TODO while animation is running, wait
        // TODO target.takeDamage


    }

    override fun hardCancel()
    {
        super.hardCancel()
    }

    override fun draw(d: DisplayAdapter)
    {

    }
}