package com.game.tama.ui;


import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.engine.Drawable;
import com.game.engine.Updateable;

// TODO make all the composite objects positions relative to one another

public abstract class UIComposite implements Drawable, Updateable
{
    /**
     * Returns whether point x y is inside this button. X and Y should be
     * in the UI tree's frame of reference
     *
     * @param x      point to check
     * @param y      point to check
     * @return
     */
    public abstract boolean isInside(float x, float y);

    /**
     * The gesture event should already be transformed to this tree's
     * frame of reference
     * @param e
     * @return true if the event is consumed
     */
    public abstract boolean handleEvent(GestureEvent e);
}
