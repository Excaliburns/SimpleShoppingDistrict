package tut.simpleshoppingdistrict.data;

import org.bukkit.Location;
import org.bukkit.World;

public class SSDRegion implements Comparable<SSDRegion> {
    int regionID;
    Point bound1;
    Point bound2;
    boolean completeRegion;

    public SSDRegion(World world, int regionID) {
        this.regionID = regionID;
        bound1 = new Point();
        bound2 = new Point();
        completeRegion = false;
    }

    public Point getBound1() { return this.bound1; }

    public Point getBound2() { return this.bound2; }

    public Location getBound1Location() { return bound1.getLocation(); }

    public Location getBound2Location() { return bound2.getLocation(); }

    public int getRegionID() {
        return regionID;
    }

    public void setBound1(Point bound1) {
        this.bound1 = bound1;
    }

    public void setBound2(Point bound2) { this.bound2 = bound2; }

    public boolean isCompleteRegion() {
        return completeRegion;
    }

    public void setCompleteRegion(boolean completeRegion) {
        this.completeRegion = completeRegion;
    }


    /**
     *
     * @param o
     * @return returns 1 if ID of input is greater than own regionID. 0 if less than. -1 if equal to.
     */
    @Override
    public int compareTo(SSDRegion o) {
        return regionID;
    }
}
