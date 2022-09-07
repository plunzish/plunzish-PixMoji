package sh.plunzi.plunzichatplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.dataUtils.DatabaseManager;

import java.util.Arrays;

public class BroadcastCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;
        DatabaseManager databaseManager = PlunziChatPlugin.DATABASE_MANAGER;

        if(sender instanceof Player) {
            if(!databaseManager.isPlayerAdmin(((Player) sender).getUniqueId())) {
                sender.sendMessage("§cYou can't do that");
                return false;
            }
        }

        if(args.length < 1) {
            sender.sendMessage("\u00a7cNot enough arguments, you can't just broadcast nothing");
            return false;
        }

        StringBuilder messageContent = new StringBuilder();
        for(String element: args) {
            messageContent.append(element).append(" ");
        }

        chatHandler.broadcastMessage(messageContent.toString(), sender);

        return true;
    }
}
