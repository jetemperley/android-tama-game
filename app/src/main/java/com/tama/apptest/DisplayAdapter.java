package com.tama.apptest;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public interface DisplayAdapter {
    void displayWorld(WorldObject t);
    void displayUI(Inventory t);
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
    public void displayUI(Inventory inv){
        Matrix mat = new Matrix();
        // find the width of 1 inv space
        float cell = screenWidth/inv.len();
        mat.setScale(cell/cellSize, cell/cellSize);
        canvas.setMatrix(mat);
        for (int i = 0; i < inv.len(); i++){
            Thing t = inv.get(i);
            canvas.drawBitmap(Assets.sprites.get(R.drawable.static_inv).getSprite(), 0, 0, GameActivity.black);
            if (t != null)
                canvas.drawBitmap(t.loc.sprite.getUISprite(), 0, 0, GameActivity.black);
            mat.preTranslate(cellSize, 0);
            canvas.setMatrix(mat);
        }
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
