package sh.plunzi.plunzichatplugin.commands.tabCompletion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendsTabCompletion implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if(args.length ==  1) {
            ArrayList<String> availableArgs1 = new ArrayList<>();

            availableArgs1.add("add");
            availableArgs1.add("remove");

            return availableArgs1;
        }
        if(args.length ==  2) {
            ArrayList<String> availableArgs2 = new ArrayList<>();

            for(Player player : Bukkit.getOnlinePlayers()) {

                if(args[1].endsWith(",")) {

                    if(!player.getName().equals(sender.getName())) {
                        availableArgs2.add(args[1] + player.getName());
                    }

                } else if(!player.getName().equals(sender.getName())) {
                    availableArgs2.add(player.getName());
                }
            }
            if(sender.isOp()) {
                availableArgs2.add("*");
            }
            return availableArgs2;
        }

        return Collections.emptyList();
    }
}
