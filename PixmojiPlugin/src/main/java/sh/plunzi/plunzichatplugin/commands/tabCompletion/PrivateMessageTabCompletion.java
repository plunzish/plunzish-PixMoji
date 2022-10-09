package sh.plunzi.plunzichatplugin.commands.tabCompletion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.pixmojiData.Pixmoji;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrivateMessageTabCompletion implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if(args.length ==  1) {
            ArrayList<String> availableArgs1 = new ArrayList<>();

            for(Player player : Bukkit.getOnlinePlayers()) {

                if(args[0].endsWith(",")) {

                    if(!player.getName().equals(sender.getName())) {
                        availableArgs1.add(args[0] + player.getName());
                    }

                } else if(!player.getName().equals(sender.getName())) {
                    availableArgs1.add(player.getName());
                }
            }
            if(sender.isOp()) {
                availableArgs1.add("*");
            }
            return availableArgs1;
        }
        if(args[args.length-1].startsWith(":")) {
            ArrayList<String> availableArgs2 = new ArrayList<>();

            for(Pixmoji pixmoji : PlunziChatPlugin.PIXMOJIS.pixmojiList) {
                availableArgs2.add(":" + pixmoji.getName() + ":");
            }
            return availableArgs2;
        }
        if(args[args.length-1].startsWith("@")) {
            ArrayList<String> availableArgs2 = new ArrayList<>();

            for(Player player : Bukkit.getOnlinePlayers()) {
                availableArgs2.add("@" + player.getName());
            }
            return availableArgs2;
        }

        return Collections.emptyList();
    }
}
