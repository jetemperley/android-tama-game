package com.game.tama.behaviour;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.Drawable;
import com.game.tama.core.Updateable;
import com.game.tama.ui.Button;
import com.game.tama.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Menu extends Behaviour
{
    List<Button> buttons = new ArrayList<>();
    List<Updateable> updateables = new ArrayList<>();
    List<Drawable> drawables = new ArrayList<>();

    public Menu(Node parent)
    {
        super(parent);
    }

    public void add(Object element)
    {
        if (element instanceof Button)
        {
            buttons.add((Button)element);
        }

        if (element instanceof Updateable)
        {
            updateables.add((Updateable) element);
        }

        if (element instanceof Drawable)
        {
            drawables.add((Drawable) element);
        }
    }

    @Override
    public void update()
    {
        for (Updateable updateable : updateables)
        {
            updateable.update();
        }
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        for (Drawable drawable : drawables)
        {
            drawable.draw(display);
        }
    }

    @Override
    public boolean handle(GestureEvent e)
    {
        Matrix matrix = new Matrix();
        node.getWorldTransform(matrix);
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

    public void loadAssets()
    {
        for (Button b : buttons)
        {
            b.load();
        }
    }
}
