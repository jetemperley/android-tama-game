package com.game.tama.thing.item;

import com.game.tama.core.Assets;
import com.game.tama.thing.Thing;

public class Poop extends Thing
{

    final static int poopTime = 10000;

    public Poop()
    {
        super();
        asset = Assets.Names.static_poop.name();
        load();
    }

    boolean isItem()
    {
        return true;
    }

    public String getDescription()
    {
        return super.getDescription() + "Ew, a poo. And this is a " +
                "super duper  duper duper duper duper duper duper duper" +
                "duper duper duper duper duper duper duper duper duper" +
                " duper duper duper duper duper duper duper duper duper" + "long description";
    }

}
