package it.italiandudes.enhancedeconomy.util;

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

    // Default Constructor
    public Config() {
        throw new RuntimeException("Can't instantiate this class!");
    }

    // Config Init
    public synchronized static void init(final JavaPlugin pluginInstance) throws IOException {
        if (!pluginInstance.getDataFolder().exists()) {
            //noinspection ResultOfMethodCallIgnored
            pluginInstance.getDataFolder().mkdirs();
        }

        JarHandler.copyDirectoryFromJar(new File(Defs.PluginInfo.PLUGIN_JAR_PATH), Resource.Path.Config.CONFIG_DIR, pluginInstance.getDataFolder(), false, false);

        // General Config File
        FileReader fileReader = new FileReader(pluginInstance.getDataFolder().getAbsolutePath()+Resource.Path.Config.GENERAL_CONFIG);
        try {
            generalConfigFile = (JSONObject) new JSONParser().parse(new FileReader(pluginInstance.getDataFolder().getAbsolutePath()+Resource.Path.Config.GENERAL_CONFIG));
            fileReader.close();
        } catch (Exception e) {
            fileReader.close();
            throw new IOException("Can't read config file \""+pluginInstance.getDataFolder().getAbsolutePath()+Resource.Path.Config.GENERAL_CONFIG+"\"!");
        }

    }
    public synchronized static void reload(final JavaPlugin pluginInstance) throws IOException {
        try {
            init(pluginInstance);
        } catch (IOException e) {
            throw new IOException("Config reload failed! No changes were applied.", e);
        }
    }
    public synchronized static void unload() {
        generalConfigFile = null;
    }
    @Nullable
    public synchronized static String getConfig(@NotNull final String CONFIG_TYPE, @NotNull final String KEY) {
        if (generalConfigFile == null) return null;
        //noinspection SwitchStatementWithTooFewBranches
        switch (CONFIG_TYPE) {
            case Defs.Config.Identifiers.GENERAL_CONFIG -> {
                return (String) generalConfigFile.get(KEY);
            }
            default -> {
                return null;
            }
        }
    }
}
