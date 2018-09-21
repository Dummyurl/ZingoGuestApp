package app.zingo.zingoguest.WebAPI;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.HotelDetails;
import app.zingo.zingoguest.Model.RoomAvailablity;
import app.zingo.zingoguest.Model.RoomResponse;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public interface HotelOperations {

    @GET(API.HOTELS)
    Call<ArrayList<HotelDetails>> getHotel(@Header("Authorization") String authKey);

    @GET(API.HOTELS+"/{HotelId}")
    Call<HotelDetails> getHotelByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);

    @GET("Hotels/GetRoomsByHotelId/{HotelId}")
    Call<ArrayList<Rooms>> getRoomsByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);

    @POST("Agent/GetCategoryAvailabilityByDateAndHotelIdAndFromDate")
    Call<ArrayList<RoomResponse>> getRoomsStatusByHotelId(@Header("Authorization") String authKey, @Body RoomAvailablity body);
}
