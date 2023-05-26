package it.italiandudes.enhancedeconomy.commands;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.util.Defs;
import it.italiandudes.enhancedeconomy.util.Defs.LangKeys;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public final class EnhancedEconomyCommand extends CommandBase {

    // Command Name
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = true;

    // Arguments
    private static final class Arguments {
        public static final String INFO = "info";
        public static final String VERSION = "version";
        public static final String MODSTATS = "modstats";
    }

    // Command Info
    @Override @NotNull
    public String getName() {
        return Defs.Commands.COMMAND_NAME[0];
    }
    @Override @NotNull
    public List<String> getAliases() {
        return Arrays.asList(Defs.Commands.COMMAND_NAME);
    }
    @Override @NotNull
    public String getUsage(@NotNull final ICommandSender sender) {
        try {
            String usage = LocalizationModule.translate(LangKeys.COMMAND_USAGE_EE);
            if (usage != null) return usage;
            throw new ModuleException("Localization failed");
        }catch (ModuleException e) {
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

        try {
            String localizedMsg;
            TextComponentString msg;
            switch (args[0]) {

                case Arguments.INFO:
                    localizedMsg = LocalizationModule.translate(LangKeys.EE_INFO);
                    if (localizedMsg == null) throw new ModuleException("Localization failed");
                    msg = new TextComponentString(localizedMsg);
                    msg.getStyle().setColor(TextFormatting.AQUA);
                    sender.sendMessage(msg);
                    break;

                /*case Arguments.VERSION: //TODO: Find a way to get version from mcmod.info
                    localizedMsg = LocalizationModule.translate(LangKeys.EE_VERSION);
                    localizedMsg+= ?
                    break;*/

                case Arguments.MODSTATS:
                    localizedMsg = LocalizationModule.translate(LangKeys.EE_MODSTATS) + '\n' +
                            LocalizationModule.NAME + ": " + (LocalizationModule.isModuleLoaded() ? "ONLINE" : "OFFLINE") +
                            CommandsModule.NAME + ": " + (LocalizationModule.isModuleLoaded() ? "ONLINE" : "OFFLINE") +
                            DBConnectionModule.NAME + ": " + (LocalizationModule.isModuleLoaded() ? "ONLINE" : "OFFLINE");
                    msg = new TextComponentString(localizedMsg);
                    msg.getStyle().setColor(TextFormatting.AQUA);
                    sender.sendMessage(msg);
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
