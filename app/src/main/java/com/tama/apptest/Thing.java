package com.tama.apptest;
import android.graphics.drawable.Drawable;
import android.util.Log;

enum Type {
    food, pet, tree, undefined, junk
}
abstract class Thing implements java.io.Serializable{

    WorldObject loc;
    String asset = Assets.static_poop;

    Thing() {
        loc = new WorldObject(null);
        loc.sprite = getAsset();
    }
    void display(DisplayAdapter d){
        d.displayWorld(loc);
    }

    Displayable getAsset(){
        return Assets.getSprite(asset);
    }

    void loadAsset(){
        loc.sprite = getAsset();
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

    Thing pickup(){
        return this;
    }
}

class Rock extends Thing {

    Rock(){
        super();
        asset = Assets.static_rock;
        loadAsset();
    }

    String getDescription(){
        return super.getDescription() + "Just a rock.";

    }
}

class Poop extends Thing {

    final static int poopTime = 10000;

    Poop(){
        super();
        asset = Assets.static_poop;
        loadAsset();
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
    int lvl = 0;
    int growth = 0;
    Animator anim;

    Tree(int level){
        super();
        asset = Assets.static_poop;
        loadAsset();

        lvl = level;
        if (lvl < 0 || lvl > 4)
            lvl = 0;
        anim.animID = lvl;

    }

    Displayable getAsset(){
        if (anim == null){
            anim = new Animator(Assets.sheets.get(R.drawable.sheet_16_treegrowth));
        } else {
            anim.sheet = Assets.sheets.get(R.drawable.sheet_16_treegrowth);
        }
        return anim;
    }


    void update(World m){
//        if (growth < 1000) {
//            growth ++;
//        } else if (growth > 1000-2 && level < 2){
//            level++;
//            loc.sprite = Assets.sprites.get(16+level);
//            growth = 0;
//        }
        // Log.d("Tree", "" + growth);
    }

    void poke(){
        anim.play();
    }

    Type type() {
        return Type.tree;
    }

    String getDescription(){
        return super.getDescription() + "Its a tree, level " + lvl + ".";
    }

    Thing pickup(){
        return this;
    }

}

class PulledBush extends Thing{

    PulledBush(){
        super();
        asset = Assets.static_pullbush;
        loadAsset();
    }
}

class Seed extends Thing{

    Seed(){
        super();
        asset = Assets.static_seed;
        loadAsset();
    }

    boolean isItem(){
        return true;
    }

    Thing apply(World m, int ax, int ay){

        Thing t = m.takeThing(ax, ay);
        if (t==null){
            m.add(new Tree(0), ax, ay);
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

    Wood(){
        asset = Assets.static_log;
        loadAsset();
    }

    boolean isItem(){
        return true;
    }

    String getDescription(){
        return "A chunk of wood.";
    }
}

class Bush extends Thing{

    Animator anim;

    Bush(){
        asset = Assets.sheet_16_bush;
        loadAsset();
    }

    Displayable getAsset(){

        if (anim == null){
            anim = new Animator(Assets.getSheet(asset));
        } else {
            anim.sheet = Assets.getSheet(asset);
        }
        anim.animID= 1;
        anim.animDur = 500;
        return anim;
    }

    void poke(){
        anim.play();
    }

    boolean isItem(){
        return true;
    }

    String getDescription(){
        return "A little bush.";
    }

    Thing pickup(){
        return new PulledBush();
    }
}


