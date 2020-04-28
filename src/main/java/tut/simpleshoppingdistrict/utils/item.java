package tut.simpleshoppingdistrict.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class item extends ItemStack {
    public item() {
        this.setType(Material.CARROT);
        this.setAmount(1);

        ItemMeta toolMeta = this.getItemMeta();

        toolMeta.setDisplayName(ChatColor.AQUA + "SimpleShoppingDistrict Tool");
        toolMeta.setUnbreakable(true);
        toolMeta.
        this.setItemMeta(toolMeta);
    }
}
