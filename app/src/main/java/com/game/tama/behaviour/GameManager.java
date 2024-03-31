package com.game.tama.behaviour;

import com.game.android.gesture.EventPrioritySubscriber;
import com.game.android.gesture.GestureEventPipe;
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

    private GestureEventPipe mainInput;
    private EventPrioritySubscriber prioritySubscriber;

    public GameManager(Node parent, GestureEventPipe input)
    {
        super(parent);
        INST = this;
        mainInput = input;
        prioritySubscriber = new EventPrioritySubscriber(mainInput);

        gameBehaviour = new PetGameBehaviour(new Node(parent));
        pauseMenu = BehaviourBuilder.buildPauseMenu(new Node(parent));
        hudMenu = BehaviourBuilder.buildHUD(new Node(parent));

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
    public void update()
    {
        prioritySubscriber.update();
        Log.log(this, "updateing");
    }

}
