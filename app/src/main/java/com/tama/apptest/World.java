package com.tama.apptest;
import android.util.Log;

public class World {

    private Tile[][] tile;
    int celln, openSpace;
    float xoff, yoff;
    DepthDisplay canvas;
    Pet target;

    World(int size) {
        celln = size;
        xoff = 0;
        yoff = 0;

        tile = new Tile[celln][celln];
        for (int x = 0; x < celln; x++) {
            for (int y = 0; y < celln; y++) {
                tile[x][y] = new Grass(x, y, true);
            }
        }





    }

    void update() {
        openSpace = 0;
        for (int x = 0; x < celln; x++) {
            for (int y = 0; y < celln; y++) {
                tile[x][y].update(this);
            }
        }
        
    }

    void display(DisplayAdapter d) {
        for (int x = 0; x < celln; x++) {
            for (int y = 0; y < celln; y++) {
                tile[x][y].display(d);
            }
        }

    }

    void add(Thing t, int x, int y) {
        if (t == null)
            return;

        if (tile[x][y].isEmpty()) {
            tile[x][y].set(t);
            t.x = x;
            t.y = y;
        }
    }

    void removeThing(Thing t) {
        if (tile[t.x][t.y].thing == t)
            tile[t.x][t.y].takeThing();
    }

    void removeThing(int x, int y) {
        if (A.inRange(tile, x, y)) {
            tile[x][y].takeThing();
        }
    }



    // param: px, py is the thing stepping, x,y is the spot to check
    // return: if the thing can be on tile x, y
    // TODO refactor this
    boolean canStepOnto(int px, int py, int x, int y) {
        // Log.d("Map", "" + px + " " + py);
        Thing t = tile[px][py].thing;
        if (isEmpty(x, y) && ((tile[x][y].type() == TileType.ground == t.canWalk()) || (tile[x][y].type() == TileType.water == t.canSwim())))
            return true;
        return false;
    }

    boolean isEmpty(int x, int y) {
        if (A.inRange(tile, x, y))
            return tile[x][y].isEmpty();
        return false;
    }

    Thing takeThing(int x, int y) {
        if (A.inRange(tile, x, y))
            return tile[x][y].takeThing();
        return null;
    }


    // swaps t with the target xy, or returns t
    Thing swap(Thing t, int x, int y) {
        if (fits(t, x, y)) {
            Thing temp = tile[x][y].takeThing();
            add(t, x, y);
            return temp;
        }
        return t;
    }


    void updateDyn(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (A.inRange(tile, x + i, y + j) && tile[x + i][y + j] != null)
                    tile[x + i][y + j].updateDetails(this);
            }
        }
    }

    // bug source
    void setTile(int x, int y, TileType type) {

        if (A.inRange(tile, x, y)) {

            switch (type) {
                case water:
                    Log.d("map", "set water");
                    tile[x][y] = new DynTile(this, x, y);
                    updateDyn(x, y);
                    break;

                case ground:
                    Log.d("map", "set ground");
                    tile[x][y] = new Grass(x, y, true);
                    updateDyn(x, y);
                    break;
            }
        }

    }

    Tile getTile(int x, int y) {
        if (A.inRange(tile, x, y)) {
            return tile[x][y];
        }
        return null;
    }

    int width() {
        return celln;
    }

    int height() {
        return celln;
    }

    // works if everything only occupies 1 cell
    boolean fits(Thing t, int x, int y){
        return A.inRange(tile, x, y);
    }

    Thing getThing(int x, int y){
        if (A.inRange(tile, x, y))
            return tile[x][y].thing;
        return null;
    }
}



