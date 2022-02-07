package com.tama.apptest;

abstract class Tile extends WorldObject {

    boolean visible;
    private Thing thing;

    Tile(boolean displayVar, Displayable d) {
        super(d);
        this.visible = displayVar;
    }

    Tile(Displayable d){

        this(true, d);
    }

    void display(DisplayAdapter d) {
        if (visible) {
            d.displayWorld(this);
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

    public void setVisible(boolean visible) {
        this.visible = visible;
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
            t.setPos(x(), y());
    }

    Thing takeThing(){
        Thing t = thing;
        thing = null;
        return t;
    }

    Thing getThing(){
        return thing;
    }

}

class Grass extends Tile {
    Grass(boolean var){
        super(var,
                Assets.sheets.get(R.drawable.sheet_16_terrainsimp).getSprite(0, 0));

    }

    Grass(){
        this(false);
    }


    public TileType type() {
        return TileType.grass;
    }

}

class Bush extends Tile{

    Bush(){
        super(true, Assets.sprites.get(R.drawable.static_bush1));

    }

}

class LongGrass extends Tile{
    WorldObject sprite2;
    WorldObject sprite3;
    LongGrass(){

        super(true, Assets.sprites.get(R.drawable.static_longgrass));
        flat = false;
        yoff = 1;
        sprite2 = new WorldObject(Assets.sprites.get(R.drawable.static_longgrass));
        sprite2.yoff = -30;
        sprite2.flat = false;
        sprite3 = new WorldObject(Assets.sprites.get(R.drawable.static_longgrass));
        sprite3.yoff = -60;
        sprite3.flat = false;
    }

    @Override
    void setPos(int x, int y){
        super.setPos(x, y);
        sprite2.setPos(x, y);
        sprite3.setPos(x, y);
    }

    @Override
    void display(DisplayAdapter d){

        super.display(d);
        d.displayWorld(sprite2);
        d.displayWorld(sprite3);
    }

}

class DynTile extends Tile {
    // considers the surrounding tiles to create a dynamic tile graphic
    static SpriteSheet sheet;
    WorldObject[][] parts;

    // img is 4 px sq 4*3 sprite sheet of possible configurations
    DynTile() {
        super(true, null);
        if (sheet == null)
            sheet = Assets.sheets.get(R.drawable.sheet_8_watersimp);

        parts = new WorldObject[2][2];

        for (int a = 0; a < parts.length; a++) {
            for (int b = 0; b < parts[a].length; b++) {

                parts[a][b] = new WorldObject(null);
                parts[a][b].xoff = a*50;
                parts[a][b].yoff = b*50;
            }
        }

    }

    public TileType type(){
        return TileType.water;
    }

    void display(DisplayAdapter d) {
        for (int a = 0; a < parts.length; a++) {
            for (int b = 0; b < parts[a].length; b++) {
                parts[a][b].display(d);
            }
        }
    }

    @Override
    void setPos(int x, int y){
        super.setPos(x, y);
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

        parts[0][0].sprite = sheet.getSprite(2, 3);
        Tile t = m.getTile(x() - 1, y());
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile( x(), y()-1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(x() - 1, y() -1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            parts[0][0].sprite = sheet.getSprite(0, 0);
            if (u) {
                parts[0][0].sprite = sheet.getSprite(1, 3);

                if (ul) {
                    parts[0][0].sprite = null;
                }
                return;
            }
            return;
        }
        if (u) {
            parts[0][0].sprite = sheet.getSprite(0, 3);
        }
    }

    private void setTR(World m) {

        parts[1][0].sprite = sheet.getSprite(2, 0);
        Tile t = m.getTile(x() + 1, y());
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(x() , y()-1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(x() + 1, y() - 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            parts[1][0].sprite = sheet.getSprite(0, 0);
            if (u) {
                parts[1][0].sprite = sheet.getSprite(1, 0);

                if (ul) {
                    parts[1][0].sprite = null;
                }
                return;
            }
            return;
        }
        if (u) {
            parts[1][0].sprite = sheet.getSprite(0, 1);
        }
    }

    private void setBL(World m) {

        parts[0][1].sprite = sheet.getSprite(2, 2);
        Tile t = m.getTile(x() - 1, y());
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(x(), y() + 1);
        boolean u =  t != null && t.type() == TileType.water;
        t = m.getTile(x() - 1, y() + 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            parts[0][1].sprite = sheet.getSprite(0, 2);
            if (u) {
                parts[0][1].sprite = sheet.getSprite(1, 2);
                if (ul) {
                    parts[0][1].sprite = null;
                }
                return;
            }
            return;
        }
        if (u) {
            parts[0][1].sprite = sheet.getSprite(0, 3);
        }
    }

    private void setBR(World m) {

        parts[1][1].sprite = sheet.getSprite(2, 1);
        Tile t = m.getTile(x() + 1, y());
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(x() , y() + 1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(x() + 1, y() + 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l) {
            parts[1][1].sprite = sheet.getSprite(0, 2);
            if (u) {
                parts[1][1].sprite = sheet.getSprite(1, 1);
                if (ul) {
                    parts[1][1].sprite = null;
                }
                return;
            }
            return;
        }
        if (u) {
            parts[1][1].sprite = sheet.getSprite(0, 1);
        }
    }

}

enum TileType {

    water, ground, grass
}