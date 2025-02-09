package com.game.tama.thing.item;


import com.game.tama.core.AssetName;
import com.game.tama.thing.Thing;
import com.tama.R;
import com.game.android.Assets;
import com.game.tama.core.Sprite;
import com.game.tama.core.Type;

public class Food extends Thing implements java.io.Serializable
{
    int id;
    int sust;


    public Food(int ID)
    {
        super();
        id = ID;
        loc.sprite=Assets.getStaticSprite(AssetName.static_meat);

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

    @Override
    public Sprite getAsset()
    {
        return Assets.sprites.get(id);
    }

    @Override
    public Type type()
    {
        return Type.food;
    }

    @Override
    public void use()
    {

//        Thing t = m.getThing(ax, ay);
//        if (t == null)
//        {
//            return m.swap(this, ax, ay);
//        }
//
//        switch (t.type())
//        {
//            case pet:
//                Pet p = (Pet) t;
//                if (p.consume(this))
//                {
//                    return null;
//                }
//                return this;
//        }
    }
}
