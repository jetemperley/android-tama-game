package com.game.tama.command;

import android.util.Log;

import com.game.engine.Time;
import com.game.tama.anim.KeyFrameAnim;
import com.game.tama.anim.KeyFrameAssets;
import com.game.tama.core.Direction;
import com.game.tama.core.World;
import com.game.tama.thing.pet.Pet;
import com.game.tama.thing.tile.Tile;
import com.game.tama.util.Vec2;

public class CommandStep extends Command
{
    private final Direction dir;
    private final KeyFrameAnim anim = KeyFrameAssets.get(KeyFrameAssets.Name.MoveUni);
    private float time = 0f;
    private final float stepTime = 1f;

    public CommandStep(final Direction dir)
    {
        this.dir = dir;
    }

    @Override
    public void start(final Pet pet, final World world)
    {
        super.start(pet, world);
        Log.d(getClass().getCanonicalName(), "stepping " + dir.x + " " + dir.y);
        pet.setDir(dir);
        final Tile tile = world.getTile(pet.loc.x + dir.x, pet.loc.y + dir.y);
        if (!pet.canMoveOnto(tile))
        {
            fail();
            return;
        }
        pet.setMovementPose(Pet.Movement.walk);
        world.removeThing(pet);
        world.add(pet, pet.loc.x + dir.x, pet.loc.y + dir.y);
        update.update(pet, world);
    }

    @Override
    public void doing(final Pet pet, final World world)
    {
        time += Time.deltaTimeS();
        if (time >= stepTime)
        {
            pet.setMovementPose(Pet.Movement.stand);
            pet.loc.setOffset(0, 0);
            complete();
            return;
        }
        final Vec2<Float> offset = anim.getPosition(time / stepTime);
        pet.loc.setOffset(offset.x.intValue() * -dir.x, offset.y.intValue() * -dir.y);
    }

    @Override
    public void hardCancel()
    {
        super.hardCancel();
        actor.loc.xoff = 0;
        actor.loc.yoff = 0;
    }
}