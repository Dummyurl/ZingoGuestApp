package app.zingo.zingoguest.UI.LandingScreens;


import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;


import java.text.DateFormat;
import java.util.Date;

import app.zingo.zingoguest.Model.WeatherData;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeScreen extends AppCompatActivity {

    //Ui declare
    private static  String TAG = "WelcomeScreen";
    private static DrawerLayout drawer;

    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, updatedField,weatherIcon;
    Typeface weatherFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_main);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            //Toolbar initialize
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            cityField = (TextView)findViewById(R.id.city_field);
            updatedField = (TextView)findViewById(R.id.updated_field);
            detailsField = (TextView)findViewById(R.id.details_field);
            currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
            humidity_field = (TextView)findViewById(R.id.humidity_field);
            pressure_field = (TextView)findViewById(R.id.pressure_field);
            weatherIcon = (TextView) findViewById(R.id.weather_icon);
            weatherFont = Typeface.createFromAsset(getAssets(), "font/weathericons-regular-webfont.ttf");
            weatherIcon.setTypeface(weatherFont);



            getWeather("12.934533","77.626579");



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getWeather(final String lati,final String longi) throws Exception{


        WeatherAPI apiService =
                Util.getClient().create(WeatherAPI.class);

        Call<WeatherData> call = apiService.getWeather(Constants.weather_api,lati,longi,"metric");

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                try
                {
                    int statusCode = response.code();
                    if (statusCode == 200 || statusCode == 201) {

                        WeatherData  s = response.body();

                        DateFormat df = DateFormat.getDateTimeInstance();
                        cityField.setText(s.getName());
                        updatedField.setText(df.format(new Date(s.getDt())));
                        detailsField.setText(s.getWeather().get(0).getDescription());
                        currentTemperatureField.setText( s.getMain().getTemp()+ "°");
                        humidity_field.setText("Humidity: "+s.getMain().getHumidity());
                        pressure_field.setText("Pressure: "+s.getMain().getPressure());

                        weatherIcon.setText(Html.fromHtml(setWeatherIcon(s.getWeather().get(0).getId())));
                    }else {

                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
//                callGetStartEnd();
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

                Log.e("TAG", t.toString());
            }
        });



    }

    public static String setWeatherIcon(int actualId){
        int id = actualId / 100;
        System.out.println("icon id"+id);
        String icon = "";

            switch(id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
            }

        return icon;
    }


}
