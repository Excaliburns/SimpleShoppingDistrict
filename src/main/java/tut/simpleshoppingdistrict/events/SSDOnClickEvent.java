package tut.simpleshoppingdistrict.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import tut.simpleshoppingdistrict.utils.SimpleShoppingDistrictItemsUtils;

public class SSDOnClickEvent implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem() != null)
            if (event.getItem().isSimilar(SimpleShoppingDistrictItemsUtils.SimpleShoppingDistrictTool())) {

            }
    }
}
