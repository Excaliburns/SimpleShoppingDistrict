package tut.simpleshoppingdistrict.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tut.simpleshoppingdistrict.utils.SimpleShoppingDistrictItemsUtils;

public class SSDBaseCommand implements CommandExecutor {

    // This class will be the base command for SimpleShoppingDistrict.

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Server) {
        }

        if (sender instanceof Player) {
                final Player player = ((Player) sender).getPlayer();

                if (player != null) {
                    player.getInventory().addItem(SimpleShoppingDistrictItemsUtils.SimpleShoppingDistrictTool());
                }

                return true;
        }

        return false;
    }
}