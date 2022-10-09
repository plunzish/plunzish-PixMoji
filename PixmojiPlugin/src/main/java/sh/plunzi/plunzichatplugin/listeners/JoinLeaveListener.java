package sh.plunzi.plunzichatplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.utils.DatabaseManager;
import sh.plunzi.plunzichatplugin.utils.FileManager;

import java.util.UUID;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {

        DatabaseManager databaseManager = PlunziChatPlugin.DATABASE_MANAGER;
        FileManager fileManager = PlunziChatPlugin.FILE_MANAGER;
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!databaseManager.doesPlayerExist(uuid)) {
            databaseManager.createPlayerEntry(uuid, fileManager.getDefaultCensorLevel(), false, 0, "0");
        }

        databaseManager.setAdmin(uuid, player.isOp());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlunziChatPlugin.CHAT_HANDLER.lastResponseHashMap.remove(event.getPlayer().getUniqueId());
    }

}
