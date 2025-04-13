package hotel.classes;
import java.io.Serializable;

/**
 *
 * @author Faysal Ahmed
 */
public class RoomFare implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roomType;
    private int pricePerDay;

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(int pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
