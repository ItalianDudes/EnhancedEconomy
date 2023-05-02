package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleAlreadyLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleLoadingException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleNotLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleReloadingException;
import it.italiandudes.enhancedeconomy.util.Resource;
import it.italiandudes.enhancedeconomy.util.ServerLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("unused")
public final class LocalizationModule {

    // Attributes
    private static JSONObject langFile = null;
    private static boolean areLangsLoading = false;

    // Default Constructor
    public LocalizationModule() {
        throw new RuntimeException("Can't instantiate this class!");
    }

    // Module Checker
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isModuleLoaded() {
        return langFile != null;
    }

    // Methods
    public synchronized static void load(@NotNull final String LOCALIZATION) throws ModuleException {
        load(LOCALIZATION, false);
    }
    public synchronized static void load(@NotNull final String LOCALIZATION, final boolean disableLog) throws ModuleException {

        if (areLangsLoading) {
            if (!disableLog) ServerLogger.getLogger().warn("Localization Module Load: Canceled! (Reason: Another thread is executing a langs loading command)");
            throw new ModuleLoadingException("Localization Module Load: Canceled! (Reason: Another thread is executing a langs loading command)");
        }
        if (isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().error("Localization Module Load: Failed! (Reason: the module has already been loaded)");
            throw new ModuleAlreadyLoadedException("Localization Module Load: Failed! (Reason: the module has already been loaded)");
        }

        areLangsLoading = true;

        InputStreamReader streamReader;

        InputStream resourceStream = Resource.getResourceAsStream(Resource.Path.Localization.LOCALIZATION_DIR+LOCALIZATION+Resource.Path.Localization.LANG_FILE_EXTENSION);
        if (resourceStream == null) {
            areLangsLoading = false;
            if (!disableLog) ServerLogger.getLogger().error("Localization Module Load: Failed! (Reason: the lang resource can't be located)");
            throw new ModuleAlreadyLoadedException("Localization Module Load: Failed! (Reason: the lang resource can't be located)");
        }

        try {
            streamReader = new InputStreamReader(resourceStream);
            langFile = (JSONObject) new JSONParser().parse(streamReader);
            streamReader.close();
        } catch (IOException | ParseException e) {
            areLangsLoading = false;
            if (!disableLog) ServerLogger.getLogger().error("Localization Module Load: Failed! (Reason: an error has occurred on localization file reading/parsing)");
            throw new ModuleLoadingException("Localization Module Load: Failed! (Reason: an error has occurred on localization file reading/parsing)");
        }

        if (!disableLog) ServerLogger.getLogger().info("Localization Module Load: Successful!");
        areLangsLoading = false;
    }
    public synchronized static void unload() throws ModuleException {
        unload(false);
    }
    public synchronized static void unload(final boolean disableLog) throws ModuleException {

        if (areLangsLoading) {
            if (!disableLog) ServerLogger.getLogger().warn("Localization Module Unload: Canceled! (Reason: Another thread is executing a langs loading command)");
            throw new ModuleLoadingException("Localization Module Unload: Canceled! (Reason: Another thread is executing a langs loading command)");
        }
        if (!isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().error("Localization Module Unload: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("Localization Module Unload: Failed! (Reason: the module isn't loaded)");
        }

        langFile = null;
        if (!disableLog) ServerLogger.getLogger().info("Localization Module Unload: Successful!");
    }
    public synchronized static void reload(@NotNull final String LOCALIZATION) throws ModuleException {
        reload(LOCALIZATION, false);
    }
    public synchronized static void reload(@NotNull final String LOCALIZATION, final boolean disableLog) throws ModuleException {

        if (areLangsLoading) {
            if (!disableLog) ServerLogger.getLogger().warn("Localization Module Reload: Canceled! (Reason: Another thread is executing a langs loading command)");
            throw new ModuleLoadingException("Localization Module Reload: Canceled! (Reason: Another thread is executing a langs loading command)");
        }
        if (!isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().error("Localization Module Reload: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("Localization Module Reload: Failed! (Reason: the module isn't loaded)");
        }

        // Do current lang backup
        JSONObject langFileBACKUP = langFile;

        try {
            unload(true);
        } catch (ModuleException e) {
            if (!disableLog) ServerLogger.getLogger().error("Localization Module Reload: Failed! (Reason: the unload routine has failed)");

            // Put lang backup online again
            langFile= langFileBACKUP;

            throw new ModuleReloadingException("Localization Module Reload: Failed! (Reason: the unload routine has failed)", e);
        }

        try {
            load(LOCALIZATION, true);
        } catch (ModuleException e) {
            if (!disableLog) ServerLogger.getLogger().error("Localization Module Reload: Failed! (Reason: the load routine has failed)");

            // Put lang backup online again
            langFile= langFileBACKUP;

            throw new ModuleReloadingException("Localization Module Reload: Failed! (Reason: the load routine has failed)", e);
        }

        if (!disableLog) ServerLogger.getLogger().info("Localization Module Reload: Successful!");
    }
    @Nullable
    public synchronized static String translate(@NotNull final String KEY) throws ModuleException {

        if (areLangsLoading) {
            ServerLogger.getLogger().warn("Translate Operation: Canceled! (Reason: Another thread is executing a langs loading command)");
            throw new ModuleLoadingException("Translate Operation: Canceled! (Reason: Another thread is executing a langs loading command)");
        }
        if (!isModuleLoaded()) {
            ServerLogger.getLogger().error("Translate Operation: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("Translate Operation: Failed! (Reason: the module isn't loaded)");
        }

        return (String) langFile.get(KEY);
    }
}
