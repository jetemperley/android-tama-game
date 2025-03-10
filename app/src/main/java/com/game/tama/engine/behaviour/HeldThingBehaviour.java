package com.game.tama.engine.behaviour;

import android.graphics.Matrix;

import com.game.engine.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.Input;
import com.game.android.gesture.Scale;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.engine.Transform;
import com.game.tama.thing.Thing;
import com.game.tama.util.Log;
import com.game.tama.util.MatrixUtil;
import com.game.tama.util.Vec2;

public class HeldThingBehaviour extends Behaviour implements Input
{
    public Thing held = null;
    public Vec2<Float> heldPos = new Vec2<>(0f, 0f);
    public Vec2<Float> heldOffset = new Vec2<>(0f, 0f);

    private Transform heldMat = node.newTransform();

    public HeldThingBehaviour(Node parent)
    {
        super(parent);
    }

    @Override
    public void draw(DisplayAdapter d)
    {
        d.push();
        d.preConcat(heldMat);
        if (held != null)
        {
            d.drawSprite(held.loc.sprite);
        }
        d.pop();
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

    public void setHeldOffset(float x, float y)
    {
        heldOffset.set(x, y);
    }

    @Override
    public void drag(Vec2<Float> prev, Vec2<Float> next)
    {
        Transform targetMat = GameManager.INST.getContainingNode(
            next.x,
            next.y).worldTransform;
        targetMat.postTranslate(next.x, next.y, 0);
        float scale = targetMat.getScale().x;
        heldMat.setScale(scale, scale);
        heldPos.set(next.x, next.y);
    }

    public void dragEnd(float x, float y)
    {
        Transform targetMat = GameManager.INST.getContainingNode(
            x, y).worldTransform;
        targetMat.invert();
        float[] f = targetMat.mapPoint(x,y,0);
        dropHeld(f[0], f[1]);
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

    void dropHeld()
    {
        dropHeld(heldPos.x, heldPos.y);
    }

    /**
     * Drop the thing on the root world at x, y, in world coords
     * @param x
     * @param y
     */
    public void dropHeld(float x, float y)
    {
        Log.log(this, "dropping");
        GameManager.getGame().world.addOrClosest(held, (int) x, (int) y);
        held = null;
    }
}
