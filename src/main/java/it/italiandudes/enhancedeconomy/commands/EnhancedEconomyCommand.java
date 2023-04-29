package it.italiandudes.enhancedeconomy.commands;

import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.EELoad;
import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.EEReload;
import it.italiandudes.enhancedeconomy.commands.enhancedeconomy.EEUnload;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.utils.Defs.Commands.EnhancedEconomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class EnhancedEconomyCommand implements CommandExecutor {

    // Command Name
    public static final String COMMAND_NAME = "enhancedeconomy";

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!CommandsModule.isModuleLoaded()) return false;
        if (args.length < 2) return false;

        Player player = (sender instanceof Player) ? ((Player) sender) : null;
        String subcommand = args[1];

        switch (subcommand) {
            case EnhancedEconomy.EE_LOAD -> {
                return EELoad.executeSubcommand(player, args);
            }
            case EnhancedEconomy.EE_UNLOAD -> {
                return EEUnload.executeSubcommand(player, args);
            }
            case EnhancedEconomy.EE_RELOAD -> {
                return EEReload.executeSubcommand(player, args);
            }
            default -> {
                return false;
            }
        }
    }
}
