package com.game.tama.behaviour;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.Contains;
import com.game.tama.core.Drawable;
import com.game.tama.core.Loadable;
import com.game.tama.core.Updateable;
import com.game.tama.ui.SimpleButtonLeaf;
import com.game.tama.ui.UIComposite;
import com.game.tama.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MenuBehaviour extends Behaviour implements Loadable
{
    public UIComposite root;

    public MenuBehaviour(Node parent)
    {
        super(parent);
    }

    @Override
    public void update()
    {
        root.update();
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        root.draw(display);
    }

    @Override
    public boolean handle(GestureEvent e)
    {
        return root.handleEvent(e, node.worldTransform);
    }

    public void load()
    {
        root.load();
    }

    public boolean isInside(float x, float y)
    {
        if (!isEnabled())
        {
            return false;
        }
        return root.isInside(x, y, node.worldTransform);
    }
}
