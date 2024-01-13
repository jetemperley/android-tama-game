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
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tama.gesture.GestureTargetPipe;
import com.tama.util.Log;

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
    final static String DATA_FILE_NAME = "gameDatapu.ser";
    public static float TOP_OFFSET = 0;

    ConstraintLayout lay;
    static Paint red, black, white;
    CustomView view;
    Timer timer;
    static Rect screenSize;
    final String
        CHANNEL_ID = "01",
        channel_name = "ch1",
        channel_desc = "test channel";
    Canvas canvas;
    GameLoop gameLoop;

    Display display;
    AndroidDisplay displayAdapter;

    GameManager gameManager;
    GestureTargetPipe gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        view = new CustomView(this);
        setContentView(view);
        TOP_OFFSET = view.getTop();

        display = getWindowManager().getDefaultDisplay();
        screenSize = new Rect();

        display.getRectSize(screenSize);

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
                new NotificationCompat.Builder(
                        this,
                        CHANNEL_ID).setContentTitle(
                        "My notification").setContentText("Hello World!").setPriority(
                        NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);
        int ID = 0;
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        // notificationManager.notify(ID, builder.build());

        Log.log(this, "staring setup");
        Assets.init(getResources());
        Log.log(this, "finished loading resources");
        Rect out = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(out);

        display.getRectSize(out);

        displayAdapter = new AndroidDisplay(16);

        gameManager = new GameManager();
        gesture = new GestureTargetPipe(gameManager);
        gameManager.game = loadGame();
        gameManager.play();

        gameLoop = new GameLoop(this);
    }

    public void updateAndDraw()
    {
        gesture.update();
        // update the view bounds
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(
                displayAdapter.view);
        TOP_OFFSET = displayAdapter.view.top;

        if (view.surface.getSurface().isValid())
        {
            canvas = view.surface.lockCanvas();

            if (canvas != null)
            {
                canvas.setMatrix(displayAdapter.worldMat);
                displayAdapter.canvas = canvas;
                canvas.drawColor(Color.BLACK);
                gameManager.updateAndDraw(displayAdapter);
                if (view.surface.getSurface().isValid())
                {
                    view.surface.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    @Override
    public void onStop()
    {
        gameLoop.play = false;
        try
        {
            gameLoop.join();
        }
        catch (InterruptedException e)
        {
            Log.log(this, "could not join gameloop");
        }
        super.onStop();
        Context context = getApplicationContext();

        try
        {
            FileOutputStream fos =
                    context.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameManager.game);
            oos.close();
            Log.log(this, "serialization complete");
        }
        catch (IOException e)
        {
            Log.log(this, "serialization failed" + e.getMessage());
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        gameLoop.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        gesture.onTouchEvent(e);
        return true;
    }

    private PetGame loadGame()
    {
        PetGame game;
        Context context = getApplicationContext();
        File dir = context.getFilesDir();
        File[] content = dir.listFiles();
        File data = new File(dir.getPath() + "/" + DATA_FILE_NAME);
        Log.log(this, "data path is " + data.getAbsolutePath());
        if (data.exists())
        {
            try
            {
                ObjectInputStream in =
                        new ObjectInputStream(new FileInputStream(data));
                game = (PetGame) in.readObject();
                game.reLoadAllAssets();
                in.close();
                Log.log(this, "deserialization complete");
            }
            catch (Exception e)
            {
                game = new PetGame();
                Log.log(this, "deserialization failed, " + e.getMessage());
            }
        }
        else
        {
            Log.log(this, "data file did not exist");
            game = new PetGame();
        }
        return game;
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


