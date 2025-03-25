package ru.laimcraft.proxy.mysql;

import ru.laimcraft.proxy.Proxy;

import java.sql.*;
import java.util.Date;

public class MySQLAccounts {

    public static String getLoginByLogin(String Login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT `login` FROM `laimcraft`.`accounts` WHERE login = '"+Login+"';");
            while (resultSet.next()) {
                return resultSet.getString(1);
            }return null;
        } catch (SQLException ex) {
            Proxy.logger.info(ex.getMessage());
            return "ex";}}

    public static int getBalance(String Login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT `balance` FROM `laimcraft`.`accounts` WHERE login = '"+Login+"';");
            while (resultSet.next()) {
                return resultSet.getInt(1);
            }return -1;
        } catch (SQLException ex) {
            Proxy.logger.info(ex.getMessage());
            return -2;}}

    public static boolean pay(String login, String pay, int amount) {
        removeBalance(login, amount);
        addBalance(pay, amount);
        return true;}

    public static void addBalance(String login, int amount) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            PreparedStatement ps = connection.prepareStatement("UPDATE `laimcraft`.`accounts` SET `balance` = balance + ? WHERE login = ?;");
            ps.setLong(1, amount);
            ps.setString(2, login);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Proxy.logger.info(ex.getMessage());}}

    public static void removeBalance(String login, int amount) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            PreparedStatement ps = connection.prepareStatement("UPDATE `laimcraft`.`accounts` SET `balance` = balance - ? WHERE login = ?;");
            ps.setLong(1, amount);
            ps.setString(2, login);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Proxy.logger.info(ex.getMessage());}}




    /*
    public boolean create(String login, String password) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            Date date = new Date();
            connection.createStatement().executeUpdate("INSERT INTO `laimcraft`.`accounts` (`login`, `password`, `regdate`, `authdate`) VALUES (" +
                    "'"+login+"', '"+password+"', '"+date.getTime()+"', '"+date.getTime()+"');");
            return true;
        } catch (SQLException ex) {
            Proxy.getInstance().logger.info(ex.getMessage());
            return false;}}

    public String getLoginByLogin(String Login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT `login` FROM `laimcraft`.`accounts` WHERE login = '"+Login+"';");
            while (resultSet.next()) {
                return resultSet.getString(1).toLowerCase();
            }return null;
        } catch (SQLException ex) {
            Proxy.getInstance().logger.info(ex.getMessage());
            return "ex";}}

    public int getBalance(String Login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT `balance` FROM `laimcraft`.`accounts` WHERE login = '"+Login+"';");
            while (resultSet.next()) {
                return resultSet.getInt(1);
            }return -1;
        } catch (SQLException ex) {
            Proxy.getInstance().logger.info(ex.getMessage());
            return -2;}}

    public boolean pay(String login, String pay, int amount) {
        removeBalance(login, amount);
        addBalance(pay, amount);
    return true;}

    public void addBalance(String login, int amount) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            PreparedStatement ps = connection.prepareStatement("UPDATE `laimcraft`.`accounts` SET `balance` = balance + ? WHERE login = ?;");
            ps.setLong(1, amount);
            ps.setString(2, login);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Proxy.getInstance().logger.info(ex.getMessage());}}

    public void removeBalance(String login, int amount) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            PreparedStatement ps = connection.prepareStatement("UPDATE `laimcraft`.`accounts` SET `balance` = balance - ? WHERE login = ?;");
            ps.setLong(1, amount);
            ps.setString(2, login);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Proxy.getInstance().logger.info(ex.getMessage());}}

    public String getUsernameByLogin(String login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT `login` FROM `laimcraft`.`accounts` WHERE login = '"+login+"';");
            while (resultSet.next()) {
                return resultSet.getString(1);
            }return null;
        } catch (SQLException ex) {
            Proxy.getInstance().logger.info(ex.getMessage());
            return "ex";}}

    public String getPasswordByLogin(String login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT `password` FROM `laimcraft`.`accounts` WHERE login = '"+login+"';");
            while (resultSet.next()) {
                return resultSet.getString(1);
            }return null;
        } catch (SQLException ex) {
            Proxy.getInstance().logger.info(ex.getMessage());
            return "ex";}}

    public boolean authDateUpdate(String login) {
        try (Connection connection = DriverManager.getConnection(Settings.host, Settings.user, Settings.password)) {
            Date date = new Date();
            PreparedStatement ps = connection.prepareStatement("UPDATE `laimcraft`.`accounts` SET `authdate` = ? WHERE login = ?;");
            ps.setLong(1, date.getTime());
            ps.setString(2, login);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Proxy.getInstance().logger.info(ex.getMessage());
            return false;}}*/
}
