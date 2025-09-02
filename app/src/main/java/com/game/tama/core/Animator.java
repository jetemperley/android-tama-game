package com.game.tama.core;

import com.game.engine.Sprite;
import com.game.engine.SpriteSheet;
import com.game.engine.Time;

public class Animator implements Sprite, java.io.Serializable
{

    public transient SpriteSheet sheet;
    public boolean play = false, repeat = false;
    /** the animation row index */
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

    public void update()
    {
        if (play)
        {
            animTime += (int) Time.deltaTimeMs();
            if (isDone())
            {
                // todo bug here
                cancel();
            }
            else
            {
                animTime %= animDur;
            }
        }
    }

    private boolean isDone()
    {
        return animTime >= animDur && !repeat;
    }

    private void cancel()
    {
        animTime = 0;
        play = false;
    }

    /** plays the animation from the beginning */
    public void restart()
    {
        cancel();
        play();
    }

    int getSlide(final int time, final int duration, final int row)
    {
        // TODO this can land exactly on the col length
        final int perSlide = duration / sheet.rowLength(row);
        final int frame = time / perSlide;
        final int b = sheet.get(row, frame);
        return b;
    }

}
