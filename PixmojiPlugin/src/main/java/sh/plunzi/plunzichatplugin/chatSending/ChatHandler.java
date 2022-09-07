package sh.plunzi.plunzichatplugin.chatSending;

import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.messages.Censorship;
import sh.plunzi.plunzichatplugin.chatSending.messages.Message;
import sh.plunzi.plunzichatplugin.chatSending.messages.MessageType;


public class ChatHandler {

    private void sendMessage(Message message) {

        if(message.isIllegal() && message.getAuthor() instanceof Player) {
            notifyAdminsIllegalMessage((Player) message.getAuthor(), message.getRawContent());
            return;
        }

        switch (message.getType()) {
            case PUBLIC:
            case BROADCAST:
                Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                for(Player player : players) {
                    displayMessage(player, message);
                }
                Bukkit.getConsoleSender().sendMessage(message.getContent(Censorship.NONE));
                break;
            case PRIVATE_IN:
                Player receiver = message.getReceiver();
                displayMessage(receiver, message);
                break;
            case PRIVATE_OUT:
                CommandSender author = message.getAuthor();
                displayMessage(author, message);
                break;
        }
    }

    private void displayMessage(CommandSender sender, Message message) {
        if(sender instanceof Player) {

            Player player = (Player) sender;
            player.sendMessage(message.getContent(PlunziChatPlugin.DATABASE_MANAGER.getCensorLevel(player.getUniqueId())));
            if(message.getRawContent().contains("@" + player.getName())) {
                Sound sound = Sound.sound(PlunziChatPlugin.CHAT_PING_SOUND, Sound.Source.VOICE, 100, 1);
                player.playSound(sound);
            }

        }
    }

    public void sendMessage(String messageContent, Player author) {
        sendMessage(new Message(messageContent, author, MessageType.PUBLIC, null));
    }

    public void broadcastMessage(String messageContent, CommandSender sender) {
        sendMessage(new Message(messageContent, sender, MessageType.BROADCAST, null));
    }

    public void sendPrivateMessage(String messageContent, CommandSender author, Player receiver) {
        Message messageIn = new Message(messageContent, author, MessageType.PRIVATE_IN, receiver);
        Message messageOut = new Message(messageContent, author, MessageType.PRIVATE_OUT, receiver);

        sendMessage(messageIn);
        sendMessage(messageOut);
        //coming soon
    }

    private void notifyAdminsIllegalMessage(Player player, String context) {
        //coming soon
    }
}

