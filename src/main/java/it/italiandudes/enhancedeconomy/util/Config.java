package it.italiandudes.enhancedeconomy.util;

import it.italiandudes.enhancedeconomy.EnhancedEconomy;
import it.italiandudes.idl.common.JarHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("unused")
public final class Config {

    // Attributes
    private static JSONObject generalConfigFile = null;

    // Config Files
    public static final class Files {
        public static final String GENERAL_CONFIG = "general";
    }

    // Config Keys
    public static final class Keys {
        public static final String LANG_KEY = "language_pack";
    }

    // Config Init
    public static void init(final JavaPlugin pluginInstance) throws IOException {
        if (!pluginInstance.getDataFolder().exists()) {
            //noinspection ResultOfMethodCallIgnored
            pluginInstance.getDataFolder().mkdirs();
        }
        JarHandler.copyDirectoryFromJar(new File(EnhancedEconomy.PLUGIN_JAR_PATH), Resource.Path.Config.CONFIG_DIR, pluginInstance.getDataFolder(), false);

        // General Config File
        FileReader fileReader = new FileReader(pluginInstance.getDataFolder().getAbsolutePath()+Resource.Path.Config.GENERAL_CONFIG);
        try {
            generalConfigFile = (JSONObject) new JSONParser().parse(new FileReader(pluginInstance.getDataFolder().getAbsolutePath()+Resource.Path.Config.GENERAL_CONFIG));
            fileReader.close();
        } catch (Exception e) {
            generalConfigFile = null;
            fileReader.close();
            throw new IOException("Can't read config file \""+pluginInstance.getDataFolder().getAbsolutePath()+Resource.Path.Config.GENERAL_CONFIG+"\"!");
        }

    }
    @Nullable
    public static String getConfig(@NotNull final String CONFIG_TYPE, @NotNull final String KEY) {
        if (generalConfigFile == null) return null;
        switch (CONFIG_TYPE) {
            case Files.GENERAL_CONFIG -> {
                return (String) generalConfigFile.get(KEY);
            }
            default -> {
                return null;
            }
        }
    }
    public static void saveConfig(final JavaPlugin pluginInstance) {
        pluginInstance.saveConfig();
    }

}
