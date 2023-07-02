package it.italiandudes.enhancedeconomy;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.ConfigModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.utils.Defs;
import it.italiandudes.enhancedeconomy.utils.Resource;
import it.italiandudes.enhancedeconomy.utils.ServerLogger;
import it.italiandudes.idl.common.StringHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public final class EnhancedEconomy extends JavaPlugin {

    // Attributes
    private boolean instanceErrored;
    private static JavaPlugin pluginInstance = null;

    // Plugin Startup
    @Override
    public void onEnable() {

        // Instance Errored Flag
        instanceErrored = false;
        pluginInstance = this;

        try {

            // Load Configs from Files
            loadConfigs(this);

            // Load Langs from Config's selected Lang
            loadLangs(this);

            // Connect to DB using configs and
            loadDB();

            ServerLogger.getLogger().info("All required modules are loaded, loading plugin modules...");

            // Load Commands
            CommandsModule.load(this);

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
        if (instanceErrored) return;
        instanceErrored = true;
        try {
            CommandsModule.unload(true);
        } catch (ModuleException ignored) {}
        try {
            DBConnectionModule.unload(true);
        } catch (ModuleException ignored) {}
        ServerLogger.getLogger().info("DB Connection Module Unload: Successful!");
        try {
            LocalizationModule.unload(true);
        } catch (ModuleException ignored) {}
        ServerLogger.getLogger().info("Localization Module Unload: Successful!");
        try {
            ConfigModule.unload(true);
        } catch (ModuleException ignored) {}
        ServerLogger.getLogger().info("Config Module Unload: Successful!");
    }

    // Instance Getter
    @NotNull
    public static JavaPlugin getPluginInstance() {
        return pluginInstance;
    }

    // Methods
    private void loadConfigs(@NotNull JavaPlugin pluginInstance) throws ModuleException {
        ConfigModule.load(pluginInstance);
    }
    private void loadLangs(@NotNull JavaPlugin pluginInstance) throws ModuleException {
        LocalizationModule.load(pluginInstance, Objects.requireNonNull(ConfigModule.getConfig(Defs.Config.Identifiers.GENERAL_CONFIG, Defs.Config.Keys.General.KEY_LANG)));
    }
    private void loadDB() throws ModuleException {
        DBConnectionModule.load(Objects.requireNonNull(ConfigModule.getConfig(Defs.Config.Identifiers.GENERAL_CONFIG, Defs.Config.Keys.General.KEY_DATABASE_URL)));
        String dbType = DBConnectionModule.getConnectorType();
        String query = DBConnectionModule.getQueryFromResourcesFileSQL(Resource.Path.DBConnection.SQL_DIR+dbType+Resource.Path.DBConnection.SQL_FILE_EXTENSION);
        PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
        try {
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            throw new ModuleException("DB Creation failed", e);
        }
    }
}
