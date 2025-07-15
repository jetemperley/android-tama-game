package com.game.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.game.tama.core.AssetName;
import com.tama.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

public class AndroidAsset
{
    public static HashMap<GLAssetName, String> loadGlsl()
    {
        final HashMap<GLAssetName, String> raw = new HashMap<>();

        final Field[] fields = R.raw.class.getFields();
        for (final Field f : fields)
        {
            try
            {
                final int id = f.getInt(f);

                try (final InputStream stream = GameActivity.resources.openRawResource(id))
                {
                    final String content = read(stream);
                    raw.put(GLAssetName.valueOf(f.getName()), content);
                }
            }
            catch (final Exception e)
            {
                Log.d(
                    "Assets: ",
                    "failed to load resource {" + f.getName() + "}");
            }
        }
        return raw;
    }

    private static String read(final InputStream stream) throws IOException
    {
        final StringBuilder sb = new StringBuilder();
        for (int ch; (ch = stream.read()) != -1; )
        {
            sb.append((char) ch);
        }
        return sb.toString();
    }

    public static HashMap<AssetName, Bitmap[][]> loadSpriteSheets()
    {
        final HashMap<AssetName, Bitmap[][]> sheets = new HashMap<>(30);

        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        final Field[] fields = R.drawable.class.getFields();
        for (final Field f : fields)
        {
            final String name = f.getName();
            try
            {
                final int id = f.getInt(f);
                if (name.startsWith("sheet"))
                {
                    final Bitmap bitmap =
                        BitmapFactory.decodeResource(GameActivity.resources, id, opts);
                    final Bitmap[][] arr =
                        processSpriteSheet(
                            bitmap,
                            Integer.parseInt(name.split("_")[1]));
                    sheets.put(AssetName.valueOf(name), arr);
                }
            }
            catch (final Exception e)
            {
                Log.d(
                    "Assets: ",
                    String.format("failed to load resource {%s}", f.getName()));
            }
        }
        return sheets;
    }

    public static HashMap<AssetName, Bitmap> loadStaticSprites()
    {
        final HashMap<AssetName, Bitmap> sprites = new HashMap<>(30);
        final HashMap<AssetName, Bitmap[][]> sheets = new HashMap<>(30);

        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        final Field[] fields = R.drawable.class.getFields();
        for (final Field f : fields)
        {
            final String name = f.getName();
            try
            {
                final int id = f.getInt(f);
                if (name.startsWith("static"))
                {
                    final Bitmap bitmap = BitmapFactory.decodeResource(
                        GameActivity.resources,
                        id,
                        opts);
                    sprites.put(
                        AssetName.valueOf(name),
                        bitmap);
                }
            }
            catch (final Exception e)
            {
                Log.d(
                    "Assets: ",
                    "failed to load resource {" + f.getName() + "}");
            }
        }
        return sprites;
    }

    // arr is the sheet png, and size is the size of each sprite cell
    static Bitmap[][] processSpriteSheet(final Bitmap arr, final int size)
    {

        Log.d("SS", arr.getWidth() + " " + arr.getHeight());
        final Bitmap[][] sheet =
            new Bitmap[arr.getHeight() / size][arr.getWidth() / size];
        for (int y = 0; y < sheet.length; y++)
        {
            for (int x = 0; x < sheet[y].length; x++)
            {
                sheet[y][x] = Bitmap.createBitmap(
                    arr,
                    x * size,
                    y * size,
                    size,
                    size);
            }
        }
        return sheet;
    }
}
