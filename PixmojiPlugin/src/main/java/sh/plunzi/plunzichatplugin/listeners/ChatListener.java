package sh.plunzi.plunzichatplugin.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmoji;

public class ChatListener implements Listener {

    @EventHandler
    public void playerChatEvent(AsyncChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true); //lol fuck Mojangs chat reporting :P

        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        String[] splitMessage = message.split(":");

        TextComponent messageComponent = Component.text("<" + player.getName() + "> ");

        TextComponent emojiComponent;

        if(message.length() == 1 && PlunziChatPlugin.pixmojis.isInList(message.charAt(0))) {
            messageComponent = messageComponent.style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_TRANSPARENT).build());

            emojiComponent = Component.text(message + "\n").style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_LARGE).build());
            messageComponent = emojiComponent.append(Component.text("<" + player.getName() + ">\n").style(Style.style().font(Key.key("minecraft","default")).build()));
            messageComponent = messageComponent.append(Component.text("<" + player.getName() + ">").style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_TRANSPARENT).build()));

            player.sendMessage(messageComponent);
            return;
        }

        TextComponent textComponent;

        for (int i = 0; i < splitMessage.length; i++) {
            String element = splitMessage[i];

            Pixmoji pixmoji = PlunziChatPlugin.pixmojis.getByName(element);

            if(pixmoji != null) {
                emojiComponent = Component.text(pixmoji.getUnicodeChar() + "");
                emojiComponent = emojiComponent.style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT).build());

                messageComponent = messageComponent.append(emojiComponent);

            } else {

                textComponent = Component.text(element);

                if(shouldGetColon(i, message)) {
                    textComponent = textComponent.append(Component.text(":"));
                }


                textComponent.style().font(Key.key("minecraft", "default"));

                textComponent = textComponent.style(Style.style().font(Key.key("minecraft", "default")).build());


                messageComponent = messageComponent.append(textComponent);
            }
        }

        player.sendMessage(messageComponent);

    }

    private boolean shouldGetColon(int i, String message) {
        String[] splitMessage = message.split(":");

        if((i >= splitMessage.length - 1)) {
            return message.endsWith(":");
        } else {
            return !PlunziChatPlugin.pixmojis.isInList(splitMessage[i + 1]);
        }
    }

}
