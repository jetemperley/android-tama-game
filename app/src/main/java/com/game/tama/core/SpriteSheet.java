package com.game.tama.core;

import android.graphics.Bitmap;

public class SpriteSheet
{

    // sheet is in [Y][X] coordinates
    StaticSprite[][] sheet;

    public SpriteSheet(StaticSprite[][] arr)
    {
        sheet = arr;

    }

    public Bitmap get(int row, int col)
    {

        return sheet[row][col].getSprite();
    }

    public StaticSprite getSprite(int row, int col)
    {

        return sheet[row][col];
    }

    public int rowLength(int row)
    {
        return sheet[row].length;
    }

    public int numRows()
    {
        return sheet.length;
    }

    /**
     * Counts the total number of sprites in this spriteSheet
     * @return
     */
    public int totalSprites()
    {
        return sheet.length * sheet[0].length;
    }

    /**
     * Gets the i-th sprite, counting left to right, row by row.
     * @param i
     * @return
     */
    public StaticSprite getSprite(int i)
    {
        int row = i/sheet[0].length;
        int col = i%sheet[0].length;
        return getSprite(row, col);
    }
}


