package com.tama.core;

import android.util.Log;
import android.view.MotionEvent;

import com.tama.util.Vec2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

class Gesture
{

    private GState[] state;
    private int stateidx = 0;

    int error = -1, wait = 0, down = 1, drag = 2,
            scale = 3, singleTap = 4, scroll = 5,
            doubleTap = 6;

    // pointer ids, (primary pointer is always at index 0)
    private int id2 = -1;
    // locations of the pointers
    private Vec2<Float> prev1, prev2, new1, new2;


    Gesture()
    {
        prev1 = new Vec2(0, 0);
        prev2 = new Vec2(0, 0);
        new1 = new Vec2(0, 0);
        new2 = new Vec2(0, 0);

        state = new GState[7];
        state[0] = new Wait();
        state[1] = new Down();
        state[2] = new Drag();
        state[3] = new Scale();
        state[4] = new SingleTap();
        state[5] = new Scroll();
        state[6] = new DoubleTap();

    }

    void update()
    {
        // Log.d("Game Gesture", "update");
        state[stateidx].update();
    }

    public boolean onTouchEvent(MotionEvent e)
    {

        // Log.d("Game Gesture", "event " + e.getAction());
        stateidx = state[stateidx].onMotion(e);
        // Log.d("Game Gesture", "state " + stateidx);

        return true;

    }

    void singleTapConfirmed(float x, float y)
    {
        Log.d("Game Gensture", "single tap confirm");
    }

    void longPressConfirmed(float x, float y)
    {
        Log.d("Game Gensture", "long press confirm");
    }

    void doubleTapConfirmed(MotionEvent e)
    {
        Log.d("Game Gensture", "double tap confirm");
    }

    void doubleTapRelease(float x, float y)
    {
        Log.d("Game Gensture", "double tap release");
    }

    void dragStart(MotionEvent e)
    {
        Log.d("Game Gensture", "drag start");
    }

    void drag(float x, float y)
    {
        Log.d("Game Gensture", "drag confirm");
    }

    void dragEnd(float x, float y)
    {
        Log.d("Game Gensture", "drag end");
    }

    void scale(Vec2<Float> p1, Vec2<Float> p2, Vec2<Float> n1, Vec2<Float> n2)
    {
        Log.d("Game Gensture", "scale confirm");
    }

    void scroll(Vec2<Float> prev, Vec2<Float> next)
    {
        Log.d("Game Gensture", "scroll confirm");

    }


    abstract class GState
    {

        final int clickTime = 250, sensetivity = 5000;

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
                state[down].start(e);
                return down;
            }
            return wait;
        }
    }

    class Down extends GState
    {

        LocalTime downTime;
        Vec2<Float> loc;

        Down()
        {
            loc = new Vec2(0, 0);
        }

        void start(MotionEvent e)
        {
            downTime = LocalTime.now();
            loc.set(e.getX(), e.getY());
        }

        void update()
        {
            if (ChronoUnit.MILLIS.between(downTime, LocalTime.now()) > clickTime)
            {
                longPressConfirmed(loc.x, loc.y);
                stateidx = wait;
            }
        }

        int onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:
                    if (Vec2.distSq(loc, new Vec2(e.getX(), e.getY())) > sensetivity)
                    {
                        state[scroll].start(e);
                        return scroll;
                    }
                    return down;

                case MotionEvent.ACTION_UP:
                    if (ChronoUnit.MILLIS.between(downTime, LocalTime.now()) < clickTime)
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
            if (ChronoUnit.MILLIS.between(upTime, LocalTime.now()) > clickTime)
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
                    if (ChronoUnit.MILLIS.between(upTime, LocalTime.now()) < clickTime)
                    {
                        state[doubleTap].start(e);
                        return doubleTap;
                    }
                    return down;

            }
            return wait;
        }
    }

    class Drag extends GState
    {

        void start(MotionEvent e)
        {
            dragStart(e);
        }

        int onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:
                    drag(e.getX(), e.getY());
                    return drag;

                case MotionEvent.ACTION_UP:
                    dragEnd(e.getX(), e.getY());
                    return wait;


            }
            return drag;
        }
    }


    class DoubleTap extends GState
    {

        Vec2<Float> loc;

        DoubleTap()
        {
            loc = new Vec2(0, 0);
        }

        void start(MotionEvent e)
        {
            loc.set(e.getX(), e.getY());
            doubleTapConfirmed(e);
        }

        int onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:
                    if (Vec2.distSq(loc, new Vec2(e.getX(), e.getY())) > sensetivity)
                    {
                        state[drag].start(e);
                        return drag;
                    }
                    return doubleTap;

                case MotionEvent.ACTION_UP:
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
