package sh.plunzi.plunzichatplugin.chatSending;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.messages.Censorship;
import sh.plunzi.plunzichatplugin.chatSending.messages.Message;
import sh.plunzi.plunzichatplugin.chatSending.messages.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class ChatHandler {
    public HashMap<UUID, UUID> lastResponseHashMap = new HashMap<>();

    private void sendMessage(Message message) {

        if(message.isIllegal() && message.getPlayer1() instanceof Player) {
            //Idk about this function, might remove it
            return;
        }

        switch (message.getType()) {
            case PUBLIC:
            case BROADCAST:
                Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                for(Player player : players) {
                    displayMessage(player, message, true);
                }
                Bukkit.getConsoleSender().sendMessage(message.getContent(Censorship.NONE));
                break;
            case PRIVATE_IN:
                CommandSender receiver = message.getPlayer2();
                displayMessage(receiver, message, (message.getPlayer1() instanceof ConsoleCommandSender || message.getPlayer2() instanceof ConsoleCommandSender));
                break;
            case PRIVATE_OUT:
                CommandSender author = message.getPlayer1();
                displayMessage(author, message, (message.getPlayer1() instanceof ConsoleCommandSender || message.getPlayer2() instanceof ConsoleCommandSender));
                break;
            case PARTY:
                List<UUID> partyPlayers = PlunziChatPlugin.PARTYSYSTEM.parties.get(((Player) message.getPlayer2()).getUniqueId());

                for(UUID player : partyPlayers) {
                    displayMessage(Bukkit.getPlayer(player), message, false);
                }
                break;
        }
    }

    private void displayMessage(CommandSender sender, Message message, boolean consoleCanSee) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(message.getContent(PlunziChatPlugin.DATABASE_MANAGER.getCensorLevel(player.getUniqueId())));
            if(message.getRawContent().contains("@" + player.getName())) {
                Sound sound = Sound.sound(PlunziChatPlugin.CHAT_PING_SOUND, Sound.Source.VOICE, 100, 1);
                player.playSound(sound);
            }
            return;
        }
        if(consoleCanSee) {
            Bukkit.getConsoleSender().sendMessage(message.getContent(Censorship.NONE));
        }
    }

    public void sendMessage(String messageContent, CommandSender author) {
        sendMessage(new Message(messageContent, author, MessageType.PUBLIC, null));
    }

    public void broadcastMessage(String messageContent, CommandSender sender) {
        sendMessage(new Message(messageContent, sender, MessageType.BROADCAST, null));
    }

    public void sendPrivateMessage(String messageContent, CommandSender author, CommandSender receiver) {
        Message messageIn = new Message(messageContent, author, MessageType.PRIVATE_IN, receiver);
        Message messageOut = new Message(messageContent, author, MessageType.PRIVATE_OUT, receiver);

        sendMessage(messageIn);
        sendMessage(messageOut);

        if (author instanceof Player && receiver instanceof Player) {
            lastResponseHashMap.put(((Player) receiver).getUniqueId(), ((Player) author).getUniqueId());
        } else if (!(receiver instanceof Player) && author instanceof Player) {
            lastResponseHashMap.put(PlunziChatPlugin.CONSOLE_UUID, ((Player) author).getUniqueId());
        } else if (receiver instanceof Player) {
            lastResponseHashMap.put(((Player) receiver).getUniqueId(), PlunziChatPlugin.CONSOLE_UUID);
        }
    }

    public void sendCommandFeedback(Component text, boolean error, CommandSender sender) {
        Component message = PlunziChatPlugin.PREFIX.append(text
                .colorIfAbsent(TextColor.color(error ? 0xfe3f3f : 0xbebebe)));
        sender.sendMessage(message);
    }

    public void sendCommandFeedback(String text, boolean error, CommandSender sender) {
        sendCommandFeedback(Component.text(text), error, sender);
    }
}

