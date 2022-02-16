package com.tama.apptest;
import android.graphics.drawable.Drawable;
import android.util.Log;

enum Type {
    food, pet, tree, undefined, junk
}
abstract class Thing implements java.io.Serializable{

    WorldObject loc;

    Thing() {
        loc = new WorldObject(null);
        loc.sprite = getAsset();
    }
    void display(DisplayAdapter d){
        d.displayWorld(loc);
    }

    Displayable getAsset(){

        return Assets.sprites.get(R.drawable.static_poop);
    }

    void reLoadAsset(){
        loc.sprite = getAsset();
    }

    boolean isItem(){
        return false;
    }

    void update(World map) {

    }

    void click() {
    }

    boolean canSwim() {
        return false;
    }
    boolean canWalk() {
        return true;
    }

    boolean contains(float x, float y) {
        if (x > loc.x + (loc.xoff/100f)
                && x < loc.x + (loc.xoff/100f) + 1
                && y > loc.y + (loc.yoff/100f)
                && y < loc.y + (loc.yoff/100f) + 1)
            return true;
        return false;
    }

    Type type() {
        return Type.undefined;
    }

    Thing apply(World m, int x, int y){
        return this;
    }

    void poke(){}

    String getDescription(){
        return "Loc: " + loc.x + ", " + loc.y + ". ";
    }
}

class Rock extends Thing {

    Displayable getAsset(){
        return Assets.sprites.get(R.drawable.static_rock);
    }

    String getDescription(){
        return super.getDescription() + "Just a rock.";

    }
}

class Poop extends Thing {

    final static int poopTime = 10000;

    Displayable getAsset(){
        return Assets.sprites.get(R.drawable.static_poop);
    }
    boolean isItem(){
        return true;
    }

    String getDescription(){
        return super.getDescription() + "Ew, a poo. And this is a " +
                "super duper  duper duper duper duper duper duper duper" +
                "duper duper duper duper duper duper duper duper duper" +
                " duper duper duper duper duper duper duper duper duper" +
                "long description";
    }

}

class Tree extends Thing implements java.io.Serializable{
    int level = 0;
    int growth = 0;

    Displayable getAsset(){
        return Assets.sprites.get(R.drawable.static_tree);
    }


    void update(World m){
        if (growth < 1000) {
            growth ++;
        } else if (growth > 1000-2 && level < 2){
            level++;
            loc.sprite = Assets.sprites.get(16+level);
            growth = 0;
        }
        Log.d("Tree", "" + growth);
    }

    Type type() {
        return Type.tree;
    }

    String getDescription(){
        return super.getDescription() + "Its a tree, level " + level + ".";
    }

}

class Seed extends Thing{

    Displayable getAsset(){
        return Assets.sprites.get(R.drawable.static_seed);
    }

    boolean isItem(){
        return true;
    }

    Thing apply(World m, int ax, int ay){

        Thing t = m.takeThing(ax, ay);
        if (t==null){
            m.add(new Tree(), ax, ay);
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

    String getDescription(){
        return super.getDescription() + "A seed, plant it and it will grow.";
    }

}

class Wood extends Thing{

    Displayable getAsset(){
        return Assets.sprites.get(R.drawable.static_log);
    }

    boolean isItem(){
        return true;
    }

    String getDescription(){
        return "A chunk of wood.";
    }
}


