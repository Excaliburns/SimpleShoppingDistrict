package tut.simpleshoppingdistrict.utils;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import tut.simpleshoppingdistrict.SimpleShoppingDistrict;
import tut.simpleshoppingdistrict.data.Point;
import tut.simpleshoppingdistrict.data.SSDRegion;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SSDCache {
    // Constants //////////////////////////////////////////////////
    private static final Logger logger = SSDLogger.getSSDLogger();
    private static Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromBGR(0, 127, 255), 1);
    private static BukkitScheduler scheduler = SimpleShoppingDistrict.getPlugin(SimpleShoppingDistrict.class).getServer().getScheduler();

    // Stores flag if player is drawing
    // Key is player UUID
    public static HashMap<String, Boolean> playerDrawingRegionCache;

    //Stores currently in progress cache of player
    //Key is player UUID
    public static HashMap<String, SSDRegion> regionInProgressCache;

    //Stores player regions
    //Key is player UUID
    public static ConcurrentHashMap<String, TreeSet<SSDRegion>> playerRegionCache;

    //Stores chunk data and claims within them
    //Key is hashcode of chunk
    public static ConcurrentHashMap<Long, List<SSDRegion>> chunkClaimCache;

    public static void initCaches() {
        playerDrawingRegionCache = new HashMap<>();
        regionInProgressCache = new HashMap<>();
        playerRegionCache = new ConcurrentHashMap<>();
        chunkClaimCache = new ConcurrentHashMap<>();
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


        double distance = finishedRegion.getBound1Location().distance(finishedRegion.getBound2Location());
        Vector p1 = finishedRegion.getBound1Location().toVector();
        Vector p2 = finishedRegion.getBound2Location().toVector();
        Vector vector = p2.clone().subtract(p1).normalize();
        World world = Bukkit.getWorld(finishedRegion.getWorld());

        for (double covered = 0; covered < distance; p1.add(vector)) {
            if (world != null) {
                Chunk chunk = world.getChunkAt(p1.toLocation(world));
                Long chunkHash = SSDUtils.getChunkHash(chunk.getX(), chunk.getZ());
                List<SSDRegion> regionList;

                if (!SSDCache.chunkClaimCache.containsKey(chunkHash)) {
                    regionList = new ArrayList<>();
                } else {
                    regionList = SSDCache.chunkClaimCache.get(chunkHash);
                }

                regionList.add(finishedRegion);
                SSDCache.chunkClaimCache.put(chunkHash, regionList);
            }
            covered += 1;
        }

        regionInProgressCache.remove(UUID);
        playerDrawingRegionCache.remove(UUID);
    }

    public static void StartCacheSavingTimer(JavaPlugin plugin) {
        logger.info("Starting cache saving timer with delay " + SSDConstants.PLUGIN_CACHE_DELAY + " ticks. ");
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskTimerAsynchronously(plugin, () -> JSONUtils.saveCacheData(SSDCache.playerRegionCache), SSDConstants.PLUGIN_CACHE_DELAY, SSDConstants.PLUGIN_CACHE_INTERVAL);
    }
}
