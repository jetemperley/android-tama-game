package com.game.tama.ui;

import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.tama.core.Assets;
import com.game.tama.core.Drawable;
import com.game.tama.core.Loadable;
import com.game.tama.core.Sprite;
import com.game.tama.core.SpriteSheet;
import com.game.tama.core.Updateable;

public class TextLeaf extends UIComposite
{
    private String text;
    private transient Sprite[] letters;
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
        load();
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

    @Override
    public void load()
    {
        SpriteSheet sheet = Assets.getSheet(Assets.Names.sheet_8_symbols.name());
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
}
