package com.game.tama.ui;


import android.graphics.Matrix;

import com.game.android.gesture.GestureEventHandler;
import com.game.tama.core.Drawable;
import com.game.tama.core.Loadable;
import com.game.tama.core.Updateable;

// TODO make all the composite objects positions relative to one another

public abstract class UIComposite implements GestureEventHandler, Drawable,
                                             Loadable, Updateable
{
    public Matrix matrix = null;
    public void setMatrix(Matrix mat)
    {
        this.matrix = mat;
    }

}
