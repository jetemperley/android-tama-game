package com.game.tama.core;

import com.game.android.DisplayAdapter;
import com.game.engine.Behaviour;
import com.game.engine.Node;

import java.util.ArrayList;
import java.util.List;

public class ContainerManager extends Behaviour
{
    public List<Container> containers;

    public ContainerManager(Node parent)
    {
        super(parent);
        containers = new ArrayList<>();
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        for (Container container : containers)
        {
            container.drawWorld(display);
        }
    }

    @Override
    public void update()
    {

    }
}
