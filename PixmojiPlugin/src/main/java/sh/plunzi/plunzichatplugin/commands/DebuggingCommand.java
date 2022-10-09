package sh.plunzi.plunzichatplugin.commands;

import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.utils.OtherUtils;

import java.util.Arrays;

public class DebuggingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;
        if(args.length < 3) {
            chatHandler.sendCommandFeedback("no. /debug <color1> <color2> <message>. Colors as these: #ffee99", false, sender);
            return false;
        }


        Color color1;
        try {
            color1 = Color.fromRGB((int) Long.parseLong(args[0].replace("#", ""), 16));
        } catch (NumberFormatException e) {
            chatHandler.sendCommandFeedback(command.getName() + " " + args[0] + " <-  Error here", true, sender);
            return false;
        }

        Color color2;
        try {
            color2 = Color.fromRGB((int) Long.parseLong(args[1].replace("#", ""), 16));
        } catch (NumberFormatException e) {
            chatHandler.sendCommandFeedback(command.getName() + " " + args[0] + " <-  Error here", true, sender);
            return false;
        }


        StringBuilder message = new StringBuilder();
        for (String arg : Arrays.copyOfRange(args, 2, args.length)) {
            message.append(arg).append(" ");
        }
        System.out.println(message);

        chatHandler.sendCommandFeedback(OtherUtils.buildComponent(message.toString(), color1, color2), true, sender);
        return true;
    }
}
