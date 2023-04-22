package it.italiandudes.enhancedeconomy;

import it.italiandudes.enhancedeconomy.util.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class EnhancedEconomy extends JavaPlugin {

    // Plugin Startup
    @Override
    public void onEnable() {
        try {

            // Load Configs from Files
            loadConfigs(this);

            // Load Langs from Config's selected Lang
            loadLangs(this);

        }catch (Exception e) {
            ServerLogger.getLogger().severe("An unhandled exception has reached the function, shutting down the plugin...");
            onDisable();
        }
    }

    // Plugin Shutdown
    @Override
    public void onDisable() {
        DBConnection.closeConnection();
        Localization.unload();
        Config.unload();
    }

    // Methods
    private void loadConfigs(@NotNull JavaPlugin pluginInstance) throws Exception {
        Config.init(pluginInstance);
    }
    private void loadLangs(@NotNull JavaPlugin pluginInstance) throws Exception {
        Localization.init(pluginInstance);
        Localization.load(pluginInstance, Config.getConfig(Defs.Config.Identifiers.GENERAL_CONFIG, Defs.Config.Keys.General.LANG_KEY));
    }
}
