package com.tama.thing;

import com.tama.core.Assets;

public class Poop extends Thing
{

    final static int poopTime = 10000;

    public Poop()
    {
        super();
        asset = Assets.static_poop;
        loadAsset();
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
