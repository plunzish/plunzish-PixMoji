package sh.plunzi.plunzichatplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;

import java.util.Arrays;
import java.util.List;

public class PrivateMessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;

        if(!((sender instanceof Player) || (sender instanceof ConsoleCommandSender))) {
            return false;
        }

        if(args.length <= 1) {
            chatHandler.sendCommandFeedback("Not enough arguments", true, sender);
            chatHandler.sendCommandFeedback("Usage: " + (sender instanceof Player ? "/" : "") + command + " <receiver> <message>", true, sender);
            return false;
        }

        List<Player> receivers = PlunziChatPlugin.UTILS.stringToPlayers(args[0], sender, true);

        StringBuilder messageContent = new StringBuilder();
        args = Arrays.copyOfRange(args, 1, args.length);
        for(String element : args) {
            messageContent.append(element).append(" ");
        }

        for(Player receiver : receivers) {
            chatHandler.sendPrivateMessage(messageContent.toString(), sender, receiver);
        }

        return true;
    }
}
