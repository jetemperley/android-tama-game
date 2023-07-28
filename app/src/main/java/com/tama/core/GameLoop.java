package com.tama.core;

import java.util.TimerTask;

public class GameLoop extends TimerTask
{

    GameActivity g;
    long timer;
    int period;
    boolean running = true;

    GameLoop(GameActivity g, int period)
    {
        super();
        this.g = g;
        timer = 0;
        this.period = period;
    }

    public void run()
    {
        while (running)
        {
            timer += period;
            g.draw();

        }
    }

    public long runtime()
    {
        return timer;
    }
}
