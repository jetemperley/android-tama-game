package com.tama.apptest;
import android.graphics.Bitmap;

abstract class Thing {

    // offsets are a percentage of a tile
    int x, y, xoff, yoff;

    Thing() {
        x = 0;
        y = 0;
        xoff = 0;
        yoff = 0;
    }

    boolean isItem(){
        return false;
    }

    void update(Map map) {
    }

    void display(Map map, Bitmap img) {
        map.canvas.drawBitmap(img, map.cellsize*x + map.cellsize*xoff/100 + map.xoff, map.cellsize*y + map.cellsize*yoff/100 + map.yoff, map.cellsize*y + map.cellsize*yoff/100);
    }

    void display(Map map, Bitmap img, int X, int Y) {
        map.canvas.drawBitmap(img, map.cellsize*X, map.cellsize*Y, map.cellsize*Y);
    }

    void display(Map m){
        display(m, getImg());
    }

    void displayManual(Map m, float left, float top){
        m.canvas.drawBitmap(getImg(), left, top, top);
    }

    Bitmap getImg(){
        return null;
    }

    void click() {
    }

    boolean canSwim() {
        return false;
    }
    boolean canWalk() {
        return true;
    }

    boolean isColliding(float X, float Y) {
        if (X > x + (xoff/100f) && X < x + (xoff/100f) + 1 && Y > y + (yoff/100f) && Y < y + (yoff/100f) + 1)
            return true;
        return false;
    }

    Type type() {
        return Type.def;
    }

    Thing apply(Map m, int mx, int my){
        return this;
    }

    void poke(){}
}

class Rock extends Thing {

    Rock(){
        super();
    }

    void display(Map map) {
        // finish rock
    }
    void update(Map map) {
    }
}

class Poo extends Thing {

    Poo() {
        super();
    }

    void display(Map map) {
        display(map, Assets.sprites.get(6));
    }
    void update(Map map) {
    }
    boolean isItem(){
        return true;
    }

    Bitmap getImg(){
        return Assets.sprites.get(6);
    }
}

class Tree extends Thing {
    int level = 0;
    int growth = 0;

    Tree() {
        super();
    }

    void display(Map m) {
        display(m, Assets.sprites.get(16 + growth));
    }

    Bitmap getImg(){
        return Assets.sprites.get(16+growth);
    }

    void update(Map m){
        if (growth < 10000) {
            // growth += millis() - timer;
        } else if (growth > 10000-1 && level < 2){
            level++;
            growth = 0;
        }
        // println(growth);
    }

    Type type() {
        return Type.tree;
    }


}

class Seed extends Thing{

    Seed(){
        super();
    }

    boolean isItem(){
        return true;
    }

    void display(Map m) {

        display(m, Assets.sprites.get(21));
    }

    Bitmap getImg(){
        return Assets.sprites.get(21);
    }

    Thing apply(Map m, int ax, int ay){

        Thing t = m.getThing(ax, ay);
        if (t==null){
            m.add(new Tree(), ax, ay);
            return null;
        }

        switch (t.type()){
            case pet:
                Pet p = (Pet)t;
                if (p.consume(t))
                    return null;
                return t;

            case food:

                break;
        }

        return this;
    }



}

class Wood extends Thing{
    Wood(){
        super();
    }

    void display(Map m){
        display(m, Assets.sprites.get(3));
    }

    Bitmap getImg(){
        return Assets.sprites.get(3);
    }

    boolean isItem(){
        return true;
    }
}

enum Type {
    food, pet, tree, def, junk
}
