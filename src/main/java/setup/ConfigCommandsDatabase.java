package setup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigCommandsDatabase {

    public static boolean changeCommandStatus(String name, boolean newStatus, String guildId) {
        String sql = "UPDATE configCommands SET " + name + " = " + (newStatus ? 1 : 0) + " WHERE guildId = " + guildId;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean changeBulkCommandStatuses(List<String> commandNames, List<Boolean> newStatuses, String guildId) {
        String sql = "UPDATE configCommands SET ";

        for (int i = 0; i < commandNames.size()-1 && i < newStatuses.size()-1; i++) {
            sql = sql.concat(commandNames.get(i) + " = " + newStatuses.get(i) + ", ");
        }

        sql = sql.concat(commandNames.get(commandNames.size()-1) + " = " + newStatuses.get(newStatuses.size()-1) + " "); //You have to manually set this one so you don't mess up the statement with the ","

        sql = sql.concat("WHERE guildId = " + guildId);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //Use only when you know command is real
    public static boolean getCommandStatus(String name, String guildId) {
        String sql = "SELECT " + name + " from configCommands WHERE guildId = " + guildId;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet.getBoolean(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean checkCommandColumn(String name) {
        String sql = "SELECT * from configCommands";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                if (resultSet.getMetaData().getColumnName(i).equals(name)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<String> getCommandColumnNames() {
        String sql = "SELECT * from configCommands";
        List<String> commandNamesList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            for (int i = 2; i <= resultSet.getMetaData().getColumnCount(); i++) {
                commandNamesList.add(resultSet.getMetaData().getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandNamesList;
    }

    public static void addCommandColumn(String name) {
        String sql = "ALTER TABLE configCommands ADD COLUMN `" + name + "` BOOLEAN DEFAULT false NOT NULL";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean addNewGuild(String guildId) {
        String sql = "INSERT OR IGNORE INTO configCommands (guildId) VALUES (" + guildId + ")";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()){

            if(statement.executeUpdate(sql) > 0) {
                System.out.println("Guild added to configCommand database, all values set to defaults");

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }

        return false;
    }
}
