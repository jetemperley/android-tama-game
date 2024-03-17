package com.game.engine;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.GestureEventHandler;
import com.game.tama.core.Drawable;
import com.game.tama.core.Updateable;

public abstract class Behaviour implements Updateable, Drawable, GestureEventHandler
{
    final public Node parent;
    private boolean enabled = true;

    public Behaviour(Node parent)
    {
        if (parent == null)
        {
            throw new RuntimeException("A behaviours parent cannot be null.");
        }
        this.parent = parent;
        parent.addBehaviour(this);
    }

    public final <T extends Behaviour> T getBehaviour(Class<T> clazz)
    {
        return parent.getBehaviour(clazz);
    }

    public final void removeBehaviour(Behaviour b)
    {
        parent.removeBehaviour(b);
    }

    void engine_draw(DisplayAdapter display)
    {
        if (enabled)
        {
            draw(display);
        }
    }

    @Override
    public void draw(DisplayAdapter display)
    {

    }

    public void engine_update()
    {
        if (enabled)
        {
            update();
        }
    }

    @Override
    public void update()
    {

    }

    public boolean handleEvent(GestureEvent event)
    {
        if (isEnabled())
        {
            return handle(event);
        }
        return false;
    }

    public boolean handle(GestureEvent event)
    {
        return false;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
        return enabled && parent.isEnabled();
    }
}
