package app.zingo.zingoguest.WebAPI;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.Service;
import app.zingo.zingoguest.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public interface ServiceApi {

    @POST("Services/AddMultipleServices")
    Call<ArrayList<Service>> addMultipleServices(@Header("Authorization") String authstring, @Body ArrayList<Service> serviceArrayList);

    @PUT("Services/{id}")
    Call<Service> updateService(@Header("Authorization") String authKey, @Path("id") int id, @Body Service service);
}
