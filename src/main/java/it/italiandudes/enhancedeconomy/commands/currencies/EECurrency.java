package it.italiandudes.enhancedeconomy.commands.currencies;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class EECurrency implements CommandExecutor {

    // Attributes
    public static final String COMMAND_NAME = "currency";
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = false;

    // Command Body
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        return false;
    }
}
