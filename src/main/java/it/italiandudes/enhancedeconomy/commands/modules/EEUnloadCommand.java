package it.italiandudes.enhancedeconomy.commands.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleNotLoadedException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.util.Defs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class EEUnloadCommand extends CommandBase {

    // Attributes
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = true;

    // Command Info
    @Override @NotNull
    public String getName() {
        return Defs.Commands.EE_UNLOAD;
    }
    @Override @NotNull
    public String getUsage(@NotNull final ICommandSender sender) {
        try {
            String usage = LocalizationModule.translate(Defs.LangKeys.COMMAND_USAGE_EEUNLOAD);
            if (usage != null) return usage;
            throw new RuntimeException("Usage localization is null");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int getRequiredPermissionLevel() {
        return super.getRequiredPermissionLevel(); // MAX OP LEVEL
    }
    @Override
    public boolean checkPermission(@NotNull final MinecraftServer server, @NotNull final ICommandSender sender) {
        return super.checkPermission(server, sender);
    }

    // Subcommand Body
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

        for (String module : args) {
            try {
                switch (module) {
                    case Defs.ModuleNames.MODULE_DBCONNECTION:
                        try {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                    LocalizationModule.translate(Defs.LangKeys.COMMAND_UNLOADING_STARTED) +
                                    Defs.ModuleNames.MODULE_DBCONNECTION
                                )
                            );
                            DBConnectionModule.unload(!(sender instanceof EntityPlayerMP));
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                    LocalizationModule.translate(Defs.LangKeys.COMMAND_UNLOADING_SUCCESS) +
                                    Defs.ModuleNames.MODULE_DBCONNECTION
                                )
                            );
                        } catch (ModuleNotLoadedException e) {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                            LocalizationModule.translate(Defs.LangKeys.MODULE_NOT_LOADED)
                                )
                            );
                        } catch (ModuleException e) {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.RED +
                                    LocalizationModule.translate(Defs.LangKeys.COMMAND_UNLOADING_FAIL) +
                                    Defs.ModuleNames.MODULE_DBCONNECTION
                                )
                            );
                        }
                        break;

                    case Defs.ModuleNames.MODULE_LOCALIZATION:
                        try {
                            sender.sendMessage(
                                new TextComponentString(
                                TextFormatting.AQUA +
                                    "Unloading Module: " +
                                    Defs.ModuleNames.MODULE_LOCALIZATION
                                )
                            );
                            LocalizationModule.unload(!(sender instanceof EntityPlayerMP));
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                    "Unloading Module Success: " +
                                    Defs.ModuleNames.MODULE_LOCALIZATION
                                )
                            );
                        } catch (ModuleNotLoadedException e) {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                            "Can't execute the command: the requested module is not loaded."
                                )
                            );
                        } catch (ModuleException e) {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.RED +
                                    "Unloading Module Failed: " +
                                    Defs.ModuleNames.MODULE_LOCALIZATION
                                )
                            );
                        }
                        break;

                    case Defs.ModuleNames.MODULE_COMMANDS:
                        try {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                    LocalizationModule.translate(Defs.LangKeys.COMMAND_UNLOADING_STARTED) +
                                    Defs.ModuleNames.MODULE_COMMANDS
                                )
                            );
                            CommandsModule.unload();
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                    LocalizationModule.translate(Defs.LangKeys.COMMAND_UNLOADING_SUCCESS) +
                                    Defs.ModuleNames.MODULE_COMMANDS
                                )
                            );
                        } catch (ModuleNotLoadedException e) {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                            LocalizationModule.translate(Defs.LangKeys.MODULE_NOT_LOADED)
                                )
                            );
                        } catch (ModuleException e) {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.RED +
                                    LocalizationModule.translate(Defs.LangKeys.COMMAND_UNLOADING_FAIL) +
                                    Defs.ModuleNames.MODULE_COMMANDS
                                )
                            );
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
}
