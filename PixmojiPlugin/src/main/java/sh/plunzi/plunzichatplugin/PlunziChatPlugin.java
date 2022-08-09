package sh.plunzi.plunzichatplugin;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import sh.plunzi.plunzichatplugin.listeners.ChatListener;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmojis;

public final class PlunziChatPlugin extends JavaPlugin {

    public static Pixmojis pixmojis = new Pixmojis();
    public static String PLUNZISH_NAMESPACE = "plunzish";

    public static Key PIXMOJI_FONT = Key.key(PLUNZISH_NAMESPACE, "pixmojis");
    public static Key PIXMOJI_FONT_LARGE = Key.key(PLUNZISH_NAMESPACE, "pixmojis_large");
    public static Key PIXMOJI_FONT_TRANSPARENT = Key.key(PLUNZISH_NAMESPACE, "transparent");

    @Override
    public void onEnable() {
        // Plugin startup logic
        register();
        Bukkit.getConsoleSender().sendMessage("Pixmoji Plugin loaded :sunglasses:");
    }

    private void register() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ChatListener(), this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
