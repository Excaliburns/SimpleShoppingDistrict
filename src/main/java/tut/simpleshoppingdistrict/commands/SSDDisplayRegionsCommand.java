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
                    sender.sendMessage("Region " + region.getRegionID()+1 + " has bounds ");
                    sender.sendMessage("Bound 1: " + region.getBound1Location().toString());
                    sender.sendMessage("Bound 2: " + region.getBound2Location().toString());
                }
            }

            return true;
    }
}
