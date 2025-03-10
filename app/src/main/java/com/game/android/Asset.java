package com.game.android;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.SpriteSheet;
import com.game.tama.core.StaticSprite;
import com.tama.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Asset
{
    private static Map<Integer, String> raw;
    private static Map<Integer, StaticSprite> sprites;
    private static Map<Integer, SpriteSheet> sheets;

    public static void init(Resources resources)
    {
        initSprites(resources);
        initGlsl(resources);
    }

    public static void initGlsl(Resources resources)
    {
        raw = new HashMap<>();

        Field[] fields = R.raw.class.getFields();
        for (Field f : fields)
        {
            String name = f.getName();
            try
            {
                int id = f.getInt(f);

                try (InputStream stream = resources.openRawResource(id))
                {
                    String content = read(stream);
                    raw.put(id, content);
                }
            }
            catch (Exception e)
            {
                Log.d(
                    "Assets: ",
                    "failed to load resource {" + f.getName() + "}");
            }
        }
    }

    public static String getRawContent(GLAssetName name)
    {
        if (name == null)
        {
            return null;
        }
        try
        {
            Field f = R.raw.class.getField(name.name());
            String content = raw.get(f.getInt(f));
            return content;
        }
        catch (Exception e)
        {
            throw new RuntimeException(
                "Raw asset did not exist: " + name,
                e);
        }
    }

    private static String read(InputStream stream) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = stream.read()) != -1; ) {
            sb.append((char) ch);
        }
        return sb.toString();
    }

    private static void initSprites(Resources resources)
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
                    sprites.put(
                        id,
                        new StaticSprite(BitmapFactory.decodeResource(
                            resources,
                            id,
                            opts)));
                }
                else if (name.startsWith("sheet"))
                {
                    Bitmap bm =
                        BitmapFactory.decodeResource(resources, id, opts);
                    StaticSprite[][] arr =
                        processSpriteSheet(
                            bm,
                            Integer.parseInt(name.split("_")[1]));
                    sheets.put(id, new SpriteSheet(arr));
                }
            }
            catch (Exception e)
            {
                Log.d(
                    "Assets: ",
                    "failed to load resource {" + f.getName() + "}");
            }
        }
    }

    // arr is the sheet png, and size is the size of each sprite cell
    static StaticSprite[][] processSpriteSheet(Bitmap arr, int size)
    {

        Log.d("SS", "" + arr.getWidth() + " " + arr.getHeight());
        StaticSprite[][] sheet =
            new StaticSprite[arr.getHeight() / size][arr.getWidth() / size];
        for (int y = 0; y < sheet.length; y++)
        {
            for (int x = 0; x < sheet[y].length; x++)
            {
                sheet[y][x] = new StaticSprite(Bitmap.createBitmap(
                    arr,
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

    public static StaticSprite getStaticSprite(AssetName name)
    {
        if (name == null)
        {
            return sprites.get(R.drawable.static_empty);
        }
        try
        {
            Field f = R.drawable.class.getField(name.name());
            StaticSprite sprite = sprites.get(f.getInt(f));
            return sprite;
        }
        catch (Exception e)
        {
            throw new RuntimeException(
                "Sprite asset did not exist: " + name,
                e);
        }
    }

    public static SpriteSheet getSpriteSheet(AssetName name)
    {
        try
        {
            Field f = R.drawable.class.getField(name.name());
            SpriteSheet sheet = sheets.get(f.getInt(f));
            return sheet;
        }
        catch (Exception e)
        {
        }

        return sheets.get(R.drawable.sheet_16_terrainsimp);
    }
}
