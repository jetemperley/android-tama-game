package com.game.android;

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

import com.game.android.gesture.Gesture;
import com.game.android.gesture.GestureEventAdaptor;
import com.game.engine.Node;
import com.game.tama.core.Assets;
import com.game.engine.GameLoop;
import com.game.tama.engine.behaviour.GameManager;
import com.game.tama.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

public class GameActivity extends Activity
{
    final static String DATA_FILE_NAME = "gameData.ser";
    /**
     * The screen bounds for this particular game application taking into
     * account the navbar and such.
     */
    private static Rect SCREEN_RECT = new Rect();

    ConstraintLayout lay;
    static Paint red, black, white, highlight;
    CustomView view;
    public static Rect screenSize;
    final String
        CHANNEL_ID = "01",
        channel_name = "ch1",
        channel_desc = "test channel";
    Canvas canvas;
    GameLoop gameLoop;

    Display display;
    AndroidDisplay displayAdapter;

    Node rootNode;
    GameManager gameManager;
    Gesture gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        view = new CustomView(this);
        setContentView(view);
        // TOP_OFFSET = view.getTop();

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

        highlight = new Paint();
        highlight.setARGB(100, 255, 255, 255);
        highlight.setDither(false);
        highlight.setFilterBitmap(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = channel_name;
            String description = channel_desc;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =
                new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the
            // importance
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

        rootNode = new Node();
        gesture = new Gesture();
        GestureEventAdaptor gestureEventAdaptor = new GestureEventAdaptor();
        gesture.gestureTarget = gestureEventAdaptor;
        gameManager = new GameManager(rootNode, gestureEventAdaptor);

        gameLoop = new GameLoop(this);
    }

    public void updateAndDraw()
    {

        // update the view bounds, incase the screen changed
        this.getWindow()
            .getDecorView()
            .getWindowVisibleDisplayFrame(SCREEN_RECT);
        // rootNode.localTransform.setTranslate(0, SCREEN_RECT.top);
        displayAdapter.view = SCREEN_RECT;
        gesture.topOffset = SCREEN_RECT.top;
        gesture.update();

        if (view.surface.getSurface().isValid())
        {
            canvas = view.surface.lockCanvas();

            if (canvas != null)
            {
                displayAdapter.canvas = canvas;
                canvas.drawColor(Color.BLACK);
                rootNode.engine_update();
                rootNode.engine_draw(displayAdapter);
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
        } catch (InterruptedException e)
        {
            Log.log(this, "could not join gameloop");
        }
        super.onStop();
        saveGame();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        loadGame();
        gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        gesture.onTouchEvent(e);
        return true;
    }

    private void saveGame()
    {
        Context context = getApplicationContext();
        try
        {
            FileOutputStream fos =
                context.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            gameManager.save(oos);
            oos.close();
            Log.log(this, "Serialization complete");
        } catch (IOException e)
        {
            Log.error(this, "Serialization failed.", e);
        }
    }

    private void loadGame()
    {
        Context context = getApplicationContext();
        File dir = context.getFilesDir();
        File data = new File(dir.getPath() + "/" + DATA_FILE_NAME);
        Log.log(this, "data path is " + data.getAbsolutePath());
        if (data.exists())
        {
            try
            {
                ObjectInputStream in =
                    new ObjectInputStream(Files.newInputStream(data.toPath()));
                gameManager.load(in);
                in.close();
                Log.log(this, "deserialization complete");
            } catch (Exception e)
            {
                gameManager.newGame();
                Log.error(this, "deserialization failed", e);
            }
        }
        else
        {
            gameManager.newGame();
            Log.log(this, "data file did not exist");
        }
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


