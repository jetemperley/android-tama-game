package com.game.tama.behaviour;

import android.graphics.Matrix;

import com.game.android.DepthDisplay;
import com.game.android.DisplayAdapter;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.Input;
import com.game.engine.Behaviour;
import com.game.engine.Node;
import com.game.tama.command.CommandFactory;
import com.game.tama.command.CommandQueue;
import com.game.tama.core.Assets;
import com.game.tama.core.Container;
import com.game.tama.core.GameLoop;
import com.game.tama.core.World;
import com.game.tama.core.WorldFactory;
import com.game.tama.thing.Pet;
import com.game.tama.thing.Thing;
import com.game.tama.ui.ContainerManager;
import com.game.tama.util.Log;
import com.game.tama.util.MatrixUtil;
import com.game.tama.util.Vec2;

public class PetGameBehaviour extends Behaviour implements Input
{
    public MenuBehaviour thingMenu;

    public Matrix tempMat = new Matrix();
    public ContainerManager containerManager = new ContainerManager();

    boolean showBackpack = true;

    private World world;
    private Thing selected = null;
    private DepthDisplay depthDisplay = new DepthDisplay();
    private Container backpackSlot;
    private Matrix identity = new Matrix();
    public HeldThing heldThing = new HeldThing();

    public PetGameBehaviour(Node parent)
    {
        super(parent);
        parent.transform.setScale(6, 6);
        // thingMenu = new MenuBehaviour(parent);
        world = WorldFactory.makeWorld();
        world.addOrClosest(new Container(2), 2, 2);

        Matrix uiMat = new Matrix();
        uiMat.setScale(6, 6);

        Matrix backpackMat = new Matrix();
        backpackMat.set(uiMat);
        backpackMat.preTranslate(16, 0);
        backpackSlot = null;//= new Container(this, 1);
    }

    public void update()
    {
        world.update();
    }

