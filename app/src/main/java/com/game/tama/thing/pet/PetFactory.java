package com.game.tama.thing.pet;

import com.game.tama.core.AssetName;

public class PetFactory
{
    public static Pet cellPet()
    {
        Pet pet = new Pet();
        pet.setAsset(AssetName.sheet_16_cell_pet);
        return pet;
    }
}
