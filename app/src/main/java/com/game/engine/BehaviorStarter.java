package com.game.engine;

import java.util.ArrayList;
import java.util.List;

public class BehaviorStarter
{
    private static List<Behaviour> startSubscribers = new ArrayList<>();

    public static void subscribe(Behaviour b)
    {
        startSubscribers.add(b);
    }

    public static void alertStart()
    {
        for (Behaviour b : startSubscribers)
        {
            b.start();
        }
        startSubscribers.clear();
    }
}
