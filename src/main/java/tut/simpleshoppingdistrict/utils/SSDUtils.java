package tut.simpleshoppingdistrict.utils;

public class SSDUtils {

    //Thank you GriefPrevention :cry:
    public static long getChunkHash(long x, long z) {
        return (x ^ (z >> 32));
    }
}
