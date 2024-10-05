package ru.laimcraft.proxy.mysql;

import ru.laimcraft.proxy.Proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLProxy {
    public static boolean add(String login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            connection.createStatement().executeUpdate("INSERT INTO `laimcraft`.`proxy` (`login`) VALUES (" +
                    "'"+login+"');");
            return true;
    } catch (SQLException ex) {
            Proxy.getInstance().logger.info("error:" + ex.getMessage());
            return false;}}

    public static String get(String login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM `laimcraft`.`proxy` WHERE login = '"+login+"';");
            while (resultSet.next()) {
                return resultSet.getString(1);
            }return null;
        } catch (SQLException ex) {
            Proxy.getInstance().logger.info(ex.getMessage());
            return "ex";}}

    public static boolean remove(String login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            connection.createStatement().execute("DELETE FROM `laimcraft`.`proxy` WHERE (`login` = '"+login+"');");
            return true;
        } catch (SQLException ex) {
            return false;}}

    public static void reset() {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            connection.createStatement().execute("TRUNCATE TABLE `laimcraft`.`proxy`;");
        }catch (SQLException ex) {
            Proxy.getInstance().logger.info("error: " + ex.getMessage());
        }
    }

}
