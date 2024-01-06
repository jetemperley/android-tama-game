package com.tama.core;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.tama.apptest.R;
import com.tama.thing.Pet;
import com.tama.thing.Thing;
import com.tama.util.Log;
import com.tama.util.Vec2;

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

    void displayUI(Thing t);

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

    void translate(float x, float y);

    void push();

    void pop();

    void drawLine(float x1, float y1, float x2, float y2);

    void drawRect(float x, float y, float width, float height);
}

class AndroidDisplay implements DisplayAdapter
{
    Matrix worldMat, idMat, uiMat;

    Canvas canvas;
    int cellSize;
    public Rect view = new Rect();

    AndroidDisplay(int cellSize)
    {
        this.cellSize = cellSize;
        worldMat = new Matrix();
        worldMat.setScale(3, 3);
        idMat = new Matrix();
        idMat.setScale(5, 5);
        idMat.preTranslate(0, view.top / 5);
        uiMat = new Matrix();
        uiMat.setScale(3, 3);
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

    // this needs to be completed
    public void displayUI(Thing t)
    {
        int uiscale = 12;
        int uisize = cellSize * uiscale, uigap = 0;

        Vec2<Float> selectedScreenPos =
            convertWorldToScreen(t.loc.getWorldPos());
        canvas.drawBitmap(
            Assets.getSprite(Assets.static_inv).getSprite(),
            selectedScreenPos.x,
            selectedScreenPos.y,
            GameActivity.black);

        Matrix mat = new Matrix();
        mat.setScale(uiscale, uiscale);

        // Rect ui = new Rect(uigap, uigap, uisize - uigap, uisize - uigap);
        Matrix old = canvas.getMatrix();
        canvas.setMatrix(new Matrix());
        canvas.drawRect(
            0,
            0,
            cellSize * uiscale * 4,
            cellSize * 1.5f * uiscale,
            GameActivity.black);
        canvas.drawBitmap(t.loc.sprite.getUISprite(), mat, GameActivity.black);

        String str = t.getDescription();
        int y = 40;

        // canvas.drawText(s, 200, y, GameActivity.white);
        // draw description text
        int i = 0;
        int j = 0;
        while (i < str.length())
        {

            j = GameActivity.white.breakText(
                str,
                i,
                str.length(),
                true,
                uisize - uigap,
                null);
            j = j + i;
            // Log.d(getClass().getName(), i + " to " + j);
            canvas.drawText(
                str,
                i,
                j,
                uigap,
                uisize + uigap + y,
                GameActivity.white);
            y += 40;
            i = j;
        }

        // draw stats
        if (t instanceof Pet)
        {
            Pet p = (Pet) t;

            mat.preTranslate(cellSize, 0);
            canvas.drawBitmap(
                Assets.sprites.get(R.drawable.static_heart).getSprite(),
                mat,
                GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.health].getProp());

            mat.preTranslate(cellSize, -cellSize);
            canvas.drawBitmap(
                Assets.sprites.get(R.drawable.static_fork).getSprite(),
                mat,
                GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.hunger].getProp());

            mat.preTranslate(cellSize, -cellSize);
            canvas.drawBitmap(
                Assets.sprites.get(R.drawable.static_energy2).getSprite(),
                mat,
                GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.energy].getProp());

            mat.preTranslate(cellSize, -cellSize);
            canvas.drawBitmap(
                Assets.sprites.get(R.drawable.static_zzz).getSprite(),
                mat,
                GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.sleep].getProp());
        }

        canvas.setMatrix(old);
    }

    private void drawBar(Matrix mat, float prop)
    {

        canvas.drawBitmap(
            Assets.sprites.get(R.drawable.static_bar).getSprite(),
            mat,
            GameActivity.black);
        mat.preTranslate(3, 3);
        float[] f = {
            0,
            0,
            (int) (prop * 10),
            2};
        mat.mapPoints(f);
        canvas.drawRect(f[0], f[1], f[2], f[3], GameActivity.white);
        mat.preTranslate(-3, -3);
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
        canvas.drawRect(x, y, x + width, y + height, GameActivity.white);
    }

    public void display(Displayable d, float x, float y)
    {
        canvas.drawBitmap(d.getSprite(), x * 16, y * 16, GameActivity.black);
    }

    float[] convertScreenToWorld(float x, float y)
    {

        float[] f2 = new float[9];
        worldMat.getValues(f2);
        float[] f = {
            (x - f2[2]) / 16,
            (y - f2[5] - view.top) / 16};

        Matrix inv = new Matrix();
        worldMat.invert(inv);
        inv.mapVectors(f);
        return f;
    }

    Vec2<Float> convertWorldToScreen(Vec2<Float> worldPos)
    {
        return new Vec2<Float>(worldPos.x * 16, worldPos.y * 16);
    }

    public void setMatrix(Matrix mat)
    {
        canvas.setMatrix(mat);
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

    public void displayUI(Thing t)
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

    public void setMatrix(Matrix mat)
    {
        display.setMatrix(mat);
    }

    @Override
    public void translate(float x, float y)
    {

    }

    @Override
    public void push()
    {

    }

    @Override
    public void pop()
    {

    }
}

