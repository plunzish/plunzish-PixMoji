package sh.plunzi.plunzichatplugin;

import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.commands.DebuggingCommand;
import sh.plunzi.plunzichatplugin.commands.PrivateMessageCommand;
import sh.plunzi.plunzichatplugin.dataUtils.FileManager;
import sh.plunzi.plunzichatplugin.listeners.ChatListener;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmojis;

public final class PlunziChatPlugin extends JavaPlugin {

    public static Pixmojis PIXMOJIS;
    public static ChatHandler CHAT_HANDLER;
    public static FileManager FILE_MANAGER;
    public static String PLUNZISH_NAMESPACE = "plunzish";

    public static final Key PIXMOJI_FONT = Key.key(PLUNZISH_NAMESPACE, "pixmojis");
    public static final Key PIXMOJI_FONT_LARGE = Key.key(PLUNZISH_NAMESPACE, "pixmojis_large");
    public static final Key PIXMOJI_FONT_TRANSPARENT = Key.key(PLUNZISH_NAMESPACE, "transparent");
    public static final Key CHAT_PING_SOUND = Key.key(PlunziChatPlugin.PLUNZISH_NAMESPACE, "chat.ping");

    @Override
    public void onEnable() {
        // Plugin startup logic
        register();
        Bukkit.getConsoleSender().sendMessage("\u00a76Pixmoji Plugin loaded \u00a7a:" + PIXMOJIS.getRandom().getName() + ":");
    }

    //THE LIST OF WHAT I NEED TO DO
    //✅ Pings
    //- Pingsound
    //
    //✅ Nachrichten ^_^
    //✅ dms
    //
    //TODO * + ,
    //
    //TODO Broadcast
    //
    //TODO Friend system
    // - Friend broadcast
    // - responde /r
    // - accept all
    // - decline all
    // - lock
    //
    //TODO Partysystem
    // - Partychat
    //TODO Formatting


    private void register() {
        FILE_MANAGER = new FileManager();
        PIXMOJIS = new Pixmojis();
        CHAT_HANDLER = new ChatHandler();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ChatListener(), this);

        Bukkit.getPluginCommand("msg").setExecutor(new PrivateMessageCommand());
        Bukkit.getPluginCommand("debug").setExecutor(new DebuggingCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("\u00a76Pixmoji Plugin unloaded \u00a7c:" + PIXMOJIS.getRandom().getName() + ":");
    }
}