    public void draw(DisplayAdapter display)
    {
        depthDisplay.display = display;
        world.draw(depthDisplay);
        depthDisplay.drawQ();
        depthDisplay.clearQ();

        if (selected != null)
        {
            display.drawArr(
                Assets.getSprite(Assets.Names.static_inv.name()),
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
            //backpackSlot.draw(display);
        }
        containerManager.draw(display);
        heldThing.drawHeld(display);
    }

    public void reLoadAllAssets()
    {
        world.reLoadAllAssets();
        if (heldThing.held != null)
        {
            heldThing.held.load();
        }
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
        heldThing.setHeld(thing, ax, ay);
    }

    public void setHeld(Thing thing, float x, float y)
    {
        heldThing.setHeld(thing, x, y);
    }

    public float[] transferFromContainer(Thing t, Matrix containerMat)
    {

        float[] f = t.loc.getWorldBitPosAsArray();
        Log.log(
            this,
            "transferFromContainer object loc is " + f[0] + " " +
                f[1]);
        containerMat.mapPoints(f);
        Log.log(
            this,
            "transferFromContainer screen loc is " + f[0] + " " + f[1]);

        Matrix inv = new Matrix();
        tempMat.reset();
        getTempWorldTransform().invert(inv);
        inv.mapPoints(f);
        Log.log(
            this,
            "transferFromContainer world loc is " + f[0] + " " + f[1]);

        t.loc.x = (int) f[0];
        t.loc.y = (int) f[1];
        heldThing.setHeld(t, f[0], f[1]);
        return f;
    }

    public void setSelected(int x, int y)
    {
        selected = world.getThing(x, y);
    }

    public void pickup(float x, float y)
    {
        if (heldThing.held != null)
        {
            heldThing.setPos(x, y);
            return;
        }
        Thing t = world.checkCollision(x, y);
        if (t != null)
        {
            heldThing.held = world.pickupThing(t.loc.x, t.loc.y);
            heldThing.setPos(x, y);
            Vec2<Float> pos = heldThing.held.loc.getWorldArrPos();
            heldThing.setHeldOffset(x - pos.x, y - pos.y);
        }
    }

    void dropHeld()
    {
        //TODO make this drop properly
        dropHeld(heldThing.heldPos.x, heldThing.heldPos.y);
    }

    public void dropHeld(float x, float y)
    {
        Log.log(this, "dropping");
        world.addOrClosest(heldThing.held, (int) x, (int) y);
        heldThing.held = null;
    }

    public Thing getThing(float x, float y)
    {
        return world.checkCollision(x, y);
    }

    public void select(float x, float y)
    {
        Thing t = getThing(x, y);
        if (t == null || selected == t)
        {
            selected = null;
        }
        else
        {
            // TODO: add the things menu items
            //GameManager.INST.hudMenu.add();
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
            CommandQueue walk = CommandFactory.Companion.commandPathTo(
                (int) ax,
                (int) ay);
            pet.currentCommand.replace(walk);
            return;
        }
        pet.setActionTarget(thing);
    }

    @Override
    public void singleTapConfirmed(float x, float y)
    {
        float[] f =
            MatrixUtil.convertScreenToWorldArray(getWorldTransform(
                tempMat), x, y);

        Thing t = getThing(f[0], f[1]);
        if (selected == null || selected != t)
        {
            select(f[0], f[1]);
        }
        else
        {
            poke(f[0], f[1]);
            selected = null;
        }
    }

    @Override
    public void singleDown(float x, float y)
    {

    }

    @Override
    public void longPressConfirmed(float x, float y)
    {
        float[] f =
            MatrixUtil.convertScreenToWorldArray(
                getTempWorldTransform(), x, y);
        use(f[0], f[1]);
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
        node.transform.postTranslate(-nmid.x, -nmid.y);
        node.transform.postScale(scale, scale);
        node.transform.postTranslate(nmid.x, nmid.y);
        node.transform.postTranslate(
            nmid.x - pmid.x,
            nmid.y - pmid.y);
    }

    @Override
    public void dragStart(float x, float y)
    {
        float[] f =
            MatrixUtil.convertScreenToWorldArray(
                getTempWorldTransform(), x, y);
        Thing touched = world.checkCollision(f[0], f[1]);
        if (selected != null && selected == touched)
        {
            setHeld(f[0], f[1]);
        }
    }

    @Override
    public void drag(Vec2<Float> prev, Vec2<Float> next)
    {
        if (heldThing.held != null)
        {
            float[] f =
                MatrixUtil.convertScreenToWorldArray(
                    getTempWorldTransform(), next.x, next.y);
            heldThing.setPos(f[0], f[1]);
            return;
        }
        node.transform.postTranslate(
            next.x - prev.x,
            next.y - prev.y);
    }

    @Override
    public void dragEnd(float x, float y)
    {
        float[] f =
            MatrixUtil.convertScreenToWorldArray(
                getTempWorldTransform(), x, y);
        if (heldThing.held != null)
        {
            this.dropHeld(f[0], f[1]);
        }
    }

    @Override
    public boolean handleEvent(GestureEvent e)
    {
        if (containerManager.handleEvent(e, getTempWorldTransform()))
        {
            return true;
        }
        e.callEvent(this);
        return true;
    }

    public Matrix getTempWorldTransform()
    {
        return getWorldTransform(tempMat);
    }

    public static class HeldThing
    {
        public Thing held = null;
        public Vec2<Float> heldPos = new Vec2<>(0f, 0f);
        public Vec2<Float> heldOffset = new Vec2<>(0f, 0f);
        public Matrix heldMatrix = null;

        public void drawHeld(DisplayAdapter d)
        {
            //display.setMatrix(heldThing.heldMatrix);
            if (held != null)
            {
                d.drawArr(
                    held.loc.sprite,
                    heldPos.x - heldOffset.x,
                    heldPos.y - heldOffset.y);
            }
        }

        /**
         * @param thing The thing to hold.
         * @param x     array position of tap
         * @param y     array position of tap
         */
        public void setHeld(Thing thing, float x, float y)
        {
            held = thing;
            heldPos.set(x, y);
            Vec2<Float> pos = held.loc.getWorldArrPos();
            heldOffset.x = x - pos.x;
            heldOffset.y = y - pos.y;
        }

        public void setPos(float x, float y)
        {
            heldPos.set(x, y);
        }

        public void setHeldOffset(float x, float y)
        {
            heldOffset.set(x, y);
        }
    }
}
