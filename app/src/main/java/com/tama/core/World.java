package com.tama.core;

import android.util.Log;

public class World implements java.io.Serializable
{

    private Tile[][] tiles;
    int celln, openSpace;
    float xoff, yoff;

    public World()
    {
        this(10);
    }

    public World(int size)
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
        openSpace = 0;
        for (int x = 0; x < celln; x++)
        {
            for (int y = 0; y < celln; y++)
            {
                tiles[x][y].update(this);
            }
        }

    }

    public void display(DisplayAdapter d)
    {
        for (int x = 0; x < celln; x++)
        {
            for (int y = 0; y < celln; y++)
            {
                tiles[x][y].display(d);
            }
        }

    }

    /**
     * Adds a thing at position x,y in the array and updates that things position
     *
     * @param t The thing to add
     * @param x The x array pos
     * @param y The y array pos
     * @return Whether the add was successful
     */
    public boolean add(Thing t, int x, int y)
    {
        if (t == null || !A.inRange(tiles, x, y) || !tiles[x][y].isEmpty())
        {
            return false;
        }
        tiles[x][y].setThing(t);
        t.loc.setPos(x, y);
        return true;
    }

    public void reLoadAllAssets()
    {
        for (int x = 0; x < tiles.length; x++)
        {
            for (int y = 0; y < tiles[x].length; y++)
            {
                tiles[x][y].reLoadAsset();
                tiles[x][y].updateDetails(this);
            }
        }
    }

    public void removeThing(Thing t)
    {
        if (tiles[t.loc.x][t.loc.y].getThing() == t)
        {
            tiles[t.loc.x][t.loc.y].takeThing();
        }
    }

    public void removeThing(int x, int y)
    {
        if (A.inRange(tiles, x, y))
        {
            tiles[x][y].takeThing();
        }
    }


    // param: px, py is the thing stepping, x,y is the spot to check
    // return: if the thing can be on tile x, y
    // TODO refactor this
    boolean canStepOnto(int px, int py, int x, int y)
    {
        // Log.d("Map", "" + px + " " + py);
        Thing t = tiles[px][py].getThing();
        if (isEmpty(x, y) && ((tiles[x][y].type() == TileType.ground == t.canWalk()) ||
                (tiles[x][y].type() == TileType.water == t.canSwim())))
        {
            return true;
        }
        return false;
    }

    public boolean isEmpty(int x, int y)
    {
        if (A.inRange(tiles, x, y))
        {
            return tiles[x][y].isEmpty();
        }
        return false;
    }

    public Thing takeThing(int x, int y)
    {
        if (A.inRange(tiles, x, y))
        {
            return tiles[x][y].takeThing();
        }
        return null;
    }


    // swaps t with the target xy, or returns t
    public Thing swap(Thing t, int x, int y)
    {
        if (fits(t, x, y))
        {
            Thing temp = tiles[x][y].takeThing();
            add(t, x, y);
            return temp;
        }
        return t;
    }


    void updateDyn(int x, int y)
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
    void setTile(int x, int y, TileType type)
    {

        if (A.inRange(tiles, x, y))
        {

            switch (type)
            {
                case water:
                    Log.d("map", "set water");
                    setTile(x, y, new DynTile());
                    break;

                case ground:
                    Log.d("map", "set ground");
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
    public Tile getTile(int x, int y)
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
    public boolean fits(Thing t, int x, int y)
    {
        return A.inRange(tiles, x, y);
    }

    public Thing getThing(int x, int y)
    {
        if (A.inRange(tiles, x, y))
        {
            return tiles[x][y].getThing();
        }
        return null;
    }

    boolean setTile(int x, int y, Tile t)
    {
        if (t == null || !A.inRange(tiles, x, y))
        {
            return false;
        }

        if (tiles[x][y] != null)
        {
            t.setThing(tiles[x][y].takeThing());
        }
        tiles[x][y] = t;
        t.setPos(x, y);
        updateDyn(x, y);
        return true;

    }

    public Thing checkCollision(float x, float y)
    {

        for (int xi = -1; xi < 2; xi++)
        {
            for (int yi = -1; yi < 2; yi++)
            {
                if (A.inRange(tiles, (int) x + xi, (int) y + yi))
                {
                    Thing t = tiles[(int) x + xi][(int) y + yi].getThing();
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
     * @return added successfully
     */
    public boolean addOrClosest(Thing t, int x, int y)
    {
        int dist = 0;
        int maxDist = tiles.length > tiles[0].length ? tiles.length : tiles[0].length;
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
     * Adds a thing anywhere on the border of a square with centre x, y and radius
     *
     * @param t      The thing to add
     * @param x      x coord of centre
     * @param y      y coord of centre
     * @param radius radius of the square
     * @return
     */
    private boolean addAnywhereOnBorder(Thing t, int x, int y, int radius)
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
}



