package com.game.engine;

import com.game.android.GameActivity;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class GameLoop extends Thread
{
    GameActivity activity;
    public boolean play = true;

    /** The time (seconds) which the last frame took. */
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
            long frameTime =  ChronoUnit.MILLIS.between(start, end);
            deltaTimeS =  frameTime/1000f;
            deltaTimeMs =  frameTime;
//            Log.log(this,  "deltaTime: " + deltaTime);
            BehaviorStarter.alertStart();
        }
    }
}
