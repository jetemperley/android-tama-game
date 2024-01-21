package com.tama.core;

import com.tama.util.Log;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class GameLoop extends Thread
{
    GameActivity activity;
    public boolean play = true;

    /** The time (ms) which the last frame took. */
    public static float deltaTime = 0;

    GameLoop(GameActivity activity)
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
            long frameTime =  ChronoUnit.MILLIS.between(start, end);
            deltaTime =  frameTime/1000f;
            Log.log(this,  "deltaTime: " + deltaTime);
        }
    }
}
