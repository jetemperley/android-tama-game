package com.game.tama.ui;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.tama.core.Drawable;
import com.game.tama.core.Updateable;
import com.game.tama.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ButtonManager implements Updateable, Drawable
{
    private List<Button> buttons = new ArrayList<>();

    @Override
    public void update()
    {
        for (Button b : buttons)
        {
            b.update();
        }
    }

    @Override
    public void draw(DisplayAdapter d)
    {
        for (Button butt : buttons)
        {
            butt.draw(d);
        }
    }

    public void add(Button button)
    {
        buttons.add(button);
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

        // TODO: get the matrix
        Matrix matrix = null;
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
