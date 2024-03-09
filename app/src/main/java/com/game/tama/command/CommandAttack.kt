package com.game.tama.command

import com.game.tama.anim.KeyFrameAnim
import com.game.tama.anim.KeyFrameAssets
import com.game.tama.component.Health
import com.game.android.DisplayAdapter
import com.game.tama.core.GameLoop
import com.game.tama.thing.Direction
import com.game.tama.thing.Pet
import com.game.tama.core.World
import com.game.tama.thing.Thing
import com.game.tama.util.Vec2

public class CommandAttack : Command
{
    val target: Thing;
    val targetHealth: Health?;

    var targetDirection: Direction?;
    val anim: KeyFrameAnim;
    var time: Float = 0f;
    val attackTime: Float = 0.4f;

    constructor(target: Thing)
    {
        this.target = target;
        targetHealth = target.getComponent(Health::class.java);
        targetDirection = null;
        anim = KeyFrameAssets.get(KeyFrameAssets.Name.AttackUni)
    }

    public override fun start(pet: Pet, world: World)
    {
        super.start(pet, world);
        if (!World.isAdjacent(pet.loc, target.loc))
        {
            fail();
            return;
        }
        targetDirection = Direction.from(pet.loc, target.loc);
        pet.setDir(targetDirection);
        doing(pet, world);
    }

    public override fun doing(pet: Pet, world: World)
    {

        time += GameLoop.deltaTime
        if (time >= attackTime)
        {
            pet.loc.setOffset(0, 0)
            complete();
            return;
        }
        val off: Vec2<Float> = anim.getPosition(time / attackTime);
        pet.loc.setOffset((off.x * targetDirection!!.x).toInt(),
                          (off.y * targetDirection!!.y).toInt());
    }

    override fun hardCancel()
    {
        super.hardCancel()
    }

    override fun draw(d: DisplayAdapter)
    {

    }
}