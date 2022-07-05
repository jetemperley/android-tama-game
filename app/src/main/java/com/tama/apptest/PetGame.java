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
        map.put(new Poo(), 9, 9);
        map.put(new Rock(), 0, 0);
        map.put(new Rock(), 0, 1);
        map.put(new Rock(), 1, 0);
        map.put(new Rock(), 2, 5);

        map.put(target, 1, 1);
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
        if (held != null)
            held.update(map);
        map.display(d);

    }

    void drawUI(DisplayAdapter d){

        // inv.display(d);
        if (held != null)
            d.displayManual(held.vis, heldPos.x, heldPos.y);

    }

    void loadAllAssets(){
        map.loadAllAssets();
        if (held != null)
            held.loadAsset();
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

    void setHeldPosition(float x, float y){
        if (held != null){
            heldPos.x = x;
            heldPos.y = y;
        }
    }

    void select(int x, int y){
        selected = map.getThing(x, y);

    }


    void dropHeld(float x, float y){
        if (held == null)
            return;
        map.put(held, (int)x, (int)y);
        held.onDrop();
        held = null;

    }

    void setSelected(float x, float y){
        Thing t = map.checkCollision(x, y);
        selected = t;
    }

    boolean pickup(float x, float y){
        if (held != null) {
            heldPos.set(x, y);
            return true;
        }
        Thing t = map.checkCollision(x, y);
        if (t == null)
            return false;
        held = map.takeThing(t.wo.x, t.wo.y);
        if (held == null)
            return false;
        held.onPickup();
        heldPos.set(x*16 -8, y*16 -8);
        return true;
    }

    void poke(float x, float y){
        Thing t = map.checkCollision(x, y);
        if (t != null) {
            t.poke(map);
        }
    }

    void release(float x, float y){
        dropHeld(x, y);

    }

    void dragHeld(float x, float y){

        setHeldPosition(x*16 - 8, y*16 - 8);
    }



}
