package com.game.tama.engine.behaviour;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.Loadable;
import com.game.tama.ui.UIComposite;
import com.game.tama.ui.UINode;

public class MenuBehaviour extends Behaviour
{
    public UINode root;

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

    public boolean isInside(float x, float y)
    {
        if (!isEnabled())
        {
            return false;
        }
        return root.isInside(x, y, node.worldTransform);
    }
}
