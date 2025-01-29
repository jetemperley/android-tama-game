package com.game.tama.engine.behaviour;

import android.graphics.Matrix;

import com.game.tama.core.WorldFactory;

public class PetGardenBehaviourConfigurer
{
    public static void testConfiguration(PetGardenBehaviour behaviour) {
        behaviour.node.localTransform.setScale(6, 6);
        // thingMenu = new MenuBehaviour(parent);
        behaviour.world = WorldFactory.makeTestWorld();

        Matrix uiMat = new Matrix();
        uiMat.setScale(6, 6);

        Matrix backpackMat = new Matrix();
        backpackMat.set(uiMat);
        backpackMat.preTranslate(16, 0);
    }
}
