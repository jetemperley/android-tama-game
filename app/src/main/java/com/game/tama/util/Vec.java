package com.game.tama.util;

public class Vec<T>
{
    private T[] vec;

    /**
     * Creates a vector with the number of elements in the arguments.
     *
     * @param args
     */
    public Vec(T... args)
    {
        vec = args;
    }

    /**
     * Sets the first N elements to the arguments specified.
     *
     * @param args
     */
    public void set(T... args)
    {
        if (args.length > vec.length)
        {
            throw new IllegalArgumentException(
                "Too many arguments in the setter.");
        }
        System.arraycopy(args, 0, vec, 0, args.length);
    }

    /**
     * Sets the first N elements after the offset to the arguments specified.
     *
     * @param args
     */
    public void set(int offset, T... args)
    {
        if (args.length + offset > vec.length)
        {
            throw new IllegalArgumentException(
                "Too many arguments in the setter.");
        }
        System.arraycopy(args, 0, vec, offset, args.length);
    }

    /**
     * Sets the possible elements from the argument vec into the first possible
     * elements in this vec
     * @param vec
     */
    public void set(Vec<T> vec)
    {
        int length = Math.min(vec.size(), this.size());
        System.arraycopy(vec.vec, 0, this.vec, 0, length);
    }

    public int size()
    {
        return vec.length;
    }

    public T get(int index)
    {
        return vec[index];
    }

    public T x()
    {
        return vec[0];
    }

    public T y()
    {
        return vec[1];
    }

    public T z()
    {
        return vec[2];
    }

    public T w()
    {
        return vec[3];
    }

    public T r()
    {
        return vec[0];
    }

    public T g()
    {
        return vec[1];
    }

    public T b()
    {
        return vec[2];
    }

    public T a()
    {
        return vec[3];
    }
}
