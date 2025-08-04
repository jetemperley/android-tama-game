package com.game.tama.core.thing.item;


import com.game.tama.core.AssetName;
import com.game.tama.core.Type;
import com.game.tama.core.thing.Thing;

public class Food extends Thing implements java.io.Serializable
{
    int sust;


    public Food(final AssetName asset)
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
