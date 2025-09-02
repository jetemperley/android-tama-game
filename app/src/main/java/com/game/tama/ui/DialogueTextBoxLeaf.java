package com.game.tama.ui;

import com.game.engine.DisplayAdapter;
import com.game.engine.Time;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueTextBoxLeaf extends SquareCellButtonLeaf
{
    private final String text;
    private final List<TextLeaf> lines;
    private final int lettersPerLine;
    private final int linesPerBox;
    private float currentLetter = 0;
    private int currentLine = 0;
    private final float runTime = 0;

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     * @param width  size (in 16 bit cells) of button
     * @param height size (in 16 bit cells) of button
     * @param text   the text to be displayed
     */
    public DialogueTextBoxLeaf(final float xPos,
                               final float yPos,
                               final int width,
                               final int height,
                               final String text)
    {
        super(xPos, yPos, width, height, Asset.sprites.get(AssetName.static_empty));
        this.text = text;
        lettersPerLine =
            width * 2 - 1; // 8 pixels per letter, 16 pixels per cell
        linesPerBox = height;
        lines = splitIntoLines(
            text,
            lettersPerLine).stream()
                           .map(TextLeaf::new)
                           .collect(Collectors.toList());
    }

    @Override
    public void draw(final DisplayAdapter d)
    {
        super.draw(d);
        // initial offsets
        final float x = 4;
        final float y = 4;
        // draw the text
        for (int i = 0; i < linesPerBox; i++)
        {
            final int lineNum = (currentLine + i) % lines.size();
            final int lettersToDraw = (int) currentLetter - lettersPerLine * lineNum;
            final TextLeaf line = lines.get(lineNum);
            line.setPos(pos.x + 0.25f, pos.y + 0.25f + i);
            line.draw(d, lettersToDraw);
        }
    }

    @Override
    public void update()
    {
        currentLetter += Time.deltaTime() * 15;
        final int letterLimit =
            currentLine * lettersPerLine + lettersPerLine * linesPerBox;
        if (currentLetter > letterLimit)
        {
            currentLetter = letterLimit;
        }
    }

    @Override
    public void activate()
    {
        // move to next text box
        if (currentLetter <
            currentLine * lettersPerLine + linesPerBox * lettersPerLine)
        {
            currentLetter =
                currentLine * lettersPerLine + linesPerBox * lettersPerLine;
        }
        else
        {
            currentLine += linesPerBox;
        }
    }

    public static List<String> splitIntoLines(final String text, final int maxLineLength)
    {
        final String[] words = text.split(" ");
        if (words.length == 0)
        {
            throw new RuntimeException("Text should have at least 1 word.");
        }

        final List<String> lines = new ArrayList<>();
        String currentLine = words[0];
        for (int i = 1; i < words.length; i++)
        {
            if (currentLine.length() + 1 + words[i].length() > maxLineLength)
            {
                lines.add(currentLine);
                currentLine = words[i];
            }
            else
            {
                currentLine = currentLine + " " + words[i];
            }
        }

        if (!currentLine.isEmpty())
        {
            lines.add(currentLine);
        }
        return lines;
    }

    public boolean isDone()
    {
        return currentLetter < lettersPerLine * lines.size();
    }
}
