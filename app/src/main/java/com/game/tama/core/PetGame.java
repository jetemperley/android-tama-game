package com.game.tama.core;

import android.graphics.Matrix;

import com.game.android.DepthDisplay;
import com.game.android.DisplayAdapter;
import com.game.android.gesture.Input;
import com.game.tama.behaviour.PetGameBehaviour;
import com.game.tama.command.CommandFactory;
import com.game.tama.command.CommandQueue;
import com.game.android.gesture.GestureEvent;
import com.game.android.gesture.GestureEventHandler;
import com.game.tama.thing.Pet;
import com.game.tama.thing.Thing;
import com.game.tama.ui.ContainerManager;
import com.game.tama.util.Log;
import com.game.tama.util.MatrixUtil;
import com.game.tama.util.Vec2;

public class PetGame implements java.io.Serializable, Input,
                                GestureEventHandler
{
    public Thing held = null;
    public Matrix worldMat;
    /** The amount of ms this game has been running for. */
    public static long time = 0;
    public ContainerManager containerManager = new ContainerManager();

    boolean showBackpack = true;

    private World world;
    private Thing selected = null;
    private Vec2<Float> heldPos = new Vec2<>(0f, 0f);
    private Vec2<Float> heldOffset = new Vec2<>(0f, 0f);
    private DepthDisplay depthDisplay = new DepthDisplay();
    private Container backpackSlot;
    private Matrix heldMatrix;
    private Matrix identity = new Matrix();
    private PetGameBehaviour behaviour;

    public PetGame(PetGameBehaviour parent)
    {
        behaviour = parent;
        world = WorldFactory.makeWorld();
        world.addOrClosest(new Container(this, 2), 2, 2);

        worldMat = new Matrix();

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
        time += GameLoop.deltaTime;
    }

    public void draw(DisplayAdapter display)
    {
        display.setMatrix(worldMat);
        depthDisplay.display = display;
        world.draw(depthDisplay);
        depthDisplay.drawQ();
        depthDisplay.clearQ();

        display.setMatrix(identity);
        if (showBackpack)
        {
            //backpackSlot.draw(display);
        }

        display.setMatrix(heldMatrix);
        drawSelected(display);
    }

    public void drawSelected(DisplayAdapter d)
    {
        if (selected != null)
        {
            d.displayArr(
                Assets.getSprite(Assets.Names.static_inv.name()),
                selected.loc.getWorldArrPos().x,
                selected.loc.getWorldArrPos().y);
            if (selected instanceof Pet)
            {
                ((Pet) selected).currentCommand.draw(d);
            }
        }
        if (held != null)
        {
            d.displayArr(
                held.loc.sprite,
                heldPos.x - heldOffset.x,
                heldPos.y - heldOffset.y);
        }
    }

    public void reLoadAllAssets()
    {
        world.reLoadAllAssets();
        if (held != null)
        {
            held.load();
        }
    }

    public void setHeldPosition(float x, float y)
    {
        heldPos.set(x, y);
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
        setHeld(thing, ax, ay);
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
        Log.log(this, "params " + x + " " + y);
        Log.log(this, "pos " + held.loc.x + " " + held.loc.y);
        heldOffset.x = x - pos.x;
        heldOffset.y = y - pos.y;
    }

    public float[] transferFromContainer(Thing t, Matrix containerMat)
    {
        Vec2<Float> worldPos = t.loc.getWorldBitPos();
        Log.log(
            this,
            "transferFromContainer object loc is " + worldPos.x + " " +
                worldPos.y);

        float[] f = new float[]{
            worldPos.x,
            worldPos.y};
        containerMat.mapPoints(f);
        Log.log(
            this,
            "transferFromContainer screen loc is " + f[0] + " " + f[1]);

        Matrix inv = new Matrix();
        worldMat.invert(inv);
        inv.mapPoints(f);
        Log.log(
            this,
            "transferFromContainer world loc is " + f[0] + " " + f[1]);

        t.loc.x = (int) f[0];
        t.loc.y = (int) f[1];
        setHeld(t, f[0], f[1]);
        return f;
    }

    public void setSelected(int x, int y)
    {
        selected = world.getThing(x, y);
    }

    public void pickup(float x, float y)
    {
        if (held != null)
        {
            heldPos.set(x, y);
            return;
        }
        Thing t = world.checkCollision(x, y);
        if (t != null)
        {
            held = world.pickupThing(t.loc.x, t.loc.y);
            heldPos.set(x, y);
            Vec2<Float> pos = held.loc.getWorldArrPos();
            heldOffset.x = x - pos.x;
            heldOffset.y = y - pos.y;
        }
    }

    void drop(float x, float y)
    {
        Log.log(this, "dropping");
        world.addOrClosest(held, (int) x, (int) y);
        held = null;
    }

    public void select(float x, float y)
    {
        Thing t = world.checkCollision(x, y);
        if (selected == t)
        {
            selected = null;
        }
        else
        {
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
        if (t == null)
        {
            return;
        }
        t.apply(world, t.loc.x, t.loc.y);
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

        float[] f = MatrixUtil.convertScreenToWorldArray(worldMat, x, y);
        Log.log(this, "tapped " + f[0] + " " + f[1]);
        select(f[0], f[1]);
        poke(f[0], f[1]);
    }

    @Override
    public void singleDown(float x, float y)
    {

    }

    @Override
    public void longPressConfirmed(float x, float y)
    {
        float[] f = MatrixUtil.convertScreenToWorldArray(worldMat, x, y);
        use(f[0], f[1]);
    }

    @Override
    public void doubleTapConfirmed(float x, float y)
    {
        Log.log(this, "double tap confirmed");
        float[] f = MatrixUtil.convertScreenToWorldArray(
            worldMat,
            x,
            y);
        doubleSelect(f[0], f[1]);
    }

    @Override
    public void doubleTapDragStart(float startX,
                                   float startY,
                                   float currentX,
                                   float currentY)
    {
        float[] f =
            MatrixUtil.convertScreenToWorldArray(worldMat, startX, startY);
        setHeld(f[0], f[1]);
    }

    @Override
    public void doubleTapDrag(float prevX,
                              float prevY,
                              float nextX,
                              float nextY)
    {
        //        if (backpackSlot.isTouchInside(nextX, nextY))
        //        {
        //            heldMatrix = backpackSlot.mat;
        //            float[] f =
        //                MatrixUtil.convertScreenToWorldArray(heldMatrix,
        //                nextX, nextY);
        //            setHeldPosition(f[0], f[1]);
        //        }
        //        else
        //        {
        heldMatrix = worldMat;
        float[] f =
            MatrixUtil.convertScreenToWorldArray(worldMat, nextX, nextY);
        setHeldPosition(f[0], f[1]);
        //        }
    }

    @Override
    public void doubleTapRelease(float x, float y)
    {
    }

    @Override
    public void doubleTapDragEnd(float x, float y)
    {
        float[] f = MatrixUtil.convertScreenToWorldArray(worldMat, x, y);
        drop(f[0], f[1]);
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
        worldMat.postTranslate(-nmid.x, -nmid.y);
        worldMat.postScale(scale, scale);
        worldMat.postTranslate(nmid.x, nmid.y);
        worldMat.postTranslate(
            nmid.x - pmid.x,
            nmid.y - pmid.y);
    }

    @Override
    public void scroll(Vec2<Float> prev, Vec2<Float> next)
    {
        worldMat.postTranslate(
            next.x - prev.x,
            next.y - prev.y);
    }

    @Override
    public boolean handleEvent(GestureEvent e)
    {
        for (int i = containerManager.containers.size() - 1; i >= 0; i--)
        {
            if (containerManager.containers.get(i).handleEvent(e))
            {
                return true;
            }
        }
        e.callEvent(this);
        return true;
    }

    public void toggle(Container container) {}
}
