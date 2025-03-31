package com.game.tama.command;

import android.util.Log;
import com.game.engine.DisplayAdapter;
import com.game.tama.core.World;
import com.game.tama.thing.pet.Pet;

public class CommandReplacer extends Command
{
    private Command currentCommand  = null;
    private Command replaceWith = null;

    @Override
    public void start(Pet pet, World world)
    {
        super.start(pet, world);
        update = this::doing;
        doing(pet, world);
    }

    @Override
    public void doing(Pet pet, World world)
    {
        if (replaceWith != null)
        {
            if (currentCommand == null || currentCommand.isReplaceable())
            {
                currentCommand = replaceWith;
                replaceWith = null;
                if (currentCommand != null)
                {
                    currentCommand.start(pet, world);
                    return;
                }
            }
        }

        if (currentCommand == null)
        {
            return;
        }

        if (currentCommand.state == CommandState.complete
            || currentCommand.state == CommandState.failed)
        {
            Log.d(this.getClass().getCanonicalName(), "complete or failed");
            currentCommand = null;
            return;
        }

        currentCommand.update.accept(pet, world);

    }

    public void replace(Command command)
    {
        if (currentCommand == null)
        {
            currentCommand = command;
        }
        else
        {
            replaceWith = command;
        }
    }

    @Override
    public boolean isReplaceable()
    {
        if (currentCommand == null) return true;
        else return currentCommand.isReplaceable();
    }

    @Override
    public void hardCancel()
    {
        super.hardCancel();
        if (currentCommand != null) currentCommand.hardCancel();
        replaceWith = null;
    }

    @Override
    public void draw(DisplayAdapter d)
    {
        if (currentCommand != null) currentCommand.draw(d);
    }

    public boolean hasCommand()
    {
        return currentCommand != null && replaceWith != null;
    }

}