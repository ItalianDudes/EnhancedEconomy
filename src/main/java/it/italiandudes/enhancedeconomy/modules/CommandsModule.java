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

import java.util.Objects;

@SuppressWarnings("unused")
public final class CommandsModule {

    // Default Constructor
    public CommandsModule() {
        throw new RuntimeException("Can't instantiate this class!");
    }
    public static void registerCommands(@NotNull final FMLServerStartingEvent event) {
        event.registerServerCommand(new EnhancedEconomyCommand());
    }
    public static void sendDefaultError(@NotNull final ICommandSender sender, String message, @Nullable final Throwable e) {
        try {
            TextComponentString msg = new TextComponentString(message);
            msg.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(msg);
            ServerLogger.getLogger().error(message);
            if (e != null) ServerLogger.getLogger().error(StringHandler.getStackTrace(e));
        } catch (Exception ignored) {}
    }
}
