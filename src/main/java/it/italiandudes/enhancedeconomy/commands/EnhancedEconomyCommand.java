package it.italiandudes.enhancedeconomy.commands;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
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
            String localizedMsg = LocalizationModule.translate(LangKeys.EE_INFO);
            if (localizedMsg == null) throw new ModuleException("Localization failed");
            TextComponentString msg = new TextComponentString(localizedMsg);
            msg.getStyle().setColor(TextFormatting.AQUA);
            sender.sendMessage(msg);
        } catch (ModuleException e) {
            CommandsModule.sendCommandExecutionError(sender, e);
        }
    }
}
