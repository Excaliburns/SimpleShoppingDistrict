package tut.simpleshoppingdistrict.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tut.simpleshoppingdistrict.utils.JSONUtils;
import tut.simpleshoppingdistrict.utils.SSDCache;
import tut.simpleshoppingdistrict.utils.SSDLogger;
import tut.simpleshoppingdistrict.utils.SimpleShoppingDistrictItemsUtils;

import java.util.logging.Logger;

public class SSDBaseCommand implements CommandExecutor {
    // Constants ////////////////////////////////////////////////////
    private static final Logger logger = SSDLogger.getSSDLogger();

    // This class will be the base command for SimpleShoppingDistrict.

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Server) {
            logger.info("hey asdasd!");
        }

        if (sender instanceof Player) {
                final Player player = ((Player) sender).getPlayer();

                if (args.length == 0) {

                    if (player != null) {
                        player.getInventory().addItem(SimpleShoppingDistrictItemsUtils.SimpleShoppingDistrictTool());
                    }

                }
                else if (args.length == 1) {
                    String arg = args[0];

                    if (arg.equalsIgnoreCase("regions")) {
                        return SSDDisplayRegionsCommand.SSDRegionCommand(sender);
                    }

                    else if (arg.equalsIgnoreCase("save")) {
                        JSONUtils.saveCacheData(SSDCache.playerRegionCache);
                        return true;
                    }
                }

                return true;
        }

        return false;
    }
}
