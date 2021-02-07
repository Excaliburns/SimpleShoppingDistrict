package tut.simpleshoppingdistrict.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import tut.simpleshoppingdistrict.data.Point;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtils {
    public static Location[] getCuboidPointsFromRegionBounds(Point c1, Point c2, World world) {
        ArrayList<Location> locationArrayList = new ArrayList<>();

        double minX = Math.min(c1.getX(), c2.getX());
        double minY = Math.min(c1.getY(), c2.getY());
        double minZ = Math.min(c1.getZ(), c2.getZ());
        double maxX = Math.max(c1.getX(), c2.getX());
        double maxY = Math.max(c1.getY(), c2.getY());
        double maxZ = Math.max(c1.getZ(), c2.getZ());

        for (double x = minX; x <= maxX + 1; x += .25) {
            for (double y = minY; y <= maxY + 1; y += .25) {
                for (double z = minZ; z <= maxZ + 1; z += .25) {
                    int components = 0;
                    if (x == minX || x == maxX + 1) components++;
                    if (y == minY || y == maxY + 1) components++;
                    if (z == minZ || z == maxZ + 1) components++;
                    if (components >= 2) {
                        locationArrayList.add(new Location(world, x, y, z));
                    }
                }
            }
        }
        return locationArrayList.toArray(new Location[0]);
    }
}
