package tut.simpleshoppingdistrict.utils;


import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import tut.simpleshoppingdistrict.data.SSDRegion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final Type SSDRegionListType = new TypeToken<ArrayList<SSDRegion>>() {
        }.getType();

        return gson.fromJson(jsonString, SSDRegionListType);
    }

    //Translating object data to JSON
    private static HashMap<Long, HashSet<String>> jsonStringToChunkCache(String jsonString) {
        final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final Type completeChunkCache = new TypeToken<HashMap<Long, HashSet<String>>>() {}.getType();

        return gson.fromJson(jsonString, completeChunkCache);
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

    public static HashMap<Long, HashSet<String>> getChunksWithPlayerInformationFromFile(File file) {
        String jsonText;

        try {
            jsonText = new String(Files.readAllBytes(file.toPath()));

            return jsonStringToChunkCache(jsonText);
        } catch (IOException e) {
            logger.warning("Could not load file: " + getBaseFileName(file.getName()) + " cache will not have the relevant data. Printing stack trace.");
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    //Save Json file from the cache
    public static void savePlayerCacheData(ConcurrentHashMap<String, TreeSet<SSDRegion>> cacheData) {
        HashMap<Long, HashSet<String>> chunkPlayerClaimContainer = new HashMap<>();

        //For each entry of cacheData it serializes the list to Json
        for (Map.Entry<String, TreeSet<SSDRegion>> cacheDataEntry : cacheData.entrySet()) {
            String currentUUID = cacheDataEntry.getKey();
            TreeSet<SSDRegion> currentRegionList = cacheDataEntry.getValue();
            List<Long> chunkHashesInCurrentRegionList = new ArrayList<>();

            for (SSDRegion region : currentRegionList) {
                chunkHashesInCurrentRegionList.addAll(region.getChunkContainerHash());
            }

            chunkHashesInCurrentRegionList.forEach( entry -> {
                                              if (chunkPlayerClaimContainer.containsKey(entry)) {
                                                  HashSet<String> currentSet = chunkPlayerClaimContainer.get(entry);
                                                  currentSet.add(currentUUID);
                                                  chunkPlayerClaimContainer.put(entry, currentSet);
                                              }
                                              else {
                                                  chunkPlayerClaimContainer.put(entry, new HashSet<>(Arrays.asList(currentUUID)));
                                              }
                                          });

            File filename = new File(SSDConstants.BASE_PLUGIN_FOLDER_PATH + File.separator + "playerdata" + File.separator + currentUUID + ".json");

            try {
                if (!filename.exists()) {
                    if (!(filename.getParentFile().mkdirs() && filename.createNewFile())) {
                        logger.info("Created region file for " + currentUUID + ".");
                    }
                }

                TreeSet<SSDRegion> fileRegions = new TreeSet<>(JSONUtils.jsonStringToSSDRegionArrayList(new String(Files.readAllBytes(filename.toPath()))));

                if (!fileRegions.equals(currentRegionList)) {
                    logger.info("Saving cache data for player " + cacheDataEntry.getKey() + ".");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                    writer.write(objectToJsonString(new ArrayList<>(currentRegionList)));
                    writer.flush();
                    writer.close();
                }


            } catch (IOException e) {
                logger.warning("Exception while saving player data!");
                e.printStackTrace();
            }
        }
        try {
            File filename = new File(SSDConstants.BASE_PLUGIN_FOLDER_PATH + File.separator + "chunkdata.json");
            HashMap<Long, HashSet<String>> fileRegions;

            if (!filename.exists()) {
                if (!(filename.getParentFile().mkdirs() && filename.createNewFile())) {
                    logger.info("Created new chunkdata.json");
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                fileRegions = new HashMap<>();
                writer.write(objectToJsonString(fileRegions));
                writer.flush();
            }
            else {
                HashMap<Long, HashSet<String>> contents = JSONUtils.jsonStringToChunkCache(new String(Files.readAllBytes(filename.toPath())));
                if (contents != null) {
                    fileRegions = contents;
                }
                else {
                    fileRegions = new HashMap<>();
                }
            }

            if (!fileRegions.equals(chunkPlayerClaimContainer)) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                logger.info("Saving chunk cache data");
                writer.write(objectToJsonString(chunkPlayerClaimContainer));
                writer.flush();
                writer.close();
            }

        } catch (IOException e) {
            logger.warning("Exception while saving player data!");
            e.printStackTrace();
        }
    }

    public static String getBaseFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
