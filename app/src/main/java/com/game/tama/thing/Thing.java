package com.game.tama.thing;

import com.game.tama.core.AssetName;
import com.game.tama.thing.component.Component;
import com.game.android.Asset;
import com.game.engine.DisplayAdapter;
import com.game.tama.core.Sprite;
import com.game.tama.core.Loadable;
import com.game.tama.core.Type;
import com.game.tama.core.World;
import com.game.tama.core.WorldObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Thing implements java.io.Serializable, Loadable
{
    protected AssetName asset = AssetName.static_poop;
    @NotNull
    public final WorldObject loc;
    public final List<Thing> children;
    private final List<Component> components;
    public String name = "Unnamed-thing";
    private LinkedHashSet<ThingControl.Name> controls = new LinkedHashSet<>();

    public Thing()
    {
        loc = new WorldObject(Asset.getStaticSprite(asset));
        loc.sprite = getAsset();
        children = new ArrayList<>();
        components = new ArrayList<>();
    }

    public void draw(DisplayAdapter d) {
        d.drawArr(loc);
        for (Thing thing : children)
        {
            thing.draw(d);
        }
    }

    public Sprite getAsset()
    {
        return Asset.getStaticSprite(asset);
    }

    @Override
    public void load()
    {
        loc.sprite = getAsset();
    }

    public void update(World map)
    {
        for (Thing thing : children)
        {
            thing.update(map);
        }
        for (Component component : components)
        {
            component.update();
        }
    }

    public boolean canSwim()
    {
        return false;
    }

    public boolean canWalk()
    {
        return true;
    }

    /**
     * Checks whether the point x,y is inside the bounding box for this object
     *
     * @param x
     * @param y
     * @return
     */
    public boolean contains(float x, float y)
    {
        if (x > loc.x + (loc.xoff / 100f) &&
            x < loc.x + (loc.xoff / 100f) + 1 &&
            y > loc.y + (loc.yoff / 100f) && y < loc.y + (loc.yoff / 100f) + 1)
        {
            return true;
        }
        return false;
    }

    public Type type()
    {
        return Type.undefined;
    }

    public void use()
    {
    }

    public void poke()
    {
    }

    public String getDescription()
    {
        return "Name: " + name + ", Loc: " + loc.x + ", " + loc.y + ". ";
    }

    public Thing pickup()
    {
        return this;
    }

    public <T extends Component> T getComponent(Class<T> clazz)
    {
        for (Component component : components)
        {
            if (component.getClass().isInstance(clazz))
            {
                return (T) component;
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> clazz)
    {
        for (int i = 0; i < components.size(); i++)
        {
            if (components.get(i).getClass().isInstance(clazz))
            {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component)
    {
        component.setParent(this);
        components.add(component);
    }

    public List<ThingControl> getControls()
    {
        return controls.stream()
            .map(name -> ThingControl.controls.get(name))
            .collect(Collectors.toList());
    }

    public void addControl(ThingControl.Name controlName)
    {
        controls.add(controlName);
    }

    public void removeControl(ThingControl.Name controlName)
    {
        controls.remove(controlName);
    }
}


