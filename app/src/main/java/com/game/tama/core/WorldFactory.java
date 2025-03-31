package com.game.tama.core;

import com.game.tama.thing.pet.PetFactory;
import com.game.tama.thing.tile.DynTile;
import com.game.tama.thing.item.Food;
import com.game.tama.thing.tile.Grass;
import com.game.tama.thing.tile.Sand;
import com.game.tama.thing.pet.Walker;
import com.tama.R;
import com.game.tama.thing.tile.BackpackSlot;
import com.game.tama.thing.item.Bush;
import com.game.tama.thing.tile.Tile;
import com.game.tama.thing.item.Tree;

public class WorldFactory
{

    public static World makeTestWorld()
    {
        World w = new World(15);
        populateTestWorld(w);
        return w;
    }

    public static void populateTestWorld(World w)
    {
        Noise n = new Noise((int) (Math.random() * 100000), 5, 0.05f);

        for (int x = 0; x < w.width(); x++)
        {
            for (int y = 0; y < w.height(); y++)
            {

                float r = n.getNoise(x, y);
                Tile tt;

                if (r < 0.2)
                {
                    tt = new DynTile();
                }
                else if (r < 0.3)
                {
                    tt = new Sand();
                }
                else
                {
                    tt = new Grass();
                }

                w.setTile(x, y, tt);

                if (r > 0.95)
                {
                    w.add(new Tree(Tree.GrowthLevel.large_3), x, y);
                }
                else if (r > 0.92)
                {
                    w.add(new Bush(), x, y);
                }

            }
        }
        w.addOrClosest(PetFactory.cellPet(), 0, 0);
        // w.addOrClosest(PetFactory.cellPet(), 0, 0);
        // w.addOrClosest(PetFactory.cellPet(), 0, 0);
        // w.addOrClosest(PetFactory.cellPet(), 0, 0);
        w.addOrClosest(new Food(AssetName.static_meat), 1, 1);
        w.addOrClosest(new Container(2), 2, 2);

    }

    public static World makeBackpack(int xSize, int ySize)
    {
        World world = new World(xSize);

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                world.setTile(x, y, new BackpackSlot());
            }
        }
        return world;
    }
}
