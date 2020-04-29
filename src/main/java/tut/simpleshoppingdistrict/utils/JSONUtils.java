package tut.simpleshoppingdistrict.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tut.simpleshoppingdistrict.data.SSDRegion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

/*Make a way to get and store the data into JSON and pull from JSON
and get the data from JSON on start up
 */
public class JSONUtils {
    private static final Logger logger = SSDLogger.getSSDLogger();
    protected static final String baseDataFolderPath = "plugins" + File.separator + "SimpleShoppingDistrict";

    //Translating object data to JSON
    private static String objectToJsonString(Object object) {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        return gson.toJson(object, object.getClass());
    }

    //Save Json file from the cache
    public static void saveCacheData(HashMap<String, TreeSet<SSDRegion>> cacheData) {
        //For each entry of cacheData it serializes the list to Json
        for (Map.Entry<String, TreeSet<SSDRegion>> cacheDataEntry : cacheData.entrySet()) {
            String currentUUID = cacheDataEntry.getKey();
            TreeSet<SSDRegion> currentRegionList = cacheDataEntry.getValue();

            File filename = new File(baseDataFolderPath + File.separator + currentUUID + ".json");

            try {

                if ( !filename.getParentFile().mkdirs() && filename.createNewFile() ) {
                    logger.info("Could not make player data file for " + currentUUID + ". Will try again on next save.");
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                writer.write(objectToJsonString(currentRegionList));
                writer.flush();
                writer.close();

            } catch (IOException e) {
                logger.warning("Exception while saving player data!");
                logger.warning(e.getMessage());
            }
        }
    }

}
