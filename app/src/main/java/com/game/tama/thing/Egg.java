package com.game.tama.thing;

import com.game.tama.core.Animator;
import com.game.tama.core.GameLoop;
import com.game.tama.core.World;
import com.game.tama.util.Rand;
import com.game.tama.core.Assets;
import com.game.tama.core.Sprite;

class Egg extends Thing
{

    Animator anim;
    float age;
    int hatchAge;

    Egg()
    {
        super();
        anim = new Animator(null);
        age = 0;
        hatchAge = 20000;

        asset = Assets.Names.sheet_16_egg.name();
        load();

    }

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

    public void update(World map)
    {
        anim.update(this);
        if (!anim.play && Rand.RandInt(0, 100) < 20 * GameLoop.deltaTime)
        {
            anim.play();
        }
        age += GameLoop.deltaTime;
        if (age > hatchAge)
        {
            // hatch
            map.removeThing(this);
            map.add(new Blob(), loc.x, loc.y);
        }
    }

    public String getDescription()
    {
        return super.getDescription() + "An egg, age " + age + ".";
    }

}
