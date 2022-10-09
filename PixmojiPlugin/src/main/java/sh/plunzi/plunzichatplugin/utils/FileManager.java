package sh.plunzi.plunzichatplugin.utils;

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

    List<String> strings = new ArrayList<>();

    private static final Field NAME_FORMATTING = new Field("name-formatting", "<%s> ");
    private static final Field DIRECT_MESSAGE_FORMATTING_IN = new Field("direct-message-formatting-in", "§7 §e[§b§l>§e] §l%s §r§e» §7");
    private static final Field DIRECT_MESSAGE_FORMATTING_OUT = new Field("direct-message-formatting-out", "§7 §e[§d§l<§e] §l%s §r§e« §7");
    private static final Field BROADCAST_FORMATTING = new Field("broadcast-formatting", "\u00a76\u00a7l");
    private static final Field CENSORED_WORDS = new Field("censored-words","[\"Monsbot ist doof\",\"Monsbot is stupid\",\"Poopserver\"]");
    private static final Field HARD_CENSORED_WORDS = new Field("hard-censored-words","[]");
    private static final Field DEBUG_BOOLEAN = new Field("debug-boolean","false ");
    private static final Field CENSOR_VALUE = new Field("default-censor","LIGHT\n#available options: HEAVY, LIGHT, NONE (not case sensitive)");
    private static final Field DATABASE_USERNAME = new Field("database-username","root");
    private static final Field DATABASE_PASSWORD = new Field("database-password","1234");
    private static final Field[] FIELDS = {
            NAME_FORMATTING,
            DIRECT_MESSAGE_FORMATTING_IN,
            DIRECT_MESSAGE_FORMATTING_OUT,
            BROADCAST_FORMATTING,
            CENSORED_WORDS,
            HARD_CENSORED_WORDS,
            DEBUG_BOOLEAN,
            CENSOR_VALUE,
            DATABASE_USERNAME,
            DATABASE_PASSWORD
    };

    public FileManager() {
        try {
            if(file.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
                dataOutputStream.writeUTF("#Config file for the plugin, can be changed without reloading server\n");

                for (Field field: FIELDS) {
                    dataOutputStream.writeUTF(field.whole);
                    dataOutputStream.writeUTF("\n");
                }
                dataOutputStream.close();
                fileOutputStream.close();
            }

        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("\u00a7c" + e);
        }
    }

    public String getPublicMessageFormatting() {
        return getField(NAME_FORMATTING);
    }
    public String getPrivateMessageFormattingIn() {
        return getField(DIRECT_MESSAGE_FORMATTING_IN);
    }
    public String getPrivateMessageFormattingOut() {
        return getField(DIRECT_MESSAGE_FORMATTING_OUT);
    }
    public String getBroadcastFormatting() {
        return getField(BROADCAST_FORMATTING);
    }
    public String getDatabasePassword() {
        return getField(DATABASE_PASSWORD);
    }
    public String getDatabaseUsername() {
        return getField(DATABASE_USERNAME);
    }
    public Censorship getDefaultCensorLevel() {
        Censorship censorship = Censorship.HEAVY;
        try {
            censorship = Censorship.valueOf(getField(CENSOR_VALUE).toUpperCase().replaceAll("\\s+", ""));
        } catch (IllegalArgumentException e) {
            Debug.throwException(e);
        }
        return censorship;
    }

    public boolean getDebugBoolean() {
        return getField(DEBUG_BOOLEAN).replace(" ", "").equals("true");
    }

    public List<String> getCensoredWords() {
        return getCensoredWords(false);
    }

    public List<String> getCensoredWords(boolean hard) {
        String censoredWords;
        if(hard) {
            censoredWords = getField(HARD_CENSORED_WORDS);
        } else {
            censoredWords = getField(CENSORED_WORDS);
        }

        Pattern pattern = Pattern.compile("\"(.+?)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(censoredWords);

        List<String> output = new ArrayList<>();
        while(matcher.find()) {
            output.add(matcher.group(1));
        }
        return output;
    }

    private String getField(Field field) {
        return getField(field.name, field.value, this.file);
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

class Field {
    String name;
    String value;
    String whole;
    public Field(String name, String value) {
        this.name = name;
        this.value = value;
        whole = name + "=" + value;
    }
}
