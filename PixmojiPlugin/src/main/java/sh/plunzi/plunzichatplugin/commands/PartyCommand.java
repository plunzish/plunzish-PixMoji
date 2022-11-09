package sh.plunzi.plunzichatplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.party.PartySystem;
import sh.plunzi.plunzichatplugin.utils.OtherUtils;

import java.util.List;

public class PartyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;
        PartySystem partySystem = PlunziChatPlugin.PARTYSYSTEM;
        OtherUtils utils = PlunziChatPlugin.UTILS;

        if (!(sender instanceof Player)) {
            chatHandler.sendCommandFeedback("You're not a player", true, sender);
            return false;
        }
        if (args.length < 1) {
            chatHandler.sendCommandFeedback("Not enough arguments", true, sender);
            return false;
        }
        Player player = (Player) sender;

        switch (args[0]) {
            case "create":

                if(partySystem.hasParty(player)) {
                    chatHandler.sendCommandFeedback("You already have a party", true, player);
                    return false;
                }
                partySystem.createParty(player);
                chatHandler.sendCommandFeedback("Party created :)"/*Keep the ':)'!*/, false, player);
                break;

            case "join":
                if(args.length < 2) {
                    chatHandler.sendCommandFeedback("You have to specify what party you want to join", true, player);
                    return false;
                }
                List<Player> playerArgs = utils.stringToPlayers(args[1], sender, true);
                if(playerArgs.isEmpty()) {
                    return false;
                }
                Player ownerJ = playerArgs.get(0);
                if(!partySystem.hasParty(ownerJ)) {
                    chatHandler.sendCommandFeedback(ownerJ.getName() + " has no party", true, player);
                    return false;
                }
                if(partySystem.isInParty(player, ownerJ)) {
                    chatHandler.sendCommandFeedback("You are already in " + ownerJ.getName() + "s party", true, player);
                    return false;
                }

                partySystem.requestJoin(player, ownerJ);
                break;

            case "leave":
                if(args.length < 2) {
                    chatHandler.sendCommandFeedback("You have to specify what party you want to leave", true, player);
                    return false;
                }
                List<Player> playerArgsL = utils.stringToPlayers(args[1], sender, true);
                if(playerArgsL.isEmpty()) {
                    return false;
                }
                Player ownerL = playerArgsL.get(0);
                if(partySystem.hasParty(ownerL)) {
                    chatHandler.sendCommandFeedback(ownerL.getName() + " has no party", true, player);
                    return false;
                }
                if(!partySystem.isInParty(player, ownerL)) {
                    chatHandler.sendCommandFeedback("You're not in " + ownerL.getName() + "s party", true, player);
                    return false;
                }
                partySystem.leaveParty(player, ownerL);
                chatHandler.sendCommandFeedback("You are no longer in " + ownerL.getName() + "s party", false, player);
                break;

            case "disband":
                if(!partySystem.hasParty(player)) {
                    chatHandler.sendCommandFeedback("You have no party to dispand", true, player);
                    return false;
                }
                partySystem.disbandParty(player);
                chatHandler.sendCommandFeedback("You have no party anymore", false, player);
                break;

            case "invite":
                if(args.length < 2) {
                    chatHandler.sendCommandFeedback("You have to specify who you want to invite", true, player);
                    return false;
                }
                List<Player> playerArgsI = utils.stringToPlayers(args[1], sender, true);
                if(playerArgsI.isEmpty()) {
                    return false;
                }
                Player playerI = playerArgsI.get(0);
                if(!partySystem.hasParty(player)) {
                    chatHandler.sendCommandFeedback("You don't have a party", true, player);
                    return false;
                }
                if(partySystem.isInParty(player, playerI)) {
                    chatHandler.sendCommandFeedback("You are already in " + playerI.getName() + "s party", true, player);
                    return false;
                }

                partySystem.invite(playerI, player);
                break;

            case "remove":
                break;
        }

        return false;
    }
}
