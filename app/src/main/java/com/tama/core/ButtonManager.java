package com.tama.core;

import android.graphics.Matrix;

import com.tama.gesture.GestureEvent;
import com.tama.gesture.GestureEventHandler;
import com.tama.gesture.SingleTap;
import com.tama.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ButtonManager implements GestureEventHandler
{
    private Matrix matrixUI = new Matrix();
    private List<Button> buttons = new ArrayList<>();

    public ButtonManager()
    {
        float scale = 6;
        matrixUI.setScale(scale, scale);

    }

    public void drawMenus(DisplayAdapter d)
    {
        d.setMatrix(matrixUI);
        for (Button butt : buttons)
        {
            butt.draw(d);
        }
    }

    public void add(Button butt)
    {
        buttons.add(butt);
    }

    public void loadAssets()
    {
        for (Button b : buttons)
        {
            b.loadAsset();
        }
    }

    public boolean handleEvent(GestureEvent e)
    {
        if (e.getClass() == SingleTap.class)
        {
            for (Button b : buttons)
            {
                Log.log(this, "checking button");
                if (b.isInside(e.x, e.y, matrixUI))
                {
                    Log.log(this, "clicked a button");
                    b.onClick();
                    return true;
                }
            }
        }
        return false;
    }
}
