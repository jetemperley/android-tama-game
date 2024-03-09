package com.game.engine;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.tama.core.Drawable;
import com.game.tama.core.Updateable;

import java.util.ArrayList;
import java.util.List;

public class Node implements Updateable, Drawable
{
    public Node currentParent = null;
    public Matrix transform = new Matrix();
    public List<Node> children = new ArrayList<>();
    private List<Behaviour> behaviours = new ArrayList<>();
    private boolean enabled = true;

    public Node()
    {
        this(null);
    }

    public Node(Node parent)
    {
        setParent(parent);
    }

    @Override
    public void update()
    {
        if (!enabled)
        {
            return;
        }

        for (Behaviour b : behaviours)
        {
            b.update();
        }

        for (Node node : children)
        {
            if (node.enabled)
            {
                node.update();
            }
        }
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        if (!enabled)
        {
            return;
        }

        for (Behaviour b : behaviours)
        {
            b.draw(display);
        }
        for (Node node : children)
        {
            if (node.enabled)
            {
                node.draw(display);
            }
        }
    }

    public void setParent(Node parent)
    {
        clearCurrentParent();
        if (parent == null)
        {
            return;
        }
        currentParent = parent;
        if (!parent.children.contains(this))
        {
            parent.children.add(this);
        }
    }

    private void clearCurrentParent()
    {
        if (currentParent == null)
        {
            return;
        }

        currentParent.children.remove(this);
        currentParent = null;
    }

    public void getTransform(Matrix out)
    {
        if (currentParent == null)
        {
            out.set(transform);
            return;
        }
        out.preConcat(transform);
        return;
    }

    public <T extends Behaviour> T addBehaviour(Class<T> clazz)
    {
        T b = null;
        try
        {
            b = clazz.getConstructor(Node.class).newInstance(this);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        behaviours.add(b);
        return b;
    }

    public void removeBehaviour(Behaviour b)
    {
        behaviours.remove(b);
    }

    public <T extends Behaviour> T getBehaviour(Class<T> clazz)
    {
        for (Behaviour b : behaviours)
        {
            if (b.getClass() == clazz)
            {
                return (T) b;
            }
        }
        return null;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
