package com.tama.apptest;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Comparator;
import java.util.PriorityQueue;

public class DepthDisplay implements DisplayAdapter {

    DisplayAdapter display;
    PriorityQueue<WorldObject> draws;
    boolean check = true;

    DepthDisplay() {
        draws = new PriorityQueue<>(200, new DepthComp());
    }

    public void displayWorld(WorldObject t){
        draws.add(t);
    }
    public void displayUI(Thing t){

    }
    public void displayManual(Displayable d, float x, float y) {

    }

    void drawQ() {
        if (display != null) {
            WorldObject b;
            while (!draws.isEmpty()) {
                b = draws.poll();
                display.displayWorld(b);
            }
        }
        check = false;
    }

    void clearQ() {
        draws.clear();
    }

    class DepthComp implements Comparator<WorldObject> {
        public int compare(WorldObject a, WorldObject b) {
            if (b.flat && a.flat)
                return 0;
            if (a.flat)
                return -1;
            if (b.flat)
                return 1;
            return (a.y + a.yoff/100f) > (b.y + b.yoff/100f) ? 1 : -1;
        }
    }

}
