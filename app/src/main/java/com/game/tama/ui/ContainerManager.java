package com.game.tama.ui;

import com.game.engine.DisplayAdapter;
import com.game.engine.gesture.gestureEvent.GestureEvent;
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

    public boolean handleEvent(GestureEvent e)
    {
        for (int i = containers.size() - 1; i >= 0; i--)
        {
            if (containers.get(i).handleEvent(e))
            {
                return true;
            }
        }
        return false;
    }
}
