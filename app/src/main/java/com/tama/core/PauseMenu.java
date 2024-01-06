package com.tama.core;

import android.graphics.Matrix;
import android.graphics.Rect;

import com.tama.gesture.GestureEvent;
import com.tama.util.Log;
import com.tama.util.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

public class PauseMenu extends InputHandler
{
    public List<Button> buttons;

    private Matrix matrixUI;

    public PauseMenu()
    {
        float scale = 6;
        matrixUI = new Matrix();
        matrixUI.setScale(scale, scale);
        Rect size = GameActivity.screenSize;
        buttons = new ArrayList<>();
        float[] f = MatrixUtil.convertScreenToMatrix(matrixUI, size.width()/2, size.height()/2);
        buttons.add(new Button(f[0], f[1], matrixUI)
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
        d.setMatrix(matrixUI);
        for (Button butt : buttons)
        {
            butt.draw(d);
        }
    }

    public void singleTapConfirmed(float x, float y)
    {
        for (Button b : buttons)
        {
            if (b.isInside(x, y))
            {
                Log.log(this, "clicked a button");
                b.onClick();
                return;
            }
        }
    }

    @Override
    public void handleEvent(GestureEvent event)
    {
        event.callEvent(this);
    }
}
