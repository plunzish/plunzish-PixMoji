package sh.plunzi.plunzichatplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;

public class DeopCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.isOp()) {
            sender.sendMessage("§cYou can't do that.");
            return false;
        }

        if(args.length != 1) {
            sender.sendMessage("§cUse like this: " + ((sender instanceof Player)?"/":"") + command.getName() + " <player>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target!=null) {
            target.setOp(false);
            target.sendMessage("§7§oyou're no longer a server operator");
            PlunziChatPlugin.DATABASE_MANAGER.setAdmin(target.getUniqueId(), false);
            sender.sendMessage("§a" + target.getName() + " is not a Server Operator anymore");
        } else {
            sender.sendMessage("§cPlayer not found");
            return false;
        }

        return true;
    }
}
