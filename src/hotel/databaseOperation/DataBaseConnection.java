package hotel.databaseoperation;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Faysal Ahmed
 */
public class DataBaseConnection {
    private static final Logger logger = Logger.getLogger(DataBaseConnection.class.getName());
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = DataBaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                url = prop.getProperty("db.url");
                username = prop.getProperty("db.username");
                password = prop.getProperty("db.password");
            } else {
                logger.severe("db.properties file not found!");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load database properties", e);
        }
    }

    private DataBaseConnection() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Connection connectTODB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Connection error", e);
            return null;
        }
    }
}
