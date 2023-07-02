package it.italiandudes.enhancedeconomy.commands.enhancedeconomy;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.objects.User;
import it.italiandudes.enhancedeconomy.utils.Defs.Localization.Keys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("deprecation")
public final class EEUser implements CommandExecutor {

    // Attributes
    public static final String COMMAND_NAME = "eeuser";
    public static final boolean COMMANDS_MODULE_REQUIRED = true;
    public static final boolean OP_REQUIRED = false;

    // Command Body
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull String[] args) {
        if (!CommandsModule.handleCommandsModuleRequired(sender, COMMANDS_MODULE_REQUIRED)) return true;
        if (!CommandsModule.handleOpRequired(sender, OP_REQUIRED)) return true;
        if (!DBConnectionModule.handleDBConnectionModuleRequired(sender)) return true;

        try {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEUSER_CONSOLE));
                return true;
            }

            String username = sender.getName();
            if (User.exist(username)) {
                sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEUSER_ALREADY_EXIST));
                return true;
            }

            String query = "INSERT INTO users (name) VALUES (?);";
            PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
            ps.setString(1, username);
            ps.executeUpdate();
            ps.close();

            sender.sendMessage(ChatColor.GREEN + LocalizationModule.translate(Keys.COMMAND_EEUSER_SUCCESS));

        } catch (ModuleException | SQLException e) {
            CommandsModule.sendDefaultError(sender, e);
        }

        return true;
    }
}
