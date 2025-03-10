package com.game.tama.engine.behaviour;

import android.graphics.Matrix;

import com.game.android.DepthDisplay;
import com.game.engine.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.Input;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.engine.Transform;
import com.game.tama.core.AssetName;
import com.game.tama.command.CommandFactory;
import com.game.tama.command.CommandQueue;
import com.game.android.Asset;
import com.game.tama.core.World;
import com.game.tama.thing.pet.Pet;
import com.game.tama.thing.Thing;
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
    private DepthDisplay depthDisplay = new DepthDisplay();

    private ThingControlsBehaviour controlsBehaviour;

    public PetGardenBehaviour(Node parent)
    {
        super(parent);
        PetGardenBehaviourConfigurer.testConfiguration(this);
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

    public void draw(DisplayAdapter display)
    {
        world.draw(display);
        if (selected != null)
        {
            display.drawArr(
                Asset.getStaticSprite(AssetName.static_inv),
                selected.loc.getWorldArrPos().x,
                selected.loc.getWorldArrPos().y);
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
    public void setHeld(float ax, float ay)
    {
        Log.log(this, "checking in " + ax + " " + ay);
        Thing thing = world.checkCollision(ax, ay);
        if (thing == null)
        {
            Log.log(this, "thing in set held was null");
            return;
        }
        Log.log(this, thing.getClass().getCanonicalName() + " set as held");
        thing = world.pickupThing(thing.loc.x, thing.loc.y);
        GameManager.INST.heldBehaviour.setHeld(thing, ax, ay);
    }

    public float[] transferFromContainer(Thing t, Matrix containerMat)
    {
        // TODO fix this
        float[] f = t.loc.getWorldBitPosAsArray();
        Log.log(
            this,
            "transferFromContainer object loc is " + f[0] + " " + f[1]);
        containerMat.mapPoints(f);
        Log.log(
            this,
            "transferFromContainer screen loc is " + f[0] + " " + f[1]);

        Matrix inv = new Matrix();
        tempMat.reset();
        //getTempWorldTransform().invert();
        inv.mapPoints(f);
        Log.log(
            this,
            "transferFromContainer world loc is " + f[0] + " " + f[1]);

        t.loc.x = (int) f[0];
        t.loc.y = (int) f[1];
        // heldThing.setHeld(t, f[0], f[1]);
        return f;
    }

    public void setSelected(int x, int y)
    {
        selected = world.getThing(x, y);
    }

    public void pickup(float x, float y)
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

    public Thing getThing(float x, float y)
    {
        return world.checkCollision(x, y);
    }

    public void select(float x, float y)
    {
        Thing t = getThing(x, y);
        select(t);
    }

    public void select(Thing t)
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
    public void poke(float x, float y)
    {
        Thing t = world.checkCollision(x, y);
        if (t != null)
        {
            t.poke();
        }
    }

    public void use(float x, float y)
    {
        Thing t = world.checkCollision(x, y);
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
    public void doubleSelect(float ax, float ay)
    {
        if (!(selected instanceof Pet))
        {
            return;
        }

        Pet pet = (Pet) selected;
        Thing thing = world.checkCollision(ax, ay);
        if (thing == pet)
        {
            pet.poke();
            Log.log(this, "double tapped the selected pet");
            return;
        }
        if (thing == null)
        {
            CommandQueue walk =
                CommandFactory.commandPathTo((int) ax, (int) ay);
            pet.currentCommand.replace(walk);
            return;
        }
        pet.setActionTarget(thing);
    }

    @Override
    public void singleTapConfirmed(float x, float y)
    {

        Thing t = getThing(x, y);
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
    public void singleDown(float x, float y)
    {

    }

    @Override
    public void longPressConfirmed(float x, float y)
    {
        use(x, y);
    }

    @Override
    public void scale(Vec2<Float> p1,
                      Vec2<Float> p2,
                      Vec2<Float> n1,
                      Vec2<Float> n2)
    {
        // find the centres of the touch pairs
        Vec2<Float> pmid = new Vec2((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        Vec2<Float> nmid = new Vec2((n1.x + n2.x) / 2, (n1.y + n2.y) / 2);

        // translations
        float xmd = nmid.x - pmid.x;
        float ymd = nmid.y - pmid.y;

        // scales
        float px = p2.x - p1.x;
        float py = p2.y - p1.y;
        float psize = (float) Math.sqrt((px * px) + (py * py));

        float nx = n2.x - n1.x;
        float ny = n2.y - n1.y;
        float nsize = (float) Math.sqrt((nx * nx) + (ny * ny));

        float scale = nsize / psize;

        // apply changes
        node.localTransform.postTranslate(-nmid.x, -nmid.y, 0);
        node.localTransform.postScale(scale, scale, 1);
        node.localTransform.postTranslate(nmid.x, nmid.y, 0);
        node.localTransform.postTranslate(nmid.x - pmid.x, nmid.y - pmid.y, 0);
    }

    @Override
    public void dragStart(float x, float y)
    {
        Thing touched = world.checkCollision(x, y);
        if (selected != null && selected == touched)
        {
            setHeld(x, y);
        }
    }

    @Override
    public void drag(Vec2<Float> prev, Vec2<Float> next)
    {
        node.localTransform.postTranslate(next.x - prev.x, next.y - prev.y, 0);
    }

    @Override
    public boolean handleEvent(GestureEvent e)
    {
        e.transform(getWorldTransform(tempMat).invert());
        if (containerManager.handleEvent(e, getWorldTransform(tempMat)))
        {
            return true;
        }
        e.callEvent(this);
        return true;
    }

    public void deselectThing()
    {
        selected = null;
        controlsBehaviour.removeControls();
    }
}
