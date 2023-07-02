package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.commands.EnhancedEconomyCommand;
import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.currencies.EECurrency;
import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.modules.EELoadCommand;
import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.modules.EEReloadCommand;
import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.modules.EEUnloadCommand;
import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleAlreadyLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleLoadingException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleNotLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleReloadingException;
import it.italiandudes.enhancedeconomy.utils.Defs;
import it.italiandudes.enhancedeconomy.utils.ServerLogger;
import it.italiandudes.idl.common.StringHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings({"unused", "deprecation"})
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
    public synchronized static void load(@NotNull final JavaPlugin pluginInstance, final boolean disableLog) throws ModuleException {

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
            registerCommand(pluginInstance, EELoadCommand.COMMAND_NAME, new EELoadCommand());
            registerCommand(pluginInstance, EEUnloadCommand.COMMAND_NAME, new EEUnloadCommand());
            registerCommand(pluginInstance, EEReloadCommand.COMMAND_NAME, new EEReloadCommand());
            registerCommand(pluginInstance, EECurrency.COMMAND_NAME, new EECurrency());
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
        reload(pluginInstance, false);
    }
    public synchronized static void reload(@NotNull final JavaPlugin pluginInstance, final boolean disableLog) throws ModuleException {

        if (areCommandsLoading) {
            if (!disableLog) ServerLogger.getLogger().warning("Commands Module Reload: Canceled! (Reason: Another thread is executing a commands loading command)");
            throw new ModuleLoadingException("Commands Module Reload: Canceled! (Reason: Another thread is executing a commands loading command)");
        }
        if (!isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().severe("Commands Module Reload: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("Commands Module Reload: Failed! (Reason: the module isn't loaded)");
        }

        try {
            unload(true);
        } catch (ModuleException e) {
            if (!disableLog) ServerLogger.getLogger().severe("Commands Module Reload: Failed! (Reason: the unload routine has failed)");
            throw new ModuleReloadingException("Commands Module Reload: Failed! (Reason: the unload routine has failed)", e);
        }

        try {
            load(pluginInstance, true);
        } catch (ModuleException e) {
            if (!disableLog) ServerLogger.getLogger().severe("Commands Module Reload: Failed! (Reason: the load routine has failed)");
            throw new ModuleReloadingException("Commands Module Reload: Failed! (Reason: the load routine has failed)", e);
        }

        if (!disableLog) ServerLogger.getLogger().info("Commands Module Reload: Successful!");
    }

    // Utilities Methods
    public static void sendSyntaxError(@NotNull final CommandSender sender) {
        try {
            sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Defs.Localization.Keys.COMMAND_SYNTAX_ERROR));
        } catch (ModuleException e) {
            LocalizationModule.sendLocalizationErrorMessage(sender);
        }
    }

    /**
     * Send to the command sender a generic error message about the command execution failed.
     *
     * @param sender    The command sender
     * @param e         The exception thrown during the command execution
     */
    public static void sendDefaultError(@NotNull final CommandSender sender, @Nullable final Throwable e) {
        try {
            String err = LocalizationModule.translate(Defs.Localization.Keys.COMMAND_EXECUTION_ERROR);
            sender.sendMessage(ChatColor.RED + err);
            ServerLogger.getLogger().severe(err);
            if (e != null) ServerLogger.getLogger().severe(StringHandler.getStackTrace(e));
        } catch (Exception ignored) {}
    }
    /**
     * Check if the sender has to be OP to use the command.
     *
     * @param sender        The command sender
     * @param OP_REQUIRED   If the command requested requires the sender to be OP
     * @return              True if the command execution can proceed, false otherwise
     * */
    public static boolean handleOpRequired(@NotNull final CommandSender sender, final boolean OP_REQUIRED) {
        if (OP_REQUIRED && !sender.isOp()) {
            try {
                sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Defs.Localization.Keys.COMMAND_MISSING_PERMISSIONS));
            } catch (ModuleException e) {
                LocalizationModule.sendLocalizationErrorMessage(sender);
            }
            return false;
        }
        return true;
    }
    /**
     * Check if the command requires the Commands Module to be enabled to work.
     *
     * @param sender                    The command sender
     * @param COMMANDS_MODULE_REQUIRED  If the command requested requires the sender to be OP
     * @return                          True if command execution can proceed, false otherwise
     * */
    public static boolean handleCommandsModuleRequired(@NotNull final CommandSender sender, final boolean COMMANDS_MODULE_REQUIRED) {
        if (COMMANDS_MODULE_REQUIRED && !CommandsModule.isModuleLoaded()) {
            try {
                sender.sendMessage(
                        ChatColor.RED +
                                LocalizationModule.translate(Defs.Localization.Keys.COMMAND_MODULE_NOT_LOADED)
                );
            } catch (ModuleException e) {
                LocalizationModule.sendLocalizationErrorMessage(sender);
            }
            return false;
        }
        return true;
    }
}
