package sh.plunzi.plunzichatplugin.chatSending.messages;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.RegExp;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmoji;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmojis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    private final Player author;
    private final Player receiver;

    private final MessageType messageType;

    private final Component contentUncensored;
    private final Component contentLightlyCensored;
    private final Component contentHeavilyCensored;

    public Message(String rawMessage, Player author, MessageType messageType, Player receiver) {

        Player player = author;
        if(messageType == MessageType.PRIVATE_OUT) {
            player = receiver;
        }

        this.contentUncensored = stringToComponent(rawMessage, player, 0, messageType);
        this.contentLightlyCensored = stringToComponent(rawMessage, player, 1, messageType);
        this.contentHeavilyCensored = stringToComponent(rawMessage, player, 2, messageType);

        this.messageType = messageType;

        this.author = author;
        this.receiver = receiver;
    }

    public TextComponent stringToComponent(String string, Player player, int censorLevel, MessageType messageType) {
        String message = censorString(string, censorLevel);

        TextComponent messageComponent = Component.text(getPlayerNameFormatted(player.getName(), messageType));
        messageComponent = messageComponent.append(Component.text(message));

        messageComponent = handlePixmojis(messageComponent);
        messageComponent = handleAts(messageComponent);
        message = PlainTextComponentSerializer.plainText().serialize(messageComponent);
        message = message.replace(getPlayerNameFormatted(player.getName(), messageType), "");

        Pixmoji pixmoji = Pixmojis.nullmoji;
        try {
            pixmoji = PlunziChatPlugin.PIXMOJIS.getPixmoji(message.charAt(0));
        } catch (IndexOutOfBoundsException ignored) {}

        if(pixmoji.exists(PlunziChatPlugin.PIXMOJIS) && message.length() == 1) {

            messageComponent = Component.text(getPlayerNameFormatted(player.getName(), messageType))
                    .style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_TRANSPARENT).build());

            messageComponent = messageComponent.append(Component.text(pixmoji.getUnicodeChar() + "\n")
                    .style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_LARGE).build()));

            messageComponent = messageComponent.append(Component.text(getPlayerNameFormatted(player.getName(), messageType) + "\n" )
                    .style(Style.style().font(Key.key("minecraft", "default")).build()));

            messageComponent = messageComponent.append(Component.text(getPlayerNameFormatted(player.getName(), messageType))
                    .style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_TRANSPARENT).build()));
        }

        return messageComponent;
    }

    protected String getPlayerNameFormatted(String playerName, MessageType messageType) {
        switch (messageType) {
            default:
                return PlunziChatPlugin.FILE_MANAGER.getPublicMessageFormatting().replace("%s", playerName);
            case PRIVATE_IN:
                return PlunziChatPlugin.FILE_MANAGER.getPrivateMessageFormattingIn().replace("%s", playerName);
            case PRIVATE_OUT:
                return PlunziChatPlugin.FILE_MANAGER.getPrivateMessageFormattingOut().replace("%s", playerName);
        }
    }

    private TextComponent handleAts(TextComponent inputTextComponent) {

        String input = PlainTextComponentSerializer.plainText().serialize(inputTextComponent);

        String patternAsString = "@.+?(\\W|\\Z)";
        Pattern pattern = Pattern.compile(patternAsString);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if(Bukkit.getServer().getPlayerExact(matcher.group(0).replaceAll("(@|\\s)+","")) != null) {

                @RegExp String replace = matcher.group(0);

                inputTextComponent = (TextComponent) inputTextComponent.replaceText(TextReplacementConfig.builder()
                        .match(replace).replacement(
                                Component.text(replace).style(
                                        Style.style()
                                                .color(TextColor.color(HSVLike.fromRGB(162, 224, 254)))
                                                .decoration(TextDecoration.BOLD, true)
                                                .build()
                                )
                        ).build());
            }
        }
        return inputTextComponent;
    }

    private TextComponent handlePixmojis(TextComponent inputTextComponent) {

        String input = PlainTextComponentSerializer.plainText().serialize(inputTextComponent);
        StringBuilder patternBuilder = new StringBuilder(":(");

        for(Pixmoji pixmoji : PlunziChatPlugin.PIXMOJIS.pixmojiList) {
            patternBuilder.append(pixmoji.getName()).append("|");
        }
        patternBuilder = new StringBuilder(patternBuilder.substring(0, patternBuilder.length() - 1));
        patternBuilder.append("):");

        Pattern emojiPattern = Pattern.compile(patternBuilder.toString());
        Matcher matcher = emojiPattern.matcher(input);

        while (matcher.find()) {
            @RegExp String pixmoji = matcher.group(0);
            inputTextComponent = (TextComponent) inputTextComponent.replaceText(TextReplacementConfig.builder()
                    .match(pixmoji).replacement(
                            Component.text(PlunziChatPlugin.PIXMOJIS.getPixmoji(pixmoji.replace(":", "")).getUnicodeChar()).style(
                                    Style.style()
                                            .font(PlunziChatPlugin.PIXMOJI_FONT)
                                            .build()
                            )
                    ).build());
        }

        return inputTextComponent;
    }


    public String censorString(String string, int censorLevel) {

        switch (censorLevel) {
            case 1:
                String censoredMessageLight = string;

                for(String censoredWord : PlunziChatPlugin.FILE_MANAGER.getCensoredWords()) {

                    Pattern pattern = Pattern.compile(".*(" + censoredWord + ").*", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(string);
                    while (matcher.find()) {
                        censoredMessageLight = censoredMessageLight.replace(matcher.group(1), censorStringLightly(matcher.group(1)));
                    }
                }
                return censoredMessageLight;
            case 2:
                String censoredMessage = string;

                for(String censoredWord : PlunziChatPlugin.FILE_MANAGER.getCensoredWords()) {
                    Pattern pattern = Pattern.compile(".*(" + censoredWord + ").*", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(string);
                    while (matcher.find()) {
                        censoredMessage = censoredMessage.replace(matcher.group(1), censorStringHeavily(matcher.group(1)));
                    }
                }
                return censoredMessage;
        }
        return string;
    }

    private String censorStringLightly(String input) {
        return input.replaceAll("[aeiou]", "*");
    }
    private String censorStringHeavily(String input) {
        return "*".repeat(input.length());
    }

    public Component getUncensoredMessage() {
        return contentUncensored;
    }
    public Component getContentLightlyCensored() {
        return contentLightlyCensored;
    }
    public Component getContentHeavilyCensored() {
        return contentHeavilyCensored;
    }

    public Player getAuthor() {
        return author;
    }

    public String getRawContent() {
        return PlainTextComponentSerializer.plainText().serialize(this.contentUncensored);
    }

    public boolean isIllegal() {
        for(String word : PlunziChatPlugin.FILE_MANAGER.getCensoredWords(true)) {
            if(getRawContent().contains(word)) {
                return true;
            }
        }
        return false;
    }

    public Player getReceiver() {
        return receiver;
    }

    public MessageType getType() {
        return messageType;
    }
}
