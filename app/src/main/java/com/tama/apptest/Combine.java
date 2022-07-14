package com.tama.apptest;

import java.util.HashMap;
import java.util.Map;

public class Combine {

    static Map combs = new HashMap<String, Thing>();

    static void init(){

    }

    static Thing combine(Thing a, Thing b){

        if (combs == null)
            init();

        if (a.type().ordinal() > b.type().ordinal()) {
            Thing x = a;
            a = b;
            b = x;
        }
        return null;
    }
}
