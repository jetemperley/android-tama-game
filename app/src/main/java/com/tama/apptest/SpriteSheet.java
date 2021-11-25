package com.tama.apptest;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

public class SpriteSheet {

    // sheet is in [Y][X] coordinates
    Bitmap[][] sheet;
    ArrayList<Anim> anims;

    SpriteSheet(Bitmap[][] arr) {
        sheet = arr;
        anims = new ArrayList<Anim>();
    }

    // arr is the sheet png, and size is the size of each sprite cell
    SpriteSheet(Bitmap arr, int size) {
        Log.d("SS", "" + arr.getWidth() + " " + arr.getHeight());
        anims = new ArrayList<Anim>();
        sheet = new Bitmap[arr.getHeight() / size][arr.getWidth() / size];
        for (int Y = 0; Y < sheet.length; Y++) {
            for (int X = 0; X < sheet[Y].length; X++) {
                sheet[Y][X] = Bitmap.createBitmap(arr, X * size, Y * size, size, size);
            }
        }

    }

    Bitmap get(int x, int y) {

        return sheet[y][x];
    }

    void addRowsAsAnims() {
        for (int i = 0; i < sheet.length; i++) {
            Anim a = new Anim(sheet[i], 1000);
            // if (!anims.contains(a))
                anims.add(a);
        }
        Log.d("SS", "anim leng: " + anims.size());
    }

    void addAnim(Bitmap[] ss, int duration) {
        anims.add(new Anim(ss, duration));
    }

    void addAnim(Bitmap ss, int size, int duration) {
        Bitmap[] arr = new Bitmap[ss.getWidth() / size];
        for (int x = 0; x < arr.length; x++) {
            arr[x] = Bitmap.createBitmap(ss, x * size, 0, size, size);
        }

        addAnim(arr, duration);
    }

    class Anim {

        Bitmap[] sprites;
        // duration is in millis
        int dur;

        Anim(Bitmap[] arr, int duration) {
            sprites = arr;
            dur = duration;
        }

        Bitmap get(int time) {

            int i = (int)((time / (float)dur) * sprites.length);
            // Log.d("Anim", "time " + time  + ", dur " + dur + "sprites.length " + sprites.length + ", got frame " + i);
            return sprites[i];
        }

        boolean isDone(int time) {
            return time > dur - 1;

        }

        boolean equals(Anim a) {

            return sprites == a.sprites && dur == a.dur;
        }
    }
}

class PetAnimator {
    int animID = 0;
    int animTime = 0;
    int dir = 0;
    SpriteSheet ss;
    SpriteSheet.Anim anim;
    boolean play = false;

    PetAnimator(SpriteSheet sheet) {
        ss = sheet;
    }

    Bitmap get() {

        if (play) {
            // Log.d("SS", "playing");
            anim = ss.anims.get(animID);
            Bitmap out = anim.get(animTime);
            animTime += GameLoop.period;

            if (anim.isDone(animTime)) {
                // Log.d("SS", "Anim done");
                play = false;
                animID = dir;
                animTime = 0;
                anim = null;
            }
            return out;
        } else {
            // Log.d("SS", "playing idle, time: " + animTime);
            anim = ss.anims.get(dir + 4);
            Bitmap out = anim.get(animTime);
            animTime = (animTime + GameLoop.period) % anim.dur;
            return out;
        }

    }

    void setDir(int dir) {

        this.dir = dir;
        animTime = 0;
        animID = dir;
        anim = null;
        play = false;
        if (dir > 3) {
            this.dir = 0;
        }
    }

    void play() {
        animTime = 0;
        play = true;
    }

}

class Animator {

    SpriteSheet sheet;
    boolean play = false;
    int animID = 0, animTime = 0;

    Animator(SpriteSheet ss) {
        sheet = ss;

    }

    Bitmap get() {
        if (play) {
            SpriteSheet.Anim a = sheet.anims.get(animID);
            Bitmap out = a.get(animTime);
            animTime += GameLoop.period;
            if (a.isDone(animTime)) {
                cancelAnim();
            }
            return out;
        }
        return sheet.sheet[0][0];
    }

    void cancelAnim() {
        animTime = 0;
        play = false;
        animID = 0;
    }
}
