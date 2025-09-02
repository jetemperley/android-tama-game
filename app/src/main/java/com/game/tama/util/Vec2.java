package com.game.tama.util;

public class Vec2<T extends Number> implements java.io.Serializable
{
    public T x, y;

    public Vec2(final T x, final T y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2(final Vec2<T> other)
    {
        this.x = other.x;
        this.y = other.y;
    }

    public Vec2<T> set(final T x, final T y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec2<T> set(final Vec2<T> a)
    {
        x = a.x;
        y = a.y;
        return this;
    }

    public static float distSq(final Vec2<Float> a, final Vec2<Float> b)
    {
        final float x = a.x - b.x;
        final float y = a.y - b.y;
        return x * x + y * y;
    }

    public static float dist(final Vec2<Float> a, final Vec2<Float> b)
    {
        return (float) Math.sqrt(distSq(a, b));
    }

    @Override
    public String toString()
    {
        return String.format("{%s, %s}", x, y);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof Vec2)
        {
            final Vec2<?> other = (Vec2) o;
            return other.x.equals(x) && other.y.equals(y);
        }
        return false;
    }

}
