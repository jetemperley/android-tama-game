package com.tama.apptest;


abstract class State implements java.io.Serializable {

    abstract void update(Pet p);

}

class Wander extends State{

    int waitTime = 0;
    boolean wait = true;

    Wander(){

    }

    public void update(Pet p){
        if (waitTime <= 0){
            if (wait){
                waitTime = getRandWaitTime();
                wait =false;
            } else {
                Vec2<Integer> v = getRandDir();
                p.acts.add(new Step(v.x, v.y));
                wait = true;
            }
        } else {

            waitTime -= GameActivity.period;
        }
    }

    int getRandWaitTime(){
        return Rand.RandInt(0, 5000);
    }

    Vec2<Integer> getRandDir(){
        int i = Rand.RandInt(0, 4);
        Vec2<Integer> out = new Vec2<>(0, 0);

        if (i == 0){
            out.x = -1;
        } else if (i == 1){
            out.x = 1;
        } else if (i == 2){
            out.y = 1;
        } else if (i == 3){
            out.y = -1;
        }
        return out;
    }


}

class Homeostasis extends State{
    public void update(Pet p){
        // check stats to find what is needed
    }
}



class Inspect extends State{

    int x, y;

    Inspect(int X, int Y){
        x = X;
        y = Y;
    }

    public void update(Pet p){

    }
}
