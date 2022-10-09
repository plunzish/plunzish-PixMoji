package sh.plunzi.plunzichatplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.utils.DatabaseManager;
import sh.plunzi.plunzichatplugin.utils.OtherUtils;

import java.util.*;

public class FriendsCommand implements CommandExecutor {
    ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;
    OtherUtils utils = PlunziChatPlugin.UTILS;
    DatabaseManager databaseManager = PlunziChatPlugin.DATABASE_MANAGER;
    HashMap<Player,List<Player>> playerPlayerHashMap = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

//        if(args.length > 0) if (args[0].isBlank()) args = new String[]{};

        if(!(sender instanceof Player)) {
            chatHandler.sendCommandFeedback("You sadly do not have any friends :)\n(you must be a player)", true, sender);
            return false;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if(args.length ==  0) {
            List<OfflinePlayer> friends = PlunziChatPlugin.DATABASE_MANAGER.getFriends(playerUUID);
            if(friends.isEmpty()) {
                chatHandler.sendCommandFeedback(Component.text("Your friend list is empty"), true, sender);
                return true;
            }

            StringBuilder message = new StringBuilder("Your friends: ");
            for(OfflinePlayer friend : friends) {
                message.append("\n");

                message.append(
                        !(friend ==  friends.get(friends.size()-1))
                                ? "├─ "
                                : "└─ "
                );
                message.append(friend.getName());
            }
            chatHandler.sendCommandFeedback(message.toString(), false, sender);


        } else {

            if(args.length < 2) {
                chatHandler.sendCommandFeedback("You must provide one or more players", true, sender);
                return false;
            }

            for (Player target : PlunziChatPlugin.UTILS.stringToPlayers(args[1], sender, true)) {
                performFriendsCommand(args, player, target);
            }

        }
        return true;
    }

    private void performFriendsCommand(String[] args, Player player, Player target) {
        UUID playerUUID = player.getUniqueId();

        if(target == player) {
            chatHandler.sendCommandFeedback(target.getName() + " > You can't (un)friend yourself", true, player);
            if(databaseManager.getFriends(playerUUID).isEmpty()) chatHandler.sendCommandFeedback("(Also I'm sorry that you are lonely)", false, player);
            return;
        }

        switch (args[0]) {
            case "add":

                if (databaseManager.getFriends(playerUUID).contains(target)) {
                    chatHandler.sendCommandFeedback(target.getName() + " > You're already friends with this player!", true, player);
                    return;
                }
                sendFriendRequest(player, target);

                break;
            case "remove":

                if (!databaseManager.getFriends(playerUUID).contains(target)) {

                    if(entryExists(target, player) || entryExists(player, target)) {
                        removeBothEntries(player, target);
                        chatHandler.sendCommandFeedback("Rejected " + target.getName() + "s friend request", false, player);
                        chatHandler.sendCommandFeedback(player.getName() + " rejected your friend request", false, target);
                        return;
                    }
                    chatHandler.sendCommandFeedback("You're not friends with this player!", true, player);
                    return;
                }
                removeBothEntries(player, target);
                databaseManager.removeFriend(player.getUniqueId(), target.getUniqueId());
                databaseManager.removeFriend(target.getUniqueId(), player.getUniqueId());
                chatHandler.sendCommandFeedback("You're no longer friends with " + target.getName() + ".", false, player);

                break;
            default:
                chatHandler.sendCommandFeedback("Invalid Syntax, use /friends ([add/remove] <player>)", true, player);
        }
    }

    private void sendFriendRequest(Player player, Player target) {

        if(playerPlayerHashMap.containsKey(target)) {
            if(playerPlayerHashMap.get(target).contains(player)) {
                chatHandler.sendCommandFeedback(Component.text(player.getName() + " accepted your friend request!"), false, target);
                chatHandler.sendCommandFeedback(Component.text(target.getName() + " accepted your friend request!"), false, player);
                databaseManager.addFriend(target.getUniqueId(), player.getUniqueId());
                databaseManager.addFriend(player.getUniqueId(), target.getUniqueId());

                removeEntry(player, target);
                return;
            }
        }
        if(playerPlayerHashMap.containsKey(player)) {
            if(playerPlayerHashMap.get(player).contains(target)) {
                chatHandler.sendCommandFeedback(Component.text("You already sent a friend request to " + target.getName()), true, player);
            }
            return;
        }
        chatHandler.sendCommandFeedback("Friend request sent to " + target.getName() + "! (they have 2 minutes to accept)", false, player);
        chatHandler.sendCommandFeedback(Component.text(player.getName() + " sent you a friend Request (you have 2 minutes to accept)")
                .append(Component.text("\n"))
                .append(
                        utils.buildComponent("[Accept]", Color.fromRGB(0x00ff0c), Color.fromRGB(0x008831))
                        .clickEvent(ClickEvent.runCommand("/friends add " + player.getName()))
                ).append(Component.text(" "))
                .append(
                        utils.buildComponent("[Reject]", Color.fromRGB(0xff5252), Color.fromRGB(0xad0000)))
                        .clickEvent(ClickEvent.runCommand("/friends remove " + player.getName())
                )
                , false, target);
        addEntry(player, target);
        startTimer(player, target);
    }

    private void startTimer(Player player, Player target) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(PlunziChatPlugin.INSTANCE, new Runnable() {
            @Override
            public void run() {
                removeEntry(player, target);
            }
        }, 20*60*2);
    }

    private void removeEntry(Player player, Player target) {
        if(playerPlayerHashMap.containsKey(player)) {
            if (playerPlayerHashMap.get(player).size() < 2) {
                playerPlayerHashMap.remove(player);
                return;
            }
            playerPlayerHashMap.get(player).remove(target);
        }
    }
    private void removeBothEntries(Player player, Player target) {
        removeEntry(player, target);
        removeEntry(target, player);
    }
    private void addEntry(Player player, Player target) {
        if(playerPlayerHashMap.containsKey(player)) {
            playerPlayerHashMap.get(player).add(target);
        } else {
            playerPlayerHashMap.put(player, Collections.singletonList(target));
        }
    }
    private boolean entryExists(Player player, Player target) {
        if(playerPlayerHashMap.containsKey(player)) {
            return playerPlayerHashMap.get(player).contains(target);
        }
        return false;
    }
}
