package app.zingo.zingoguest.WebAPI;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.PaidAmenities;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public interface PaidAmenitiesOperation {


    @GET("Hotels/GetPaidAmenitiesByHotelId/{HotelId}")
    Call<ArrayList<PaidAmenities>> getAmenitiesByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int id);

    @GET("PaidAmenities/GetAmenitiesByCategoryId/{PaidAmenitiesCategoriesId}")
    Call<ArrayList<PaidAmenities>> getAmenitiesByCategoryId(@Header("Authorization") String authKey, @Path("PaidAmenitiesCategoriesId") int PaidAmenitiesCategoriesId);
}
