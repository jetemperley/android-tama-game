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

    static Map<String, Bitmap> sprites;
    static Map<String, Bitmap[][]> sheets;


    static void init(Resources r){

        sprites = new HashMap<>(30);
        sheets = new HashMap<>(30);


        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        int[] colors = new int[] {-1};

        Bitmap b1 = Bitmap.createBitmap(colors, 1, 1, Bitmap.Config.ARGB_8888);
        sprites.put("static_1", b1);

        Field[] fields = R.drawable.class.getFields();
        for (Field f : fields){
            String name = f.getName();
            try {
                int id = f.getInt(f);
                if (name.startsWith("static")){

                    sprites.put(name, BitmapFactory.decodeResource(r, id, opts));

                } else if (name.startsWith("sheet")) {
                    Bitmap bm = BitmapFactory.decodeResource(r, id, opts);
                    Bitmap[][] arr =
                            processSpriteSheet(bm, Integer.parseInt(name.split("_")[1]));
                    sheets.put(name, arr);
                }
            } catch (Exception e){ Log.d("Assets: ", "failed to load resource"); }
        }

    }

     // arr is the sheet png, and size is the size of each sprite cell
    static Bitmap[][] processSpriteSheet(Bitmap arr, int size) {

        Log.d("SS", "" + arr.getWidth() + " " + arr.getHeight());
        Bitmap[][] sheet = new Bitmap[arr.getHeight() / size][arr.getWidth() / size];
        for (int y = 0; y < sheet.length; y++) {
            for (int x = 0; x < sheet[y].length; x++) {
                sheet[y][x] = Bitmap.createBitmap(arr, x * size, y * size, size, size);
            }
        }
        return sheet;
    }

    static Bitmap getSprite(String name) {
        Bitmap s = sprites.get(name);
        if (s == null)
            Log.e("Asset load fail", "could not find " + name);
        return s;
    }
    static Bitmap[][] getSheet(String name){
        Bitmap[][] s = sheets.get(name);
        if (s == null)
            Log.e("Asset load fail", "could not find " + name);
        return s;
    }

}

