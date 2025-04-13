package hotel.tablemodel;

import hotel.databaseoperation.DatabaseOperation;
import hotel.databaseoperation.RoomDb;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public BookingTableModel(long start, long end) {
        iniColNames();
        fetchDataFromDB(start, end);
    }

    public void iniColNames() {
        Date localDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("d");
        int today = (Integer.parseInt(ft.format(localDate)) - 1) % getMonthLimit(localDate);
        columnNames = new String[11];
        columnNames[0] = "#";
        for (int i = 1; i < 11; i++) {
            today = today % getMonthLimit(localDate);
            today++;
            columnNames[i] = today + "";
        }
    }

    public int getMonthLimit(Date x) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(ft.format(x));
        ft.applyPattern("M");
        int month = Integer.parseInt(ft.format(x));

        switch (month) {
            case 2:
                return isLeapYear(year) ? 29 : 28;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            default:
                return 30;
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
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
            initializeEmptyData();

            ResultSet roomNames = new RoomDb().getAllRoomNames();

            for (int i = 0; i < rows; i++) {
                if (roomNames.next()) {
                    String roomName = roomNames.getString("room_no");
                    data[i][0] = roomName;

                    ResultSet result = new DatabaseOperation().getBookingInfo(start, end, roomName);

                    while (result.next()) {
                        long checkIn = Long.parseLong(result.getString("check_in"));
                        long checkOut = Long.parseLong(result.getString("check_out"));
                        processBookingDates(start, end, i, checkIn, checkOut);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "from Booking table model class\n " + ex.toString());
        }
    }

    private void initializeEmptyData() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = "";
            }
        }
    }

    private void processBookingDates(long start, long end, int rowIndex, long checkIn, long checkOut) {
        int getToday = Integer.parseInt(new SimpleDateFormat("d").format(new Date(start * 1000)));
        int checkInDay = Integer.parseInt(new SimpleDateFormat("d").format(new Date(checkIn * 1000)));
        int checkOutDay = checkOut > 0 ? Integer.parseInt(new SimpleDateFormat("d").format(new Date(checkOut * 1000))) : 0;

        if (checkIn <= start && (checkOut == 0 || checkOut <= end)) {
            data[rowIndex][1] = "<<";
        } else if (checkIn > start && checkOut < end) {
            data[rowIndex][(checkInDay - getToday) + 1] = ">";
            data[rowIndex][(checkOutDay - getToday) + 1] = "<";
        } else if (checkIn <= end && (checkOut == 0 || checkOut > end)) {
            data[rowIndex][(checkInDay - getToday) + 1] = ">>";
        }
    }
}
