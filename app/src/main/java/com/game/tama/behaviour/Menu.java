package com.game.tama.behaviour;

import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.Drawable;
import com.game.tama.core.Updateable;
import com.game.tama.ui.Button;
import com.game.tama.ui.ButtonManager;

import java.util.ArrayList;
import java.util.List;

public class Menu extends Behaviour
{
    ButtonManager buttonManager = new ButtonManager();
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
            buttonManager.add((Button)element);
            return;
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

    }


}
