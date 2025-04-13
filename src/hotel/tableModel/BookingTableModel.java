package hotel.tablemodel;


import hotel.databaseoperation.DatabaseOperation;
import hotel.databaseoperation.RoomDb;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author Faysal Ahmed
 */
public class BookingTableModel extends AbstractTableModel {

    private static final Logger logger = Logger.getLogger(BookingTableModel.class.getName());

    private String[] columnNames;
    private transient Object[][] data;

    public BookingTableModel(long start ,long end) {
        iniColNames();
        fetchDataFromDB(start, end);
    }

    public void iniColNames() {
        Date localDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("d");
        // -1 , because date starts with 0
        int today = ( Integer.parseInt(ft.format(localDate))-1 )%getMonthLimit(localDate);
        columnNames = new String[11];
        columnNames[0] = "#";
        for(int i =1;i<11;i++) {
            today = today%getMonthLimit(localDate);
            today++;
            columnNames[i] = today+"";
        }
    }

    public int getMonthLimit(Date x) {
        SimpleDateFormat ft = new SimpleDateFormat("M");
        int y = Integer.parseInt(ft.format(x));
        return switch (y) {
            case 2 -> 28;
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            default -> 30;
        };
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void fetchDataFromDB(long start, long end) {
        try {
            int rows = new RoomDb().getNoOfRooms();
            data = new Object[rows][11];
            for(int i=0;i<rows;i++) {
                for(int j=0;j<data[0].length;j++) {
                    data[i][j]= "";
                }
            }

            ResultSet result;
            ResultSet roomNames;
            roomNames = new RoomDb().getAllRoomNames();

            for (int i = 0; i < rows; i++) {
                if (roomNames.next()) {
                    String roomName = roomNames.getString("room_no");
                    data[i][0] = roomName;
                    result = new DatabaseOperation().getBookingInfo(start, end, roomName);
                    while (result.next()) {
                        logger.log(Level.INFO, "coming here for {0}", roomName);
                        long checkIn = Long.parseLong(result.getString("check_in"));
                        long checkOut = Long.parseLong(result.getString("check_out"));
                        logger.log(Level.INFO, "check in  {0} .... check out   {1}", new Object[]{new Date(checkIn*1000).toString(), new Date(checkOut*1000).toString()});

                        if(checkIn <= start && (checkOut == 0 || checkOut <= end)) {
                            logger.log(Level.INFO, "first LOOP {0}", roomName);
                            data[i][1] = "<<";
                        } else if(checkIn > start && checkOut < end) {
                            int checkInDay = Integer.parseInt(new SimpleDateFormat("d").format(new Date(checkIn*1000)));
                            int checkOutDay = Integer.parseInt(new SimpleDateFormat("d").format(new Date(checkOut*1000)));
                            int getToday = Integer.parseInt(new SimpleDateFormat("d").format(new Date(start*1000)));
                            logger.log(Level.INFO, "xxxxxxxxx {0}........ {1}", new Object[]{getToday, checkInDay});
                            data[i][(checkInDay-getToday)+1] = ">";
                            data[i][(checkOutDay-getToday)+1] = "<";
                        } else if(checkIn <= end && (checkOut == 0 || checkOut > end)) {
                            int xx = Integer.parseInt(new SimpleDateFormat("d").format(new Date(checkIn*1000))); 
                            int getToday = Integer.parseInt(new SimpleDateFormat("d").format(new Date(start*1000)));
                            logger.log(Level.INFO, "..... {0} ...........  {1}", new Object[]{getToday, xx});
                            data[i][(xx-getToday)+1] = ">>";
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "from Booking table model class\n " + ex.toString());
        }
    }
}
