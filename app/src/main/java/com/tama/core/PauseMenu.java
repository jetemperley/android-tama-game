package com.tama.core;

import android.graphics.Matrix;
import android.graphics.Rect;

import com.tama.gesture.GestureEvent;
import com.tama.util.Log;
import com.tama.util.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

public class PauseMenu extends Interactive
{
    public ButtonManager buttons;

    public PauseMenu()
    {
        Rect size = GameActivity.screenSize;
        buttons = new ButtonManager();
        buttons.add(new Button(50, 50)
        {
            @Override
            void onClick()
            {
                GameManager.INST.play();
            }
        });
    }

    @Override
    public void draw(DisplayAdapter adapter)
    {
        drawMenus(adapter);
    }

    void drawMenus(DisplayAdapter d)
    {
        buttons.drawMenus(d);
    }

    @Override
    public boolean handleEvent(GestureEvent event)
    {
        if (buttons.handleEvent(event))
            return true;
        event.callEvent(this);
        return true;
    }
}
