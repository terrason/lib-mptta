package org.terramagnet.mptta;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    private App() {
    }
    private static App instance;

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }
    private Properties configuration;

    private Properties getConfiguration() throws IOException {
        if (configuration == null) {
            File configFile = new File("application.properties");
            if (!configFile.canRead()) {
                throw new IOException("无法定位配置文件：jdbc.properties");
            }
            FileInputStream fis = new FileInputStream(configFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            Properties prop = new Properties();
            prop.load(bis);
            bis.close();
            configuration = prop;
        }
        return configuration;
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(configuration.getProperty("jdbc.url"), configuration.getProperty("jdbc.username"), configuration.getProperty("jdbc.password"));
    }

    public static void main(String[] args) {
        App.getInstance();
    }
}
