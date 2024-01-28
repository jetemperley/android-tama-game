package com.tama.core;

import android.graphics.Matrix;

import com.tama.gesture.GestureEvent;
import com.tama.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ButtonManager extends Interactive
{
    private Matrix matrix = new Matrix();
    private List<Button> buttons = new ArrayList<>();

    public ButtonManager(Matrix mat)
    {
        this.matrix = mat;
    }

    @Override
    public void update()
    {
        for (Button b : buttons)
        {
            b.update();
        }
    }

    public void draw(DisplayAdapter d)
    {
        d.push();
        d.preConcat(matrix);
        for (Button butt : buttons)
        {
            butt.draw(d);
        }
        d.pop();
    }

    public void add(Button butt)
    {
        buttons.add(butt);
    }

    public void loadAssets()
    {
        for (Button b : buttons)
        {
            b.load();
        }
    }

    public boolean handleEvent(GestureEvent e)
    {
        for (Button b : buttons)
        {
            Log.log(this, "checking button");
            if (b.isInside(e.x, e.y, matrix))
            {
                if (b.handleEvent(e))
                {
                    return true;
                }
            }
        }

        return e.type() == GestureEvent.Type.press;
    }
}
