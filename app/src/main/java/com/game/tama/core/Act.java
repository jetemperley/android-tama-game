package com.game.tama.core;

import android.util.Log;

import com.game.engine.Time;
import com.game.tama.core.thing.item.Poop;
import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;
import com.game.tama.util.Path;
import com.game.tama.util.Vec2;

import java.util.ArrayList;

interface Act
{
    ActState update(World m, Pet p);
}

enum ActState
{
    start, doing, complete, failed
}

// completes a sequence of actions
// and fails when the first action fails
class ActSequence implements Act
{
    ArrayList<Act> acts;
    ActState status = ActState.start;

    ActSequence()
    {
        acts = new ArrayList<Act>();
    }

    void add(final Act b)
    {
        acts.add(b);
    }

    public ActState update(final World m, final Pet p)
    {

        if (acts.size() == 0)
        {
            return status = ActState.complete;
        }

        status = acts.get(0).update(m, p);

        if (status == ActState.complete)
        {
            acts.remove(0);
            return status = ActState.doing;
        }
        return status;
    }

}

class Consume implements Act
{
    public ActState update(final World m, final Pet p)
    {
        return ActState.failed;
    }
}

class GoTo implements Act
{

    int x, y;
    int dist;
    ActSequence pathActs;

    GoTo(final int x, final int y, final int dist)
    {
        super();
        this.x = x;
        this.y = y;
        this.dist = dist;
        pathActs = null;
    }

    public ActState update(final World world, final Pet pet)
    {
        if (pathActs == null)
        {
            pathActs = new ActSequence();
            final Vec2<Integer>[] path =
                new Path(dist).findPath(world, pet, pet.loc.x, pet.loc.y, x, y);
            if (path == null)
            {
                Log.d("Act", "path was null");
                return ActState.failed;
            }

            int xi = pet.loc.x, yi = pet.loc.y;
            for (final Vec2<Integer> s : path)
            {
                Log.d("goto path: ", (s.x - xi) + " " + (s.y - yi));
                pathActs.add(new Step(s.x - xi, s.y - yi));
                xi = s.x;
                yi = s.y;
            }
        }
        // Log.d("goto status: ", status + " ");
        return pathActs.update(world, pet);
    }
}

class Pat implements Act
{
    public ActState update(final World m, final Pet p)
    {
        return ActState.failed;
    }
}

class DoPoop implements Act
{
    ActState state;
    Step step;
    long startTime;

    DoPoop()
    {
        startTime = Time.time();
    }

    public ActState update(final World m, final Pet p)
    {

        // make a random Step
        if (step == null)
        {
            final boolean vert = Math.random() < 0.5;
            final int d = Math.random() < 0.5 ? -1 : 1;
            if (vert)
            {
                step = new Step(d, 0);
            }
            else
            {
                step = new Step(0, d);
            }
        }
        final int x = p.loc.x;
        final int y = p.loc.y;

        if (step.state == ActState.start)
        {
            state = step.update(m, p);

            if (state == ActState.doing)
            {
                m.add(new Poop(), x, y);

            }
        }
        else if (state == ActState.doing)
        {
            state = step.update(m, p);

        }
        if (state == ActState.failed)
        {

            final long now = Time.time();
            final long time = startTime - now;
            startTime = now;
            // p.sickness += time;
            step = null;
            state = ActState.doing;

        }

        return state;
    }
}

class Step implements Act
{
    int x, y;
    ActState state;

    Step(final int x, final int y)
    {
        this.x = x;
        this.y = y;
        state = ActState.start;
    }

    public ActState update(final World m, final Pet p)
    {
        // println("updating");
        if (state == ActState.start)
        {
            if (!step(m, p, x, y))
            {
                return (state = ActState.failed);
            }
            return (state = ActState.doing);
        }
        else if (state == ActState.doing)
        {
            if (updateOffsets(p))
            {
                return (state = ActState.complete);
            }
        }
        return state;
    }

    boolean step(final World world, final Pet pet, final int X, final int Y)
    {

        if (pet.canMoveOnto(world.getTile(pet.loc.x + X, pet.loc.y + Y)))
        {
            // p.setDir(X, Y);
            world.removeThing(pet.loc.x, pet.loc.y);
            world.add(pet, pet.loc.x + X, pet.loc.y + Y);
            pet.anim.play();
            pet.loc.xoff = -X * 100;
            pet.loc.yoff = -Y * 100;
            return true;
        }
        return false;
    }

    boolean updateOffsets(final Pet p)
    {
        // println("updating offsets");
        if (p.loc.xoff != 0)
        {
            if (p.loc.xoff < 0)
            {
                p.loc.xoff += p.speed;
                if (p.loc.xoff > 0)
                {
                    p.loc.xoff = 0;
                }
            }
            else
            {
                p.loc.xoff -= p.speed;
                if (p.loc.xoff < 0)
                {
                    p.loc.xoff = 0;
                }
            }
        }
        else if (p.loc.yoff != 0)
        {
            if (p.loc.yoff < 0)
            {
                p.loc.yoff += p.speed;
                if (p.loc.yoff > 0)
                {
                    p.loc.yoff = 0;
                }
            }
            else
            {
                p.loc.yoff -= p.speed;
                if (p.loc.yoff < 0)
                {
                    p.loc.yoff = 0;
                }
            }
        }

        // println("step complete");
        return p.loc.xoff == 0 && p.loc.yoff == 0;
    }
}
