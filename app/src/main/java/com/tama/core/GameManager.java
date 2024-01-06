package com.tama.core;

import com.tama.gesture.GestureEvent;
import com.tama.gesture.GestureEventHandler;

public class GameManager implements GestureEventHandler
{
    public static GameManager INST;
    public PetGame game = null;
    public PauseMenu pauseMenu = new PauseMenu();
    public InputHandler target = new InputHandler()
    {
        @Override
        public void handleEvent(GestureEvent event) {}
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

    @Override
    public void handleEvent(GestureEvent event)
    {

    }
}
