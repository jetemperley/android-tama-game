package com.tama.core;


public class GameManager
{
    public static GameManager INST;
    public PetGame game = null;
    public PauseMenu pauseMenu = new PauseMenu();
    public InputHandler target = new InputHandler(){};

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
