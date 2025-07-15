package com.game.tama.ui;

import com.game.engine.DisplayAdapter;
import com.game.tama.core.Asset;
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
    public SquareCellButtonLeaf(final float xPos,
                                final float yPos,
                                final int width,
                                final int height,
                                final Sprite sprite)
    {
        this(xPos, yPos, width, height, sprite, () -> {});
    }

    /**
     * @param xPos pixel x position of the button
     * @param yPos pixel y position of the button
     */
    public SquareCellButtonLeaf(final float xPos,
                                final float yPos,
                                final Sprite sprite,
                                final Runnable activate)
    {
        this(xPos, yPos, 1, 1, sprite, activate);
    }

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     * @param width  size (in 16 bit cells) of button
     * @param height size (in 16 bit cells) of button
     */
    public SquareCellButtonLeaf(final float xPos,
                                final float yPos,
                                final float width,
                                final float height,
                                final Sprite sprite,
                                final Runnable activate)
    {
        super(xPos, yPos, width, height, sprite, activate);
        sizeInCells = new Vec2<>(width, height);
        init();
    }

    private void init()
    {
        final SpriteSheet sheet =
            Asset.sheets.get(AssetName.sheet_16_rect);
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
    public void draw(final DisplayAdapter display)
    {
        //        display.clearRect(pos.x, pos.y, 1, 1);
        if (sizeInCells.x == 1 && sizeInCells.y == 1)
        {
            display.drawUi(SQUARE, pos.x, pos.y);
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
        display.drawUi(sprite, pos.x, pos.y);
    }

    private void drawCol(final DisplayAdapter display)
    {
        display.drawUi(TOP_CAP, pos.x, pos.y);

        float y = pos.y + 1;
        for (int i = 1; i < sizeInCells.y - 1; i++)
        {
            display.drawUi(VERT_PIPE, pos.x, y);
            y++;
        }
        display.drawUi(BOT_CAP, pos.x, y);
    }

    private void drawRow(final DisplayAdapter display)
    {
        display.drawUi(LEFT_CAP, pos.x, pos.y);

        float x = pos.x + 1;
        for (int i = 1; i < sizeInCells.x - 1; i++)
        {
            display.drawUi(HORZ_PIPE, x, pos.y);
            x += 1;
        }
        display.drawUi(RIGHT_CAP, x, pos.y);
    }

    private void drawRect(final DisplayAdapter display)
    {
        display.drawUi(TOP_LEFT, pos.x, pos.y);
        display.drawUi(TOP_RIGHT, pos.x + (sizeInCells.x - 1), pos.y);
        display.drawUi(BOT_LEFT, pos.x, pos.y + (sizeInCells.y - 1));
        display.drawUi(
            BOT_RIGHT,
            pos.x + (sizeInCells.x - 1),
            pos.y + (sizeInCells.y - 1));

        float x = pos.x + 1;
        for (int i = 1; i < sizeInCells.x - 1; i++)
        {
            display.drawUi(TOP, x, pos.y);
            display.drawUi(BOT, x, pos.y + (sizeInCells.y - 1));
            x += 1;
        }

        float y = pos.y + 1;
        for (int i = 1; i < sizeInCells.y - 1; i++)
        {
            display.drawUi(LEFT, pos.x, y);
            display.drawUi(RIGHT, pos.x + (sizeInCells.x - 1), y);
            y += 1;
        }
    }
}
