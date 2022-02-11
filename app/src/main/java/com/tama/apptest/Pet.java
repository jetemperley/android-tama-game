package com.tama.apptest;
import java.util.ArrayList;

abstract class Pet extends Thing{

    static final int down = 0, right = 1, up = 2, left = 3;

    State state;
    String name;
    int speed = 3;
    Movement moves;
    ArrayList<Act> acts;
    // this pet animator is the same object as Displayable.sprite;
    Animator anim;

    int poop = 0;
    int hunger = 0;
    int thirst = 0;
    int happy = 0;
    int sleep = 0;


    Pet() {
        anim = new Animator(null);
        loadAssets();
        anim.play();
        anim.repeat(true);
        state = new Wander();
        acts = new ArrayList<Act>();
        name = "";
    }

    void loadAssets(){
        anim.sheet = Assets.sheets.get(R.drawable.sheet_16_blob);
    }

    void update(World map) {

        updateStats();
        updateActions(map);

        state.update(this);
    }


    void updateStats() {
        if (hunger == 0) {
            happy--;
        } else if (hunger > 0) {

            hunger -= GameLoop.period;
            poop += GameLoop.period;
        }
        poop++;
        if (poop > 10000) {
            // println("pooping");
            acts.add(new Poop());
            poop -= 10000;
        }
    }

    void updateActions(World m) {

        if (acts.size() > 0) {
            ActState stat = acts.get(0).update(m, this);
            if (stat == ActState.complete || stat == ActState.failed) {
                acts.remove(0);
            }
        }
    }

    boolean consume(Thing t) {
        //COMPLETE
        return true;
    }

    void click() {
        // println("patted");
        acts.add(0, new Pat());

    }

    Type type() {
        return Type.pet;
    }

    void setDir(int x, int y){
        if (x == 1)
            anim.animID = right;
        else if (x == -1)
            anim.animID = left;
        else if (y == -1)
            anim.animID = up;
        else if (y == 1)
            anim.animID = down;
    }
}

class Blob extends Pet {

    void loadAssets(){
        anim.sheet = Assets.sheets.get(R.drawable.sheet_16_blob);
    }
}

class Walker extends Pet {

    void loadAssets(){
        anim.sheet = Assets.sheets.get(R.drawable.sheet_16_walker);
    }
}

class Egg extends Thing {

    Animator anim;
    int age;
    int hatchAge;

    Egg() {
        super();
        anim = new Animator(null);
        age = 0;
        hatchAge = 20000;

    }

    void loadAssets(){
        anim.sheet = Assets.sheets.get(R.drawable.sheet_16_egg);
    }

    void update(World map) {
        if (!anim.play && Rand.RandInt(0, 100) < 20*(GameLoop.period/1000f)) {
            anim.play = true;
        }
        age += GameLoop.period;
        if (age > hatchAge) {
            // hatch
            map.removeThing(this);
            map.add(new Blob(), x, y);
        }
    }

}

