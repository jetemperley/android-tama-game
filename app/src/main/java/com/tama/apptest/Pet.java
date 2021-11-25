package com.tama.apptest;


import java.util.ArrayList;

abstract class Pet extends Thing {

    // x, y, w and h are relative to cell height and cell width
    // eg w = 1 == w = cellwidth
    State state;
    String name;
    int speed = 3;
    Movement moves;
    ArrayList<Act> acts;
    PetAnimator anim;

    int poop = 0;
    int hunger = 0;
    int thirst = 0;
    int happy = 0;
    int sleep = 0;


    Pet(PetAnimator pa) {
        super();
        state = new Wander();
        acts = new ArrayList<Act>();
        name = "";
        anim = pa;
    }


    void display(Map map) {

        display(map, anim.get());
    }

    void update(Map map) {

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

    void updateActions(Map m) {

        if (acts.size() > 0) {
            byte stat = acts.get(0).update(m, this);
            if (stat == Act.COMPLETE || stat == Act.FAILED) {
                acts.remove(0);
                // println("removed");
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
            anim.setDir(2);
        else if (x == -1)
            anim.setDir(3);
        else if (y == -1)
            anim.setDir(1);
        else if (y == 1)
            anim.setDir(0);
    }
}

class Blob extends Pet {

    Blob() {
        super(new PetAnimator(Assets.sheets.get(1)));

    }
}

class Walker extends Pet {

    Walker() {
        super(new PetAnimator(Assets.sheets.get(3)));

    }
}

class Egg extends Thing {

    Animator anim;
    int age;
    int hatchAge;

    Egg() {
        super();
        anim = new Animator(Assets.sheets.get(2));
        age = 0;
        hatchAge = 20000;

    }

    void display(Map map) {
        display(map, anim.get());
    }

    void update(Map map) {
        if (!anim.play && Rand.RandInt(0, 100) < 20*(GameLoop.period/1000f)) {
            anim.play = true;
        }
        age += GameLoop.period;
        if (age > hatchAge) {
            // hatch
            map.remove(this);
            map.add(new Blob(), x, y);
        }
    }

}

