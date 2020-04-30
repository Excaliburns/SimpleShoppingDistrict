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
}
