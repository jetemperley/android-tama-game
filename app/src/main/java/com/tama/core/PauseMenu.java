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
    private Text text, text2;


    public PauseMenu()
    {
        for (byte b = '!'; b < 'z'; b++)
        {
            Log.log(this, b + " " + (char)b);
        }
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
        text = new Text("a hello z", 0, 0);
        text2 = new Text("aBz, cool.", 0, 8);
    }

    @Override
    public void draw(DisplayAdapter d)
    {
        buttons.drawMenus(d);
        text.draw(d);
        text2.draw(d);

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
