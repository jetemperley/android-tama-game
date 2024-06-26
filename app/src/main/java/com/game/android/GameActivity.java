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
import com.game.tama.core.GameLoop;
import com.game.tama.behaviour.GameManager;
import com.game.tama.core.World;
import com.game.tama.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameActivity extends Activity
{
    final static String DATA_FILE_NAME = "gameData.ser";
    private static float TOP_OFFSET = 0;

    ConstraintLayout lay;
    static Paint red, black, white;
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

    Node root;
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

        root = new Node();
        gesture = new Gesture();
        GestureEventAdaptor gestureEventAdaptor = new GestureEventAdaptor();
        gesture.gestureTarget = gestureEventAdaptor;
        gameManager = new GameManager(root, gestureEventAdaptor);
        // TODO load the game somewhere
        gameManager.play();

        gameLoop = new GameLoop(this);
    }

    public void updateAndDraw()
    {
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(
                displayAdapter.view);
        TOP_OFFSET = displayAdapter.view.top;
        gesture.topOffset = TOP_OFFSET;
        gesture.update();
        // update the view bounds

        if (view.surface.getSurface().isValid())
        {
            canvas = view.surface.lockCanvas();

            if (canvas != null)
            {
                Log.log(this, "UpdateAndDraw");
                displayAdapter.canvas = canvas;
                canvas.drawColor(Color.BLACK);
                root.engine_update();
                root.engine_draw(displayAdapter);
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
            // TODO: this should go somewhere: oos.writeObject(gameManager.game);
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

    private World loadGame()
    {
        World game;
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
                game = (World) in.readObject();
                game.reLoadAllAssets();
                in.close();
                Log.log(this, "deserialization complete");
            }
            catch (Exception e)
            {
                //game = new PetGame();
                Log.log(this, "deserialization failed, " + e.getMessage());
            }
        }
        else
        {
            Log.log(this, "data file did not exist");
//            game = new PetGame();
        }
        return null;
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


