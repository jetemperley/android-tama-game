package com.game.tama.ui;

import com.game.engine.DisplayAdapter;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.SpriteSheet;

public class TextLeaf extends UIComposite
{
    private final String text;
    private Sprite[] letters;
    float xPos;
    float yPos;

    public TextLeaf(final String text)
    {
        this(text, 0, 0);
    }

    public TextLeaf(final String text, final float x, final float y)
    {
        this.xPos = x;
        this.yPos = y;
        if (text == null)
        {throw new RuntimeException("Text string must be non-null.");}
        this.text = text;
        loadChars();
    }

    @Override
    public void draw(final DisplayAdapter display)
    {
        int x = 0;
        for (final Sprite d : letters)
        {
            if (d != null)
            {
                display.drawSprite(d, xPos + x, yPos);
            }
            x += 8;
        }
    }

    public void draw(final DisplayAdapter display, final int firstN)
    {
        int x = 0;
        for (int i = 0; i < firstN && i < letters.length; i++)
        {
            final Sprite d = letters[i];
            if (d != null)
            {
                display.drawSprite(d, xPos + x, yPos);
            }
            x += 8;
        }
    }

    public void loadChars()
    {
        final SpriteSheet sheet = Asset.sheets.get(AssetName.sheet_8_symbols);
        letters = new Sprite[text.length()];
        final byte[] bytes = text.getBytes();
        for (int i = 0; i < text.length(); i++)
        {
            final byte b = bytes[i];
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
                letters[i] = sheet.getSprite(b - '!');
            }
        }
    }

    /**
     * Set the position in pixel coordinates
     *
     * @param xPos
     * @param yPos
     */
    public void setPos(final float xPos, final float yPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public boolean handleEvent(final GestureEvent event)
    {
        return false;
    }

    @Override
    public boolean isInside(final float x, final float y)
    {
        return false;
    }
}
