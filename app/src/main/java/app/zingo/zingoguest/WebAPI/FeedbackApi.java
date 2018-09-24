package app.zingo.zingoguest.WebAPI;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.FeedBack;
import app.zingo.zingoguest.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public interface FeedbackApi {

    //hotel feedback
    @POST(API.HOTEL_FEED_BACK)
    Call<FeedBack> postHotelFeedback(@Header("Authorization") String authKey, @Body FeedBack feedBack);


    //customer feedback
    @POST(API.CUSTOMER_FEED_BACK)
    Call<FeedBack> postCustomerFeedback(@Header("Authorization") String authKey, @Body FeedBack feedBack);

    @GET("RoomBooking/GetCustomerFeedBackByBookingId/{BookingId}")
    Call<ArrayList<FeedBack>> getCustomerFeedBack(@Header("Authorization") String authKey, @Path("BookingId") int id);
}
