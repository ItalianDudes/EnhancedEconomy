package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.commands.TestCommand;
import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleAlreadyLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleLoadingException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleNotLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleReloadingException;
import it.italiandudes.enhancedeconomy.utils.ServerLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
public final class CommandsModule {

    // Attributes
    private static boolean isModuleLoaded = false;
    private static boolean areCommandsLoading = false;

    // Default Constructor
    public CommandsModule() {
        throw new RuntimeException("Can't instantiate this class!");
    }

    // Module Checker
    public static boolean isModuleLoaded() {
        return isModuleLoaded;
    }

    // Methods
    public synchronized static void load(@NotNull final JavaPlugin pluginInstance) throws ModuleException {
        load(pluginInstance, false);
    }
    private synchronized static void load(@NotNull final JavaPlugin pluginInstance, final boolean disableLog) throws ModuleException {

        if (areCommandsLoading) {
            if (!disableLog) ServerLogger.getLogger().warning("Commands Module Load: Canceled! (Reason: Another thread is executing a commands loading command)");
            throw new ModuleLoadingException("Commands Module Load: Canceled! (Reason: Another thread is executing a commands loading command)");
        }
        if (isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().severe("Commands Module Load: Failed! (Reason: the module has already been loaded)");
            throw new ModuleAlreadyLoadedException("Commands Module Load: Failed! (Reason: the module has already been loaded)");
        }

        areCommandsLoading = true;

        // List of commands here...
        try {
            Objects.requireNonNull(pluginInstance.getCommand(TestCommand.COMMAND_NAME)).setExecutor(new TestCommand());
        } catch (Exception e) {
            areCommandsLoading = false;
            if (!disableLog) ServerLogger.getLogger().severe("Commands Module Load: Failed! (Reason: an error has occurred on module loading)");
            throw new ModuleLoadingException("Commands Module Load: Failed! (Reason: an error has occurred on module loading)", e);
        }

        areCommandsLoading = false;
        isModuleLoaded = true;
        if (!disableLog) ServerLogger.getLogger().info("Commands Module Load: Successful!");
    }
    public synchronized static void unload() throws ModuleException {
        unload(false);
    }
    public synchronized static void unload(final boolean disableLog) throws ModuleException {

        if (areCommandsLoading) {
            if (!disableLog) ServerLogger.getLogger().warning("Commands Module Unload: Canceled! (Reason: Another thread is executing a commands loading command)");
            throw new ModuleLoadingException("Commands Module Unload: Canceled! (Reason: Another thread is executing a commands loading command)");
        }
        if (!isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().severe("Commands Module Unload: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("Commands Module Unload: Failed! (Reason: the module isn't loaded)");
        }

        isModuleLoaded = false;

        if(!disableLog) ServerLogger.getLogger().info("Commands Module Unload: Successful!");
    }
    public synchronized static void reload(@NotNull final JavaPlugin pluginInstance) throws Exception {

        if (areCommandsLoading) {
            ServerLogger.getLogger().warning("Commands Module Reload: Canceled! (Reason: Another thread is executing a commands loading command)");
            throw new ModuleLoadingException("Commands Module Reload: Canceled! (Reason: Another thread is executing a commands loading command)");
        }
        if (!isModuleLoaded()) {
            ServerLogger.getLogger().severe("Commands Module Reload: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("Commands Module Reload: Failed! (Reason: the module isn't loaded)");
        }

        try {
            unload(true);
        } catch (ModuleException e) {
            ServerLogger.getLogger().severe("Commands Module Reload: Failed! (Reason: the unload routine has failed)");
            throw new ModuleReloadingException("Commands Module Reload: Failed! (Reason: the unload routine has failed)", e);
        }

        try {
            load(pluginInstance, true);
        } catch (ModuleException e) {
            ServerLogger.getLogger().severe("Commands Module Reload: Failed! (Reason: the load routine has failed)");
            throw new ModuleReloadingException("Commands Module Reload: Failed! (Reason: the load routine has failed)", e);
        }

        ServerLogger.getLogger().info("Commands Module Reload: Successful!");
    }
}
