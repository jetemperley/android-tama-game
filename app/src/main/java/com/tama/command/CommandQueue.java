package com.tama.command;

import com.tama.core.Direction;
import com.tama.core.Pet;
import com.tama.core.World;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A self managing list commands that a pet will execute
 */
public class CommandQueue extends Command
{
    private Queue<Command> commands;

    public CommandQueue()
    {
        commands = new PriorityQueue<>((a, b) -> a.priority - b.priority);
    }

    protected void start(Pet pet, World world)
    {
        updater = this::doing;
    }

    protected void doing(Pet pet, World world)
    {
        Command current = commands.peek();
        if (current == null)
        {
            return;
        }
        current.updater.update(pet, world);
        if (current.state == CommandState.complete || current.state == CommandState.failed)
        {
            commands.poll();
        }
    }


}
