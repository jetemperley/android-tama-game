package com.tama.apptest;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;

public class GameActivity extends Activity{

    ConstraintLayout lay;
    static Paint red, black;
    CustomView view;
    View window;
    Timer timer;
    Rect screenSize;
    int statusBarHeight;
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
        Log.d("sizes: " , screenSize.top + " " + screenSize.bottom + " " + screenSize.left + " " + screenSize.right);

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


    Rect rectangle = new Rect();
    public void draw() {

        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        statusBarHeight = rectangle.top;
        // Log.d("top", view.getHeight() + " " + statusBarHeight);

        if (view.surface.getSurface().isValid()) {
            canvas = view.surface.lockCanvas();

            if (canvas != null) {
                canvas.setMatrix(mat);
                depthDisplay.display = displayAdapter;
                displayAdapter.canvas = canvas;
                //Rect size = canvas.getClipBounds();
                //Log.d("canvas clip ", " "  +size.top + " " + size.bottom + " " + size.left + " " + size.right);
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
    int[] loc = new int[] {-1, -1};
    float[] convertScreenToGame(float[] point){

        point[1] -= statusBarHeight;

        inv.reset();
        mat.invert(inv);

        inv.mapPoints(point);
        point[0] /= 16;
        point[1] /= 16;


        return point;

    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        super.onTouchEvent(e);
        controls.onTouchEvent(e);
        return true;
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

        Thread t;
        Runnable r = () -> {
            try {
                Thread.sleep(waitTime);
            } catch (Exception e){
                // Log.d("controls exception",  "cancelled" +  e.getMessage());
                resetTouch();
                return;
            }

            press();
        };

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
                        touchPos[0] = e.getX();
                        touchPos[1] = e.getY();
                        touchPos = convertScreenToGame(touchPos);
                        Log.d("control down loc ", touchPos[0] + " " + touchPos[1]);
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
                        if (Instant.now().toEpochMilli() - touchTime < waitTime)
                            tap(e);
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

        // a single finger presses the screen and is not released
        void press(){
            if (!game.pickup(touchPos[0], touchPos[1])){
                resetTouch();
            }
        }

        // an item was dragged with a single finger
        float[] temp = new float[2];
        void itemDrag(MotionEvent e){
            // Log.d("controls ", "item drag");

            temp[0] = e.getX();
            temp[1] = e.getY();
            temp = convertScreenToGame(temp);
            game.dragHeld(temp[0], temp[1]);

        }

        // a drag occured but did not press an item
        void emptyDrag(MotionEvent e){
            // Log.d("controls ", "empty drag ");
            // + (prev0.x -e.getX()) + " " + (prev1.y -e.getY()));
            mat.postTranslate(e.getX() - prev0.x, e.getY()- prev0.y);
        }

        // two fingers were dragged
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


        }

        // the primary finger was removed
        void release(MotionEvent e){
            temp[0] = e.getX();
            temp[1] = e.getY();
            convertScreenToGame(temp);
            game.release(temp[0], temp[1]);
        }

        // a single finger was confirmed to tap
        void tap(MotionEvent e){
            Log.d("controls", "tap");
            temp[0] = e.getX();
            temp[1] = e.getY();
            convertScreenToGame(temp);
            game.poke(temp[0], temp[1]);
        }

        private synchronized void resetTouch(){
            touchTime = -1;
            touchPos[0] = -1f;
            touchPos[1] = -1f;
        }

    }


}

