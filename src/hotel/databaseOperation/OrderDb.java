package hotel.databaseoperation;

import hotel.classes.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Faysal
 */


/// ######                       DORKAR NAI EI DB ER , ETA PORE BAD DIYE DIBO
public class OrderDb {

    private static final Logger logger = Logger.getLogger(OrderDb.class.getName());

    Connection conn = DataBaseConnection.connectTODB();
    PreparedStatement statement = null;
    ResultSet result = null;

    public void insertOrder(Order order) {
        try {
            String insertOrder = "insert into orderItem('booking_id','item_food','price','quantity','total') values(" + order.getBookingId() + ",'" + order.getFoodItem() + "'," + order.getPrice() + "," + order.getQuantity() + "," + order.getTotal() + ")";
            statement = conn.prepareStatement(insertOrder);
            logger.log(Level.INFO, ">>>>>>>>>> {0}", insertOrder);
            statement.execute();

            JOptionPane.showMessageDialog(null, "successfully inserted a new Order");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\n" + "Order Failed");
        } finally {
            flushStatmentOnly();
        }

    }

    public void flushAll() {
        {
            try {
                statement.close();
                result.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error closing database resources", ex);
            }
        }
    }

    private void flushStatmentOnly() {
        {
            try {
                statement.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error closing statement", ex);
            }
        }
    }

}
