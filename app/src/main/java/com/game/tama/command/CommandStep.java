package com.game.tama.command;

import android.util.Log;

import com.game.tama.anim.KeyFrameAnim;
import com.game.tama.anim.KeyFrameAssets;
import com.game.engine.DisplayAdapter;
import com.game.engine.GameLoop;
import com.game.tama.core.Direction;
import com.game.tama.thing.pet.Pet;
import com.game.tama.core.World;
import com.game.tama.thing.tile.Tile;
import com.game.tama.util.Vec2;

public class CommandStep extends Command
{
    private Direction dir;
    private KeyFrameAnim anim = KeyFrameAssets.get(KeyFrameAssets.Name.MoveUni);
    private float time = 0f;
    private float stepTime = 1f;

    public CommandStep(Direction dir)
    {
        this.dir = dir;
    }

    @Override
    public void start(Pet pet, World world)
    {
        super.start(pet, world);
        Log.d(getClass().getCanonicalName(), "stepping " + dir.x + " " + dir.y);
        pet.setDir(dir);
        Tile tile = world.getTile(pet.loc.x + dir.x, pet.loc.y + dir.y);
        if (!pet.canMoveOnto(tile))
        {
            fail();
            return;
        }
        pet.setMovementPose(Pet.Movement.walk);
        world.removeThing(pet);
        world.add(pet, pet.loc.x + dir.x, pet.loc.y + dir.y);
        update.accept(pet, world);
    }

    @Override
    public void doing(Pet pet, World world)
    {
        time += GameLoop.deltaTimeS;
        if (time >= stepTime)
        {
            pet.setMovementPose(Pet.Movement.stand);
            pet.loc.setOffset(0, 0);
            complete();
            return;
        }
        Vec2<Float> offset = anim.getPosition(time / stepTime);
        pet.loc.setOffset(offset.x.intValue()*-dir.x, offset.y.intValue()*-dir.y);
    }

    @Override
    public void hardCancel()
    {
        super.hardCancel();
        actor.loc.xoff = 0;
        actor.loc.yoff = 0;
    }
}