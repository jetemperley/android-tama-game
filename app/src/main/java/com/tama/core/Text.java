package com.tama.core;

public class Text implements UpdateDraw, Loadable
{
    private String text;
    private transient Displayable[] letters;
    float xPos;
    float yPos;

    public Text(String text, float x, float y)
    {
        this.xPos = x;
        this.yPos = y;
        if (text == null)
            throw new RuntimeException("Text string must be non-null.");
        this.text = text;
        load();
    }

    public Text(String text)
    {
        this(text, 0, 0);
    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(DisplayAdapter display)
    {
        int x = 0;
        for (Displayable d : letters)
        {
            if (d != null)
            {
                display.displayAbsolute(d, xPos + x, yPos);
            }
            x+=8;
        }
    }

    public void draw(DisplayAdapter display, int firstN)
    {
        int x = 0;
        for (int i = 0; i < firstN && i < letters.length; i++)
        {
            Displayable d = letters[i];
            if (d != null)
            {
                display.displayAbsolute(d, xPos + x, yPos);
            }
            x+=8;
        }
    }

    @Override
    public void load()
    {
        SpriteSheet sheet = Assets.getSheet(Assets.Names.sheet_8_symbols.name());
        letters = new Displayable[text.length()];
        byte[] bytes = text.getBytes();
        for (int i = 0; i < text.length(); i++)
        {
            byte b = bytes[i];
            if ((b < '!' || b > 'z') && b != ' ')
            {
                throw new RuntimeException("Text character must be a-z");
            }
            if (b == ' ')
            {
                letters[i] = null;
            }
            else
            {
                letters[i] = sheet.getSprite(b-'!');
            }
        }
    }

    /**
     * Set the position in pixel coordinates
     * @param xPos
     * @param yPos
     */
    public void setPos(float xPos, float yPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
