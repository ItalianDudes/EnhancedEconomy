package it.italiandudes.enhancedeconomy.commands;

import com.google.common.collect.Lists;
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

import java.util.List;

@SuppressWarnings("unused")
public final class EnhancedEconomyCommand extends CommandBase {

    // Command Name
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = true;

    @Override @NotNull
    public String getName() {
        return Defs.Commands.COMMAND_NAME[0];
    }
    @Override @NotNull
    public List<String> getAliases() {
        return Lists.newArrayList(Defs.Commands.COMMAND_NAME);
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

        try {
            switch (args[0].toLowerCase()) {
                case Arguments.INFO:
                    sender.sendMessage(
                        new TextComponentString(
                                TextFormatting.AQUA +
                                        LocalizationModule.translate(LangKeys.EE_INFO)
                        )
                    );
                    break;

                case Arguments.VERSION:
                    sender.sendMessage(
                        new TextComponentString(
                                TextFormatting.AQUA +
                                        LocalizationModule.translate(LangKeys.EE_VERSION) +
                                        Defs.ModInfo.VERSION
                        )
                    );
                    break;

                default:
                    CommandsModule.sendCommandSyntaxError(sender, null);
                    break;
            }
        } catch (ModuleException e) {
            throw new RuntimeException();
        }
    }

    // Command Arguments
    public static final class Arguments {
        public static final String INFO = "info";
        public static final String VERSION = "version";
    }
}
