package com.vane.hotel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Config {
    private static Properties props = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try {
            props.load(Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String clave) {
        return props.getProperty(clave);
    }

    public static void set(String clave, String valor) {
        props.setProperty(clave, valor);
        try (OutputStream out = new FileOutputStream(
                Config.class.getClassLoader().getResource(CONFIG_FILE).getPath())) {
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}