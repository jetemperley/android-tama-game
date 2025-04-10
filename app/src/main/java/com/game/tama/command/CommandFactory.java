package com.game.tama.command;

import android.util.Log;

import com.game.tama.core.Direction;
import com.game.tama.core.World;
import com.game.tama.core.WorldObject;
import com.game.tama.thing.Thing;
import com.game.tama.thing.pet.Pet;
import com.game.tama.util.Path;
import com.game.tama.util.Vec2;

public class CommandFactory
{
    public static CommandQueue commandPathTo(int xTarget,
                                             int yTarget)
    {
        CommandQueue command =
            new CommandQueue(pathInitializer(xTarget, yTarget, 0));
        command.ultimateTarget = new WorldObject(null);
        command.ultimateTarget.setPos(xTarget, yTarget);
        command.failAllOnFail(true);
        return command;
    }

    public static CommandQueue commandWalkAndAdjacentAction(Thing target,
                                                            Command adjacentCommand)
    {
        CommandQueue commandQueue = new CommandQueue();
        commandQueue.failAllOnFail(true);
        CommandQueue walk =
            new CommandQueue(pathInitializer(target.loc, 1));
        walk.failAllOnFail(true);
        commandQueue.add(walk);
        commandQueue.add(adjacentCommand);
        commandQueue.ultimateTarget = target.loc;
        return commandQueue;
    }

    private static PathInit pathInitializer(WorldObject target, int dist)
    {
        return new PathInit()
        {
            @Override
            public void run(CommandQueue queue, World world, Pet pet)
            {
                pathInitializer(target.x, target.y, dist).run(
                    queue,
                    world,
                    pet);
            }
        };
    }

    private static PathInit pathInitializer(int xTarget,
                                     int yTarget,
                                     int dist)
    {
        return new PathInit()
        {

            @Override
            public void run(CommandQueue queue, World world, Pet pet)
            {
                Path path = new Path(dist);
                Vec2<Integer>[] steps =
                    path.findPath(world, pet, pet.loc.x,
                        pet.loc.y, xTarget, yTarget);
                Vec2<Integer> previous = new Vec2<>(pet.loc.x, pet.loc.y);
                for (Vec2<Integer> next : steps)
                {
                    Vec2<Integer> step =
                        new Vec2<>(
                            next.x - previous.x,
                            next.y - previous.y);
                    previous = next;
                    Log.d(getClass().getCanonicalName(), "${step.x} ${step.y}");
                    queue.add(new CommandStep(Direction.from(step)));
                }
            }
        };
    }
}
