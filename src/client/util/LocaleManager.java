package client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocaleManager {
    private static Map<Locale, Map<String, String>> dictionary = new HashMap<>();

    static {
        Map<String, String> ru_RU = new HashMap<>(),
                cz_CZ = new HashMap<>(),
                bg_BG = new HashMap<>(),
                es_CO = new HashMap<>();
        dictionary.put(Locale.ru_RU, ru_RU);
        dictionary.put(Locale.cz_CZ, cz_CZ);
        dictionary.put(Locale.bg_BG, bg_BG);
        dictionary.put(Locale.es_CO, es_CO);

        ru_RU.put("loginTitle", "Вход");
        ru_RU.put("login", "Логин");
        ru_RU.put("password", "Пароль");
        ru_RU.put("enter", "Войти");
        ru_RU.put("register", "Регистрация");
    }

    private static List<OnLocaleChangedListener> listeners = new ArrayList<>();

    private static Locale currentLocale = Locale.ru_RU;

    public static void addOnLocaleChangedListener(OnLocaleChangedListener onLocaleChangedListener) {
        listeners.add(onLocaleChangedListener);
    }

    public static void setLanguage(Locale locale) {
        currentLocale = locale;
        listeners.forEach(listener -> listener.onLocaleChanged(currentLocale));
    }

    public static String getString(String stringID) {
        return dictionary
                .getOrDefault(currentLocale, new HashMap<>())
                .getOrDefault(stringID, "No such stringID");
    }

    enum Locale {
        ru_RU("Русский"),
        cz_CZ("čeština"),
        bg_BG("български"),
        es_CO("Español (colombia)");

        private final String name;

        Locale(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    interface OnLocaleChangedListener {
        void onLocaleChanged(Locale locale);
    }
}
