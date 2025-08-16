package com.game.tama.engine.behaviour;

import com.game.engine.Behaviour;
import com.game.engine.DisplayAdapter;
import com.game.engine.Node;
import com.game.engine.Transform;
import com.game.tama.core.thing.Thing;
import com.game.tama.util.Log;
import com.game.tama.util.Vec2;
import com.game.tama.util.Vec4;

public class HeldThingBehaviour extends Behaviour
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
        drag(new Vec2<>(x, y));
    }

    public void drag(final Vec2<Float> next)
    {

        final Transform t = node.getParent().getWorldTransform().invert();
        final Vec4<Float> nextLocal = t.mapVector(next.x, next.y, 0);
        Log.log(this, "Drag local " + nextLocal);

        final Transform hoveredMat = GameManager.INST.getContainingNode(
            next.x,
            next.y).getWorldTransform();
        final Transform local = node.localTransform;
        local.reset().preTranslate(nextLocal.x, nextLocal.y, 0);
        final float scale = hoveredMat.getScale().x;
        local.preScale(scale, scale, 1);
    }

    public void dragEnd(final float x, final float y)
    {
        final Transform targetMat = GameManager.INST.getContainingNode(
            x, y).getWorldTransform().invert();
        final Vec4<Float> f = targetMat.mapVector(x, y, 0);
        dropHeld(f.x - heldOffset.x + 0.5f, f.y - heldOffset.y + 0.5f);
        Log.log(this, String.format("x: %s, y: %s, dropX: %s, dropY: %s", x, y, f.x, f.y));
    }

    public void dropHeld()
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
}
