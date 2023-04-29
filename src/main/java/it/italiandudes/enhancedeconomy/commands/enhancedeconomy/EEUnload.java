package it.italiandudes.enhancedeconomy.commands.enhancedeconomy;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.ConfigModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.utils.Defs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EEUnload {

    // Subcommand Body
    public static boolean executeSubcommand(@Nullable Player player, @NotNull String[] args) {
        if (player != null && !player.isOp()) return false;
        if (args.length < 3) return false;

        for (int i = 2; i< args.length; i++) {
            switch (args[i]) {
                case Defs.ModuleNames.MODULE_DBCONNECTION -> {
                    try {
                        DBConnectionModule.unload();
                    }catch (ModuleException e) {
                        return false;
                        // TODO: handle error
                    }
                }
                case Defs.ModuleNames.MODULE_LOCALIZATION -> {
                    try {
                        LocalizationModule.unload();
                    }catch (ModuleException e) {
                        return false;
                        // TODO: handle error
                    }
                }
                case Defs.ModuleNames.MODULE_CONFIG -> {
                    try {
                        ConfigModule.unload();
                    }catch (ModuleException e) {
                        return false;
                        // TODO: handle error
                    }
                }
                case Defs.ModuleNames.MODULE_COMMANDS -> {
                    try {
                        CommandsModule.unload();
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
