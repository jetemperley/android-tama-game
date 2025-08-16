package com.game.tama.engine.behaviour.garden;

import android.graphics.Matrix;

import com.game.engine.Behaviour;
import com.game.engine.DisplayAdapter;
import com.game.engine.Node;
import com.game.engine.Transform;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.tama.command.CommandFactory;
import com.game.tama.command.CommandQueue;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;
import com.game.tama.engine.behaviour.BehaviourBuilder;
import com.game.tama.engine.behaviour.GameManager;
import com.game.tama.engine.behaviour.MenuBehaviour;
import com.game.tama.engine.behaviour.ThingControlsBehaviour;
import com.game.tama.ui.ContainerManager;
import com.game.tama.util.Log;
import com.game.tama.util.Vec2;
import com.game.tama.util.Vec4;

public class PetGardenBehaviour extends Behaviour
{
    public MenuBehaviour thingMenu;

    public World world;
    public Transform tempMat = node.newTransform();
    public ContainerManager containerManager = new ContainerManager();

    boolean showBackpack = true;

    private Thing selected = null;

    private final ThingControlsBehaviour controlsBehaviour;

    GardenInputStateManger stateManger;

    public PetGardenBehaviour(final Node parent)
    {
        super(parent);
        BehaviourBuilder.testConfiguration(this);
        controlsBehaviour = new ThingControlsBehaviour(parent);
        stateManger = new GardenInputStateManger(this);
    }

    @Override
    public void start()
    {
        controlsBehaviour.setMenuRoot(GameManager.getHud().root);
    }

    @Override
    public void update()
    {
        world.update();
    }

    @Override
    public void draw(final DisplayAdapter display)
    {
        world.draw(display);
        if (selected != null)
        {
            display.draw(
                Asset.sprites.get(AssetName.static_inv),
                selected.loc.getWorldArrPos().x,
                selected.loc.getWorldArrPos().y,
                -2);
            if (selected instanceof Pet)
            {
                ((Pet) selected).currentCommand.draw(display);
            }
        }
        //        display.setMatrix(identity);
        if (showBackpack)
        {
            // backpackSlot.draw(display);
        }
        containerManager.draw(display);
    }

    /**
     * @param thing thing thing which we are picking up
     * @param ax    array tap
     * @param ay    array tap
     */
    public void setHeld(final Thing thing, final float ax, final float ay)
    {
        final Thing taken = world.pickupThing(thing.loc.x, thing.loc.y);
        GameManager.INST.heldBehaviour.setHeld(taken, ax, ay);
    }

    public float[] transferFromContainer(final Thing t, final Matrix containerMat)
    {
        // TODO fix this method
        final float[] f = t.loc.getWorldBitPosAsArray();
        Log.log(
            this,
            "transferFromContainer object loc is " + f[0] + " " + f[1]);
        containerMat.mapPoints(f);
        Log.log(
            this,
            "transferFromContainer screen loc is " + f[0] + " " + f[1]);

        final Matrix inv = new Matrix();
        tempMat.reset();
        inv.mapPoints(f);
        Log.log(
            this,
            "transferFromContainer world loc is " + f[0] + " " + f[1]);

        t.loc.x = (int) f[0];
        t.loc.y = (int) f[1];
        // heldThing.setHeld(t, f[0], f[1]);
        return f;
    }

    public void setSelected(final int x, final int y)
    {
        selected = world.getThing(x, y);
    }

    public Thing getThing(final float x, final float y)
    {
        return world.checkCollision(x, y);
    }

    public void select(final Thing t)
    {
        if (t == null || selected == t)
        {
            controlsBehaviour.removeControls();
            selected = null;
        }
        else
        {
            controlsBehaviour.setCurrentControls(t);
            selected = t;
        }
    }

    /**
     * poke the world position x y
     *
     * @param x coord relative to array position
     * @param y coord relative to array position
     */
    public void poke(final float x, final float y)
    {
        final Thing t = world.checkCollision(x, y);
        if (t != null)
        {
            t.poke();
        }
    }

