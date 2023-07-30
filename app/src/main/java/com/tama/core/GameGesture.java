package com.tama.core;

import android.util.Log;
import android.view.MotionEvent;

import com.tama.util.Vec2;

class GameGesture extends Gesture
{

    private final GameActivity gameActivity;

    public GameGesture(GameActivity gameActivity)
    {
        this.gameActivity = gameActivity;
    }

    void singleTapConfirmed(float x, float y)
    {


        float[] f = gameActivity.displayAdapter.convertScreenToWorld(x, y);
        Log.d("Game Gesture", "tapped " + f[0] + " " + f[1]);
        gameActivity.game.select(f[0], f[1]);
        gameActivity.game.poke(f[0], f[1]);
    }

    void longPressConfirmed(float x, float y)
    {
        float[] f = gameActivity.displayAdapter.convertScreenToWorld(x, y);
        gameActivity.game.pickup(x, y);

    }

    void doubleTapConfirmed(MotionEvent e)
    {
        float[] f = gameActivity.displayAdapter.convertScreenToWorld(e.getX(), e.getY());
        gameActivity.game.pickup(f[0], f[1]);
    }

    void doubleTapRelease(float x, float y)
    {
        float[] f = gameActivity.displayAdapter.convertScreenToWorld(x, y);
        gameActivity.game.drop(f[0], f[1]);
    }

    void dragStart(MotionEvent e)
    {

    }

    void drag(float x, float y)
    {
        float[] f = gameActivity.displayAdapter.convertScreenToWorld(x, y);
        gameActivity.game.setHeldPosition(f[0], f[1]);
    }

    void dragEnd(float x, float y)
    {
        float[] f = gameActivity.displayAdapter.convertScreenToWorld(x, y);
        gameActivity.game.drop(f[0], f[1]);
    }

    void scale(Vec2<Float> p1, Vec2<Float> p2, Vec2<Float> n1, Vec2<Float> n2)
    {
        // find the centres of the touch pairs
        Vec2<Float> pmid = new Vec2((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        Vec2<Float> nmid = new Vec2((n1.x + n2.x) / 2, (n1.y + n2.y) / 2);

        // translations
        float xmd = nmid.x - pmid.x;
        float ymd = nmid.y - pmid.y;

        // scales
        float px = p2.x - p1.x;
        float py = p2.y - p1.y;
        float psize = (float) Math.sqrt((px * px) + (py * py));

        float nx = n2.x - n1.x;
        float ny = n2.y - n1.y;
        float nsize = (float) Math.sqrt((nx * nx) + (ny * ny));

        float scale = nsize / psize;

        // apply changes
        gameActivity.displayAdapter.mat.postTranslate(-nmid.x, -nmid.y);
        gameActivity.displayAdapter.mat.postScale(scale, scale);
        gameActivity.displayAdapter.mat.postTranslate(nmid.x, nmid.y);
        gameActivity.displayAdapter.mat.postTranslate(nmid.x - pmid.x, nmid.y - pmid.y);

    }

    void scroll(Vec2<Float> prev, Vec2<Float> next)
    {

        gameActivity.displayAdapter.mat.postTranslate(next.x - prev.x, next.y - prev.y);
    }
}
