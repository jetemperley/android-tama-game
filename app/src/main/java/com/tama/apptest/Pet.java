package com.tama.apptest;
import android.util.Log;

import java.util.ArrayList;

abstract class Pet extends Thing{

    static final int down = 0, right = 1, up = 2, left = 3;

    State state;
    String name;

    // Movement moves;
    ArrayList<Act> acts;
    // this pet animator is the same object as Displayable.sprite;
    Animator anim;

    int speed = 3;
    Stats stats;

    Pet() {
        super();
        state = new Wander();
        acts = new ArrayList<Act>();
        name = "";
        stats = new Stats();
    }

    Displayable getAsset(){
        if (anim == null) {
            anim = new Animator(Assets.sheets.get(R.drawable.sheet_16_blob));
            anim.play();
            anim.repeat(true);
        } else {
            anim.sheet = Assets.sheets.get(R.drawable.sheet_16_blob);
        }
        return anim;
    }

    void update(World map) {
        stats.updateStats(this);
        updateActions(map);

        state.update(this);
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

    String getDescription(){
        return super.getDescription() + "It's a Pet!";
    }

}

class Blob extends Pet {

    Displayable getAsset(){
        super.getAsset();
        anim.sheet = Assets.sheets.get(R.drawable.sheet_16_blob);
        return anim;
    }
}

class Walker extends Pet {

    Displayable getAsset(){
        super.getAsset();
        anim.sheet = Assets.sheets.get(R.drawable.sheet_16_walker);
        return anim;
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

    Displayable getAsset(){

        if (anim == null) {
            anim = new Animator(Assets.sheets.get(R.drawable.sheet_16_egg));
            anim.play();
            anim.repeat(true);
        } else {
            anim.sheet = Assets.sheets.get(R.drawable.sheet_16_blob);
        }
        return anim;
    }

    void update(World map) {
        if (!anim.play && Rand.RandInt(0, 100) < 20*(PetGame.gameSpeed/1000f)) {
            anim.play = true;
        }
        age += PetGame.gameSpeed;
        if (age > hatchAge) {
            // hatch
            map.removeThing(this);
            map.add(new Blob(), loc.x, loc.y);
        }
    }
    String getDescription(){
        return super.getDescription() + "An egg, age " + age + ".";
    }


}

