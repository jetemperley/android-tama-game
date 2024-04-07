package com.game.tama.behaviour;

import com.game.engine.Node;
import com.game.tama.core.Assets;
import com.game.tama.ui.CircleCellButtonLeaf;
import com.game.tama.ui.SquareCellButtonLeaf;
import com.game.tama.ui.DialogueTextBoxLeaf;
import com.game.tama.ui.Text;

public class BehaviourBuilder
{

    public static Node buildPetGameNode()
    {
        return null;
    }

    public static MenuBehaviour buildPauseMenu(Node parent)
    {
        MenuBehaviour pauseMenu = new MenuBehaviour(parent);
        pauseMenu.add(new SquareCellButtonLeaf(
            50,
            50,
            Assets.Names.static_poop.name(),
            () -> GameManager.INST.play()));
        pauseMenu.add(new Text("a hello z", 0, 0));
        pauseMenu.add(new Text("aBz, cool.", 0, 8));
        pauseMenu.add(new DialogueTextBoxLeaf(
            0,
            70,
            10,
            3,
            "Ayee this is some text that is gonna be in a text box and " +
                "hopefullly its just gonna work the first time.!!"));
        parent.transform.setScale(6, 6);
        pauseMenu.add(new CircleCellButtonLeaf(
            0,
            0,
            Assets.Names.static_x.toString(),
            CircleCellButtonLeaf.Size.p14));

        return pauseMenu;
    }

    public static MenuBehaviour buildHUD(Node parent)
    {
        MenuBehaviour hud = new MenuBehaviour(parent);
        hud.add(new SquareCellButtonLeaf(
            0,
            0,
            Assets.Names.static_poop.name(),
            () -> GameManager.INST.pause()));
        parent.transform.setScale(6, 6);
        return hud;
    }
}
