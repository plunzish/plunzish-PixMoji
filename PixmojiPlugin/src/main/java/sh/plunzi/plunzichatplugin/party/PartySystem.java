package sh.plunzi.plunzichatplugin.party;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.ChatHandler;
import sh.plunzi.plunzichatplugin.utils.OtherUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PartySystem {
    ChatHandler chatHandler = PlunziChatPlugin.CHAT_HANDLER;
    OtherUtils utils = PlunziChatPlugin.UTILS;

    public HashMap<UUID, List<UUID>> parties = new HashMap<>();
    public HashMap<UUID, List<UUID>> partyInvites = new HashMap<>();
    public HashMap<UUID, List<UUID>> joinRequests = new HashMap<>();

    public void createParty(Player player) {
        parties.put(player.getUniqueId(), new ArrayList<>());
    }

    public void disbandParty(Player player) {
        parties.remove(player.getUniqueId());
    }

    public void changeOwner(Player oldOwner, Player newOwner) {
        List<UUID> players = new ArrayList<>(parties.get(oldOwner.getUniqueId()));
        parties.remove(oldOwner.getUniqueId());
        parties.put(newOwner.getUniqueId(), players);

    }

    public void invite(Player player, Player owner) {

        if(joinRequests.get(owner.getUniqueId()).contains(player.getUniqueId())) {
            joinParty(player, owner);
            return;
        }

        if (partyInvites.containsKey(owner.getUniqueId())) {
            List<UUID> invites = partyInvites.get(owner.getUniqueId());
            invites.add(owner.getUniqueId());
            partyInvites.replace(player.getUniqueId(), invites);

        } else {
            List<UUID> invites = new ArrayList<>();
            invites.add(owner.getUniqueId());
            partyInvites.put(player.getUniqueId(), invites);
        }

        chatHandler.sendCommandFeedback("You invited " + player.getName() + " to your party! (they have 2 minutes to accept)", false, player);
        chatHandler.sendCommandFeedback(Component.text("You were invited to join " + owner.getName() + "s party! (you have 2 minutes to accept)")
                        .append(Component.text("\n"))
                        .append(
                                utils.buildComponent("[Accept]", Color.fromRGB(0x00ff0c), Color.fromRGB(0x008831))
                                        .clickEvent(ClickEvent.runCommand("/party join " + owner.getName()))
                        ).append(Component.text(" "))
                , false, owner);
    }
    public void requestJoin(Player player, Player owner) {

        if(partyInvites.get(owner.getUniqueId()).contains(player.getUniqueId())) {
            joinParty(player, owner);
            return;
        }

        if (joinRequests.containsKey(owner.getUniqueId())) {
            List<UUID> requests = joinRequests.get(owner.getUniqueId());
            requests.add(owner.getUniqueId());
            joinRequests.replace(player.getUniqueId(), requests);

        } else {
            List<UUID> requests = new ArrayList<>();
            requests.add(owner.getUniqueId());
            joinRequests.put(player.getUniqueId(), requests);
        }

        chatHandler.sendCommandFeedback("You requested to join " + owner.getName() + "s party! (they have 2 minutes to accept)", false, player);
        chatHandler.sendCommandFeedback(Component.text(player.getName() + " wants to join your party! (you have 2 minutes to accept)")
                        .append(Component.text("\n"))
                        .append(
                                utils.buildComponent("[Accept]", Color.fromRGB(0x00ff0c), Color.fromRGB(0x008831))
                                        .clickEvent(ClickEvent.runCommand("/party invite " + owner.getName()))
                        ).append(Component.text(" "))
                , false, owner);
    }

    public void joinParty(Player player, Player owner) {

        chatHandler.sendCommandFeedback("You joined " + owner.getName() + "s party!", false, player);
        chatHandler.sendCommandFeedback(player.getName() + " joined your party!", false, owner);

        joinRequests.remove(player.getUniqueId());
        partyInvites.remove(player.getUniqueId());

        List<UUID> players = new ArrayList<>(parties.get(owner.getUniqueId()));
        players.add(player.getUniqueId());
        parties.replace(owner.getUniqueId(), players);

    }

    public void leaveParty(Player player, Player owner) {
        if(!parties.get(owner.getUniqueId()).contains(player.getUniqueId())) return;

        List<UUID> players = new ArrayList<>(parties.get(owner.getUniqueId()));
        players.remove(player.getUniqueId());
        parties.replace(owner.getUniqueId(), players);

    }

    public boolean isInParty(Player player, Player owner) {

        if(parties.containsKey(owner.getUniqueId()))
        return parties.get(owner.getUniqueId()).contains(player.getUniqueId());

        return false;
    }

    public boolean hasParty(Player player) {
        return parties.containsKey(player.getUniqueId());
    }




    //TODO
//    /party create
//=> creates a party
//
///party invite *
//            => invites * to the party previously created
//
///party remove *
//            => removes *
//
//            /party leave
//=> leaves party
//
///party join *
//            => sends join request to *
//
//            /party disband
//=> disbands party
//
///p <String>| /party chat <String>
//=> sends a message into the party chat


}
