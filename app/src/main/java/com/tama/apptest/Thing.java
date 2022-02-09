package com.tama.apptest;
import android.util.Log;

enum Type {
    food, pet, tree, undefined, junk
}
abstract class Thing extends WorldObject {

    Thing(Displayable img) {
        super(img);
        flat = false;
    }

    void pickedUp(){

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

    boolean contains(float X, float Y) {
        if (X > x() + (xoff/100f) && X < x() + (xoff/100f) + 1 && Y > y() + (yoff/100f) && Y < y() + (yoff/100f) + 1)
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
}

class Rock extends Thing {

    Rock(){

        super(Assets.sprites.get(R.drawable.static_rock));
    }

}

class Poo extends Thing {

    Poo() {
        super(Assets.sprites.get(R.drawable.static_poop));
    }

    void update(World map) {
    }
    boolean isItem(){
        return true;
    }

}

class Tree extends Thing {
    int level = 0;
    int growth = 0;

    Tree() {
        super(Assets.sprites.get(R.drawable.static_tree));
    }



    void update(World m){
        if (growth < 1000) {
            growth ++;
        } else if (growth > 1000-2 && level < 2){
            level++;
            sprite = Assets.sprites.get(16+level);
            growth = 0;
        }
        Log.d("Tree", "" + growth);
    }

    Type type() {
        return Type.tree;
    }


}

class Seed extends Thing{

    Seed(){
        super(Assets.sprites.get(R.drawable.static_seed));
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



}

class Wood extends Thing{

    Wood(){
        super(Assets.sprites.get(R.drawable.static_log));
    }


    boolean isItem(){
        return true;
    }
}


