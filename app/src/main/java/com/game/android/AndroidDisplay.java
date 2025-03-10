package com.game.android;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.game.tama.core.Sprite;
import com.game.tama.core.WorldObject;
import com.game.tama.util.Log;

public class AndroidDisplay implements DisplayAdapter
{
    Matrix idMatrix;
    Matrix currentMatrix = new Matrix();

    Canvas canvas;
    int cellSize;
    /**
     * The rect bounds of the game activity
     */
    public Rect view = new Rect();

    public AndroidDisplay(int cellSize)
    {
        this.cellSize = cellSize;
        idMatrix = new Matrix();
        idMatrix.reset();
    }

    public void drawArr(WorldObject t)
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

    public void drawSprite(Sprite d, float x, float y)
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
        canvas.drawRect(x, y, x + width, y + height, GameActivity.black);
    }

    public void drawArr(Sprite d, float x, float y)
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
        currentMatrix.preTranslate(dx, dy);
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

    @Override
    public void preConcat(Matrix mat)
    {
        currentMatrix.preConcat(mat);
        canvas.concat(mat);
    }
}
