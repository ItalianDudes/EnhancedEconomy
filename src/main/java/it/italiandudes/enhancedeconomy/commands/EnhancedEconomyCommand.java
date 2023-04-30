package it.italiandudes.enhancedeconomy.commands;

import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public final class EnhancedEconomyCommand implements CommandExecutor {

    // Command Name
    public static final String COMMAND_NAME = "enhancedeconomy";
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = true;

    // Command Arguments
    public static final class Arguments {
        public static final String INFO = "info";
        public static final String VERSION = "version";
    }

    // Command Body
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!CommandsModule.isModuleLoaded() && !RUN_WITH_MODULE_NOT_LOADED) return false;
        if (args.length < 1) return false;

        for (String key : args) {
            switch (key) {
                case Arguments.INFO -> {

                }
                case Arguments.VERSION -> {
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.GOLD + "CLIENT SIDE");
                    }else {
                        sender.sendMessage(ChatColor.AQUA + "SERVER SIDE");
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
