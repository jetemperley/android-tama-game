package com.game.android.gesture;

import com.game.tama.core.Updateable;

import java.util.PriorityQueue;

public class EventPrioritySubscriber implements GestureEventHandler
{
    private PriorityQueue<InputPriority> subscribers = new PriorityQueue<>();

    public boolean handleEvent(GestureEvent event)
    {
        for (InputPriority ip : subscribers)
        {
            if (ip.handler.handleEvent(event))
            {
                return true;
            }
        }
        return false;
    }

    public void subscribe(GestureEventHandler handler, int priority)
    {
        subscribe(new InputPriority(handler, priority));
    }

    private void subscribe(InputPriority ip)
    {
        subscribers.add(ip);
    }

    private static class InputPriority implements Comparable<InputPriority>
    {
        public int priority = 0;
        public GestureEventHandler handler;

        InputPriority(GestureEventHandler handler, int priority)
        {
            this.priority = priority;
            this.handler = handler;
        }

        @Override
        public int compareTo(InputPriority input)
        {
            return priority - input.priority;
        }
    }
}
