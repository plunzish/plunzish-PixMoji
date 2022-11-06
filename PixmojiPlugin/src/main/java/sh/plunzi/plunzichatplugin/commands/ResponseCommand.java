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
import sh.plunzi.plunzichatplugin.chatSending.Debug;

import java.util.UUID;

public class ResponseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;

        if(args.length < 1) return false;

        if(!(sender instanceof Player || sender instanceof ConsoleCommandSender)) {
            chatHandler.sendCommandFeedback("You're neither a player nor a console, so no one can message you", true, sender);
            return false;
        }

        UUID uuid = sender instanceof Player ? ((Player) sender).getUniqueId() : PlunziChatPlugin.CONSOLE_UUID;

        if(!chatHandler.lastResponseHashMap.containsKey(uuid)) {
            chatHandler.sendCommandFeedback("You have nobody to respond to", true, sender);
            return false;
        }

        StringBuilder messageContent = new StringBuilder();
        for(String element: args) {
            messageContent.append(element).append(" ");
        }

        UUID receiverUUID = chatHandler.lastResponseHashMap.get(uuid);

        CommandSender receiver = (receiverUUID.equals(PlunziChatPlugin.CONSOLE_UUID)) ? Bukkit.getServer().getConsoleSender() : Bukkit.getPlayer(receiverUUID);

        chatHandler.sendPrivateMessage(messageContent.toString(), sender, receiver);

        return true;
    }
}
