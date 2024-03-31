package com.game.android.gesture;

import com.game.tama.core.Updateable;

import java.util.PriorityQueue;

public class EventPrioritySubscriber implements Updateable
{
    private PriorityQueue<InputPriority> subscribers = new PriorityQueue<>();

    private GestureEventPipe eventSource;

    public EventPrioritySubscriber(GestureEventPipe eventSource)
    {
        this.eventSource = eventSource;
    }

    @Override
    public void update()
    {
        GestureEvent e = eventSource.getCurrentEvent();
        if (e == null)
            return;
        handleEvent(e);
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

    private void handleEvent(GestureEvent event)
    {
        for (InputPriority ip : subscribers)
        {
            if (ip.handler.handleEvent(event))
            {
                return;
            }
        }
    }

    public void subscribe(GestureEventHandler handler, int priority)
    {
        subscribe(new InputPriority(handler, priority));
    }

    private void subscribe(InputPriority ip)
    {
        subscribers.add(ip);
    }
}
