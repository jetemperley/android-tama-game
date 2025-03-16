package com.game.engine;

import com.game.tama.util.Vec2;

public interface Transform
{
    /**
     * this = this * args
     * @param args
     * @return itself
     */
    public Transform preMult(Transform... args);

    /**
     * this = args * this
     * @param args
     * @return itself
     */
    public Transform postMult(Transform... args);

    /**
     * This = this * T(x, y, z) . I.e. translate in local coords
     *
     * @param x
     * @param y
     * @param z
     * @return itself
     */
    public Transform preTranslate(float x, float y, float z);

    /**
     * This = T(x, y, z) * this. I.e. translate in world coords
     * @param x
     * @param y
     * @param z
     * @return itself
     */
    public Transform postTranslate(float x, float y, float z);

    /**
     * this = this * S(x, y, z)
     * @param x
     * @param y
     * @param z
     * @return itself
     */
    public Transform preScale(float x, float y, float z);

    /**
     * this = S(x, y, z) * this
     * @param x
     * @param y
     * @param z
     * @return itself
     */
    public Transform postScale(float x, float y, float z);

    /**
     * Sets this matrix's values to the same as the passed matrix
     * @param from
     * @return itself
     */
    public Transform setValues(Transform from);

    /**
     * Sets this matrix's values to the same as the passed values
     * @param from
     * @return itself
     */
    public Transform setValues(float[] from);

    /**
     * Get a deep copy of the underlying values
     * @return
     */
    public float[] getValues();

    /**
     * Get a copy of the values into the destination
     * @param dest
     */
    public void getValues(float[] dest);

    /**
     * Set this transform to the identity transform
     * @return itself
     */
    public Transform reset();

    /**
     * Invert this transform and store the reslut in dest. Does not mutate
     * this transform.
     * @param dest
     */
    public void invert(Transform dest);

    /**
     * Invert this transform in place and returns this.
     */
    public Transform invert();

    /**
     * Multiply this transform (out = T * P) with the point P = x, y, x, 0
     * @param x
     * @param y
     * @param z
     * @return
     */
    public float[] mapPoint(float x, float y, float z);

    /**
     * Multiply this transform (out = T * V) with the vector V = x, y, x, 1
     * @param x
     * @param y
     * @param z
     * @return
     */
    public float[] mapVector(float x, float y, float z);

    public Vec2<Float> getScale();

    public Vec2<Float> getTranslate();

    public Transform copy();

    /**
     * Sets the scale of this transform, leave other details unchanged.
     * @return
     */
    public Transform setScale(float x, float y);

    /**
     * Sets the translate of this transform, leave other details unchanged.
     * @return
     */
    public Transform setTranslate(float x, float y);
}
