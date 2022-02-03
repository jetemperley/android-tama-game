package com.tama.apptest;
import android.graphics.Canvas;
import android.util.Log;

public interface DisplayAdapter {
    void displayWorld(WorldObject t);
    void displaySplit(WorldObject t);
    void displayUI(WorldObject t);
    void display(Displayable d, int x, int y, int xoff, int yoff);

}

class AndroidDisplay implements DisplayAdapter {

    Canvas canvas;
    int cellSize;
    int disXoff, disYoff;

    AndroidDisplay(int cellSize){
        this.cellSize = cellSize;
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

    public void displaySplit(WorldObject t){
        canvas.drawBitmap( t.sprite.getSprite(),
                t.x*cellSize + t.xoff*cellSize/100f,
                t.y*cellSize + t.yoff*cellSize/100f,
                GameActivity.black);
        canvas.drawBitmap( t.sprite.getSprite(),
                t.x*cellSize + t.xoff*cellSize/100f,
                t.y*cellSize + t.yoff*cellSize/(100f-50),
                GameActivity.black);
    }

    // this needs to be completed
    public void displayUI(WorldObject t){
        // canvas.drawBitmap(t.sprite.getUISprite());
    }

    public void display(Displayable d, int x, int y, int xoff, int yoff){
        canvas.drawBitmap(d.getSprite(),
                x*cellSize + xoff*cellSize/100f,
                y*cellSize + yoff*cellSize/100f,
                GameActivity.black);
    }

    void offset(int x, int y){
        disXoff += x;
        disYoff += y;
    }

}
