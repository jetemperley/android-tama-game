package com.game.tama.thing;


import com.tama.R;
import com.game.tama.core.Assets;
import com.game.tama.core.Displayable;
import com.game.tama.core.Type;
import com.game.tama.core.World;

public class Food extends Thing implements java.io.Serializable
{
    int id;
    int sust;
    String name;


    public Food(int ID)
    {
        super();
        id = ID;
        loc.sprite=Assets.getSprite(Assets.Names.static_meat.name());

        switch (id)
        {
            case R.drawable.static_meat:
                name = "meat";
                break;

            case R.drawable.static_leaf:
                name = "leaf";
                break;

            case R.drawable.static_meatbone:
                name = "drumstick";
                break;

            case R.drawable.static_herb:
                name = "herb";
                break;

            case R.drawable.static_poop:
                name = "poop";
                break;

            case R.drawable.static_mushroom:
                name = "mush";
                break;

            case R.drawable.static_apple:
                name = "apple";
                break;

            case R.drawable.static_fish:
                name = "fish";
                break;

            case R.drawable.static_carrot:
                name = "carrot";
                break;

            case R.drawable.static_acorn:
                name = "acorn";
                break;

            case R.drawable.static_cherries:
                name = "cherries";
                break;

            default:
                name = "junk";
                id = 5;
                break;

        }
    }

    Displayable getAsset()
    {
        return Assets.sprites.get(id);
    }

    Type type()
    {
        return Type.food;
    }

    boolean isItem()
    {
        return true;
    }

    public Thing apply(World m, int ax, int ay)
    {

        Thing t = m.getThing(ax, ay);
        if (t == null)
        {
            return m.swap(this, ax, ay);
        }

        switch (t.type())
        {
            case pet:
                Pet p = (Pet) t;
                if (p.consume(this))
                {
                    return null;
                }
                return this;
        }

        return this;
    }
}
