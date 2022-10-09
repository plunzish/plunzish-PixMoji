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

    public static void send(String message) {
        send(message, null);
    }

    public static void send(Exception exception, Player player) {
        String message = "\u00a7c" + Arrays.toString(exception.getStackTrace()).replace(",", "\n");
        send(message, player);
    }
    public static void send(Exception exception) {
        send(exception, null);
    }

    public static void throwException(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        Bukkit.getConsoleSender().sendMessage("§c" + errors);
    }

}
