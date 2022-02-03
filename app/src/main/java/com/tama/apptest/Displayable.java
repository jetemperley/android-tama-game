package com.tama.apptest;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

public interface Displayable {

    Bitmap getSprite();
    Bitmap getUISprite();
}

class StaticSprite implements Displayable {

    Bitmap img;

    StaticSprite(Bitmap bm){
        img = bm;
    }

    public Bitmap getSprite(){
        return img;
    }

    public Bitmap getUISprite(){
        return img;
    }
}
