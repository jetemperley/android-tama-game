package com.game.android.gesture;

import android.view.MotionEvent;

import com.game.tama.util.Log;
import com.game.tama.util.Vec2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class Gesture implements Input
{
    private HashMap<Class<? extends GState>, GState> states;
    private Class<? extends GState> stateidx = null;

    int error = -1, wait = 0, down = 1, doubleTapDrag = 2,
        scale = 3, singleTap = 4, drag = 5,
        doubleTap = 6;

    // pointer ids, (primary pointer is always at index 0)
    private int id2 = -1;
    // locations of the pointers
    // private Vec2<Float> prev1, prev2, new1, new2;

    private int singleTapConfirmDelay = 200;
    private int dragConfirmSensitivity = 10;

    public Input gestureTarget;

    public float topOffset = 0;

    public Gesture()
    {
        states = new HashMap<>();
        states.put(Wait.class, new Wait());
        states.put(Down.class, new Down());
        states.put(Scale.class, new Scale());
        states.put(SingleTap.class, new SingleTap());
        states.put(Drag.class, new Drag());
        states.put(DoubleTap.class, new DoubleTap());
        stateidx = Wait.class;
    }

    public void update()
    {
        // Log.log("Game Gesture", "update");
        states.get(stateidx).update();
    }

    public boolean onTouchEvent(MotionEvent e)
    {
        stateidx = states.get(stateidx).onMotion(e);
        return true;
    }

    public void singleTapConfirmed(float x, float y)
    {
        Log.log(this, "single tap confirm");
        gestureTarget.singleTapConfirmed(x, y - topOffset);
    }

    public void singleDown(float x, float y)
    {
        Log.log(this, "single down");
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
        Log.log(this, "double drag confirm");
        gestureTarget.doubleTapDrag(
            prevX,
            prevY - topOffset,
            nextX,
            nextY - topOffset);
    }

    public void doubleTapDragEnd(float x, float y)
    {
        Log.log(this, "double drag end");
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

    @Override
    public void dragStart(float x, float y)
    {
        Log.log(this, "drag start");
        y -= topOffset;
        gestureTarget.dragStart(x, y);
    }

    public void drag(Vec2<Float> prev, Vec2<Float> next)
    {
        prev.y -= topOffset;
        next.y -= topOffset;
        gestureTarget.drag(prev, next);
    }

    @Override
    public void dragEnd(float x, float y)
    {
        Log.log(this, "drag end");
        y -= topOffset;
        gestureTarget.dragEnd(x, y);
    }

    abstract class GState
    {

        abstract void start(MotionEvent e);

        abstract Class<? extends GState> onMotion(MotionEvent e);

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

        Class onMotion(MotionEvent e)
        {
            switch (e.getAction())
            {

                case MotionEvent.ACTION_POINTER_2_DOWN:
                case MotionEvent.ACTION_DOWN:
                    singleDown(e.getX(), e.getY());
                    states.get(Down.class).start(e);
                    return Down.class;

                case MotionEvent.ACTION_MOVE:
                    ((Drag)states.get(Drag.class)).start(e.getX(), e.getY());
                    return Drag.class;
            }
            Log.log(this, "Unexpected starting motion event was: " + e.getAction());
            return Wait.class;
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
                stateidx = Wait.class;
            }
        }

        Class onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:
                    if (Vec2.distSq(initialPos, new Vec2(e.getX(), e.getY())) >
                        dragConfirmSensitivity)
                    {
                        ((Drag) states.get(Drag.class)).start(
                            initialPos.x,
                            initialPos.y);
                        return Drag.class;
                    }
                    return Down.class;

                case MotionEvent.ACTION_UP:

                    singleTapConfirmed(initialPos.x, initialPos.y);
                    return Wait.class;

                case MotionEvent.ACTION_POINTER_2_DOWN:
                    states.get(Scale.class).start(e);
                    return Scale.class;
            }
            Log.log(
                this,
                "Unexpected starting motion event was: " + e.getAction());
            return Down.class;
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
                stateidx = Wait.class;
            }
        }

        Class onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_DOWN:
                    if (ChronoUnit.MILLIS.between(upTime, LocalTime.now()) <
                        singleTapConfirmDelay)
                    {
                        states.get(DoubleTap.class).start(e);
                        return DoubleTap.class;
                    }
                    return Down.class;
            }
            return Wait.class;
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

        Class onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_UP:
                    if (ChronoUnit.MILLIS.between(downTime, LocalTime.now()) <
                        singleTapConfirmDelay)
                    {
                        doubleTapConfirmed(e.getX(), e.getY());
                        return Wait.class;
                    }
                    doubleTapRelease(e.getX(), e.getY());
                    return Wait.class;
            }
            return Wait.class;
        }
    }

    private class Drag extends GState
    {
        Vec2<Float> initialPos;
        Vec2<Float> prev, next;

        Drag()
        {
            initialPos = new Vec2(0, 0);
            prev = new Vec2(0, 0);
            next = new Vec2(0, 0);
        }

        void start(float x, float y)
        {
            initialPos.set(x, y);
            next(x, y);
            dragStart(x, y);
        }

        void next(float x, float y)
        {
            prev.set(next);
            next.set(x, y);
        }

        @Override
        void start(MotionEvent e)
        {
            next(e.getX(), e.getY());
        }

        Class onMotion(MotionEvent e)
        {

            switch (e.getAction())
            {

                case MotionEvent.ACTION_MOVE:
                    next(e.getX(), e.getY());
                    drag(new Vec2<>(prev), new Vec2<>(next));
                    return Drag.class;

                case MotionEvent.ACTION_UP:
                    dragEnd(e.getX(), e.getY());
                    return Wait.class;

                case MotionEvent.ACTION_POINTER_2_DOWN:
                    states.get(Scale.class).start(e);
                    return Scale.class;
            }
            return Wait.class;
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

        Class onMotion(MotionEvent e)
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
                    return Scale.class;

                case MotionEvent.ACTION_UP:
                    point2ID = -1;
                    return Wait.class;

                case MotionEvent.ACTION_POINTER_2_UP:
                    point2ID = -1;
                    states.get(Drag.class).start(e);
                    return Drag.class;
            }
            return Wait.class;
        }
    }
}
