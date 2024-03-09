package com.game.tama.core;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StatsDict implements java.io.Serializable
{

    Map<String, Integer> stats;
    transient Map<String, Displayable> pics;

    StatsDict()
    {
        stats = new HashMap();
        pics = new HashMap();
    }

    void alterStat(String stat, int val)
    {
        Integer curr = stats.get(stat);
        if (curr == null)
        {
            Log.d("Stats", "error, stat does not exist error");
            return;
        }
        stats.put(stat, curr + val);
    }

    Integer getStat(String stat)
    {
        Integer curr = stats.get(stat);
        if (curr == null)
        {
            Log.d("Stats", "error, stat does not exist");
        }
        return curr;
    }

    void addNewStat(String stat)
    {
        stats.put(stat, 0);
        pics.put(stat, Assets.getStatPic(stat));
    }

    void reLoadAssets()
    {
        Set<String> strs = stats.keySet();
        pics = new HashMap<>(strs.size());
        for (String stat : strs)
        {
            pics.put(stat, Assets.getStatPic(stat));
        }
    }

}
