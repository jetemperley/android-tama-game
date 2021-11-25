package com.tama.apptest;
import java.util.TimerTask;

public class GameLoop extends TimerTask {

    GameActivity g;
    static int timer, period;


    boolean running = true;
    GameLoop(GameActivity g){
        super();
        this.g = g;
        timer = 0;
        period = 25;
    }

    public void run(){
        while (running){
            g.draw();
            timer+= period;
        }
    }
}
