package it.italiandudes.enhancedeconomy.commands.enhancedeconomy;

import it.italiandudes.enhancedeconomy.EnhancedEconomy;
import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.ConfigModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.utils.Defs.ModuleNames;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EELoad {

    // Subcommand Body
    public static boolean executeSubcommand(@Nullable Player player, @NotNull String[] args) {
        if (player != null && !player.isOp()) return false;
        if (args.length < 3) return false;

        for (int i = 2; i< args.length; i++) {
            switch (args[i]) {
                case ModuleNames.MODULE_DBCONNECTION -> {
                    if (i+1 < args.length) {
                        i++;
                        try {
                            DBConnectionModule.load(args[i]);
                        }catch (ModuleException e) {
                            return false;
                            // TODO: handle error
                        }
                    }
                }
                case ModuleNames.MODULE_LOCALIZATION -> {
                    if (i+1 < args.length) {
                        i++;
                        try {
                            LocalizationModule.load(EnhancedEconomy.getPluginInstance(), args[i]);
                        }catch (ModuleException e) {
                            return false;
                            // TODO: handle error
                        }
                    }
                }
                case ModuleNames.MODULE_CONFIG -> {
                    try {
                        ConfigModule.load(EnhancedEconomy.getPluginInstance());
                    }catch (ModuleException e) {
                        return false;
                        // TODO: handle error
                    }
                }
                case ModuleNames.MODULE_COMMANDS -> {
                    try {
                        CommandsModule.load(EnhancedEconomy.getPluginInstance());
                    }catch (ModuleException e) {
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
