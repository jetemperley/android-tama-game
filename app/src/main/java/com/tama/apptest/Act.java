package com.tama.apptest;

import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;

interface Act {

    ActState update(World m, Thing t);

}

enum ActState{
    start, doing, complete, failed
}

abstract class ActSequence implements Act {
    ArrayList<Act> acts;
    ActState status = ActState.start;
    ActSequence() {
        acts = new ArrayList<Act>();
    }

    void addAct(Act b) {
        acts.add(b);
    }

    public ActState update(World m, Pet p) {

        if (acts.size() == 0)
            return status = ActState.complete;

        status = acts.get(0).update(m, p);

        if (status == ActState.complete) {
            acts.remove(0);
            return status = ActState.doing;
        }
        return status;
    }

}

class Consume implements Act {
    public ActState update(World m, Thing t) {
        return ActState.failed;
    }
}

class GoTo extends ActSequence {
    int x, y;
    int dist;

    GoTo(int x, int y, int dist) {
        super();
        this.x = x;
        this.y = y;
        this.dist = dist;
    }

    public ActState update(World m, Thing t) {
        Pet p = (Pet)t;
        if (status == ActState.start){
            Vec2<Integer>[] path = new Path(dist).findPath(m, p.wo.x, p.wo.y, x, y);
            if (path == null) {
                Log.d("Act", "path was null");
                return ActState.failed;
            }

            int xi = p.wo.x, yi = p.wo.y;
            for (Vec2<Integer> s : path){
                // Log.d("goto path: ", (s.x - xi) + " " + (s.y - yi));
                acts.add(new Step(s.x - xi, s.y - yi));
                xi = s.x;
                yi = s.y;
            }
        }
        status = super.update(m, p);
        // Log.d("goto status: ", status + " ");
        return (status);
    }
}

class Pat implements Act {
    public ActState update(World m, Thing t) {
        return ActState.failed;
    }
}

class Poop implements Act {
    public ActState update(World m, Thing t) {
        return ActState.failed;
    }
}

class Step implements Act {
    int x, y;
    ActState status;

    Step(int x, int y) {
        this.x = x;
        this.y = y;
        status = ActState.start;
    }

    public ActState update(World m, Thing t) {
        Pet p = (Pet)t;
        // println("updating");
        if (status == ActState.start) {
            if (!step(m, p, x, y)) {
                return (status = ActState.failed);
            }
            return (status = ActState.doing);
        } else if (status == ActState.doing) {
            if (updateOffsets(p))
                return (status = ActState.complete);
        }
        return status;
    }

    boolean step(World m, Pet p, int X, int Y) {

        if (m.canStepOnto(p.wo.x, p.wo.y, p.wo.x + X, p.wo.y + Y)) {
            p.setDir(X, Y);
            m.removeThing(p.wo.x, p.wo.y);
            m.put(p, p.wo.x + X, p.wo.y + Y);
            p.anim.play();
            p.wo.xoff = -X * 100;
            p.wo.yoff = -Y * 100;
            return true;
        }
        return false;
    }

    boolean updateOffsets(Pet p) {
        // println("updating offsets");
        if (p.wo.xoff != 0) {
            if (p.wo.xoff < 0) {
                p.wo.xoff += p.speed;
                if (p.wo.xoff > 0)
                    p.wo.xoff = 0;
            } else {
                p.wo.xoff -= p.speed;
                if (p.wo.xoff < 0)
                    p.wo.xoff = 0;
            }
        } else if (p.wo.yoff != 0) {
            if (p.wo.yoff < 0) {
                p.wo.yoff += p.speed;
                if (p.wo.yoff > 0)
                    p.wo.yoff = 0;
            } else {
                p.wo.yoff -= p.speed;
                if (p.wo.yoff < 0)
                    p.wo.yoff = 0;
            }
        }

        if (p.wo.xoff == 0 && p.wo.yoff == 0) {
            return true;
            // println("step complete");
        }
        return false;
    }

}

class Panic implements Act{

    int switchTime = 200;
    int time = 0;
    ActState state = ActState.start;


    public ActState update(World m, Thing t){
        Pet p = (Pet)t;
        if (state == ActState.start) {
            p.anim.animIDX = Pet.right;
            state = ActState.doing;
            p.wo.xoff = 0;
            p.wo.yoff = 0;
        }

        time += 25;

        if (time >= switchTime) {
            time = 0;

            if (p.anim.animIDX == Pet.right)
                p.anim.animIDX = Pet.left;
            else
                p.anim.animIDX = Pet.right;
        }
        return ActState.doing;
    }

}

class Nudge implements Act{

    private ActState state = ActState.complete;
    private int shakeTime = 100;
    private int time = 0;
    private float[] off = new float[] {0, 0};
    final static private int maxDist = 15, minDist = 5;

    Matrix mat = new Matrix();
    @Override
    public ActState update(World w, Thing t) {

        switch (state){

            case start:{
                mat.reset();
                mat.postRotate(Rand.RandInt(0, 360));
                off[0] = Rand.RandInt(minDist, maxDist);
                mat.mapPoints(off);
                t.wo.xoff += off[0];
                t.wo.yoff += off[1];
                state = ActState.doing;
            }
            break;

            case doing:{
                time += 25;
                if (time >shakeTime){
                    state = ActState.complete;
                    t.wo.xoff -= off[0];
                    t.wo.yoff -= off[1];
                }
            }
            break;

            case complete:{

            }
            break;

        }

        return state;

    }

    public void start(Thing t){
        if (state == ActState.doing){
            t.wo.xoff -= off[0];
            t.wo.yoff -= off[1];
        }

        time = 0;
        state = ActState.start;

    }
}
