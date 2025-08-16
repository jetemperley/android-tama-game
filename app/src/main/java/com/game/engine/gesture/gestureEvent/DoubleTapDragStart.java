package com.game.engine.gesture.gestureEvent;

import com.game.engine.Transform;
import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;
import com.game.tama.util.Vec2;
import com.game.tama.util.Vec4;

public class DoubleTapDragStart extends GestureEvent
{

    public Vec2<Float> start = new Vec2<>(0f, 0f);
    public Vec2<Float> current = new Vec2<>(0f, 0f);

    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.doubleTapDragStart(start.x, start.y, current.x, current.y);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.doubleTapDragStart(this);
    }

    public void set(final Vec2<Float> start, final Vec2<Float> current)
    {
        this.start.set(start);
        this.current.set(current);
    }

    public void set(final float startX,
                    final float startY,
                    final float currentX,
                    final float currentY)
    {
        this.start.set(startX, startY);
        this.current.set(currentX, currentY);
    }

    @Override
    public GestureEvent transform(final Transform transform)
    {
        final DoubleTapDragStart copy =
            (DoubleTapDragStart) super.transform(transform);
        final Vec4<Float> start = transform.mapVector(this.start.x, this.start.y, 0);
        copy.start.set(start.x, start.y);
        final Vec4<Float> curr = transform.mapVector(current.x, current.y, 0);
        copy.current.set(curr.x, curr.y);
        return copy;
    }

    @Override
    public EventType getType()
    {
        return EventType.DOUBLE_TAP_DRAG_START;
    }
}
