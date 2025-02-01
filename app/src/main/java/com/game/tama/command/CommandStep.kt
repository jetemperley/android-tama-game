package com.game.tama.command

import android.util.Log
import com.game.tama.anim.KeyFrameAssets
import com.game.android.DisplayAdapter
import com.game.engine.GameLoop
import com.game.tama.core.Direction
import com.game.tama.thing.pet.Pet
import com.game.tama.core.World
import com.game.tama.util.Vec2

public class CommandStep(var dir: Direction) : Command()
{
    val anim = KeyFrameAssets.get(KeyFrameAssets.Name.MoveUni);
    var time: Float = 0f;
    val stepTime: Float = 1f;

    public override fun start(pet: Pet, world: World)
    {
        super.start(pet, world);
        Log.d(javaClass.canonicalName, "stepping " + dir.x + " " + dir.y);
        pet.setDir(dir);
        val tile = world.getTile(pet.loc.x + dir.x, pet.loc.y + dir.y)
        if (!pet.canMoveOnto(tile))
        {
            fail()
            return;
        }
        pet.setMovementPose(Pet.Movement.walk);
        world.removeThing(pet);
        world.add(pet, pet.loc.x + dir.x, pet.loc.y + dir.y);
        update(pet, world);
    }

    public override fun doing(pet: Pet, world: World)
    {
        time += GameLoop.deltaTimeS;
        if (time >= stepTime)
        {
            pet.setMovementPose(Pet.Movement.stand)
            pet.loc.setOffset(0, 0);
            complete();
            return;
        }
        var offset: Vec2<Float> = anim.getPosition(time / stepTime);
        pet.loc.setOffset(offset.x.toInt()*-dir.x, offset.y.toInt()*-dir.y);
    }

    override fun hardCancel()
    {
        super.hardCancel()
        actor?.loc?.xoff = 0;
        actor?.loc?.yoff = 0;
    }

    override fun draw(d: DisplayAdapter)
    {

    }
}