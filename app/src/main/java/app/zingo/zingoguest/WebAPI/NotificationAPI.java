package app.zingo.zingoguest.WebAPI;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.HotelNotification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels Tech on 21-09-2018.
 */

public interface NotificationAPI {

    @POST("Calculation/SendNotificationForMultipleDeviceByHotelId")
    Call<ArrayList<String>> sendnotificationToHotel(@Header("Authorization") String auth , @Body HotelNotification hotelNotification);
}
