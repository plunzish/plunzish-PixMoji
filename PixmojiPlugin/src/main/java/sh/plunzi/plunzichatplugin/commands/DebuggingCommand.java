package sh.plunzi.plunzichatplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.chatSending.messages.Message;
import sh.plunzi.plunzichatplugin.utils.FileManager;
import sh.plunzi.plunzichatplugin.utils.OtherUtils;

import java.util.Arrays;

public class DebuggingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;

        chatHandler.sendCommandFeedback(

                Component.text("@Ping" + sender.getName()).style(
                Style.style()
                        .color(PlunziChatPlugin.FILE_MANAGER.getPingColor())
                        .decoration(TextDecoration.BOLD, true)
                        .build()).append(

                        Component.text(" text ").style(
                                Style.style()
                                        .color(PlunziChatPlugin.FILE_MANAGER.getTextColor())
                                        .decoration(TextDecoration.BOLD, false)
                                        .build()).append(

                        Component.text("@Mention").style(
                                Style.style()
                                        .color(PlunziChatPlugin.FILE_MANAGER.getMentionColor())
                                        .decoration(TextDecoration.BOLD, false)
                                        .build())).append(

                                Component.text("  Filepath: " + PlunziChatPlugin.FILE_MANAGER.file.getAbsolutePath()).style(
                                        Style.style()
                                                .color(PlunziChatPlugin.FILE_MANAGER.getTextColor())
                                                .decoration(TextDecoration.BOLD, false)
                                                .build()))
                ),


                false, sender);

        return true;
    }
}
