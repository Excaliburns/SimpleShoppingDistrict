package tut.simpleshoppingdistrict;

import org.bukkit.plugin.java.JavaPlugin;
import tut.simpleshoppingdistrict.commands.SSDBaseCommand;
import tut.simpleshoppingdistrict.utils.JSONUtils;
import tut.simpleshoppingdistrict.utils.SSDCache;

import java.util.logging.Logger;

public class SimpleShoppingDistrict extends JavaPlugin {
    public Logger logger = this.getLogger();

    //This runs after program has ben loaded and before it has been enabled.
    @Override
    public void onLoad() {
        logger.info("SimpleShoppingDistrict has loaded!");
        SSDCache.initCaches();
    }


    // After program enabled
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Events(), this);

        // Add the SSDBaseCommand as the Executor to the command "ssd"
        this.getCommand("ssd").setExecutor(new SSDBaseCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        JSONUtils.saveCacheData(SSDCache.playerRegionCache);
    }
}
