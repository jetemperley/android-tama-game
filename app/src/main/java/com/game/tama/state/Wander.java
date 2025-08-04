package com.game.tama.state;

import com.game.engine.Time;
import com.game.tama.command.CommandStep;
import com.game.tama.command.CommandWait;
import com.game.tama.core.Direction;
import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;
import com.game.tama.util.Rand;

import java.util.List;

public class Wander extends StateController
{
    float chancePerSecond = 0.25f;

    @Override
    public void update(final Pet pet, final World world)
    {
        if (!pet.currentCommand.hasCommand())
        {
            final float rand = (float) Math.random();
            final float chance = chancePerSecond * Time.deltaTimeS();
            if (rand < chance)
            {
                final List<Direction>
                    moves = pet.getPossibleMoves(world, pet.loc.x, pet.loc.y);
                if (moves.isEmpty())
                {
                    pet.currentCommand.replace(new CommandWait(1000));
                    return;
                }
                final Direction randDir = moves.get(Rand.RandInt(0, moves.size()));
                pet.currentCommand.replace(new CommandStep(randDir));
            }
        }
    }
}