package com.tama.anim;

import com.tama.util.Log;
import com.tama.util.Vec2;

import java.util.ArrayList;
import java.util.List;

public class KeyFrameAnim
{
    private List<KeyFrame> keyFrames = new ArrayList<>();

    /**
     * Optimization params, keep track of most recent (lower) frame and check
     * this first.
     */
    private int recentFrame = 0;

    public KeyFrameAnim(List<KeyFrame> keyFrameList)
    {

        for (KeyFrame frame : keyFrameList)
        {
            keyFrames.add(frame);
        }

        if (keyFrames.size() == 0)
        {
            throw new RuntimeException(
                "An animation was specified with no frames.");
        }

        if (keyFrames.get(0).time != 0)
        {
            keyFrames.add(0, new KeyFrame(0, new Vec2<Float>(0f, 0f)));
        }

        if (keyFrames.get(keyFrames.size() - 1).time != 1)
        {
            keyFrames.add(new KeyFrame(1, new Vec2<Float>(0f, 0f)));
        }

        for (int i = 0; i < keyFrames.size() - 1; i++)
        {
            if (keyFrames.get(i).time >= keyFrames.get(i + 1).time)
            {
                throw new RuntimeException(
                    "Keyframes (size=" + keyFrames.size() + ") were in the wrong order: frame " + i + " time=" +
                        keyFrames.get(i).time + ", frame " + (i + 1) + " time=" +
                        keyFrames.get(i+1).time + ".");
            }
        }

        this.keyFrames = keyFrames;
    }

    public Vec2<Float> getPosition(float time)
    {
        if (time < 0 || time > 1)
        {
            throw new RuntimeException(
                "Time must be normalized (0 - 1): requested time = " + time);
        }

        int i = 0;
        while (keyFrames.get(i).time  < time) i++;

        KeyFrame upperFrame = keyFrames.get(i);
        if (upperFrame.time == time)
        {
            return upperFrame.pos;
        }

        KeyFrame lowerFrame = keyFrames.get(i - 1);
        // Log.log(this, "Time: " + time + ", lower=" + lowerFrame.time);

        float frameTime = (time - lowerFrame.time) /
            (upperFrame.time - lowerFrame.time);


        float xDiff = (upperFrame.pos.x - lowerFrame.pos.x) * frameTime;
        float yDiff = (upperFrame.pos.y - lowerFrame.pos.y) * frameTime;

        return new Vec2<Float>(
            lowerFrame.pos.x + xDiff,
            lowerFrame.pos.y + yDiff);
    }
}
