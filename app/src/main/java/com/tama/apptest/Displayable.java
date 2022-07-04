package com.tama.apptest;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

public interface Displayable {

    Bitmap getSprite();
    Bitmap getUISprite();
    void loadAsset();
    void setAsset(String asset);
}

class StaticSprite implements Displayable {

    Bitmap img;
    String assetName;

    StaticSprite(String asset){
        assetName = asset;
        img = Assets.getSprite(asset);
    }

    @Override
    public Bitmap getSprite(){
        return img;
    }

    @Override
    public Bitmap getUISprite(){
        return img;
    }

    @Override
    public void loadAsset(){
        img = Assets.getSprite(assetName);
    }

    @Override
    public void setAsset(String asset){
        assetName = asset;
        loadAsset();
    }
}


