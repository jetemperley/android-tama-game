package com.tama.command;

import com.tama.core.Tile;

import com.tama.core.Direction;
import com.tama.core.Pet;
import com.tama.core.World;

class CommandStep extends Command
{
    Direction dir;

    public CommandStep(Direction dir)
    {
        this.dir = dir;
    }

    public void start(Pet pet, World world)
    {
        pet.setDir(dir);
        Tile tile = world.getTile(pet.loc.x + dir.x, pet.loc.y + dir.y);
        if (pet.canMoveOnto(tile))
        {
            world.removeThing(pet);
            world.add(pet, pet.loc.x + dir.x, pet.loc.y + dir.y);
            updater = this::doing;
            return;
        }
        state = CommandState.failed;
    }

    public void doing(Pet pet, World world)
    {

    }
}
