package it.italiandudes.enhancedeconomy;

import it.italiandudes.enhancedeconomy.util.Config;
import it.italiandudes.enhancedeconomy.util.Localization;
import it.italiandudes.enhancedeconomy.util.ServerLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public final class EnhancedEconomy extends JavaPlugin {

    // Attributes
    public static final String PLUGIN_JAR_PATH;
    static {
        try {
            PLUGIN_JAR_PATH = new File(EnhancedEconomy.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // Plugin Startup
    @Override
    public void onEnable() {
        try {
            try {
                Config.init(this);
            } catch (IOException e) {
                ServerLogger.getLogger().severe("Can't load config files!");
                onDisable();
            }
            try {
                Localization.init(this);
                Localization.load(this, Config.getConfig(Config.Files.GENERAL_CONFIG, Config.Keys.LANG_KEY));
            } catch (Exception e) {
                ServerLogger.getLogger().severe("Can't load lang file!");
                onDisable();
            }


        }catch (Exception e) {
            ServerLogger.getLogger().severe("An unhandled exception has reached the function, shutting down the plugin...");
            onDisable();
        }
    }

    // Plugin Shutdown
    @Override
    public void onDisable() {
    }
}
