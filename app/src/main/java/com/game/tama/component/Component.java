package com.game.tama.component;

import com.game.tama.thing.Thing;

public abstract class Component implements java.io.Serializable
{
    Thing parent;
    public Component(Thing parent)
    {
        this.parent = parent;
    }
    public void start(){}
    public void update(){}
}
