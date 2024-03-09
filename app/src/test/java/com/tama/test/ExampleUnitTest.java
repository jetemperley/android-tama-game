package com.tama.test;

import com.game.tama.anim.KeyFrame;
import com.game.tama.anim.KeyFrameAnim;
import com.game.tama.anim.KeyFrameAssets;
import com.game.tama.core.DialogueTextBox;
import com.game.tama.util.Vec2;

import org.junit.Assert;
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

    @Test
    public void dialogueLines()
    {
        String text =
            "This is some text that should be split into smaller lines";
        List<String> actual = DialogueTextBox.splitIntoLines(text, 10);
        Assert.assertEquals(7, actual.size());
        Assert.assertEquals("This is", actual.get(0));
        Assert.assertEquals("some text", actual.get(1));
        Assert.assertEquals("that", actual.get(2));
        Assert.assertEquals("should be", actual.get(3));
        Assert.assertEquals("split into", actual.get(4));
        Assert.assertEquals("smaller", actual.get(5));
        Assert.assertEquals("lines", actual.get(6));
    }

    @Test
    public void dialogueLines1()
    {
        String text =
            "123456789";
        List<String> actual = DialogueTextBox.splitIntoLines(text, 6);
        Assert.assertEquals(2, actual.size());
        Assert.assertEquals("12345-", actual.get(0));
        Assert.assertEquals("6789", actual.get(1));
    }
}