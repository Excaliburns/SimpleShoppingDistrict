package tut.simpleshoppingdistrict.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import tut.simpleshoppingdistrict.data.SSDRegion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/*Make a way to get and store the data into JSON and pull from JSON
and get the data from JSON on start up
 */
public class JSONUtils {
    // Constants //////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final Logger logger       = SSDLogger.getSSDLogger();
    private static final boolean isDebugMode = SSDConstants.PLUGIN_DEBUG_MODE;

    //Translating object data to JSON
    private static String objectToJsonString(Object object) {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        return gson.toJson(object, object.getClass());
    }

    //Translating object data to JSON
    private static ArrayList<SSDRegion> jsonStringToSSDRegionArrayList(String jsonString) {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        Type SSDRegionListType = new TypeToken<ArrayList<SSDRegion>>() {
        }.getType();

        return gson.fromJson(jsonString, SSDRegionListType);
    }

    //Read Json file from the cache
    public static TreeSet<SSDRegion> getPlayerRegionDataFromFile(File file) {
        String jsonText;

        try {
            //Read the bytes of file
            jsonText = new String(Files.readAllBytes(file.toPath()));

            //Return new TreeSet based on the list data
            return new TreeSet<>(jsonStringToSSDRegionArrayList(jsonText));

        } catch (IOException e) {
            logger.warning("Could not load file: " + getBaseFileName(file.getName()) + " cache will not have the relevant data. Printing stack trace.");
            logger.warning(e.getMessage());
        }

        return new TreeSet<>();
    }

    //Save Json file from the cache
    public static void saveCacheData(ConcurrentHashMap<String, TreeSet<SSDRegion>> cacheData) {
        //For each entry of cacheData it serializes the list to Json
        for (Map.Entry<String, TreeSet<SSDRegion>> cacheDataEntry : cacheData.entrySet()) {
            logger.info("Saving cache data for player " + cacheDataEntry.getKey() + ".");
            String currentUUID = cacheDataEntry.getKey();
            TreeSet<SSDRegion> currentRegionList = cacheDataEntry.getValue();

            File filename = new File(SSDConstants.BASE_PLUGIN_FOLDER_PATH + File.separator + currentUUID + ".json");

            try {

                if (!filename.exists()) {
                    if (!(filename.getParentFile().mkdirs() && filename.createNewFile())) {
                        logger.info("Created region file for " + currentUUID + ".");
                    }
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                writer.write(objectToJsonString(new ArrayList<>(currentRegionList)));
                writer.flush();
                writer.close();

            } catch (IOException e) {
                logger.warning("Exception while saving player data!");
                logger.warning(e.getMessage());
            }
        }
    }

    public static String getBaseFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
