package sh.plunzi.plunzichatplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import sh.plunzi.plunzichatplugin.PlunziChatPlugin;
import sh.plunzi.plunzichatplugin.chatSending.Debug;
import sh.plunzi.plunzichatplugin.chatSending.messages.Censorship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {
    private final FileManager fileManager = PlunziChatPlugin.FILE_MANAGER;
    private final String username = fileManager.getDatabaseUsername();
    private final String password = fileManager.getDatabasePassword();
    private final String connectionUrl = fileManager.getDatabaseConnectionUrl();
    Connection conn = null;

    public DatabaseManager() {
        try {
            conn = DriverManager.getConnection(connectionUrl, username, password);
        } catch (SQLException e) {
            Debug.throwException(e);
        }
    }

    public Censorship getCensorLevel(UUID player) {
        int censorLevel = 2;

        String sqlGetCensorLevel = "SELECT * FROM minecraftdb.players WHERE uuid = '" + player.toString() + "';";

        try (PreparedStatement prepareStatement = conn.prepareStatement(sqlGetCensorLevel);
             ResultSet resultSet = prepareStatement.executeQuery()) {

            while (resultSet.next()) {
                censorLevel = resultSet.getInt("censor_level");
            }
        } catch (SQLException e) {
            Debug.throwException(e);
        }

        switch (censorLevel) {
            case 0:
                return Censorship.NONE;
            case 1:
                return Censorship.LIGHT;
            default:
                return Censorship.HEAVY;
        }
    }

    public boolean setCensorLevel(UUID player, Censorship censorship) {
        return setCensorLevel(player, censorshipToInt(censorship));
    }
    public boolean setCensorLevel(UUID player, int censorLevel) {

        String sqlUpdateCensorLevel =
                "UPDATE minecraftdb.players " +
                        "SET censor_level = " + censorLevel +
                        " WHERE uuid = '" + player.toString() + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlUpdateCensorLevel);
            prepareStatement.execute();

        } catch (SQLException e) {
            Debug.throwException(e);
            return false;
        }
        return true;
    }

    public boolean doesPlayerExist(UUID player) {
        String sqlGetPlayer =
                "SELECT exists(SELECT * from minecraftdb.players WHERE uuid='" + player + "') AS is_real;";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlGetPlayer);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("is_real") == 1;
            }
            return false;
        } catch (SQLException e) {
            Debug.throwException(e);
            return false;
        }
    }

    public void createPlayerEntry(UUID player, Censorship censorLevel, boolean isAdmin, int playerscore, String friends) {

        String sqlUpdateCensorLevel =
                "INSERT INTO minecraftdb.players " +
                        "(uuid, censor_level, is_admin, playerscore, friends) " +
                        "VALUES ('" + player + "', '" + censorshipToInt(censorLevel) + "', '" + (isAdmin?1:0) + "', '" + playerscore + "', '" + friends + "');";

        try {

            PreparedStatement prepareStatement = conn.prepareStatement(sqlUpdateCensorLevel);
            prepareStatement.execute();

        } catch (SQLException e) {
            Debug.throwException(e);
        }

    }

    public List<OfflinePlayer> getAdmins() {

        List<OfflinePlayer> admins = new ArrayList<>();

        String sqlSelectAllAdmins = "SELECT * FROM minecraftdb.players WHERE is_admin = 1;";


        try (PreparedStatement ps = conn.prepareStatement(sqlSelectAllAdmins);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                admins.add(Bukkit.getOfflinePlayer(uuid));
            }
        } catch (SQLException e) {
            Debug.throwException(e);
            return null;
        }
        return admins;
    }

    public void deletePlayer(UUID player) {
        String sqlDeletePlayer =
                "DELETE FROM minecraftdb.players WHERE uuid = '" + player + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlDeletePlayer);
            prepareStatement.execute();
        } catch (SQLException e) {
            Debug.throwException(e);
        }
    }

    public boolean isPlayerAdmin(UUID player) {
        String sqlGetPlayer =
                "select is_admin from minecraftdb.players where " +
                        "uuid='" + player + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlGetPlayer);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("is_admin") == 1;
            }
            return false;
        } catch (SQLException e) {
            Debug.throwException(e);
            return false;
        }
    }

    public void setAdmin(UUID player, boolean isAdmin) {

        String sqlUpdateCensorLevel =
                "UPDATE minecraftdb.players " +
                        "SET is_admin = " + (isAdmin?1:0) +
                        " WHERE uuid = '" + player + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlUpdateCensorLevel);
            prepareStatement.execute();

        } catch (SQLException e) {
            Debug.throwException(e);
        }
    }

    private String getFriendsAsString(UUID player) {
        String sqlGetFriends = "SELECT friends FROM minecraftdb.players WHERE uuid='" + player + "';";

        try (PreparedStatement ps = conn.prepareStatement(sqlGetFriends);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String friendsAsString = rs.getString("friends");

                friendsAsString = friendsAsString.replace(",,", ",");
                if(friendsAsString.endsWith(",")) {
                    friendsAsString = friendsAsString.substring(0, friendsAsString.length() - 1);
                }

                if(friendsAsString.length() ==  0) {
                    friendsAsString = "0";
                }

                return friendsAsString;
            }
        } catch (SQLException e) {
            Debug.throwException(e);
        }
        return "0";
    }

    public List<OfflinePlayer> getFriends(UUID player) {

        List<OfflinePlayer> friends = new ArrayList<>();

        List<Integer> friendsAsInIds = new ArrayList<>();
        String friendsAsString = getFriendsAsString(player);

        for(String s : friendsAsString.split(",")) friendsAsInIds.add(Integer.valueOf(s));

        for (int i : friendsAsInIds) {

            if(i ==  0) continue;

            UUID uuid = inIdToUUID(i);
            if(uuid !=  null)
            friends.add(Bukkit.getOfflinePlayer(uuid));
        }

        return friends;
    }

    public void addFriend(UUID player, UUID friend) {

        String currentFriendsAsInIds = getFriendsAsString(player);

        String newFriendsAsInIds = currentFriendsAsInIds + "," + UUIDToInId(friend);

        if(newFriendsAsInIds.startsWith("0")) {
            newFriendsAsInIds = newFriendsAsInIds.replace("0,", "");
        }

        String sqlUpdateFriends =
                "UPDATE minecraftdb.players " +
                        "SET friends = '" + newFriendsAsInIds + "'" +
                        " WHERE uuid = '" + player + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlUpdateFriends);
            prepareStatement.execute();
        } catch (SQLException e) {
            Debug.throwException(e);
        }
    }

    public void removeFriend(UUID player, UUID friend) {

        String sqlGetFriends = "SELECT friends FROM minecraftdb.players WHERE uuid='" + player + "';";

        String currentFriendsAsInIds = "";

        try (PreparedStatement ps = conn.prepareStatement(sqlGetFriends);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                currentFriendsAsInIds = rs.getString("friends");
            }
        } catch (SQLException e) {
            Debug.throwException(e);
            return;
        }
        int friendAsInId = UUIDToInId(friend);


        String newFriendsAsInIds = currentFriendsAsInIds.replace(String.valueOf(friendAsInId), "");


        newFriendsAsInIds = newFriendsAsInIds.replace(",,", ",");
        if(newFriendsAsInIds.endsWith(",")) {
            newFriendsAsInIds = newFriendsAsInIds.substring(0, newFriendsAsInIds.length() - 1);
        }

        if(newFriendsAsInIds.length() ==  0) {
            newFriendsAsInIds = "0";
        }

        String sqlUpdateFriends =
                "UPDATE minecraftdb.players " +
                        "SET friends = '" + newFriendsAsInIds + "'" +
                        " WHERE uuid = '" + player + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlUpdateFriends);
            prepareStatement.execute();
        } catch (SQLException e) {
            Debug.throwException(e);
        }
    }


    private UUID inIdToUUID(int inId) {
        String sqlGetPlayer =
                    "SELECT uuid FROM minecraftdb.players WHERE " +
                        "internal_id='" + inId + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlGetPlayer);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                return UUID.fromString(resultSet.getString("uuid"));
            }
            return null;
        } catch (SQLException e) {
            Debug.throwException(e);
            return null;
        }
    }
    private int UUIDToInId(UUID uuid) {
        String sqlGetPlayer =
                "SELECT internal_id FROM minecraftdb.players WHERE " +
                        "uuid='" + uuid + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlGetPlayer);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("internal_id");
            }
            return 0;
        } catch (SQLException e) {
            Debug.throwException(e);
            return 0;
        }
    }




    private int censorshipToInt(Censorship censorship) {
        int censorLevel = 0;
        switch (censorship) {
            case LIGHT:
                censorLevel = 1;
                break;
            case HEAVY:
                censorLevel = 2;
                break;
        }
        return censorLevel;
    }
}
