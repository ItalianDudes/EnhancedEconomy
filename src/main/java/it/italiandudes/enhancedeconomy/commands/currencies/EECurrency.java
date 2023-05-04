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

import java.sql.ResultSet;
import java.sql.SQLException;
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

        try {
            String name, iso;
            String query;
            String output;
            ArrayList<Currency> resultContent = new ArrayList<>();
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
                    break;

                case Defs.Commands.EECurrency.EE_CURRENCY_LIST:
                    try {
                        query = "SELECT name, symbol, iso FROM currencies;";
                        ResultSet results = DBConnectionModule.executePreparedStatementFromQuery(query);
                        if (results == null) {
                            throw new ModuleException("The result set is null");
                        }
                        while (results.next()) {
                            name = results.getString("name");
                            iso = results.getString("iso");
                            symbol = results.getString("symbol").charAt(0);
                            resultContent.add(new Currency(name, iso, symbol));
                        }
                        if (resultContent.size() > 0) {
                            // TODO: prepare a formatted output
                            formattedOutput = new TextComponentString(TextFormatting.AQUA + LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_LIST_HEADER) + "\n");
                            formattedOutput = formattedOutput.appendText(TextFormatting.AQUA + LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_LIST_SEPARATOR));
                        }else {
                            formattedOutput = new TextComponentString(TextFormatting.AQUA + LocalizationModule.translate(Defs.LangKeys.COMMAND_EECURRENCY_LIST_NO_CURRENCY));
                        }
                        results.close();
                        sender.sendMessage(formattedOutput);
                    } catch (Exception e) {
                        throw new ModuleException("EE Currency List execution error",e);
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