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

public class PartyTabCompletion implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if(args.length ==  1) {
            ArrayList<String> availableArgs1 = new ArrayList<>();

            availableArgs1.add("create");
            availableArgs1.add("invite");
            availableArgs1.add("join");
            availableArgs1.add("leave");
            availableArgs1.add("remove");
            availableArgs1.add("disband");

            return availableArgs1;
        }

        if(args.length ==  2) {

            if(   !(args[0].equals("invite") ||
                    args[0].equals("remove") ||
                    args[0].equals("join"))) {
                return Collections.emptyList();
            }

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
            availableArgs2.add(args[1].endsWith(",") ? args[1] + "r" : "r");
            if(sender.isOp()) {
                availableArgs2.add("*");
            }
            return availableArgs2;
        }

        return Collections.emptyList();
    }
}
