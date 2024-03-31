package com.game.tama.ui;

import android.graphics.Matrix;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.Down;
import com.game.android.gesture.GestureEvent;
import com.game.tama.core.Assets;
import com.game.tama.core.Drawable;
import com.game.tama.core.Loadable;
import com.game.tama.core.Sprite;
import com.game.tama.core.SpriteSheet;
import com.game.tama.core.Updateable;
import com.game.tama.util.MatrixUtil;
import com.game.tama.util.Vec2;
import com.game.android.gesture.GestureEventHandler;

public class Button implements Loadable, GestureEventHandler, Drawable, Updateable
{
    protected String asset;
    protected Vec2<Float> pos;
    protected Vec2<Integer> size;
    private Runnable activateMethod;
    private Runnable updateMethod;
    private transient Sprite sprite;

    private static transient Sprite TOP_LEFT;
    private static transient Sprite TOP_RIGHT;
    private static transient Sprite BOT_LEFT;
    private static transient Sprite BOT_RIGHT;
    private static transient Sprite HORZ_PIPE;
    private static transient Sprite VERT_PIPE;
    private static transient Sprite LEFT_CAP;
    private static transient Sprite RIGHT_CAP;
    private static transient Sprite TOP_CAP;
    private static transient Sprite BOT_CAP;
    private static transient Sprite TOP;
    private static transient Sprite LEFT;
    private static transient Sprite BOT;
    private static transient Sprite RIGHT;
    private static transient Sprite SQUARE;

    /**
     * @param xPos pixel x position of the button
     * @param yPos pixel y position of the button
     */
    public Button(float xPos, float yPos, String asset)
    {
        this(xPos, yPos, 1, 1, asset);
    }

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     * @param width  size (in 16 bit cells) of button
     * @param height size (in 16 bit cells) of button
     */
    public Button(float xPos, float yPos, int width, int height, String asset)
    {
        this(xPos, yPos, width, height, asset, ()->{}, ()->{});
    }
    public Button(float xPos, float yPos, String asset, Runnable activate)
    {
        this(xPos, yPos, 1, 1, asset, activate, ()->{});
    }

    public Button(float xPos,
                  float yPos,
                  int width,
                  int height,
                  String asset,
                  Runnable activate,
                  Runnable update)
    {
        this.pos = new Vec2<Float>(xPos, yPos);
        this.size = new Vec2<Integer>(width, height);
        this.asset = asset;
        this.updateMethod = update;
        this.activateMethod = activate;
        load();
    }

    private void init()
    {
        SpriteSheet sheet =
            Assets.getSheet(Assets.Names.sheet_16_button.name());
        TOP_LEFT = sheet.getSprite(0, 0);
        TOP_RIGHT = sheet.getSprite(0, 2);
        BOT_LEFT = sheet.getSprite(2, 0);
        BOT_RIGHT = sheet.getSprite(2, 2);

        HORZ_PIPE = sheet.getSprite(1, 4);
        VERT_PIPE = sheet.getSprite(1, 3);

        TOP_CAP = sheet.getSprite(0, 3);
        BOT_CAP = sheet.getSprite(2, 3);
        LEFT_CAP = sheet.getSprite(0, 4);
        RIGHT_CAP = sheet.getSprite(2, 4);

        TOP = sheet.getSprite(0, 1);
        LEFT = sheet.getSprite(1, 0);
        BOT = sheet.getSprite(2, 1);
        RIGHT = sheet.getSprite(1, 2);

        SQUARE = sheet.getSprite(1, 1);
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        display.drawRect(pos.x, pos.y, size.x * 16, size.y * 16);
        if (size.x == 1 && size.y == 1)
        {
            display.displayAt(SQUARE, pos.x, pos.y);
        }
        else if (size.x == 1)
        {
            drawCol(display);
        }
        else if (size.y == 1)
        {
            drawRow(display);
        }
        else
        {
            drawRect(display);
        }
        display.displayAt(sprite, pos.x, pos.y);
    }

    @Override
    public void update()
    {
        updateMethod.run();
    }

    @Override
    public void load()
    {
        sprite = Assets.getSprite(asset);
        // if top is null, they all are
        if (TOP == null)
        {
            init();
        }
    }

    public void activate()
    {
        activateMethod.run();
    }

    public boolean isInside(float x, float y, Matrix matrix)
    {
        float[] loc = MatrixUtil.convertScreenToMatrix(matrix, x, y);
        return (
            loc[0] > pos.x &&
                loc[1] > pos.y &&
                loc[0] < pos.x + 16 * size.x &&
                loc[1] < pos.y + 16 * size.y);
    }

    private void drawCol(DisplayAdapter display)
    {
        display.displayAt(TOP_CAP, pos.x, pos.y);

        float y = pos.y + 16;
        for (int i = 1; i < size.y - 1; i++)
        {
            display.displayAt(VERT_PIPE, pos.x, y);
            y += 16;
        }
        display.displayAt(BOT_CAP, pos.x, y);
    }

    private void drawRow(DisplayAdapter display)
    {
        display.displayAt(LEFT_CAP, pos.x, pos.y);

        float x = pos.x + 16;
        for (int i = 1; i < size.x - 1; i++)
        {
            display.displayAt(VERT_PIPE, x, pos.y);
            x += 16;
        }
        display.displayAt(BOT_CAP, x, pos.y);
    }

    private void drawRect(DisplayAdapter display)
    {
        display.displayAt(TOP_LEFT, pos.x, pos.y);
        display.displayAt(TOP_RIGHT, pos.x + (size.x - 1) * 16, pos.y);
        display.displayAt(BOT_LEFT, pos.x, pos.y + (size.y - 1) * 16);
        display.displayAt(
            BOT_RIGHT,
            pos.x + (size.x - 1) * 16,
            pos.y + (size.y - 1) * 16);

        float x = pos.x + 16;
        for (int i = 1; i < size.x - 1; i++)
        {
            display.displayAt(TOP, x, pos.y);
            display.displayAt(BOT, x, pos.y + (size.y - 1) * 16);
            x += 16;
        }

        float y = pos.y + 16;
        for (int i = 1; i < size.y - 1; i++)
        {
            display.displayAt(LEFT, pos.x, y);
            display.displayAt(RIGHT, pos.x + (size.x - 1) * 16, y);
            y += 16;
        }
    }

    @Override
    public boolean handleEvent(GestureEvent e)
    {
        if (e instanceof Down)
        {
            activate();
            return true;
        }
        return false;
    }
}
