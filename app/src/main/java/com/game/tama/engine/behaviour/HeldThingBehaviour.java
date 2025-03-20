package com.game.tama.engine.behaviour;

import com.game.engine.DisplayAdapter;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.engine.gesture.Input;
import com.game.engine.gesture.gestureEvent.Scale;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.engine.Transform;
import com.game.tama.thing.Thing;
import com.game.tama.util.Log;
import com.game.tama.util.Vec2;

public class HeldThingBehaviour extends Behaviour implements Input
{
    public Thing held = null;
    public Vec2<Float> heldPos = new Vec2<>(0f, 0f);
    public Vec2<Float> heldOffset = new Vec2<>(0f, 0f);

    public HeldThingBehaviour(Node parent)
    {
        super(parent);
    }

    @Override
    public void draw(DisplayAdapter d)
    {
        if (held != null)
        {
            d.drawSprite(held.loc.sprite);
        }
    }

    /**
     * @param thing The thing to hold.
     * @param x     array position of tap
     * @param y     array position of tap
     */
    public void setHeld(Thing thing, float x, float y)
    {
        held = thing;
        heldPos.set(x, y);
        //        Vec2<Float> pos = held.loc.getWorldArrPos();
        //        heldOffset.x = x - pos.x;
        //        heldOffset.y = y - pos.y;
    }

    @Override
    public void drag(Vec2<Float> prev, Vec2<Float> next)
    {

        Transform t = node.getParent().getWorldTransform().invert();
        float[] nextLocal = t.mapVector(next.x, next.y, 0);

        Transform hoveredMat = GameManager.INST.getContainingNode(
            next.x,
            next.y).getWorldTransform();
        Transform local = node.localTransform;
        local.reset().preTranslate(nextLocal[0], nextLocal[1], 0);
        float scale = hoveredMat.getScale().x;
        local.preScale(scale, scale, 1);
    }

    public void dragEnd(float x, float y)
    {
        Transform targetMat = GameManager.INST.getContainingNode(
            x, y).getWorldTransform().invert();
        float[] f = targetMat.mapVector(x, y, 0);
        dropHeld(f[0], f[1]);
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
    public void dropHeld(float x, float y)
    {
        Log.log(this, "dropping");
        GameManager.getGame().world.addOrClosest(held, (int) x, (int) y);
        held = null;
    }

    @Override
    public boolean handleEvent(GestureEvent e)
    {
        if (held == null || e.getClass() == Scale.class)
        {
            return false;
        }
        e.callEvent(this);
        return true;
    }
}
