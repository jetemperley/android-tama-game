package com.game.tama.engine.behaviour;

import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.Assets;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;
import com.game.tama.thing.ThingControl;
import com.game.tama.ui.SquareCellButtonLeaf;
import com.game.tama.ui.UIComposite;
import com.game.tama.ui.UINode;

import java.util.List;
import java.util.stream.Collectors;

public class ThingControlsBehaviour extends Behaviour
{

    private UINode menu;
    private List<ThingControl> currentControls;
    private ThingControl selectedControl = null;
    private final Runnable deselectControl;
    private final PetGardenBehaviour petGardenBehaviour;

    public ThingControlsBehaviour(Node parent)
    {
        this(parent, null);
    }

    public ThingControlsBehaviour(Node parent, UINode menu)
    {
        super(parent);
        this.menu = menu;
        petGardenBehaviour = parent.getBehaviour(PetGardenBehaviour.class);
        deselectControl = petGardenBehaviour::deselectThing;
    }

    /**
     * Set the controls to the list provied, plus a default deselect option.
     *
     * @param thing
     */
    public void setCurrentControls(Thing thing)
    {
        if (menu == null)
        {
            throw new RuntimeException(
                "Tried to add controls with no menu set.");
        }
        if (thing == null)
        {
            throw new RuntimeException(
                "Cannot set the controls for a null thing.");
        }

        List<ThingControl> controls = thing.getControls();
        selectedControl = null;
        currentControls = controls;

        UINode controlNode = new UINode();

        // add the thing picture
        controlNode.add(
            Thing.class,
            new SquareCellButtonLeaf(16, 0, 1, 1, thing.getAsset()));
        // set the controls defined by the thing
        for (int i = 0; i < controls.size(); i++)
        {
            final int index = i;
            final ThingControl tc = controls.get(i);
            controlNode.add(
                tc,
                new SquareCellButtonLeaf(
                    16 * (i + 2),
                    0,
                    1,
                    1,
                    Assets.getSprite(tc.assetName.name()),
                    () ->
                    {
                        if (selectedControl == tc)
                        {
                            selectedControl = null;
                        }
                        else
                        {
                            selectedControl = tc;
                        }
                    }
                ));
        }
        controlNode.add(
            deselectControl,
            new SquareCellButtonLeaf(
                16 * (controls.size() + 2),
                0,
                1,
                1,
                Assets.getSprite(Assets.Names.static_x.name()),
                deselectControl));
        menu.add(this, controlNode);
    }

    public void removeControls()
    {
        menu.remove(this);
        this.currentControls = null;
        selectedControl = null;
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

    /**
     * The parameters are passed to the current controls ThingControlLambda
     *
     * @param thing
     * @param world
     * @param x
     * @param y
     */
    public void executeCurrentControl(Thing thing,
                                      World world,
                                      float x,
                                      float y)
    {
        if (selectedControl == null)
        {
            return;
        }
        selectedControl.func.execute(thing, world, x, y);
    }

    public void deselectControl()
    {
        selectedControl = null;
    }

    public ThingControl getSelectedControl()
    {
        return selectedControl;
    }
}
