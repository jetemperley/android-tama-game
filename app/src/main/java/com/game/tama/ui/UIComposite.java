package com.game.tama.ui;


import android.graphics.Matrix;

import com.game.android.gesture.GestureEvent;
import com.game.engine.Drawable;
import com.game.engine.Updateable;

// TODO make all the composite objects positions relative to one another

public abstract class UIComposite implements Drawable, Updateable
{

    public abstract boolean isInside(float x, float y, Matrix matrix);
    public abstract boolean handleEvent(GestureEvent e, Matrix mat);

}
