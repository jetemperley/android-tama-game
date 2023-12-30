package com.tama.core;

import android.graphics.Matrix;
import android.view.MotionEvent;

import com.tama.command.CommandFactory;
import com.tama.command.CommandQueue;
import com.tama.thing.Pet;
import com.tama.thing.Thing;
import com.tama.util.Log;
import com.tama.util.Vec2;

public class PetGame extends ScreenTarget implements java.io.Serializable
{
    private World world = WorldFactory.makeWorld();
    private Thing held = null;
    private Thing selected = null;
    private Vec2<Float> heldPos = new Vec2<>(0f, 0f);
    private Vec2<Float> heldOffset = new Vec2<>(0f, 0f);

    private DepthDisplay depthDisplay = new DepthDisplay();
    private Matrix worldMat = new Matrix();

    /**
     * The amount of ms this game has been running for
     */
    public static long time = 0;
    /**
     * The time that this game should aim to run at (ms)
     */
    public final static int gameSpeed = 25;

    public void update()
    {
        world.update();
        time += gameSpeed;
    }

    public void draw(DisplayAdapter display)
    {
        display.setMatrix(worldMat);
        depthDisplay.display = display;
        drawEnv(depthDisplay);
        depthDisplay.drawQ();
        depthDisplay.clearQ();
        drawUI(display);
    }

    void drawEnv(DisplayAdapter d)
    {

        world.display(d);
    }

    void drawUI(DisplayAdapter d)
    {
        if (selected != null)
        {
            d.display(
                Assets.getSprite(Assets.static_inv),
                selected.loc.getWorldPos().x,
                selected.loc.getWorldPos().y);
            if (selected instanceof Pet)
            {
                ((Pet) selected).currentCommand.draw(d);
            }
        }
        if (held != null)
        {
            d.display(
                held.loc.sprite,
                heldPos.x - heldOffset.x,
                heldPos.y - heldOffset.y);
        }
    }

    void reLoadAllAssets()
    {
        world.reLoadAllAssets();
        if (held != null)
        {
            held.loadAsset();
        }
    }

    void setHeldPosition(float x, float y)
    {
        heldPos.set(x, y);
    }

    void setHeld(float ax, float ay)
    {
        Thing thing = world.checkCollision(ax, ay);
        if (thing == null)
        {
            return;
        }
        thing = world.pickupThing(thing.loc.x, thing.loc.y);
        setHeld(thing);
    }

    void setHeld(Thing thing)
    {
        held = thing;
    }

    void setSelected(int x, int y)
    {
        selected = world.getThing(x, y);
    }

    void setSelectedAsHeld()
    {
        if (selected != null)
        {
            setHeld(selected.loc.x, selected.loc.y);
        }
        else
        {
            held = null;
        }
    }

    void pickup(float x, float y)
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
            Vec2<Float> pos = held.loc.getWorldPos();
            heldOffset.x = x - pos.x;
            heldOffset.y = y - pos.y;
        }
    }

    void drop(float x, float y)
    {
        world.addOrClosest(held, (int) x, (int) y);
        held = null;
    }

    void select(float x, float y)
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
    void poke(float x, float y)
    {
        Thing t = world.checkCollision(x, y);
        if (t != null)
        {
            t.poke();
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
        if (selected == null)
        {
            setHeld(ax, ay);
            return;
        }

        if (!(selected instanceof Pet))
        {
            return;
        }

        Pet pet = (Pet) selected;
        Thing thing = world.checkCollision(ax, ay);
        if (thing == pet)
        {
            pickup(ax, ay);
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
        float[] f = convertScreenToWorld(x, y);
        Log.log(this, "tapped " + f[0] + " " + f[1]);
        select(f[0], f[1]);
        poke(f[0], f[1]);
    }

    float[] convertScreenToWorld(float x, float y)
    {
        float[] f2 = new float[9];
        worldMat.getValues(f2);
        float[] f = {
            (x - f2[2]) / 16,
            (y - f2[5] - GameActivity.TOP_OFFSET) / 16};

        Matrix inv = new Matrix();
        worldMat.invert(inv);
        inv.mapVectors(f);
        return f;
    }

    @Override
    public void longPressConfirmed(float x, float y)
    {
        // float[] f = gameActivity.displayAdapter.convertScreenToWorld(x, y);
        // gameActivity.game.pickup(x, y);
    }

    @Override
    public void doubleTapConfirmed(MotionEvent e)
    {
        super.doubleTapConfirmed(e);
        float[] f = convertScreenToWorld(
            e.getX(),
            e.getY());
        //gameActivity.game.pickup(f[0], f[1]);
        doubleSelect(f[0], f[1]);
    }

    @Override
    public void doubleTapRelease(float x, float y)
    {
        float[] f = convertScreenToWorld(x, y);
        drop(f[0], f[1]);
    }

    @Override
    public void doubleTapDragStart(MotionEvent e)
    {

    }

    @Override
    public void doubleTapDrag(float x, float y)
    {
        Log.log(this, "double tap drag");
        float[] f = convertScreenToWorld(x, y);
        setHeldPosition(f[0], f[1]);
    }

    @Override
    public void doubleTapDragEnd(float x, float y)
    {
        float[] f = convertScreenToWorld(x, y);
        drop(f[0], f[1]);
    }

    @Override
    public void scale(Vec2<Float> p1, Vec2<Float> p2, Vec2<Float> n1, Vec2<Float> n2)
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
}
