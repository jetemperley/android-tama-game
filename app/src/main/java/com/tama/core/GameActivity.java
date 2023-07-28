package com.tama.core;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;

public class GameActivity extends Activity
{

    ConstraintLayout lay;
    static Paint red, black, white;
    CustomView view;
    Timer timer;
    Rect screenSize;
    final String CHANNEL_ID = "01", channel_name = "ch1", channel_desc = "test channel";
    Canvas canvas;
    Matrix mat, idmat;
    static int frameTime = 0;

    GameGesture controls;
    Display d;
    AndroidDisplay displayAdapter;
    DepthDisplay depthDisplay;
    PetGame game;
    final static String dataFile = "gameData.ser";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        view = new CustomView(this);
        setContentView(view);

        d = getWindowManager().getDefaultDisplay();
        screenSize = new Rect();
        d.getRectSize(screenSize);

        Log.i("sizes: ",
              screenSize.top + " " + screenSize.bottom + " " + screenSize.left + " " + screenSize.right);

        red = new Paint();
        red.setARGB(255, 255, 0, 0);

        black = new Paint();
        black.setARGB(255, 0, 0, 0);
        black.setDither(false);
        black.setFilterBitmap(false);

        white = new Paint();
        white.setARGB(255, 255, 255, 255);
        white.setDither(false);
        white.setFilterBitmap(false);
        white.setTextSize(30);

        // gesture setup
        controls = new GameGesture();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
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


        Log.d("display height ",
              " " + d.getHeight() + " " + view.getHeight() + " " + view.getTop());
        depthDisplay = new DepthDisplay();
        displayAdapter = new AndroidDisplay(16, d.getWidth(), d.getHeight(), 0);

        mat = new Matrix();
        mat.setScale(3, 3);
        idmat = new Matrix();
        idmat.setScale(5, 5);
        idmat.preTranslate(0, displayAdapter.topIn / 5);

        new Thread(() ->
                   {

                       LocalTime start;
                       LocalTime end = LocalTime.now();

                       while (true)
                       {
                           start = end;
                           this.draw();
                           end = LocalTime.now();
                           frameTime = (int) ChronoUnit.MILLIS.between(start, end);
                           long ytime = PetGame.gameSpeed - frameTime;
                           // Log.d("Time", "" + ytime);
                           try
                           {
                               if (ytime > 0)
                               {
                                   Thread.currentThread().wait(ytime);
                               }
                           } catch (Exception e)
                           {
                           }
                       }
                   }).start();

    }


    public void draw()
    {
        controls.update();
        if (view.surface.getSurface().isValid())
        {
            canvas = view.surface.lockCanvas();

            if (canvas != null)
            {
                canvas.setMatrix(mat);
                depthDisplay.display = displayAdapter;
                displayAdapter.canvas = canvas;
                // size = canvas.getClipBounds();
                // Log.d("canvas clip ", " "  +size.top + " " + size.bottom + " " + size.left + " " + size.right);
                displayAdapter.topIn = d.getHeight() - canvas.getHeight();
                canvas.drawColor(Color.BLACK);
                game.update();
                game.drawEnv(depthDisplay);
                depthDisplay.drawQ();
                depthDisplay.clearQ();
                game.drawUI(displayAdapter);

                view.surface.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Context context = getApplicationContext();


        try
        {
            FileOutputStream fos = context.openFileOutput(dataFile, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.close();
            Log.d("GameActivity", "serialization complete");
        } catch (IOException e)
        {
            Log.d("GameActivity", e.getMessage());
        }

    }

    @Override
    public void onStart()
    {
        super.onStart();
        Context context = getApplicationContext();
        File dir = context.getFilesDir();
        File[] content = dir.listFiles();
        File data = null;
        for (File f : content)
        {
            if (f.getName().equals(dataFile))
            {
                data = f;
                break;
            }
        }
        if (data != null)
        {
            try
            {
                ObjectInputStream in =
                        new ObjectInputStream(
                                new FileInputStream(data));
                game = (PetGame) in.readObject();
                game.reLoadAllAssets();
                in.close();
                Log.d("GameActivity", "deserialization complete");
            } catch (Exception e)
            {
                game = new PetGame();
                Log.d("GameActivity", "deserialization failed");
            }
        }
        else
        {
            game = new PetGame();
        }
    }


    float[] convertScreenToGame(float x, float y)
    {
        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {(x - f2[2]) / 16, (y - f2[5] - displayAdapter.topIn) / 16};

        Matrix inv = new Matrix();
        mat.invert(inv);
        inv.mapVectors(f);
        return f;

    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {

        controls.onTouchEvent(e);
        return true;// super.onTouchEvent(e);

    }


    public class CustomView extends SurfaceView
    {

        SurfaceHolder surface;

        CustomView(Context context)
        {
            super(context);
            surface = getHolder();
        }

    }

    class GameGesture extends Gesture
    {

        void singleTapConfirmed(float x, float y)
        {
            float[] f = convertScreenToGame(x, y);
            game.select(f[0], f[1]);
            game.poke(f[0], f[1]);
        }

        void longPressConfirmed(float x, float y)
        {
            float[] f = convertScreenToGame(x, y);

        }

        void doubleTapConfirmed(MotionEvent e)
        {
            float[] f = convertScreenToGame(e.getX(), e.getY());
            game.pickup(f[0], f[1]);
        }

        void doubleTapRelease(float x, float y)
        {
            float[] f = convertScreenToGame(x, y);
            game.drop(f[0], f[1]);
        }

        void dragStart(MotionEvent e)
        {

        }

        void drag(float x, float y)
        {
            float[] f = convertScreenToGame(x, y);
            game.setHeldPosition(f[0], f[1]);
        }

        void dragEnd(float x, float y)
        {
            float[] f = convertScreenToGame(x, y);
            game.drop(f[0], f[1]);
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
            mat.postTranslate(-nmid.x, -nmid.y);
            mat.postScale(scale, scale);
            mat.postTranslate(nmid.x, nmid.y);
            mat.postTranslate(nmid.x - pmid.x, nmid.y - pmid.y);

        }

        void scroll(Vec2<Float> prev, Vec2<Float> next)
        {

            mat.postTranslate(next.x - prev.x, next.y - prev.y);
        }
    }


}


class Rand
{

    static int RandInt(int min, int max)
    {
        return (int) (Math.random() * (max - min) + min);
    }

}

class Vec2<T> implements java.io.Serializable
{
    T x, y;

    Vec2(T x, T y)
    {
        this.x = x;
        this.y = y;
    }

    void set(T x, T y)
    {
        this.x = x;
        this.y = y;
    }

    void set(Vec2<T> a)
    {
        x = a.x;
        y = a.y;

    }

    static float distSq(Vec2<Float> a, Vec2<Float> b)
    {
        float x = a.x - b.x;
        float y = a.y - b.y;

        return x * x + y * y;
    }
}

class A
{

    static boolean inRange(Object[][] arr, int x, int y)
    {
        return !(x < 0 || y < 0 || x > arr.length - 1 || y > arr[x].length - 1);
    }

    static boolean inRange(Object[] arr, int idx)
    {
        return !(idx < 0 || idx > arr.length - 1);
    }
}