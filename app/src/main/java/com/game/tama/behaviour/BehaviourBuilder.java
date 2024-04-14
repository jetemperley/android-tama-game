package com.game.tama.behaviour;

import com.game.engine.Node;
import com.game.tama.core.Assets;
import com.game.tama.ui.CircleCellButtonLeaf;
import com.game.tama.ui.SquareCellButtonLeaf;
import com.game.tama.ui.DialogueTextBoxLeaf;
import com.game.tama.ui.TextLeaf;
import com.game.tama.ui.UINode;

public class BehaviourBuilder
{

    public static MenuBehaviour buildPauseMenu(Node parent)
    {
        MenuBehaviour pauseMenuBehaviour = new MenuBehaviour(parent);
        UINode pauseMenu = new UINode();
        pauseMenu.add("play button", new SquareCellButtonLeaf(
            50,
            50,
            Assets.Names.static_poop.name(),
            () -> GameManager.INST.play()));
        pauseMenu.add("test text", new TextLeaf("a hello z", 0, 0));
        pauseMenu.add("test text 2", new TextLeaf("aBz, cool.", 0, 8));
        pauseMenu.add("text box", new DialogueTextBoxLeaf(
            0,
            70,
            10,
            3,
            "Ayee this is some text that is gonna be in a text box and " +
                "hopefullly its just gonna work the first time.!!"));
        parent.transform.setScale(6, 6);
        pauseMenu.add("circle button", new CircleCellButtonLeaf(
            0,
            0,
            Assets.Names.static_x.toString(),
            CircleCellButtonLeaf.Size.p14));
        pauseMenuBehaviour.root = pauseMenu;
        return pauseMenuBehaviour;
    }

    public static MenuBehaviour buildHUD(Node parent)
    {
        MenuBehaviour hud = new MenuBehaviour(parent);
        UINode hudRoot = new UINode();
        hudRoot.add("pause", new SquareCellButtonLeaf(
            0,
            0,
            Assets.Names.static_poop.name(),
            () -> GameManager.INST.pause()));
        parent.transform.setScale(6, 6);
        hud.root = hudRoot;
        return hud;
    }
}
