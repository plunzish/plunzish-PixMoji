package sh.plunzi.plunzichatplugin.dataUtils;

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
    private final String connectionUrl = "jdbc:mysql://localhost:3306/";
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

        String sqlGetCensorLevel = "SELECT * FROM minecraftdb.players WHERE uuid = '" + player.toString() + "'";

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
            while (resultSet.next()) {
                return resultSet.getInt("is_real") == 1;
            }
            return false;
        } catch (SQLException e) {
            Debug.throwException(e);
            return false;
        }
    }

    public boolean createPlayerEntry(UUID player, Censorship censorLevel, boolean isAdmin, int playerscore) {

        String sqlUpdateCensorLevel =
                "INSERT INTO `minecraftdb`.`players` " +
                        "(`uuid`, `censor_level`, `is_admin`, `playerscore`) " +
                        "VALUES ('" + player + "', '" + censorshipToInt(censorLevel) + "', '" + (isAdmin?1:0) + "', '" + playerscore + "');\n";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlUpdateCensorLevel);
            prepareStatement.execute();

        } catch (SQLException e) {
            Debug.throwException(e);
            return false;
        }
        return true;
    }

    public List<OfflinePlayer> getAdmins() {

        List<OfflinePlayer> admins = new ArrayList<>();

        String sqlSelectAllAdmins = "SELECT * FROM minecraftdb.players WHERE is_admin = 1";


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

    public boolean deletePlayer(UUID player) {
        String sqlDeletePlayer =
                "DELETE FROM `minecraftdb`.`players` WHERE `uuid` = '" + player + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlDeletePlayer);
            prepareStatement.execute();
        } catch (SQLException e) {
            Debug.throwException(e);
            return false;
        }
        return true;
    }

    public boolean isPlayerAdmin(UUID player) {
        String sqlGetPlayer =
                "select is_admin from minecraftdb.players where " +
                        "uuid='" + player + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlGetPlayer);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("is_admin") == 1;
            }
            return false;
        } catch (SQLException e) {
            Debug.throwException(e);
            return false;
        }
    }

    public boolean setAdmin(UUID player, boolean isAdmin) {

        String sqlUpdateCensorLevel =
                "UPDATE minecraftdb.players " +
                        "SET is_admin = " + (isAdmin?1:0) +
                        " WHERE uuid = '" + player + "';";

        try {
            PreparedStatement prepareStatement = conn.prepareStatement(sqlUpdateCensorLevel);
            prepareStatement.execute();

        } catch (SQLException e) {
            Debug.throwException(e);
            return false;
        }
        return true;
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
