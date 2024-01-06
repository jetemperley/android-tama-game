package com.tama.core;

import android.graphics.Matrix;

import com.tama.thing.Bush;
import com.tama.util.Vec2;

import java.util.ArrayList;
import java.util.List;

public class BackpackWorld
{
    private World world;

    List<Button> buttons = new ArrayList<>();

    Matrix mat;
    Vec2<Float> offset = new Vec2<Float>(0f, 0f);

    public BackpackWorld(Matrix worldMat, int size)
    {
        world = new World(size);
        world.add(new Bush(), 0, 0);
        mat = new Matrix();
        buttons.add(new Button(0, 0, mat){});

    }

    void draw(DisplayAdapter display)
    {
        display.translate(offset.x, offset.y);
        display.d
        world.draw(display);
        display.translate(-offset.x, -offset.y);
    }

    void update()
    {

    }
}
