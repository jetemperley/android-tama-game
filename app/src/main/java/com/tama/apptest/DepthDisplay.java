package com.tama.apptest;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Comparator;
import java.util.PriorityQueue;

public class DepthDisplay implements DisplayAdapter {

    Canvas canvas;
    PriorityQueue<DepthBitmap> draws;
    boolean check = true;

    DepthDisplay() {
        draws = new PriorityQueue<DepthBitmap>(200, new DBComp());
    }

    public void displayWorld(WorldObject t){

    }
    public void displaySplit(WorldObject t){

    }
    public void displayUI(WorldObject t){

    }
    public void display(Displayable d, int x, int y, int xoff, int yoff){

    }

    void drawBitmap(Bitmap bm, float left, float top, float depth) {
        draws.add(new DepthBitmap(bm, left, top, depth));

    }

    void drawQ() {
        if (canvas != null) {
            DepthBitmap b;
            while (!draws.isEmpty()) {
                b = draws.poll();
                canvas.drawBitmap(b.bm, b.left, b.top, GameActivity.black);
            }
        }
        check = false;
    }

    void clearQ() {
        draws.clear();
    }

    class DepthBitmap {

        Bitmap bm;
        float left;
        float top;
        float depth;

        DepthBitmap(Bitmap bm, float left, float top, float depth) {
            this.bm = bm;
            this.left = left;
            this.top = top;
            this.depth = depth;
        }

    }

    class DBComp implements Comparator<DepthBitmap> {
        public int compare(DepthBitmap a, DepthBitmap b) {
            if (a.depth > b.depth)
                return 1;
            if (a.depth < b.depth)
                return -1;
            return 0;
        }
    }
}
