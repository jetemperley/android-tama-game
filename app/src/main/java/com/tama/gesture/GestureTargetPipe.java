package com.tama.gesture;

import com.tama.core.GameManager;
import com.tama.util.Vec2;

public class GestureTargetPipe extends Gesture
{
    public GameManager gameManager;

    private com.tama.gesture.SingleTap tap = new com.tama.gesture.SingleTap();
    private com.tama.gesture.DoubleTap
        doubleTap = new com.tama.gesture.DoubleTap();
    private DoubleTapRelease doubleTapRelease = new DoubleTapRelease();
    private DoubleTapDragStart doubleTapDragStart = new DoubleTapDragStart();
    private com.tama.gesture.DoubleTapDrag
        doubleTapDrag = new com.tama.gesture.DoubleTapDrag();
    private DoubleTapDragEnd doubleTapDragEnd = new DoubleTapDragEnd();
    private LongPress longPress = new LongPress();
    private com.tama.gesture.Down down = new com.tama.gesture.Down();
    private com.tama.gesture.Scale scale = new com.tama.gesture.Scale();
    private com.tama.gesture.Scroll scroll = new com.tama.gesture.Scroll();

    public GestureTargetPipe(GameManager manager)
    {
        gameManager = manager;
    }

    @Override
    public void singleTapConfirmed(float x, float y)
    {
        gameManager.target.singleTapConfirmed(x, y);
    }

    @Override
    public void singleDown(float x, float y)
    {
        gameManager.target.singleDown(x, y);
    }

    @Override
    public void longPressConfirmed(float x, float y)
    {
        gameManager.target.longPressConfirmed(x, y);
    }

    @Override
    public void doubleTapConfirmed(float x, float y)
    {
        gameManager.target.doubleTapConfirmed(x, y);
    }

    @Override
    public void doubleTapRelease(float x, float y)
    {
        gameManager.target.doubleTapRelease(x, y);
    }

    @Override
    public void doubleTapDragStart(float startX, float startY, float currentX, float currentY)
    {
        gameManager.target.doubleTapDragStart(startX, startY, currentX, currentY);
    }

    @Override
    public void doubleTapDrag(float x, float y)
    {
        gameManager.target.doubleTapDrag(x, y);
    }

    @Override
    public void doubleTapDragEnd(float x, float y)
    {
        gameManager.target.doubleTapDragEnd(x, y);
    }

    @Override
    public void scale(Vec2<Float> p1, Vec2<Float> p2, Vec2<Float> n1, Vec2<Float> n2)
    {
        gameManager.target.scale(p1, p2, n1, n2);
    }

    @Override
    public void scroll(Vec2<Float> prev, Vec2<Float> next)
    {
        gameManager.target.scroll(prev, next);
    }
}
