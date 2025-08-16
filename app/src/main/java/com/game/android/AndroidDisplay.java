package com.game.android;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.game.engine.DisplayAdapter;
import com.game.engine.Transform;
import com.game.tama.core.Sprite;
import com.game.tama.core.world.WorldObject;
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

    public AndroidDisplay(final int cellSize)
    {
        this.cellSize = cellSize;
        idMatrix = new Matrix();
        idMatrix.reset();
    }

    @Override
    public void draw(final WorldObject worldObject)
    {
        if (worldObject.sprite == null)
        {
            Log.log(
                worldObject,
                "sprite was null");
        }
        //        canvas.drawBitmap(
        //            worldObject.sprite.getSpriteId(),
        //            worldObject.x * cellSize + worldObject.xoff * cellSize / 100f,
        //            worldObject.y * cellSize + worldObject.yoff * cellSize / 100f,
        //            GameActivity.black);
    }

    @Override
    public void draw(final Sprite sprite, final float x, final float y, final float z)
    {
        //        canvas.drawBitmap(sprite.getSpriteId(), x, y, GameActivity.black);
    }

    @Override
    public void drawSprite(final Sprite sprite, final float x, final float y)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawSprite(final Sprite sprite)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawLine(final float x1, final float y1, final float x2, final float y2)
    {
        canvas.drawLine(x1, y1, x2, y2, GameActivity.white);
    }

    @Override
    public void clearRect(final float x, final float y, final float width, final float height)
    {
        canvas.drawRect(x, y, x + width, y + height, GameActivity.black);
    }

    @Override
    public void setTransform(final Transform mat)
    {
        currentMatrix.setValues(mat.getValues());
        canvas.setMatrix(currentMatrix);
    }

    @Override
    public Transform getTransform()
    {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public void translate(final float dx, final float dy)
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
    public void preConcat(final Transform mat)
    {
        // currentMatrix.preConcat(mat.getValues());
        // canvas.concat(mat);
    }

    @Override
    public void drawUi(final Sprite sprite, final float x, final float y)
    {

    }
}
