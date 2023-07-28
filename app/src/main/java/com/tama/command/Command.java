package com.tama.command;

import com.tama.core.Pet;
import com.tama.core.World;

/**
 * An abtraction for any single action that a pet can execute
 */
public abstract class Command
{
    interface Update
    {
        void update(Pet pet, World world);
    }
    public Update updater = this::start;

    public int priority = 0;
    public CommandState state = CommandState.doing;

    protected abstract void start(Pet pet, World world);
    protected abstract void doing(Pet pet, World world);
}


