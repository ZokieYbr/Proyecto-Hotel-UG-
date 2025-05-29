package com.vane.hotel;

import java.util.Locale;
import java.util.ResourceBundle;

public class i18n {
    private static ResourceBundle messages;

    public static void reload() {
        String idioma = Config.get("idioma");
        Locale locale;
        switch (idioma) {
            case "en":
                locale = new Locale("en", "US");
                break;
            case "es":
            default:
                locale = new Locale("es", "MX");
                break;
        }
        messages = ResourceBundle.getBundle("lenguaje.mensajes", locale);
    }

    static {
        reload();
    }

    public static String get(String clave) {
        return messages.getString(clave);
    }
}