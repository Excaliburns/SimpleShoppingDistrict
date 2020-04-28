package tut.simpleshoppingdistrict;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SimpleShoppingDistrict extends JavaPlugin {
    public Logger logger = this.getLogger();

    @Override
    public void onLoad() {
        logger.info("SimpleShoppingDistrict has loaded!");
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
