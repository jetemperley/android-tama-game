package com.game.tama.util;

public class Log
{
    public static void log(Object obj, String msg)
    {
        log(obj.getClass(), msg);
    }
    public static void log(Class obj, String msg)
    {
        android.util.Log.d(obj.getCanonicalName(), msg);
    }

    public static void error(Class obj, String msg, Throwable throwable)
    {
        android.util.Log.e(obj.getCanonicalName(), msg, throwable);
    }

    public static void error(Class obj, String msg)
    {
        android.util.Log.e(obj.getCanonicalName(), msg);
    }

    public static void error(Object obj, String msg, Throwable throwable)
    {
        error(obj.getClass(), msg, throwable);
    }
}
