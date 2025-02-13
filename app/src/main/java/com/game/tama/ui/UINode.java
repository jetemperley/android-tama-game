package com.game.tama.ui;

import android.graphics.Matrix;

import com.game.engine.DisplayAdapter;
import com.game.android.gesture.GestureEvent;

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
    public boolean handleEvent(GestureEvent event, Matrix mat)
    {
        for (UIComposite ui : childMap.values())
        {
            if (ui.handleEvent(event, mat))
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
    public boolean isInside(float x, float y, Matrix matrix)
    {
        for (UIComposite ui : childMap.values())
        {
            if (ui.isInside(x, y, matrix))
            {
                return true;
            }
        }
        return false;
    }


}
