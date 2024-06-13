package com.game.tama.behaviour;

import android.graphics.Matrix;

import com.game.android.gesture.EventPrioritySubscriber;
import com.game.android.gesture.GestureEventSource;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.GameLoop;

public class GameManager extends Behaviour
{
    public static GameManager INST;
    public PetGameBehaviour gameBehaviour;
    public MenuBehaviour pauseMenu;
    public MenuBehaviour hudMenu;
    public HeldThingBehaviour heldBehaviour;

    private GestureEventSource mainInput;
    private EventPrioritySubscriber prioritySubscriber;
    private Node mainGameNode;

    /** The amount of ms this game has been running for. */
    public static long time = 0;

    public GameManager(Node parent, GestureEventSource input)
    {
        super(parent);
        INST = this;
        mainInput = input;
        mainGameNode = new Node(parent);

        gameBehaviour = new PetGameBehaviour(new Node(mainGameNode));
        heldBehaviour = new HeldThingBehaviour(new Node(mainGameNode));
        hudMenu = BehaviourBuilder.buildHUD(new Node(mainGameNode));
        pauseMenu = BehaviourBuilder.buildPauseMenu(new Node(parent));

        prioritySubscriber = new EventPrioritySubscriber();
        mainInput.setTarget(prioritySubscriber);
        prioritySubscriber.subscribe(heldBehaviour, 0);
        prioritySubscriber.subscribe(pauseMenu, 1);
        prioritySubscriber.subscribe(hudMenu, 1);
        prioritySubscriber.subscribe(gameBehaviour, 2);

        play();
    }

    public void play()
    {
        mainGameNode.setEnabled(true);
        pauseMenu.setEnabled(false);
    }

    public void pause()
    {
        mainGameNode.setEnabled(false);
        pauseMenu.setEnabled(true);
    }

    @Override
    public void update()
    {
        // TODO fix this time unit mismatch
        time += GameLoop.deltaTime;
    }

    public static HeldThingBehaviour getHeld()
    {
        return INST.heldBehaviour;
    }

    public static PetGameBehaviour getGame()
    {
        return INST.gameBehaviour;
    }

    public static MenuBehaviour getHud()
    {
        return INST.hudMenu;
    }

    public static MenuBehaviour getPauseMenu()
    {
        return INST.pauseMenu;
    }

    public Node getContainingNode(float x, float y)
    {
        if (hudMenu.isInside(x, y))
        {
            return hudMenu.node;
        }
        return gameBehaviour.node;
    }
}
