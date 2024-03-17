package com.game.tama.behaviour;

import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.Assets;
import com.game.tama.ui.Button;
import com.game.tama.ui.DialogueTextBox;
import com.game.tama.ui.Text;

public class BehaviourBuilder
{

    public static Node buildPetGameNode()
    {
        return null;
    }

    public static Menu buildPauseMenu(Node parent)
    {
        Menu pauseMenu = new Menu(parent);
        pauseMenu.add(new Button(
            50,
            50,
            Assets.Names.static_poop.name(),
            () -> GameManager.INST.play()));
        pauseMenu.add(new Text("a hello z", 0, 0));
        pauseMenu.add(new Text("aBz, cool.", 0, 8));
        pauseMenu.add(new DialogueTextBox(
            0,
            70,
            10,
            3,
            "Ayee this is some text that is gonna be in a text box and " +
                "hopefullly its just gonna work the first time.!!"));
        return pauseMenu;
    }

    public static Menu buildHUD(Node parent)
    {
        Menu hud = new Menu(parent);
        hud.add(new Button(
            50,
            50,
            Assets.Names.static_poop.name(),
            () -> GameManager.INST.pause()));
        return hud;
    }
}
