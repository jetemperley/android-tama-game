package com.game.tama.core;

import android.util.Log;

import com.game.tama.behaviour.GameManager;
import com.game.tama.util.Path;
import com.game.tama.util.Vec2;
import com.game.tama.thing.Pet;
import com.game.tama.thing.Poop;

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

    void add(Act b)
    {
        acts.add(b);
    }

    public ActState update(World m, Pet p)
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
    public ActState update(World m, Pet p)
    {
        return ActState.failed;
    }
}

class GoTo implements Act
{

    int x, y;
    int dist;
    ActSequence pathActs;

    GoTo(int x, int y, int dist)
    {
        super();
        this.x = x;
        this.y = y;
        this.dist = dist;
        pathActs = null;
    }

    public ActState update(World world, Pet pet)
    {
        if (pathActs == null)
        {
            pathActs = new ActSequence();
            Vec2<Integer>[] path = new Path(dist).findPath(world, pet, pet.loc.x, pet.loc.y, x, y);
            if (path == null)
            {
                Log.d("Act", "path was null");
                return ActState.failed;
            }

            int xi = pet.loc.x, yi = pet.loc.y;
            for (Vec2<Integer> s : path)
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
    public ActState update(World m, Pet p)
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
        startTime = GameManager.time;
    }

    public ActState update(World m, Pet p)
    {

        // make a random Step
        if (step == null)
        {
            boolean vert = Math.random() < 0.5;
            int d = Math.random() < 0.5 ? -1 : 1;
            if (vert)
            {
                step = new Step(d, 0);
            }
            else
            {
                step = new Step(0, d);
            }
        }
        int x = p.loc.x;
        int y = p.loc.y;

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

            long now = GameManager.time;
            long time = startTime - now;
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

    Step(int x, int y)
    {
        this.x = x;
        this.y = y;
        state = ActState.start;
    }

    public ActState update(World m, Pet p)
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

    boolean step(World world, Pet pet, int X, int Y)
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

    boolean updateOffsets(Pet p)
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

        if (p.loc.xoff == 0 && p.loc.yoff == 0)
        {
            return true;
            // println("step complete");
        }
        return false;
    }
}
