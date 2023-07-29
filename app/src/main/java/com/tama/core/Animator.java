package com.tama.core;

import android.graphics.Bitmap;

public class Animator implements Displayable, java.io.Serializable
{

    public transient SpriteSheet sheet;
    public boolean play = false, repeat = false;
    public int animID = 0;
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
        return getSlide(animTime, animDur, animID);
    }

    public void play()
    {
        play = true;
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
