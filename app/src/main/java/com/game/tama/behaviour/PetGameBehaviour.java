package com.game.tama.behaviour;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.PetGame;

public class PetGameBehaviour extends Behaviour
{
    public PetGame petGame;
    public MenuBehaviour thingMenu;

    public PetGameBehaviour(Node parent)
    {
        super(parent);
        petGame = new PetGame(this);
        parent.transform.setScale(6, 6);
        thingMenu = new MenuBehaviour(parent);
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

    @Override
    public boolean handle(GestureEvent event)
    {
        return petGame.handleEvent(event);
    }
}
