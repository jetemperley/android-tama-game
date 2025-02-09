package com.game.tama.ui;

import com.game.android.DisplayAdapter;
import com.game.android.Assets;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.SpriteSheet;
import com.game.tama.util.Vec2;

public class SquareCellButtonLeaf extends SimpleButtonLeaf
{
    public Vec2<Float> sizeInCells;

    private static Sprite TOP_LEFT;
    private static Sprite TOP_RIGHT;
    private static Sprite BOT_LEFT;
    private static Sprite BOT_RIGHT;
    private static Sprite HORZ_PIPE;
    private static Sprite VERT_PIPE;
    private static Sprite LEFT_CAP;
    private static Sprite RIGHT_CAP;
    private static Sprite TOP_CAP;
    private static Sprite BOT_CAP;
    private static Sprite TOP;
    private static Sprite LEFT;
    private static Sprite BOT;
    private static Sprite RIGHT;
    private static Sprite SQUARE;

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     * @param width  size (in 16 bit cells) of button
     * @param height size (in 16 bit cells) of button
     */
    public SquareCellButtonLeaf(float xPos, float yPos, int width, int height, Sprite sprite)
    {
        this(xPos, yPos, width, height, sprite, ()->{});
    }

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     */
    public SquareCellButtonLeaf(float xPos, float yPos, Sprite sprite, Runnable activate)
    {
        this(xPos, yPos, 1, 1, sprite, activate);
    }

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     * @param width  size (in 16 bit cells) of button
     * @param height size (in 16 bit cells) of button
     */
    public SquareCellButtonLeaf(float xPos,
                                float yPos,
                                float width,
                                float height,
                                Sprite sprite,
                                Runnable activate)
    {
        super(xPos, yPos, width*16, height*16, sprite, activate);
        sizeInCells = new Vec2<>(width, height);
        init();
    }

    private void init()
    {
        SpriteSheet sheet =
            Assets.getSpriteSheet(AssetName.sheet_16_button);
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
        display.drawRect(pos.x, pos.y, sizeInCells.x * 16, sizeInCells.y * 16);
        if (sizeInCells.x == 1 && sizeInCells.y == 1)
        {
            display.drawSprite(SQUARE, pos.x, pos.y);
        }
        else if (sizeInCells.x == 1)
        {
            drawCol(display);
        }
        else if (sizeInCells.y == 1)
        {
            drawRow(display);
        }
        else
        {
            drawRect(display);
        }
        display.drawSprite(sprite, pos.x, pos.y);
    }

    private void drawCol(DisplayAdapter display)
    {
        display.drawSprite(TOP_CAP, pos.x, pos.y);

        float y = pos.y + 16;
        for (int i = 1; i < sizeInCells.y - 1; i++)
        {
            display.drawSprite(VERT_PIPE, pos.x, y);
            y += 16;
        }
        display.drawSprite(BOT_CAP, pos.x, y);
    }

    private void drawRow(DisplayAdapter display)
    {
        display.drawSprite(LEFT_CAP, pos.x, pos.y);

        float x = pos.x + 16;
        for (int i = 1; i < sizeInCells.x - 1; i++)
        {
            display.drawSprite(HORZ_PIPE, x, pos.y);
            x += 16;
        }
        display.drawSprite(RIGHT_CAP, x, pos.y);
    }

    private void drawRect(DisplayAdapter display)
    {
        display.drawSprite(TOP_LEFT, pos.x, pos.y);
        display.drawSprite(TOP_RIGHT, pos.x + (sizeInCells.x - 1) * 16, pos.y);
        display.drawSprite(BOT_LEFT, pos.x, pos.y + (sizeInCells.y - 1) * 16);
        display.drawSprite(
            BOT_RIGHT,
            pos.x + (sizeInCells.x - 1) * 16,
            pos.y + (sizeInCells.y - 1) * 16);

        float x = pos.x + 16;
        for (int i = 1; i < sizeInCells.x - 1; i++)
        {
            display.drawSprite(TOP, x, pos.y);
            display.drawSprite(BOT, x, pos.y + (sizeInCells.y - 1) * 16);
            x += 16;
        }

        float y = pos.y + 16;
        for (int i = 1; i < sizeInCells.y - 1; i++)
        {
            display.drawSprite(LEFT, pos.x, y);
            display.drawSprite(RIGHT, pos.x + (sizeInCells.x - 1) * 16, y);
            y += 16;
        }
    }
}
