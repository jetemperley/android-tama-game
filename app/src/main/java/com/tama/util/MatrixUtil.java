package com.tama.util;

import android.graphics.Matrix;

import com.tama.core.GameActivity;

public class MatrixUtil
{
    static MatrixUtil util = new MatrixUtil();

    public static float[] convertScreenToMatrix(Matrix mat, float x, float y)
    {
        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {
            (x - f2[2]),
            (y - f2[5] - GameActivity.TOP_OFFSET)};

        Matrix inv = new Matrix();
        mat.invert(inv);
        inv.mapVectors(f);
        return f;
    }

    private static Matrix temp = new Matrix();
    public static float[] convertScreenToWorldArray(Matrix mat, float x, float y)
    {
        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {
            (x - f2[2]) / 16,
            (y - f2[5] - GameActivity.TOP_OFFSET) / 16};

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
            (y - f2[5] - GameActivity.TOP_OFFSET)};

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
            Log.log(util, f[0+i] + " " + f[3+i] + " " + f[6+i]);
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
        //f[8] = scale;
        mat.setValues(f);
    }

}
