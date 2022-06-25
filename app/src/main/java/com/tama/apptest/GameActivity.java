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
import android.support.annotation.RequiresApi;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class GameActivity extends Activity{

    ConstraintLayout lay;
    static Paint red, black;
    CustomView view;
    Timer timer;
    Rect screenSize;
    final String CHANNEL_ID = "01", channel_name = "ch1", channel_desc = "test channel";
    Canvas canvas;
    Matrix mat, idmat;
    GestureDetectorCompat gdc;
    ScaleGestureDetector sgd;
    GameControls controls;
    Display d;
    AndroidDisplay displayAdapter;
    DepthDisplay depthDisplay;
    PetGame game;
    static int period = 25;
    final static String dataFile = "gameData.ser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new CustomView(this);
        setContentView(view);

        d = getWindowManager().getDefaultDisplay();
        screenSize = new Rect();
        d.getRectSize(screenSize);

        Log.i("sizes: " , screenSize.top + " " + screenSize.bottom + " " + screenSize.left + " " + screenSize.right);

        red = new Paint();
        red.setARGB(255, 255, 0, 0);

        black = new Paint();
        black.setARGB(255, 0, 0, 0);
        black.setDither(false);
        black.setFilterBitmap(false);

        // gesture setup
        gdc = new GestureDetectorCompat(this, new Gestures());
        sgd = new ScaleGestureDetector(this, new ScaleGesture());
        controls = new GameControls();


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
        Log.d("Setup", "finished loading resources");


        Log.d("display height ", " " + d.getHeight() +" " + view.getHeight() + " " + view.getTop());
        depthDisplay = new DepthDisplay();
        displayAdapter = new AndroidDisplay(16, d.getWidth(), d.getHeight(), 0);

        mat = new Matrix();
        mat.setScale(3, 3);
        idmat = new Matrix();
        idmat.setScale(5, 5);
        idmat.preTranslate(0, displayAdapter.topIn/5);

        new Thread(() -> {

            LocalTime start;
            LocalTime end = LocalTime.now();
            long frameTime;

            while(true) {
                start = end;
                this.draw();
                end = LocalTime.now();
                frameTime = ChronoUnit.MILLIS.between(start, end);
                long ytime = period - frameTime;
                Log.d("Time", "" + ytime);
                try {
                    if (ytime > 0)
                        Thread.currentThread().wait(ytime);
                } catch (Exception e) {
                }
            }
        }).start();

    }



    public void draw() {
        if (view.surface.getSurface().isValid()) {
            canvas = view.surface.lockCanvas();

            if (canvas != null) {
                canvas.setMatrix(mat);
                depthDisplay.display = displayAdapter;
                displayAdapter.canvas = canvas;
                // size = canvas.getClipBounds();
                // Log.d("canvas clip ", " "  +size.top + " " + size.bottom + " " + size.left + " " + size.right);
                displayAdapter.topIn = d.getHeight() - canvas.getHeight();
                canvas.drawColor(Color.BLACK);
                game.drawEnv(depthDisplay);
                depthDisplay.drawQ();
                depthDisplay.clearQ();

                // canvas.setMatrix(idmat);
                game.drawUI(displayAdapter);

                view.surface.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        Context context = getApplicationContext();


        try {
            FileOutputStream fos = context.openFileOutput(dataFile, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.close();
            Log.d("GameActivity", "serialization complete");
        } catch (IOException e){
            Log.d("GameActivity", e.getMessage());
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        Context context = getApplicationContext();
        File dir = context.getFilesDir();
        File[] content = dir.listFiles();
        File data = null;
        for (File f : content){
            if (f.getName().equals(dataFile)) {
                data = f;
                break;
            }
        }
        if (data != null){
            try{
                ObjectInputStream in =
                        new ObjectInputStream(
                        new FileInputStream(data));
                game = (PetGame)in.readObject();
                game.reLoadAllAssets();
                in.close();
                Log.d("GameActivity", "deserialization complete");
            } catch (Exception e){
                game = new PetGame();
                Log.d("GameActivity", "deserialization failed");
            }
        } else {
            game = new PetGame();
        }
    }



    float[] convertScreenToGame(float x, float y){
        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {(x - f2[2])/16, (y - f2[5]- displayAdapter.topIn)/16};

        Matrix inv = new Matrix();
        mat.invert(inv);
        inv.mapVectors(f);
        return f;

    }

    @Override
    public boolean onTouchEvent(MotionEvent e){

        controls.onTouchEvent(e);
        return true;// super.onTouchEvent(e);

    }



    public class CustomView extends SurfaceView {

        SurfaceHolder surface;

        CustomView(Context context) {
            super(context);
            surface = getHolder();
        }

    }

    class GameControls {
        boolean down = false;
        LocalTime downtime;

        public boolean onTouchEvent(MotionEvent e){

            float[] f = convertScreenToGame(e.getX(), e.getY());

            if (e.getAction() == MotionEvent.ACTION_DOWN){
                game.setSelected((int)f[0], (int)f[1]);
                down = true;
                downtime = LocalTime.now();
                Log.d("Game Controld", "down");

            } else if (e.getAction() == MotionEvent.ACTION_MOVE){
                if (down){
                    // the press was dragged for the first time
                    game.setSelectedAsHeld();
                    game.setHeldPosition(f[0]*16, f[1]*16);

                    if (false){
                        // if the move is near the edge, move the world
                    }
                    down = false;
                }
                // Log.d("Game Controld", "move");
                game.setHeldPosition(f[0]*16, f[1]*16);

            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                if (down){
                    // there was a regular press
                    game.singlePress((int)f[0], (int)f[1]);
                }
                game.releaseHeld((int)f[0], (int)f[1]);
                down = false;
                Log.d("Game Controls", "up");
            }
            return true;

        }

        void singleTapConfirmed(){

        }

        void doubleTapConfirmed(){

        }

        void drag(){

        }

        void scale(){

        }
    }

    class Gestures extends GestureDetector.SimpleOnGestureListener{

        boolean down = false;

        @Override
        public boolean onDown(MotionEvent e){
            Log.d("Gesture", "down");
            down = true;
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e){
            Log.d("Gesture", "single tap");
            float[] f = convertScreenToGame(e.getX(), e.getY());
            game.singlePress((int)f[0], (int)f[1]);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float d1, float d2){
            Log.d("Gesture", "scroll");
            float[] f = convertScreenToGame(e2.getX(), e2.getY());
            game.setHeldPosition(f[0]*16, f[1]*16);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e){
            Log.d("Gesture", "double tap");
            float[] f = convertScreenToGame(e.getX(), e.getY());
            game.doublePress((int)f[0], (int)f[1]);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e){
            Log.d("Gesture", "long press");
            float[] f = convertScreenToGame(e.getX(), e.getY());
            game.longPress((int)f[0], (int)f[1]);
        }

    }


    private class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector d){
            Log.d("ScaleGesture", "scale");
            mat.preScale(d.getScaleFactor(), d.getScaleFactor());
            return true;
        }
    }
}



class Rand{

    static int RandInt(int min, int max){
        return (int)(Math.random()*(max-min) + min);
    }

}

class Vec2<T> implements java.io.Serializable{
    T x, y;
    Vec2(T x, T y){
        this.x = x;
        this.y = y;
    }

    void set(T x, T y){
        this.x = x;
        this.y = y;
    }
}

class A{

    static boolean inRange(Object[][] arr, int x, int y) {
        return !(x < 0 || y < 0 || x > arr.length -1 || y > arr[x].length -1);
    }

    static boolean inRange(Object[] arr, int idx){
        return !(idx < 0 || idx > arr.length-1);
    }
}