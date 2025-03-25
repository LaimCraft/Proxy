package ru.laimcraft.proxy.commands;

import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ru.laimcraft.proxy.Proxy;
import ru.laimcraft.proxy.mysql.MySQLAccounts;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class Coins implements SimpleCommand {

    @Override
    public void execute(Invocation data) {
        Player player = (Player) data.source();
        if(!Proxy.authPlayers.containsKey(player.getUsername())) return;

        String[] args = data.arguments();

        if(args.length == 0) {
            int balance = MySQLAccounts.getBalance(player.getUsername());
            player.sendMessage(Component.text("Баланс: " + balance + " coins", NamedTextColor.GREEN));
        return;}

        switch (args[0].toLowerCase()) {
            case "pay":
                switch (args.length) {
                    case 3:
                        if (!checkPlayerName(args[1])) {
                            player.sendMessage(Component.text("Вы неправильно ввели имя пользователя",
                                    NamedTextColor.RED));return;}
                        String answer = MySQLAccounts.getLoginByLogin(args[1]);
                        if (answer == null || answer.isEmpty()) {
                            player.sendMessage(Component.text("Игрока с таким ником не существует!",
                                    NamedTextColor.DARK_RED));return;}
                        if (answer.equals("ex")) {
                            player.sendMessage(Component.text("Произошла системная ошибка обратитесь к Администрацие проекта",
                                    NamedTextColor.RED));return;}
                        if (!checkInteger(args[2])) {
                            player.sendMessage(Component.text("Вы неправильно ввели сумму перевода!",
                                    NamedTextColor.RED));return;}
                        int amount = Integer.parseInt(args[2]);
                        MySQLAccounts.pay(player.getUsername(), args[1], amount);
                        player.sendMessage(Component.text("Вы успешно перевели игроку " + args[1] + " " + amount + " coins",
                                NamedTextColor.DARK_GREEN));
                        if(Proxy.server.getAllPlayers().contains(args[1])) {
                            Proxy.server.getPlayer(args[1]).get()
                                    .sendMessage(Component.text("Вам перевел игрок " + player.getUsername() + " " + amount + " coins",
                                    NamedTextColor.DARK_GREEN));}
                        return;
                    default:
                        sendPayMessage(player);
                        return;
                }
            case "", "bal", "balance":
                switch (args.length) {
                    case 0, 1:
                        int balance = MySQLAccounts.getBalance(player.getUsername());
                        player.sendMessage(Component.text("Баланс: " + balance + " coins", NamedTextColor.GREEN));
                        return;
                    case 2:
                        if(!checkPlayerName(args[1])) {
                            player.sendMessage(Component.text("Вы неправильно ввели имя пользователя",
                                    NamedTextColor.RED));return;}
                        String answer = MySQLAccounts.getLoginByLogin(args[1]);
                        if(answer == null || answer.isEmpty()) {
                            player.sendMessage(Component.text("Игрока с таким ником не существует!",
                                    NamedTextColor.DARK_RED));return;}
                        if(answer.equals("ex")) {
                            player.sendMessage(Component.text("Произошла системная ошибка обратитесь к Администрацие проекта",
                                    NamedTextColor.RED));return;}
                        player.sendMessage(Component.text("Баланс игрока " + args[1]+  ": "
                                + MySQLAccounts.getBalance(args[1]) + " coins", NamedTextColor.GREEN));
                        return;
                    default:
                        sendHelpMessage(player);
                        return;
                }
            default:
                sendHelpMessage(player);
                return;
        }
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage(Component.text("Введите /coin -> проверить баланс", NamedTextColor.GOLD));
        player.sendMessage(Component.text("Введите /coin balance <ник игрока> -> проверить чужой баланс", NamedTextColor.GOLD));
        player.sendMessage(Component.text("Введите /coin pay <player> <amount> -> отправить деньги другому игроку", NamedTextColor.GOLD));
    }

    private void sendPayMessage(Player player) {
        player.sendMessage(Component.text("Введите /coin pay <player> <amount>", NamedTextColor.AQUA));
    }

    private boolean checkPlayerName(String player) {
        if(player == null || player.isEmpty()) return false;
        if(player.length() < 3 || player.length() > 16) return false;
        if(!Pattern.matches("^[a-zA-Z0-9_]+$", player)) return false;
        if(Character.isDigit(player.charAt(0))) return false;
        return true;}

    private boolean checkInteger(String Integer) {
        if(Integer == null || Integer.isEmpty()) return false;
        if(Integer.length() > 6) return false;
        if(!Pattern.matches("^[0-9]+$", Integer)) return false;
        if(!Character.isDigit(Integer.charAt(0))) return false;
        return true;}

    /*
       public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Вы не игрок!");
        return true;}
        switch (args.length) {
            case 0:
                sender.sendMessage(ChatColor.GREEN + "Баланс: " + core.accounts.getBalance(sender.getName()) + " coins");
                break;
            case 3:
                if(args[0] == null || args[0].isEmpty()) return true;
                if(args[0].equalsIgnoreCase("pay")) {
                    if(args[1] == null || args[1].isEmpty()) return true;
                    if(!checkPlayerName(args[1])) {
                        sender.sendMessage(ChatColor.RED + "Вы неправильно ввели имя пользователя которому хотите передать деньги");
                    return true;}
                    String login = core.accounts.getLoginByLogin(sender.getName());
                    if(login == null || login.isEmpty()) {
                        sender.sendMessage(ChatColor.DARK_RED + "Игрока которому вы пытаетесь передать деньги не существует!");
                    return true;}
                    if(!checkInteger(args[2])) {
                        sender.sendMessage(ChatColor.RED + "Вы ввели неверно сумму перевода!");
                    return true;}

                    int integer = Integer.parseInt(args[2]);
                    if(integer <= 0) {
                        sender.sendMessage(ChatColor.RED + "Сумма не может быть меньше 1 единицы!");
                    return true;}
                    if(sender.getName().equalsIgnoreCase(args[1])) {
                        sender.sendMessage(ChatColor.RED + "Нельзя переводить себе!");
                    return true;}
                    if(core.accounts.getBalance(sender.getName()) < integer) {
                        sender.sendMessage(ChatColor.RED + "У вас недостаточно средств!");
                    return true;}
                    core.accounts.pay(sender.getName(), args[1], integer);
                    sender.sendMessage(ChatColor.DARK_GREEN + "Вы успешно перевели игроку " + args[1] + " " + integer + " coins");
                    if(Bukkit.getOnlinePlayers().contains(args[1])) {
                        Bukkit.getPlayer(args[1]).sendMessage(ChatColor.DARK_GREEN + "Вам перевел игрок " + sender.getName() + " " + integer + " coins");}
                return true;}
                break;
            default:
                sender.sendMessage(ChatColor.GOLD + "Введите /coin -> проверить баланс");
                sender.sendMessage(ChatColor.GOLD + "Введите /coin pay <player> <amount> -> отправить деньги другому игроку");
                break;
        }
    return true;}
     */

}
