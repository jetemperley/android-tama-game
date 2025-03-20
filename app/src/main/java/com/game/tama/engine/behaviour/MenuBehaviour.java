package com.game.tama.engine.behaviour;

import com.game.engine.DisplayAdapter;
import com.game.engine.Transform;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.engine.Behaviour;
import com.game.engine.Node;
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
        GestureEvent localEvent =
            e.transform(getWorldTransform(node.localTransform.copy()
                .reset()).invert());
        return root.handleEvent(localEvent);
    }

    public boolean isInside(float x, float y)
    {
        if (!isEnabled())
        {
            return false;
        }
        float[] point = node.getWorldTransform().invert().mapVector(x, y, 0);
        return root.isInside(point[0], point[1]);
    }
}
