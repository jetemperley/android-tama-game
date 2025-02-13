package com.game.tama.command;

import com.game.engine.GameLoop;
import com.game.tama.anim.KeyFrameAnim;
import com.game.tama.anim.KeyFrameAssets;
import com.game.tama.core.Direction;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;
import com.game.tama.thing.component.Health;
import com.game.tama.thing.pet.Pet;
import com.game.tama.util.Vec2;

public class CommandAttack extends Command
{
    private final Thing target;
    private Health targetHealth;

    private Direction targetDirection;
    private KeyFrameAnim anim;
    private float time = 0;
    private final float attackTime = 0.4f;

    public CommandAttack(Thing target)
    {
        this.target = target;
        this.targetHealth = target.getComponent(Health.class);
        this.targetDirection = null;
        this.anim = KeyFrameAssets.get(KeyFrameAssets.Name.AttackUni);
    }

    @Override
    public void start(Pet pet, World world)
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

    @Override
    public void doing(Pet pet, World world)
    {

        time += GameLoop.deltaTimeS;
        if (time >= attackTime)
        {
            // TODO do damage or something
            pet.loc.setOffset(0, 0);
            complete();
            return;
        }
        Vec2<Float> off = anim.getPosition(time / attackTime);
        pet.loc.setOffset(
            (int) (off.x * targetDirection.x),
            (int) (off.y * targetDirection.y));
    }
}
