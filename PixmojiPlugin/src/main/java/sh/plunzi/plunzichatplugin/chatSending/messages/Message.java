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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.RegExp;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmoji;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmojis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    private final CommandSender author;
    private final Player receiver;

    private final MessageType messageType;

    private final Component contentUncensored;
    private final Component contentLightlyCensored;
    private final Component contentHeavilyCensored;

    public Message(String rawMessage, CommandSender author, MessageType messageType, Player receiver) {

        CommandSender sender = author;
        if(messageType == MessageType.PRIVATE_OUT) {
            sender = receiver;
        }

        this.contentUncensored = stringToComponent(rawMessage, sender.getName(), Censorship.NONE, messageType);
        this.contentLightlyCensored = stringToComponent(rawMessage, sender.getName(), Censorship.LIGHT, messageType);
        this.contentHeavilyCensored = stringToComponent(rawMessage, sender.getName(), Censorship.HEAVY, messageType);

        this.messageType = messageType;

        this.author = author;
        this.receiver = receiver;
    }

    public TextComponent stringToComponent(String string, String authorName, Censorship censorLevel, MessageType messageType) {
        String message = censorString(string, censorLevel);

        TextComponent messageComponent = Component.text(getPlayerNameFormatted(authorName, messageType));
        messageComponent = messageComponent.append(Component.text(message));

        messageComponent = handleFormatting(messageComponent);
        messageComponent = handlePixmojis(messageComponent);
        messageComponent = handleAts(messageComponent);

        message = PlainTextComponentSerializer.plainText().serialize(messageComponent);
        message = message.replace(getPlayerNameFormatted(authorName, messageType), "");

        Pixmoji pixmoji = Pixmojis.nullmoji;
        try {
            pixmoji = PlunziChatPlugin.PIXMOJIS.getPixmoji(message.charAt(0));
        } catch (IndexOutOfBoundsException ignored) {}

        if(pixmoji.exists(PlunziChatPlugin.PIXMOJIS) && message.length() == 1) {

            messageComponent = Component.text(getPlayerNameFormatted(authorName, messageType))
                    .style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_TRANSPARENT).build());

            messageComponent = messageComponent.append(Component.text(pixmoji.getUnicodeChar() + "\n")
                    .style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_LARGE).build()));

            messageComponent = messageComponent.append(Component.text(getPlayerNameFormatted(authorName, messageType) + "\n" )
                    .style(Style.style().font(Key.key("minecraft", "default")).build()));

            messageComponent = messageComponent.append(Component.text(getPlayerNameFormatted(authorName, messageType))
                    .style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_TRANSPARENT).build()));
        }

        return messageComponent;
    }

    protected String getPlayerNameFormatted(String playerName, MessageType messageType) {

        String playerNameFormatted = PlunziChatPlugin.FILE_MANAGER.getPublicMessageFormatting();
        switch (messageType) {
            case PRIVATE_IN:
                playerNameFormatted = PlunziChatPlugin.FILE_MANAGER.getPrivateMessageFormattingIn();
                break;
            case PRIVATE_OUT:
                playerNameFormatted = PlunziChatPlugin.FILE_MANAGER.getPrivateMessageFormattingOut();
                break;
            case BROADCAST:
                playerNameFormatted = PlunziChatPlugin.FILE_MANAGER.getBroadcastFormatting();
                break;
        }
        return playerNameFormatted.replace("%s", playerName);
    }

    private TextComponent handleAts(TextComponent inputTextComponent) {

        String input = PlainTextComponentSerializer.plainText().serialize(inputTextComponent);

        String patternAsString = "@([^\\s\\W]+)";
        Pattern pattern = Pattern.compile(patternAsString);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if(Bukkit.getServer().getPlayerExact(matcher.group(1)) != null) {

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

    private TextComponent handleFormatting(TextComponent inputTextComponent) {
    /*
        String input = PlainTextComponentSerializer.plainText().serialize(inputTextComponent);

        List<PixmojiFormatting> formatList = new ArrayList<>();

        @RegExp String patternItalic =        "([*_]([^_*]+?)[*_])[^*_]";
        @RegExp String patternBold =          "(\\*{2}([^_*]+?)\\*{2})[^*]";
        @RegExp String patternUnderlined =    "(_{2}([^_*]+?)_{2})[^_]";
        @RegExp String patternStrikethrough = "(~{2}([^_*]+?)~{2})[^~]";
        @RegExp String patternObfuscated =    "(~{3}([^_*]+?)~{3})[^~]";
        formatList.add(new PixmojiFormatting(patternBold, TextDecoration.BOLD));
        formatList.add(new PixmojiFormatting(patternUnderlined, TextDecoration.UNDERLINED));
        formatList.add(new PixmojiFormatting(patternItalic, TextDecoration.ITALIC));
        formatList.add(new PixmojiFormatting(patternStrikethrough, TextDecoration.STRIKETHROUGH));
        formatList.add(new PixmojiFormatting(patternObfuscated, TextDecoration.OBFUSCATED));

        for(PixmojiFormatting format : formatList) {
            Pattern formattingPattern = Pattern.compile(format.pattern);
            Matcher matcher = formattingPattern.matcher(input);

            while (matcher.find()) {
                @RegExp String string = matcher.group(1);
                String rawString = matcher.group(2);

                String nonRegexString = string;
                {
                nonRegexString  = nonRegexString.replace("[", "\\[")
                        .replace("]", "\\]")
                        .replace(".", "\\.")
                        .replace("§", "\\§")
                        .replace("{", "\\{")
                        .replace("}", "\\}")
                        .replace("*", "\\*")
                        .replace("(", "\\(")
                        .replace(")", "\\)")
                        .replace("\\", "\\\\")
                        .replace("+", "\\+")
                        .replace("|", "\\|")
                        .replace("?", "\\?")
                        .replace("<", "\\<")
                        .replace(">", "\\>");}

                inputTextComponent = (TextComponent) inputTextComponent.replaceText(TextReplacementConfig.builder()
                        .match(nonRegexString).replacement(
                                Component.text(rawString).style(
                                        Style.style()
                                                .decoration(format.textDecoration, true)
                                                .build()
                                )
                        ).build());
            }
        }*/

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

        Pattern emojiPattern = Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE);
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

    public String censorString(String string, Censorship censorLevel) {

        switch (censorLevel) {
            case LIGHT:
                String censoredMessageLight = string;

                for(String censoredWord : PlunziChatPlugin.FILE_MANAGER.getCensoredWords()) {

                    Pattern pattern = Pattern.compile(".*(" + censoredWord + ").*", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(string);
                    while (matcher.find()) {
                        censoredMessageLight = censoredMessageLight.replace(matcher.group(1), censorStringLightly(matcher.group(1)));
                    }
                }
                return censoredMessageLight;
            case HEAVY:
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

    public Component getContent(Censorship censorship) {
        switch (censorship) {
            case HEAVY:
                return getContentHeavilyCensored();
            case LIGHT:
                return getContentLightlyCensored();
            case NONE:
                return getUncensoredMessage();
        }
        return null;
    }
    private String censorStringHeavily(String input) {
        return "*".repeat(input.length());
    }

    private Component getUncensoredMessage() {
        return contentUncensored;
    }
    private Component getContentLightlyCensored() {
        return contentLightlyCensored;
    }
    public Component getContentHeavilyCensored() {
        return contentHeavilyCensored;
    }

    public CommandSender getAuthor() {
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

class PixmojiFormatting {
    public String pattern;
    public TextDecoration textDecoration;

    PixmojiFormatting(String pattern, TextDecoration textDecoration) {
        this.pattern = pattern;
        this.textDecoration = textDecoration;
    }
}
