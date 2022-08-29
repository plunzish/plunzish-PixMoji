package sh.plunzi.plunzichatplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;

import java.util.Arrays;

public class PrivateMessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;

        if(!(sender instanceof Player)) {
            sender.sendMessage("\u00a7cYou are not a player");
            return false;
        }
        if(args.length <= 1) {
            sender.sendMessage("\u00a7cNot enough arguments.\nUsage: \u00a7e\u00a7l" + command + " <receiver> <message>");
            return false;
        }

        Player receiver = Bukkit.getPlayer(args[0]);
        if(receiver == null) {
            sender.sendMessage("\u00a7cCannot find Player '" + args[0] + "'");
            return false;
        }

        Player player = (Player) sender;

        StringBuilder messageContent = new StringBuilder();
        args = Arrays.copyOfRange(args, 1, args.length);
        for(String element: args) {
            messageContent.append(element).append(" ");
        }

        chatHandler.sendPrivateMessage(messageContent.toString(), player, receiver);

        return true;
    }
}
