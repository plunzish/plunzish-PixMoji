package sh.plunzi.plunzichatplugin.chatSending.messages;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.RegExp;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.Debug;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmoji;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmojis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    private final CommandSender player1;
    private final CommandSender player2;

    private final MessageType messageType;

    private final Component contentUncensored;
    private final Component contentLightlyCensored;
    private final Component contentHeavilyCensored;


    public Message(String rawMessage, CommandSender author, MessageType messageType, CommandSender receiver) {

        CommandSender sender = author;
        if(messageType == MessageType.PRIVATE_OUT) {
            sender = receiver;
        }

        this.contentUncensored = stringToComponent(rawMessage, sender.getName(), Censorship.NONE, messageType);
        this.contentLightlyCensored = stringToComponent(rawMessage, sender.getName(), Censorship.LIGHT, messageType);
        this.contentHeavilyCensored = stringToComponent(rawMessage, sender.getName(), Censorship.HEAVY, messageType);

        this.messageType = messageType;

        this.player1 = author;
        this.player2 = receiver;
    }

    TextColor textColor = PlunziChatPlugin.FILE_MANAGER.getTextColor();
    TextColor mentionColor = PlunziChatPlugin.FILE_MANAGER.getMentionColor();

    public TextComponent stringToComponent(String string, String authorName, Censorship censorLevel, MessageType messageType) {
        String message = censorString(string, censorLevel);

        TextComponent messageComponent = Component.text(getPlayerNameFormatted(authorName, messageType));

        messageComponent = messageComponent.append(
                Component.text(message)
        .style(Style.style().color(textColor).build()));

        messageComponent = handlePixmojis(messageComponent);
        messageComponent = handleFormatting(messageComponent);
        messageComponent = handleAts(messageComponent);

        message = PlainTextComponentSerializer.plainText().serialize(messageComponent);
        message = message.replace(getPlayerNameFormatted(authorName, messageType), "");

        Pixmoji pixmoji = Pixmojis.nullmoji;
        try {
            pixmoji = PlunziChatPlugin.PIXMOJIS.getPixmoji(message.charAt(0));
        } catch (IndexOutOfBoundsException ignored) {}

        if(pixmoji.exists(PlunziChatPlugin.PIXMOJIS) && message.length() == 1) {

            messageComponent = Component.text(getPlayerNameFormatted(authorName, messageType))
                    .style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_TRANSPARENT).build()); //make space                         //[formattedInvis][Emoji]
                                                                                                                                        //
                                                                                                                                        //
            messageComponent = messageComponent.append(
                    Component.text(" ").style(Style.style().font(Key.key(Key.MINECRAFT_NAMESPACE, "default")).build())
                    .append(
                    Component.text(pixmoji.getUnicodeChar() + "\n")
                    .style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_LARGE).build())
                            .hoverEvent(HoverEvent.showText(Component.text(":" + pixmoji.getName() + ":")))));
                                                                                              //add Emoji                               //[formattedInvis][Emoji]
                                                                                                                                        //                IIIIIII <- The Emoji is overlapping into the other rows
                                                                                                                                        //                IIIIIII
            messageComponent = messageComponent.append(Component.text(getPlayerNameFormatted(authorName, messageType) + "\n" )
                    .style(Style.style().font(Key.key("minecraft", "default")).build()))
            .hoverEvent(HoverEvent.showText(Component.text("(All Messages can be seen by console)")));//add Name                  //[formattedInvis][Emoji]
                                                                                                                                        //[formattedVisib]IIIIIII
                                                                                                                                        //                IIIIIII
            messageComponent = messageComponent.append(Component.text(getPlayerNameFormatted(authorName, messageType))
                    .style(Style.style().font(PlunziChatPlugin.PIXMOJI_FONT_TRANSPARENT).build())); //add Name                          //[formattedInvis][Emoji]
                                                                                                                                        //[formattedVisib]IIIIIII   Done! :D
                                                                                                                                        //[formattedInvis]IIIIIII
        }

        if(messageType == MessageType.PARTY) {
            messageComponent = (TextComponent) PlunziChatPlugin.UTILS.buildComponent("[Party]", Color.fromRGB(0x0cd9ff), Color.fromRGB(0x2888ff))
                    .hoverEvent(Component.text(player2.getName()))

                    .append(Component.text(" ")).append(messageComponent);
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

    private final Player randomPlayer = PlunziChatPlugin.UTILS.getRandomPlayer();
    private TextComponent handleAts(TextComponent inputTextComponent) {

        String input = PlainTextComponentSerializer.plainText().serialize(inputTextComponent);

        String patternAsString = "@([^\\s\\W]+-?)";
        Pattern pattern = Pattern.compile(patternAsString);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {

            Player player = Bukkit.getServer().getPlayerExact(matcher.group(1));

            if(player != null || matcher.group(1).equals("r-")) {

                Debug.send(matcher.group(1) + " 2");

                if(player== null && matcher.group(0).equals("@r-")) player = randomPlayer;

                assert player != null;
                @RegExp String replacement = "@" + player.getName();

                @RegExp String magic = matcher.group(0);

                inputTextComponent = (TextComponent) inputTextComponent.replaceText(TextReplacementConfig.builder()
                        .match(magic).replacement(
                                Component.text(replacement).style(
                                        Style.style()
                                                .color(mentionColor)
                                                .build()
                                )
                        ).build());
            }
        }
        return inputTextComponent;
    }

    private TextComponent handleFormatting(TextComponent inputTextComponent) {

        String patternBoldItalic =       "\\*{3}([^*\\n]+?)\\*{3}";
        String patternUnderlinedItalic = "_{3}([^*\\n]+?)_{3}";
        String patternBold =             "\\*\\*([^*\\n]+?)\\*\\*";
        String patternUnderlined =       "__([^*\\n]+?)__";
        String patternItalic =           "_([^*\\n]+?)_|\\*([^*\\n]+?)\\*";

        String[] patterns = new String[]{
                patternBoldItalic,
                patternUnderlinedItalic,
                patternBold,
                patternUnderlined,
                patternItalic
        };

        TextComponent textComponent = inputTextComponent;

        for (int i = 0; i < 5; i++) {

            String text = PlainTextComponentSerializer.plainText().serialize(textComponent);

            Pattern pattern = Pattern.compile(patterns[i]);
            Matcher matcher = pattern.matcher(text);

            TextDecoration[] textDecorations = null;

            switch (i) {
                case 0:
                    textDecorations = new TextDecoration[]
                            {TextDecoration.BOLD, TextDecoration.ITALIC};
                    break;
                case 1:
                    textDecorations = new TextDecoration[]
                            {TextDecoration.UNDERLINED, TextDecoration.ITALIC};
                    break;
                case 2:
                    textDecorations = new TextDecoration[]
                            {TextDecoration.BOLD};
                    break;
                case 3:
                    textDecorations = new TextDecoration[]
                            {TextDecoration.UNDERLINED};
                    break;
                case 4:
                    textDecorations = new TextDecoration[]
                            {TextDecoration.ITALIC};
                    break;
            }

            Style style = Style.style()
                    .decoration(textDecorations[0], true)
                    .color(textColor)
                    .build();

            if(i < 2) {
                style = Style.style()
                        .decoration(textDecorations[0], true)
                        .decoration(textDecorations[1], true)
                        .color(textColor)
                        .build();
            }

            while (matcher.find()) {

                @RegExp String match = matcher.group(0);

                Debug.send("\n" + matcher.group(0) + "|||" + (matcher.group(1) == null ? matcher.group(2) : matcher.group(1)) + "\n" + pattern + "\n");

                textComponent = (TextComponent) textComponent.replaceText(TextReplacementConfig.builder()
                        .matchLiteral(match).replacement(
                                Component.text((matcher.group(1) == null ? matcher.group(2) : matcher.group(1))).style(style)

                        ).build());
            }
        }

        return textComponent;
    }


    private TextComponent handlePixmojis(TextComponent inputTextComponent) {

        StringBuilder patternBuilder = new StringBuilder(":(");

        for(Pixmoji pixmoji : PlunziChatPlugin.PIXMOJIS.pixmojiList) {
            patternBuilder.append(pixmoji.getName()).append("|");

            if(pixmoji.getEmoji() != null) {
                inputTextComponent = (TextComponent) inputTextComponent.replaceText(TextReplacementConfig.builder()
                        .matchLiteral(pixmoji.getEmoji())
                        .replacement(Component.text(":" + pixmoji.getName() + ":")).build());
            }
        }
        patternBuilder = new StringBuilder(patternBuilder.substring(0, patternBuilder.length() - 1));
        patternBuilder.append("):");

        String input = PlainTextComponentSerializer.plainText().serialize(inputTextComponent);

        Pattern emojiPattern = Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = emojiPattern.matcher(input);

        while (matcher.find()) {
            @RegExp String pixmojiString = matcher.group(0);
            Pixmoji pixmoji = PlunziChatPlugin.PIXMOJIS.getPixmoji(pixmojiString.replace(":", ""));
            inputTextComponent = (TextComponent) inputTextComponent.replaceText(TextReplacementConfig.builder()
                    .match(pixmojiString).replacement(
                            Component.text(pixmoji.getUnicodeChar()).style(
                                    Style.style()
                                            .font(PlunziChatPlugin.PIXMOJI_FONT)
                                            .build()
                            ).hoverEvent(HoverEvent.showText(Component.text(":" + pixmoji.getName() + ":")))
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

    public CommandSender getPlayer1() {
        return player1;
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

    public CommandSender getPlayer2() {
        return player2;
    }

    public MessageType getType() {
        return messageType;
    }
}