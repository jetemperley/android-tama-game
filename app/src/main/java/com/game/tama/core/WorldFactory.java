package com.game.tama.core;

import com.game.tama.thing.DynTile;
import com.game.tama.thing.Food;
import com.game.tama.thing.Grass;
import com.game.tama.thing.Sand;
import com.game.tama.thing.Walker;
import com.tama.R;
import com.game.tama.thing.BackpackSlot;
import com.game.tama.thing.Bush;
import com.game.tama.thing.Tile;
import com.game.tama.thing.Tree;

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
        w.addOrClosest(new Walker(), 0, 0);
        w.addOrClosest(new Food(R.drawable.static_meat), 1, 1);
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
