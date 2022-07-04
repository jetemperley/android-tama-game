package com.tama.apptest;

import android.util.Log;

public class Axe extends Thing {

    @Override
    protected Displayable createVis (){
        return new StaticSprite("static_axe");
    }

    boolean isItem() {
        return true;
    }

    Thing apply(World m, int ax, int ay) {

        Thing t = m.getThing(ax, ay);
        if (t == null) {
            return this;
        }

        switch (t.type()) {
            case tree:

                Tree tree = (Tree) t;
                m.takeThing(ax, ay);
                if (tree.level == 2) {
                    m.put(new Wood(), tree.wo.x, tree.wo.y);
                }
                break;

            case food:

                break;

        }
        return this;
    }
}

class Shovel extends Thing {

    @Override
    protected Displayable createVis (){
        return new StaticSprite("static_axe");
    }

    boolean isItem() {
        return true;
    }

    Thing apply(World m, int ax, int ay) {

        Thing t = m.takeThing(ax, ay);
        TileType tt = m.getTile(ax, ay).type();
        Log.d("shovel", "apply" + tt);
        if (tt == TileType.water) {
            Log.d("shovel", "apply ground");
            m.setTile(ax, ay, TileType.grass);

        } else if (tt == TileType.grass) {
            Log.d("shovel", "apply water");
            m.setTile(ax, ay, TileType.water);
        }

        return this;
    }
}

class Hammer extends Thing {

    @Override
    protected Displayable createVis (){
        return new StaticSprite("static_axe");
    }

    boolean isItem() {
        return true;
    }

    Thing apply(World m, int ax, int ay) {

        Thing t = m.takeThing(ax, ay);

        return this;
    }
}
