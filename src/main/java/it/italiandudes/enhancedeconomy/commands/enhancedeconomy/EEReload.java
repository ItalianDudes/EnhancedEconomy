package it.italiandudes.enhancedeconomy.commands.enhancedeconomy;

import it.italiandudes.enhancedeconomy.EnhancedEconomy;
import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.ConfigModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.utils.Defs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EEReload {

    // Subcommand Body
    public static boolean executeSubcommand(@Nullable Player player, @NotNull String[] args) {
        if (player != null && !player.isOp()) return false;
        if (args.length < 3) return false;

        for (int i = 2; i< args.length; i++) {
            switch (args[i]) {
                case Defs.ModuleNames.MODULE_DBCONNECTION -> {
                    if (i+1 < args.length) {
                        i++;
                        try {
                            DBConnectionModule.reload(args[i]);
                        }catch (ModuleException e) {
                            return false;
                            // TODO: handle error
                        }
                    }
                }
                case Defs.ModuleNames.MODULE_LOCALIZATION -> {
                    if (i+1 < args.length) {
                        i++;
                        try {
                            LocalizationModule.reload(EnhancedEconomy.getPluginInstance(), args[i]);
                        }catch (ModuleException e) {
                            return false;
                            // TODO: handle error
                        }
                    }
                }
                case Defs.ModuleNames.MODULE_CONFIG -> {
                    try {
                        ConfigModule.reload(EnhancedEconomy.getPluginInstance());
                    }catch (ModuleException e) {
                        return false;
                        // TODO: handle error
                    }
                }
                case Defs.ModuleNames.MODULE_COMMANDS -> {
                    try {
                        CommandsModule.reload(EnhancedEconomy.getPluginInstance());
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
