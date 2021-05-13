package client.lang;

import java.util.ListResourceBundle;

public class Resource_ru_RU extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"loginTitle", "Вход"},
                {"login", "Логин"},
                {"password", "Пароль"},
                {"enter", "Войти"},
                {"register", "Регистрация"},
        };
    }
}
