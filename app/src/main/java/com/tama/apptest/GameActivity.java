package com.tama.apptest;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Timer;

public class GameActivity extends Activity{

    ConstraintLayout lay;
    static Paint red, black;
    float x = -1, y = -1, px = -1, py = -1, scale = 1;
    static float topOff = 0;
    CustomView view;
    Timer timer;
    Rect size;
    Bitmap bm;
    final String CHANNEL_ID = "01", channel_name = "ch1", channel_desc = "test channel";
    Canvas canvas;
    Matrix mat, idmat;
    Gestures g;
    GestureDetectorCompat gdc;
    ScaleGestureDetector sgd;
    Display d;
    AndroidDisplay displayAdapter;
    PetGame game;
    DepthDisplay dc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new CustomView(this);
        setContentView(view);

        d = getWindowManager().getDefaultDisplay();
        size = new Rect();
        d.getRectSize(size);

        Log.i("sizes: " ,size.top + " " + size.bottom + " " + size.left + " " + size.right);

        red = new Paint();
        red.setARGB(255, 255, 0, 0);

        black = new Paint();
        black.setARGB(255, 0, 0, 0);
        black.setDither(false);
        black.setFilterBitmap(false);

        // gesture setup
        g = new Gestures();
        gdc = new GestureDetectorCompat(this, g);
        sgd = new ScaleGestureDetector(this, new ScaleGesture());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_name;
            String description = channel_desc;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        int ID = 0;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationManager.notify(ID, builder.build());

        Log.d("Setup", "staring setup");
        Assets.init(getResources());
        Log.d("Setup", "loaded resources");

        timer = new Timer();
        timer.schedule(new GameLoop(this), 0, GameLoop.period);
        mat = new Matrix();
        mat.setScale(3, 3);
        idmat = new Matrix();
        idmat.setScale(5, 5);
        idmat.preTranslate(0, topOff/5);

        game = new PetGame();
        Log.d("display height ", " " + d.getHeight() +" " + view.getHeight() + " " + view.getTop());
        dc = new DepthDisplay();
        displayAdapter = new AndroidDisplay(16);
    }

    public void draw() {
        if (view.surface.getSurface().isValid()) {
            canvas = view.surface.lockCanvas();

            if (canvas != null) {
                canvas.setMatrix(mat);
                dc.canvas = canvas;
                displayAdapter.canvas = canvas;
                // size = canvas.getClipBounds();
                // Log.d("canvas clip ", " "  +size.top + " " + size.bottom + " " + size.left + " " + size.right);
                topOff = d.getHeight() - canvas.getHeight();
                canvas.drawColor(Color.BLACK);
                game.drawEnv(displayAdapter);
                // dc.drawQ();
                // dc.clearQ();

                canvas.setMatrix(idmat);
                game.drawUI(displayAdapter);
                // canvas.drawCircle(x , y - (d.getHeight() - canvas.getHeight()), 20, red);
                // dc.drawQ();
                // dc.clearQ();
                view.surface.unlockCanvasAndPost(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent e){

        this.gdc.onTouchEvent(e);
        this.sgd.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    float[] convertScreenToGame(float x, float y){
        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {(x - f2[2])/16, (y - f2[5]- topOff)/16};

        Matrix inv = new Matrix();
        mat.invert(inv);
        inv.mapVectors(f);
        return f;

    }

    public class CustomView extends SurfaceView {

        SurfaceHolder surface;

        CustomView(Context context) {
            super(context);
            surface = getHolder();
        }

    }

    private  class Gestures extends GestureDetector.SimpleOnGestureListener{


        @Override
        public boolean onDown(MotionEvent e){

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e){
            Log.d("Gesture", "tap confirmed");
            float[] f = convertScreenToGame(e.getX(), e.getY());
            game.press((int)f[0], (int)f[1]);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float d1, float d2){
            mat.postTranslate(-d1, -d2);
//            map.offsetMap(-d1/scale, -d2/scale);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e){

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e){
            float[] f = convertScreenToGame(e.getX(), e.getY());
            game.longPress((int)f[0], (int)f[1]);
        }

    }

    private class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector d){
            Log.d("ScaleGesture", "onScale called");
//            float xs = canvas.getWidth()/2*scale;
//            float ys = canvas.getHeight()/2*scale;
//            mat.postTranslate(-xs, -ys);
//            scale *= d.getScaleFactor();
            mat.preScale(d.getScaleFactor(), d.getScaleFactor());
//            xs = canvas.getWidth()/2*scale;
//            ys = canvas.getHeight()/2*scale;
//            mat.postTranslate(xs, ys);
            return true;
        }
    }
}



class Rand{

    static int RandInt(int min, int max){
        return (int)(Math.random()*(max-min) + min);
    }

}

class Vec2{
    int x, y;
    Vec2(int x, int y){
        this.x = x;
        this.y = y;
    }
    Vec2(){
        this(0, 0);
    }
}

class A{

    static boolean inRange(Object[][] arr, int x, int y) {
        if (x < 0 || y < 0 || x > arr.length -1 || y > arr[x].length -1)
            return false;
        return true;
    }
}