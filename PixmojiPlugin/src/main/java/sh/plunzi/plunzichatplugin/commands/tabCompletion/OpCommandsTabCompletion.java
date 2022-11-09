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

public class OpCommandsTabCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length ==  1 && sender.isOp()) {
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
            availableArgs1.add(args[0].endsWith(",") ? args[0] + "r" : "r");
            if(sender.isOp()) {
                availableArgs1.add("*");
            }
            return availableArgs1;
        }

        return Collections.emptyList();
    }
}
