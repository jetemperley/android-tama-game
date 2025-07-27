package com.game.tama.engine.behaviour;

import com.game.engine.Behaviour;
import com.game.engine.DisplayAdapter;
import com.game.engine.Node;
import com.game.engine.Transform;
import com.game.engine.gesture.Input;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.engine.gesture.gestureEvent.Scale;
import com.game.tama.thing.Thing;
import com.game.tama.util.Log;
import com.game.tama.util.Vec2;

public class HeldThingBehaviour extends Behaviour implements Input
{
    public Thing held = null;
    private final Vec2<Float> heldPos = new Vec2<>(0f, 0f);
    private final Vec2<Float> heldOffset = new Vec2<>(0f, 0f);

    public HeldThingBehaviour(final Node parent)
    {
        super(parent);
    }

    @Override
    public void draw(final DisplayAdapter d)
    {
        if (held != null)
        {
            d.draw(held.loc.sprite, -heldOffset.x, -heldOffset.y, DisplayAdapter.ABOVE_UI_LAYER);
        }
    }

    /**
     * @param thing The thing to hold.
     * @param x     world position of tap
     * @param y     world position of tap
     */
    public void setHeld(final Thing thing, final float x, final float y)
    {
        held = thing;
        heldOffset.set(x % 1, y % 1);
        drag(null, new Vec2<>(x, y));
    }

    @Override
    public void drag(final Vec2<Float> prev, final Vec2<Float> next)
    {

        final Transform t = node.getParent().getWorldTransform().invert();
        final float[] nextLocal = t.mapVector(next.x, next.y, 0);

        final Transform hoveredMat = GameManager.INST.getContainingNode(
            next.x,
            next.y).getWorldTransform();
        final Transform local = node.localTransform;
        local.reset().preTranslate(nextLocal[0], nextLocal[1], 0);
        final float scale = hoveredMat.getScale().x;
        local.preScale(scale, scale, 1);
    }

    @Override
    public void dragEnd(final float x, final float y)
    {
        final Transform targetMat = GameManager.INST.getContainingNode(
            x, y).getWorldTransform().invert();
        final float[] f = targetMat.mapVector(x, y, 0);
        dropHeld(f[0] - heldOffset.x + 0.5f, f[1] - heldOffset.y + 0.5f);
        Log.log(this, String.format("x: %s, y: %s, dropX: %s, dropY: %s", x, y, f[0], f[1]));
    }

    void dropHeld()
    {
        dropHeld(heldPos.x, heldPos.y);
    }

    /**
     * Drop the thing on the root world at x, y, in world coords
     *
     * @param x
     * @param y
     */
    public void dropHeld(final float x, final float y)
    {
        GameManager.getGame().world.addOrClosest(held, (int) x, (int) y);
        held = null;
    }

    @Override
    public boolean handleEvent(final GestureEvent e)
    {
        if (held == null || e.getClass() == Scale.class)
        {
            return false;
        }
        e.callEvent(this);
        return true;
    }
}
