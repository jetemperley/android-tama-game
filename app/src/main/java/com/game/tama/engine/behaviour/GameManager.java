package com.game.tama.engine.behaviour;

import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.engine.gesture.EventPrioritySubscriber;
import com.game.engine.gesture.GestureEventSource;
import com.game.tama.core.World;
import com.game.tama.core.WorldFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameManager extends Behaviour
{
    public static GameManager INST;

    public PetGardenBehaviour gameBehaviour;
    public HeldThingBehaviour heldBehaviour;
    public MenuBehaviour pauseMenu;
    public MenuBehaviour hudMenu;

    private final GestureEventSource mainInput;
    private final EventPrioritySubscriber prioritySubscriber;
    private final Node mainGameNode;


    public GameManager(final Node parent, final GestureEventSource input)
    {
        super(parent);
        INST = this;
        mainInput = input;
        mainGameNode = new Node(parent);

        gameBehaviour = new PetGardenBehaviour(new Node(mainGameNode));
        hudMenu = BehaviourBuilder.buildHUD(new Node(mainGameNode));
        heldBehaviour = new HeldThingBehaviour(new Node(mainGameNode));
        // translate the game down below the hud button
        // gameBehaviour.node.localTransform.preTranslate(0, 1, 0);
        pauseMenu = BehaviourBuilder.buildPauseMenu(new Node(parent));

        prioritySubscriber = new EventPrioritySubscriber();
        prioritySubscriber.subscribe(heldBehaviour, 0);
        prioritySubscriber.subscribe(pauseMenu, 1);
        prioritySubscriber.subscribe(hudMenu, 1);
        prioritySubscriber.subscribe(gameBehaviour, 2);
        mainInput.setTarget(prioritySubscriber);

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

    public static HeldThingBehaviour getHeld()
    {
        return INST.heldBehaviour;
    }

    public static PetGardenBehaviour getGame()
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

    public Node getContainingNode(final float x, final float y)
    {
        if (hudMenu.isInside(x, y))
        {
            return hudMenu.node;
        }
        return gameBehaviour.node;
    }

    public void save(final ObjectOutputStream oos) throws IOException
    {
        oos.writeObject(gameBehaviour.world);
    }

    public void load(final ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        gameBehaviour.world = (World) in.readObject();
    }

    public void newGame()
    {
        gameBehaviour.world = WorldFactory.makeTestWorld();
    }

}
