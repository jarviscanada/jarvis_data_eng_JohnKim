package ca.jrvs.apps.stockquote.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

    private static Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/properties.txt")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties() {
        return properties;
    }
}