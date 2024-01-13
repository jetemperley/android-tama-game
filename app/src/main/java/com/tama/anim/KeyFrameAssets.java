package com.tama.anim;

import com.tama.util.Vec2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyFrameAssets
{
    public enum Name
    {
        AttackUni,
    }

    private static Map<Name, KeyFrameAnim> assets;

    public static KeyFrameAnim get(Name name)
    {
        if (assets == null)
        {
            init();
        }
        return assets.get(name);
    }

    private static void init()
    {
        assets = new HashMap<>();
        List<KeyFrame> frames = new ArrayList<>();

        frames.add(new KeyFrame(0.2f, new Vec2<>(0.5f, 0.5f)));
        assets.put(Name.AttackUni, new KeyFrameAnim(frames));
    }
}
