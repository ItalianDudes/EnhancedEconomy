package it.italiandudes.enhancedeconomy;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.Config;
import it.italiandudes.enhancedeconomy.modules.DBConnection;
import it.italiandudes.enhancedeconomy.modules.Localization;
import it.italiandudes.enhancedeconomy.utils.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class EnhancedEconomy extends JavaPlugin {

    // Plugin Startup
    @Override
    public void onEnable() {
        try {

            // Load Configs from Files
            loadConfigs(this);

            // Load Langs from Config's selected Lang
            loadLangs(this);

            ServerLogger.getLogger().info(Localization.translate(Defs.Localization.Keys.TEST_ENTRY));

        }catch (Exception e) {
            ServerLogger.getLogger().severe("An unhandled exception has reached the function, shutting down the plugin...");
            onDisable();
        }
    }

    // Plugin Shutdown
    @Override
    public void onDisable() {
        try {
            DBConnection.unload();
        } catch (ModuleException ignored) {}
        try {
            Localization.unload();
        } catch (ModuleException ignored) {}
        try {
            Config.unload();
        } catch (ModuleException ignored) {}
    }

    // Methods
    private void loadConfigs(@NotNull JavaPlugin pluginInstance) throws Exception {
        Config.load(pluginInstance);
    }
    private void loadLangs(@NotNull JavaPlugin pluginInstance) throws Exception {
        Localization.load(pluginInstance, Objects.requireNonNull(Config.getConfig(Defs.Config.Identifiers.GENERAL_CONFIG, Defs.Config.Keys.General.LANG_KEY)));
    }
}
