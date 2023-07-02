package it.italiandudes.enhancedeconomy.commands.enhancedeconomy;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.objects.Country;
import it.italiandudes.enhancedeconomy.utils.ArgumentUtilities;
import it.italiandudes.enhancedeconomy.utils.Defs.Localization.Keys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("deprecation")
public final class EECountry implements CommandExecutor {

    // Attributes
    public static final String COMMAND_NAME = "eecountry";
    public static final boolean COMMANDS_MODULE_REQUIRED = true;
    public static final boolean OP_REQUIRED = false;

    // Args
    private static final class Args {
        public static final String LIST = "list";
        public static final String NEW = "new";
        public static final String DELETE = "delete";
        public static final String GET = "get";
    }

    // Command Body
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull String[] args) {
        if (!CommandsModule.handleCommandsModuleRequired(sender, COMMANDS_MODULE_REQUIRED)) return true;
        if (!CommandsModule.handleOpRequired(sender, OP_REQUIRED)) return true;
        if (!DBConnectionModule.handleDBConnectionModuleRequired(sender)) return true;
        if (args.length < 1) return false;

        args = ArgumentUtilities.reparseArgs(args);
        if (args == null) {
            CommandsModule.sendDefaultError(sender, null);
            return true;
        }

        String query;

        try {
            switch (args[0]) {

                case Args.LIST -> {
                    query = "SELECT * FROM countries;";
                    ResultSet result = DBConnectionModule.executePreparedStatementFromQuery(query);

                    boolean firstElement = true;
                    while (result.next()) {

                        if (firstElement) {
                            firstElement = false;
                            sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EECOUNTRY_LIST_HEADER));
                        }

                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EECOUNTRY_LIST_SEPARATOR));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COUNTRY_NAME) + ": " + ChatColor.WHITE + result.getString("name"));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COUNTRY_CREATION_DATE) + ": " + ChatColor.WHITE + result.getDate("creation_date"));
                    }

                    if (firstElement) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EECOUNTRY_LIST_EMPTY));
                    }

                    result.close();
                }

                case Args.GET -> {
                    if (args.length < 2) {
                        CommandsModule.sendSyntaxError(sender);
                        return true;
                    }
                    query = "SELECT * FROM countries WHERE name=?;";
                    PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
                    ps.setString(1, args[1]);
                    ResultSet result = ps.executeQuery();

                    if (result.next()) {
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COUNTRY_NAME) + ": " + ChatColor.WHITE + result.getString("name"));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COUNTRY_CREATION_DATE) + ": " + ChatColor.WHITE + result.getDate("creation_date"));
                    } else {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EECOUNTRY_GET_NOT_FOUND));
                    }

                    result.close();
                }

                case Args.NEW -> {
                    if (args.length < 2) {
                        CommandsModule.sendSyntaxError(sender);
                        return true;
                    }
                    String name = args[1];
                    if (Country.exist(name)) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EECOUNTRY_NEW_ALREADY_EXIST));
                        return true;
                    }

                    query = "INSERT INTO countries (name) VALUES (?);";
                    PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
                    ps.setString(1, name);
                    ps.executeUpdate();
                    ps.close();

                    sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EECOUNTRY_NEW_SUCCESS));
                }

                case Args.DELETE -> {
                    if (args.length < 2) {
                        CommandsModule.sendSyntaxError(sender);
                        return true;
                    }

                    String name = args[1];

                    if (!Country.exist(name)) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EECOUNTRY_DELETE_NOT_FOUND));
                        return true;
                    }

                    query = "DELETE FROM countries WHERE name=?;";
                    PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
                    ps.setString(1, name);
                    ps.executeUpdate();
                    ps.close();

                    sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EECOUNTRY_DELETE_SUCCESS));
                }

                default -> CommandsModule.sendSyntaxError(sender);
            }
        } catch (ModuleException | SQLException e) {
            CommandsModule.sendDefaultError(sender, e);
        }

        return true;
    }
}
