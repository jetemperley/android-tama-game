package com.tama.apptest;

import android.util.Log;

public class PetGame implements java.io.Serializable {

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
            d.displayManual(held.loc.sprite, heldPos.x, heldPos.y);

    }

    void reLoadAllAssets(){
        map.reLoadAllAssets();
        if (held != null)
            held.reLoadAsset();
    }

    void applyHeldTo(int x, int y){
        // Log.d("PetGame", "longpress");
        if (held != null)
            held = held.apply(map, x, y);
    }

    // x and y are distance dragged
    void drag(float x, float y){
        heldPos.x +=x;
        heldPos.y +=y;
    }

    void releaseHeld(int x, int y){
        if (held != null){
            int ax = held.loc.x, ay = held.loc.y;
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

    void select(int x, int y){
        selected = map.getThing(x, y);

    }

    void setSelectedAsHeld(){
        if (selected != null)
            setHeld(selected.loc.x, selected.loc.y);
        else
            held = null;
    }

    void pickup(float x, float y){
        if (held != null) {
            heldPos.set(x, y);
            return;
        }
        Thing t = map.checkCollision(x, y);
        if (t != null){
            held = map.takeThing(t.loc.x, t.loc.y);
            // held.pickedUp();
            setHeldPosition(x, y);
        }
    }

    void dropHeld(float x, float y){

        if (map.getThing((int)x, (int)y) == null){
            map.add(held, (int)x, (int)y);
            held = null;
        }
    }

    void select(float x, float y){
        Thing t = map.checkCollision(x, y);
        selected = t;
    }

    // sets the held object to x,y and returns true if sucessful
    boolean setHeld(float x, float y){
        Thing t = map.checkCollision(x, y);
        held = t;
        return t != null;
    }



}
