package com.game.android.gesture;

import java.util.PriorityQueue;

/**
 * Manages a list of input handlers, which each have a priority (lowest first).
 * If a handler returns true, then the event is considered consumed and will not
 * be passed to sequential input handlers.
 */
public class EventPrioritySubscriber implements GestureEventHandler
{
    private PriorityQueue<InputPriority> subscribers = new PriorityQueue<>();

    public boolean handleEvent(GestureEvent event)
    {
        for (InputPriority input : subscribers)
        {
            if (input.handleEvent(event))
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
        public int priority;
        private final GestureEventHandler handler;

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


        public boolean handleEvent(GestureEvent event)
        {
            return handler.handleEvent(event);
        }
    }
}
