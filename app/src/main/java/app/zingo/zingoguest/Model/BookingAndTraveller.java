package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class BookingAndTraveller implements Serializable {

    @SerializedName("roomBooking")
    private Bookings roomBooking;

    @SerializedName("travellers")
    private Traveller travellers;

    public Bookings getRoomBooking() {
        return roomBooking;
    }

    public void setRoomBooking(Bookings roomBooking) {
        this.roomBooking = roomBooking;
    }

    public Traveller getTravellers() {
        return travellers;
    }

    public void setTravellers(Traveller travellers) {
        this.travellers = travellers;
    }
}
