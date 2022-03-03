package com.tama.apptest;

import java.util.Random;

public class WorldFactory {

    static World makeWorld(){
        World w = new World(15);

        Noise n = new Noise((int)(Math.random()*100000), 5, 0.05f);

        for (int x = 0; x < w.width(); x++){
            for (int y = 0; y < w.height(); y++){

                float r = n.getNoise(x, y);
                Tile tt;

                if (r < 0.2){
                    tt = new DynTile();
                } else if (r < 0.3) {
                    tt = new Sand();
                } else {
                    tt = new Grass();
                }

                w.setTile(x, y, tt);

                if (r > 0.95){
                    w.add(new Tree(3), x, y);
                } else if (r > 0.92){
                    w.add(new Bush(), x, y);
                }

            }
        }

        return w;
    }
}
