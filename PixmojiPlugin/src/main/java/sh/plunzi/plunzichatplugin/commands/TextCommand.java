package sh.plunzi.plunzichatplugin.commands;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;

public class TextCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        StringBuilder messageContent = new StringBuilder();
        for(String arg : args) {
            messageContent.append(arg).append(" ");
        }
        PlunziChatPlugin.CHAT_HANDLER.sendMessage(messageContent.substring(0, messageContent.toString().length() - 1), sender);
        return true;
    }
}
