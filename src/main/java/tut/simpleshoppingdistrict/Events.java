package tut.simpleshoppingdistrict;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import tut.simpleshoppingdistrict.data.Point;
import tut.simpleshoppingdistrict.data.SSDRegion;
import tut.simpleshoppingdistrict.utils.SSDCache;
import tut.simpleshoppingdistrict.utils.SSDConstants;
import tut.simpleshoppingdistrict.utils.SSDLogger;
import tut.simpleshoppingdistrict.utils.SimpleShoppingDistrictItemsUtils;

import java.util.logging.Logger;

public class Events implements Listener {
    // Constants //////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final Logger logger       = SSDLogger.getSSDLogger();
    private static final boolean isDebugMode = SSDConstants.PLUGIN_DEBUG_MODE;

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        // Get the item of the event and make sure it exists
        if (event.getItem() != null)
            // If the item is a SimpleShoppingDistrictTool
            if (event.getItem().isSimilar(SimpleShoppingDistrictItemsUtils.SimpleShoppingDistrictTool())) {

                //If it's air, do nothing
                if ((event.getClickedBlock() != null && event.getClickedBlock().isEmpty()) || event.getClickedBlock() == null) {
                    logger.warning("Clicked block was air... but also solid.");
                } else {
                    String UUID = event.getPlayer().getUniqueId().toString();

                    if (SSDCache.playerDrawingRegionCache.containsKey(UUID)) {
                        boolean isPlayerDrawingRegion = SSDCache.playerDrawingRegionCache.get(UUID);

                        if (isPlayerDrawingRegion) {
                            SSDRegion regionInProgress;

                            if (SSDCache.regionInProgressCache.containsKey(UUID)) {
                                regionInProgress = SSDCache.regionInProgressCache.get(UUID);

                                if (regionInProgress.isCompleteRegion()) {
                                    if (isDebugMode) {
                                        logger.warning("Player " + event.getPlayer().getName() + "tried to draw a complete region. ID = " + regionInProgress.getRegionID());
                                    }
                                } else {
                                    regionInProgress.setBound2(new Point(event.getClickedBlock().getLocation()));
                                    event.getPlayer().sendMessage(ChatColor.AQUA + "Set Bound 2!");

                                    regionInProgress.setCompleteRegion(true);
                                    event.getPlayer().sendMessage(ChatColor.AQUA + "Region complete!");

                                    SSDCache.finishDrawingRegion(UUID, regionInProgress);
                                }
                            } else {
                                if (isDebugMode) {
                                    logger.warning("Player" + event.getPlayer().getName() + "was drawing region but cache does not contain their UUID.");
                                }
                            }


                        } else {
                            initializeDrawingBounds(event);
                        }
                    } else {
                        initializeDrawingBounds(event);
                    }
                }
            }
    }

    private void initializeDrawingBounds(PlayerInteractEvent event) {
        event.getPlayer().sendMessage(ChatColor.AQUA + "Starting Drawing Bounds");
        String UUID = event.getPlayer().getUniqueId().toString();
        int nextRegionID;

        if (SSDCache.playerRegionCache.containsKey(UUID)) {
            nextRegionID = SSDCache.playerRegionCache.get(UUID).last().getRegionID() + 1;
        } else {
            nextRegionID = 0;
        }


        //Make new SSD Region that we put in the cache of all regions
        SSDRegion region;
        Location location;
        if (event.getClickedBlock()!= null) {
            region = new SSDRegion(event.getClickedBlock().getWorld(), nextRegionID);
            location = event.getClickedBlock().getLocation();
            region.setBound1(new Point(location));
            event.getPlayer().sendMessage(ChatColor.AQUA + "Set Bound 1!");


            SSDCache.playerDrawingRegionCache.put(UUID, true);
            SSDCache.regionInProgressCache.put(UUID, region);
        } else {
            logger.warning("Clicked block was null after being passed to drawingBounds! How?");
        }
    }
}
