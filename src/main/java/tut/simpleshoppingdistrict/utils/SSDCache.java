package tut.simpleshoppingdistrict.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import tut.simpleshoppingdistrict.data.SSDRegion;

import javax.xml.stream.Location;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSDCache {
    // Constants //////////////////////////////////////////////////
    private static final Logger logger = SSDLogger.getSSDLogger();

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

    public static void loadPlayerRegionCache() {
        File[] diskData = new File(SSDConstants.BASE_PLUGIN_FOLDER_PATH).listFiles();

        if (diskData != null) {
            for (File file : diskData) {
                String UUID = JSONUtils.getBaseFileName(file.getName());
                playerRegionCache.put(UUID, JSONUtils.getPlayerRegionDataFromFile(file));
            }

        } else {
            logger.warning("Could not find files of region data. If this is the first time running the server, this is to be expected.");
        }
    }

    public static void finishDrawingRegion(final String UUID, SSDRegion finishedRegion) {
        if (playerRegionCache.containsKey(UUID)) {
            TreeSet<SSDRegion> regionList = playerRegionCache.get(UUID);
            regionList.add(finishedRegion);

            playerRegionCache.put(UUID, regionList);
            regionInProgressCache.remove(UUID);
            playerDrawingRegionCache.remove(UUID);
        } else {
            playerRegionCache.put(UUID, new TreeSet<SSDRegion>() {{
                add(finishedRegion);
            }});
            regionInProgressCache.remove(UUID);
            playerDrawingRegionCache.remove(UUID);
        }
    }

    public static void startCacheSavingTimer(JavaPlugin plugin) {
        logger.info("Starting cache saving timer with delay " + SSDConstants.PLUGIN_CACHE_DELAY + " ticks. ");
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskTimerAsynchronously(
                plugin,
                () -> JSONUtils.saveCacheData(SSDCache.playerRegionCache),
                SSDConstants.PLUGIN_CACHE_DELAY,
                SSDConstants.PLUGIN_CACHE_INTERVAL
        );
    }
}
