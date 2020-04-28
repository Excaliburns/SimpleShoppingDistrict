package tut.simpleshoppingdistrict;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import tut.simpleshoppingdistrict.utils.SSDLogger;
import tut.simpleshoppingdistrict.utils.SimpleShoppingDistrictItemsUtils;

import java.util.logging.Logger;

public class Events implements Listener {

    Logger logger = SSDLogger.getSSDLogger();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        // Get the item of the event and make sure it exists
        if (event.getItem() != null)
            // If the item is a SimpleShoppingDistrictTool
            if (event.getItem().isSimilar(SimpleShoppingDistrictItemsUtils.SimpleShoppingDistrictTool())) {

                //If it's air, do nothing
                if ((event.getClickedBlock() != null && event.getClickedBlock().isEmpty()) || event.getClickedBlock() == null) {
                    logger.info("Interact block was empty or null.");
                } else {
                    event.getPlayer().sendMessage(ChatColor.AQUA + "Interacted with a block!");
                }
            }
    }
}
