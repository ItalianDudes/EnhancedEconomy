package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.commands.EnhancedEconomyCommand;
import it.italiandudes.enhancedeconomy.commands.currencies.EECurrency;
import it.italiandudes.enhancedeconomy.commands.modules.EELoadCommand;
import it.italiandudes.enhancedeconomy.commands.modules.EEReloadCommand;
import it.italiandudes.enhancedeconomy.commands.modules.EEUnloadCommand;
import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
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

    // Attributes
    public static final String NAME = Defs.ModuleNames.MODULE_COMMANDS;
    private static boolean isModuleLoaded = false;

    // Module Checker
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isModuleLoaded() {
        return isModuleLoaded;
    }

    // Load Fail Handler
    public static void handleLoadFail(@Nullable final Throwable e) {
        throw new RuntimeException(e);
    }

    // Default Constructor
    public CommandsModule() {
        throw new RuntimeException("Can't instantiate this class!");
    }

    // Methods
    public static void load() {
        isModuleLoaded = true;
    }
    public static void load(@NotNull final FMLServerStartingEvent event) {
        event.registerServerCommand(new EnhancedEconomyCommand());
        event.registerServerCommand(new EELoadCommand());
        event.registerServerCommand(new EEUnloadCommand());
        event.registerServerCommand(new EEReloadCommand());
        event.registerServerCommand(new EECurrency());
        isModuleLoaded = true;
    }
    public static void unload(){
        isModuleLoaded = false;
    }
    public static void reload(){
        CommandsModule.unload();
        CommandsModule.load();
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
    public static void sendMissingPermissionsError(@NotNull final ICommandSender sender) {
        try {
            sender.sendMessage(
                    new TextComponentString(
                            TextFormatting.RED +
                                    LocalizationModule.translate(Defs.LangKeys.COMMAND_MISSING_PERMISSIONS)
                    )
            );
        } catch (ModuleException ignored) {}
    }
    public static void sendModuleNotLoadedError(@NotNull final ICommandSender sender) {
        try {
            sender.sendMessage(
                    new TextComponentString(
                            TextFormatting.RED +
                                    LocalizationModule.translate(Defs.LangKeys.COMMAND_MODULE_NOT_LOADED)
                    )
            );
        } catch (ModuleException ignored) {}
    }
    public static void sendAllRequiredModulesAreNotLoadedError(@NotNull final ICommandSender sender) {
        try {
            sender.sendMessage(
                    new TextComponentString(
                            TextFormatting.RED +
                                    LocalizationModule.translate(Defs.LangKeys.ALL_REQUIRED_MODULES_ARE_NOT_LOADED)
                    )
            );
        } catch (ModuleException ignored) {}
    }
}
