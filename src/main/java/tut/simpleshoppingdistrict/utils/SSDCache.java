package tut.simpleshoppingdistrict.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import tut.simpleshoppingdistrict.data.SSDRegion;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    public static ConcurrentHashMap<String, TreeSet<SSDRegion>> playerRegionCache;

    // Stores chunk data and claims within them
    // Key is hashcode of chunk
    public static ConcurrentHashMap<Long, HashSet<SSDRegion>> chunkClaimCache;

    // Stores chunk hash and players that have claims within those chunks
    // Key is hashcode of chunk
    public static ConcurrentHashMap<Long, HashSet<String>> chunkHashToPlayerIds;

    static {
        playerDrawingRegionCache = new HashMap<>();
        regionInProgressCache = new HashMap<>();
        playerRegionCache = new ConcurrentHashMap<>();
        chunkClaimCache = new ConcurrentHashMap<>();
        chunkHashToPlayerIds = new ConcurrentHashMap<>();
    }

    public static void loadPlayerRegionCache() {
        File chunkCache = new File(SSDConstants.BASE_PLUGIN_FOLDER_PATH + File.separator + "chunkdata.json");
        File[] diskData = new File(SSDConstants.BASE_PLUGIN_FOLDER_PATH + File.separator + "playerdata").listFiles();

        if (diskData != null) {
            for (File file : diskData) {
                String UUID = JSONUtils.getBaseFileName(file.getName());
                playerRegionCache.put(UUID, JSONUtils.getPlayerRegionDataFromFile(file));
            }

        }
        else {
            logger.warning("Could not find files of region data. If this is the first time running the server, this is to be expected.");
        }

        if (chunkCache.exists()) {
            chunkHashToPlayerIds = new ConcurrentHashMap<>(JSONUtils.getChunksWithPlayerInformationFromFile(chunkCache));
        }
        else {
            logger.warning("Could not find chunk cache data. If this is the first time running the server, this is to be expected.");
        }
    }

    public synchronized static void finishDrawingRegion(final String UUID, SSDRegion finishedRegion) {
        if (playerRegionCache.containsKey(UUID)) {
            TreeSet<SSDRegion> regionList = playerRegionCache.get(UUID);
            regionList.add(finishedRegion);

            playerRegionCache.put(UUID, regionList);
        } else {
            playerRegionCache.put(UUID, new TreeSet<SSDRegion>() {{
                add(finishedRegion);
            }});
        }

        SSDUtils.addRegionChunkData(finishedRegion);

        regionInProgressCache.remove(UUID);
        playerDrawingRegionCache.remove(UUID);
    }

    public static void StartCacheSavingTimer(JavaPlugin plugin) {
        logger.info("Starting cache saving timer with delay " + SSDConstants.PLUGIN_CACHE_DELAY + " ticks. ");
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskTimerAsynchronously(plugin, () ->
                JSONUtils.savePlayerCacheData(SSDCache.playerRegionCache),
                SSDConstants.PLUGIN_CACHE_DELAY,
                SSDConstants.PLUGIN_CACHE_INTERVAL);
    }
}
