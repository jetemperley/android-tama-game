package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;
import com.game.engine.Transform;
import com.game.tama.util.Vec2;

public class DoubleTapDragStart extends GestureEvent
{

    Vec2<Float> start = new Vec2<>(0f, 0f);
    Vec2<Float> current = new Vec2<>(0f, 0f);

    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapDragStart(start.x, start.y, current.x, current.y);
    }

    public void set(Vec2<Float> start, Vec2<Float> current)
    {
        this.start.set(start);
        this.current.set(current);
    }

    public void set(float startX, float startY, float currentX, float currentY)
    {
        this.start.set(startX, startY);
        this.current.set(currentX, currentY);
    }

    @Override
    public GestureEvent transform(Transform transform)
    {
        DoubleTapDragStart copy =
            (DoubleTapDragStart) super.transform(transform);
        float[] start = transform.mapVector(this.start.x, this.start.y, 0);
        copy.start.set(start[0], start[1]);
        float[] curr = transform.mapVector(current.x, current.y, 0);
        copy.current.set(curr[0], curr[1]);
        return copy;
    }
}
