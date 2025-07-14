package com.game.tama.core;

import android.graphics.Bitmap;

public class SpriteSheet
{
    // sheet is in [y][x] coordinates
    StaticSprite[][] sheet;

    public SpriteSheet(final StaticSprite[][] arr)
    {
        sheet = arr;
    }

    public Bitmap get(final int row, final int col)
    {
        return sheet[row][col].getSprite();
    }

    public StaticSprite getSprite(final int row, final int col)
    {
        return sheet[row][col];
    }

    public int rowLength(final int row)
    {
        return sheet[row].length;
    }

    public int numRows()
    {
        return sheet.length;
    }

    /**
     * Counts the total number of sprites in this spriteSheet
     *
     * @return
     */
    public int totalSprites()
    {
        return sheet.length * sheet[0].length;
    }

    /**
     * Gets the i-th sprite, counting left to right, row by row.
     *
     * @param i
     * @return
     */
    public StaticSprite getSprite(final int i)
    {
        final int row = i / sheet[0].length;
        final int col = i % sheet[0].length;
        return getSprite(row, col);
    }
}


