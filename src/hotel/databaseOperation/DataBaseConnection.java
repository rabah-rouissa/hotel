package hotel.databaseoperation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Faysal Ahmed
 */


public class DataBaseConnection {
    private static final Logger logger = Logger.getLogger(DataBaseConnection.class.getName());
    private DataBaseConnection() {
        throw new UnsupportedOperationException("Utility class");
    }
	static String url= "jdbc:mysql://localhost:3306/hotel?useUnicode=true" + 
			"&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" + 
			"serverTimezone=UTC" ;
    public static Connection connectTODB(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return  DriverManager.getConnection(url,"root","root");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Connection error", e);
            e.printStackTrace();
            return null;
        }
    }
    
}
