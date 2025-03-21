package com.game.tama.thing;

import com.game.tama.core.World;

public interface ThingControlLambda
{
    /**
     * Apply the control action to thing t, in world w, at array position x, y
     * @param thing selected thing
     * @param world world where the action occurred
     * @param x array position of the action target
     * @param y array position of the action target
     */
    public void execute(Thing thing, World world, float x, float y);
}
