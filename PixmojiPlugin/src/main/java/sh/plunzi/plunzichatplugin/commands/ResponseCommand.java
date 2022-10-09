package sh.plunzi.plunzichatplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;

import java.util.UUID;

public class ResponseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;

        if(args.length < 1) return false;

        if(!(sender instanceof Player)) {
            chatHandler.sendCommandFeedback("You're not a player, so no one can message you", true, sender);
            return false;
        }

        Player player = (Player) sender;
        if(!chatHandler.lastResponseHashMap.containsKey(player.getUniqueId())) {
            chatHandler.sendCommandFeedback("You have nobody to respond to", true, player);
            return false;
        }

        StringBuilder messageContent = new StringBuilder();
        for(String element: args) {
            messageContent.append(element).append(" ");
        }

        UUID receiverUUID = chatHandler.lastResponseHashMap.get(player.getUniqueId());

        if (receiverUUID == UUID.fromString("00000000-0000-0000-0000-000000000000")) {
            Bukkit.getConsoleSender().sendMessage(player.getName() + " replied: " + messageContent);
            return true;
        }

        Player receiver = Bukkit.getPlayer(receiverUUID);



        chatHandler.sendPrivateMessage(messageContent.toString(), sender, receiver);

        return true;
    }
}
