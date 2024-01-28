package com.tama.core;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.tama.thing.Thing;
import com.tama.util.Log;

import java.util.Comparator;
import java.util.PriorityQueue;

public interface DisplayAdapter
{
    /**
     * Display the object based on its x, y, and offsets
     *
     * @param t
     */
    void display(WorldObject t);

    /**
     * Dispay the sprite relative to the world array
     *
     * @param d  the thing to display
     * @param ax array position x
     * @param ay array position y
     */
    void display(Displayable d, float ax, float ay);

    /**
     * Display at the povided pixel location, with respect to the current
     * matrix
     *
     * @param d Thing to display
     * @param x Pixel x
     * @param y Pixel y
     */
    void displayAbsolute(Displayable d, float x, float y);

    void setMatrix(Matrix mat);

    Matrix getMatrix();

    void translate(float x, float y);

    void push();

    void pop();

    void drawLine(float x1, float y1, float x2, float y2);

    void drawRect(float x, float y, float width, float height);

    void preConcat(Matrix mat);
}

class AndroidDisplay implements DisplayAdapter
{
    Matrix idMatrix;
    Matrix currentMatrix = new Matrix();

    Canvas canvas;
    int cellSize;
    public Rect view = new Rect();

    AndroidDisplay(int cellSize)
    {
        this.cellSize = cellSize;
        idMatrix = new Matrix();
        idMatrix.reset();
    }

    public void display(WorldObject t)
    {
        if (t.sprite == null)
        {
            Log.log(
                t,
                "sprite was null");
            return;
        }
        canvas.drawBitmap(
            t.sprite.getSprite(),
            t.x * cellSize + t.xoff * cellSize / 100f,
            t.y * cellSize + t.yoff * cellSize / 100f,
            GameActivity.black);
    }

    public void displayAbsolute(Displayable d, float x, float y)
    {
        canvas.drawBitmap(d.getSprite(), x, y, GameActivity.black);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2)
    {
        canvas.drawLine(x1, y1, x2, y2, GameActivity.white);
    }

    @Override
    public void drawRect(float x, float y, float width, float height)
    {
        canvas.drawRect(x, y, x + width, y + height, GameActivity.red);
    }

    public void display(Displayable d, float x, float y)
    {
        canvas.drawBitmap(d.getSprite(), x * 16, y * 16, GameActivity.black);
    }

    public void setMatrix(Matrix mat)
    {
        currentMatrix.set(mat);
        canvas.setMatrix(currentMatrix);
    }

    @Override
    public Matrix getMatrix()
    {
        return currentMatrix;
    }

    @Override
    public void translate(float dx, float dy)
    {
        canvas.translate(dx, dy);
    }

    @Override
    public void push()
    {
        canvas.save();
    }

    @Override
    public void pop()
    {
        canvas.restore();
    }

    /**
     * preConcat effect the matrix in its *local* frame of reference
     * @param mat
     */
    @Override
    public void preConcat(Matrix mat)
    {
        currentMatrix.preConcat(mat);
        canvas.concat(mat);
    }
}

class DepthDisplay implements DisplayAdapter
{

    DisplayAdapter display;
    PriorityQueue<WorldObject> draws;
    boolean check = true;

    DepthDisplay()
    {
        draws = new PriorityQueue<>(200, new DepthComp());
    }

    public void display(WorldObject t)
    {
        draws.add(t);
    }

    @Override
    public void display(Displayable d, float x, float y)
    {
        throw new RuntimeException("Operation not supported");
    }

    public void displayAbsolute(Displayable d, float x, float y)
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2)
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void drawRect(float x, float y, float width, float height)
    {
        throw new RuntimeException("Operation not supported");
    }

    void drawQ()
    {
        if (display != null)
        {
            WorldObject b;
            while (!draws.isEmpty())
            {
                b = draws.poll();
                display.display(b);
            }
        }
        check = false;
    }

    void clearQ()
    {
        draws.clear();
    }

    class DepthComp implements Comparator<WorldObject>
    {
        public int compare(WorldObject a, WorldObject b)
        {
            if (b.flat && a.flat)
            {
                return 0;
            }
            if (a.flat)
            {
                return -1;
            }
            if (b.flat)
            {
                return 1;
            }
            return (a.y + a.yoff / 100f) > (b.y + b.yoff / 100f) ? 1 : -1;
        }
    }

    @Override
    public void setMatrix(Matrix mat)
    {
        display.setMatrix(mat);
    }

    @Override
    public Matrix getMatrix()
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void translate(float x, float y)
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void push()
    {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void pop()
    {
        throw new RuntimeException("Operation not supported");
    }
    @Override
    public void preConcat(Matrix mat)
    {
        throw new RuntimeException("Operation not supported");
    }

}

