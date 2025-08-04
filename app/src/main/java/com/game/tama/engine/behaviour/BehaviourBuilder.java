package com.game.tama.engine.behaviour;

import android.graphics.Matrix;

import com.game.engine.Node;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.world.WorldFactory;
import com.game.tama.ui.DialogueTextBoxLeaf;
import com.game.tama.ui.SquareCellButtonLeaf;
import com.game.tama.ui.TextLeaf;
import com.game.tama.ui.UINode;

public class BehaviourBuilder
{

    public static MenuBehaviour buildPauseMenu(final Node parent)
    {
        final MenuBehaviour pauseMenuBehaviour = new MenuBehaviour(parent);
        final UINode pauseMenu = new UINode();
        pauseMenu.add("play button", new SquareCellButtonLeaf(
            0,
            0,
            Asset.sprites.get(AssetName.static_menu),
            () -> GameManager.INST.play()));
        pauseMenu.add("test text", new TextLeaf("a hello z", 0, 1));
        pauseMenu.add("test text 2", new TextLeaf("aBz, cool.", 0, 2));
        pauseMenu.add("text box", new DialogueTextBoxLeaf(
            0,
            70,
            10,
            3,
            "Ayee this is some text that is gonna be in a text box and " +
                "hopefullly its just gonna work the first time.!!"));
        // parent.localTransform.setScale(6, 6);
        pauseMenuBehaviour.root = pauseMenu;
        return pauseMenuBehaviour;
    }

    public static MenuBehaviour buildHUD(final Node parent)
    {
        final MenuBehaviour hud = new MenuBehaviour(parent);
        final UINode hudRoot = new UINode();
        hudRoot.add("pause", new SquareCellButtonLeaf(
            0,
            0,
            Asset.sprites.get(AssetName.static_menu),
            () -> GameManager.INST.pause()));
        parent.localTransform.setScale(1f / 6, 1f / 6);
        hud.root = hudRoot;
        return hud;
    }

    public static void testConfiguration(final PetGardenBehaviour behaviour)
    {
        behaviour.node.localTransform.setScale(1f / 6, 1f / 6);
        // thingMenu = new MenuBehaviour(parent);
        behaviour.world = WorldFactory.makeTestWorld();

        final Matrix uiMat = new Matrix();
        uiMat.setScale(6, 6);

        // Matrix backpackMat = new Matrix();
        // backpackMat.set(uiMat);
        // backpackMat.preTranslate(1, 0);
    }
}
