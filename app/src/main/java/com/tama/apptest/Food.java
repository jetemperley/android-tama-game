package com.tama.apptest;


import android.graphics.Bitmap;

class Food extends Thing{
    int id;
    int sust;
    String name;


    Food(int ID){
        super(Assets.sprites.get(ID));
        id = ID;

        switch (id){
            case R.drawable.static_meat:
                name = "meat";
                break;

            case R.drawable.static_leaf:
                name = "leaf";
                break;

            case R.drawable.static_meatbone:
                name = "drumstick";
                break;

            case R.drawable.static_herb:
                name = "herb";
                break;

            case R.drawable.static_poop:
                name = "poop";
                break;

            case R.drawable.static_mushroom:
                name = "mush";
                break;

            case R.drawable.static_apple:
                name = "apple";
                break;

            case R.drawable.static_fish:
                name = "fish";
                break;

            case R.drawable.static_carrot:
                name = "carrot";
                break;

            case R.drawable.static_acorn:
                name = "acorn";
                break;

            case R.drawable.static_cherries:
                name = "cherries";
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

    boolean isItem(){
        return true;
    }

    Thing apply(World m, int ax, int ay){

        Thing t = m.getThing(ax, ay);
        if (t==null){
            return m.swap(this, ax, ay);
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
