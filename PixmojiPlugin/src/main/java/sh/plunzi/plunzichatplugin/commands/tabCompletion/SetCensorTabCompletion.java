package sh.plunzi.plunzichatplugin.commands.tabCompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetCensorTabCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length ==  1) {
            ArrayList<String> availableArgs1 = new ArrayList<>();

            availableArgs1.add("heavy");
            availableArgs1.add("light");
            availableArgs1.add("none");

            return availableArgs1;
        }
        return Collections.emptyList();
    }
}
