package com.tama.apptest;

import java.util.ArrayList;

public class Map {
    private Thing[][] block;
    private Tile[][] terrain;
    int celln, cellsize, openSpace;
    float xoff, yoff;
    DepthCanvas canvas;
    Pet target;

    Map(int size) {
        celln = size;
        cellsize = 16;
        xoff = 0;
        yoff = 0;

        block = new Thing[celln][celln];
        terrain = new Tile[celln][celln];


        for (int x = 0; x < celln; x++) {
            for (int y = 0; y < celln; y++) {
                terrain[x][y] = new Grass(x, y, true);
            }
        }

        terrain[3][5] = new DynTile(this, 3, 5);
        updateDyn(3, 5);
        terrain[3][6]  = new DynTile(this, 3, 6);
        updateDyn(3, 6);
//        terrain[4][1] = new LongGrass(4, 1);
//        terrain[5][1] = new LongGrass(5, 1);
//        terrain[4][2] = new LongGrass(4, 2);
//        terrain[5][2] = new LongGrass(5, 2);



        add(new Tree(), 4, 4);
        add(new Tree(), 4, 5);
        add(new Tree(), 5, 4);
        add(new Food(0), 5, 5);
        add(new Food(1), 5, 6);
        add(new Seed(), 0, 0);
        add(new Axe(), 9, 9);
        add(new Shovel(), 8, 9);
        target = new Blob();
        add(target, 5, 0);
        add(new Walker(), 0, 8);


    }

    void update() {
        openSpace = 0;
        for (int x = 0; x < celln; x++) {
            for (int y = 0; y < celln; y++) {
                if (block[x][y] != null)
                    block[x][y].update(this);
                terrain[x][y].update(this);
                if (terrain[x][y].type() == TileType.grass){
                    openSpace++;
                }
            }
        }
        
    }

    void display() {
        for (int x = 0; x < celln; x++) {
            for (int y = 0; y < celln; y++) {
                terrain[x][y].display(this);
            }
        }

        for (int x = 0; x < celln; x++) {
            for (int y = 0; y < celln; y++) {
                if (block[x][y] != null)
                    block[x][y].display(this);
            }
        }

    }

    void add(Thing t, int x, int y) {
        if (t == null)
            return;

//        if (block[x][y] != null) {
//            Vec2 pos = findNearestOpenSpace(x, y);
//            if (pos == null)
//                return;
//            x = pos.x;
//            y = pos.y;
//        }
        if (block[x][y] == null) {
            block[x][y] = t;
            t.x = x;
            t.y = y;
        }
    }

    void remove(Thing t) {
        if (block[t.x][t.y] == t)
            block[t.x][t.y] = null;
    }

    void removeThing(int x, int y) {
        if (A.inRange(block, x, y)) {
            block[x][y] = null;
        }
    }

    Vec2 findNearestOpenSpace(int X, int Y) {

        byte[][] m = new byte[block.length][block[0].length];
        ArrayList<Vec2> stack = new ArrayList<Vec2>();
        m[X][Y] = 1;
        Vec2 node = new Vec2(X, Y);

        while (block[node.x][node.y] != null || terrain[node.x][node.y].type() != TileType.ground) {

            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {

                    if (m[node.x + x][node.y + y] == 0) {
                        stack.add(new Vec2(node.x + x, node.y + y));
                        m[node.x + x][node.y + y] = 1;
                    }
                }
            }
            node = stack.get(0);
            stack.remove(0);
        }

        if (block[node.x][node.y] == null && terrain[node.x][node.y].type() == TileType.ground)
            return node;

        return null;
    }

    // param: ax,ay is the thing stepping, x,y is the spot to check
    // return: if the thing can be on tile x, y
    // TODO refactor this
    boolean canStepOnto(int px, int py, int x, int y) {
        // Log.d("Map", "" + px + " " + py);
        Thing t = block[px][py];
        if (isEmpty(x, y) && ((terrain[x][y].type() == TileType.ground == t.canWalk()) || (terrain[x][y].type() == TileType.water == t.canSwim())))
            return true;
        return false;
    }

    boolean isEmpty(int x, int y) {
        if (A.inRange(block, x, y) && block[x][y] == null)
            return true;
        return false;
    }

    Thing getThing(int x, int y) {
        if (A.inRange(block, x, y))
            return block[x][y];
        return null;
    }

    // takes mouse coordinates
    Thing getMouseThing(int X, int Y) {
        int x = (int)(X - xoff) / cellsize;
        int y = (int)(Y - yoff) / cellsize;
        // println("map", x, y);
        if (A.inRange(block, x, y)) {
            return block[x][y];
        }
        return null;
    }

    void offsetMap(float x, float y) {
        xoff += x;
        yoff += y;
    }

    Thing swapMp(Thing t, int mx, int my) {
        int x = (int)((mx - xoff) / cellsize);
        int y = (int)((my - yoff) / cellsize);
        return swapAp(t, x, y);
    }

    Thing swapAp(Thing t, int x, int y) {
        if (A.inRange(block, x, y) && (block[x][y] == null || block[x][y].isItem())) {
            Thing temp = block[x][y];
            block[x][y] = null;
            add(t, x, y);
            return temp;
        }
        return t;
    }

    Vec2 mouseToPos(int mx, int my) {
        return new Vec2((int)(mx - xoff) / cellsize, (int)(my - yoff) / cellsize);
    }

    void updateDyn(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (A.inRange(terrain, x + i, y + j) && terrain[x + i][y + j] != null)
                    terrain[x + i][y + j].updateDetails(this);
            }
        }
    }

    void setTile(int x, int y, TileType tile) {

        if (A.inRange(terrain, x, y)) {

            switch (tile) {
                case water:
                    terrain[x][y] = new DynTile(this, x, y);
                    updateDyn(x, y);
                    break;

                case ground:
                    terrain[x][y] = new Grass(x, y, true);
                    break;
            }
        }

    }

    Tile getTile(int x, int y) {
        if (A.inRange(terrain, x, y)) {
            return terrain[x][y];
        }
        return null;
    }

    int width() {
        return celln;
    }

    int height() {
        return celln;
    }
}



