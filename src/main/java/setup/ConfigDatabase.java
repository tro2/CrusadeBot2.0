package setup;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ConfigDatabase {

    public static String retrieve(String columnName, String guildId) {
        String sql = "SELECT "  + columnName + " FROM config WHERE guildId = " + guildId;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return resultSet.getString(columnName);
            }
            else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean setString(String columnName, String value, String guildId) {
        String sql = "UPDATE config SET " + columnName + " = '" + value + "' WHERE guildId = " + guildId;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

             return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean setBulkStrings(List<String> columnNames, List<String> values, String guildId) {
        String sql = "UPDATE config SET ";

        for (int i = 0; i < columnNames.size() && i < values.size(); i++) {
            sql = sql.concat(columnNames.get(i) + " = '" + values.get(i) + "'");
        }

        sql = sql.concat("WHERE guildId = " + guildId);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean setLong(String columnName, String value, String guildId) {
        String sql = "UPDATE config SET " + columnName + " = " + value + " WHERE guildId = " + guildId;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean setBulkLongs(List<String> columnNames, List<String> values, String guildId) {
        String sql = "UPDATE config SET ";

        for (int i = 0; i < columnNames.size()-1 && i < values.size()-1; i++) {
            sql = sql.concat(columnNames.get(i) + " = " + values.get(i) + ", ");
        }

        sql = sql.concat(columnNames.get(columnNames.size()-1) + " = " + values.get(values.size()-1) + " "); //You have to manually set this one so you don't mess up the statement with the ","

        sql = sql.concat("WHERE guildId = " + guildId);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean setJsonObject(String columnName, JSONObject object, String guildId) {
        String sql = "UPDATE config SET " + columnName + "= '" + object + "' WHERE guildId = " + guildId;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static JSONObject getJsonObject(String columnName, String guildId) {
        JSONParser parser = new JSONParser();
        String sql = "SELECT " + columnName + " FROM config WHERE guildId = " + guildId;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            return (JSONObject) parser.parse(resultSet.getString(columnName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }

    public static List<String> getConfigColumnNames(String guildId) {
        String sql = "SELECT * FROM config WHERE guildId = " + guildId;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()){

            ResultSet resultSet = statement.executeQuery(sql);

            ResultSetMetaData r = resultSet.getMetaData();

            List<String> list = new ArrayList<>();

            for (int i = 1; i <= r.getColumnCount(); i++) {
                list.add(r.getColumnName(i));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean addNewGuild(String guildId) {
        String sql = "INSERT OR IGNORE INTO config (guildId) VALUES (" + guildId + ")";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()){

            if(statement.executeUpdate(sql) > 0) {
                System.out.println("Guild added to config database, all values set to defaults");

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }

        return false;
    }

    public static boolean testPingDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String getPrefix(String guildId) {
        return retrieve("prefix", guildId);
    }
}
