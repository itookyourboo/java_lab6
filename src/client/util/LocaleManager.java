package client.util;

import java.util.*;

public class LocaleManager {
    private static List<OnLocaleChangedListener> listeners = new ArrayList<>();

    public static LinkedHashMap<String, Lang> langMap;
    private static Lang currentLang = Lang.ru_RU;
    private static Locale currentLocale = new Locale(currentLang.getLanguage(), currentLang.getCountry());

    public static void addOnLocaleChangedListener(OnLocaleChangedListener onLocaleChangedListener) {
        listeners.add(onLocaleChangedListener);
    }

    public static void setLanguage(Lang lang) {
        currentLang = lang;
        currentLocale = new Locale(currentLang.getLanguage(), currentLang.getCountry());
        listeners.forEach(listener -> listener.onLocaleChanged(currentLocale));
    }

    public static Lang getLanguage() {
        return currentLang;
    }

    public static Locale getLocale() {
        return currentLocale;
    }

    public static String getString(String stringID) {
        try {
            return ResourceBundle.getBundle("client.lang.Resource", currentLocale).getString(stringID);
        } catch (Exception e) {
            return currentLang.getLanguage() + stringID;
        }
    }

    static {
        langMap = new LinkedHashMap<>();
        for (Lang lang: Lang.values())
            langMap.put(lang.getName(), lang);
    }

    public static enum Lang {
        ru_RU("Русский", "ru", "RU"),
        cz_CZ("čeština", "cs", "CZ"),
        bg_BG("български", "bg", "BG"),
        es_CO("Español (colombia)", "es", "CO");

        private final String name;
        private final String language;
        private final String country;

        Lang(String name, String language, String country) {
            this.name = name;
            this.language = language;
            this.country = country;
        }

        public String getName() {
            return name;
        }

        public String getLanguage() {
            return language;
        }

        public String getCountry() {
            return country;
        }
    }

    public static interface OnLocaleChangedListener {
        void onLocaleChanged(Locale locale);
    }
}
