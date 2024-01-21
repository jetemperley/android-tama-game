package com.tama.core;

public class Text implements UpdateDraw
{
    private String text;

    public Text(String text)
    {
        if (text == null)
            throw new RuntimeException("Text string must be non-null.");
        this.text = text;
    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(DisplayAdapter display)
    {

    }
}
