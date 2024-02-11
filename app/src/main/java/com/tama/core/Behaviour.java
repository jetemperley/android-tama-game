package com.tama.core;

public abstract class Behaviour implements Updateable
{
    public Node parent;

    public Behaviour(Node parent)
    {
        this.parent = parent;
    }

}
