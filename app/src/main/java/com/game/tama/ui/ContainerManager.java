package com.game.tama.ui;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
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

    public boolean handleEvent(GestureEvent e, Matrix mat)
    {
        for (int i = containers.size() - 1; i >= 0; i--)
        {
            if (containers.get(i).handleEvent(e, mat))
            {
                return true;
            }
        }
        return false;
    }
}
