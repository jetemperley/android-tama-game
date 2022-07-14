package com.tama.apptest;
import android.util.Log;

enum Type {
    food, pet, tree, undefined, junk
}

abstract class Thing implements java.io.Serializable{

    WorldObject wo;
    Displayable vis = null;

    Thing(){
        wo = new WorldObject();
        vis = createVis();
    }

    protected Displayable createVis (){
        return new StaticSprite("static_poop");
    }

    void loadAsset(){
        vis.loadAsset();
    }

    void display(DisplayAdapter d){
        d.displayWorld(wo, vis);
    }

    void update(World map) {

    }

    boolean canSwim() {
        return false;
    }
    boolean canWalk() {
        return true;
    }

    boolean contains(float x, float y) {
        if (x > wo.x + (wo.xoff/100f)
                && x < wo.x + (wo.xoff/100f) + 1
                && y > wo.y + (wo.yoff/100f)
                && y < wo.y + (wo.yoff/100f) + 1)
            return true;
        return false;
    }

    Type type() {
        return Type.undefined;
    }

    Thing apply(World m, int x, int y){
        return this;
    }

    void poke(World w){}
    void onPickup(){}
    void onDrop(){}
    Thing leaveBehind(){return null;}
    Thing take(){return this;}
}

class Rock extends Thing {

    Nudge a = new Nudge();
    int health = 4;
    SpriteSheet sheet;

    @Override
    protected Displayable createVis (){
        sheet = new SpriteSheet("sheet_16_rock");
        return sheet;
    }

    @Override
    public void update(World w){
        a.update(w, this);
    }

    @Override
    Thing leaveBehind(){return this;}

    @Override
    Thing take(){return null;}

    @Override
    void poke(World w){
        Log.d("Rock", "poke");
        a.start(this);
        health--;
        Log.d("Rock", "health " + health);
        sheet.x++;
        if (health <= 0){
            sheet.x = 0;
            health = 4;
            w.removeThing(wo.x, wo.y);
            w.effects.add(new Break(vis.getSprite(), wo));
            Log.d("Rock", "Added effect");
        }

    }
}

class Poo extends Thing {


}

class Tree extends Thing implements java.io.Serializable{
    int level = 0;
    int growth = 0;
    SpriteSheet sheet;

    void update(World m){
        if (growth < 1000) {
            growth ++;
        } else if (growth > 1000-2 && level < 2){
            level++;
            sheet.x++;
            growth = 0;
        }
        Log.d("Tree", "" + growth);
    }

    @Override
    protected Displayable createVis (){
        sheet = new SpriteSheet("sheet_16_treegrowth");
        return sheet;
    }

    Type type() {
        return Type.tree;
    }


}

class Seed extends Thing{

    Thing apply(World m, int ax, int ay){

        Thing t = m.takeThing(ax, ay);
        if (t==null){
            m.put(new Tree(), ax, ay);
            return null;
        }

        switch (t.type()){
            case pet:
                Pet p = (Pet)t;
                if (p.consume(t))
                    return null;
                return t;

            case food:

                break;
        }

        return this;
    }

    @Override
    protected Displayable createVis (){
        return new StaticSprite("static_seed");
    }

}

class Wood extends Thing{
    @Override
    protected Displayable createVis (){
        return new StaticSprite("static_log");
    }
}


