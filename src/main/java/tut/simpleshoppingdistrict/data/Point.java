package tut.simpleshoppingdistrict.data;


import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;

public class Point {
    String world;
    double x;
    double y;
    double z;

    public Point() {
        this.world = null;
    }

    public Point(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(Location location) {
        this.world =  ( !Objects.isNull(location.getWorld()) ) ? location.getWorld().getName() : "";
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), this.x, this.y, this.z);
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }


    @Override
    public String toString() {
        return "Point{" + "world='" + world + '\'' + ", x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0 && Double.compare(point.z, z) == 0 && Objects.equals(world, point.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }
}
