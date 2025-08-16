package com.game.engine.gesture.gestureEvent;

import com.game.engine.Node;
import com.game.engine.Transform;
import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;
import com.game.tama.util.Vec4;

public abstract class GestureEvent
{

    public float x, y;

    protected GestureEvent parent = null;

    public abstract void callEventMethod(InputEventMethod handler);

    public abstract void callObjectMethod(InputObjectMethod handler);

    /**
     * Sets the x and y values for the base class
     *
     * @param x
     * @param y
     */
    public void setTouch(final float x, final float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Transforms all the points in the class by the transform and returns it in
     * a new object. Subclasses with extra data should override this method, and
     * apply the transform to all the subclass points.
     *
     * @param transform
     * @return the transformed event
     */
    public GestureEvent transform(final Transform transform)
    {
        final GestureEvent copy = this.copy();
        final Vec4<Float> f = transform.mapVector(x, y, 0);
        copy.x = f.x;
        copy.y = f.y;
        copy.parent = this;
        return copy;
    }

    public <T extends GestureEvent> T transformToLocal(final Node node)
    {
        return (T) this.transform(node.getWorldTransform().invert());
    }

    /**
     * Create a deep copy of this event. Subclasses should override this method
     * and populate their own fields.
     *
     * @return
     */
    public GestureEvent copy()
    {
        final GestureEvent event;
        try
        {
            event = this.getClass().newInstance();
        }
        catch (final InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        event.x = this.x;
        event.y = this.y;
        event.parent = this.parent;
        return event;
    }

    public <T extends GestureEvent> T getParent(final Class<T> clazz)
    {
        return (T) parent;
    }

    abstract public EventType getType();
}

