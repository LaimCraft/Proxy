package ru.laimcraft.proxy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Messages {
    public static final Component error =
            Component.text("Ошибка, обратитесь за помощью к Администрации проекта!")
                    .color(TextColor.color(0xFF0000));
    public static final Component errorDataBase =
            Component.text("Ошибка в базе данных, обратитесь за помощью к Администрации проекта!")
                    .color(TextColor.color(0xFF0000));
    public static final Component kickError =
            Component.text("Критическая ошибка, обратитесь за помощью к Администрации проекта!")
                    .color(TextColor.color(0xFF0000));
    public static final Component reloading =
            Component.text("Перезагрузка....")
                    .color(TextColor.color(0xFF0000));
    public static final Component kickReloading =
            Component.text("Вы были кикнуты с сервера так как сервер перезагрузился а вы не успели авторизоваться!")
                    .color(TextColor.color(0xFF0000));
    public static final Component loginSendMessage =
            Component.text("Для авторизации введите в чат /login <Пароль>")
                    .color(TextColor.color(0xFF0000));
    public static final Component AccountCreated =
            Component.text("Вы уже зарегистрировались!")
                    .color(TextColor.color(0xFF0000));
    public static final Component registerSuccess =
            Component.text("Вы успешно зарегистрировались!")
                    .color(TextColor.color(0xFF0000));
    public static final Component passwordMaxLength =
            Component.text("Пароль не может содержать больше 48 символов!")
                    .color(TextColor.color(0xFF0000));
    public static final Component noPassword =
            Component.text("Вы ввели неверный пароль!")
                    .color(TextColor.color(0xFF0000));
    public static final Component registerSendMessage =
            Component.text("Для регистрации введите в чат /register <Пароль>")
                    .color(TextColor.color(0xFF0000));
    public static final Component noRegister =
            Component.text("Вы ещё не зарегистрировались!")
                    .color(TextColor.color(0xFF0000));
    public static final Component auth =
            Component.text("Вы успешно авторизовались!")
                    .color(TextColor.color(0xFF0000));

    public static Component getError(String error) {
        return Component.text("Ошибка: \n" + error)
                .color(TextColor.color(0xFF0000));

    }
    public static Component kickUsername(String player, String username) {
        return Component.text("Вы зашли в игру под ником " + player + ", а зарегистрировались под ником " + username + "\n" +
                        "Поменяйте свой ник и попробуйте снова")
                .color(TextColor.color(0xFF0000));
    }
}
