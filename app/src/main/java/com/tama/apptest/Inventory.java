package com.tama.apptest;


public class Inventory {

    private Thing[] inv;

    Inventory(){
        inv = new Thing[5];

    }

    void display(DisplayAdapter d){
        d.displayUI(this);
    }

    int len(){
        return inv.length;
    }

    Thing get(int idx){
        if (A.inRange(inv, idx))
            return inv[idx];
        return null;
    }

    Thing take(int idx){
        if (A.inRange(inv, idx)){
            Thing t = inv[idx];
            inv[idx] = null;
            return t;
        }
        return null;
    }

    Thing swap(Thing t, int idx){
        if (A.inRange(inv, idx)){
            Thing out = inv[idx];
            inv[idx] = t;
            return out;
        }
        return t;
    }


}
