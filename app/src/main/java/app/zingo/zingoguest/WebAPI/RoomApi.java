package app.zingo.zingoguest.WebAPI;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.RoomCategories;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public interface RoomApi {

    @PUT(API.ROOMS+"/UpdateRooms/{id}")
    Call<Rooms> updateRoom(@Header("Authorization") String authKey, @Path("id") int id, @Body Rooms rooms);

    @GET(API.ROOMS+"/{id}")
    Call<Rooms> getRoom(@Header("Authorization") String authKey, @Path("id") int id);

    @GET(API.ROOM_CATEGORIES+"/GetRoomCategoriesByHotelId/{HotelId}")
    Call<ArrayList<RoomCategories>> fetchRoomCategoriesByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);
}
