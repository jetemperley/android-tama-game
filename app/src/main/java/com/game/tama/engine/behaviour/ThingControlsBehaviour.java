package com.game.tama.engine.behaviour;

import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.Assets;
import com.game.tama.thing.ThingControl;
import com.game.tama.ui.SquareCellButtonLeaf;
import com.game.tama.ui.UIComposite;
import com.game.tama.ui.UINode;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ThingControlsBehaviour extends Behaviour
{

    private UINode menu;
    private List<ThingControl> currentControls;
    private int selectedControl = -1;

    public ThingControlsBehaviour(Node parent)
    {
        this(parent, null);
    }

    public ThingControlsBehaviour(Node parent, UINode menu)
    {
        super(parent);
        this.menu = menu;
    }

    public void setCurrentControls(List<ThingControl.Name> controls)
    {
        if (menu == null)
        {
            throw new RuntimeException("Tried to add controls with no menu " +
                "set.");
        }
        selectedControl = -1;
        menu.remove(this);
        if (controls != null)
        {
            List<ThingControl> thingControls = controls.stream()
                .map(name -> ThingControl.controls.get(name))
                .collect(
                    Collectors.toList());
            this.currentControls = thingControls;
            addNewControls(thingControls);
        }
        else
        {
            this.currentControls = null;
        }
    }

    private void addNewControls(List<ThingControl> controls)
    {
        UINode controlNode = new UINode();
        for (int i = 0; i < controls.size(); i++)
        {
            final int index = i;
            ThingControl tc = controls.get(i);
            controlNode.add(
                tc,
                new SquareCellButtonLeaf(
                    16 * i,
                    16,
                    1,
                    1,
                    tc.assetName,
                    () ->
                    {
                        if (selectedControl == index)
                        {
                            selectedControl = -1;
                        }
                        else
                        {
                            selectedControl = index;
                        }
                    }
                ));
        }
        menu.add(this, controlNode);
    }

    public void setMenuRoot(UINode root)
    {
        if (menu != null)
        {
            UIComposite controls = menu.remove(this);
            if (root != null && controls != null)
            {
                root.add(this, controls);
            }
        }
        menu = root;
    }
}
