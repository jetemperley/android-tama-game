package com.tama.apptest;

import android.util.Log;

public class PetGame {

    World map;
    Thing held;
    Pet target;

    PetGame(){

        map = new World(10);
        held = null;
        target = new Blob();
        map.add(target, 1, 1);
        map.setTile(2, 2, TileType.water);
        map.setTile(2, 3, TileType.water);
        map.setTile(3, 2, TileType.water);
    }

    void drawEnv(DisplayAdapter d) {
        map.update();
        map.display(d);

    }

    void drawUI(DisplayAdapter d){
        if (held != null)
            d.displayUI(held);

    }

    void press(int x, int y){
        Log.d("game press ", " " +x+ " " + y);
        // held = map.swap(held, x, y);
        // map.target.acts.add(new GoTo(x, y, 0));
        target.acts.add(new GoTo(x, y, 0));
    }

    void longPress(int x, int y){
        Log.d("PetGame longpress", "coord " + x + " " + y);
        if (held != null)
            held = held.apply(map, x, y);
    }

}
