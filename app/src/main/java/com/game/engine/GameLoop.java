package com.game.engine;

import com.game.android.GameActivity;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class GameLoop extends Thread
{
    GameActivity activity;
    public boolean play = true;


    public GameLoop(final GameActivity activity)
    {
        this.activity = activity;
    }

    public void run()
    {
        LocalTime start = LocalTime.now();
        LocalTime end;

        while (play)
        {
            end = LocalTime.now();
            long frameTime = ChronoUnit.MILLIS.between(start, end);
            while (frameTime < 10)
            {
                try
                {
                    synchronized (this)
                    {
                        wait(10 - frameTime);
                    }
                }
                catch (final InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
                end = LocalTime.now();
                frameTime = ChronoUnit.MILLIS.between(start, end);
            }
            start = end;

            Time.updateTime(frameTime);
            BehaviorStarter.alertStart();
            activity.updateAndDraw();


        }
    }
}
