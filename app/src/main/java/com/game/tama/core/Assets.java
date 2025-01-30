package com.game.tama.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tama.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Assets
{
    public enum Names
    {

        ic_launcher_background,
        sheet_16_blob,
        sheet_16_bush,
        sheet_16_egg,
        sheet_16_terrain,
        sheet_16_terrainsimp,
        sheet_16_treegrowth,
        sheet_16_treeripped,
        sheet_16_walker,
        sheet_16_button,
        sheet_16_cell_pet,

        sheet_5_num,
        sheet_8_water,
        sheet_8_watersimp,
        sheet_8_letters,
        sheet_8_symbols,

        static_acorn,
        static_apple,
        static_axe,
        static_bar,
        static_bush1,
        static_carrot,
        static_cherries,
        static_energy,
        static_energy2,
        static_fish,
        static_flower,
        static_fork,
        static_happiness,
        static_heart,
        static_herb,
        static_inv,
        static_leaf,
        static_leaf2,
        static_log,
        static_longgrass,
        static_longgrass2,
        static_meat,
        static_meatbone,
        static_meatbone2,
        static_mushroom,
        static_poop,
        static_rock,
        static_seed,
        static_shovel,
        static_smallheart,
        static_tree,
        static_waterdrop,
        static_waterdrop2,
        static_zzz,
        static_pullbush,
        static_menu,
        static_backpack,
        static_empty,
        static_circle_16,
        static_circle_14,
        static_x,
        static_o,
        static_cell_pet
    }

    public static Map<Integer, StaticSprite> sprites;
    public static Map<Integer, SpriteSheet> sheets;

    public static void init(Resources r)
    {

        sprites = new HashMap<>(30);
        sheets = new HashMap<>(30);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        Field[] fields = R.drawable.class.getFields();
        for (Field f : fields)
        {
            String name = f.getName();
            try
            {
                int id = f.getInt(f);
                if (name.startsWith("static"))
                {

                    sprites.put(id, new StaticSprite(BitmapFactory.decodeResource(r, id, opts)));

                }
                else if (name.startsWith("sheet"))
                {
                    Bitmap bm = BitmapFactory.decodeResource(r, id, opts);
                    StaticSprite[][] arr =
                            processSpriteSheet(bm, Integer.parseInt(name.split("_")[1]));
                    sheets.put(id, new SpriteSheet(arr));
                }
            } catch (Exception e)
            {
                Log.d("Assets: ", "failed to load resource");
            }
        }
    }

    // arr is the sheet png, and size is the size of each sprite cell
    static StaticSprite[][] processSpriteSheet(Bitmap arr, int size)
    {

        Log.d("SS", "" + arr.getWidth() + " " + arr.getHeight());
        StaticSprite[][] sheet = new StaticSprite[arr.getHeight() / size][arr.getWidth() / size];
        for (int y = 0; y < sheet.length; y++)
        {
            for (int x = 0; x < sheet[y].length; x++)
            {
                sheet[y][x] = new StaticSprite(Bitmap.createBitmap(arr,
                                                                   x * size,
                                                                   y * size,
                                                                   size,
                                                                   size));
            }
        }
        return sheet;
    }

    public static Sprite getStatPic(String stat)
    {

        switch (stat)
        {
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

    public static StaticSprite getSprite(String name)
    {
        if (name == null)
        {
            return sprites.get(R.drawable.static_empty);
        }
        try
        {
            Field f = R.drawable.class.getField(name);
            StaticSprite ret = sprites.get(f.getInt(f));
            return ret;
        } catch (Exception e)
        {
            throw new RuntimeException("Sprite asset did not exist: " + name, e);
        }
    }

    public static SpriteSheet getSheet(String name)
    {
        try
        {
            Field f = R.drawable.class.getField(name);
            SpriteSheet sheet = sheets.get(f.getInt(f));
            return sheet;
        } catch (Exception e) {}

        return sheets.get(R.drawable.sheet_16_terrainsimp);
    }

}
