package tut.simpleshoppingdistrict.data;

import org.bukkit.Location;
import org.bukkit.World;

public class SSDRegion {
    int regionID;
    Location bound1;
    Location bound2;
    boolean completeRegion;

    public SSDRegion(World world) {
        regionID = 0;
        bound1 = new Location(world, 0, 0, 0);
        bound2 = new Location(world, 0, 0, 0);
        completeRegion = false;
    }

    public Location getBound1() {
        return bound1;
    }

    public Location getBound2() {
        return bound2;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setBound1(Location bound1) {
        this.bound1 = bound1;
    }

    public void setBound2(Location bound2) {
        this.bound2 = bound2;
    }

    public boolean isCompleteRegion() {
        return completeRegion;
    }

    public void setCompleteRegion(boolean completeRegion) {
        this.completeRegion = completeRegion;
    }
}
