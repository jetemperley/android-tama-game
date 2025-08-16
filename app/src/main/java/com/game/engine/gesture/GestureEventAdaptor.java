package com.game.engine.gesture;

import com.game.engine.Transform;
import com.game.engine.gesture.gestureEvent.DoubleTapConfirmed;
import com.game.engine.gesture.gestureEvent.DoubleTapDrag;
import com.game.engine.gesture.gestureEvent.DoubleTapDragEnd;
import com.game.engine.gesture.gestureEvent.DoubleTapDragStart;
import com.game.engine.gesture.gestureEvent.DoubleTapRelease;
import com.game.engine.gesture.gestureEvent.Down;
import com.game.engine.gesture.gestureEvent.Drag;
import com.game.engine.gesture.gestureEvent.DragEnd;
import com.game.engine.gesture.gestureEvent.DragStart;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.engine.gesture.gestureEvent.LongPressConfirmed;
import com.game.engine.gesture.gestureEvent.Scale;
import com.game.engine.gesture.gestureEvent.SingleTapConfirmed;
import com.game.tama.util.Vec2;

public class GestureEventAdaptor extends GestureEventSource implements InputEventMethod
{
    private final SingleTapConfirmed singleTap = new SingleTapConfirmed();
    private final DoubleTapConfirmed doubleTapConfirmed = new DoubleTapConfirmed();
    private final DoubleTapRelease doubleTapRelease = new DoubleTapRelease();
    private final DoubleTapDragStart doubleTapDragStart =
        new DoubleTapDragStart();
    private final DoubleTapDrag doubleTapDrag = new DoubleTapDrag();
    private final DoubleTapDragEnd doubleTapDragEnd = new DoubleTapDragEnd();
    private final LongPressConfirmed longPress = new LongPressConfirmed();
    private final Down down = new Down();
    private final Scale scale = new Scale();
    private final DragStart dragStart = new DragStart();
    private final Drag drag = new Drag();
    private final DragEnd dragEnd = new DragEnd();

    private final Transform transform;

    /**
     * Converts generic android motion events to game contextual gesture events.
     *
     * @param gestureTransformer This argument transforms all the points passed
     *                           through this class. Its purpose is to abstract
     *                           the hardware from the game.
     */
    public GestureEventAdaptor(final Transform gestureTransformer)
    {
        this.transform = gestureTransformer;
    }

    @Override
    public void singleTapConfirmed(final float x, final float y)
    {
        singleTap.setTouch(x, y);
        handleEvent(singleTap);
    }

    @Override
    public void singleDown(final float x, final float y)
    {
        down.setTouch(x, y);
        handleEvent(down);
    }

    @Override
    public void longPressConfirmed(final float x, final float y)
    {
        longPress.setTouch(x, y);
        handleEvent(longPress);
    }

    @Override
    public void doubleTapConfirmed(final float x, final float y)
    {
        doubleTapConfirmed.setTouch(x, y);
        handleEvent(doubleTapConfirmed);
    }

    @Override
    public void doubleTapRelease(final float x, final float y)
    {
        doubleTapRelease.setTouch(x, y);
        handleEvent(doubleTapRelease);
    }

    @Override
    public void doubleTapDragStart(final float startX,
                                   final float startY,
                                   final float currentX,
                                   final float currentY)
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
    public void doubleTapDrag(final float prevX,
                              final float prevY,
                              final float nextX,
                              final float nextY)
    {
        doubleTapDrag.set(prevX, prevY, nextX, nextY);
        handleEvent(doubleTapDrag);
    }

    @Override
    public void doubleTapDragEnd(final float x, final float y)
    {
        doubleTapDragEnd.setTouch(x, y);
        handleEvent(doubleTapDragEnd);
    }

    @Override
    public void scale(final Vec2<Float> p1,
                      final Vec2<Float> p2,
                      final Vec2<Float> n1,
                      final Vec2<Float> n2)
    {
        scale.set(p1, p2, n1, n2);
        handleEvent(scale);
    }

    @Override
    public void dragStart(final float x, final float y)
    {
        dragStart.setTouch(x, y);
        handleEvent(dragStart);
    }

    @Override
    public void drag(final Vec2<Float> prev, final Vec2<Float> next)
    {
        drag.setTouch(prev.x, prev.y);
        drag.set(prev, next);
        handleEvent(drag);
    }

    @Override
    public void dragEnd(final float x, final float y)
    {
        dragEnd.setTouch(x, y);
        handleEvent(dragEnd);
    }

    @Override
    public boolean handleEvent(final GestureEvent event)
    {
        final GestureEvent tevent = event.transform(transform);
        return target.handleEvent(tevent);
    }
}
