package tut.simpleshoppingdistrict.commands;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tut.simpleshoppingdistrict.data.SSDRegion;
import tut.simpleshoppingdistrict.utils.SSDCache;

import java.util.Map;
import java.util.TreeSet;

public class SSDDisplayRegionsCommand {
    public static boolean SSDRegionCommand(CommandSender sender) {
        if (sender instanceof Server) {
        }

        if (sender instanceof Player) {
            final Player player = ((Player) sender).getPlayer();

            if (player == null)
                return false;

            for (Map.Entry<String, TreeSet<SSDRegion>> entry : SSDCache.playerRegionCache.entrySet()) {
                player.sendMessage("Player - " + entry.getKey() + " has regions");

                for (SSDRegion region : entry.getValue()) {
                    player.sendMessage("Region " + region.getRegionID()+1 + " has bounds ");
                    player.sendMessage("Bound 1: " + region.getBound1Location().toString());
                    player.sendMessage("Bound 2: " + region.getBound2Location().toString());
                }
            }

            return true;
        }

        return false;
    }
}
