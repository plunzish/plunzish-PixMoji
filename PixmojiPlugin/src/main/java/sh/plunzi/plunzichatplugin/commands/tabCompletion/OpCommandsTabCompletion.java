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
        if(args.length ==  1) {
            ArrayList<String> availableArgs1 = new ArrayList<>();

            for(Player player : Bukkit.getOnlinePlayers()) {
                if(!player.getName().equals(sender.getName())) {
                    availableArgs1.add(player.getName());
                }
            }
            return availableArgs1;
        }

        return Collections.emptyList();
    }
}
