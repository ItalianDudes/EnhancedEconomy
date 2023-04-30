package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.commands.EnhancedEconomyCommand;
import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.modules.LoadCommand;
import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.modules.ReloadCommand;
import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.modules.UnloadCommand;
import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleAlreadyLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleLoadingException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleNotLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleReloadingException;
import it.italiandudes.enhancedeconomy.utils.ServerLogger;
import org.bukkit.command.CommandExecutor;
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
            registerCommand(pluginInstance, EnhancedEconomyCommand.COMMAND_NAME, new EnhancedEconomyCommand());
            registerCommand(pluginInstance, LoadCommand.COMMAND_NAME, new LoadCommand());
            registerCommand(pluginInstance, UnloadCommand.COMMAND_NAME, new UnloadCommand());
            registerCommand(pluginInstance, ReloadCommand.COMMAND_NAME, new ReloadCommand());
        } catch (Exception e) {
            areCommandsLoading = false;
            if (!disableLog) ServerLogger.getLogger().severe("Commands Module Load: Failed! (Reason: an error has occurred on module loading)");
            throw new ModuleLoadingException("Commands Module Load: Failed! (Reason: an error has occurred on module loading)", e);
        }

        areCommandsLoading = false;
        isModuleLoaded = true;
        if (!disableLog) ServerLogger.getLogger().info("Commands Module Load: Successful!");
    }
    private synchronized static void registerCommand(@NotNull final JavaPlugin PLUGIN_INSTANCE, @NotNull final String COMMAND_NAME, @NotNull final CommandExecutor COMMAND) {
        Objects.requireNonNull(PLUGIN_INSTANCE.getCommand(COMMAND_NAME)).setExecutor(COMMAND);
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
    public synchronized static void reload(@NotNull final JavaPlugin pluginInstance) throws ModuleException {

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
