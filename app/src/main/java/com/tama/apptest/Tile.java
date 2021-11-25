package com.tama.apptest;

import android.graphics.Bitmap;

abstract class Tile {
    boolean var;
    int x, y;
    Tile(int x, int y, boolean displayVar) {
        this.var = displayVar;
        this.x = x;
        this.y = y;
    }

    void display(Map map) {
        if (var) {
            // display
            map.canvas.drawBitmap(Assets.sprites.get(22), map.cellsize*x + map.xoff, map.cellsize*y + map.yoff, (y-1)* map.cellsize);
        }

    }

    void display(Map map, Bitmap bm){

        map.canvas.drawBitmap(bm, map.cellsize*x + map.xoff, map.cellsize*y + map.yoff, (y-1)* map.cellsize);

    }

    void displaySplitStep(Map map,Bitmap bm){

        map.canvas.drawBitmap(bm, map.cellsize*x + map.xoff, map.cellsize*y + map.yoff + 2, y* map.cellsize + 1);
        map.canvas.drawBitmap(bm, map.cellsize*x + map.xoff, map.cellsize*y + map.yoff + 2 - map.cellsize/2, y* map.cellsize-map.cellsize/2);

    }

    void display(Map map,Bitmap bm, float d){

        map.canvas.drawBitmap(bm, map.cellsize*x + map.xoff, map.cellsize*y + map.yoff, d);

    }

    void update(Map m) {


    }

    void updateDetails(Map m) {


    }

    public void setVar(boolean var) {
        this.var = var;
    }

    public TileType type() {
        return TileType.ground;
    }
}

class Grass extends Tile {
    Grass(int x, int y, boolean var){
        super(x, y, var);

    }

    void update(Map m){

    }

    public TileType type() {
        return TileType.grass;
    }

}

class Bush extends Tile{

    Bush(int x, int y){
        super(x, y, true);

    }

    void display(Map m){
        display(m, Assets.sprites.get(26), y* m.cellsize+1);
    }

}

class LongGrass extends Tile{

    LongGrass(int x, int y){
        super(x, y, true);

    }

    void display(Map m){

        displaySplitStep(m, Assets.sprites.get(27));
    }



}

class DynTile extends Tile {
    // considers the surrounding tile to create a dynamic tile graphic

    static SpriteSheet ss;
    Bitmap[][] parts;

    // img is 4 px sq 4*3 sprite sheet of possible configurations
    DynTile(Map m, int X, int Y) {

        super(X, Y, true);
        if (ss == null)
            ss = Assets.sheets.get(0);
        parts = new Bitmap[2][2];
        updateDetails(m);

    }

    public TileType type(){
        return TileType.water;
    }

    void display(Map m) {
        float p = m.cellsize / 2;
        for (int a = 0; a < parts.length; a++) {
            for (int b = 0; b < parts[a].length; b++) {
                if (parts[a][b] != null)
                    m.canvas.drawBitmap(parts[a][b], (x * m.cellsize) + (a * p) + m.xoff, (y * m.cellsize) + (b * p) + m.yoff, 0);
            }
        }
    }

    void updateDetails(Map m) {
        setTL(m);
        setTR(m);
        setBL(m);
        setBR(m);
    }

    private void setTL(Map m) {

        parts[0][0] = ss.get(3, 2);
        Tile t = m.getTile(x - 1, y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile( x, y-1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(x - 1, y -1 );
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            parts[0][0] = ss.get(0, 0);
            if (u) {
                parts[0][0] = ss.get(3, 1);

                if (ul) {
                    parts[0][0] = null;
                }
                return;
            }
            return;
        }
        if (u) {
            parts[0][0] = ss.get(3, 0);
        }
    }

    private void setTR(Map m) {

        parts[1][0] = ss.get(0, 2);
        Tile t = m.getTile(x + 1, y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(x , y-1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(x + 1, y - 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            parts[1][0] = ss.get(0, 0);
            if (u) {
                parts[1][0] = ss.get(0, 1);

                if (ul) {
                    parts[1][0] = null;
                }
                return;
            }
            return;
        }
        if (u) {
            parts[1][0] = ss.get(1, 0);
        }
    }

    private void setBL(Map m) {

        parts[0][1] = ss.get(2, 2);
        Tile t = m.getTile(x - 1, y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(x, y + 1);
        boolean u =  t != null && t.type() == TileType.water;
        t = m.getTile(x - 1, y + 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            parts[0][1] = ss.get(2, 0);
            if (u) {
                parts[0][1] = ss.get(2, 1);
                if (ul) {
                    parts[0][1] = null;
                }
                return;
            }
            return;
        }
        if (u) {
            parts[0][1] = ss.get(3, 0);
        }
    }

    private void setBR(Map m) {

        parts[1][1] = ss.get(1, 2);
        Tile t = m.getTile(x + 1, y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(x , y + 1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(x + 1, y + 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            parts[1][1] = ss.get(2, 0);
            if (u) {
                parts[1][1] = ss.get(1, 1);
                if (ul) {
                    parts[1][1] = null;
                }
                return;
            }
            return;
        }
        if (u) {
            parts[1][1] = ss.get(1, 0);
        }
    }

}

enum TileType {

    water, ground, grass
}