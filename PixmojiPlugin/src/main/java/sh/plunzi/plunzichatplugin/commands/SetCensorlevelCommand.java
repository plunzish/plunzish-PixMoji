package sh.plunzi.plunzichatplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.messages.Censorship;
import sh.plunzi.plunzichatplugin.utils.DatabaseManager;

public class SetCensorlevelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("§cYou are not a player, so nothing gets censored for you anyway");
            return false;
        }
        Player player = (Player) sender;
        if(args.length < 1) {
            sender.sendMessage("§cNot enough arguments" +
                    "\n§c/" + command.getName() + " [heavy, light, none]");
            return false;
        }

        Censorship censorship;

        try {
            censorship = Censorship.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage("§c'" + args[0] + " is not a valid argument. Use \"heavy\", \"light\" or \"none\".");
            return false;
        }

        DatabaseManager databaseManager = PlunziChatPlugin.DATABASE_MANAGER;
        if(databaseManager.setCensorLevel(((Player) sender).getUniqueId(), censorship)) {
            player.sendMessage("§aUpdated Censorship to: §l" + censorship + "§r§a!");
        } else {
            player.sendMessage("§cSomething didn't work. Try again and if it still doesn't work file a bug report or something ._.");
            return false;
        }
        return true;
    }
}
