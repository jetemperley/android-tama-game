package com.game.tama.command;

import com.game.engine.KeyFrameAnim;
import com.game.engine.Time;
import com.game.tama.core.Direction;
import com.game.tama.core.KeyFrameAssets;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.thing.component.Health;
import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;
import com.game.tama.util.Vec2;

public class CommandAttack extends Command
{
    private final Thing target;
    private final Health targetHealth;

    private Direction targetDirection;
    private final KeyFrameAnim anim;
    private float time = 0;
    private final float attackTime = 0.4f;

    public CommandAttack(final Thing target)
    {
        this.target = target;
        this.targetHealth = target.getComponent(Health.class);
        this.targetDirection = null;
        this.anim = KeyFrameAssets.get(KeyFrameAssets.Name.AttackUni);
    }

    @Override
    public void start(final Pet pet, final World world)
    {
        super.start(pet, world);
        if (!World.isAdjacent(pet.loc, target.loc))
        {
            fail();
            return;
        }
        targetDirection = Direction.from(pet.loc, target.loc);
        pet.look(targetDirection);
        doing(pet, world);
    }

    @Override
    public void doing(final Pet pet, final World world)
    {

        time += Time.deltaTimeS();
        if (time >= attackTime)
        {
            // TODO do damage or something
            pet.loc.setOffset(0, 0);
            complete();
            return;
        }
        final Vec2<Float> off = anim.getPosition(time / attackTime);
        pet.loc.setOffset(
            (int) (off.x * targetDirection.x),
            (int) (off.y * targetDirection.y));
    }
}
