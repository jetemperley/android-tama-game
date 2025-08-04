package com.game.tama.core;

import com.game.engine.Time;
import com.game.tama.core.thing.Thing;

public class Animator implements Sprite, java.io.Serializable
{

    public transient SpriteSheet sheet;
    public boolean play = false, repeat = false;
    public int animId = 0;
    private int animTime = 0;
    public int animDur = 1000;

    public Animator(final SpriteSheet ss)
    {
        sheet = ss;
    }

    @Override
    public int getUISpriteId()
    {
        return sheet.get(0, 0);
    }

    @Override
    public int getSpriteId()
    {
        return getSlide(animTime, animDur, animId);
    }

    public void play()
    {
        play = true;
    }

    public void update(final Thing t)
    {
        if (play)
        {
            animTime += (int) Time.deltaTimeMs();
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


    public void repeat(final boolean repeat)
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

    int getSlide(final int time, final int duration, final int row)
    {
        final long l = Time.time();
        // todo this can land exactly on the col length
        final int perSlide = duration / sheet.rowLength(row);
        final int frame = time / perSlide;
        final int b = sheet.get(row, frame);
        return b;
    }

}
