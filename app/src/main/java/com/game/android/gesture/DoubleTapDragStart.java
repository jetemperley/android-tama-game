package com.game.android.gesture;

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
    public void transform(Transform transform) {
        super.transform(transform);
        float[] start = transform.mapVector(startX, startY, 0);
        startX = start[0];
        startY = start[1];
        float[] curr = transform.mapVector(currentX, currentY, 0);
        currentX = curr[0];
        currentY = curr[1];
    }
}
