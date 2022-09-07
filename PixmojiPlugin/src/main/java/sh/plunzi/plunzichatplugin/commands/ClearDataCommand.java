package sh.plunzi.plunzichatplugin.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClearDataCommand implements CommandExecutor {

    List<UUID> players = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cYou aren't a player, you got no data");
            return false;
        }

        Player player = (Player) sender;
        if(!players.contains(player.getUniqueId())) {
            player.sendMessage("§l§cWARNING! All your progress, your playerscore, your settings, etc. will be deleted\nTo confirm execute the command again. You will be kicked and your data will be gone. To cancel the process type /clearmydata cancel");
            players.add(player.getUniqueId());
            return true;
        }

        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("cancel")) {
                players.remove(player.getUniqueId());
                player.sendMessage("§aDeletion has been cancelled");
                return true;
            }
            player.sendMessage("§cTo confirm your deletion, please don't use any arguments.");
            return false;
        }
        players.remove(player.getUniqueId());
        player.setOp(false);
        player.kick(Component.text("Your data has been deleted."));

        PlunziChatPlugin.DATABASE_MANAGER.deletePlayer(player.getUniqueId());

        boolean success = true;
        for (World world : Bukkit.getWorlds()) {
            List<File> files = new ArrayList<>();
            files.add(new File(world.getWorldFolder().getAbsolutePath() + "/playerdata/" + player.getUniqueId() + ".dat"));
            files.add(new File(world.getWorldFolder().getAbsolutePath() + "/playerdata/" + player.getUniqueId() + ".dat_old"));
            files.add(new File(world.getWorldFolder().getAbsolutePath() + "/playerdata/" + player.getUniqueId() + ".dat.offline-read"));

            for(File file : files) {
                if (file.exists()) {
                    if(!file.delete()) success = false;
                }
            }
        }

        return success;
    }
}
