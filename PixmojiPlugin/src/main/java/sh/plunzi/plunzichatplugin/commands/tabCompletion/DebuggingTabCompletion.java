package sh.plunzi.plunzichatplugin.commands.tabCompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DebuggingTabCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if(args.length > 0 && args.length < 3 ) {
            List<String> tabArgs = new ArrayList<>();
            tabArgs.add("#");
            return tabArgs;
        }
        return new ArrayList<>();
    }
}
