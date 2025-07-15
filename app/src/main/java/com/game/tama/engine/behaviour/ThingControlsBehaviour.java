package com.game.tama.engine.behaviour;

import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;
import com.game.tama.thing.ThingControl;
import com.game.tama.ui.SquareCellButtonLeaf;
import com.game.tama.ui.UIComposite;
import com.game.tama.ui.UINode;

import java.util.List;

public class ThingControlsBehaviour extends Behaviour
{

    private UINode menu;
    private List<ThingControl> currentControls;
    private ThingControl selectedControl = null;
    private final Runnable deselectControl;
    private final PetGardenBehaviour petGardenBehaviour;

    public ThingControlsBehaviour(final Node parent)
    {
        this(parent, null);
    }

    public ThingControlsBehaviour(final Node parent, final UINode menu)
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
    public void setCurrentControls(final Thing thing)
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

        final List<ThingControl> controls = thing.getControls();
        selectedControl = null;
        currentControls = controls;

        final UINode controlNode = new UINode();

        // add the thing picture
        controlNode.add(
            Thing.class,
            new SquareCellButtonLeaf(1, 0, 1, 1, thing.getAsset()));
        // set the controls defined by the thing
        for (int i = 0; i < controls.size(); i++)
        {
            final int index = i;
            final ThingControl tc = controls.get(i);
            controlNode.add(
                tc,
                new SquareCellButtonLeaf(
                    (i + 2),
                    0,
                    1,
                    1,
                    Asset.sprites.get(tc.assetName),
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
                controls.size() + 2,
                0,
                1,
                1,
                Asset.sprites.get(AssetName.static_x),
                deselectControl));
        menu.add(this, controlNode);
    }

    public void removeControls()
    {
        menu.remove(this);
        this.currentControls = null;
        selectedControl = null;
    }

    public void setMenuRoot(final UINode root)
    {
        if (menu != null)
        {
            final UIComposite controls = menu.remove(this);
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
    public void executeCurrentControl(final Thing thing,
                                      final World world,
                                      final float x,
                                      final float y)
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
