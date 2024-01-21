package com.tama.apptest;

import com.tama.anim.KeyFrame;
import com.tama.anim.KeyFrameAnim;
import com.tama.anim.KeyFrameAssets;
import com.tama.util.Vec2;

import org.jetbrains.annotations.TestOnly;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine
 * (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
    @Test
    public void attackAnimTest()
    {
        KeyFrameAnim attack = KeyFrameAssets.get(KeyFrameAssets.Name.AttackUni);
    }

    @Test
    public void animTest1()
    {
        List<KeyFrame> frames = new ArrayList<>();
        frames.add(new KeyFrame(0, new Vec2<>(0f, 0f)));
        frames.add(new KeyFrame(1, new Vec2<>(1f, 1f)));

        KeyFrameAnim anim = new KeyFrameAnim(frames);
        assertEquals(new Vec2<>(0f, 0f), anim.getPosition(0));
        assertEquals(new Vec2<>(1f, 1f), anim.getPosition(1));
        assertEquals(new Vec2<>(0.5f, 0.5f), anim.getPosition(0.5f));
    }

    @Test
    public void animTest2()
    {
        List<KeyFrame> frames = new ArrayList<>();
        frames.add(new KeyFrame(0, new Vec2<>(0f, 0f)));
        frames.add(new KeyFrame(0.5f, new Vec2<>(1f, 1f)));
        frames.add(new KeyFrame(1f, new Vec2<>(0f, 0f)));

        KeyFrameAnim anim = new KeyFrameAnim(frames);
        assertEquals(new Vec2<>(0f, 0f), anim.getPosition(0f));
        assertEquals(new Vec2<>(1f, 1f), anim.getPosition(0.5f));
        assertEquals(new Vec2<>(0f, 0f), anim.getPosition(1f));
    }

    @Test
    public void vec2Test()
    {
        assertEquals(new Vec2<Float>(0f, 0f), new Vec2<Float>(0f, 0f));
        assertNotEquals(new Vec2<Float>(0f, 1f), new Vec2<Float>(0f, 0f));
        assertNotEquals(new Vec2<Float>(0f, 1f), new Vec2<Float>(1f, 0f));

    }
}