package com.tama.util;

import android.view.MotionEvent;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Gesture
{

    private GState[] state;
    private int stateidx = 0;

    int error = -1, wait = 0, down = 1, doubleTapDrag = 2,
            scale = 3, singleTap = 4, scroll = 5,
            doubleTap = 6;

    // pointer ids, (primary pointer is always at index 0)
    private int id2 = -1;
    // locations of the pointers
    private Vec2<Float> prev1, prev2, new1, new2;

    private int singleTapConfirmDelay = 100;
    private int dragConfirmSensitivity = 2000;

    public Gesture()
    {
        prev1 = new Vec2(0, 0);
        prev2 = new Vec2(0, 0);
        new1 = new Vec2(0, 0);
        new2 = new Vec2(0, 0);

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

        // Log.log("Game Gesture", "event " + e.getAction());
        stateidx = state[stateidx].onMotion(e);
        // Log.log("Game Gesture", "state " + stateidx);

        return true;
    }

    public void singleTapConfirmed(float x, float y)
    {
        Log.log(this, "single tap confirm");
    }

    public void singleDown(float x, float y)
    {
        Log.log(this, "long press confirm");
    }

    public void longPressConfirmed(float x, float y)
    {
        Log.log(this, "long press confirm");
    }

    public void doubleTapConfirmed(MotionEvent e)
    {
        Log.log(this, "double tap confirm");
    }

    public void doubleTapRelease(float x, float y)
    {
        Log.log(this, "double tap release");
    }

    public void doubleTapDragStart(MotionEvent e)
    {
        Log.log(this, "drag start");
    }

    public void doubleTapDrag(float x, float y)
    {
        Log.log(this, "drag confirm");
    }

    public void doubleTapDragEnd(float x, float y)
    {
        Log.log(this, "drag end");
    }

    public void scale(Vec2<Float> p1, Vec2<Float> p2, Vec2<Float> n1, Vec2<Float> n2)
    {
        Log.log(this, "scale confirm");
    }

    public void scroll(Vec2<Float> prev, Vec2<Float> next)
    {
        Log.log(this, "scroll confirm");
    }

    abstract class GState
    {

        abstract void start(MotionEvent e);

        abstract int onMotion(MotionEvent e);

        void update()
        {
        }
    }

    class Wait extends GState
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

    class Down extends GState
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

    class SingleTap extends GState
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
            if (ChronoUnit.MILLIS.between(upTime, LocalTime.now()) > singleTapConfirmDelay)
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

    class DoubleTapDrag extends GState
    {

        void start(MotionEvent e)
        {
            doubleTapDragStart(e);
        }

        int onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:
                    doubleTapDrag(e.getX(), e.getY());
                    return doubleTapDrag;

                case MotionEvent.ACTION_UP:
                    doubleTapDragEnd(e.getX(), e.getY());
                    return wait;
            }
            return doubleTapDrag;
        }
    }

    class DoubleTap extends GState
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
                        doubleTapDragStart(e);
                        state[doubleTapDrag].start(e);
                        return doubleTapDrag;
                    }
                    return doubleTap;

                case MotionEvent.ACTION_UP:
                    if (ChronoUnit.MILLIS.between(downTime, LocalTime.now()) <
                        singleTapConfirmDelay)
                    {
                        doubleTapConfirmed(e);
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

    class Scroll extends GState
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
                    scroll(prev, next);
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

    class Scale extends GState
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
                    scale(prev1, prev2, next1, next2);
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
