package app.zingo.zingoguest.WebAPI;

import app.zingo.zingoguest.Model.WeatherData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ZingoHotels Tech on 19-09-2018.
 */

public interface WeatherAPI {

    @GET("http://api.openweathermap.org/data/2.5/weather")//?lat={lati}&lon={longi}&units=metric"
    Call<WeatherData> getWeather(@Header("x-api-key") String authKey, @Query("lat") String lati,  @Query("lon") String longi,@Query("units") String units);

}
