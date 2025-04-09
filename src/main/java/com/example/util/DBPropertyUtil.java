package com.example.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getConnectionString(String propertyFileName) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(propertyFileName)) {
            props.load(fis);
            
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            
            return String.format("%s?user=%s&password=%s", url, user, password);
        }
    }
}
