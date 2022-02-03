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

    //static ArrayList<StaticSprite> staticSprites;
    //static ArrayList<SpriteSheet> sheets;

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
    //    Bitmap bm = BitmapFactory.decodeResource(r, R.drawable., opts);
//        processStaticSprites(BitmapFactory.decodeResource(r, R.drawable.tree, opts), 16);
//        processStaticSprites(BitmapFactory.decodeResource(r, R.drawable.items, opts), 16);
//        processStaticSprites(BitmapFactory.decodeResource(r, R.drawable.tools, opts), 16);
//        processStaticSprites(BitmapFactory.decodeResource(r, R.drawable.treegrowth, opts), 16);
//        processStaticSprites(BitmapFactory.decodeResource(r, R.drawable.seed, opts), 16);
//        processStaticSprites(BitmapFactory.decodeResource(r, R.drawable.terrainsimp, opts), 16);
//        processStaticSprites(BitmapFactory.decodeResource(r, R.drawable.bush1, opts), 16);
//        processStaticSprites(BitmapFactory.decodeResource(r, R.drawable.longgrass, opts), 16);
//        processStaticSprites(BitmapFactory.decodeResource(r, R.drawable.rock, opts), 16);
//        Log.d("Assets init", staticSprites.size() + "");

        // Log.d("Assets", "sprites = " + sprites.size());
        // Log.d("Assets", "added seed");

        // sheets = new ArrayList<SpriteSheet>();
//        SpriteSheet ss = new SpriteSheet(
//                processSpriteSheet(BitmapFactory.decodeResource(r, R.drawable.watersimp, opts), 8));
//        sheets.add(ss);
//
//        ss = new SpriteSheet(
//                processSpriteSheet(BitmapFactory.decodeResource(r, R.drawable.blob, opts), 16));
//        sheets.add(ss);
//
//        ss = new SpriteSheet(
//                processSpriteSheet(BitmapFactory.decodeResource(r, R.drawable.egg, opts), 16));
//        sheets.add(ss);
//
//        ss = new SpriteSheet(
//                processSpriteSheet(BitmapFactory.decodeResource(r, R.drawable.walker, opts), 16));
//        sheets.add(ss);



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


}
