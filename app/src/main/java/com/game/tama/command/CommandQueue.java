package com.game.tama.command;

import android.util.Log;

import com.game.engine.DisplayAdapter;
import com.game.tama.thing.pet.Pet;
import com.game.tama.core.World;
import com.game.tama.core.WorldObject;
import com.game.tama.util.Vec2;

import java.util.*;

/**
 * A self managing list commands that a pet will execute one at a time,
 * regardless of command failures unless failAllOnFail is set to true
 */
public class CommandQueue extends Command
{
    private final Queue<Command> queue;
    private Runnable failAction = () ->
    {
    };
    private PathInit initializer = null;
    public WorldObject ultimateTarget = null;

    public CommandQueue()
    {
        this((CommandQueue queue, World world, Pet pet) ->
        {
            Log.d("Command queue", "default initiliser");
        });
    }

    public CommandQueue(PathInit initializer)
    {
        super();
        this.initializer = initializer;
        update = this::start;
        queue = new LinkedList<>();
        failAction = () ->
        {queue.poll();};
    }

    @Override
    public void start(Pet pet, World world)
    {
        Log.d(this.getClass().getCanonicalName(), "starting queue");
        super.start(pet, world);
        initializer.run(this, world, pet);
    }

    @Override
    public void doing(Pet pet, World world)
    {
        Command current = queue.peek();
        if (current == null)
        {
            state = CommandState.complete;
            return;
        }
        current.update.accept(pet, world);
        switch (current.state)
        {
            case complete:
                queue.poll();
                break;
            case failed:
            {
                Log.d(
                    this.getClass().getCanonicalName(),
                    "failed queue element");
                failAction.run();
                break;
            }
        }
    }

    public void failAllOnFail(boolean failAll)
    {
        if (failAll)
        {
            failAction = () ->
            {
                state = CommandState.failed;
            };
        }
        else
        {
            failAction = () ->
            {
                queue.poll();
            };
        }
    }

    public int length()
    {
        return queue.size();
    }

    public void add(Command command)
    {
        queue.add(command);
    }

    @Override
    public void draw(DisplayAdapter d)
    {
        if (actor == null || ultimateTarget == null)
        {
            return;
        }
        Vec2<Float> start = actor.loc.getWorldArrPos();
        start.x += 1;
        start.y += 1;
        Vec2<Float> end = ultimateTarget.getWorldArrPos();
        end.x += 1;
        end.y += 1;
        d.drawLine(
            (start.x - 0.5f),
            (start.y - 0.5f),
            (end.x - 0.5f),
            (end.y - 0.5f));
    }

    @Override
    public boolean isReplaceable()
    {
        Command current = queue.peek();
        return current == null || current.isReplaceable();
    }

    @Override
    public void hardCancel()
    {
        Command command = queue.peek();
        command.hardCancel();
        super.hardCancel();
    }
}