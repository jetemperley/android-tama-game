package com.game.engine;

import com.game.engine.gesture.GestureEventHandler;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.tama.util.Vec2;
import com.game.tama.util.Vec4;

import java.util.ArrayList;
import java.util.List;

public class Node implements GestureEventHandler
{
    public final Class<? extends Transform> transformClass;
    protected Node parent = null;
    protected List<Node> children = new ArrayList<>();
    protected List<Behaviour> behaviours = new ArrayList<>();
    protected boolean enabled = true;

    public Transform localTransform;
    public Transform worldTransform;

    public Node(final Class<? extends Transform> klass)
    {
        transformClass = klass;
        try
        {
            worldTransform = klass.newInstance();
            localTransform = klass.newInstance();
        }
        catch (final Exception e)
        {
            throw new RuntimeException("Transforms need a default constructor", e);
        }
    }

    public Node(final Node parent)
    {
        this(parent.transformClass);
        setParent(parent);
    }

    public final void engine_start()
    {
        for (final Behaviour b : behaviours)
        {
            b.start();
        }
        for (final Node n : children)
        {
            n.engine_start();
        }
    }

    public final void engine_update()
    {
        if (!enabled)
        {
            return;
        }

        getWorldTransform(worldTransform);

        for (final Behaviour b : behaviours)
        {
            b.engine_update();
        }

        for (final Node node : children)
        {
            node.engine_update();
        }
    }

    public final void engine_draw(final DisplayAdapter display)
    {
        if (!enabled)
        {
            return;
        }

        display.push();
        display.preConcat(localTransform);
        for (final Behaviour b : behaviours)
        {
            b.engine_draw(display);
        }
        for (final Node node : children)
        {
            node.engine_draw(display);
        }
        display.pop();
    }

    public final void engine_input(final GestureEvent event)
    {
        if (!enabled)
        {
            return;
        }
        for (final Behaviour behaviour : behaviours)
        {
            behaviour.engine_input(event);
        }
        for (final Node node : children)
        {
            node.engine_input(event);
        }
    }

    public final boolean engine_ui_input(final GestureEvent event)
    {
        if (!enabled)
        {
            return false;
        }

        for (final Behaviour behaviour : behaviours)
        {

            if (behaviour.engine_ui_input(event))
            {
                return true;
            }

        }
        for (final Node node : children)
        {

            if (node.engine_ui_input(event))
            {
                return true;
            }
        }
        return false;
    }

    public void setParent(final Node newParent)
    {
        clearCurrentParent();
        if (newParent == null)
        {
            return;
        }
        this.parent = newParent;
        if (!newParent.children.contains(this))
        {
            newParent.children.add(this);
        }
    }

    private void clearCurrentParent()
    {
        if (parent == null)
        {
            return;
        }

        parent.children.remove(this);
        parent = null;
    }

    /**
     * Gets the world transform and returns it in the out matrix.
     *
     * @param out
     * @return out
     */
    public Transform getWorldTransform(final Transform out)
    {
        if (parent == null)
        {
            out.setValues(localTransform);
            return out;
        }
        parent.getWorldTransform(out);
        out.preMult(localTransform);
        return out;
    }

    /**
     * Calculates the world transform and returns it in a locally reused transform.
     */
    public Transform getWorldTransform()
    {
        return getWorldTransform(worldTransform);
    }

    public void removeBehaviour(final Behaviour b)
    {
        behaviours.remove(b);
    }

    public <B extends Behaviour> B getBehaviour(final Class<B> clazz)
    {
        for (final Behaviour b : behaviours)
        {
            if (b.getClass() == clazz)
            {
                return (B) b;
            }
        }
        return null;
    }

    void addBehaviour(final Behaviour b)
    {
        behaviours.add(b);
    }

    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
        return enabled && (parent == null || parent.isEnabled());
    }

    public Transform newTransform()
    {
        try
        {
            return transformClass.newInstance();
        }
        catch (final IllegalAccessException | InstantiationException e)
        {
            throw new RuntimeException("Could not instantiate transform.");
        }
    }

    public Node getRoot()
    {
        if (parent == null)
        {
            return this;
        }
        return parent.getRoot();
    }

    public Node getParent()
    {
        return parent;
    }

    /**
     * Converts the point x, y, which should be in screen space, to local space relateive to node.
     */
    public static Vec2<Float> toLocalSpace(final Node node, final float x, final float y)
    {
        final Vec4<Float> f =
            node.getWorldTransform(node.worldTransform).invert().mapVector(x, y, 0);
        return new Vec2<>(f.x, f.y);
    }

    @Override
    public boolean handleEvent(final GestureEvent event)
    {
        if (engine_ui_input(event))
        {
            return true;
        }
        engine_input(event);
        return false;
    }
}
