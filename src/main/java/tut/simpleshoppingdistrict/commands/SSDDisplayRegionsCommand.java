package tut.simpleshoppingdistrict.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import tut.simpleshoppingdistrict.data.SSDRegion;
import tut.simpleshoppingdistrict.utils.SSDCache;

import java.util.Map;
import java.util.TreeSet;

public class SSDDisplayRegionsCommand {
    public static boolean SSDRegionCommand(CommandSender sender) {

            if (SSDCache.playerRegionCache.isEmpty()) {
                sender.sendMessage(ChatColor.AQUA + "Player region cache is empty.");
            }

            for (Map.Entry<String, TreeSet<SSDRegion>> entry : SSDCache.playerRegionCache.entrySet()) {
                sender.sendMessage("Player - " + entry.getKey() + " has regions");

                for (SSDRegion region : entry.getValue()) {
                    int regionId = (region.getRegionID() + 1);
                    sender.sendMessage("Region " + regionId + ": ");
                    String sb = String.format("Bound 1: %s\n", region.getBound1().toString())
                              + String.format("Bound 2: %s", region.getBound2().toString());
                    sender.sendMessage(sb);
                }
            }

            return true;
    }
}
