package com.tama.apptest;

import android.util.Log;

public class PetGame {

    World map;
    Thing held, selected;
    Pet target;
    Inventory inv;
    Vec2<Float> heldPos;

    PetGame(){

        map = new World(10);
        held = null;
        target = new Blob();
        map.add(new Poo(), 9, 9);
        map.add(target, 1, 1);
        map.setTile(2, 2, TileType.water);
        map.setTile(2, 3, TileType.water);
        map.setTile(3, 2, TileType.water);

        for (int x = 4; x < 8; x++){
            for (int y = 4; y < 8; y++){
                map.setTile(x, y, new LongGrass());

            }
        }
        inv = new Inventory();
        heldPos = new Vec2<>(0f, 0f);
    }

    void drawEnv(DisplayAdapter d) {
        map.update();
        map.display(d);

    }

    void drawUI(DisplayAdapter d){

        // inv.display(d);
        if (held != null)
            d.displayManual(held.sprite, heldPos.x, heldPos.y);


    }

    // the tap location in game coords
    void singlePress(float x, float y){
        Thing t = map.checkCollision(x, y);
        if (t == null){
            singlePress((int)x, (int)y);
        }
    }

    // the tap in array coords
    void singlePress(int x, int y){
        // Log.d("game press ", " " +x+ " " + y);
        //  target.acts.add(new GoTo(x, y, 0));
        setSelected(x, y);
    }

    void longPress(int x, int y){
        // Log.d("PetGame", "longpress");
        if (held != null)
            held = held.apply(map, x, y);
    }

    void doublePress(int x, int y){

    }

    // x and y are distance dragged
    void drag(float x, float y){
        heldPos.x +=x;
        heldPos.y +=y;
    }

    void releaseHeld(int x, int y){
        if (held != null){
            int ax = held.x(), ay = held.y();
            held = map.swap(held, x, y);
            map.add(held, ax, ay);
            held = null;
        }
    }

    void setHeldPosition(float x, float y){
        if (held != null){
            heldPos.x = x;
            heldPos.y = y;
        }
    }

    void setHeld(int x, int y){
        held = map.takeThing(x, y);

    }

    void setSelected(int x, int y){
        selected = map.getThing(x, y);

    }

    void setSelectedAsHeld(){
        if (selected != null)
            setHeld(selected.x(), selected.y());
        else
            held = null;
    }



}
