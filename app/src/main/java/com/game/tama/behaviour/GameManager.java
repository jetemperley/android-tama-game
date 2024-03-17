package com.game.tama.behaviour;

import com.game.android.gesture.GesturePrioritySubscriber;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.PetGame;
import com.game.tama.util.Log;

public class GameManager extends Behaviour
{
    public static GameManager INST;
    private PetGameBehaviour gameBehaviour;
    private Menu pauseMenu;
    private Menu hudMenu;

    public GameManager(Node parent)
    {
        super(parent);
        INST = this;
        pauseMenu = BehaviourBuilder.buildPauseMenu(new Node(parent));
        hudMenu = BehaviourBuilder.buildHUD(new Node(parent));
        gameBehaviour = new PetGameBehaviour(new Node(parent));
        GesturePrioritySubscriber.subscribe(pauseMenu, 1);
        GesturePrioritySubscriber.subscribe(hudMenu, 1);
        GesturePrioritySubscriber.subscribe(gameBehaviour, 2);
        play();
    }

    public void play()
    {
        gameBehaviour.setEnabled(true);
        hudMenu.setEnabled(true);
        pauseMenu.setEnabled(false);
    }

    public void pause()
    {
        gameBehaviour.setEnabled(false);
        hudMenu.setEnabled(false);
        pauseMenu.setEnabled(true);
    }

    public void setGame(PetGame game)
    {
        this.gameBehaviour.petGame = game;
    }

    @Override
    public void update()
    {
        Log.log(this, "updateing");
    }

}
