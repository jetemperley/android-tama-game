package com.tama.apptest;

import android.graphics.Bitmap;

public class Axe extends Thing {

    Axe() {
        super();
    }

    boolean isItem() {
        return true;
    }

    Thing apply(Map m, int ax, int ay) {

        Thing t = m.getThing(ax, ay);
        if (t == null) {
            return this;
        }

        switch (t.type()) {
            case tree:

                Tree tree = (Tree) t;
                m.remove(t);
                if (tree.level == 2) {
                    m.add(new Wood(), tree.x, tree.y);
                }
                break;

            case food:

                break;

        }
        return this;
    }

    public void display(Map m) {
        display(m, Assets.sprites.get(13));
    }

    Bitmap getImg(){
        return Assets.sprites.get(13);
    }
}

class Shovel extends Thing {
    Shovel() {
        super();
    }

    boolean isItem() {
        return true;
    }

    Thing apply(Map m, int ax, int ay) {

        Thing t = m.getThing(ax, ay);

        if (m.getTile(ax, ay).type() == TileType.water) {
            m.setTile(ax, ay, TileType.ground);
        } else if (m.getTile(ax, ay).type() == TileType.ground) {
            m.setTile(ax, ay, TileType.water);
        }

        return this;
    }

    public void display(Map m) {
        display(m, Assets.sprites.get(14));
    }

    Bitmap getImg(){
        return Assets.sprites.get(14);
    }
}

class Hammer extends Thing {


    Hammer() {
        super();
    }

    boolean isItem() {
        return true;
    }

    Thing apply(Map m, int ax, int ay) {

        Thing t = m.getThing(ax, ay);

        return this;
    }

    public void display(Map m) {
        display(m, Assets.sprites.get(15));
    }

    Bitmap getImg(){
        return Assets.sprites.get(15);
    }
}
