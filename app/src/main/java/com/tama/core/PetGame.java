package com.tama.core;

import android.graphics.Matrix;

import com.tama.command.CommandFactory;
import com.tama.command.CommandQueue;
import com.tama.gesture.GestureEvent;
import com.tama.thing.Pet;
import com.tama.thing.Thing;
import com.tama.util.Log;
import com.tama.util.MatrixUtil;
import com.tama.util.Vec2;

public class PetGame extends Interactive implements java.io.Serializable
{
    private World world = WorldFactory.makeWorld();
    public Thing held = null;
    private Thing selected = null;
    private Vec2<Float> heldPos = new Vec2<>(0f, 0f);
    private Vec2<Float> heldOffset = new Vec2<>(0f, 0f);

    private DepthDisplay depthDisplay = new DepthDisplay();
    private Matrix worldMat = new Matrix();

    private ButtonManager buttonManager;
    private Container backpack = new Container(this, worldMat, 2);

    /** The amount of ms this game has been running for. */
    public static long time = 0;

    boolean showBackpack = true;

    public PetGame()
    {
        Matrix mat = new Matrix();
        mat.setScale(6, 6);
        buttonManager = new ButtonManager(mat);
        buttonManager.add(new Button(0, 0, Assets.Names.static_menu.name())
        {
            @Override
            void activate()
            {
                GameManager.INST.pause();
            }
        });

        buttonManager.add(new Button(16, 0, Assets.Names.static_backpack.name())
        {

            @Override
            void activate()
            {
                showBackpack = !showBackpack;
            }
        });
    }

    public void update()
    {
        backpack.worldMat = worldMat;
        world.update();
        backpack.update();
        time += GameLoop.deltaTime;
    }

    public void draw(DisplayAdapter display)
    {
        display.setMatrix(worldMat);
        depthDisplay.display = display;
        drawEnv(depthDisplay);
        depthDisplay.drawQ();
        depthDisplay.clearQ();

        if (showBackpack)
        {
            backpack.draw(display);
        }

        drawSelected(display);
        buttonManager.draw(display);
    }

    void drawEnv(DisplayAdapter d)
    {
        world.draw(d);
    }

    void drawSelected(DisplayAdapter d)
    {
        if (selected != null)
        {
            d.display(
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
            held.load();
        }
        buttonManager.loadAssets();
    }

    void setHeldPosition(float x, float y)
    {
        heldPos.set(x, y);
    }

    /**
     * @param ax array tap
     * @param ay array tap
     */
    void setHeld(float ax, float ay)
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
        Log.log(this, "object loc is " + worldPos.x + " " + worldPos.y);

        float[] f = new float[]{
            worldPos.x,
            worldPos.y};
        containerMat.mapPoints(f);
        Log.log(this, "screen loc is " + f[0] + " " + f[1]);

        Matrix inv = new Matrix();
        worldMat.invert(inv);
        inv.mapPoints(f);
        Log.log(this, "world loc is " + f[0] + " " + f[1]);

        t.loc.x = (int) f[0];
        t.loc.y = (int) f[1];
        setHeld(t, f[0], f[1]);
        return f;
    }

    void setSelected(int x, int y)
    {
        selected = world.getThing(x, y);
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

    void use(float x, float y)
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
    public void longPressConfirmed(float x, float y)
    {
        float[] f = MatrixUtil.convertScreenToWorldArray(worldMat, x, y);
        use(f[0], f[1]);
    }

    @Override
    public void doubleTapConfirmed(float x, float y)
    {
        Log.log(this, "double tap confirmed");
        super.doubleTapConfirmed(x, y);
        float[] f = MatrixUtil.convertScreenToWorldArray(
            worldMat,
            x,
            y);
        //gameActivity.game.pickup(f[0], f[1]);
        doubleSelect(f[0], f[1]);
    }

    @Override
    public void doubleTapDragStart(float startX, float startY, float currentX, float currentY)
    {
        float[] f =
            MatrixUtil.convertScreenToWorldArray(worldMat, startX, startY);
        setHeld(f[0], f[1]);
    }

    @Override
    public void doubleTapDrag(float prevX, float prevY, float nextX, float nextY)
    {
        float[] f =
            MatrixUtil.convertScreenToWorldArray(worldMat, nextX, nextY);
        setHeldPosition(f[0], f[1]);
    }

    @Override
    public void doubleTapRelease(float x, float y)
    {
        float[] f = MatrixUtil.convertScreenToWorldArray(worldMat, x, y);
        // drop(f[0], f[1]);
    }

    @Override
    public void doubleTapDragEnd(float x, float y)
    {
        float[] f = MatrixUtil.convertScreenToWorldArray(worldMat, x, y);
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

    @Override
    public boolean handleEvent(GestureEvent e)
    {
        if (buttonManager.handleEvent(e))
        {
            return true;
        }
        if (showBackpack && backpack.handleEvent(e))
        {
            return true;
        }
        e.callEvent(this);
        return true;
    }
}
