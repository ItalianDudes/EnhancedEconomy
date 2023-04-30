package it.italiandudes.enhancedeconomy.commands.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.EnhancedEconomy;
import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.ConfigModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.utils.Defs;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"deprecation", "DuplicatedCode"})
public final class ReloadCommand implements CommandExecutor {

    // Attributes
    public static final String COMMAND_NAME = "reload";
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = true;

    // Subcommand Body
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!CommandsModule.isModuleLoaded() && !RUN_WITH_MODULE_NOT_LOADED) {
            try {
                sender.sendMessage(
                        ChatColor.RED +
                                LocalizationModule.translate(Defs.Localization.Keys.COMMAND_MODULE_NOT_LOADED)
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

        for (int i = 0; i< args.length; i++) {
            try {
                switch (args[i]) {
                    case Defs.ModuleNames.MODULE_DBCONNECTION -> {
                        if (i + 1 < args.length) {
                            i++;
                            try {
                                sender.sendMessage(
                                    ChatColor.AQUA +
                                    LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_STARTED) +
                                    Defs.ModuleNames.MODULE_DBCONNECTION
                                );
                                DBConnectionModule.reload(args[i]);
                                sender.sendMessage(
                                    ChatColor.AQUA +
                                    LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_SUCCESS) +
                                    Defs.ModuleNames.MODULE_DBCONNECTION
                                );
                            } catch (ModuleException e) {
                                sender.sendMessage(
                                    ChatColor.RED +
                                    LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_FAIL) +
                                    Defs.ModuleNames.MODULE_DBCONNECTION
                                );
                            }
                        }
                    }
                    case Defs.ModuleNames.MODULE_LOCALIZATION -> {
                        if (i + 1 < args.length) {
                            i++;
                            try {
                                sender.sendMessage(
                                    ChatColor.AQUA +
                                    LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_STARTED) +
                                    Defs.ModuleNames.MODULE_LOCALIZATION
                                );
                                LocalizationModule.reload(EnhancedEconomy.getPluginInstance(), args[i]);
                                sender.sendMessage(
                                    ChatColor.AQUA +
                                    LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_SUCCESS) +
                                    Defs.ModuleNames.MODULE_LOCALIZATION
                                );
                            } catch (ModuleException e) {
                                sender.sendMessage(
                                    ChatColor.RED +
                                    LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_FAIL) +
                                    Defs.ModuleNames.MODULE_LOCALIZATION
                                );
                            }
                        }
                    }
                    case Defs.ModuleNames.MODULE_CONFIG -> {
                        try {
                            sender.sendMessage(
                                ChatColor.AQUA +
                                LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_STARTED) +
                                Defs.ModuleNames.MODULE_CONFIG
                            );
                            ConfigModule.reload(EnhancedEconomy.getPluginInstance());
                            sender.sendMessage(
                                ChatColor.AQUA +
                                LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_SUCCESS) +
                                Defs.ModuleNames.MODULE_CONFIG
                            );
                        } catch (ModuleException e) {
                            sender.sendMessage(
                                ChatColor.RED +
                                LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_FAIL) +
                                Defs.ModuleNames.MODULE_CONFIG
                            );
                        }
                    }
                    case Defs.ModuleNames.MODULE_COMMANDS -> {
                        try {
                            sender.sendMessage(
                                ChatColor.AQUA +
                                LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_STARTED) +
                                Defs.ModuleNames.MODULE_COMMANDS
                            );
                            CommandsModule.reload(EnhancedEconomy.getPluginInstance());
                            sender.sendMessage(
                                ChatColor.AQUA +
                                LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_SUCCESS) +
                                Defs.ModuleNames.MODULE_COMMANDS
                            );
                        } catch (ModuleException e) {
                            sender.sendMessage(
                                ChatColor.RED +
                                LocalizationModule.translate(Defs.Localization.Keys.COMMAND_RELOADING_FAIL) +
                                Defs.ModuleNames.MODULE_COMMANDS
                            );
                        }
                    }
                    default -> sender.sendMessage(
                        ChatColor.RED +
                        LocalizationModule.translate(Defs.Localization.Keys.COMMAND_SYNTAX_ERROR)
                    );
                }
            } catch (ModuleException e) {
                CommandsModule.sendDefaultError(sender, e);
            }
        }

        return true;
    }
}
