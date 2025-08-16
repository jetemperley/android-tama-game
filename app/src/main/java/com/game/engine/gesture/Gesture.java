package com.game.engine.gesture;

import android.view.MotionEvent;

import com.game.tama.util.Log;
import com.game.tama.util.Vec2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class Gesture implements InputEventMethod
{
    private final HashMap<Class<? extends GState>, GState> states;
    private Class<? extends GState> stateKey = null;

    private final int singleTapConfirmDelay = 200;
    private final int dragConfirmSensitivity = 10;

    public InputEventMethod gestureTarget;

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
        stateKey = Wait.class;
    }

    public void update()
    {
        // Log.log("Game Gesture", "update");
        states.get(stateKey).update();
    }

    public boolean onTouchEvent(final MotionEvent e)
    {
        stateKey = states.get(stateKey).onMotion(e);
        return true;
    }

    /**
     * Triggers after the users finger is removed from the screen and
     * the finger has not moved
     *
     * @param x
     * @param y
     */
    @Override
    public void singleTapConfirmed(final float x, final float y)
    {
        Log.log(this, "single tap confirm");
        gestureTarget.singleTapConfirmed(x, y - topOffset);
    }

    /**
     * Triggers when a finger is first touched to the screen
     *
     * @param x
     * @param y
     */
    @Override
    public void singleDown(final float x, final float y)
    {
        Log.log(this, "single down");
        gestureTarget.singleDown(x, y - topOffset);
    }

    /**
     * Triggers after the long press duration has elapsed and the finger has
     * not moved
     *
     * @param x
     * @param y
     */
    @Override
    public void longPressConfirmed(final float x, final float y)
    {
        Log.log(this, "long press confirm");
        gestureTarget.longPressConfirmed(x, y - topOffset);
    }

    /**
     * Triggers after 2 taps have been confirmed (singleTapConfirmed)
     * within a specified period
     *
     * @param x
     * @param y
     */
    @Override
    public void doubleTapConfirmed(final float x, final float y)
    {
        Log.log(this, "double tap confirm");
        gestureTarget.doubleTapConfirmed(x, y - topOffset);
    }

    /**
     * Triggers on when the finger is released from the screen after
     * doubleTapConfirmed() (i think, double check that)
     *
     * @param x
     * @param y
     */
    @Override
    public void doubleTapRelease(final float x, final float y)
    {
        Log.log(this, "double tap release");
        gestureTarget.doubleTapRelease(x, y - topOffset);
    }

    @Override
    public void doubleTapDragStart(final float startX,
                                   final float startY,
                                   final float currentX,
                                   final float currentY)
    {
        Log.log(this, "drag start");
        gestureTarget.doubleTapDragStart(
            startX,
            startY - topOffset,
            currentX,
            currentY - topOffset);
    }

    @Override
    public void doubleTapDrag(final float prevX,
                              final float prevY,
                              final float nextX,
                              final float nextY)
    {
        Log.log(this, "double drag confirm");
        gestureTarget.doubleTapDrag(
            prevX,
            prevY - topOffset,
            nextX,
            nextY - topOffset);
    }

    @Override
    public void doubleTapDragEnd(final float x, final float y)
    {
        Log.log(this, "double drag end");
        gestureTarget.doubleTapDragEnd(x, y - topOffset);
    }

    @Override
    public void scale(final Vec2<Float> p1,
                      final Vec2<Float> p2,
                      final Vec2<Float> n1,
                      final Vec2<Float> n2)
    {
        Log.log(this, "scale confirm");
        p1.y -= topOffset;
        p2.y -= topOffset;
        n1.y -= topOffset;
        n2.y -= topOffset;

        gestureTarget.scale(p1, p2, n1, n2);
    }

    @Override
    public void scaleRelease(final Vec2<Float> point1,
                             final Vec2<Float> point2)
    {
        Log.log(this, "scale release");
        point1.y -= topOffset;
        point2.y -= topOffset;

        gestureTarget.scaleRelease(point1, point2);
    }

    @Override
    public void dragStart(final float x, float y)
    {
        Log.log(this, "drag start");
        y -= topOffset;
        gestureTarget.dragStart(x, y);
    }

    @Override
    public void drag(final Vec2<Float> prev, final Vec2<Float> next)
    {
        prev.y -= topOffset;
        next.y -= topOffset;
        gestureTarget.drag(prev, next);
    }

    @Override
    public void dragEnd(final float x, float y)
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

        @Override
        void start(final MotionEvent e)
        {
            //reset everything?
        }

        @Override
        Class onMotion(final MotionEvent e)
        {
            switch (e.getAction())
            {

                case MotionEvent.ACTION_POINTER_2_DOWN:
                case MotionEvent.ACTION_DOWN:
                    singleDown(e.getX(), e.getY());
                    states.get(Down.class).start(e);
                    return Down.class;

                case MotionEvent.ACTION_MOVE:
                    ((Drag) states.get(Drag.class)).start(e.getX(), e.getY());
                    return Drag.class;
            }
            Log.error(this, "Unexpected starting motion event was: " + e.getAction());
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

        @Override
        void start(final MotionEvent e)
        {
            downTime = LocalTime.now();
            initialPos.set(e.getX(), e.getY());
        }

        @Override
        void update()
        {
            if (ChronoUnit.MILLIS.between(downTime, LocalTime.now()) >
                singleTapConfirmDelay)
            {
                longPressConfirmed(initialPos.x, initialPos.y);
                stateKey = Wait.class;
            }
        }

        @Override
        Class onMotion(final MotionEvent e)
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

        @Override
        void start(final MotionEvent e)
        {

            upTime = LocalTime.now();
            loc.set(e.getX(), e.getY());
        }

        @Override
        void update()
        {
            if (ChronoUnit.MILLIS.between(upTime, LocalTime.now()) >
                singleTapConfirmDelay)
            {
                singleTapConfirmed(loc.x, loc.y);
                stateKey = Wait.class;
            }
        }

        @Override
        Class onMotion(final MotionEvent e)
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

        @Override
        void start(final MotionEvent e)
        {
            downTime = LocalTime.now();
            loc.set(e.getX(), e.getY());
        }

        @Override
        Class onMotion(final MotionEvent e)
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

        void start(final float x, final float y)
        {
            initialPos.set(x, y);
            next(x, y);
            dragStart(x, y);
        }

        void next(final float x, final float y)
        {
            prev.set(next);
            next.set(x, y);
        }

        @Override
        void start(final MotionEvent e)
        {
            next(e.getX(), e.getY());
        }

        @Override
        Class onMotion(final MotionEvent e)
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

        @Override
        void start(final MotionEvent e)
        {
            if (point2ID == -1)
            {
                point2ID = e.getPointerId(1);
            }
            final int idx = e.findPointerIndex(point2ID);
            if (idx != -1)
            {
                prev1.set(next1);
                next1.set(e.getX(), e.getY());
                prev2.set(next2);
                next2.set(e.getX(idx), e.getY(idx));
            }
        }

        @Override
        Class onMotion(final MotionEvent e)
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
                    // todo release both fingers at the same time
                    scaleRelease(next1, next2);
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
