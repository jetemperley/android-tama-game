package com.game.tama.thing;

import com.game.tama.core.AssetName;

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
                AssetName.static_footprints,
                null));
    }

    public final Name controlName;
    public final AssetName assetName;
    public final ThingControlLambda func;

    private ThingControl(Name controlName,
                         AssetName assetName,
                         ThingControlLambda func)
    {
        this.controlName = controlName;
        this.assetName = assetName;
        this.func = func;
    }
}
