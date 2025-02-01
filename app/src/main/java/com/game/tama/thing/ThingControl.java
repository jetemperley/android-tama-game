package com.game.tama.thing;

import com.game.tama.core.Assets;
import com.game.tama.core.Sprite;

import java.util.HashMap;

public class ThingControl
{
    public static HashMap<Name, ThingControl> controls = new HashMap<>();

    public enum Name
    {
        move
    }

    static
    {
        controls.put(
            Name.move,
            new ThingControl(Name.move,
                Assets.Names.static_arrow_corner,
                null));
    }

    public final Name controlName;
    public final Assets.Names assetName;
    public final ThingControlLambda func;

    private ThingControl(Name controlName,
                         Assets.Names assetName,
                         ThingControlLambda func)
    {
        this.controlName = controlName;
        this.assetName = assetName;
        this.func = func;
    }
}
