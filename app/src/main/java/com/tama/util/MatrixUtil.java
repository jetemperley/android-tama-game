package com.tama.util;

import android.graphics.Matrix;

import com.tama.core.GameActivity;

public class MatrixUtil
{
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

    public static float[] convertScreenToWorldArray(Matrix mat, float x, float y)
    {
        float[] f2 = new float[9];
        mat.getValues(f2);
        float[] f = {
            (x - f2[2]) / 16,
            (y - f2[5] - GameActivity.TOP_OFFSET) / 16};

        Matrix inv = new Matrix();
        mat.invert(inv);
        inv.mapVectors(f);
        return f;
    }
}
