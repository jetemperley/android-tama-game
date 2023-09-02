package com.tama.core;

import android.graphics.Bitmap;

public class SpriteSheet
{

    // sheet is in [Y][X] coordinates
    StaticSprite[][] sheet;

    SpriteSheet(StaticSprite[][] arr)
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

    int len(int row)
    {
        return sheet[row].length;
    }

}


