package com.game.android;

import android.opengl.Matrix;

import com.game.engine.Transform;

import java.util.Arrays;

public class Matrix4 implements Transform<Matrix4>
{
    private float[] altValues = new float[16];
    private float[] values = new float[16];

    public Matrix4()
    {
        Matrix.setIdentityM(values, 0);
    }

    /**
     * this = args * this
     * @param args
     */
    public void postMult(Matrix4 ... args)
    {
        for (int i = args.length - 1; i >= 0; i--)
        {
            Matrix4 mat = args[i];
            Matrix.multiplyMM(
                altValues, 0,
                mat.getValues(), 0,
                values, 0);
            swapValues();
        }
    }

    /**
     * this = this * args
     * @param args
     */
    public void preMult(Matrix4 ... args)
    {
        for (Matrix4 mat : args)
        {
            Matrix.multiplyMM(
                altValues, 0,
                values, 0,
                mat.getValues(), 0);
            swapValues();
        }
    }

    private void swapValues()
    {
        float[] temp = values;
        values = altValues;
        altValues = temp;
    }

    public float[] getValues()
    {
        return values;
    }

    /**
     * Sets this matrix's values to the same as the passed matrix
     * @param from
     */
    public void setValues(Matrix4 from)
    {
        copy(from.getValues(), values);
    }

    private void copy(float[] source, float[] dest)
    {
        for (int i = 0; i < values.length; i++)
        {
            dest[i] = source[i];
        }
    }

    /**
     * this = this * T(x, y, z)
     * @param x
     * @param y
     * @param z
     */
    public void preTranslate(float x, float y, float z)
    {
        Matrix.translateM(values, 0, x, y, z);
    }
    /**
     * this = T(x, y, z) * this
     * @param x
     * @param y
     * @param z
     */
    public void postTranslate(float x, float y, float z)
    {
        float[] translate = new float[16];
        Matrix.setIdentityM(translate, 0);
        Matrix.translateM(translate, 0, x, y, z);
        Matrix.multiplyMM(altValues, 0, translate, 0, values, 0);
        swapValues();
    }

    /**
     * this = this * S(x, y, z)
     * @param x
     * @param y
     * @param z
     */
    public void preScale(float x, float y, float z)
    {
        Matrix.scaleM(values, 0, x, y, z);
    }

    /**
     * this = S(x, y, z) * this
     * @param x
     * @param y
     * @param z
     */
    public void postScale(float x, float y, float z)
    {
        float[] scale = new float[16];
        Matrix.setIdentityM(scale, 0);
        Matrix.scaleM(scale, 0, x, y, z);
        Matrix.multiplyMM(altValues, 0, scale, 0, values, 0);
        swapValues();
    }
}