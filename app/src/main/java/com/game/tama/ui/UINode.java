package com.game.tama.ui;

import com.game.engine.DisplayAdapter;
import com.game.engine.gesture.gestureEvent.GestureEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class UINode extends UIComposite
{
    private Map<Object, UIComposite> childMap = new LinkedHashMap<>();

    public void add(Object key, UIComposite child)
    {
        childMap.put(key, child);
    }

    public UIComposite remove(Object key)
    {
        return childMap.remove(key);
    }

    public UIComposite get(Object key)
    {
        return childMap.get(key);
    }

    @Override
    public boolean handleEvent(GestureEvent event)
    {
        for (UIComposite ui : childMap.values())
        {
            if (ui.handleEvent(event))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        for (UIComposite ui : childMap.values())
        {
            ui.draw(display);
        }
    }

    @Override
    public void update()
    {
        for (UIComposite ui : childMap.values())
        {
            ui.update();
        }
    }

    @Override
    public boolean isInside(float x, float y)
    {
        for (UIComposite ui : childMap.values())
        {
            if (ui.isInside(x, y))
            {
                return true;
            }
        }
        return false;
    }


}
