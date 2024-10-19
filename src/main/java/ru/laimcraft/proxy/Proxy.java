package ru.laimcraft.proxy;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;
import ru.laimcraft.proxy.commands.Coins;
import ru.laimcraft.proxy.commands.Online;
import ru.laimcraft.proxy.components.Servers;
import ru.laimcraft.proxy.mysql.SQLManager;

import java.nio.file.Path;

@Plugin(id = "proxy", name = "Proxy", version = "1.0.0", url = "https://github.com/LaimCraft/Proxy", authors = {"LaimCraft"})
public class Proxy {

    //public final List<String> AuthPlayers = new ArrayList<>();
    public final ProxyServer server;
    public final Logger logger;
    private static Proxy instance;
    public final Path dir;
    public final String secret = "YAm3Q5pioe4q";

    @Inject
    public Proxy(ProxyServer server, Logger logger, @DataDirectory Path dir) throws InterruptedException {
        this.server = server;
        this.logger = logger;
        this.dir = dir;
        instance = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if(!dir.toFile().exists()) {dir.toFile().mkdir();}
        server.getEventManager().register(this, new EventHandler());
        SQLManager.reset();
        Servers.registerAll(server);
        CommandMeta meta = server.getCommandManager().metaBuilder("coins")
                .plugin(this)
                .build();
        CommandMeta metaOnline = server.getCommandManager().metaBuilder("online")
                .plugin(this)
                .build();
        server.getCommandManager().register(meta, new Coins());
        server.getCommandManager().register(metaOnline, new Online());
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.create("server", "transfer"));
        // server.getCommandManager().register(coins());
    }

    @Subscribe
    public void onReload(ProxyReloadEvent event) {

        //Сохранение авторизованных игроков и их сессий
    }

    public static Proxy getInstance() {
        return instance;
    }

    private BrigadierCommand coins() {
        LiteralCommandNode<CommandSource> coins = BrigadierCommand.literalArgumentBuilder("coins") //Команда
                .executes(commandContext -> {
                    CommandSource source = commandContext.getSource();
                    Component text = Component.text("Coins", NamedTextColor.AQUA);
                    source.sendMessage(text);
                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("argument", StringArgumentType.word())
                        .suggests((commandContext, suggestionsBuilder) -> {
                            //suggestionsBuilder.suggest("hi"); есть all хз как убрать)
                            /*server.getAllPlayers().forEach(player -> suggestionsBuilder.suggest(
                                    player.getUsername(),
                                    VelocityBrigadierMessage.tooltip(MiniMessage.miniMessage().deserialize("<rainbow>" + player.getUsername())
                                    )
                            ));*/
                            //suggestionsBuilder.suggest("all"); //Убрал all
                            return suggestionsBuilder.buildFuture();
                        })
                        .executes(commandContext -> {
                            String argumentProvided = commandContext.getArgument("argumant", String.class);
                            server.getPlayer(argumentProvided).ifPresent(player ->
                                    player.sendMessage(Component.text("COINS EPTA!"))
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
        return new BrigadierCommand(coins);
    }
}
