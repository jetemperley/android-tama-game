package com.game.android;

import android.opengl.Matrix;

import com.game.engine.Transform;
import com.game.tama.util.Vec2;

public class Matrix4 implements Transform
{
    private float[] tempValues = new float[16];
    private float[] values = new float[16];

    public Matrix4()
    {
        Matrix.setIdentityM(values, 0);
    }

    /**
     * this = args * this
     *
     * @param args
     */
    public Transform postMult(Transform... args)
    {
        for (int i = args.length - 1; i >= 0; i--)
        {
            Transform mat = args[i];
            Matrix.multiplyMM(
                tempValues, 0,
                mat.getValues(), 0,
                values, 0);
            swapTempValues();
        }
        return this;
    }

    /**
     * this = this * args
     *
     * @param args
     */
    public Transform preMult(Transform... args)
    {
        for (Transform mat : args)
        {
            Matrix.multiplyMM(
                tempValues, 0,
                values, 0,
                mat.getValues(), 0);
            swapTempValues();
        }
        return this;
    }

    private void swapTempValues()
    {
        float[] temp = values;
        values = tempValues;
        tempValues = temp;
    }

    @Override
    public float[] getValues()
    {
        return values;
    }

    @Override
    public void getValues(float[] dest)
    {
        copy(values, dest);
    }

    @Override
    public Transform reset()
    {
        Matrix.setIdentityM(values, 0);
        return this;
    }

    @Override
    public void invert(Transform dest)
    {
        Matrix.invertM(dest.getValues(), 0, values, 0);
    }

    /**
     * Sets this matrix's values to the same as the passed matrix
     *
     * @param from
     */
    @Override
    public Transform setValues(float[] from)
    {
        copy(from, values);
        return this;
    }

    /**
     * Sets this matrix's values to the same as the passed matrix
     *
     * @param from
     */
    @Override
    public Transform setValues(Transform from)
    {
        copy(from.getValues(), values);
        return this;
    }

    private void copy(float[] source, float[] dest)
    {
        System.arraycopy(source, 0, dest, 0, values.length);
    }

    /**
     * this = this * T(x, y, z)
     *
     * @param x
     * @param y
     * @param z
     */
    public Transform preTranslate(float x, float y, float z)
    {
        Matrix.translateM(values, 0, x, y, z);
        return this;
    }

    /**
     * this = T(x, y, z) * this
     *
     * @param x
     * @param y
     * @param z
     */
    public Transform postTranslate(float x, float y, float z)
    {
        float[] translate = new float[16];
        Matrix.setIdentityM(translate, 0);
        Matrix.translateM(translate, 0, x, y, z);
        Matrix.multiplyMM(tempValues, 0, translate, 0, values, 0);
        swapTempValues();
        return this;
    }

    /**
     * this = this * S(x, y, z)
     *
     * @param x
     * @param y
     * @param z
     */
    public Transform preScale(float x, float y, float z)
    {
        Matrix.scaleM(values, 0, x, y, z);
        return this;
    }

    /**
     * this = S(x, y, z) * this
     *
     * @param x
     * @param y
     * @param z
     */
    public Transform postScale(float x, float y, float z)
    {
        float[] scale = new float[16];
        Matrix.setIdentityM(scale, 0);
        Matrix.scaleM(scale, 0, x, y, z);
        Matrix.multiplyMM(tempValues, 0, scale, 0, values, 0);
        swapTempValues();
        return this;
    }

    @Override
    public float[] mapPoint(float x, float y, float z)
    {
        float[] result = new float[4];
        Matrix.multiplyMV(
            result,
            0,
            this.values,
            0,
            new float[]{
                x,
                y,
                z,
                0},
            0);
        return result;
    }

    @Override
    public float[] mapVector(float x, float y, float z)
    {
        float[] result = new float[4];
        Matrix.multiplyMV(
            result,
            0,
            this.values,
            0,
            new float[]{
                x,
                y,
                z,
                1},
            0);
        return result;
    }

    @Override
    public Transform invert()
    {
        Matrix.invertM(tempValues, 0, values, 0);
        swapTempValues();
        return this;
    }

    @Override
    public Vec2<Float> getScale()
    {
        return new Vec2<>(values[0], values[5]);
    }

    @Override
    public Vec2<Float> getTranslate()
    {
        return new Vec2<>(values[3], values[7]);
    }

    @Override
    public Transform copy()
    {
        return new Matrix4().setValues(this);
    }

    @Override
    public Transform setScale(float x, float y)
    {
        values[0] = x;
        values[5] = y;
        return this;
    }

    @Override
    public Transform setTranslate(float x, float y)
    {
        values[3] = x;
        values[7] = y;
        return this;
    }
}