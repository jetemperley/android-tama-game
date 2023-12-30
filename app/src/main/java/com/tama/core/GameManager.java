package com.tama.core;

import com.tama.util.GestureTargetPipe;

public class GameManager
{
    public PetGame game = null;
    public PauseMenu pauseMenu = new PauseMenu();
    public ScreenTarget target = new ScreenTarget(){};

    public void updateAndDraw(DisplayAdapter display)
    {
        gesture.update();
        game.update();
        game.draw(display);
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
