package com.tama.core;

import com.tama.gesture.GestureEvent;

public class GameManager
{
    public static GameManager INST;
    public PetGame game = null;
    public PauseMenu pauseMenu = new PauseMenu();
    public Interactive target = new Interactive()
    {
        @Override
        public boolean handleEvent(GestureEvent event) {return true;}
    };

    public GameManager()
    {
        INST = this;
    }

    public void updateAndDraw(DisplayAdapter display)
    {
        target.update();
        target.draw(display);
    }

    public void play()
    {
        target = game;
    }

    public void pause()
    {
        target = pauseMenu;
    }
}
