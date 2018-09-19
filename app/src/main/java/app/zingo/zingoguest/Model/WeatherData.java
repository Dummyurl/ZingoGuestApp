package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 19-09-2018.
 */

public class WeatherData {

    @SerializedName("weather")
    private ArrayList<WeatherModel> weather;

    @SerializedName("main")
    private WeatherMain main;

    @SerializedName("name")
    private String name;

    @SerializedName("dt")
    private long dt;

    public ArrayList<WeatherModel> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherModel> weather) {
        this.weather = weather;
    }

    public WeatherMain getMain() {
        return main;
    }

    public void setMain(WeatherMain main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }
}
