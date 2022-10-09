package sh.plunzi.plunzichatplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.chatSending.messages.Message;

import java.util.List;

public class DeopCommand implements CommandExecutor {

    ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.isOp()) {
            chatHandler.sendCommandFeedback("You can't do that.", true, sender);
            return false;
        }

        if(args.length != 1) {
            chatHandler.sendCommandFeedback("Wrong syntax, use like this: " + ((sender instanceof Player)?"/":"") + command.getName() + " <player|player1,player2>", true, sender);
            return false;
        }

        List<Player> targets = PlunziChatPlugin.UTILS.stringToPlayers(args[0], sender, true);
        for(Player target : targets) {
            setDeop(target, sender);
        }
        return true;
    }

    private void setDeop(Player target, CommandSender sender) {
        if(target!= null) {
            if(target.isOp()) {
                target.setOp(false);
                target.sendMessage("§7§oyou're no longer a server operator");
                PlunziChatPlugin.DATABASE_MANAGER.setAdmin(target.getUniqueId(), true);
                chatHandler.sendCommandFeedback("§a" + target.getName() + " is no longer a server operator", false, sender);
                return;
            }
            chatHandler.sendCommandFeedback(target.getName() + " is not a server operator", true, sender);
        }
    }
}
