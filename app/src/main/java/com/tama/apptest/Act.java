package com.tama.apptest;

import android.util.Log;

import java.util.ArrayList;

interface Act {
    static byte START = 0, DOING = START+1, COMPLETE = DOING + 1, FAILED = COMPLETE + 1;
    byte update(Map m, Pet p);

}

abstract class BSequence implements Act {
    ArrayList<Act> acts;
    byte status = Act.START;
    BSequence() {
        acts = new ArrayList<Act>();
    }

    void addAct(Act b) {
        acts.add(b);
    }

    public byte update(Map m, Pet p) {

        if (acts.size() == 0)
            return status = Act.COMPLETE;

        status = acts.get(0).update(m, p);

        if (status == Act.COMPLETE) {
            acts.remove(0);
            return status = Act.DOING;
        }
        return status;
    }

}

class Consume implements Act {
    public byte update(Map m, Pet p) {
        return Act.FAILED;
    }
}

class GoTo extends BSequence {
    int x, y;
    int nextTo;

    GoTo(int x, int y, int nextTo) {
        super();
        this.x = x;
        this.y = y;
        this.nextTo = nextTo;
    }

    public byte update(Map m, Pet p) {
        if (status == Act.START){
            Vec2[] path = new Path(nextTo).findPath(m, p.x, p.y, x, y);
            if (path == null)
                return Act.FAILED;

            int xi = p.x, yi = p.y;
            for (Vec2 s : path){
                Log.d("goto path: ", (s.x - xi) + " " + (s.y - yi));
                acts.add(new Step(s.x - xi, s.y - yi));
                xi = s.x;
                yi = s.y;
            }
        }
        status = super.update(m, p);
        Log.d("goto status: ", status + " ");
        return (status);
    }
}

class Pat implements Act {
    public byte update(Map m, Pet p) {
        return Act.FAILED;
    }
}

class Poop implements Act {
    public byte update(Map m, Pet p) {
        return Act.FAILED;
    }
}

class Step implements Act {
    int x, y;
    byte status;

    Step(int x, int y) {
        this.x = x;
        this.y = y;
        status = Act.START;
    }

    public byte update(Map m, Pet p) {
        // println("updating");
        if (status == Act.START) {
            if (!step(m, p, x, y)) {
                return (status = Act.FAILED);
            }
            return (status = Act.DOING);
        } else if (status == Act.DOING) {
            if (updateOffsets(p))
                return (status = Act.COMPLETE);
        }
        return status;
    }

    boolean step(Map m, Pet p, int X, int Y) {

        if (m.canStepOnto(p.x, p.y, p.x + X, p.y + Y)) {
            p.setDir(X, Y);
            m.removeThing(p.x, p.y);
            m.add(p, p.x + X, p.y + Y);
            p.anim.play();
            p.xoff = -X * 100;
            p.yoff = -Y * 100;
            return true;
        }
        return false;
    }

    boolean updateOffsets(Pet p) {
        // println("updating offsets");
        if (p.xoff != 0) {
            if (p.xoff < 0) {
                p.xoff += p.speed;
                if (p.xoff > 0)
                    p.xoff = 0;
            } else {
                p.xoff -= p.speed;
                if (p.xoff < 0)
                    p.xoff = 0;
            }
        } else if (p.yoff != 0) {
            if (p.yoff < 0) {
                p.yoff += p.speed;
                if (p.yoff > 0)
                    p.yoff = 0;
            } else {
                p.yoff -= p.speed;
                if (p.yoff < 0)
                    p.yoff = 0;
            }
        }

        if (p.xoff == 0 && p.yoff == 0) {
            return true;
            // println("step complete");
        }
        return false;
    }
}
