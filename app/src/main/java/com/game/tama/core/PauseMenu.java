package com.game.tama.core;

import android.graphics.Matrix;
import android.graphics.Rect;

import com.game.android.DisplayAdapter;
import com.game.android.GameActivity;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.GestureEventHandler;
import com.game.tama.util.MatrixUtil;

public class PauseMenu extends Behaviour implements GestureEventHandler
{
    public ButtonManager buttons;
    private Text text, text2;
    private DialogueTextBox box;

    public PauseMenu(Node parent)
    {
        super(parent);
        Matrix matrix = new Matrix();
        float scale = MatrixUtil.getScaleToFitWidth(10 * 16);
        matrix.setScale(scale, scale);

        Rect size = GameActivity.screenSize;
        buttons = new ButtonManager(matrix);
        buttons.add(new Button(50, 50, Assets.Names.static_poop.name())
        {
            @Override
            void activate()
            {
                GameManager.INST.play();
            }
        });
        text = new Text("a hello z", 0, 0);
        text2 = new Text("aBz, cool.", 0, 8);
        box = new DialogueTextBox(
            0,
            70,
            10,
            3,
            "Ayee this is some text that is gonna be in a text box and " +
                "hopefullly its just gonna work the first time.!!");
        buttons.add(box);
    }

    @Override
    public void draw(DisplayAdapter d)
    {
        buttons.draw(d);
    }

    @Override
    public void update()
    {
        buttons.update();
    }

    @Override
    public boolean handleEvent(GestureEvent event)
    {
        buttons.handleEvent(event);
        return true;
    }
}
