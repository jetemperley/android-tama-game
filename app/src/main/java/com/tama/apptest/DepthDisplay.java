package com.tama.apptest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class DepthDisplay implements DisplayAdapter {

    DisplayAdapter display;
    PriorityQueue<WorldObject> draws;
    HashMap<WorldObject, Displayable> disps;
    List<Displayable> post;
    List<Float> postX;
    List<Float> postY;


    boolean check = true;

    DepthDisplay() {

        draws = new PriorityQueue<>(200, new DepthComp());
        disps = new HashMap<>();
        post = new ArrayList<>();
        postX = new ArrayList<>();
        postY = new ArrayList<>();
    }

    @Override
    public void displayWorld(WorldObject t, Displayable d){

        draws.add(t);
        disps.put(t, d);
    }
    public void displayUI(Inventory t){

    }
    public void displayManual(Displayable d, float x, float y) {
        post.add(d);
        postX.add(x);
        postY.add(y);
    }

    void drawQ() {
        if (display != null) {
            WorldObject wo;
            while (!draws.isEmpty()) {
                wo = draws.poll();
                display.displayWorld(wo, disps.get(wo));
            }
        }
        for (int i = 0; i < post.size(); i++){
            // Log.d("depth", "draw post");
            display.displayManual(post.get(i), postX.get(i), postY.get(i));
        }
        check = false;
    }

    void clearQ() {

        draws.clear();
        disps.clear();
        post.clear();
        postX.clear();
        postY.clear();
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
