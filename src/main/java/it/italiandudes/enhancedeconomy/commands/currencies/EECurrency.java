package it.italiandudes.enhancedeconomy.commands.currencies;

import it.italiandudes.enhancedeconomy.datatypes.Currency;
import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@SuppressWarnings("unused")
public final class EECurrency extends CommandBase {

    // Attributes
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = false;

    // Command Info
    @Override @NotNull
    public String getName() {
        return Defs.Commands.EECurrency.EE_CURRENCY;
    }
    @Override @NotNull
    public String getUsage(@NotNull final ICommandSender sender) {
        try {
            String usage = LocalizationModule.translate(Defs.LangKeys.COMMAND_USAGE_EECURRENCY);
            if (usage != null) return usage;
            throw new ModuleException("Localization failed");
        } catch (ModuleException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean checkPermission(@NotNull final MinecraftServer server, @NotNull final ICommandSender sender) {
        return true;
    }

    // Command Body
    @Override
    public void execute(@NotNull final MinecraftServer server, @NotNull final ICommandSender sender, final String @NotNull [] args) {
        if (!CommandsModule.isModuleLoaded() && !RUN_WITH_MODULE_NOT_LOADED) {
            CommandsModule.sendModuleNotLoadedError(sender);
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(
                new TextComponentString(
                TextFormatting.RED + getUsage(sender)
                )
            );
            return;
        }
        if (!DBConnectionModule.isModuleLoaded()) {
            CommandsModule.sendAllRequiredModulesAreNotLoadedError(sender);
            return;
        }

        System.out.println("PRE-DONE");

        try {
            String name, iso, creationDate;
            String query;
            String output;
            ArrayList<Currency> resultContent;
            ITextComponent formattedOutput;
            char symbol;
            switch (args[0]) {

                case Defs.Commands.EECurrency.EE_CURRENCY_NEW:
                    if (args.length < 4) {
                        sender.sendMessage(
                            new TextComponentString(
                                    TextFormatting.RED + getUsage(sender)
                            )
                        );
                        return;
                    }
                    name = args[1];
                    iso = args[2];
                    symbol = args[3].charAt(0);
                    try {
                        query = "INSERT INTO currencies (name, iso, symbol) VALUES (?, ?, ?);";
                        PreparedStatement preparedStatement = DBConnectionModule.getPreparedStatement(query);
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, iso);
                        preparedStatement.setString(3, String.valueOf(symbol));
                        int newRows = preparedStatement.executeUpdate();
                        preparedStatement.close();
                        if (newRows > 0) {
                            formattedOutput = new TextComponentString(LocalizationModule.translate(Defs.LangKeys.COMMANDS_EECURRENCY_NEW_SUCCESS));
                            formattedOutput.getStyle().setColor(TextFormatting.AQUA);
                        }else {
                            formattedOutput = new TextComponentString(LocalizationModule.translate(Defs.LangKeys.COMMANDS_EECURRENCY_NEW_FAIL));
                            formattedOutput.getStyle().setColor(TextFormatting.RED);
                        }
                        sender.sendMessage(formattedOutput);
                    }catch (Exception e) {
                        throw new ModuleException("EE Currency New execution error", e);
                    }
                    break;

                case Defs.Commands.EECurrency.EE_CURRENCY_DELETE:
                    if (args.length < 2) {
                        sender.sendMessage(
                            new TextComponentString(
                                TextFormatting.RED + getUsage(sender)
                            )
                        );
                        return;
                    }
                    iso = args[1];
                    try {
                        query = "DELETE FROM currencies WHERE iso=?;";
                        PreparedStatement preparedStatement = DBConnectionModule.getPreparedStatement(query);
                        preparedStatement.setString(1, iso);
                        int deletedRows = preparedStatement.executeUpdate();
                        preparedStatement.close();
                        if (deletedRows > 0) {
                            formattedOutput = new TextComponentString(TextFormatting.AQUA + LocalizationModule.translate(Defs.LangKeys.COMMANDS_EECURRENCY_DELETE_SUCCESS));
                        }else {
                            formattedOutput = new TextComponentString(TextFormatting.RED + LocalizationModule.translate(Defs.LangKeys.COMMANDS_EECURRENCY_DELETE_FAIL));
                        }
                        sender.sendMessage(formattedOutput);
                    }catch (Exception e) {
                        throw new ModuleException("EE Currency Delete execution error", e);
                    }
                    break;

                case Defs.Commands.EECurrency.EE_CURRENCT_GET:
                    if (args.length < 2) {
                        sender.sendMessage(
                                new TextComponentString(
                                        TextFormatting.RED + getUsage(sender)
                                )
                        );
                        return;
                    }
                    iso = args[1];
                    try {
                        query = "SELECT name, symbol, creation_date FROM currencies;";
                        ResultSet results = DBConnectionModule.executePreparedStatementFromQuery(query);
                        if (results == null) {
                            throw new ModuleException("The result is null");
                        }
                        resultContent = new ArrayList<>();
                        while (results.next()) {
                            name = results.getString("name");
                            symbol = results.getString("symbol").charAt(0);
                            creationDate = results.getDate("creation_date").toString();
                            resultContent.add(new Currency(name, iso, symbol, creationDate));
                        }
                        results.close();
                        if (resultContent.size() > 0) {
                            Currency c = resultContent.get(0);
                            formattedOutput = new TextComponentString(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_LIST_SEPARATOR)+'\n');
                            formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_NAME)+TextFormatting.RESET+c.getCurrencyName()+'\n');
                            formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_ISO)+TextFormatting.RESET+c.getIso()+'\n');
                            formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_SYMBOL)+TextFormatting.RESET+c.getSymbol()+'\n');
                            formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_CREATION_DATE)+TextFormatting.RESET+c.getCreationDate()+'\n');
                            formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_LIST_SEPARATOR));
                        }else {
                            formattedOutput = new TextComponentString(TextFormatting.AQUA + LocalizationModule.translate(Defs.LangKeys.COMMANDS_EECURRENCY_GET_NO_CURRENCY));
                        }
                        sender.sendMessage(formattedOutput);
                    }catch (Exception e) {
                        throw new ModuleException("EE Currency Get execution error", e);
                    }
                    break;

                case Defs.Commands.EECurrency.EE_CURRENCY_LIST:
                    System.out.println("A");
                    try {
                        System.out.println("B");
                        query = "SELECT name, symbol, iso, creation_date FROM currencies;";
                        System.out.println("C");
                        ResultSet results = DBConnectionModule.executePreparedStatementFromQuery(query);
                        System.out.println("D");
                        if (results == null) {
                            System.out.println("DA");
                            sender.sendMessage(new TextComponentString("Something is wrong i can feel it"));
                            throw new ModuleException("The result set is null");
                        }
                        System.out.println("E");
                        resultContent = new ArrayList<>();
                        System.out.println("F");
                        while (results.next()) {
                            System.out.println("G");
                            name = results.getString("name");
                            iso = results.getString("iso");
                            symbol = results.getString("symbol").charAt(0);
                            creationDate = results.getDate("creation_date").toString();
                            resultContent.add(new Currency(name, iso, symbol, creationDate));
                        }
                        System.out.println("H");
                        results.close();
                        System.out.println("I");
                        if (resultContent.size() > 0) {
                            System.out.println("J");
                            formattedOutput = new TextComponentString(TextFormatting.AQUA + LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_LIST_HEADER) + '\n');
                            formattedOutput = formattedOutput.appendText(TextFormatting.AQUA + LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_LIST_SEPARATOR) + '\n');
                            System.out.println("K");
                            for (int i=0;i<resultContent.size();i++) {
                                System.out.println("L");
                                Currency c = resultContent.get(i);
                                formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_NAME)+TextFormatting.RESET+c.getCurrencyName()+'\n');
                                formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_ISO)+TextFormatting.RESET+c.getIso()+'\n');
                                formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_SYMBOL)+TextFormatting.RESET+c.getSymbol()+'\n');
                                formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_CREATION_DATE)+TextFormatting.RESET+c.getCreationDate()+'\n');
                                formattedOutput = formattedOutput.appendText(TextFormatting.AQUA+LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_LIST_SEPARATOR));
                                if (i+1 < resultContent.size()) formattedOutput = formattedOutput.appendText(TextFormatting.AQUA + "\n");
                            }
                            System.out.println("M");
                        }else {
                            System.out.println("N");
                            formattedOutput = new TextComponentString(TextFormatting.AQUA + LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_LIST_NO_CURRENCY));
                            System.out.println("O");
                        }
                        System.out.println("P");
                        sender.sendMessage(formattedOutput);
                        System.out.println("Q");
                    } catch (Exception e) {
                        System.out.println("R");
                        throw new ModuleException("EE Currency List execution error", e);
                    }
                    break;

                default:
                    CommandsModule.sendCommandSyntaxError(sender, null);
                    break;
            }
        } catch (ModuleException e) {
            CommandsModule.sendCommandExecutionError(sender, e);
        }
    }
}