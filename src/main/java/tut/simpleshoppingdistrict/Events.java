package tut.simpleshoppingdistrict;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitScheduler;
import tut.simpleshoppingdistrict.data.Point;
import tut.simpleshoppingdistrict.data.SSDRegion;
import tut.simpleshoppingdistrict.runnables.SpawnParticleRunnable;
import tut.simpleshoppingdistrict.utils.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Events implements Listener {
    private static BukkitScheduler scheduler = SimpleShoppingDistrict.getPlugin(SimpleShoppingDistrict.class).getServer().getScheduler();


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

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Chunk chunk = event.getBlock().getChunk();
        long chunkHash = SSDUtils.getChunkHash(chunk.getX(), chunk.getZ());

        if (SSDCache.chunkClaimCache.containsKey(chunkHash)) {
            Set<SSDRegion> regionList = SSDCache.chunkClaimCache.get(chunkHash);

            for (SSDRegion region : regionList) {
                final boolean brokenBlockWithinRegion = event.getBlock().getLocation().toVector()
                                                             .isInAABB(
                                                                     SSDUtils.getMinLocationFromTwoPoints(region.getBound1(), region.getBound2(), event.getBlock().getWorld()).toVector(),
                                                                     SSDUtils.getMaxLocationFromTwoPoints(region.getBound1(), region.getBound2(), event.getBlock().getWorld()).toVector()
                                                             );
                if (brokenBlockWithinRegion) {
                    event.setCancelled(true);

                    final Location[] regionCube = ParticleUtils.getCuboidPointsFromRegionBounds(region.getBound1(), region.getBound2(), event.getBlock().getWorld());

                    final SpawnParticleRunnable runnable = new SpawnParticleRunnable(event.getPlayer(), SimpleShoppingDistrict.getInstance(), regionCube);
                    runnable.start();
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent event){
        Long hash = SSDUtils.getChunkHash(event.getChunk());
        HashSet<String> playersThatHaveClaimsInRegion = SSDCache.chunkHashToPlayerIds.get(hash);

        if (playersThatHaveClaimsInRegion != null && !playersThatHaveClaimsInRegion.isEmpty()) {
            HashSet<SSDRegion> playerClaimsInRegion = new HashSet<>();

            for (String pUuid : playersThatHaveClaimsInRegion) {
                playerClaimsInRegion.addAll(
                        SSDCache.playerRegionCache.get(pUuid)
                                                  .stream()
                                                  .filter(region -> region.getChunkContainerHash().contains(hash))
                                                  .collect(Collectors.toList())
                );
            }

            if (!SSDCache.chunkClaimCache.containsKey(hash)) {
                SSDCache.chunkClaimCache.put(hash, playerClaimsInRegion);
            }
            else {
                playerClaimsInRegion.addAll(SSDCache.chunkClaimCache.get(hash));
                SSDCache.chunkClaimCache.put(hash, playerClaimsInRegion);
            }

        }
    }

    @EventHandler
    public void onChunkUnload(final ChunkUnloadEvent event){
        SSDCache.chunkClaimCache.remove(SSDUtils.getChunkHash(event.getChunk()));
    }


    private void initializeDrawingBounds(PlayerInteractEvent event) {
        event.getPlayer().sendMessage(ChatColor.AQUA + "Starting Drawing Bounds");
        String UUID = event.getPlayer().getUniqueId().toString();
        int nextRegionID;

        if (SSDCache.playerRegionCache.containsKey(UUID)) {
            nextRegionID = SSDCache.playerRegionCache.get(UUID).first().getRegionID() + 1;
        } else {
            nextRegionID = 0;
        }


        //Make new SSD Region that we put in the cache of all regions
        SSDRegion region;
        Location location;
        if (event.getClickedBlock()!= null) {
            region = new SSDRegion(nextRegionID);
            location = event.getClickedBlock().getLocation();
            region.setBound1(new Point(location));
            region.setWorld(event.getClickedBlock().getWorld().getName());
            region.addChunkToContainerHashList(
                    SSDUtils.getChunkHash(location.getChunk().getX(), location.getChunk().getZ())
            );

            event.getPlayer().sendMessage(ChatColor.AQUA + "Set Bound 1!");


            SSDCache.playerDrawingRegionCache.put(UUID, true);
            SSDCache.regionInProgressCache.put(UUID, region);
        } else {
            logger.warning("Clicked block was null after being passed to drawingBounds! How?");
        }
    }
}
