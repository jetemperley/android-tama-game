package com.tama.apptest;
import android.util.Log;

import java.util.ArrayList;

public class World implements java.io.Serializable{

    private Tile[][] tile;
    int celln, openSpace;
    float xoff, yoff;
    Pet target;

    World(int size) {
        celln = size;
        xoff = 0;
        yoff = 0;

        tile = new Tile[celln][celln];
        for (int x = 0; x < celln; x++) {
            for (int y = 0; y < celln; y++) {
                setTile(x, y, new Grass());
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

    boolean put(Thing t, int x, int y) {
        if (t == null)
            return true;

        if (isEmpty(x, y)) {
            tile[x][y].setThing(t);
            t.wo.setPos(x, y);
            return true;
        }  else {
            Vec2<Integer> v = findNearestEmpty((int)x, (int)y);
            Log.d("world put", v.toString());
            if (v == null)
                return false;
            tile[v.x][v.y].setThing(t);
            t.wo.setPos(v.x, v.y);
            return true;
        }

    }

    void loadAllAssets(){
        for (int x = 0; x < tile.length; x++){
            for (int y = 0; y < tile[x].length; y++){
                tile[x][y].loadAsset();
                tile[x][y].updateDetails(this);
            }
        }
    }

    void removeThing(Thing t) {
        if (tile[t.wo.x][t.wo.y].getThing() == t)
            tile[t.wo.x][t.wo.y].takeThing();
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
        Thing t = tile[px][py].getThing();
        if (isEmpty(x, y) && ((tile[x][y].type() == TileType.ground == t.canWalk()) || (tile[x][y].type() == TileType.water == t.canSwim())))
            return true;
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
            put(t, x, y);
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
                    setTile(x, y, new DynTile());
                    break;

                case ground:
                    Log.d("map", "set ground");
                    setTile(x, y, new Grass());
                    break;
            }
            updateDyn(x, y);
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
            return tile[x][y].getThing();
        return null;
    }

    void setTile(int x, int y, Tile t){
        if (t == null)
            return;
        if (A.inRange(tile, x, y)) {
            if (tile[x][y] != null) {
                t.setThing(tile[x][y].takeThing());

            }
            tile[x][y] = t;
            t.setPos(x, y);
            updateDyn(x, y);
        }
    }

    Thing checkCollision(float x, float y){

        for (int xi = -1; xi < 2; xi++){
            for (int yi = -1; yi < 2; yi++){
                Thing t = getThing((int)x+xi, (int)y+yi);
                if (t != null && t.contains(x, y)) {
                    return t;
                }
            }
        }
        return null;

    }

    ArrayList<Vec2<Integer>> visited = new ArrayList<>();
    ArrayList<Vec2<Integer>> queue = new ArrayList<>();

    Vec2<Integer> findNearestEmpty(int x, int y){

        visited.clear();
        queue.clear();
        queue.add(new Vec2(x, y));

        while (queue.size() > 0){
            Log.d("dfs deets", "q " + queue.size());
            Vec2<Integer> curr = queue.get(0);
            queue.remove(0);

            if (isEmpty(curr.x, curr.y))
                return curr;
            visited.add(curr);

            for (int i = -1; i < 2; i+=2){
                Vec2<Integer> n = new Vec2<>(curr.x+i, curr.y);
                if (!visited.contains(n) && !queue.contains(n))
                    queue.add(n);
                Vec2<Integer> n1 = new Vec2<>(curr.x, curr.y+i);
                if (!visited.contains(n1) && !queue.contains(n1))
                    queue.add(n1);
            }
        }
        return null;
    }


    public boolean isEmpty(int x, int y){
        return A.inRange(tile, x, y) && tile[x][y].isEmpty();
    }
}



