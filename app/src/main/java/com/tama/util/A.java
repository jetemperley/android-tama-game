package com.tama.util;

public class A
{

    public static boolean inRange(Object[][] arr, int x, int y)
    {
        return !(x < 0 || y < 0 || x > arr.length - 1 || y > arr[x].length - 1);
    }

    static boolean inRange(Object[] arr, int idx)
    {
        return !(idx < 0 || idx > arr.length - 1);
    }
}
