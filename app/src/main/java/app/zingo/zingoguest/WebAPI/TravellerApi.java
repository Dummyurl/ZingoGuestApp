package app.zingo.zingoguest.WebAPI;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.Traveller;
import app.zingo.zingoguest.Model.TravellerDocuments;
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

public interface TravellerApi {

    @GET("Travellers/GetTravellerByPhoneNumber/{PhoneNumber}")
    Call<ArrayList<Traveller>> fetchTravelerByPhone(@Header("Authorization") String authKey, @Path("PhoneNumber") String PhoneNumber);

    @GET("Travellers/{id}")
    Call<Traveller> fetchTravelerById(@Header("Authorization") String authKey, @Path("id") int id);

    @POST("TravellerDocuments")
    Call<TravellerDocuments> postDocument(@Header("Authorization") String authKey, @Body TravellerDocuments travellerDocuments);

    @PUT("TravellerDocuments/{id}")
    Call<TravellerDocuments> updateDocument(@Header("Authorization") String authKey,
                                            @Body TravellerDocuments travellerDocuments,
                                            @Path("id") int id);

    @PUT("Travellers/{id}")
    Call<Traveller> updatetarveller(@Header("Authorization") String authKey, @Body Traveller travellerDocuments,@Path("id") int id);

    @GET("TravellerDocuments/GetTravellerDocumentByTravellerId/{id}")
    Call<ArrayList<TravellerDocuments>> getDocument(@Header("Authorization") String authKey,@Path("id") int id);

    @POST("TravellerDocuments")
    Call<TravellerDocuments> postdocument(@Header("Authorization") String authKey,
                                          @Body TravellerDocuments travellerDocuments);


}
