package com.tama.apptest;

import android.graphics.Bitmap;

public class SpriteSheet{

    // sheet is in [Y][X] coordinates
    StaticSprite[][] sheet;

    SpriteSheet(StaticSprite[][] arr) {
        sheet = arr;

    }

    public Bitmap get(int row, int col) {

        return sheet[row][col].getSprite();
    }

    public StaticSprite getSprite(int row, int col){
        return sheet[row][col];
    }

    int len(int row){
        return sheet[row].length;
    }

}



class Animator implements Displayable, java.io.Serializable{

    transient SpriteSheet sheet;
    boolean play = false, repeat = false;
    int animIDX = 0, animTime = 0;
    int animDur = 1000;

    Animator(SpriteSheet ss) {

        sheet = ss;
    }

    public Bitmap getUISprite() {

        return sheet.get(0, 0);
    }

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
        return sheet.get(row, i);
    }

}
