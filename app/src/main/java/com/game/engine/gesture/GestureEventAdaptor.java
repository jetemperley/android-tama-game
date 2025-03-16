package com.game.engine.gesture;

import com.game.engine.gesture.gestureEvent.DoubleTap;
import com.game.engine.gesture.gestureEvent.DoubleTapDrag;
import com.game.engine.gesture.gestureEvent.DoubleTapDragEnd;
import com.game.engine.gesture.gestureEvent.DoubleTapDragStart;
import com.game.engine.gesture.gestureEvent.DoubleTapRelease;
import com.game.engine.gesture.gestureEvent.Down;
import com.game.engine.gesture.gestureEvent.Drag;
import com.game.engine.gesture.gestureEvent.DragEnd;
import com.game.engine.gesture.gestureEvent.DragStart;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.engine.gesture.gestureEvent.LongPress;
import com.game.engine.gesture.gestureEvent.Scale;
import com.game.engine.gesture.gestureEvent.SingleTap;
import com.game.engine.Transform;
import com.game.tama.util.Vec2;

public class GestureEventAdaptor extends GestureEventSource implements Input
{
    private final SingleTap singleTap = new SingleTap();
    private final DoubleTap doubleTap = new DoubleTap();
    private final DoubleTapRelease doubleTapRelease = new DoubleTapRelease();
    private final DoubleTapDragStart doubleTapDragStart =
        new DoubleTapDragStart();
    private final DoubleTapDrag doubleTapDrag = new DoubleTapDrag();
    private final DoubleTapDragEnd doubleTapDragEnd = new DoubleTapDragEnd();
    private final LongPress longPress = new LongPress();
    private final Down down = new Down();
    private final Scale scale = new Scale();
    private final DragStart dragStart = new DragStart();
    private final Drag drag = new Drag();
    private final DragEnd dragEnd = new DragEnd();

    private Transform transform;

    /**
     * Converts generic android motion events to game contextual gesture events.
     * @param gestureTransformer This argument transforms all the points passed
     *                           through this class. Its purpose is to abstract
     *                           the hardware from the game.
     */
    public GestureEventAdaptor(Transform gestureTransformer)
    {
        this.transform = gestureTransformer;
    }

    @Override
    public void singleTapConfirmed(float x, float y)
    {
        singleTap.setTouch(x, y);
        handleEvent(singleTap);
    }

    @Override
    public void singleDown(float x, float y)
    {
        down.setTouch(x, y);
        handleEvent(down);
    }

    @Override
    public void longPressConfirmed(float x, float y)
    {
        longPress.setTouch(x, y);
        handleEvent(longPress);
    }

    @Override
    public void doubleTapConfirmed(float x, float y)
    {
        doubleTap.setTouch(x, y);
        handleEvent(doubleTap);
    }

    @Override
    public void doubleTapRelease(float x, float y)
    {
        doubleTapRelease.setTouch(x, y);
        handleEvent(doubleTapRelease);
    }

    @Override
    public void doubleTapDragStart(float startX,
                                   float startY,
                                   float currentX,
                                   float currentY)
    {
        doubleTapDragStart.setTouch(startX, startY);
        doubleTapDragStart.set(
            startX,
            startY,
            currentX,
            currentY);
        handleEvent(doubleTapDragStart);
    }

    @Override
    public void doubleTapDrag(float prevX,
                              float prevY,
                              float nextX,
                              float nextY)
    {
        doubleTapDrag.set(prevX, prevY, nextX, nextY);
        handleEvent(doubleTapDrag);
    }

    @Override
    public void doubleTapDragEnd(float x, float y)
    {
        doubleTapDragEnd.setTouch(x, y);
        handleEvent(doubleTapDragEnd);
    }

    @Override
    public void scale(Vec2<Float> p1,
                      Vec2<Float> p2,
                      Vec2<Float> n1,
                      Vec2<Float> n2)
    {
        scale.set(p1, p2, n1, n2);
        handleEvent(scale);
    }

    @Override
    public void dragStart(float x, float y)
    {
        dragStart.setTouch(x, y);
        handleEvent(dragStart);
    }

    @Override
    public void drag(Vec2<Float> prev, Vec2<Float> next)
    {
        drag.setTouch(prev.x, prev.y);
        drag.set(prev, next);
        handleEvent(drag);
    }

    @Override
    public void dragEnd(float x, float y)
    {
        dragEnd.setTouch(x, y);
        handleEvent(dragEnd);
    }

    public boolean handleEvent(GestureEvent event)
    {
        GestureEvent tevent = event.transform(transform);
        return target.handleEvent(tevent);
    }
}
