package com.game.tama.core;

import com.game.android.DisplayAdapter;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.GestureEventHandler;

public class GameManager extends Behaviour
{
    public static GameManager INST;
    private PetGameBehaviour game;
    private PauseMenu pauseMenu;

    public GameManager(Node parent)
    {
        super(parent);
        INST = this;
        pauseMenu = new PauseMenu(new Node(parent));
        game = new PetGameBehaviour(new Node(parent));
    }

    public void play()
    {
        pauseMenu.enabled = false;
        game.enabled = true;
    }

    public void pause()
    {
        pauseMenu.enabled = true;
        game.enabled = false;
    }

    public void setGame(PetGame game)
    {
        this.game.petGame = game;
    }

    @Override
    public void draw(DisplayAdapter display)
    {

    }

    @Override
    public void update()
    {

    }
}
