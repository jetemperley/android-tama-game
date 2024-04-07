package com.game.tama.ui;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.GestureEventHandler;
import com.game.tama.core.Drawable;
import com.game.tama.core.Loadable;
import com.game.tama.core.Updateable;

import java.util.LinkedHashMap;
import java.util.Map;

public class UINode extends UIComposite
{
    private Map<Object, UIComposite> childMap = new LinkedHashMap<>();

    public void addChild(Object key, UIComposite child)
    {
        childMap.put(key, child);
    }

    public void removeChild(Object key)
    {
        childMap.remove(key);
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
    public void load()
    {
        for (UIComposite ui : childMap.values())
        {
            ui.load();
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
    public void setMatrix(Matrix mat)
    {
        this.matrix = mat;
        for (UIComposite ui : childMap.values())
        {
            ui.setMatrix(mat);
        }
    }
}
