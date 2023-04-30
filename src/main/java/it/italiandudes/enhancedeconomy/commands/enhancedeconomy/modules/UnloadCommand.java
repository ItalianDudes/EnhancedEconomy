package it.italiandudes.enhancedeconomy.commands.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.ConfigModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.utils.Defs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public final class UnloadCommand implements CommandExecutor {

    // Attributes
    public static final String COMMAND_NAME = "unload";
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = true;

    // Subcommand Body
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!CommandsModule.isModuleLoaded() && !RUN_WITH_MODULE_NOT_LOADED) return false;
        if (!sender.isOp()) return false;
        if (args.length < 1) return false;

        for (String module : args) {
            switch (module) {
                case Defs.ModuleNames.MODULE_DBCONNECTION -> {
                    try {
                        DBConnectionModule.unload();
                    } catch (ModuleException e) {
                        return false;
                        // TODO: handle error
                    }
                }
                case Defs.ModuleNames.MODULE_LOCALIZATION -> {
                    try {
                        LocalizationModule.unload();
                    } catch (ModuleException e) {
                        return false;
                        // TODO: handle error
                    }
                }
                case Defs.ModuleNames.MODULE_CONFIG -> {
                    try {
                        ConfigModule.unload();
                    } catch (ModuleException e) {
                        return false;
                        // TODO: handle error
                    }
                }
                case Defs.ModuleNames.MODULE_COMMANDS -> {
                    try {
                        CommandsModule.unload();
                    } catch (ModuleException e) {
                        return false;
                        // TODO: handle error
                    }
                }
                default -> {
                    return false;
                }
            }
        }

        return true;
    }
}
