package sh.plunzi.plunzichatplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.dataUtils.DatabaseManager;
import sh.plunzi.plunzichatplugin.dataUtils.FileManager;

import java.util.UUID;

public class JoinListener implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {

        DatabaseManager databaseManager = PlunziChatPlugin.DATABASE_MANAGER;
        FileManager fileManager = PlunziChatPlugin.FILE_MANAGER;
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!databaseManager.doesPlayerExist(uuid)) {
            databaseManager.createPlayerEntry(uuid, fileManager.getDefaultCensorLevel(), false, 0);
        }

        databaseManager.setAdmin(uuid, player.isOp());
    }

}
