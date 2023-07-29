package com.tama.util;

public class Rand
{

    public static int RandInt(int min, int max)
    {
        return (int) (Math.random() * (max - min) + min);
    }

}
