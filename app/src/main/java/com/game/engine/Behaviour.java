package com.game.engine;

import com.game.engine.gesture.gestureEvent.GestureEvent;

public abstract class Behaviour implements Updateable, Drawable
{
    final public Node node;
    private boolean enabled = true;

    public Behaviour(final Node parent)
    {
        if (parent == null)
        {
            throw new RuntimeException("A behaviours parent cannot be null.");
        }
        this.node = parent;
        parent.addBehaviour(this);
        BehaviorStarter.subscribe(this);
    }

    public void start()
    {

    }

    public final <T extends Behaviour> T getBehaviour(final Class<T> clazz)
    {
        return node.getBehaviour(clazz);
    }

    public final void removeBehaviour(final Behaviour b)
    {
        node.removeBehaviour(b);
    }

    final void engine_draw(final DisplayAdapter display)
    {
        if (enabled)
        {
            draw(display);
        }
    }

    @Override
    public void draw(final DisplayAdapter display)
    {

    }

    final void engine_update()
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

    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
        return enabled && node.isEnabled();
    }

    final void engine_input(final GestureEvent event)
    {
        if (!enabled)
        {
            return;
        }

        handleInput(event);
    }

    public void handleInput(final GestureEvent event)
    {

    }

    /**
     * Processes the event and returns true if the event is consumed
     */
    final boolean engine_ui_input(final GestureEvent event)
    {
        if (!enabled)
        {
            return false;
        }
        return handleUiInput(event);

    }

    /**
     * Processes the event and returns true if the event is consumed
     */
    public boolean handleUiInput(final GestureEvent event)
    {
        return false;
    }
}
