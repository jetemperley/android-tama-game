package com.tama.core;

import com.tama.util.Log;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

class GameLoop extends Thread
{
    /**
     * The time (ms) which the last frame took
     */
    public int frameTime = 0;
    GameActivity activity;
    public boolean play = true;
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
            frameTime =
                (int) ChronoUnit.MILLIS.between(start, end);
            deltaTime =  frameTime/1000f;
            Log.log(this,  "deltaTime: " + deltaTime);
            long waitTime = PetGame.gameSpeed - frameTime;
            try
            {
                if (waitTime > 0)
                {
                    Thread.currentThread().wait(waitTime);
                }
            }
            catch (Exception e) {}
        }
    }
}
