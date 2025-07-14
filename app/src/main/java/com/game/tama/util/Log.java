package com.game.tama.util;

public class Log
{
    public static void log(final Object obj, final String msg)
    {
        log(obj.getClass(), msg);
    }

    public static void log(final Class obj, final String msg)
    {
        android.util.Log.d(obj.getSimpleName(), msg);
    }

    public static void error(final Class obj, final String msg, final Throwable throwable)
    {
        android.util.Log.e(obj.getSimpleName(), msg, throwable);
    }

    public static void error(final Class obj, final String msg)
    {
        android.util.Log.e(obj.getSimpleName(), msg);
    }

    public static void error(final Object obj, final String msg)
    {
        error(obj.getClass(), msg);
    }

    public static void error(final Object obj, final String msg, final Throwable throwable)
    {
        error(obj.getClass(), msg, throwable);
    }
}
