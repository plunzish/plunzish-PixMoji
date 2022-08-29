package sh.plunzi.plunzichatplugin.chatSending;

import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.messages.Message;
import sh.plunzi.plunzichatplugin.chatSending.messages.MessageType;


public class ChatHandler {

    private void sendMessage(Message message) {

        if(message.isIllegal()) {
            notifyAdminsIllegalMessage(message.getAuthor(), message.getRawContent());
            return;
        }

        switch (message.getType()) {
            case PUBLIC:
            case BROADCAST:
                Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                for(Player player : players) {
                    displayMessage(player, message);
                }
                break;
            case PRIVATE_IN:
                Player receiver = message.getReceiver();
                displayMessage(receiver, message);
                break;
            case PRIVATE_OUT:
                Player author = message.getAuthor();
                displayMessage(author, message);
                break;
        }
    }

    private void displayMessage(Player player, Message message) {

        /*TODO Config for censoring
        player.sendMessage(Component.text("None: \n").append(message.getUncensoredMessage()));
        player.sendMessage(Component.text("Medium: \n").append(message.getContentLightlyCensored()));
        player.sendMessage(Component.text("High: \n").append(message.getContentHeavilyCensored()));
        */


        player.sendMessage(message.getContentHeavilyCensored());

        if(message.getRawContent().contains("@" + player.getName())) {
            Sound sound = Sound.sound(PlunziChatPlugin.CHAT_PING_SOUND, Sound.Source.VOICE, 100, 1);
            player.playSound(sound);
        }
    }

    public void sendMessage(String messageContent, Player author) {
        sendMessage(new Message(messageContent, author, MessageType.PUBLIC, null));
    }

    public void sendPrivateMessage(String messageContent, Player author, Player receiver) {
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

