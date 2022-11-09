package sh.plunzi.plunzichatplugin.pixmojiData;

public class Pixmoji {
    private String name;
    private char unicodeChar;

    Pixmoji(String name, char unicodeChar) {
        this.name = name;
        this.unicodeChar = unicodeChar;
    }

    public String getName() {
        return name;
    }

    public char getUnicodeChar() {
        return unicodeChar;
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
