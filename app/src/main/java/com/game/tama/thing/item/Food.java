package com.game.tama.thing.item;


import com.game.tama.core.AssetName;
import com.game.tama.thing.Thing;
import com.game.tama.core.Type;

public class Food extends Thing implements java.io.Serializable
{
    int sust;


    public Food(AssetName asset)
    {
        super();
        this.asset = asset;
        load();
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
