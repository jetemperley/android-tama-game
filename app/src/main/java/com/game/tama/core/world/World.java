package com.game.tama.core.world;

import com.game.engine.DisplayAdapter;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.thing.tile.DynTile;
import com.game.tama.core.thing.tile.Grass;
import com.game.tama.core.thing.tile.Tile;
import com.game.tama.core.thing.tile.TileType;
import com.game.tama.util.A;
import com.game.tama.util.Log;

import java.io.IOException;

public class World implements java.io.Serializable
{

    private final Tile[][] tiles;
    public int celln;
    float xoff, yoff;

    public World()
    {
        this(10);
    }

    public World(final int size)
    {
        celln = size;
        xoff = 0;
        yoff = 0;

        tiles = new Tile[celln][celln];
        for (int x = 0; x < celln; x++)
        {
            for (int y = 0; y < celln; y++)
            {
                setTile(x, y, new Grass());
            }
        }
    }

    public void update()
    {
        for (int x = 0; x < celln; x++)
        {
            for (int y = 0; y < celln; y++)
            {
                tiles[x][y].update(this);
            }
        }
    }

    public void draw(final DisplayAdapter d)
    {
        for (int x = 0; x < celln; x++)
        {
            for (int y = 0; y < celln; y++)
            {
                tiles[x][y].draw(d);
            }
        }
    }

    /**
     * Adds a thing at position x,y in the array and updates that things
     * position
     *
     * @param t The thing to add
     * @param x The x array pos
     * @param y The y array pos
     * @return Whether the add was successful
     */
    public boolean add(final Thing t, final int x, final int y)
    {
        if (t == null || !A.inRange(tiles, x, y) || !tiles[x][y].isEmpty())
        {
            return false;
        }
        Log.log(this, "setting obj pos to " + x + " " + y);
        tiles[x][y].setThing(t);
        t.loc.setPos(x, y);
        return true;
    }

    public void reLoadAllAssets()
    {
        for (final Tile[] tileLine : tiles)
        {
            for (final Tile tile : tileLine)
            {
                tile.load();
                tile.updateDetails(this);
            }
        }
    }

    /**
     * Pulls the thing out of the world at world array based on the things
     * internal position, and triggers any necessary response in the thing
     *
     * @param thing
     * @return
     */

    public Thing removeThing(final Thing thing)
    {
        if (tiles[thing.loc.x][thing.loc.y].getThing() == thing)
        {
            return tiles[thing.loc.x][thing.loc.y].removeThing();
        }
        return null;
    }

    /**
     * Pulls the thing out of the world at world array at index x, y, and
     * triggering a response in the thing.
     *
     * @param x
     * @param y
     * @return
     */

    public Thing removeThing(final int x, final int y)
    {
        if (A.inRange(tiles, x, y))
        {
            return tiles[x][y].removeThing();
        }
        return null;
    }

    public boolean isEmpty(final int x, final int y)
    {
        if (A.inRange(tiles, x, y))
        {
            return tiles[x][y].isEmpty();
        }
        return false;
    }

    // swaps t with the target xy, or returns t
    public Thing swap(final Thing t, final int x, final int y)
    {
        if (fits(t, x, y))
        {
            final Thing temp = tiles[x][y].removeThing();
            add(t, x, y);
            return temp;
        }
        return t;
    }

    void updateDyn(final int x, final int y)
    {
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                if (A.inRange(tiles, x + i, y + j) && tiles[x + i][y + j] != null)
                {
                    tiles[x + i][y + j].updateDetails(this);
                }
            }
        }
    }

    // bug source
    public void setTile(final int x, final int y, final TileType type)
    {

        if (A.inRange(tiles, x, y))
        {

            switch (type)
            {
                case water:
                    setTile(x, y, new DynTile());
                    break;

                case ground:
                    setTile(x, y, new Grass());
                    break;
            }
            updateDyn(x, y);
        }
    }

    /**
     * Retrieves a tile at position x, y
     *
     * @param x x coord of tile
     * @param y y coord of tile
     * @return the tile object
     */
    public Tile getTile(final int x, final int y)
    {
        if (A.inRange(tiles, x, y))
        {
            return tiles[x][y];
        }
        return null;
    }

    public int width()
    {
        return celln;
    }

    public int height()
    {
        return celln;
    }

    // works if everything only occupies 1 cell
    public boolean fits(final Thing t, final int x, final int y)
    {
        return A.inRange(tiles, x, y);
    }

    public Thing getThing(final int x, final int y)
    {
        if (A.inRange(tiles, x, y))
        {
            return tiles[x][y].getThing();
        }
        return null;
    }

    boolean setTile(final int x, final int y, final Tile t)
    {
        if (t == null || !A.inRange(tiles, x, y))
        {
            return false;
        }

        if (tiles[x][y] != null)
        {
            t.setThing(tiles[x][y].removeThing());
        }
        tiles[x][y] = t;
        t.setPos(x, y);
        updateDyn(x, y);
        return true;
    }

    public Thing checkCollision(final float x, final float y)
    {

        for (int xi = -1; xi < 2; xi++)
        {
            for (int yi = -1; yi < 2; yi++)
            {
                if (A.inRange(tiles, (int) x + xi, (int) y + yi))
                {
                    final Thing t = tiles[(int) x + xi][(int) y + yi].getThing();
                    if (t != null && t.contains(x, y))
                    {
                        return t;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Add a thing at the position x,y or at the closest possible position
     *
     * @param t The thing to add
     * @param x x position in the tiles array
     * @param y y position in the tiles array
     * @return has added successfully
     */
    public boolean addOrClosest(final Thing t, final int x, final int y)
    {
        int dist = 0;
        final int maxDist = tiles.length > tiles[0].length ? tiles.length : tiles[0].length;
        while (dist < maxDist)
        {
            if (addAnywhereOnBorder(t, x, y, dist))
            {
                return true;
            }
            dist++;
        }
        return false;
    }

    /**
     * Adds a thing anywhere on the border of a square with centre x, y and
     * radius
     *
     * @param t      The thing to add
     * @param x      x coord of centre
     * @param y      y coord of centre
     * @param radius radius of the square
     * @return
     */
    private boolean addAnywhereOnBorder(final Thing t, final int x, final int y, final int radius)
    {
        for (int i = x - radius; i <= x + radius; i++)
        {
            if (add(t, i, y + radius) || add(t, i, y - radius))
            {
                return true;
            }
        }
        for (int i = y - radius; i <= y + radius; i++)
        {
            if (add(t, x + radius, i) || add(t, x - radius, i))
            {
                return true;
            }
        }
        return false;
    }

    public Thing pickupThing(final int ax, final int ay)
    {
        final Thing thing = removeThing(ax, ay);
        return thing.pickup();
    }

    public static boolean isAdjacent(final WorldObject a, final WorldObject b)
    {
        final int xDiff = Math.abs(a.x - b.x);
        final int yDiff = Math.abs(a.y - b.y);
        return (xDiff == 1) ^ (yDiff == 1);
    }

    private void readObject(final java.io.ObjectInputStream in) throws IOException,
        ClassNotFoundException
    {
        in.defaultReadObject();
        reLoadAllAssets();
    }
}



