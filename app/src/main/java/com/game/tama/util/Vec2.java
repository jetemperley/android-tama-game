package com.game.tama.util;

public class Vec2<T extends Number> implements java.io.Serializable
{
    public T x, y;

    public Vec2(T x, T y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec2<T> other)
    {
        this.x = other.x;
        this.y = other.y;
    }

    public Vec2<T> set(T x, T y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec2<T> set(Vec2<T> a)
    {
        x = a.x;
        y = a.y;
        return this;
    }

    public static float distSq(Vec2<Float> a, Vec2<Float> b)
    {
        float x = a.x - b.x;
        float y = a.y - b.y;

        return Math.abs(x * x + y * y);
    }

    @Override
    public String toString()
    {
        return "x=" + x + ", y=" + y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Vec2)
        {
            Vec2<?> other = (Vec2)o;
            return other.x.equals(x) && other.y.equals(y);
        }
        return false;
    }

}
