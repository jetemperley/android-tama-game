package com.game.engine;

import android.support.annotation.NonNull;

import com.game.tama.core.Drawable;
import com.game.tama.core.Updateable;

public abstract class Behaviour implements Updateable, Drawable
{
    final public Node parent;

    public boolean enabled = true;

    public Behaviour(Node parent)
    {
        if (parent == null)
        {
            throw new RuntimeException("A behaviours parent cannot be null.");
        }
        this.parent = parent;
    }

    public <T extends Behaviour> T getBehaviour(Class<T> clazz)
    {
        return parent.getBehaviour(clazz);
    }

    public void removeBehaviour(Behaviour b)
    {
        parent.removeBehaviour(b);
    }

    public <T extends Behaviour> T addBehaviour(Class<T> clazz)
    {
        return parent.addBehaviour(clazz);
    }
}
