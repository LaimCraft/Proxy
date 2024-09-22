package ru.laimcraft.proxy.mysql;

import com.velocitypowered.api.proxy.Player;

public class SQLManager {
    public static boolean isAuth(Player player) {
        return isAuth(player.getUsername());
    }

    public static boolean isAuth(String player) {
        String login = MySQLProxy.get(player);
        if(login == null) return false;
        return !login.equals("ex");
    }

    public static boolean add(Player player) {
        return add(player.getUsername());
    }

    public static boolean add(String player) {
        return MySQLProxy.add(player);
    }

    public static boolean remove(Player player) {
        return remove(player.getUsername());
    }

    public static boolean remove(String player) {
        return MySQLProxy.remove(player);
    }

    public static void reset() {
        MySQLProxy.reset();
    }
}
