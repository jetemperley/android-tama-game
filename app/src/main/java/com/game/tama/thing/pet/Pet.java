package com.game.tama.thing.pet;

import com.game.tama.core.Direction;
import com.game.tama.engine.behaviour.GameManager;
import com.game.tama.core.Animator;
import com.game.tama.core.Type;
import com.game.tama.state.StateController;
import com.game.tama.state.Wander;
import com.game.tama.thing.item.Food;
import com.game.tama.thing.Thing;
import com.game.tama.thing.tile.Tile;
import com.game.tama.util.Log;
import com.game.tama.util.Vec2;
import com.game.tama.command.Command;
import com.game.tama.command.CommandAttack;
import com.game.tama.command.CommandFactory;
import com.game.tama.command.CommandReplacer;
import com.game.tama.command.CommandEat;
import com.game.tama.thing.component.Health;
import com.game.tama.core.Assets;
import com.game.tama.core.Sprite;
import com.game.tama.core.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Pet extends Thing
{
    private long time = 0;

    private StateController state;
    final public CommandReplacer currentCommand;

    public Animator anim;

    public enum Movement
    {
        stand, walk
    }

    // Movement moves
    public static Direction[] steps;
    public static Vec2<Integer>[] stepsVec;

    static
    {
        steps = new Direction[]{
            Direction.up,
            Direction.down,
            Direction.left,
            Direction.right};
        stepsVec = Arrays.stream(steps).map(d -> new Vec2<>(d.x, d.y)).collect(
            Collectors.toList()).toArray(new Vec2[]{});
    }

    public int speed = 1;

    Pet()
    {
        super();
        state = new Wander();
        currentCommand = new CommandReplacer();
        asset = Assets.Names.sheet_16_blob.name();
        load();
        addComponent(new Health());
    }

    /**
     * Gets (or creates) the animator associated with this particular pet. This
     * should also be passed to the WorldObject for rendering (currently handled
     * by Thing.load())
     *
     * @return
     */
    @Override
    public Sprite getAsset()
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
        {
            Log.error(
                this,
                "Pet doubled up on an update. Does this matter?");
        }
        currentCommand.getUpdate().invoke(this, world);
        anim.update(this);
        state.update(this, world);
        time = GameManager.time;
    }

    public boolean consume(Thing t)
    {
        // TODO COMPLETE
        return true;
    }

    @Override
    public Type type()
    {
        return Type.pet;
    }

    public void setDir(Direction dir)
    {
        // TODO move the pose and direction calc stuff into those enums
        if (anim == null)
        {
            return;
        }
        int pose = anim.animId / 4;
        anim.animId = 4 * pose + dir.ordinal();
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

    public List<Direction> getPossibleMoves(World world, int x, int y)
    {
        List<Direction> out = new ArrayList<>();
        for (Direction dir : steps)
        {
            if (world.isEmpty(loc.x + dir.x, loc.y + dir.y))
            {
                out.add(dir);
            }
        }
        return out;
    }

    public void setMovementPose(Movement movement)
    {
        if (movement == Movement.stand)
        {
            anim.animId = 4 + (anim.animId % 4);
        }
        else
        {
            anim.animId = anim.animId % 4;
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
            currentCommand.replace(CommandFactory.Companion.commandWalkAndAdjacentAction(
                thing,
                new CommandEat(thing)));
        }
        else
        {
            currentCommand.replace(CommandFactory.Companion.commandWalkAndAdjacentAction(
                thing,
                new CommandAttack(thing)));
        }
    }

    public void setAsset(Assets.Names name)
    {
        asset = name.name();
        load();
    }
}

