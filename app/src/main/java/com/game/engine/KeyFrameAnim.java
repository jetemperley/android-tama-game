package com.game.engine;

import com.game.tama.util.Vec2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KeyFrameAnim implements Serializable
{
    private final List<KeyFrame<Vec2<Float>>> posKeyFrames = new ArrayList<>();
    private final List<KeyFrame<Integer>> animKeyFrames = new ArrayList<>();

    /**
     * Optimization params, keep track of most recent (lower) frame and check
     * this first.
     */
    private final int recentFrame = 0;

    public KeyFrameAnim(final List<KeyFrame<Vec2<Float>>> posKeyFrameList,
                        final List<KeyFrame<Integer>> animKeyFrameList)
    {
        if (posKeyFrameList != null)
        {
            posKeyFrames.addAll(posKeyFrameList);
        }
        formatPosKeyFrames();
        if (animKeyFrameList != null)
        {
            animKeyFrames.addAll(animKeyFrameList);
        }
    }

    private void formatPosKeyFrames()
    {

        // add a starting time key frame if one does not exist
        if (posKeyFrames.isEmpty() || posKeyFrames.get(0).time != 0)
        {
            posKeyFrames.add(0, new KeyFrame(0, new Vec2(0f, 0f)));
        }

        // add an ending time key frame if one does not exist
        if (posKeyFrames.get(posKeyFrames.size() - 1).time != 1)
        {
            posKeyFrames.add(new KeyFrame(1, new Vec2(0f, 0f)));
        }

        // check the key frames are in order, exception otherwise
        for (int i = 0; i < posKeyFrames.size() - 1; i++)
        {
            if (posKeyFrames.get(i).time >= posKeyFrames.get(i + 1).time)
            {
                throw new RuntimeException(
                    "Keyframes (size=" + posKeyFrames.size() + ") were in the wrong order: frame "
                        + i
                        + " time=" +
                        posKeyFrames.get(i).time + ", frame " + (i + 1) + " time=" +
                        posKeyFrames.get(i + 1).time + ".");
            }
        }
    }

    public Vec2<Float> getPosition(final float time)
    {
        if (time < 0 || time > 1)
        {
            throw new RuntimeException(
                "Time must be normalized (0 - 1): requested time = " + time);
        }

        int i = 0;
        while (posKeyFrames.get(i).time < time)
        {i++;}

        final KeyFrame<Vec2<Float>> upperFrame = posKeyFrames.get(i);
        if (upperFrame.time == time)
        {
            return upperFrame.frame;
        }

        final KeyFrame<Vec2<Float>> lowerFrame = posKeyFrames.get(i - 1);
        // Log.log(this, "Time: " + time + ", lower=" + lowerFrame.time);

        final float frameTime = (time - lowerFrame.time) /
            (upperFrame.time - lowerFrame.time);

        final float xDiff = (upperFrame.frame.x - lowerFrame.frame.x) * frameTime;
        final float yDiff = (upperFrame.frame.y - lowerFrame.frame.y) * frameTime;

        return new Vec2<Float>(
            lowerFrame.frame.x + xDiff,
            lowerFrame.frame.y + yDiff);
    }
}
