package it.italiandudes.enhancedeconomy.commands.enhancedeconomy;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
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
public final class EEBankCurrency implements CommandExecutor {

    // Attributes
    public static final String COMMAND_NAME = "eebank";
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

            if (!CommandsModule.isUserRegistered(sender)) {
                sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_USER_NOT_REGISTERED));
                return true;
            }

            switch (args[0]) {

                case Args.LIST -> {
                    query = "SELECT * FROM v_bank_currencies ORDER BY BankName;";
                    ResultSet result = DBConnectionModule.executePreparedStatementFromQuery(query);

                    boolean firstElement = true;
                    while (result.next()) {

                        if (firstElement) {
                            firstElement = false;
                            sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_LIST_HEADER));
                        }

                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_LIST_SEPARATOR));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_NAME) + ": " + ChatColor.WHITE + result.getString("BankName"));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.CURRENCY_ISO) + ": " + ChatColor.WHITE + result.getString("ISO"));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_BALANCE) + ": " + ChatColor.WHITE + result.getString("Balance"));
                    }

                    if (firstElement) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_LIST_EMPTY));
                    }

                    result.close();
                }

                case Args.GET -> {
                    if (args.length < 2) {
                        CommandsModule.sendSyntaxError(sender);
                        return true;
                    }

                    String name = args[1];

                    query = "SELECT * FROM v_bank_currencies WHERE BankName=? ORDER BY Balance DESC;";
                    PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
                    ps.setString(1, name);
                    ResultSet result = ps.executeQuery();

                    sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_GET_HEADER) + name + ":");
                    while (result.next()) {
                        sender.sendMessage(ChatColor.AQUA + String.valueOf(result.getDouble("Balance")) + result.getString("ISO"));
                    }

                    result.close();
                }

                case Args.NEW -> {
                    // TODO: New
                }

                case Args.DELETE -> {
                    // TODO: Delete
                }

                default -> CommandsModule.sendSyntaxError(sender);

            }
        } catch (ModuleException | SQLException e) {
            CommandsModule.sendDefaultError(sender, e);
        }

        return true;
    }
}
