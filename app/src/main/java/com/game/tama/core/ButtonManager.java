package com.game.tama.core;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.tama.util.Log;

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
                return b.handleEvent(e);

            }
        }

        return false;
    }
}
