package com.game.tama.util;

import android.support.annotation.NonNull;

public class Vec4<T>
{
    public T x = null;
    public T y = null;
    public T z = null;
    public T w = null;

    public Vec4(final T x, final T y, final T z, final T w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

    }

    /**
     * Set the elements of the vector to the values in the array, with the 4th element optional.
     */
    public Vec4(final T[] arr, final int offset)
    {
        this.x = arr[offset];
        this.y = arr[offset + 1];
        this.z = arr[offset + 2];
        if (arr.length > 4)
        {
            this.w = arr[offset + 3];
        }
    }

    /**
     * Set the elements of the vector to the values in the array, with the 4th element optional.
     */
    public Vec4(final T[] arr)
    {
        this(arr, 0);
    }

    public Vec4(final Vec4<T> vec)
    {
        set(vec);
    }

    public void set(final T x, final T y, final T z, final T w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Set this vector to be the same as the provided vec.
     */
    public void set(final Vec4<T> vec)
    {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        w = vec.w;
    }

    @NonNull
    @Override
    public String toString()
    {
        return String.format("{%s, %s, %s, %s", x, y, z, w);
    }
}
