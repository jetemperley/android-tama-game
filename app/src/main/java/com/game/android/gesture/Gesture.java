package com.game.android.gesture;

import android.view.MotionEvent;

import com.game.tama.util.Log;
import com.game.tama.util.Vec2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Gesture implements Input
{
    private GState[] state;
    private int stateidx = 0;

    int error = -1, wait = 0, down = 1, doubleTapDrag = 2,
        scale = 3, singleTap = 4, scroll = 5,
        doubleTap = 6;

    // pointer ids, (primary pointer is always at index 0)
    private int id2 = -1;
    // locations of the pointers
    // private Vec2<Float> prev1, prev2, new1, new2;

    private int singleTapConfirmDelay = 200;
    private int dragConfirmSensitivity = 2000;

    public Input gestureTarget;

    public float topOffset = 0;

    public Gesture()
    {
        state = new GState[7];
        state[0] = new Wait();
        state[1] = new Down();
        state[2] = new DoubleTapDrag();
        state[3] = new Scale();
        state[4] = new SingleTap();
        state[5] = new Scroll();
        state[6] = new DoubleTap();
    }

    public void update()
    {
        // Log.log("Game Gesture", "update");
        state[stateidx].update();
    }

    public boolean onTouchEvent(MotionEvent e)
    {
        stateidx = state[stateidx].onMotion(e);
        return true;
    }

    public void singleTapConfirmed(float x, float y)
    {
        Log.log(this, "single tap confirm");
        gestureTarget.singleTapConfirmed(x, y - topOffset);
    }

    public void singleDown(float x, float y)
    {
        Log.log(this, "long press confirm");
        gestureTarget.singleDown(x, y - topOffset);
    }

    public void longPressConfirmed(float x, float y)
    {
        Log.log(this, "long press confirm");
        gestureTarget.longPressConfirmed(x, y - topOffset);
    }

    public void doubleTapConfirmed(float x, float y)
    {
        Log.log(this, "double tap confirm");
        gestureTarget.doubleTapConfirmed(x, y - topOffset);
    }

    public void doubleTapRelease(float x, float y)
    {
        Log.log(this, "double tap release");
        gestureTarget.doubleTapRelease(x, y - topOffset);
    }

    public void doubleTapDragStart(float startX,
                                   float startY,
                                   float currentX,
                                   float currentY)
    {
        Log.log(this, "drag start");
        gestureTarget.doubleTapDragStart(
            startX,
            startY - topOffset,
            currentX,
            currentY - topOffset);
    }

    public void doubleTapDrag(float prevX,
                              float prevY,
                              float nextX,
                              float nextY)
    {
        Log.log(this, "drag confirm");
        gestureTarget.doubleTapDrag(
            prevX,
            prevY - topOffset,
            nextX,
            nextY - topOffset);
    }

    public void doubleTapDragEnd(float x, float y)
    {
        Log.log(this, "drag end");
        gestureTarget.doubleTapDragEnd(x, y - topOffset);
    }

    public void scale(Vec2<Float> p1,
                      Vec2<Float> p2,
                      Vec2<Float> n1,
                      Vec2<Float> n2)
    {
        Log.log(this, "scale confirm");
        p1.y -= topOffset;
        p2.y -= topOffset;
        n1.y -= topOffset;
        n2.y -= topOffset;

        gestureTarget.scale(p1, p2, n1, n2);
    }

    public void scroll(Vec2<Float> prev, Vec2<Float> next)
    {
        Log.log(this, "scroll confirm");
        prev.y -= topOffset;
        next.y -= topOffset;
        gestureTarget.scroll(prev, next);
    }

    abstract class GState
    {

        abstract void start(MotionEvent e);

        abstract int onMotion(MotionEvent e);

        void update()
        {
        }
    }

    private class Wait extends GState
    {

        void start(MotionEvent e)
        {
            //reset everything?
        }

        int onMotion(MotionEvent e)
        {
            if (e.getAction() == MotionEvent.ACTION_DOWN)
            {
                singleDown(e.getX(), e.getY());
                state[down].start(e);
                return down;
            }
            return wait;
        }
    }

    private class Down extends GState
    {

        LocalTime downTime;
        Vec2<Float> initialPos;

        Down()
        {
            initialPos = new Vec2(0, 0);
        }

        void start(MotionEvent e)
        {
            downTime = LocalTime.now();
            initialPos.set(e.getX(), e.getY());
        }

        void update()
        {
            if (ChronoUnit.MILLIS.between(downTime, LocalTime.now()) >
                singleTapConfirmDelay)
            {
                longPressConfirmed(initialPos.x, initialPos.y);
                // stateidx = wait;
            }
        }

        int onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:
                    if (Vec2.distSq(initialPos, new Vec2(e.getX(), e.getY())) >
                        dragConfirmSensitivity)
                    {
                        state[scroll].start(e);
                        return scroll;
                    }
                    return down;

                case MotionEvent.ACTION_UP:
                    if (ChronoUnit.MILLIS.between(downTime, LocalTime.now()) <
                        singleTapConfirmDelay)
                    {
                        state[singleTap].start(e);
                        return singleTap;
                    }
                    return wait;

                case MotionEvent.ACTION_POINTER_2_DOWN:
                    state[scale].start(e);
                    return scale;
            }
            return down;
        }
    }

    private class SingleTap extends GState
    {

        boolean still = false;
        LocalTime upTime;
        Vec2<Float> loc;

        SingleTap()
        {
            loc = new Vec2(0, 0);
        }

        void start(MotionEvent e)
        {

            upTime = LocalTime.now();
            loc.set(e.getX(), e.getY());
        }

        void update()
        {
            if (ChronoUnit.MILLIS.between(upTime, LocalTime.now()) >
                singleTapConfirmDelay)
            {
                singleTapConfirmed(loc.x, loc.y);
                stateidx = wait;
            }
        }

        int onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_DOWN:
                    if (ChronoUnit.MILLIS.between(upTime, LocalTime.now()) <
                        singleTapConfirmDelay)
                    {
                        state[doubleTap].start(e);
                        return doubleTap;
                    }
                    return down;
            }
            return wait;
        }
    }

    private class DoubleTapDrag extends GState
    {
        Vec2<Float> previous = new Vec2<>(0f, 0f);

        void start(MotionEvent e)
        {
            previous.set(e.getX(), e.getY());
        }

        int onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {
                case MotionEvent.ACTION_MOVE:
                    doubleTapDrag(previous.x, previous.y, e.getX(), e.getY());
                    previous.set(e.getX(), e.getY());
                    return doubleTapDrag;

                case MotionEvent.ACTION_UP:
                    doubleTapDragEnd(e.getX(), e.getY());
                    return wait;
            }
            return doubleTapDrag;
        }
    }

    private class DoubleTap extends GState
    {

        Vec2<Float> loc;
        LocalTime downTime = null;

        DoubleTap()
        {
            loc = new Vec2(0, 0);
        }

        void start(MotionEvent e)
        {
            downTime = LocalTime.now();
            loc.set(e.getX(), e.getY());
        }

        int onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:
                    if (Vec2.distSq(loc, new Vec2(e.getX(), e.getY())) >
                        dragConfirmSensitivity)
                    {
                        doubleTapDragStart(loc.x, loc.y, e.getX(), e.getY());
                        state[doubleTapDrag].start(e);
                        return doubleTapDrag;
                    }
                    return doubleTap;

                case MotionEvent.ACTION_UP:
                    if (ChronoUnit.MILLIS.between(downTime, LocalTime.now()) <
                        singleTapConfirmDelay)
                    {
                        doubleTapConfirmed(e.getX(), e.getY());
                        return wait;
                    }
                    doubleTapRelease(e.getX(), e.getY());
                    return wait;

                // pretty sure this is not meant to be here
                //                case 261:
                //                    state[doubleTap].start(e);
                //                    return doubleTap;

            }
            return doubleTap;
        }
    }

    private class Scroll extends GState
    {

        Vec2<Float> prev, next;

        Scroll()
        {
            prev = new Vec2(0, 0);
            next = new Vec2(0, 0);
        }

        void start(MotionEvent e)
        {
            prev.set(next);
            next.set(e.getX(), e.getY());
        }

        int onMotion(MotionEvent e)
        {
            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:
                    start(e);
                    scroll(new Vec2<>(prev), new Vec2<>(next));
                    return scroll;

                case MotionEvent.ACTION_UP:
                    return wait;

                case MotionEvent.ACTION_POINTER_2_DOWN:

                    state[scale].start(e);
                    return scale;
            }
            return scroll;
        }
    }

    private class Scale extends GState
    {

        int point2ID = -1;
        Vec2<Float> next1, next2, prev1, prev2;

        Scale()
        {
            prev1 = new Vec2(0, 0);
            prev2 = new Vec2(0, 0);
            next1 = new Vec2(0, 0);
            next2 = new Vec2(0, 0);
        }

        void start(MotionEvent e)
        {
            if (point2ID == -1)
            {
                point2ID = e.getPointerId(1);
            }
            int idx = e.findPointerIndex(point2ID);
            if (idx != -1)
            {
                prev1.set(next1);
                next1.set(e.getX(), e.getY());
                prev2.set(next2);
                next2.set(e.getX(idx), e.getY(idx));
            }
        }

        int onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:

                    start(e);
                    scale(
                        new Vec2<>(prev1),
                        new Vec2<>(prev2),
                        new Vec2<>(next1),
                        new Vec2<>(next2));
                    return scale;

                case MotionEvent.ACTION_UP:
                    point2ID = -1;
                    return wait;

                case MotionEvent.ACTION_POINTER_2_UP:
                    point2ID = -1;
                    state[scroll].start(e);
                    return scroll;
            }
            return wait;
        }
    }
}
