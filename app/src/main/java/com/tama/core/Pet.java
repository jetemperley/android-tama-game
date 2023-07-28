package com.tama.core;

import com.tama.command.CommandQueue;

public abstract class Pet extends Thing
{

    static final int down = 0, right = 1, up = 2, left = 3;

    State state;
    String name;

    // Movement moves;
    CommandQueue actions;
    // this pet animator is the same object as Displayable.sprite;
    Animator anim;

    int speed = 3;
    Stats stats;

    Pet()
    {
        super();
        state = new Wander();
        name = "";
        stats = new Stats();

        asset = Assets.sheet_16_blob;
        loadAsset();
    }

    Displayable getAsset()
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

    public void update(World map)
    {
        stats.updateStats(this);

        state.update(this);
    }

    public boolean consume(Thing t)
    {
        //COMPLETE
        return true;
    }

    public Type type()
    {
        return Type.pet;
    }

    public void setDir(Direction dir)
    {
        anim.animID = dir.ordinal();
    }

    String getDescription()
    {
        return super.getDescription() + "It's a Pet!";
    }

    public boolean canMoveOnto(Tile tile)
    {
        return tile.isEmpty();
    }

}

class Blob extends Pet
{

    Blob()
    {
        super();
        asset = Assets.sheet_16_blob;
        loadAsset();
    }
}

class Walker extends Pet
{

    Walker()
    {
        super();
        asset = Assets.sheet_16_walker;
        loadAsset();
    }
}

class Egg extends Thing
{

    Animator anim;
    int age;
    int hatchAge;

    Egg()
    {
        super();
        anim = new Animator(null);
        age = 0;
        hatchAge = 20000;

        asset = Assets.sheet_16_egg;
        loadAsset();

    }

    Displayable getAsset()
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

    void update(World map)
    {
        if (!anim.play && Rand.RandInt(0, 100) < 20 * (PetGame.gameSpeed / 1000f))
        {
            anim.play = true;
        }
        age += PetGame.gameSpeed;
        if (age > hatchAge)
        {
            // hatch
            map.removeThing(this);
            map.add(new Blob(), loc.x, loc.y);
        }
    }

    String getDescription()
    {
        return super.getDescription() + "An egg, age " + age + ".";
    }


}

