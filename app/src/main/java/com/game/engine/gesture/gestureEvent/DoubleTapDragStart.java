package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;
import com.game.engine.Transform;

public class DoubleTapDragStart extends GestureEvent
{
    public float startX;
    public float startY;
    public float currentX;
    public float currentY;

    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapDragStart(x, y, currentX, currentY);
    }

    public void set(float startX, float startY, float currentX, float currentY)
    {
        this.startX = startX;
        this.startY = startY;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    @Override
    public GestureEvent transform(Transform transform)
    {
        DoubleTapDragStart copy =
            (DoubleTapDragStart) super.transform(transform);
        float[] start = transform.mapVector(startX, startY, 0);
        copy.startX = start[0];
        copy.startY = start[1];
        float[] curr = transform.mapVector(currentX, currentY, 0);
        copy.currentX = curr[0];
        copy.currentY = curr[1];
        return copy;
    }
}
