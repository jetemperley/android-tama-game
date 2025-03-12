package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;
import com.game.engine.Transform;

public abstract class GestureEvent implements Cloneable
{

    public float x, y;

    public abstract void callEvent(Input handler);

    /**
     * Sets the x and y values for the base class
     *
     * @param x
     * @param y
     */
    public void setTouch(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Transforms all the points in the class by the transform and returns it in
     * a new object. Subclasses with extra data should override this method,
     * and apply the transform to all the subclass points.
     *
     * @param transform
     * @return the transformed event
     */
    public GestureEvent transform(Transform transform)
    {
        GestureEvent copy;
        try
        {
            copy = (GestureEvent) this.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }

        float[] f = transform.mapVector(x, y, 0);
        x = f[0];
        y = f[1];
        return copy;
    }
}

