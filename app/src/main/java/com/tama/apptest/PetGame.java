package com.tama.apptest;

import android.util.Log;

public class PetGame {

    Map map;
    Thing held;

    PetGame(){

        map = new Map(10);
        held = null;

    }

    void drawEnv(DepthCanvas c) {

        map.canvas = c;
        map.update();
        map.display();

    }

    void drawUI(DepthCanvas c){
        if (held != null)
            held.displayManual(map, 0, 0);

    }

    void press(float x, float y){
        Log.d("game press ", " " +x+ " " + y);
        held = map.swapAp(held, (int)x, (int)y);
        map.target.acts.add(new GoTo((int)x, (int)y, 0));

    }

    void longPress(float x, float y){
        Log.d("PetGame longpress", "coord " + x + " " + y);
        if (held != null)
            held = held.apply(map, (int)x, (int)y);
    }

}
