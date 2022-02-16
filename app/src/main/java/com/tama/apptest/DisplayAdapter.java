package com.tama.apptest;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public interface DisplayAdapter {
    void displayWorld(WorldObject t);
    void displayUI(Thing t);
    void displayManual(Displayable d, float x, float y);

}

class AndroidDisplay implements DisplayAdapter {

    Canvas canvas;
    int cellSize;
    int screenWidth, screenHeight;
    int topIn;
    private int xoff, yoff;

    AndroidDisplay(int cellSize, int screenWidth, int screenHeight, int topIn){

        this.cellSize = cellSize;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.topIn = topIn;

    }

    public void displayWorld(WorldObject t){

        if (t.sprite == null) {
            Log.d("display adapter ", "sprite was null");
            return;
        }
        canvas.drawBitmap( t.sprite.getSprite(),
                t.x*cellSize + t.xoff*cellSize/100f,
                t.y*cellSize + t.yoff*cellSize/100f,
                GameActivity.black);
    }

    // this needs to be completed
    public void displayUI(Thing t){
        int uiscale = 12;
        int uisize = cellSize*uiscale, uigap = 0;

        Matrix mat = new Matrix();
        mat.setScale(uiscale, uiscale);

        // Rect ui = new Rect(uigap, uigap, uisize - uigap, uisize - uigap);
        Matrix old = canvas.getMatrix();
        canvas.setMatrix(new Matrix());
        canvas.drawRect(0, 0, cellSize*uiscale*4, cellSize*1.5f*uiscale, GameActivity.black);
        canvas.drawBitmap(t.loc.sprite.getUISprite(), mat, GameActivity.black);

        String str = t.getDescription();
        int y = 40;

        // canvas.drawText(s, 200, y, GameActivity.white);
        // draw description text
        int i = 0;
        int j = 0;
        while (i < str.length()){

            j = GameActivity.white.breakText(
                    str, i, str.length(),
                    true, uisize - uigap, null);
            j = j+i;
            // Log.d(getClass().getName(), i + " to " + j);
            canvas.drawText(str, i, j, uigap, uisize + uigap +y, GameActivity.white);
            y+=40;
            i = j;
        }

        // draw stats
        if (t instanceof Pet){
            Pet p  = (Pet)t;

            mat.preTranslate(cellSize, 0);
            canvas.drawBitmap(Assets.sprites.get(R.drawable.static_heart).getSprite(),
                    mat, GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.health].getProp());

            mat.preTranslate(cellSize, -cellSize);
            canvas.drawBitmap(Assets.sprites.get(R.drawable.static_fork).getSprite(),
                    mat, GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.hunger].getProp());

            mat.preTranslate(cellSize, -cellSize);
            canvas.drawBitmap(Assets.sprites.get(R.drawable.static_energy2).getSprite(),
                    mat, GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.energy].getProp());

            mat.preTranslate(cellSize, -cellSize);
            canvas.drawBitmap(Assets.sprites.get(R.drawable.static_zzz).getSprite(),
                    mat, GameActivity.black);
            mat.preTranslate(0, cellSize);
            drawBar(mat, p.stats.stats[Stats.sleep].getProp());

        }

        canvas.setMatrix(old);
    }

    private void drawBar(Matrix mat, float prop){

        canvas.drawBitmap(Assets.sprites.get(R.drawable.static_bar).getSprite(),
                mat, GameActivity.black);
        mat.preTranslate(3, 3);
        float[] f = {0, 0, (int)(prop*10), 2};
        mat.mapPoints(f);
        canvas.drawRect(f[0], f[1], f[2], f[3], GameActivity.white);
                mat.preTranslate(-3, -3);

    }

    public void displayManual(Displayable d, float x, float y){
        Matrix mat = canvas.getMatrix();
        canvas.drawBitmap(d.getSprite(), x, y, GameActivity.black);

    }

    void offset(int x, int y){
        xoff += x;
        yoff += y;
    }


}
