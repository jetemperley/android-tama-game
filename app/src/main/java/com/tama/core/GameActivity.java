package com.tama.core;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    final String CHANNEL_ID = "01", channel_name = "ch1", channel_desc =
            "test channel";
    Canvas canvas;

    /**
     * The time (ms) which the last frame took
     */
    static int frameTime = 0;

    GameGesture controls;
    Display display;
    AndroidDisplay displayAdapter;
    DepthDisplay depthDisplay;
    PetGame game;
    final static String dataFile = "gameData.ser";

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        view = new CustomView(this);
        setContentView(view);

        display = getWindowManager().getDefaultDisplay();
        screenSize = new Rect();

        display.getRectSize(screenSize);

        Log.i("sizes: ",
              screenSize.top + " " + screenSize.bottom + " " + screenSize.left +
                      " " + screenSize.right);

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
        controls = new GameGesture(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = channel_name;
            String description = channel_desc;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this,
                                               CHANNEL_ID).setContentTitle(
                        "My notification").setContentText("Hello World!").setPriority(
                        NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);
        int ID = 0;
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        // notificationManager.notify(ID, builder.build());

        Log.d("Setup", "staring setup");
        Assets.init(getResources());
        Log.d("Setup", "finished loading resources");

        Log.d("display height ",
              " " + display.getHeight() + " " + view.getHeight() + " " +
                      view.getTop());
        depthDisplay = new DepthDisplay();

        Rect out = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(out);
        Log.d("Game Activity decorview", out.top + " " + out.bottom + " ");

        display.getRectSize(out);
        Log.d("Game Activity display", out.top + " " + out.bottom + " ");

        displayAdapter = new AndroidDisplay(16);

        new Thread(() ->
                   {

                       LocalTime start;
                       LocalTime end = LocalTime.now();

                       while (true)
                       {
                           start = end;
                           this.draw();
                           end = LocalTime.now();
                           frameTime =
                                   (int) ChronoUnit.MILLIS.between(start, end);
                           long ytime = PetGame.gameSpeed - frameTime;

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
        // update controls
        controls.update();

        // update the view bounds
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(
                displayAdapter.view);

        if (view.surface.getSurface().isValid())
        {
            canvas = view.surface.lockCanvas();

            if (canvas != null)
            {
                canvas.setMatrix(displayAdapter.mat);
                depthDisplay.display = displayAdapter;
                displayAdapter.canvas = canvas;
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

    @Override public void onStop()
    {
        super.onStop();
        Context context = getApplicationContext();

        try
        {
            FileOutputStream fos =
                    context.openFileOutput(dataFile, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.close();
            Log.d("GameActivity", "serialization complete");
        } catch (IOException e)
        {
            Log.d("GameActivity", e.getMessage());
        }
    }

    @Override public void onStart()
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
                        new ObjectInputStream(new FileInputStream(data));
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

    @Override public boolean onTouchEvent(MotionEvent e)
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
}


