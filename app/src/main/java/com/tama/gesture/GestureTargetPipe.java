package com.tama.gesture;

import com.tama.core.GameManager;
import com.tama.core.Input;
import com.tama.util.Vec2;

public class GestureTargetPipe extends Gesture
{
    public GameManager gameManager;

    private final SingleTap singleTap = new SingleTap();
    private final DoubleTap doubleTap = new DoubleTap();
    private final DoubleTapRelease doubleTapRelease = new DoubleTapRelease();
    private final DoubleTapDragStart doubleTapDragStart = new DoubleTapDragStart();
    private final DoubleTapDrag doubleTapDrag = new DoubleTapDrag();
    private final DoubleTapDragEnd doubleTapDragEnd = new DoubleTapDragEnd();
    private final LongPress longPress = new LongPress();
    private final Down down = new Down();
    private final Scale scale = new Scale();
    private final Scroll scroll = new Scroll();

    public GestureTargetPipe(GameManager manager)
    {
        gameManager = manager;
    }

    @Override
    public void singleTapConfirmed(float x, float y)
    {
        singleTap.set(x, y);
        gameManager.target.handleEvent(singleTap);
    }

    @Override
    public void singleDown(float x, float y)
    {
        down.set(x, y);
        gameManager.target.handleEvent(down);
    }

    @Override
    public void longPressConfirmed(float x, float y)
    {
        longPress.set(x, y);
        gameManager.target.handleEvent(longPress);
    }

    @Override
    public void doubleTapConfirmed(float x, float y)
    {
        doubleTap.set(x, y);
        gameManager.target.handleEvent(doubleTap);
    }

    @Override
    public void doubleTapRelease(float x, float y)
    {
        doubleTapRelease.set(x, y);
        gameManager.target.handleEvent(doubleTapRelease);
    }

    @Override
    public void doubleTapDragStart(float startX, float startY, float currentX, float currentY)
    {
        doubleTapDragStart.set(startX, startY);
        doubleTapDragStart.set(
            startX,
            startY,
            currentX,
            currentY);
        gameManager.target.handleEvent(doubleTapDragStart);
    }

    @Override
    public void doubleTapDrag(float prevX, float prevY, float nextX, float nextY)
    {
        doubleTapDrag.set(prevX, prevY, nextX, nextY);
        gameManager.target.handleEvent(doubleTapDrag);
    }

    @Override
    public void doubleTapDragEnd(float x, float y)
    {
        doubleTapDragEnd.set(x, y);
        gameManager.target.handleEvent(doubleTapDragEnd);
    }

    @Override
    public void scale(Vec2<Float> p1, Vec2<Float> p2, Vec2<Float> n1, Vec2<Float> n2)
    {
        scale.set(p1, p2, n1, n2);
        gameManager.target.handleEvent(scale);
    }

    @Override
    public void scroll(Vec2<Float> prev, Vec2<Float> next)
    {
        scroll.set(prev.x, prev.y);
        scroll.set(prev, next);
        gameManager.target.handleEvent(scroll);
    }
}
