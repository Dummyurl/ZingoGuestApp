package app.zingo.zingoguest.WebAPI;

import app.zingo.zingoguest.Model.TravellerDeviceMapping;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public interface TravellerMapApi {

    @POST("TravellerDeviceMappings")
    Call<TravellerDeviceMapping> registerdevice(@Body TravellerDeviceMapping travellerMap);

}
