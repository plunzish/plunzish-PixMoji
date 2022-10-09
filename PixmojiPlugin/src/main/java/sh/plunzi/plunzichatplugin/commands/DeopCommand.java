package sh.plunzi.plunzichatplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.chatSending.messages.Message;

public class DeopCommand implements CommandExecutor {

    ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.isOp()) {
            chatHandler.sendCommandFeedback("You can't do that.", true, sender);
            return false;
        }

        if(args.length !=  1) {
            chatHandler.sendCommandFeedback("Wrong syntax, use like this: " + ((sender instanceof Player)?"/":"") + command.getName() + " <player>", true, sender);
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target !=  null) {
            target.setOp(false);
            target.sendMessage(
                    PlunziChatPlugin.PREFIX.append(
                    Component.text("you're no longer a server operator")
                    .color(TextColor.color(HSVLike.fromRGB(Color.GRAY.getRed(), Color.GRAY.getBlue(), Color.GRAY.getGreen())))
                    .decoration(TextDecoration.ITALIC, true)));

            PlunziChatPlugin.DATABASE_MANAGER.setAdmin(target.getUniqueId(), false);
            chatHandler.sendCommandFeedback(target.getName() + " is no longer a Server Operator", false, sender);
        } else {
            chatHandler.sendCommandFeedback("Player not found", true, sender);
            return false;
        }

        return true;
    }
}
