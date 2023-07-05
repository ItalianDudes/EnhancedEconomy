package it.italiandudes.enhancedeconomy.commands.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.EnhancedEconomy;
import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.ConfigModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.utils.ArgumentUtilities;
import it.italiandudes.enhancedeconomy.utils.Defs;
import it.italiandudes.enhancedeconomy.utils.Defs.Localization.Keys;
import it.italiandudes.enhancedeconomy.utils.Defs.ModuleNames;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"deprecation", "DuplicatedCode"})
public final class EELoadCommand implements CommandExecutor {

    // Attributes
    @NotNull public static final String COMMAND_NAME = "eeload";
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = true;

    // Command Body
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull String[] args) {
        if (!CommandsModule.isModuleLoaded() && !RUN_WITH_MODULE_NOT_LOADED) {
            try {
                sender.sendMessage(
                    ChatColor.RED +
                    LocalizationModule.translate(Keys.COMMAND_MODULE_NOT_LOADED)
                );
            } catch (ModuleException ignored) {}
            return true;
        }
        if (!sender.isOp()) {
            try {
                sender.sendMessage(
                    ChatColor.RED +
                    LocalizationModule.translate(Defs.Localization.Keys.COMMAND_MISSING_PERMISSIONS)
                );
            }catch (ModuleException ignored) {}
            return true;
        }
        if (args.length < 1) return false;

        args = ArgumentUtilities.reparseArgs(args);
        if (args == null) {
            CommandsModule.sendDefaultError(sender, null);
            return true;
        }

        boolean commandError = false;
        for (int i = 0; i< args.length && !commandError; i++) {
            try {
                switch (args[i]) {
                    case ModuleNames.MODULE_DBCONNECTION -> {
                        if (i + 1 < args.length) {
                            i++;
                            try {
                                sender.sendMessage(
                                    ChatColor.AQUA +
                                    LocalizationModule.translate(Keys.COMMAND_LOADING_STARTED) +
                                    ModuleNames.MODULE_DBCONNECTION
                                );
                                DBConnectionModule.load(args[i], !(sender instanceof Player));
                                sender.sendMessage(
                                    ChatColor.AQUA +
                                    LocalizationModule.translate(Keys.COMMAND_LOADING_SUCCESS) +
                                    ModuleNames.MODULE_DBCONNECTION
                                );
                            } catch (ModuleException e) {
                                sender.sendMessage(
                                    ChatColor.RED +
                                    LocalizationModule.translate(Keys.COMMAND_LOADING_FAIL) +
                                    ModuleNames.MODULE_DBCONNECTION
                                );
                            }
                        }else {
                            sender.sendMessage(
                                ChatColor.RED +
                                LocalizationModule.translate(Keys.COMMAND_SYNTAX_ERROR)
                            );
                            commandError = true;
                        }
                    }
                    case ModuleNames.MODULE_LOCALIZATION -> {
                        if (i + 1 < args.length) {
                            i++;
                            try {
                                sender.sendMessage(
                                    ChatColor.AQUA +
                                    "Loading Module: " +
                                    ModuleNames.MODULE_LOCALIZATION
                                );
                                LocalizationModule.load(EnhancedEconomy.getPluginInstance(), args[i], !(sender instanceof Player));
                                sender.sendMessage(
                                    ChatColor.AQUA +
                                    "Loading Module Success: " +
                                    ModuleNames.MODULE_LOCALIZATION
                                );
                            } catch (ModuleException e) {
                                sender.sendMessage(
                                    ChatColor.RED +
                                    "Loading Module Failed: " +
                                    ModuleNames.MODULE_LOCALIZATION
                                );
                            }
                        }else {
                            sender.sendMessage(
                                ChatColor.RED +
                                LocalizationModule.translate(Keys.COMMAND_SYNTAX_ERROR)
                            );
                            commandError = true;
                        }
                    }
                    case ModuleNames.MODULE_CONFIG -> {
                        try {
                            sender.sendMessage(
                                ChatColor.AQUA +
                                LocalizationModule.translate(Keys.COMMAND_LOADING_STARTED) +
                                ModuleNames.MODULE_CONFIG
                            );
                            ConfigModule.load(EnhancedEconomy.getPluginInstance(), !(sender instanceof Player));
                            sender.sendMessage(
                                ChatColor.AQUA +
                                LocalizationModule.translate(Keys.COMMAND_LOADING_SUCCESS) +
                                ModuleNames.MODULE_CONFIG
                            );
                        } catch (ModuleException e) {
                            sender.sendMessage(
                                ChatColor.RED +
                                LocalizationModule.translate(Keys.COMMAND_LOADING_FAIL) +
                                ModuleNames.MODULE_CONFIG
                            );
                        }
                    }
                    case ModuleNames.MODULE_COMMANDS -> {
                        try {
                            sender.sendMessage(
                                ChatColor.AQUA +
                                LocalizationModule.translate(Keys.COMMAND_LOADING_STARTED) +
                                ModuleNames.MODULE_COMMANDS
                            );
                            CommandsModule.load(EnhancedEconomy.getPluginInstance(), !(sender instanceof Player));
                            sender.sendMessage(
                                ChatColor.AQUA +
                                LocalizationModule.translate(Keys.COMMAND_LOADING_SUCCESS) +
                                ModuleNames.MODULE_COMMANDS
                            );
                        } catch (ModuleException e) {
                            sender.sendMessage(
                                ChatColor.RED +
                                LocalizationModule.translate(Keys.COMMAND_LOADING_FAIL) +
                                ModuleNames.MODULE_COMMANDS
                            );
                        }
                    }
                    default -> sender.sendMessage(
                        ChatColor.RED +
                        LocalizationModule.translate(Keys.COMMAND_SYNTAX_ERROR)
                    );
                }
            } catch (ModuleException e) {
                CommandsModule.sendDefaultError(sender, e);
            }
        }

        return true;
    }
}
