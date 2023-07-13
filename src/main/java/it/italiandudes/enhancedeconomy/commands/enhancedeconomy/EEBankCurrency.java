package it.italiandudes.enhancedeconomy.commands.enhancedeconomy;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.objects.Bank;
import it.italiandudes.enhancedeconomy.objects.Currency;
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
                    if (args.length < 3) {
                        CommandsModule.sendSyntaxError(sender);
                        return true;
                    }

                    String bankName = args[1];
                    String iso = args[2];

                    Bank bank = new Bank(bankName);
                    if (bank.getBankID() == null) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_NEW_BANK_DOES_NOT_EXIST));
                        return true;
                    }

                    Currency currency = new Currency(iso);
                    if (currency.getCurrencyID() == null) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_NEW_ISO_DOES_NOT_EXIST));
                        return true;
                    }

                    if (!bank.getOwner().getName().equals(sender.getName())) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_NEW_NOT_BANK_OWNER));
                        return true;
                    }

                    if (Bank.getCurrencies(bank.getBankID()).contains(currency)) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_NEW_CURRENCY_ALREADY_REGISTERED));
                        return true;
                    }

                    query = "INSERT INTO bank_currencies (bank_id, currency_id) VALUES (?, ?);";
                    PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
                    ps.setInt(1, bank.getBankID());
                    ps.setInt(2, currency.getCurrencyID());
                    ps.executeUpdate();
                    ps.close();

                    sender.sendMessage(ChatColor.GREEN + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_NEW_SUCCESS));
                }

                case Args.DELETE -> {
                    if (args.length < 3) {
                        CommandsModule.sendSyntaxError(sender);
                        return true;
                    }

                    String bankName = args[1];
                    String iso = args[2];

                    Bank bank = new Bank(bankName);
                    if (bank.getBankID() == null) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_DELETE_BANK_DOES_NOT_EXIST));
                        return true;
                    }

                    Currency currency = new Currency(iso);
                    if (currency.getCurrencyID() == null) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_DELETE_ISO_DOES_NOT_EXIST));
                        return true;
                    }

                    if (!bank.getOwner().getName().equals(sender.getName())) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_DELETE_NOT_BANK_OWNER));
                        return true;
                    }

                    if (!Bank.getCurrencies(bank.getBankID()).contains(currency)) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_DELETE_CURRENCY_NOT_REGISTERED));
                        return true;
                    }

                    query = "DELETE FROM bank_currencies WHERE bank_id=? AND currency_id=?;";
                    PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
                    ps.setInt(1, bank.getBankID());
                    ps.setInt(2, currency.getCurrencyID());
                    ps.executeUpdate();
                    ps.close();

                    sender.sendMessage(ChatColor.GREEN + LocalizationModule.translate(Keys.COMMAND_EEBANK_CURRENCY_DELETE_SUCCESS));
                }

                default -> CommandsModule.sendSyntaxError(sender);

            }
        } catch (ModuleException | SQLException e) {
            CommandsModule.sendDefaultError(sender, e);
        }

        return true;
    }
}
