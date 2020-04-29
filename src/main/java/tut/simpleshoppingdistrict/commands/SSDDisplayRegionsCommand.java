package tut.simpleshoppingdistrict.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tut.simpleshoppingdistrict.data.SSDRegion;
import tut.simpleshoppingdistrict.utils.SSDCache;
import tut.simpleshoppingdistrict.utils.SimpleShoppingDistrictItemsUtils;

import java.util.ArrayList;
import java.util.Map;

public class SSDDisplayRegionsCommand {
    public static boolean SSDRegionCommand(CommandSender sender) {
        if (sender instanceof Server) {
        }

        if (sender instanceof Player) {
            final Player player = ((Player) sender).getPlayer();

            if (player == null)
                return false;

            for (Map.Entry<String, ArrayList<SSDRegion>> entry : SSDCache.playerRegionCache.entrySet()) {
                player.sendMessage("Player - " + entry.getKey() + " has regions");

                for (SSDRegion region : entry.getValue()) {
                    player.sendMessage("Region " + region.getRegionID() + " has bounds ");
                    player.sendMessage("Bound 1: " + region.getBound1().toString());
                    player.sendMessage("Bound 2: " + region.getBound2().toString());
                }
            }

            return true;
        }

        return false;
    }
}
