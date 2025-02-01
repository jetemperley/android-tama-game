package com.game.tama.state;

import com.game.tama.command.CommandStep;
import com.game.tama.command.CommandWait;
import com.game.engine.GameLoop;
import com.game.tama.core.World;
import com.game.tama.core.Direction;
import com.game.tama.thing.pet.Pet;
import com.game.tama.util.Rand;

import java.util.List;

public class Wander extends StateController
{
    float chancePerSecond = 0.25f;

    @Override
    public void update(Pet pet, World world)
    {
        if (!pet.currentCommand.hasCommand())
        {
            float rand = (float) Math.random();
            float chance = chancePerSecond * GameLoop.deltaTimeS;
            if (rand < chance)
            {
                List<Direction>
                    moves = pet.getPossibleMoves(world, pet.loc.x, pet.loc.y);
                if (moves.isEmpty()) {
                    pet.currentCommand.replace(new CommandWait(4000));
                    return;
                }
                Direction randDir = moves.get(Rand.RandInt(0, moves.size()));
                pet.currentCommand.replace(new CommandStep(randDir));
            }

        }
    }
}