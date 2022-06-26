package com.tama.apptest;

import android.util.Log;

import java.util.ArrayList;

interface Act {

    ActState update(World m, Pet p);

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
    public ActState update(World m, Pet p) {
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

    public ActState update(World m, Pet p) {
        if (status == ActState.start){
            Vec2<Integer>[] path = new Path(dist).findPath(m, p.loc.x, p.loc.y, x, y);
            if (path == null) {
                Log.d("Act", "path was null");
                return ActState.failed;
            }

            int xi = p.loc.x, yi = p.loc.y;
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
    public ActState update(World m, Pet p) {
        return ActState.failed;
    }
}

class Poop implements Act {
    public ActState update(World m, Pet p) {
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

    public ActState update(World m, Pet p) {
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

        if (m.canStepOnto(p.loc.x, p.loc.y, p.loc.x + X, p.loc.y + Y)) {
            p.setDir(X, Y);
            m.removeThing(p.loc.x, p.loc.y);
            m.add(p, p.loc.x + X, p.loc.y + Y);
            p.anim.play();
            p.loc.xoff = -X * 100;
            p.loc.yoff = -Y * 100;
            return true;
        }
        return false;
    }

    boolean updateOffsets(Pet p) {
        // println("updating offsets");
        if (p.loc.xoff != 0) {
            if (p.loc.xoff < 0) {
                p.loc.xoff += p.speed;
                if (p.loc.xoff > 0)
                    p.loc.xoff = 0;
            } else {
                p.loc.xoff -= p.speed;
                if (p.loc.xoff < 0)
                    p.loc.xoff = 0;
            }
        } else if (p.loc.yoff != 0) {
            if (p.loc.yoff < 0) {
                p.loc.yoff += p.speed;
                if (p.loc.yoff > 0)
                    p.loc.yoff = 0;
            } else {
                p.loc.yoff -= p.speed;
                if (p.loc.yoff < 0)
                    p.loc.yoff = 0;
            }
        }

        if (p.loc.xoff == 0 && p.loc.yoff == 0) {
            return true;
            // println("step complete");
        }
        return false;
    }
}
