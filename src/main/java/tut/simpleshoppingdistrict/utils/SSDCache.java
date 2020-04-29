package tut.simpleshoppingdistrict.utils;

import tut.simpleshoppingdistrict.data.SSDRegion;

import javax.xml.stream.Location;
import java.util.*;

public class SSDCache {
    // Stores flag if player is drawing
    // Key is player UUID
    public static HashMap<String, Boolean> playerDrawingRegionCache;

    //Stores currently in progress cache of player
    //Key is player UUID
    public static HashMap<String, SSDRegion> regionInProgressCache;

    //Stores player regions
    //Key is player UUID
    public static HashMap<String, TreeSet<SSDRegion>> playerRegionCache;

    public static void initCaches() {
        playerDrawingRegionCache    = new HashMap<>();
        playerRegionCache           = new HashMap<>();
        regionInProgressCache       = new HashMap<>();
    }

    public static void finishDrawingRegion(final String UUID, SSDRegion finishedRegion) {
        if (playerRegionCache.containsKey(UUID)) {
            TreeSet<SSDRegion> regionList = playerRegionCache.get(UUID);
            regionList.add(finishedRegion);

            playerRegionCache.put(UUID, regionList);
            regionInProgressCache.remove(UUID);
            playerDrawingRegionCache.remove(UUID);
        } else {
            playerRegionCache.put(UUID, new TreeSet<SSDRegion>() {{ add(finishedRegion); }});
            regionInProgressCache.remove(UUID);
            playerDrawingRegionCache.remove(UUID);
        }
    }
}
