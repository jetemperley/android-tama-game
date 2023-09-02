package com.tama.util;

public class Log
{
    public static void log(Object obj, String msg)
    {
        android.util.Log.d(obj.getClass().getCanonicalName(), msg);
    }
}
