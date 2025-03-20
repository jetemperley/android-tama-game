package com.game.engine;

import com.game.android.GameActivity;
import com.game.tama.util.Log;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class GameLoop extends Thread
{
    GameActivity activity;
    public boolean play = true;

    public static float deltaTimeS = 0;
    public static long deltaTimeMs = 0;

    public GameLoop(GameActivity activity)
    {
        this.activity = activity;
    }

    public void run()
    {
        LocalTime start;
        LocalTime end = LocalTime.now();

        while (play)
        {
            start = end;
            activity.updateAndDraw();
            end = LocalTime.now();
            long frameTime = ChronoUnit.MILLIS.between(start, end);
            deltaTimeS = frameTime / 1000f;
            deltaTimeMs = frameTime;
            // Log.log(this, "deltaTimeMs: " + deltaTimeMs);
            if (deltaTimeMs < 10)
            {
                try
                {
                    synchronized (this)
                    {
                        wait(10 - deltaTimeMs);
                    }
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
            BehaviorStarter.alertStart();
        }
    }
}
