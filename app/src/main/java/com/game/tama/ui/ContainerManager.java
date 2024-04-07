package com.game.tama.ui;

import com.game.android.DisplayAdapter;
import com.game.tama.core.Container;

import java.util.ArrayList;
import java.util.List;

public class ContainerManager
{
    public List<Container> containers;

    public ContainerManager()
    {
        containers = new ArrayList<>();
    }

    public void draw(DisplayAdapter display)
    {
        for (Container container : containers)
        {
            container.drawContainer(display);
        }
    }

    public void toggle(Container container)
    {
        if (containers.contains(container))
        {
            containers.remove(container);
            return;
        }
        containers.add(container);
    }

    public void update()
    {

    }
}
