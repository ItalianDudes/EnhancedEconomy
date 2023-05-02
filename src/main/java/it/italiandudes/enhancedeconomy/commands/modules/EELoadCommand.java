package it.italiandudes.enhancedeconomy.commands.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleAlreadyLoadedException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.util.Defs;
import it.italiandudes.enhancedeconomy.util.Defs.LangKeys;
import it.italiandudes.enhancedeconomy.util.Defs.ModuleNames;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class EELoadCommand extends CommandBase {

    // Attributes
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = true;

    // Command Info
    @Override @NotNull
    public String getName() {
        return Defs.Commands.EE_LOAD;
    }
    @Override @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        try {
            String usage = LocalizationModule.translate(Defs.LangKeys.COMMAND_USAGE_EELOAD);
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
    public boolean checkPermission(@NotNull MinecraftServer server, @NotNull ICommandSender sender) {
        return super.checkPermission(server, sender);
    }

    // Command Body
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args) {
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
        
        boolean commandError = false;
        for (int i = 0; i< args.length && !commandError; i++) {
            try {
                switch (args[i].toLowerCase()) {
                    case ModuleNames.MODULE_DBCONNECTION:
                        if (i + 1 < args.length) {
                            i++;
                            try {
                                sender.sendMessage(
                                    new TextComponentString(
                                        TextFormatting.AQUA +
                                        LocalizationModule.translate(LangKeys.COMMAND_LOADING_STARTED) +
                                        ModuleNames.MODULE_DBCONNECTION
                                    )
                                );
                                DBConnectionModule.load(args[i], !(sender instanceof EntityPlayerMP));
                                sender.sendMessage(
                                    new TextComponentString(
                                        TextFormatting.AQUA +
                                        LocalizationModule.translate(LangKeys.COMMAND_LOADING_SUCCESS) +
                                        ModuleNames.MODULE_DBCONNECTION
                                    )
                                );
                            } catch (ModuleAlreadyLoadedException e) {
                                sender.sendMessage(
                                    new TextComponentString(
                                        TextFormatting.AQUA +
                                                LocalizationModule.translate(LangKeys.MODULE_ALREADY_LOADED)
                                    )
                                );
                            } catch (ModuleException e) {
                                sender.sendMessage(
                                    new TextComponentString(
                                        TextFormatting.RED +
                                        LocalizationModule.translate(LangKeys.COMMAND_LOADING_FAIL) +
                                        ModuleNames.MODULE_DBCONNECTION
                                    )
                                );
                            }
                        }else {
                            CommandsModule.sendCommandSyntaxError(sender, null);
                            commandError = true;
                        }
                        break;

                    case ModuleNames.MODULE_LOCALIZATION:
                        if (i + 1 < args.length) {
                            i++;
                            try {
                                sender.sendMessage(
                                    new TextComponentString(
                                        TextFormatting.AQUA +
                                        "Loading Module: " +
                                        ModuleNames.MODULE_LOCALIZATION
                                    )
                                );
                                LocalizationModule.load(args[i], !(sender instanceof EntityPlayerMP));
                                sender.sendMessage(
                                    new TextComponentString(
                                        TextFormatting.AQUA +
                                        "Loading Module Success: " +
                                        ModuleNames.MODULE_LOCALIZATION
                                    )
                                );
                            } catch (ModuleAlreadyLoadedException e) {
                                sender.sendMessage(
                                    new TextComponentString(
                                        TextFormatting.AQUA + "Can't execute the command: the requested module is already loaded."
                                    )
                                );
                            } catch (ModuleException e) {
                                sender.sendMessage(
                                    new TextComponentString(
                                        TextFormatting.RED +
                                        "Loading Module Failed: " +
                                        ModuleNames.MODULE_LOCALIZATION
                                    )
                                );
                            }
                        }else {
                            CommandsModule.sendCommandSyntaxError(sender, null);
                            commandError = true;
                        }
                        break;

                    case ModuleNames.MODULE_COMMANDS:
                        try {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                    LocalizationModule.translate(LangKeys.COMMAND_LOADING_STARTED) +
                                    ModuleNames.MODULE_COMMANDS
                                )
                            );
                            CommandsModule.load();
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                    LocalizationModule.translate(LangKeys.COMMAND_LOADING_SUCCESS) +
                                    ModuleNames.MODULE_COMMANDS
                                )
                            );
                        } catch (ModuleAlreadyLoadedException e) {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.AQUA +
                                            LocalizationModule.translate(LangKeys.MODULE_ALREADY_LOADED)
                                )
                            );
                        } catch (ModuleException e) {
                            sender.sendMessage(
                                new TextComponentString(
                                    TextFormatting.RED +
                                    LocalizationModule.translate(LangKeys.COMMAND_LOADING_FAIL) +
                                    ModuleNames.MODULE_COMMANDS
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
