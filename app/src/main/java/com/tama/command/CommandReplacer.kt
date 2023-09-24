package com.tama.command

import com.tama.core.DisplayAdapter
import com.tama.core.World
import com.tama.thing.Pet

class CommandReplacer : Command()
{
    private var currentCommand: Command? = null;
    private var replaceWith: Command? = null;

    override fun start(pet: Pet, world: World)
    {
        super.start(pet, world)
        update = ::doing;
        doing(pet, world);
    }

    override fun doing(pet: Pet, world: World)
    {
        if (replaceWith != null)
        {
            if (currentCommand == null || currentCommand!!.isReplaceable())
            {
                currentCommand = replaceWith;

            }
        }

        if (currentCommand == null)
        {
            return;
        }

        if (currentCommand!!.state == CommandState.complete
            || currentCommand!!.state == CommandState.failed)
        {
            currentCommand = null;
        }

        currentCommand?.update?.invoke(pet, world)

    }

    public fun replace(command: Command)
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

    override fun isReplaceable(): Boolean
    {
        return if (currentCommand == null) true else currentCommand!!.isReplaceable();
    }

    override fun hardCancel()
    {
        super.hardCancel()
        currentCommand?.hardCancel();
        replaceWith = null;
    }

    override fun draw(d: DisplayAdapter)
    {
        currentCommand?.draw(d);
    }

}