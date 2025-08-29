package com.game.tama.core;

import com.game.engine.KeyFrame;
import com.game.engine.KeyFrameAnim;
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

    public static KeyFrameAnim get(final Name name)
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
        assets.put(Name.AttackUni, createAttack());
        assets.put(Name.MoveUni, createMove());
    }

    private static KeyFrameAnim createAttack()
    {
        final List<KeyFrame<Vec2<Float>>> frames = new ArrayList<>();
        frames.add(new KeyFrame(0.3f, new Vec2<>(50f, 50f)));
        return new KeyFrameAnim(frames, null);
    }

    private static KeyFrameAnim createMove()
    {
        final List<KeyFrame<Vec2<Float>>> frames = new ArrayList<>();
        frames.add(new KeyFrame(0, new Vec2<>(100f, 100f)));
        frames.add(new KeyFrame(1, new Vec2<>(0f, 0f)));
        return new KeyFrameAnim(frames, null);
    }

    private static KeyFrameAnim createEmote()
    {
        final List<KeyFrame<Vec2<Float>>> frames = new ArrayList<>();
        return new KeyFrameAnim(null, null);
    }
}
