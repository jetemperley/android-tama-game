package com.game.tama.thing;

import android.util.Log;

import com.game.tama.engine.behaviour.GameManager;
import com.game.tama.core.Animator;
import com.game.tama.core.Stats;
import com.game.tama.core.Type;
import com.game.tama.util.Vec2;
import com.game.tama.command.Command;
import com.game.tama.command.CommandAttack;
import com.game.tama.command.CommandFactory;
import com.game.tama.command.CommandReplacer;
import com.game.tama.command.State;
import com.game.tama.command.Wander;
import com.game.tama.command.CommandEat;
import com.game.tama.component.Health;
import com.game.tama.core.Assets;
import com.game.tama.core.Sprite;
import com.game.tama.core.World;

import java.util.ArrayList;
import java.util.List;

public abstract class Pet extends Thing
{
    public Stats stats;
    private long time = 0;

    State state;
    String name;

    final public CommandReplacer currentCommand;

    /** this pet animator is the same object as Displayable.sprite */
    public Animator anim;

    public enum Movement
    {
        stand, walk
    }

    // Movement moves;
    public static Vec2<Integer>[] steps = new Vec2[]{
            new Vec2<>(0, 1),
            new Vec2<>(0, -1),
            new Vec2<>(1, 0),
            new Vec2<>(-1, 0)};

    public int speed = 1;

    Pet()
    {
        super();
        state = new Wander();
        name = "";
        stats = new Stats();
        currentCommand = new CommandReplacer();
        asset = Assets.Names.sheet_16_blob.name();
        load();
        components.add(new Health(this));
    }

    @Override
    Sprite getAsset()
    {
        if (anim == null)
        {
            anim = new Animator(Assets.getSheet(asset));
            anim.play();
            anim.repeat(true);
        }
        else
        {
            anim.sheet = Assets.getSheet(asset);
        }
        return anim;
    }

    @Override
    public void update(World world)
    {
        if (time == GameManager.time)
            Log.d("PET", "doubled up");
        stats.update();
        if (currentCommand != null)
        {
            currentCommand.getUpdate().invoke(this, world);
        }
        anim.update(this);
        state.update(world, this);
        time = GameManager.time;
    }

    public boolean consume(Thing t)
    {
        //COMPLETE
        return true;
    }

    @Override
    public Type type()
    {
        return Type.pet;
    }

    public void setDir(Direction dir)
    {
        int pose = anim.animId/4;

        anim.animId = 4*pose + dir.ordinal();
    }

    @Override
    public String getDescription()
    {
        return super.getDescription() + "It's a Pet!";
    }

    public boolean canMoveOnto(Tile tile)
    {
        if (tile == null)
        {
            return false;
        }
        return tile.isEmpty();
    }

    public List<Vec2<Integer>> getPossibleMoves(World world, int x, int y)
    {
        List<Vec2<Integer>> out = new ArrayList<>();
        for (Vec2<Integer> step : steps)
        {
            if (world.isEmpty(loc.x + step.x, loc.y + step.y))
            {
                out.add(step);
            }
        }
        return out;
    }

    public void setMovementPose(Movement movement)
    {
        if (movement == Movement.stand)
        {
            anim.animId = 4 + (anim.animId %4);
        }
        else
        {
            anim.animId = anim.animId %4;
        }
    }

    public Command getActionForTarget(Thing thing)
    {
        if (thing instanceof Food)
        {
            return new CommandEat(thing);
        }
        return null;
    }

    @Override
    public Thing pickup()
    {
        // currentCommand.hardCancel();
        return this;
    }

    public void setActionTarget(Thing thing)
    {
        if (thing.type() == Type.food)
        {
            currentCommand.replace(CommandFactory.Companion.commandWalkAndAdjacentAction(thing, new CommandEat(thing)));
        }
        else
        {
            currentCommand.replace(CommandFactory.Companion.commandWalkAndAdjacentAction(thing, new CommandAttack(thing)));
        }
    }
}

