package com.game.tama.util;

import android.graphics.Matrix;

import com.game.android.GameActivity;

public class MatrixUtil
{
    private static MatrixUtil util = new MatrixUtil();

    public static void convertScreenToWorldArr(Matrix mat, float[] points)
    {
        float[] f2 = new float[9];
        mat.getValues(f2);

        points[0] = (f[0] - f2[2]) / 16;
        points[1] = (f[1] - f2[5]) / 16;

        temp.reset();
        mat.invert(temp);
        temp.mapVectors(f);
    }

    public static float[] convertInvScreenToMatrix(Matrix mat, float x, float y)
    {
        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {
            (x - f2[2]),
            (y - f2[5])};

        Matrix inv = new Matrix();
        mat.invert(inv);
        inv.mapVectors(f);
        return f;
    }

    private static Matrix temp = new Matrix();

    public static float[] convertScreenToWorldArray(Matrix mat,
                                                    float x,
                                                    float y)
    {
        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {
            (x - f2[2]) / 16,
            (y - f2[5]) / 16};

        temp.reset();
        mat.invert(temp);
        temp.mapVectors(f);
        return f;
    }

    public static float[] convertScreenToWorldBits(Matrix mat, float x, float y)
    {
        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {
            (x - f2[2]),
            (y - f2[5])};

        temp.reset();
        mat.invert(temp);
        temp.mapVectors(f);

        return f;
    }

    static float[] f = new float[9];

    public static float dx(Matrix mat)
    {
        mat.getValues(f);
        return f[2];
    }

    public static float dy(Matrix mat)
    {
        mat.getValues(f);
        return f[5];
    }

    public static void printMat(Matrix mat)
    {
        float[] f = new float[9];
        mat.getValues(f);
        for (int i = 0; i < 3; i++)
        {
            Log.log(util, f[0 + i] + " " + f[3 + i] + " " + f[6 + i]);
        }
    }

    public static void clearTranslate(Matrix mat)
    {
        float[] f = new float[9];
        mat.getValues(f);
        f[2] = 0;
        f[5] = 0;
        mat.setValues(f);
    }

    public static float getScale(Matrix mat)
    {
        float[] f = new float[9];
        mat.getValues(f);
        return f[0];
    }

    public static void setScale(Matrix mat, float scale)
    {
        float[] f = new float[9];
        mat.getValues(f);
        f[0] = scale;
        f[4] = scale;
        mat.setValues(f);
    }

    public static float relativeScreenWidth(Matrix mat)
    {
        float scale = getScale(mat);
        return GameActivity.screenSize.width() / scale;
    }

    public static float relativeScreenHeight(Matrix mat)
    {
        float scale = getScale(mat);
        return GameActivity.screenSize.height() / scale;
    }

    public static float getScaleToFitWidth(int pixels)
    {
        return GameActivity.screenSize.width()/(float)pixels;
    }

//    public static Matrix identity()
//    {
//
//    }
}
