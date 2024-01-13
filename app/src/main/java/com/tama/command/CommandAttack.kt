package com.tama.command

import android.util.Log
import com.tama.anim.KeyFrameAnim
import com.tama.anim.KeyFrameAssets
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

    var targetDirection: Direction?;
    val anim: KeyFrameAnim;

    constructor(target: Thing)
    {
        this.target = target;
        targetHealth = target.getComponent(Health::class.java);
        targetDirection = null;
        anim = KeyFrameAssets.get(KeyFrameAssets.Name.AttackUni)
    }

    public override fun start(pet: Pet, world: World)
    {
        super.start(pet, world)
        if (World.isAdjacent(pet.loc, target.loc))
        {
            fail()
        }
        targetDirection = Direction.from(pet.loc, target.loc)
        doing(pet, world)
    }

    public override fun doing(pet: Pet, world: World)
    {


    }

    override fun hardCancel()
    {
        super.hardCancel()
    }

    override fun draw(d: DisplayAdapter)
    {

    }
}