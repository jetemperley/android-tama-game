package com.tama.core;

import android.graphics.Matrix;

import com.tama.gesture.Down;
import com.tama.gesture.GestureEvent;
import com.tama.gesture.GestureEventHandler;
import com.tama.util.MatrixUtil;
import com.tama.util.Vec2;

public abstract class Button implements Loadable, GestureEventHandler
{
    private transient Displayable sprite;
    protected String asset;
    protected Vec2<Float> pos;
    protected Vec2<Integer> size;

    private static transient Displayable TOP_LEFT;
    private static transient Displayable TOP_RIGHT;
    private static transient Displayable BOT_LEFT;
    private static transient Displayable BOT_RIGHT;
    private static transient Displayable HORZ_PIPE;
    private static transient Displayable VERT_PIPE;
    private static transient Displayable LEFT_CAP;
    private static transient Displayable RIGHT_CAP;
    private static transient Displayable TOP_CAP;
    private static transient Displayable BOT_CAP;
    private static transient Displayable TOP;
    private static transient Displayable LEFT;
    private static transient Displayable BOT;
    private static transient Displayable RIGHT;
    private static transient Displayable SQUARE;

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
        this.pos = new Vec2<Float>(xPos, yPos);
        this.size = new Vec2<Integer>(width, height);
        this.asset = asset;
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

    void draw(DisplayAdapter display)
    {
        display.drawRect(pos.x, pos.y, size.x * 16, size.y * 16);
        if (size.x == 1 && size.y == 1)
        {
            display.displayAbsolute(SQUARE, pos.x, pos.y);
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
        display.displayAbsolute(sprite, pos.x, pos.y);
    }

    void update() {}

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

    abstract void activate();

    public boolean isInside(float x, float y, Matrix matrix)
    {
        float[] loc = MatrixUtil.convertScreenToMatrix(matrix, x, y);
        return (
            loc[0] > pos.x &&
                loc[1] > pos.y &&
                loc[0] < pos.x + 16*size.x &&
                loc[1] < pos.y + 16*size.y);
    }

    private void drawCol(DisplayAdapter display)
    {
        display.displayAbsolute(TOP_CAP, pos.x, pos.y);

        float y = pos.y + 16;
        for (int i = 1; i < size.y - 1; i++)
        {
            display.displayAbsolute(VERT_PIPE, pos.x, y);
            y += 16;
        }
        display.displayAbsolute(BOT_CAP, pos.x, y);
    }

    private void drawRow(DisplayAdapter display)
    {
        display.displayAbsolute(LEFT_CAP, pos.x, pos.y);

        float x = pos.x + 16;
        for (int i = 1; i < size.x - 1; i++)
        {
            display.displayAbsolute(VERT_PIPE, x, pos.y);
            x += 16;
        }
        display.displayAbsolute(BOT_CAP, x, pos.y);
    }

    private void drawRect(DisplayAdapter display)
    {
        display.displayAbsolute(TOP_LEFT, pos.x, pos.y);
        display.displayAbsolute(TOP_RIGHT, pos.x + (size.x - 1) * 16, pos.y);
        display.displayAbsolute(BOT_LEFT, pos.x, pos.y + (size.y - 1) * 16);
        display.displayAbsolute(
            BOT_RIGHT,
            pos.x + (size.x - 1) * 16,
            pos.y + (size.y - 1) * 16);

        float x = pos.x + 16;
        for (int i = 1; i < size.x - 1; i++)
        {
            display.displayAbsolute(TOP, x, pos.y);
            display.displayAbsolute(BOT, x, pos.y + (size.y - 1) * 16);
            x += 16;
        }

        float y = pos.y + 16;
        for (int i = 1; i < size.y - 1; i++)
        {
            display.displayAbsolute(LEFT, pos.x, y);
            display.displayAbsolute(RIGHT, pos.x + (size.x - 1) * 16, y);
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
