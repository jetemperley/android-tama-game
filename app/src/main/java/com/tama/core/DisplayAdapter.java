package com.tama.core;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.tama.apptest.R;
import com.tama.thing.Pet;
import com.tama.thing.Thing;
import com.tama.util.Vec2;

public interface DisplayAdapter
{
    void displayWorld(WorldObject t);

    void displayWorld(Displayable d, float x, float y);

    void displayUI(Thing t);

    void displayManual(Displayable d, float x, float y);

}

class AndroidDisplay implements DisplayAdapter
{
    Matrix mat, idmat;

    Canvas canvas;
    int cellSize;
    public Rect view = new Rect();
    private int xoff, yoff;

    AndroidDisplay(int cellSize)
    {
        this.cellSize = cellSize;
        mat = new Matrix();
        mat.setScale(3, 3);
        idmat = new Matrix();
        idmat.setScale(5, 5);
        idmat.preTranslate(0, view.top / 5);
    }

    public void displayWorld(WorldObject t)
    {
        if (t.sprite == null)
        {
            Log.d("Display Adapter ",
                  t.getClass().getName() + " sprite was null");
            return;
        }
        canvas.drawBitmap(t.sprite.getSprite(),
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
        canvas.drawBitmap(Assets.getSprite(Assets.static_inv).getSprite(),
                          selectedScreenPos.x,
                          selectedScreenPos.y,
                          GameActivity.black);

        Matrix mat = new Matrix();
        mat.setScale(uiscale, uiscale);


        // Rect ui = new Rect(uigap, uigap, uisize - uigap, uisize - uigap);
        Matrix old = canvas.getMatrix();
        canvas.setMatrix(new Matrix());
        canvas.drawRect(0,
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

            j = GameActivity.white.breakText(str,
                                             i,
                                             str.length(),
                                             true,
                                             uisize - uigap,
                                             null);
            j = j + i;
            // Log.d(getClass().getName(), i + " to " + j);
            canvas.drawText(str,
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
            canvas.drawBitmap(Assets.sprites.get(R.drawable.static_heart).getSprite(),
                              mat,
                              GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.health].getProp());

            mat.preTranslate(cellSize, -cellSize);
            canvas.drawBitmap(Assets.sprites.get(R.drawable.static_fork).getSprite(),
                              mat,
                              GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.hunger].getProp());

            mat.preTranslate(cellSize, -cellSize);
            canvas.drawBitmap(Assets.sprites.get(R.drawable.static_energy2).getSprite(),
                              mat,
                              GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.energy].getProp());

            mat.preTranslate(cellSize, -cellSize);
            canvas.drawBitmap(Assets.sprites.get(R.drawable.static_zzz).getSprite(),
                              mat,
                              GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.sleep].getProp());

        }

        canvas.setMatrix(old);
    }

    private void drawBar(Matrix mat, float prop)
    {

        canvas.drawBitmap(Assets.sprites.get(R.drawable.static_bar).getSprite(),
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

    public void displayManual(Displayable d, float x, float y)
    {
        canvas.drawBitmap(d.getSprite(), x, y, GameActivity.black);

    }

    public void displayWorld(Displayable d, float x, float y)
    {
        canvas.drawBitmap(d.getSprite(), x*16, y*16, GameActivity.black);
    }

    void offset(int x, int y)
    {
        xoff += x;
        yoff += y;
    }

    float[] convertScreenToWorld(float x, float y)
    {

        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {
                (x - f2[2]) / 16,
                (y - f2[5] - view.top) / 16};

        Matrix inv = new Matrix();
        mat.invert(inv);
        inv.mapVectors(f);
        return f;
    }

    Vec2<Float> convertWorldToScreen(Vec2<Float> worldPos)
    {
        return new Vec2<Float>(worldPos.x * 16, worldPos.y * 16);
    }


}
