package com.game.tama.behaviour;

import android.os.Debug;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.GesturePrioritySubscriber;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.PetGame;
import com.game.tama.util.Log;

public class PetGameBehaviour extends Behaviour
{
    public PetGame petGame;
    public PetGameBehaviour(Node parent)
    {
        super(parent);
        petGame = new PetGame(this);
        parent.transform.setScale(6, 6);
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        Log.log(this, "drawing");
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
