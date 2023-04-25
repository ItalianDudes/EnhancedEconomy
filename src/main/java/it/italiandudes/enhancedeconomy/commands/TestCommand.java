package it.italiandudes.enhancedeconomy.commands;

import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.utils.ServerLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class TestCommand implements CommandExecutor {

    // Command Name
    public static final String COMMAND_NAME = "eetest";

    // Command Instance
    private static final CommandExecutor COMMAND_INSTANCE = CommandsModule.COMMANDS.put(COMMAND_NAME, new TestCommand());

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!CommandsModule.isModuleLoaded()) return false;
        if (sender instanceof Player) {
            ServerLogger.getLogger().info("Command executed by a player");
        }else {
            ServerLogger.getLogger().info("Command executed by the server");
        }
        return true;
    }
}
