package tut.simpleshoppingdistrict.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import tut.simpleshoppingdistrict.data.Point;
import tut.simpleshoppingdistrict.data.SSDRegion;

import java.util.ArrayList;
import java.util.List;

public class SSDUtils {

    public static void addRegionChunkData(SSDRegion region) {
        double distance = region.getBound1Location().distance(region.getBound2Location());
        Vector p1 = region.getBound1Location().toVector();
        Vector p2 = region.getBound2Location().toVector();
        Vector vector = p2.clone().subtract(p1).normalize();
        World world = Bukkit.getWorld(region.getWorld());

        for (double covered = 0; covered < distance; p1.add(vector)) {
            if (world != null) {
                Chunk chunk = world.getChunkAt(p1.toLocation(world));
                Long chunkHash = SSDUtils.getChunkHash(chunk.getX(), chunk.getZ());
                List<SSDRegion> regionList;

                if (!region.getChunkContainerHash().contains(chunkHash)) {
                    region.addChunkToContainerHashList(chunkHash);
                }

                if (!SSDCache.chunkClaimCache.containsKey(chunkHash)) {
                    regionList = new ArrayList<>();
                } else {
                    regionList = SSDCache.chunkClaimCache.get(chunkHash);
                }

                regionList.add(region);
                SSDCache.chunkClaimCache.put(chunkHash, regionList);
            }

            covered += 1;
        }
    }

    public static Location getMinLocationFromTwoPoints(final Point p1, final Point p2, final World world) {
        return new Location(world, Math.min(p1.getX(), p2.getX()), Math.min(p1.getY(), p2.getY()), Math.min(p1.getZ(), p2.getZ()));
    }

    public static Location getMaxLocationFromTwoPoints(final Point p1, final Point p2, final World world) {
        return new Location(world, Math.max(p1.getX(), p2.getX()), Math.max(p1.getY(), p2.getY()), Math.max(p1.getZ(), p2.getZ()));
    }

    //Thank you GriefPrevention :cry:
    public static long getChunkHash(long x, long z) {
        return (x ^ (z >> 32));
    }
}
