package com.game.tama.core;

import com.game.android.DisplayAdapter;
import com.game.engine.Behaviour;
import com.game.engine.Node;

public class PetGameBehaviour extends Behaviour
{

    PetGame petGame;

    public PetGameBehaviour(Node parent)
    {
        super(parent);
        petGame = new PetGame();

    }

    @Override
    public void draw(DisplayAdapter display)
    {
        petGame.draw(display);
    }

    @Override
    public void update()
    {
        petGame.update();
    }
}