    public void use(final float x, final float y)
    {
        final Thing t = world.checkCollision(x, y);
        if (t != null)
        {
            t.use();
        }
    }

    /**
     * Triggers the held object to target position x, y
     *
     * @param ax coord relative to array position
     * @param ay coord relative to array position
     */
    public void doubleSelect(final float ax, final float ay)
    {
        if (!(selected instanceof final Pet pet))
        {
            return;
        }

        final Thing thing = world.checkCollision(ax, ay);
        if (thing == pet)
        {
            pet.poke();
            Log.log(this, "double tapped the selected pet");
            return;
        }
        if (thing == null)
        {
            final CommandQueue walk =
                CommandFactory.commandPathTo((int) ax, (int) ay);
            pet.currentCommand.replace(walk);
            return;
        }
        pet.setActionTarget(thing);
    }

    public void select(final float x, final float y)
    {
        final Thing t = getThing(x, y);
        if (selected == null || t == selected ||
            controlsBehaviour.getSelectedControl() == null)
        {
            select(t);
        }
        else
        {
            controlsBehaviour.executeCurrentControl(
                selected,
                world,
                x,
                y);
        }
    }

    public void scaleView(final Vec2<Float> p1,
                          final Vec2<Float> p2,
                          final Vec2<Float> n1,
                          final Vec2<Float> n2)
    {
        // find the centres of the touch pairs
        final Vec2<Float> pmid = new Vec2((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        final Vec2<Float> nmid = new Vec2((n1.x + n2.x) / 2, (n1.y + n2.y) / 2);

        // translations
        final float xmd = nmid.x - pmid.x;
        final float ymd = nmid.y - pmid.y;

        // scales
        final float px = p2.x - p1.x;
        final float py = p2.y - p1.y;
        final float psize = (float) Math.sqrt((px * px) + (py * py));

        final float nx = n2.x - n1.x;
        final float ny = n2.y - n1.y;
        final float nsize = (float) Math.sqrt((nx * nx) + (ny * ny));

        final float scale = nsize / psize;

        final Transform target = node.localTransform;

        final Vec4<Float> nmidt = target.mapVector(nmid.x, nmid.y, 0);
        final Vec4<Float> pmidt = target.mapVector(pmid.x, pmid.y, 0);

        final Transform transform = target.copy().reset();
        transform.postTranslate(-nmidt.x, -nmidt.y, 0);
        transform.postScale(scale, scale, 1);
        transform.postTranslate(nmidt.x, nmidt.y, 0);
        transform.postTranslate(nmidt.x - pmidt.x, nmidt.y - pmidt.y, 0);

        node.localTransform.postMult(transform);
    }

    /**
     * Attempts to pick up the thing at world location x, y.
     */
    public boolean attemptPickup(final float x, final float y)
    {
        final Thing touched = world.checkCollision(x, y);
        if (selected != null && selected == touched)
        {
            setHeld(touched, x, y);
            return true;
        }
        return false;
    }

    public void panWorld(final Vec2<Float> prev, final Vec2<Float> next)
    {
        node.localTransform.preTranslate(next.x - prev.x, next.y - prev.y, 0);
    }

    @Override
    public void handleInput(final GestureEvent event)
    {
        final GestureEvent localEvent =
            event.transform(node.getWorldTransform().invert());
        if (containerManager.handleEvent(localEvent))
        {
            return;
        }
        stateManger.handleEvent(localEvent);
    }

    public void deselectThing()
    {
        selected = null;
        controlsBehaviour.removeControls();
    }

    public Thing getSelected()
    {
        return selected;
    }

    /** Checks if the touch x, y (world coords) is touching the currently selected thing */
    public boolean isTouchingSelectedThing(final float x, final float y)
    {
        return selected != null && getThing(x, y) == selected;
    }
}
