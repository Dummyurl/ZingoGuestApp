package app.zingo.zingoguest.WebAPI;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.SearchBooking;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public interface BookingsApi {

    @POST("RoomBookings/SearchBooking")
    Call<ArrayList<Bookings>> getBookings(@Header("Authorization") String authKey, @Body SearchBooking searchBooking);

    @GET("RoomBooking/GetAllBooking/{HotelId}")
    Call<ArrayList<Bookings>> getBookingByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);

    @GET("RoomBookings/{id}")
    Call<Bookings> getBookingById(@Header("Authorization") String authKey,@Path("id") int id);

    @GET("RoomBooking/GetActiveBooking/{HotelId}")
    Call<ArrayList<Bookings>> getActiveBookingByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);

    @GET("RoomBookings/GetAllRoomBookingsByTravellerId/{TravellerId}")
    Call<ArrayList<Bookings>>  getBookingsByTravellerId(@Header("Authorization") String authKey, @Path("TravellerId") int TravellerId);

    @GET("RoomBookings/GetAllRoomBookingsByTravellerIdAndBookingStatus/{TravellerId}/{BookingStatus}")
    Call<ArrayList<Bookings>>  getBookingsByTravellerIdStatus(@Header("Authorization") String authKey, @Path("TravellerId") int TravellerId, @Path("BookingStatus") String Status);


   /* @GET("RoomBooking/GetCustomerFeedBackByBookingId/{BookingId}")
    Call<ArrayList<FeedBack>> getFeedBackByBookingId(@Header("Authorization") String authKey, @Path("BookingId") int BookingId);*/

}
