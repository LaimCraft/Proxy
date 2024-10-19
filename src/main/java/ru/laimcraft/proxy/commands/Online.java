package ru.laimcraft.proxy.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ru.laimcraft.proxy.Proxy;
import ru.laimcraft.proxy.mysql.MySQLAccounts;
import ru.laimcraft.proxy.mysql.SQLManager;

import java.util.regex.Pattern;

public class Online implements SimpleCommand {

    @Override
    public void execute(Invocation data) {
        Player player = (Player) data.source();
        player.sendMessage(Component.text("Онлайн на всех серверах: " + Proxy.getInstance().server.getAllPlayers().size(),
                NamedTextColor.GOLD));
    }
}
