package com.game.tama.core.thing.component;

import com.game.tama.core.thing.Thing;

public abstract class Component implements java.io.Serializable
{
    private Thing parent = null;

    public void start() {}

    public void update() {}

    public Thing getParent()
    {
        return parent;
    }

    public void setParent(final Thing thing)
    {
        if (parent != null)
        {
            throw new RuntimeException(
                "Cannot re-set the parent for a component");
        }
        parent = thing;
    }
}
