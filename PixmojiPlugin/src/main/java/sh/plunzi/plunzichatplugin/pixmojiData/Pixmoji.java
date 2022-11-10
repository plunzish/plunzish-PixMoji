package sh.plunzi.plunzichatplugin.pixmojiData;

public class Pixmoji {
    private String name;
    private char unicodeChar;
    private final String emoji;

    Pixmoji(String name, char unicodeChar, String emoji) {
        this.name = name;
        this.unicodeChar = unicodeChar;
        this.emoji = emoji;
    }

    public String getName() {
        return name;
    }

    public char getUnicodeChar() {
        return unicodeChar;
    }

    public String getEmoji() {
        return emoji;
    }

    public boolean exists(Pixmojis pixmojis) {
        return pixmojis.isInList(unicodeChar);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnicodeChar(char unicodeChar) {
        this.unicodeChar = unicodeChar;
    }
}
