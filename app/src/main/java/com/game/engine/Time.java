package com.game.engine;

public class Time
{
    /** the time in seconds the last frame took */
    private static float deltaTimeS = 0;
    /** the time in ms the last frame took */
    private static long deltaTimeMs = 0;
    /** The amount of ms this game has been running for. */
    private static long time = 0;

    /** Update all the time metrics */
    static void updateTime(final long msToAdd)
    {
        Time.deltaTimeS = msToAdd / 1000f;
        Time.deltaTimeMs = msToAdd;
        Time.time += Time.deltaTimeMs;
    }

    /** Time in ms */
    public static long deltaTimeMs()
    {
        return deltaTimeMs;
    }

    /** Time in seconds */
    public static float deltaTime()
    {
        return deltaTimeS;
    }

    /** Total running time in ms */
    public static long time()
    {
        return time;
    }
}
