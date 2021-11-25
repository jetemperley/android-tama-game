package com.tama.apptest;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;
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
    PetGame game;
    DepthCanvas dc;


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
        dc = new DepthCanvas();
    }

    public void draw() {
        if (view.surface.getSurface().isValid()) {
            canvas = view.surface.lockCanvas();

            if (canvas != null) {
                canvas.setMatrix(mat);
                dc.canvas = canvas;
                // size = canvas.getClipBounds();
                // Log.d("canvas clip ", " "  +size.top + " " + size.bottom + " " + size.left + " " + size.right);
                topOff = d.getHeight() - canvas.getHeight();
                canvas.drawColor(Color.BLACK);
                game.drawEnv(dc);
                dc.drawQ();
                dc.clearQ();

                canvas.setMatrix(idmat);
                game.drawUI(dc);
                // canvas.drawCircle(x , y - (d.getHeight() - canvas.getHeight()), 20, red);
                dc.drawQ();
                dc.clearQ();
                view.surface.unlockCanvasAndPost(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent e){

//        switch(e.getAction()){
//
//            case MotionEvent.ACTION_DOWN:
//                px = e.getX();
//                py = e.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                map.offsetMap((e.getX() - px)/scale, (e.getY() - py)/scale);
//                px = e.getX();
//                py = e.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//                px = -1;
//                py = -1;
//                break;
//        }

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
            game.press(f[0], f[1]);
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
            game.longPress(f[0], f[1]);
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

class Assets{

    static ArrayList<Bitmap> sprites;
    static ArrayList<SpriteSheet> sheets;


    static void init(Resources r){

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        sprites = new ArrayList<Bitmap>();

        sprites.add(BitmapFactory.decodeResource(r, R.drawable.tree));
        Log.d("Assets", "added tree");
        ProcessAndAddSprites(BitmapFactory.decodeResource(r, R.drawable.items, opts), 16);
        Log.d("Assets", "added items");
        ProcessAndAddSprites(BitmapFactory.decodeResource(r, R.drawable.tools, opts), 16);
        Log.d("Assets", "added tools");
        ProcessAndAddSprites(BitmapFactory.decodeResource(r, R.drawable.treegrowth, opts), 16);
        //Log.d("Assets", "added tree growth");
        sprites.add(BitmapFactory.decodeResource(r, R.drawable.seed, opts));
        ProcessAndAddSprites(BitmapFactory.decodeResource(r, R.drawable.terrainsimp, opts), 16);
        sprites.add(BitmapFactory.decodeResource(r, R.drawable.bush1, opts));
        sprites.add(BitmapFactory.decodeResource(r, R.drawable.longgrass, opts));
        Log.d("Assets init", sprites.size() + "");

        // Log.d("Assets", "sprites = " + sprites.size());
        // Log.d("Assets", "added seed");

        sheets = new ArrayList<SpriteSheet>();
        SpriteSheet ss = new SpriteSheet(BitmapFactory.decodeResource(r, R.drawable.watersimp, opts), 8);
        sheets.add(ss);

        ss = new SpriteSheet(BitmapFactory.decodeResource(r, R.drawable.blob, opts), 16);
        ss.addRowsAsAnims();
        sheets.add(ss);

        ss = new SpriteSheet(BitmapFactory.decodeResource(r, R.drawable.egg, opts), 16);
        ss.addRowsAsAnims();
        sheets.add(ss);

        ss = new SpriteSheet(BitmapFactory.decodeResource(r, R.drawable.walker, opts), 16);
        ss.addRowsAsAnims();
        sheets.add(ss);

    }

    static void ProcessAndAddSprites(Bitmap bm, int size){
        Log.d("width", "" + bm.getWidth());
        Log.d("height", "" + bm.getHeight());
        for (int x = 0; x < bm.getWidth(); x+=size){
            for (int y =0; y < bm.getHeight(); y+=size){
                sprites.add(Bitmap.createBitmap(bm, x, y, size, size));
            }
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