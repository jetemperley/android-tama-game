package com.tama.apptest;

public class PetGame implements java.io.Serializable {

    private World map;
    private Thing held, selected;
    private Vec2<Float> heldPos;

    long gameTime = 0;
    static long time = 0;
    final static long gameSpeed = 25;

    PetGame(){

        map = WorldFactory.makeWorld();
        held = null;



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
            held.loadAsset();
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

    void poke(float x, float y){
        Thing t = map.checkCollision(x, y);
        if (t != null){
            t.poke();
        }
    }
}
