package com.game.tama.ui;

import com.game.engine.DisplayAdapter;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.android.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.SpriteSheet;

public class TextLeaf extends UIComposite
{
    private String text;
    private Sprite[] letters;
    float xPos;
    float yPos;

    public TextLeaf(String text)
    {
        this(text, 0, 0);
    }

    public TextLeaf(String text, float x, float y)
    {
        this.xPos = x;
        this.yPos = y;
        if (text == null)
            throw new RuntimeException("Text string must be non-null.");
        this.text = text;
        loadChars();
    }

    @Override
    public void draw(DisplayAdapter display)
    {
        int x = 0;
        for (Sprite d : letters)
        {
            if (d != null)
            {
                display.drawSprite(d, xPos + x, yPos);
            }
            x+=8;
        }
    }

    public void draw(DisplayAdapter display, int firstN)
    {
        int x = 0;
        for (int i = 0; i < firstN && i < letters.length; i++)
        {
            Sprite d = letters[i];
            if (d != null)
            {
                display.drawSprite(d, xPos + x, yPos);
            }
            x+=8;
        }
    }

    public void loadChars()
    {
        SpriteSheet sheet = Asset.getSpriteSheet(AssetName.sheet_8_symbols);
        letters = new Sprite[text.length()];
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

    @Override
    public boolean handleEvent(GestureEvent event)
    {
        return false;
    }

    @Override
    public boolean isInside(float x, float y)
    {
        return false;
    }
}
