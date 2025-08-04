package com.game.tama.engine.behaviour;

import android.graphics.Matrix;

import com.game.engine.Behaviour;
import com.game.engine.DisplayAdapter;
import com.game.engine.Node;
import com.game.engine.Transform;
import com.game.engine.gesture.Input;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.tama.command.CommandFactory;
import com.game.tama.command.CommandQueue;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;
import com.game.tama.ui.ContainerManager;
import com.game.tama.util.Log;
import com.game.tama.util.Vec2;

public class PetGardenBehaviour extends Behaviour implements Input
{
    public MenuBehaviour thingMenu;

    public World world;
    public Transform tempMat = node.newTransform();
    public ContainerManager containerManager = new ContainerManager();

    boolean showBackpack = true;

    private Thing selected = null;

    private final ThingControlsBehaviour controlsBehaviour;

    public PetGardenBehaviour(final Node parent)
    {
        super(parent);
        BehaviourBuilder.testConfiguration(this);
        controlsBehaviour = new ThingControlsBehaviour(parent);
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
     * @param ax array tap
     * @param ay array tap
     */
    public void setHeld(final float ax, final float ay)
    {
        Log.log(this, "checking in " + ax + " " + ay);
        Thing thing = world.checkCollision(ax, ay);
        if (thing == null)
        {
            Log.log(this, "thing in set held was null");
            return;
        }
        thing = world.pickupThing(thing.loc.x, thing.loc.y);
        GameManager.INST.heldBehaviour.setHeld(thing, ax, ay);
    }

    public float[] transferFromContainer(final Thing t, final Matrix containerMat)
    {
        // TODO fix this
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
        // getTempWorldTransform().invert();
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

    public void pickup(final float x, final float y)
    {
        //        if (heldThing.held != null)
        //        {
        //            heldThing.setPos(x, y);
        //            return;
        //        }
        //        Thing t = world.checkCollision(x, y);
        //        if (t != null)
        //        {
        //            heldThing.held = world.pickupThing(t.loc.x, t.loc.y);
        //            heldThing.setPos(x, y);
        //            Vec2<Float> pos = heldThing.held.loc.getWorldArrPos();
        //            heldThing.setHeldOffset(x - pos.x, y - pos.y);
        //        }
    }

    public Thing getThing(final float x, final float y)
    {
        return world.checkCollision(x, y);
    }

    public void select(final float x, final float y)
    {
        final Thing t = getThing(x, y);
        select(t);
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
        if (!(selected instanceof Pet))
        {
            return;
        }

        final Pet pet = (Pet) selected;
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

    @Override
    public void singleTapConfirmed(final float x, final float y)
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

    @Override
    public void singleDown(final float x, final float y)
    {

    }

    @Override
    public void longPressConfirmed(final float x, final float y)
    {
        use(x, y);
    }

    @Override
    public void scale(final Vec2<Float> p1,
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

        final float[] nmidt = target.mapVector(nmid.x, nmid.y, 0);
        final float[] pmidt = target.mapVector(pmid.x, pmid.y, 0);

        final Transform transform = target.copy().reset();
        transform.postTranslate(-nmidt[0], -nmidt[1], 0);
        transform.postScale(scale, scale, 1);
        transform.postTranslate(nmidt[0], nmidt[1], 0);
        transform.postTranslate(nmidt[0] - pmidt[0], nmidt[1] - pmidt[1], 0);

        node.localTransform.postMult(transform);
    }

    @Override
    public void dragStart(final float x, final float y)
    {
        final Thing touched = world.checkCollision(x, y);
        if (selected != null && selected == touched)
        {
            setHeld(x, y);
        }
    }

    @Override
    public void drag(final Vec2<Float> prev, final Vec2<Float> next)
    {
        node.localTransform.preTranslate(next.x - prev.x, next.y - prev.y, 0);
    }

    @Override
    public boolean handleEvent(final GestureEvent e)
    {
        final GestureEvent localEvent =
            e.transform(getWorldTransform(tempMat).invert());
        if (containerManager.handleEvent(localEvent))
        {
            return true;
        }
        localEvent.callEvent(this);
        return true;
    }

    public void deselectThing()
    {
        selected = null;
        controlsBehaviour.removeControls();
    }
}
