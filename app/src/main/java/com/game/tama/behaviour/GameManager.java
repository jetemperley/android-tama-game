package com.game.tama.behaviour;

import com.game.android.gesture.EventPrioritySubscriber;
import com.game.android.gesture.GestureEventSource;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.PetGame;

public class GameManager extends Behaviour
{
    public static GameManager INST;
    public PetGameBehaviour gameBehaviour;
    public MenuBehaviour pauseMenu;
    public MenuBehaviour hudMenu;

    private GestureEventSource mainInput;
    private EventPrioritySubscriber prioritySubscriber;

    public GameManager(Node parent, GestureEventSource input)
    {
        super(parent);
        INST = this;
        mainInput = input;

        gameBehaviour = new PetGameBehaviour(new Node(parent));
        pauseMenu = BehaviourBuilder.buildPauseMenu(new Node(parent));
        hudMenu = BehaviourBuilder.buildHUD(new Node(parent));

        prioritySubscriber = new EventPrioritySubscriber();
        mainInput.setTarget(prioritySubscriber);
        prioritySubscriber.subscribe(pauseMenu, 1);
        prioritySubscriber.subscribe(hudMenu, 1);
        prioritySubscriber.subscribe(gameBehaviour, 2);

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
    public void update() {}
}
