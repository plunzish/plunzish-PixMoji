package sh.plunzi.plunzichatplugin.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;

public class ChatListener implements Listener {

    ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;

    @EventHandler
    public void playerChatEvent(AsyncChatEvent event) {

        Player player = event.getPlayer();
        event.setCancelled(true); //lol fuck Mojangs chat reporting :P   We do our own chat reporting and we do it better ÙwÚ
        String messageContent = PlainTextComponentSerializer.plainText().serialize(event.message());

        chatHandler.sendMessage(messageContent, player);
    }

}