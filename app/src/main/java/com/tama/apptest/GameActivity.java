package com.tama.apptest;

import android.app.ActionBar;
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
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
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

    Display d;
    AndroidDisplay displayAdapter;
    DepthDisplay depthDisplay;
    PetGame game;
    static int period = 25;
    final static String dataFile = "gameData.ser";

    Controls controls;


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
                //Log.d("Time", "" + ytime);
                try {
                    if (ytime > 0)
                        Thread.currentThread().wait(ytime);
                } catch (Exception e) {
                }
            }
        }).start();

        setupGame();
        // gesture setup
        controls = new Controls(this, game);

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



    }

    void setupGame(){
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


    Matrix inv = new Matrix();
    float[] convertScreenToGame(float x, float y, float[] out){
        float[] f2 = new float[9];
        mat.getValues(f2);
        out[0] = (x - f2[2])/16;
        out[1] = (y - f2[5]- displayAdapter.topIn)/16;

        inv.reset();
        mat.invert(inv);
        inv.mapPoints(out);
        return out;

    }

    @Override
    public boolean onTouchEvent(MotionEvent e){

        controls.onTouchEvent(e);
        return super.onTouchEvent(e);
    }



    public class CustomView extends SurfaceView {

        SurfaceHolder surface;

        CustomView(Context context) {
            super(context);
            surface = getHolder();
        }

    }

    class Controls {
        PetGame game;

        // time to check if a 2nd pointer is down
        int waitTime = 100;
        long touchTime = -1;
        float[] touchPos = new float[] {-1, -1};
        int id0 = -1, id1 = -1;
        Vec2<Float> prev0 = new Vec2(-1f, -1f);
        Vec2<Float> prev1 = new Vec2(-1f, -1f);


        Runnable r = () -> {
            try {
                Thread.sleep(waitTime);
            } catch (Exception e){
                // Log.d("controls exception",  "cancelled" +  e.getMessage());
                resetTouch();
                return;
            }
            // Log.d("controls ", "holding");
            if (!game.setHeld(touchPos[0], touchPos[1])){
                resetTouch();
            }

        };
        Thread t;

        Controls(Activity a, PetGame p){
            game = p;
            t = new Thread(r);
        }

        boolean onTouchEvent(MotionEvent e){
//            Log.d("control pointers ", e.getPointerCount()+"");
//             for (int i = 0; i < e.getPointerCount(); i++) {
//
//                 int id = e.getPointerId(i);
//
//                 Log.d("controls ", "index " + i + " id " + id + " event code "
//                         + MotionEvent.actionToString(e.getAction())
//                         + " " + e.getAction());
//             }

                switch (e.getAction()) {

                    // placed the first finger down
                    case MotionEvent.ACTION_DOWN: {
                        // now in milliseconds
                        touchTime = Instant.now().toEpochMilli();
                        touchPos = convertScreenToGame(e.getX(), e.getY(), touchPos);
                        Log.d("controls ", touchPos[0] + " " + touchPos[1]);
                        if (t.getState() == Thread.State.WAITING)
                            t.interrupt();
                        t = new Thread(r);
                        t.start();
                        prev0.set(e.getX(), e.getY());
                        id0 = e.getPointerId(0);


                    }
                    break;

                    // brought the final finger up finger up
                    // swap fingers
                    case MotionEvent.ACTION_POINTER_UP:{

                        prev0.x = prev1.x;
                        prev0.y = prev1.y;

                    }
                    break;

                    case MotionEvent.ACTION_POINTER_DOWN:{
                        prev1.set(prev0);
                        prev0.set(e.getX(), e.getY());
                    }
                    break;

                    // all fingers are up
                    case MotionEvent.ACTION_UP: {
                        resetTouch();
                        release(e);
                    }
                    break;

                    // placed the second finger down
                    case MotionEvent.ACTION_POINTER_2_DOWN:{
                        if (Instant.now().toEpochMilli() - touchTime < waitTime) {
                            t.interrupt();
                        }
                        prev1.set(e.getX(1), e.getY(1));
                        id1 = e.getPointerId(0);

                    }
                    break;

                    // 2nd finger up
                    case MotionEvent.ACTION_POINTER_2_UP:{


                    }
                    break;

                    // fingers were moved
                    case MotionEvent.ACTION_MOVE:
                    {

                        if (e.getPointerCount() > 1){

                            twoFingerDrag(e);
                            if (touchPos[0] != -1)
                                itemDrag(e);

                            prev0.set(e.getX(0), e.getY(0));
                            prev1.set(e.getX(1), e.getY(1));

                        } else {

                            if (touchPos[1] != -1)
                                itemDrag(e);
                            else
                                emptyDrag(e);

                            prev0.set(e.getX(0), e.getY(0));
                        }
                    }
                    break;

                }


            return true;
        }

        float[] temp = new float[2];
        void itemDrag(MotionEvent e){
            // Log.d("controls ", "item drag");
            temp[0] = e.getX();
            temp[1] = e.getY();
            // mat.mapVectors(temp);
            temp = convertScreenToGame(e.getX(), e.getY(), temp);
            game.setHeldPosition(temp[0], temp[1]);

        }

        void emptyDrag(MotionEvent e){
            // Log.d("controls ", "empty drag ");
            // + (prev0.x -e.getX()) + " " + (prev1.y -e.getY()));
            mat.postTranslate(e.getX() - prev0.x, e.getY()- prev0.y);
        }

        Vec2<Float> t0 = new Vec2(0f,0f), t1 = new Vec2(0f, 0f);
        void twoFingerDrag(MotionEvent e){
            // Log.d("controls ", "two drag");

            t0.set(e.getX(0), e.getY(0));
            t1.set(e.getX(1), e.getY(1));

            float tfx = (t0.x + t1.x)/2f;
            float tfy = (t0.y + t1.y)/2f;
            mat.postTranslate(-tfx, -tfy);

            // calculate the length of each set of scale gestures
            float td = (t0.x - t1.x)*(t0.x - t1.x) + (t0.y - t1.y)*(t0.y - t1.y);
            td = (float)Math.sqrt(td);
            float pd = (prev0.x - prev1.x)*(prev0.x - prev1.x) + (prev0.y - prev1.y)*(prev0.y - prev1.y);
            pd = (float)Math.sqrt(pd);
            mat.postScale(td/pd, td/pd);
            // Log.d("scale ", td/pd +"");
            mat.postTranslate(tfx, tfy);

            float pfx = (prev0.x+prev1.x)/2f;
            float pfy = (prev0.y+prev1.y)/2f;

            mat.postTranslate(tfx - pfx, tfy - pfy);

//            Log.d("scale points ", t0.x + " " t1.);

        }

        void release(MotionEvent e){
            temp[0] = e.getX();
            temp[1] = e.getY();
            mat.mapVectors(temp);
            game.dropHeld(temp[0], temp[1]);
        }

        private synchronized void resetTouch(){
            touchTime = -1;
            touchPos[0] = -1f;
            touchPos[1] = -1f;
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
    void set(Vec2<T> other){
        x = other.x;
        y = other.y;
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