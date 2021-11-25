package com.tama.apptest;


import android.graphics.Bitmap;

class Food extends Thing{
    int id;
    int sust;
    String name;


    Food(int ID){
        super();
        id = ID + 1;

        switch (id){
            case 0:
                name = "meat";
                break;

            case 1:
                name = "leaf";
                break;

            case 3:
                name = "drum stick";
                break;

            case 4:
                name = "herb";
                break;

            case 5:
                name = "poop";
                break;

            case 6:
                name = "mush";
                break;

            case 7:
                name = "fruit";
                break;

            case 8:
                name = "fish";
                break;

            case 9:
                name = "vegetable";
                break;

            case 10:
                name = "nut";
                break;

            case 11:
                name = "berry";
                break;

            default:
                name = "junk";
                id = 5;
                break;

        }
    }
    Type type(){
        return Type.food;
    }

    Bitmap getImg(){
        return Assets.sprites.get(id);
    }

    boolean isItem(){
        return true;
    }

    void display(Map m){
        super.display(m, Assets.sprites.get(id));
    }

    Thing apply(Map m, int mx, int my){

        Thing t = m.getMouseThing(mx, my);
        if (t==null){
            return m.swapMp(this, mx, my);
        }

        switch (t.type()){
            case pet:
                Pet p = (Pet)t;
                if (p.consume(this)){
                    return null;
                }
                return this;
        }

        return this;
    }
}
