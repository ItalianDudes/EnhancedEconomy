package it.italiandudes.enhancedeconomy;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.Config;
import it.italiandudes.enhancedeconomy.modules.DBConnection;
import it.italiandudes.enhancedeconomy.modules.Localization;
import it.italiandudes.enhancedeconomy.utils.*;
import it.italiandudes.idl.common.StringHandler;
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

            // Connect to DB using configs and
            loadDB();

        }catch (Exception e) {
            ServerLogger.getLogger().severe("An unhandled exception has reached the function, shutting down the plugin...");
            ServerLogger.getLogger().severe("Exception stacktrace:");
            ServerLogger.getLogger().severe(StringHandler.getStackTrace(e));
            onDisable();
        }
    }

    // Plugin Shutdown
    @Override
    public void onDisable() {
        try {
            DBConnection.unload(true);
        } catch (ModuleException ignored) {}
        ServerLogger.getLogger().info("DB Connection Module Unload: Successful!");
        try {
            Localization.unload(true);
        } catch (ModuleException ignored) {}
        ServerLogger.getLogger().info("Localization Module Unload: Successful!");
        try {
            Config.unload(true);
        } catch (ModuleException ignored) {}
        ServerLogger.getLogger().info("Config Module Unload: Successful!");
    }

    // Methods
    private void loadConfigs(@NotNull JavaPlugin pluginInstance) throws ModuleException {
        Config.load(pluginInstance);
    }
    private void loadLangs(@NotNull JavaPlugin pluginInstance) throws ModuleException {
        Localization.load(pluginInstance, Objects.requireNonNull(Config.getConfig(Defs.Config.Identifiers.GENERAL_CONFIG, Defs.Config.Keys.General.KEY_LANG)));
    }
    private void loadDB() throws ModuleException {
        DBConnection.load(Objects.requireNonNull(Config.getConfig(Defs.Config.Identifiers.GENERAL_CONFIG, Defs.Config.Keys.General.KEY_DATABASE_URL)));
        String query = DBConnection.getQueryFromSQL(Resource.Path.DBConnection.DATABASE_QUERY_PATH);
        DBConnection.executePreparedStatementFromQueryIgnoreResult(query);
    }
}
