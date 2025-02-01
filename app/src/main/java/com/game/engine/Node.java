package com.game.engine;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.tama.core.Drawable;
import com.game.tama.core.Updateable;

import java.util.ArrayList;
import java.util.List;

public class Node implements Updateable, Drawable
{
    Node parent = null;
    List<Node> children = new ArrayList<>();
    private List<Behaviour> behaviours = new ArrayList<>();
    private boolean enabled = true;
    public Matrix localTransform = new Matrix();
    public Matrix worldTransform = new Matrix();

    public Node()
    {
        this(null);
    }

    public Node(Node parent)
    {
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

    @Override
    public void update()
    {

    }

    @Override
    public void draw(DisplayAdapter display)
    {

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
     * @param out
     * @return out
     */
    public Matrix getWorldTransform(Matrix out)
    {
        if (parent == null)
        {
            out.set(localTransform);
            return out;
        }
        parent.getWorldTransform(out);
        out.preConcat(localTransform);
        return out;
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
}
