package tut.simpleshoppingdistrict;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import tut.simpleshoppingdistrict.commands.SSDBaseCommand;
import tut.simpleshoppingdistrict.utils.JSONUtils;
import tut.simpleshoppingdistrict.utils.SSDCache;
import tut.simpleshoppingdistrict.utils.SSDConstants;

import java.util.logging.Logger;

public class SimpleShoppingDistrict extends JavaPlugin {
    // Constants //////////////////////////////////////////////////////////////////////////////////////////////////////
    private final Logger logger              = this.getLogger();
    private static final boolean isDebugMode = SSDConstants.PLUGIN_DEBUG_MODE;

    //This runs after program has ben loaded and before it has been enabled.
    @Override
    public void onLoad() {
        logger.info("SimpleShoppingDistrict has loaded!");
        SSDCache.initCaches();
    }

    // After program enabled
    @Override
    public void onEnable() {
        //Load caches
        SSDCache.loadPlayerRegionCache();

        //Initialize task runnable to run every 2.5 minutes
        SSDCache.StartCacheSavingTimer(this);

        // Register plugin events
        getServer().getPluginManager().registerEvents(new Events(), this);

        // Add the SSDBaseCommand as the Executor to the command "ssd"
        PluginCommand command = this.getCommand("ssd");

        if (command != null) {
            command.setExecutor(new SSDBaseCommand());
        } else {
            logger.warning("Plugin.yml is incorrect. ssd command is not defined.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        JSONUtils.saveCacheData(SSDCache.playerRegionCache);
    }
}
