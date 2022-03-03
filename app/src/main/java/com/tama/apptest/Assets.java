package com.tama.apptest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Assets{
    final static String
    ic_launcher_background = "ic_launcher_background.xml",
    sheet_16_blob = "sheet_16_blob",
    sheet_16_bush = "sheet_16_bush",
    sheet_16_egg = "sheet_16_egg",
    sheet_16_terrain = "sheet_16_terrain",
    sheet_16_terrainsimp = "sheet_16_terrainsimp",
    sheet_16_treegrowth = "sheet_16_treegrowth",
    sheet_16_treeripped = "sheet_16_treeripped",
    sheet_16_walker = "sheet_16_walker",
    sheet_5_num = "sheet_5_num",
    sheet_8_water = "sheet_8_water",
    sheet_8_watersimp = "sheet_8_watersimp",
    static_acorn = "static_acorn",
    static_apple = "static_apple",
    static_axe = "static_axe",
    static_bar = "static_bar",
    static_bush1 = "static_bush1",
    static_carrot = "static_carrot",
    static_cherries = "static_cherries",
    static_energy = "static_energy",
    static_energy2 = "static_energy2",
    static_fish = "static_fish",
    static_flower = "static_flower",
    static_fork = "static_fork",
    static_happiness = "static_happiness",
    static_heart = "static_heart",
    static_herb = "static_herb",
    static_inv = "static_inv",
    static_leaf = "static_leaf",
    static_leaf2 = "static_leaf2",
    static_log = "static_log",
    static_longgrass = "static_longgrass",
    static_longgrass2 = "static_longgrass2",
    static_meat = "static_meat",
    static_meatbone = "static_meatbone",
    static_meatbone2 = "static_meatbone2",
    static_mushroom = "static_mushroom",
    static_poop = "static_poop",
    static_rock = "static_rock",
    static_seed = "static_seed",
    static_shovel = "static_shovel",
    static_smallheart = "static_smallheart",
    static_tree = "static_tree",
    static_waterdrop = "static_waterdrop",
    static_waterdrop2 = "static_waterdrop2",
    static_zzz = "static_zzz",
    static_pullbush = "static_pullbush";

    static Map<Integer, StaticSprite> sprites;
    static Map<Integer, SpriteSheet> sheets;

    static void init(Resources r){

        sprites = new HashMap<>(30);
        sheets = new HashMap<>(30);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        Field[] fields = R.drawable.class.getFields();
        for (Field f : fields){
            String name = f.getName();
            try {
                int id = f.getInt(f);
                if (name.startsWith("static")){

                    sprites.put(id, new StaticSprite(BitmapFactory.decodeResource(r, id, opts)));

                } else if (name.startsWith("sheet")) {
                    Bitmap bm = BitmapFactory.decodeResource(r, id, opts);
                    StaticSprite[][] arr =
                            processSpriteSheet(bm, Integer.parseInt(name.split("_")[1]));
                    sheets.put(id, new SpriteSheet(arr));
                }
            } catch (Exception e){ Log.d("Assets: ", "failed to load resource"); }
        }
    }

     // arr is the sheet png, and size is the size of each sprite cell
    static StaticSprite[][] processSpriteSheet(Bitmap arr, int size) {

        Log.d("SS", "" + arr.getWidth() + " " + arr.getHeight());
        StaticSprite[][] sheet = new StaticSprite[arr.getHeight() / size][arr.getWidth() / size];
        for (int y = 0; y < sheet.length; y++) {
            for (int x = 0; x < sheet[y].length; x++) {
                sheet[y][x] = new StaticSprite(Bitmap.createBitmap(arr, x * size, y * size, size, size));
            }
        }
        return sheet;
    }

    static Displayable getStatPic(String stat){
        
        switch(stat){
            case "hunger":
                return sprites.get(R.drawable.static_fork);

            case "thirst":
                return sprites.get(R.drawable.static_waterdrop);

            case "happy":
                return sprites.get(R.drawable.static_happiness);

            case "sleep":
                return sprites.get(R.drawable.static_zzz);

            case "poop":
                return sprites.get(R.drawable.static_poop);

            case "energy":
                return sprites.get(R.drawable.static_energy);



        }
        return null;
    }

    static StaticSprite getSprite(String name){
        try {
            Field f = R.drawable.class.getField(name);
            StaticSprite ret = sprites.get(f.getInt(f));
            return ret;
        } catch (Exception e){}
        return sprites.get(R.drawable.static_poop);
    }

    static SpriteSheet getSheet(String name){
        try {
            Field f = R.drawable.class.getField(name);
            SpriteSheet ret = sheets.get(f.getInt(f));
            return ret;
        } catch (Exception e){}
        return sheets.get(R.drawable.sheet_16_terrainsimp);
    }

}
