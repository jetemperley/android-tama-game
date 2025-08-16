package com.game.tama.command;

import com.game.engine.KeyFrameAnim;
import com.game.engine.Time;
import com.game.tama.core.Direction;
import com.game.tama.core.KeyFrameAssets;
import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.thing.tile.Tile;
import com.game.tama.core.world.World;
import com.game.tama.util.Log;
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
        Log.log(this, "stepping " + dir.x + " " + dir.y);
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