package it.italiandudes.enhancedeconomy.commands;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.util.Defs;
import it.italiandudes.enhancedeconomy.util.Defs.LangKeys;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;
import scala.actors.threadpool.Arrays;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("deprecation")
public final class EnhancedEconomyCommand extends CommandBase {

    // Command Name
    public static final boolean RUN_WITH_MODULE_NOT_LOADED = true;

    @Override @NotNull
    public String getName() {
        return "enhancedeconomy";
    }

    @SuppressWarnings("unchecked")
    @Override @NotNull
    public List<String> getAliases() {
        return (List<String>) Arrays.asList(new String[]{
            getName(),
            "ee"
        });
    }

    @Override @NotNull
    public String getUsage(@NotNull final ICommandSender sender) {
        if (sender instanceof EntityPlayerMP) {
            return I18n.format("commands.enhancedeconomy.enhancedeconomy.usage");
        }else {
            return '/'+getName()+" <info>";
        }
    }

    @Override
    public void execute(@NotNull final MinecraftServer server, @NotNull final ICommandSender sender, final String @NotNull [] args) throws CommandException {
        if (args.length < 1) {
            if (sender instanceof EntityPlayerMP) {
                sender.sendMessage(
                        new TextComponentString(
                                TextFormatting.RED +
                                I18n.format("commands.enhancedeconomy.syntax_error")
                        )
                );
            }
            return;
        }

        try {

            switch (args[0].toLowerCase()) {
                case Arguments.INFO:
                case Arguments.INFO -> sender.sendMessage(
                        ChatColor.AQUA +
                                LocalizationModule.translate(LangKeys.EE_INFO)
                );
                case Arguments.VERSION -> sender.sendMessage(
                        ChatColor.AQUA +
                                LocalizationModule.translate(LangKeys.EE_VERSION) +
                                Defs.ModInfo.PLUGIN_VERSION
                );
                default -> sender.sendMessage(
                        ChatColor.RED +
                                LocalizationModule.translate(LangKeys.COMMAND_SYNTAX_ERROR)
                );
            }

        } catch (ModuleException e) {
            CommandsModule.sendDefaultError(sender, e);
        }
    }

    // Command Arguments
    public static final class Arguments {
        public static final String INFO = "info";
        public static final String VERSION = "version";
    }

    // Command Body
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {

}
