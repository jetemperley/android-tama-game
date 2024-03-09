package com.game.tama.core;

import android.graphics.Bitmap;

import com.game.tama.thing.Thing;

public class Animator implements Displayable, java.io.Serializable
{

    public transient SpriteSheet sheet;
    public boolean play = false, repeat = false;
    public int animId = 0;
    private int animTime = 0;
    public int animDur = 1000;

    public Animator(SpriteSheet ss)
    {
        sheet = ss;

    }

    public Bitmap getUISprite()
    {
        return sheet.get(0, 0);
    }

    public Bitmap getSprite()
    {
        return getSlide(animTime, animDur, animId);
    }

    public void play()
    {
        play = true;
    }

    public void update(Thing t)
    {
        if (play)
        {
            animTime += 25;
            if (isDone() && !repeat)
            {
                cancelAnim();
            }
            else
            {
                animTime %= animDur;
            }
        }
    }


    public void repeat(boolean repeat)
    {
        this.repeat = repeat;
    }

    boolean isDone()
    {
        return animTime >= animDur;
    }

    void cancelAnim()
    {
        animTime = 0;
        play = false;
    }

    Bitmap getSlide(int time, int duration, int row)
    {
        int perSlide = duration / sheet.len(row);
        int i = time / perSlide;
        return sheet.get(row, i);
    }

}
