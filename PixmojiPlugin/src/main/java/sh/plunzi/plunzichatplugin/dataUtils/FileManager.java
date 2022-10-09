package sh.plunzi.plunzichatplugin.dataUtils;

import org.bukkit.Bukkit;
import sh.plunzi.plunzichatplugin.chatSending.Debug;
import sh.plunzi.plunzichatplugin.chatSending.messages.Censorship;

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
    private static final String CENSOR_VALUE_STRING = "default-censor";

    String defaultCensoredWords = CENSORED_WORDS_STRING + "=[\"Monsbot ist doof\",\"Monsbot is stupid\",\"Poopserver\"]";
    String defaultHardCensoredWords = HARD_CENSORED_WORDS_STRING + "=[]";
    String defaultNameFormatting = NAME_FORMATTING_STRING + "=<%s> ";
    String defaultBroadcastFormatting = BROADCAST_FORMATTING_STRING + "\u00a76\u00a7l";
    String defaultDirectMessageFormattingIn = DIRECT_MESSAGE_FORMATTING_IN_STRING + "\u00a77%s>>";
    String defaultDirectMessageFormattingOut = DIRECT_MESSAGE_FORMATTING_OUT_STRING + "\u00a77%s<<";
    String defaultDebugBoolean = DEBUG_BOOLEAN_STRING + "=false ";
    String defaultCensorValue = CENSOR_VALUE_STRING + "=LIGHT\n#available options: HEAVY, LIGHT, NONE (not case sensitive)";


    public FileManager() {
        try {
            if(file.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);

                fileOutputStream.write("#Config file for the plugin, can be changed without reloading server".getBytes());
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(defaultNameFormatting.getBytes());
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(defaultCensoredWords.getBytes());
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(defaultHardCensoredWords.getBytes());
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(defaultDebugBoolean.getBytes());
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(defaultCensorValue.getBytes());
                fileOutputStream.close();
            }

        } catch (IOException e) {
            Debug.throwException(e);
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
    public Censorship getCensorLevel() {
        String censorLevel = this.defaultCensoredWords.replace(CENSOR_VALUE_STRING + "=", "");
        Censorship censorship = Censorship.HEAVY;
        try {
            censorship = Censorship.valueOf(getField(CENSOR_VALUE_STRING, censorLevel).toUpperCase().replaceAll("\\s+", ""));
        } catch (IllegalArgumentException e) {
            Debug.throwException(e);
        }
        return censorship;
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
                        line = line.split("#")[0];
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

        } catch (IOException e) {
            Debug.throwException(e);
        }

        return value;
    }

}
