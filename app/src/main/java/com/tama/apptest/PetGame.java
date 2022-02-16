package com.tama.apptest;

public class PetGame implements java.io.Serializable {

    private World map;
    private Thing held, selected;
    private Vec2<Float> heldPos;

    long gameTime = 0;
    static long time = 0;
    final static long gameSpeed = 25;

    PetGame(){

        map = new World(10);
        held = null;
        map.add(new Poop(), 9, 9);
        map.add(new Blob(), 1, 1);
        map.setTile(2, 2, TileType.water);
        map.setTile(2, 3, TileType.water);
        map.setTile(3, 2, TileType.water);

        for (int x = 4; x < 8; x++){
            for (int y = 4; y < 8; y++){
                map.setTile(x, y, new LongGrass());
            }
        }
        heldPos = new Vec2<>(0f, 0f);
    }

    void update(){
        map.update();
        gameTime += gameSpeed;
        time = gameTime;

    }

    void drawEnv(DisplayAdapter d) {

        map.display(d);

    }

    void drawUI(DisplayAdapter d){

        if (selected != null) {
            d.displayUI(selected);
        }

        if (held != null)
            d.displayManual(held.loc.sprite, heldPos.x, heldPos.y);

    }

    void reLoadAllAssets(){
        map.reLoadAllAssets();
        if (held != null)
            held.reLoadAsset();
    }

    void setHeldPosition(float x, float y){

        heldPos.x = x*16;
        heldPos.y = y*16;

    }

    void setHeld(int x, int y){
        held = map.takeThing(x, y);

    }

    void setSelected(int x, int y){
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

    void drop(float x, float y){
        if (map.getThing((int)x, (int)y) == null){
            map.add(held, (int)x, (int)y);
            held = null;
        }
    }

    void select(float x, float y){
        Thing t = map.checkCollision(x, y);
        selected = t;
    }
}
