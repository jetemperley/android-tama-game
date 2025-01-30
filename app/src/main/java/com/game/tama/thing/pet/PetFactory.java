package com.game.tama.thing.pet;

import com.game.tama.core.Assets;

public class PetFactory
{
    public static Pet cellPet()
    {
        Pet pet = new Pet();
        pet.setAsset(Assets.Names.sheet_16_cell_pet);
        return pet;
    }
}
