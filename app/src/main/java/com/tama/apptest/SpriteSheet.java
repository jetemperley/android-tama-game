package com.tama.apptest;

import android.graphics.Bitmap;

public class SpriteSheet implements Displayable{

    // sheet is in [Y][X] coordinates
    transient Bitmap[][] sheet;

    // current sprite
    int x = 0, y = 0;
    String assetName;

    SpriteSheet(String name) {
        assetName = name;
        loadAsset();

    }

    public Bitmap getSprite(int row, int col){
        return sheet[row][col];
    }

    int len(int row){
        return sheet[row].length;
    }

    @Override
    public Bitmap getSprite() {
        if (x == -1)
            return null;
        return getSprite(y, x);
    }

    @Override
    public Bitmap getUISprite() {
        return null;
    }

    @Override
    public void loadAsset() {
        sheet = Assets.getSheet(assetName);
    }

    @Override
    public void setAsset(String asset) {
        assetName = asset;
        loadAsset();
    }

    void setSprite(int row, int col){
        x = col;
        y = row;
    }

}



class Animator implements Displayable, java.io.Serializable{

    SpriteSheet sheet;
    boolean play = false, repeat = false;
    int animIDX = 0, animTime = 0;
    int animDur = 1000;

    Animator(String sheetName) {
        sheet = new SpriteSheet(sheetName);

    }

    @Override
    public Bitmap getUISprite() {

        return sheet.getSprite(0, 0);
    }

    @Override
    public void loadAsset() {
        sheet.loadAsset();
    }

    @Override
    public void setAsset(String asset) {
        sheet.setAsset(asset);
    }

    @Override
    public Bitmap getSprite() {
        if (play) {
            animTime += 25;
            if (isDone() && !repeat) {
                cancelAnim();
            } else {
                animTime %= animDur;
            }
        }
        return  getSlide(animTime, animDur, animIDX);
    }

    void play(){
        play = true;
    }

    void repeat(boolean repeat){
        this.repeat = repeat;
    }

    boolean isDone(){
        return animTime >= animDur;
    }

    void cancelAnim() {
        animTime = 0;
        animIDX = 0;
        play = false;


    }

    Bitmap getSlide(int time, int duration, int row){
        int perSlide = duration/sheet.len(row);
        int i = time/perSlide;
        return sheet.getSprite(row, i);
    }


}
