package tut.simpleshoppingdistrict.utils;

import tut.simpleshoppingdistrict.SimpleShoppingDistrict;

import java.util.logging.Logger;

public class SSDLogger {
    public static Logger getSSDLogger() {
        return SimpleShoppingDistrict.getPlugin(SimpleShoppingDistrict.class).getLogger();
    }
}
