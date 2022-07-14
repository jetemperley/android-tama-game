package com.tama.apptest;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public interface Effect {

    boolean display(DisplayAdapter d);

}

class Break implements Effect{

    static StaticSprite sprite;
    List<Vec2<Float>> pix;
    List<Vec2<Float>> dir;
    float time = 0, maxTime = 1000;


    Matrix m = new Matrix();
    float[] f = new float[2];

    Break(Bitmap b, WorldObject wo){
        if (sprite == null)
            sprite = new StaticSprite("static_1");

        pix = new ArrayList<>(50);
        dir = new ArrayList<>(50);

        for (int x = 0; x < b.getWidth(); x++){
            for (int y = 0; y < b.getHeight(); y++){

                int p = b.getPixel(x, y);
                if (p == -1){
                    pix.add(new Vec2<Float>(wo.x*16 +(float)x, wo.y*16 +(float)y));
                    m.reset();
                    int d = Rand.RandInt(0, 360);
                    Log.d("Effect", "d = " + d);
                    m.postRotate(d);
                    f[0] = 0;
                    f[1] = -20;
                    m.mapPoints(f);
                    dir.add(new Vec2<Float>(f[0], f[1]));

                }
            }
        }

    }

    public boolean display(DisplayAdapter d){

        time += 25;
        if (time >= maxTime)
            return true;

        for (int i = 0; i < pix.size(); i++){
            Vec2<Float> pos = pix.get(i);
            Vec2<Float> vel = dir.get(i);
            d.displayManual(sprite, (float)pos.x, (float)pos.y);
            pos.x += vel.x*25/1000;
            pos.y += vel.y*25/1000;
            vel.y += 50f * 20/1000;
        }

        return false;

    }
}
