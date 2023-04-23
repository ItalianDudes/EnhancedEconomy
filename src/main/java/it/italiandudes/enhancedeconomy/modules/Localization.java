package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleAlreadyLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleLoadingException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleNotLoadedException;
import it.italiandudes.enhancedeconomy.utils.Defs;
import it.italiandudes.enhancedeconomy.utils.Resource;
import it.italiandudes.enhancedeconomy.utils.ServerLogger;
import it.italiandudes.idl.common.JarHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

@SuppressWarnings("unused")
public final class Localization {

    // Attributes
    private static JSONObject langFile = null;
    private static boolean areLangsLoading = false;

    // Default Constructor
    public Localization() {
        throw new RuntimeException("Can't instantiate this class!");
    }

    // Module Checker
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isModuleLoaded() {
        return langFile != null;
    }

    // Localized String Getter
    public synchronized static void load(@NotNull final JavaPlugin pluginInstance, @NotNull final String LOCALIZATION) throws ModuleException {
        load(pluginInstance, LOCALIZATION, false);
    }
    private synchronized static void load(@NotNull final JavaPlugin pluginInstance, @NotNull final String LOCALIZATION, boolean disableLog) throws ModuleException {

        if (areLangsLoading) {
            if (!disableLog) ServerLogger.getLogger().warning("Localization Module Load: Canceled! (Reason: Another thread is executing a langs loading command)");
            throw new ModuleLoadingException("Localization Module Load: Canceled! (Reason: Another thread is executing a langs loading command)");
        }
        if (isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().severe("Localization Module Load: Failed! (Reason: the module has already been loaded)");
            throw new ModuleAlreadyLoadedException("Localization Module Load: Failed! (Reason: the module has already been loaded)");
        }

        areLangsLoading = true;

        if (!new File(pluginInstance.getDataFolder().getAbsolutePath()+Resource.Path.Localization.LOCALIZATION_DIR).exists()) {
            try {
                if (!pluginInstance.getDataFolder().exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    pluginInstance.getDataFolder().mkdirs();
                }
                JarHandler.copyDirectoryFromJar(new File(Defs.PluginInfo.PLUGIN_JAR_PATH), Resource.Path.Localization.LOCALIZATION_DIR, pluginInstance.getDataFolder(), false, false);
            } catch (IOException e) {
                areLangsLoading = false;
                if (!disableLog) ServerLogger.getLogger().severe("Localization Module Load: Failed! (Reason: Localization file copy failed)");
                throw new ModuleLoadingException("Localization Module Load: Failed! (Reason: Localization file copy failed)");
            }
        }

        File filepath = new File(pluginInstance.getDataFolder().getAbsolutePath()+Resource.Path.Localization.LOCALIZATION_DIR+LOCALIZATION+".json");
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(filepath);
            langFile = (JSONObject) new JSONParser().parse(fileReader);
            fileReader.close();
        } catch (IOException | ParseException e) {
            areLangsLoading = false;
            try {
                if (fileReader != null) fileReader.close();
            } catch (Exception ignored){}
            if (!disableLog) ServerLogger.getLogger().severe("Localization Module Load: Failed! (Reason: an error has occurred on localization file reading/parsing)");
            throw new ModuleLoadingException("Localization Module Load: Failed! (Reason: an error has occurred on localization file reading/parsing)");
        }

        if (!disableLog) ServerLogger.getLogger().info("Localization Module Load: Successful!");
        areLangsLoading = false;
    }
    public synchronized static void unload() throws ModuleException {
        unload(false);
    }
    public synchronized static void unload(boolean disableLog) throws ModuleException {

        if (areLangsLoading) {
            if (!disableLog) ServerLogger.getLogger().warning("Localization Module Unload: Canceled! (Reason: Another thread is executing a langs loading command)");
            throw new ModuleLoadingException("Localization Module Unload: Canceled! (Reason: Another thread is executing a langs loading command)");
        }
        if (!isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().severe("Localization Module Unload: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("Localization Module Unload: Failed! (Reason: the module isn't loaded)");
        }

        langFile = null;
        if (!disableLog) ServerLogger.getLogger().info("Localization Module Unload: Successful!");
    }
    public synchronized static void reload(@NotNull final JavaPlugin pluginInstance, final String LOCALIZATION) throws ModuleException {

        if (areLangsLoading) {
            ServerLogger.getLogger().warning("Localization Module Reload: Canceled! (Reason: Another thread is executing a langs loading command)");
            throw new ModuleLoadingException("Localization Module Reload: Canceled! (Reason: Another thread is executing a langs loading command)");
        }
        if (!isModuleLoaded()) {
            ServerLogger.getLogger().severe("Localization Module Reload: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("Localization Module Reload: Failed! (Reason: the module isn't loaded)");
        }

        // Do current lang backup
        JSONObject langFileBACKUP = langFile;

        unload();

        try {
            load(pluginInstance, LOCALIZATION, true);
        } catch (ModuleException e) {
            ServerLogger.getLogger().severe("Localization Module Reload: Failed! (Reason: the load routine has failed)");

            // Put lang backup online again
            langFile= langFileBACKUP;

            throw new ModuleLoadingException("Localization Module Reload: Failed! (Reason: the load routine has failed)", e);
        }

        ServerLogger.getLogger().info("Localization Module Reload: Successful!");
    }
    @Nullable
    public synchronized static String translate(final String KEY) throws ModuleException {

        if (areLangsLoading) {
            ServerLogger.getLogger().warning("Translate Operation: Canceled! (Reason: Another thread is executing a langs loading command)");
            throw new ModuleLoadingException("Translate Operation: Canceled! (Reason: Another thread is executing a langs loading command)");
        }
        if (!isModuleLoaded()) {
            ServerLogger.getLogger().severe("Translate Operation: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("Translate Operation: Failed! (Reason: the module isn't loaded)");
        }

        return (String) langFile.get(KEY);
    }
}
