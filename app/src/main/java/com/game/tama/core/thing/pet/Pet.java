package com.game.tama.core.thing.pet;

import com.game.engine.Sprite;
import com.game.engine.Time;
import com.game.tama.command.Command;
import com.game.tama.command.CommandAttack;
import com.game.tama.command.CommandEat;
import com.game.tama.command.CommandEmote;
import com.game.tama.command.CommandFactory;
import com.game.tama.command.CommandReplacer;
import com.game.tama.core.Animator;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Direction;
import com.game.tama.core.Type;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.thing.ThingControl;
import com.game.tama.core.thing.component.Health;
import com.game.tama.core.thing.item.Food;
import com.game.tama.core.thing.tile.Tile;
import com.game.tama.core.world.World;
import com.game.tama.state.StateController;
import com.game.tama.state.Wander;
import com.game.tama.util.Log;
import com.game.tama.util.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Pet extends Thing
{
    private long time = 0;

    private final StateController state;
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
            Direction.right
        };
        stepsVec = Arrays.stream(steps).map(d -> new Vec2<>(d.x, d.y)).collect(
            Collectors.toList()).toArray(new Vec2[]{});
    }

    public int speed = 1;

    Pet()
    {
        super();
        state = new Wander();
        currentCommand = new CommandReplacer();
        asset = AssetName.sheet_16_blob;
        load();
        addComponent(new Health());
        addControl(ThingControl.Name.move);
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
            anim = new Animator(Asset.sheets.get(asset));
            anim.play();
            anim.repeat = true;
        }
        else
        {
            anim.sheet = Asset.sheets.get(asset);
        }
        return anim;
    }

    @Override
    public void update(final World world)
    {
        //        Log.log(this, "Pet update time " + Time.time());
        if (time == Time.time())
        {
            Log.error(
                this,
                "Pet doubled up on an update. Does this matter?");
        }
        currentCommand.update.update(this, world);
        anim.update();
        state.update(this, world);
        time = Time.time();
    }

    public boolean consume(final Thing t)
    {
        // TODO COMPLETE
        return true;
    }

    @Override
    public Type type()
    {
        return Type.pet;
    }

    public void look(final Direction dir)
    {
        // TODO move the pose and direction calc stuff into those enums
        if (anim == null)
        {
            return;
        }
        final int pose = anim.animId / 4;
        anim.animId = 4 * pose + dir.ordinal();
    }

    @Override
    public String getDescription()
    {
        return super.getDescription() + "It's a Pet!";
    }

    public boolean canMoveOnto(final Tile tile)
    {
        if (tile == null)
        {
            return false;
        }
        return tile.isEmpty();
    }

    public List<Direction> getPossibleMoves(final World world, final int x, final int y)
    {
        final List<Direction> out = new ArrayList<>();
        for (final Direction dir : steps)
        {
            if (world.isEmpty(loc.x + dir.x, loc.y + dir.y))
            {
                out.add(dir);
            }
        }
        return out;
    }

    public void setMovementPose(final Movement movement)
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

    public Command getActionForTarget(final Thing thing)
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

    public void setActionTarget(final Thing thing)
    {
        if (thing.type() == Type.food)
        {
            currentCommand.replace(CommandFactory.commandWalkAndAdjacentAction(
                thing,
                new CommandEat(thing)));
        }
        else
        {
            currentCommand.replace(CommandFactory.commandWalkAndAdjacentAction(
                thing,
                new CommandAttack(thing)));
        }
    }

    public void setAsset(final AssetName asset)
    {
        this.asset = asset;
        load();
    }

    @Override
    public void stroke()
    {
        if (currentCommand.isCommand(CommandEmote.class))
        {
            return;
        }
        currentCommand.replace(new CommandEmote());
    }
}

