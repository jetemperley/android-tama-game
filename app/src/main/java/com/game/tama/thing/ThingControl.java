package com.game.tama.thing;

import com.game.tama.core.Sprite;

public class ThingControl
{
    public final Sprite sprite;
    public final ThingControlLambda func;

    public ThingControl(Sprite sprite, ThingControlLambda func)
    {
        this.sprite = sprite;
        this.func = func;
    }
}
