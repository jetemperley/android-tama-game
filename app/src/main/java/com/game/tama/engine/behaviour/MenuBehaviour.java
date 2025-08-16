package com.game.tama.engine.behaviour;

import com.game.engine.Behaviour;
import com.game.engine.DisplayAdapter;
import com.game.engine.Node;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.tama.ui.UINode;
import com.game.tama.util.Vec4;

public class MenuBehaviour extends Behaviour
{
    public UINode root;

    public MenuBehaviour(final Node parent)
    {
        super(parent);
    }

    @Override
    public void update()
    {
        root.update();
    }

    @Override
    public void draw(final DisplayAdapter display)
    {
        root.draw(display);
    }

    @Override
    public boolean handleUiInput(final GestureEvent e)
    {
        final GestureEvent localEvent =
            e.transform(node.getWorldTransform().invert());
        return root.handleEvent(localEvent);
    }

    public boolean isInside(final float x, final float y)
    {
        if (!isEnabled())
        {
            return false;
        }
        final Vec4<Float> point = node.getWorldTransform().invert().mapVector(x, y, 0);
        return root.isInside(point.x, point.y);
    }
}
