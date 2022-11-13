package sh.plunzi.plunzichatplugin.chatSending;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class Debug {

    public static void send(String message, Player player) {
        if(!PlunziChatPlugin.FILE_MANAGER.getDebugBoolean()) {
            return;
        }
        Component component = Component.text(message);
        Bukkit.getConsoleSender().sendMessage(component);
        if(player != null) {
            player.sendMessage(component);
        }
    }

    public static void send(Object message) {

        if(message == null) message = "null";

        send(message.toString(), null);
    }

    public static void send(Exception e, Player player) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        player.sendMessage("§c" + errors);
    }

    public static void throwException(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        Bukkit.getConsoleSender().sendMessage("§c" + errors);
    }

}
