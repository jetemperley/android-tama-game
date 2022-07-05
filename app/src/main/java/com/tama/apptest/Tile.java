package com.tama.apptest;

abstract class Tile implements java.io.Serializable {

    WorldObject loc;
    Displayable vis;

    boolean visible;
    private Thing thing;

    Tile(){
        loc = new WorldObject();
        visible = true;
        loc.flat = true;
        vis = getDisp();

    }

    protected Displayable getDisp(){
        return new SpriteSheet("sheet_16_terrain");
    }

    void display(DisplayAdapter d) {
        if (visible) {
            d.displayWorld(loc, vis);
        }
        if (thing != null){
            thing.display(d);
        }

    }


    void update(World m) {
        if (thing != null){
            thing.update(m);
        }

    }

    void updateDetails(World m) {


    }

    void loadAsset(){
        vis.loadAsset();
        if (thing != null)
            thing.loadAsset();
    }

    public TileType type() {
        return TileType.ground;
    }

    boolean isEmpty(){
        return thing == null;
    }

    void setThing(Thing t){
        thing = t;
        if (t != null)
            t.wo.setPos(loc.x, loc.y);
    }

    final Thing takeThing(){
        if (thing == null)
            return null;
        Thing t = thing.take();
        thing = thing.leaveBehind();
        return t;
    }

    final Thing getThing(){
        return thing;
    }

    void setPos(int x, int y){
        loc.setPos(x, y);
    }

    final void deleteThing(){
        thing = null;
    }

}

class Grass extends Tile {


    public TileType type() {
        return TileType.grass;
    }

}

class Bush extends Tile{


}

class LongGrass extends Tile{

    WorldObject loc0;
    WorldObject loc1;

    LongGrass(){

        super();
        loc.flat = false;
        loc.yoff = 1;
        loc0 = new WorldObject();
        loc0.yoff = -30;
        loc0.flat = false;
        loc1 = new WorldObject();
        loc1.yoff = -60;
        loc1.flat = false;
    }

    @Override
    public Displayable getDisp(){
        return new StaticSprite("static_longgrass");
    }

    void setPos(int x, int y){
        loc.setPos(x, y);
        loc0.setPos(x, y);
        loc1.setPos(x, y);
    }

    @Override
    void display(DisplayAdapter d){

        super.display(d);
        d.displayWorld(loc0, vis);
        d.displayWorld(loc1, vis);
    }

}

class DynTile extends Tile {

    // considers the surrounding tiles to create a dynamic tile graphic
    WorldObject[][] parts;
    SpriteSheet[][] sprites;

    // img is 4 px sq 4*3 sprite sheet of possible configurations
    DynTile() {
        super();

        parts = new WorldObject[2][2];
        sprites = new SpriteSheet[2][2];

        for (int a = 0; a < parts.length; a++) {
            for (int b = 0; b < parts[a].length; b++) {
                sprites[a][b] = new SpriteSheet("sheet_8_watersimp");
                parts[a][b] = new WorldObject();
                parts[a][b].xoff = a*50;
                parts[a][b].yoff = b*50;
            }
        }


    }

    @Override
    void loadAsset(){
        for (int x = 0; x < sprites.length; x++){
            for (int y = 0; y < sprites[x].length; y++){
                sprites[x][y].loadAsset();
            }
        }
    }

    public TileType type(){
        return TileType.water;
    }

    void display(DisplayAdapter d) {
        for (int a = 0; a < parts.length; a++) {
            for (int b = 0; b < parts[a].length; b++) {

                d.displayWorld(parts[a][b], sprites[a][b]);

            }
        }
    }

    void setPos(int x, int y){
        loc.setPos(x, y);
        for (int a = 0; a < parts.length; a++){
            for (int b = 0; b < parts[a].length; b++){
                parts[a][b].setPos(x, y);
            }
        }
    }

    @Override
    void updateDetails(World m) {
        setTL(m);
        setTR(m);
        setBL(m);
        setBR(m);
    }

    private void setTL(World m) {

        sprites[0][0].setSprite(2, 3);
        Tile t = m.getTile(loc.x - 1, loc.y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile( loc.x, loc.y-1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x - 1, loc.y -1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            sprites[0][0].setSprite(0, 0);
            if (u) {
                sprites[0][0].setSprite(1, 3);

                if (ul) {
                    sprites[0][0].x = -1;
                }
                return;
            }
            return;
        }
        if (u) {
            sprites[0][0].setSprite(0, 3);
        }
    }

    private void setTR(World m) {

        sprites[1][0].setSprite(2, 0);
        Tile t = m.getTile(loc.x + 1, loc.y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x , loc.y-1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x + 1, loc.y - 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            sprites[1][0].setSprite(0, 0);
            if (u) {
                sprites[1][0].setSprite(1, 0);

                if (ul) {
                    sprites[1][0].x = -1;
                }
                return;
            }
            return;
        }
        if (u) {
            sprites[1][0].setSprite(0, 1);
        }
    }

    private void setBL(World m) {

        sprites[0][1].setSprite(2, 2);
        Tile t = m.getTile(loc.x - 1, loc.y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x, loc.y + 1);
        boolean u =  t != null && t.type() == TileType.water;
        t = m.getTile(loc.x - 1, loc.y + 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            sprites[0][1].setSprite(0, 2);
            if (u) {
                sprites[0][1].setSprite(1, 2);
                if (ul) {
                    sprites[0][1].x = -1;
                }
                return;
            }
            return;
        }
        if (u) {
            sprites[0][1].setSprite(0, 3);
        }
    }

    private void setBR(World m) {

        sprites[1][1].setSprite(2, 1);
        Tile t = m.getTile(loc.x + 1, loc.y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x , loc.y + 1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x + 1, loc.y + 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            sprites[1][1].setSprite(0, 2);
            if (u) {
                sprites[1][1].setSprite(1, 1);
                if (ul) {
                    sprites[1][1].x = -1;
                }
                return;
            }
            return;
        }
        if (u) {
            sprites[1][1].setSprite(0, 1);
        }
    }

}

enum TileType {

    water, ground, grass
}