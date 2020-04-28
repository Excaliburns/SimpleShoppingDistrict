package tut.simpleshoppingdistrict.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SimpleShoppingDistrictItemsUtils {

    public static ItemStack SimpleShoppingDistrictSelector(int position) {
        return new ItemStack(Material.BIRCH_BOAT);
    }

    public static ItemStack SimpleShoppingDistrictTool() {
        ItemStack tool = new ItemStack(Material.CARROT, 1);
        ItemMeta toolMeta = tool.getItemMeta();

        toolMeta.setDisplayName(ChatColor.AQUA + "SimpleShoppingDistrict Tool");

        tool.setItemMeta(toolMeta);
        return tool;
    }

    //public static ItemMeta SSDToolMeta() {
    //}
}
