package com.game.tama.anim;

import com.game.tama.util.Vec2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyFrameAssets
{
    public enum Name
    {
        AttackUni,
        MoveUni,
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

        frames.add(new KeyFrame(0.3f, new Vec2<>(50f, 50f)));
        assets.put(Name.AttackUni, new KeyFrameAnim(frames));

        frames.clear();
        frames.add(new KeyFrame(0, new Vec2<>(100f, 100f)));
        frames.add(new KeyFrame(1, new Vec2<>(0f, 0f)));
        assets.put(Name.MoveUni, new KeyFrameAnim(frames));
    }
}
