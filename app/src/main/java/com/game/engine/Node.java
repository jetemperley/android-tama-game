package com.game.engine;

import com.game.android.Matrix4;

import java.util.ArrayList;
import java.util.List;

public class Node
{
    public final Class<? extends Transform> transformClass;
    protected Node parent = null;
    protected List<Node> children = new ArrayList<>();
    protected List<Behaviour> behaviours = new ArrayList<>();
    protected boolean enabled = true;

    public Transform localTransform;
    public Transform worldTransform;

    public Node(Class<? extends Transform> klass)
    {
        transformClass = klass;
        try
        {
            worldTransform = klass.newInstance();
            localTransform = klass.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException(
                "Transforms need a default constructor",
                e);
        }
    }

    public Node(Node parent)
    {
        this(parent.transformClass);
        setParent(parent);
    }

    public final void engine_start()
    {
        for (Behaviour b : behaviours)
        {
            b.start();
        }
        for (Node n : children)
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

        for (Behaviour b : behaviours)
        {
            b.engine_update();
        }

        for (Node node : children)
        {
            node.engine_update();
        }
    }

    public final void engine_draw(DisplayAdapter display)
    {
        if (!enabled)
        {
            return;
        }

        display.push();
        display.preConcat(localTransform);
        for (Behaviour b : behaviours)
        {
            b.engine_draw(display);
        }
        for (Node node : children)
        {
            node.engine_draw(display);
        }
        display.pop();
    }

    public void setParent(Node newParent)
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
    public Transform getWorldTransform(Transform out)
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

    public void removeBehaviour(Behaviour b)
    {
        behaviours.remove(b);
    }

    public <B extends Behaviour> B getBehaviour(Class<B> clazz)
    {
        for (Behaviour b : behaviours)
        {
            if (b.getClass() == clazz)
            {
                return (B) b;
            }
        }
        return null;
    }

    void addBehaviour(Behaviour b)
    {
        behaviours.add(b);
    }

    public void setEnabled(boolean enabled)
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
        catch (IllegalAccessException | InstantiationException e)
        {
            throw new RuntimeException("Could not instantiate transform.");
        }
    }

    public Node getRoot()
    {
        if (parent == null) {
            return this;
        }
        return parent.getRoot();
    }
}
