package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.commands.EnhancedEconomyCommand;
import it.italiandudes.enhancedeconomy.util.Defs;
import it.italiandudes.enhancedeconomy.util.ServerLogger;
import it.italiandudes.idl.common.StringHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class CommandsModule {

    // Default Constructor
    public CommandsModule() {
        throw new RuntimeException("Can't instantiate this class!");
    }
    public static void registerCommands(@NotNull final FMLServerStartingEvent event) {
        event.registerServerCommand(new EnhancedEconomyCommand());
    }
    public static void sendCommandExecutionError(@NotNull final ICommandSender sender, @Nullable final Throwable e) {
        try {
            String errorMessage = LocalizationModule.translate(Defs.LangKeys.COMMAND_EXECUTION_ERROR);
            if (errorMessage == null) {
                ServerLogger.getLogger().error("Can't retrieve command execution error default message");
                return;
            }
            TextComponentString msg = new TextComponentString(errorMessage);
            msg.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(msg);
            ServerLogger.getLogger().error(errorMessage);
            if (e != null) ServerLogger.getLogger().error(StringHandler.getStackTrace(e));
        } catch (Exception ignored) {}
    }
    public static void sendCommandSyntaxError(@NotNull final ICommandSender sender, @Nullable final Throwable e) {
        try {
            String errorMessage = LocalizationModule.translate(Defs.LangKeys.COMMAND_SYNTAX_ERROR);
            if (errorMessage == null) {
                ServerLogger.getLogger().error("Can't retrieve command syntax error default message");
                return;
            }
            TextComponentString msg = new TextComponentString(errorMessage);
            msg.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(msg);
            ServerLogger.getLogger().error(errorMessage);
            if (e != null) ServerLogger.getLogger().error(StringHandler.getStackTrace(e));
        } catch (Exception ignored) {}
    }
}
