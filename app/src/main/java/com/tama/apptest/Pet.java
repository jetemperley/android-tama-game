package com.tama.apptest;
import java.util.ArrayList;

abstract class Pet extends Thing{

    static final int down = 0, up = 1, right = 2, left = 3;

    State state;
    String name;
    int speed = 3;
    ArrayList<Act> acts;
    // this pet animator is the same object as Displayable.sprite;
    Animator anim;
    Thing held = null;

    int poop = 0;
    int hunger = 0;
    int thirst = 0;
    int happy = 0;
    int sleep = 0;


    Pet() {
        super();
        state = new Wander();
        acts = new ArrayList<Act>();
        name = "";
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

            hunger -= GameActivity.period;
            poop += GameActivity.period;
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

    Type type() {
        return Type.pet;
    }

    void setDir(int x, int y){
        if (x == 1)
            anim.animIDX = right;
        else if (x == -1)
            anim.animIDX = left;
        else if (y == -1)
            anim.animIDX = up;
        else if (y == 1)
            anim.animIDX = down;
    }

    @Override
    void onPickup(){
        acts.clear();
        acts.add(new Panic());
    }

    @Override
    void onDrop(){
        acts.clear();
    }

    @Override
    boolean combine(Thing t, World w){
        if (held == null) {
            held = t;
            return true;
        }

        return false;
    }

}

class Blob extends Pet {

    @Override
    protected Displayable createVis(){
        anim = new Animator("sheet_16_blob");
        anim.play();
        anim.repeat(true);
        return anim;
    }

}

class Walker extends Pet {


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


    void update(World map) {
        if (!anim.play && Rand.RandInt(0, 100) < 20*(GameActivity.period/1000f)) {
            anim.play = true;
        }
        age += GameActivity.period;
        if (age > hatchAge) {
            // hatch
            map.removeThing(this);
            map.put(new Blob(), wo.x, wo.y);
        }
    }

}

