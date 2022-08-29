package sh.plunzi.plunzichatplugin.fileUtils;

import org.bukkit.Bukkit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {

    File file = new File(Bukkit.getServer().getWorldContainer(), "pixmojiConfig.properties");

    private static final String NAME_FORMATTING_STRING = "name-formatting";
    private static final String DIRECT_MESSAGE_FORMATTING_IN_STRING = "direct-message-formatting-in";
    private static final String DIRECT_MESSAGE_FORMATTING_OUT_STRING = "direct-message-formatting-out";
    private static final String BROADCAST_FORMATTING_STRING = "broadcast-formatting";
    private static final String CENSORED_WORDS_STRING = "censored-words";
    private static final String HARD_CENSORED_WORDS_STRING = "hard-censored-words";
    private static final String DEBUG_BOOLEAN_STRING = "debug-boolean";

    String defaultCensoredWords = CENSORED_WORDS_STRING + "=[\"Monsbot ist doof\",\"Monsbot is stupid\",\"Poopserver\"]";
    String defaultHardCensoredWords = HARD_CENSORED_WORDS_STRING + "=[]";
    String defaultNameFormatting = NAME_FORMATTING_STRING + "=<%s> ";
    String defaultBroadcastFormatting = BROADCAST_FORMATTING_STRING + "\u00a76\u00a7l";
    String defaultDirectMessageFormattingIn = DIRECT_MESSAGE_FORMATTING_IN_STRING + "\u00a77%s>>";
    String defaultDirectMessageFormattingOut = DIRECT_MESSAGE_FORMATTING_OUT_STRING + "\u00a77%s<<";
    String defaultDebugBoolean = DEBUG_BOOLEAN_STRING + "=false ";


    public FileManager() {
        try {
            if(file.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                fileOutputStream.write(defaultNameFormatting.getBytes());
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(defaultCensoredWords.getBytes());
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(defaultDebugBoolean.getBytes());
                fileOutputStream.close();
            }

        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("\u00a7c" + e);
        }
    }

    public String getPublicMessageFormatting() {
        String messageFormatting = this.defaultNameFormatting.replace(NAME_FORMATTING_STRING + "=", "");
        messageFormatting = getField(NAME_FORMATTING_STRING, messageFormatting);
        return messageFormatting;
    }
    public String getPrivateMessageFormattingIn() {
        String messageFormatting = this.defaultDirectMessageFormattingIn.replace(DIRECT_MESSAGE_FORMATTING_IN_STRING + "=", "");
        messageFormatting = getField(DIRECT_MESSAGE_FORMATTING_IN_STRING, messageFormatting);
        return messageFormatting;
    }
    public String getPrivateMessageFormattingOut() {
        String messageFormatting = this.defaultDirectMessageFormattingOut.replace(DIRECT_MESSAGE_FORMATTING_OUT_STRING + "=", "");
        messageFormatting = getField(DIRECT_MESSAGE_FORMATTING_OUT_STRING, messageFormatting);
        return messageFormatting;
    }
    public String getBroadcastFormatting() {
        String messageFormatting = this.defaultBroadcastFormatting.replace(BROADCAST_FORMATTING_STRING + "=", "");
        messageFormatting = getField(BROADCAST_FORMATTING_STRING, messageFormatting);
        return messageFormatting;
    }

    public boolean getDebugBoolean() {
        boolean debugBoolean = true;
        debugBoolean = getField(DEBUG_BOOLEAN_STRING, debugBoolean+"").replace(" ", "").equals("true");
        return debugBoolean;
    }

    public List<String> getCensoredWords() {
        return getCensoredWords(false);
    }

    public List<String> getCensoredWords(boolean hard) {
        String censoredWords;
        if(hard) {
            censoredWords = getField(HARD_CENSORED_WORDS_STRING, defaultHardCensoredWords);
        } else {
            censoredWords = getField(CENSORED_WORDS_STRING, defaultCensoredWords);
        }

        Pattern pattern = Pattern.compile("\"(.+?)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(censoredWords);

        List<String> output = new ArrayList<>();
        while(matcher.find()) {
            output.add(matcher.group(1));
        }
        return output;
    }

    private String getField(String fieldName, String fallbackValue) {
        return getField(fieldName, fallbackValue, this.file);
    }

    public String getField(String fieldName, String fallbackValue, File file) {

        String value = fallbackValue;
        String line;

        try {
            FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);

            boolean found = false;

            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {

                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith(fieldName)) {
                        value = line.replace(fieldName + "=", "");
                        found = true;
                    }
                }
            }

            if (!found && fallbackValue != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write((fieldName + "=" + fallbackValue).getBytes());
                fileOutputStream.close();
            }

        } catch (FileNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("\u00a7c" + e);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return value;
    }

}
