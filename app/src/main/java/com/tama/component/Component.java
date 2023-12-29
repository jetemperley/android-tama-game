package com.tama.component;

import com.tama.thing.Thing;

public abstract class Component
{
    Thing parent;
    public Component(Thing parent)
    {
        this.parent = parent;
    }
    public void start(){}
    public void update(){}
}
