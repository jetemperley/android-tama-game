package com.tama.gesture;

import com.tama.core.Interactive;

public class DoubleTapDragStart extends GestureEvent
{
    public float startX;
    public float startY;
    public float currentX;
    public float currentY;

    @Override
    public void callEvent(Interactive handler)
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
}
