package it.italiandudes.enhancedeconomy.commands.enhancedeconomy;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.objects.Bank;
import it.italiandudes.enhancedeconomy.objects.Country;
import it.italiandudes.enhancedeconomy.objects.User;
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
public final class EEBank implements CommandExecutor {

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
                    query = "SELECT * FROM banks;";
                    ResultSet result = DBConnectionModule.executePreparedStatementFromQuery(query);

                    boolean firstElement = true;
                    while (result.next()) {

                        if (firstElement) {
                            firstElement = false;
                            sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EEBANK_LIST_HEADER));
                        }

                        User user = new User(result.getInt("owner_id"));
                        Country country = new Country(result.getInt("headquarter_country"));

                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EEBANK_LIST_SEPARATOR));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_NAME) + ": " + ChatColor.WHITE + result.getString("name"));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_IS_PRIVATE) + ": " + ChatColor.WHITE + (result.getInt("is_private")!=0?Keys.CHECK_MARK:"X"));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_HEADQUARTER_COUNTRY) + ": " + ChatColor.WHITE + country.getName());
                        if (user.getUserID()!=null) {
                            sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_OWNER) + ": " + ChatColor.WHITE + user.getName());
                        }
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_CREATION_DATE) + ": " + ChatColor.WHITE + result.getDate("creation_date"));
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
                    query = "SELECT * FROM banks WHERE name=?;";
                    PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
                    ps.setString(1, args[1]);
                    ResultSet result = ps.executeQuery();

                    if (result.next()) {
                        User user = new User(result.getInt("owner_id"));
                        Country country = new Country(result.getInt("headquarter_country"));

                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.COMMAND_EEBANK_LIST_SEPARATOR));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_NAME) + ": " + ChatColor.WHITE + result.getString("name"));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_IS_PRIVATE) + ": " + ChatColor.WHITE + (result.getInt("is_private")!=0?Keys.CHECK_MARK:"X"));
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_HEADQUARTER_COUNTRY) + ": " + ChatColor.WHITE + country.getName());
                        if (user.getUserID()!=null) {
                            sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_OWNER) + ": " + ChatColor.WHITE + user.getName());
                        }
                        sender.sendMessage(ChatColor.AQUA + LocalizationModule.translate(Keys.BANK_CREATION_DATE) + ": " + ChatColor.WHITE + result.getDate("creation_date"));
                    } else {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_GET_NOT_FOUND));
                    }

                    result.close();
                }

                case Args.NEW -> {
                    if (args.length < 3) {
                        CommandsModule.sendSyntaxError(sender);
                        return true;
                    }

                    String name = args[1];
                    String countryName = args[2];
                    boolean isBankPrivate = true;

                    if (args.length > 3) {
                        isBankPrivate = Boolean.parseBoolean(args[3]);
                    }

                    if (Bank.exist(name)) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_NEW_ALREADY_EXIST));
                        return true;
                    }

                    Country country = new Country(countryName);
                    if (country.getCountryID() == null) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_NEW_COUNTRY_DOES_NOT_EXIST));
                        return true;
                    }

                    Integer userID;
                    User user = new User(sender.getName());
                    if (isBankPrivate) { // The bank is private
                        userID = user.getUserID();
                    } else if (!user.equals(country.getOwner())) { // The user is not the country owner
                        sender.sendMessage(ChatColor.RED + Keys.COMMAND_EEBANK_NEW_PUBLIC_NOT_COUNTRY_OWNER);
                        return true;
                    } else { // The bank is public
                        userID = country.getOwner().getUserID();
                    }

                    query = "INSERT INTO banks (name, headquarter_country, owner_id, is_private) VALUES (?, ?, ?, ?);";
                    PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
                    ps.setString(1, name);
                    ps.setInt(2, country.getCountryID());
                    ps.setInt(3, userID);
                    ps.setInt(4, (isBankPrivate?1:0));
                    ps.executeUpdate();
                    ps.close();

                    sender.sendMessage(ChatColor.GREEN + LocalizationModule.translate(Keys.COMMAND_EEBANK_NEW_SUCCESS));
                }

                case Args.DELETE -> {
                    if (args.length < 2) {
                        CommandsModule.sendSyntaxError(sender);
                        return true;
                    }

                    String name = args[1];

                    Bank bank = new Bank(name);

                    if (bank.getBankID()==null) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_DELETE_NOT_FOUND));
                        return true;
                    }

                    User commandSender = new User(sender.getName());
                    if (!commandSender.equals(bank.getOwner())) {
                        sender.sendMessage(ChatColor.RED + LocalizationModule.translate(Keys.COMMAND_EEBANK_DELETE_NOT_OWNER));
                        return true;
                    }

                    query = "DELETE FROM banks WHERE name=?;";
                    PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
                    ps.setString(1, name);
                    ps.executeUpdate();
                    ps.close();

                    sender.sendMessage(ChatColor.GREEN + LocalizationModule.translate(Keys.COMMAND_EEBANK_DELETE_SUCCESS));
                }

                default -> CommandsModule.sendSyntaxError(sender);

            }
        } catch (ModuleException | SQLException e) {
            CommandsModule.sendDefaultError(sender, e);
        }

        return true;
    }
}
