package com.game.tama.ui;

import com.game.android.DisplayAdapter;
import com.game.tama.core.GameLoop;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueTextBox extends Button
{
    private String text;
    private List<Text> lines;
    private final int lettersPerLine;
    private final int linesPerBox;
    private float currentLetter = 0;
    private int currentLine = 0;
    private float runTime = 0;

    /**
     * @param xPos   pixel x position of the button
     * @param yPos   pixel y position of the button
     * @param width  size (in 16 bit cells) of button
     * @param height size (in 16 bit cells) of button
     * @param text   the text to be displayed
     */
    public DialogueTextBox(float xPos,
                           float yPos,
                           int width,
                           int height,
                           String text)
    {
        super(xPos, yPos, width, height, null);
        this.text = text;
        lettersPerLine =
            width * 2 - 1; // 8 pixels per letter, 16 pixels per cell
        linesPerBox = height;
        lines = splitIntoLines(
            text,
            lettersPerLine).stream()
            .map((s) -> new Text(s))
            .collect(Collectors.toList());
    }

    @Override
    public void draw(DisplayAdapter d)
    {
        super.draw(d);
        // initial offsets
        float x = 4;
        float y = 4;
        // draw the text
        for (int i = 0; i < linesPerBox; i++)
        {
            int lineNum = (currentLine + i) % lines.size();
            int lettersToDraw = (int) currentLetter - lettersPerLine * lineNum;
            Text line = lines.get(lineNum);
            line.setPos(pos.x + 4, pos.y + 4 + i * 16);
            line.draw(d, lettersToDraw);
        }
    }

    @Override
    public void update()
    {
        currentLetter += GameLoop.deltaTime * 15;
        int letterLimit =
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

    public static List<String> splitIntoLines(String text, int maxLineLength)
    {
        String[] words = text.split(" ");
        if (words.length == 0)
        {
            throw new RuntimeException("Text should have at least 1 word.");
        }

        List<String> lines = new ArrayList<>();
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
        return currentLetter < lettersPerLine*lines.size();
    }
}
