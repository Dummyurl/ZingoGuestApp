package app.zingo.zingoguest.UI.LandingScreens;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import app.zingo.zingoguest.Google.TrackGps;
import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.HotelDetails;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Model.WeatherData;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.UI.BookingDetails.TripDetailsScreen;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.BookingsApi;
import app.zingo.zingoguest.WebAPI.HotelOperations;
import app.zingo.zingoguest.WebAPI.RoomApi;
import app.zingo.zingoguest.WebAPI.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.squareup.picasso.Picasso;

public class WelcomeScreen extends AppCompatActivity {

    //Ui declare
    private static  String TAG = "WelcomeScreen";
    private static DrawerLayout drawer;

    private static TextView cityField, detailsField, currentTemperatureField,weatherIcon,
                currentMonth,currentDate,currentDay,mGuestName,mGreeting;
    private static Typeface weatherFont;
    private static RelativeLayout mWeatherLayout;

    private static CardView mBookingLayout,mSelectRoom,mUpgradeRoom,
                               mViewBill,mRoomService,mAmenity,mFeedBack;
    private static ImageView mHotelImage;
    private static AppCompatTextView mBookingId,mHotelName,mHotelLocation,mCheckIn,mCheckOut,
                                      mRoomNumText,mRoomNum,mViewDetails;

    private static LinearLayout mRoomLayout;

    //GPS
    int MY_PERMISSIONS_REQUEST_RESULT =1;
    TrackGps gps;
    double latitude,longitude;

    //Bookings
    ArrayList<Bookings> activebookingsList;
    Bookings activeBooking;

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
            detailsField = (TextView)findViewById(R.id.details_field);
            currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
            currentMonth = (TextView)findViewById(R.id.month);
            currentDate = (TextView)findViewById(R.id.current_date);
            currentDay = (TextView)findViewById(R.id.current_day);

            mGuestName = (TextView)findViewById(R.id.guest_name);
            mGreeting = (TextView)findViewById(R.id.guest_greeting);

            mBookingLayout = (CardView)findViewById(R.id.booking_layout);
            mHotelImage = (ImageView)findViewById(R.id.hotel_wallpaper);
            mBookingId = (AppCompatTextView)findViewById(R.id.booking_id);
            mHotelName = (AppCompatTextView)findViewById(R.id.hotel_name);
            mHotelLocation = (AppCompatTextView)findViewById(R.id.hotel_location);
            mCheckIn = (AppCompatTextView)findViewById(R.id.check_in_date);
            mCheckOut = (AppCompatTextView)findViewById(R.id.check_out);
            mRoomNumText = (AppCompatTextView)findViewById(R.id.room_num_text);
            mRoomNum = (AppCompatTextView)findViewById(R.id.room_num);
            mViewDetails = (AppCompatTextView)findViewById(R.id.view_details);

            mRoomLayout = (LinearLayout) findViewById(R.id.room_layout);

            mSelectRoom = (CardView)findViewById(R.id.select_room);
            mUpgradeRoom = (CardView)findViewById(R.id.room_upgrade);
            mViewBill = (CardView)findViewById(R.id.view_bill);
            mRoomService = (CardView)findViewById(R.id.room_services);
            mAmenity = (CardView)findViewById(R.id.paid_amenity);
            mFeedBack = (CardView)findViewById(R.id.feed_back);

            String guestName = PreferenceHandler.getInstance(WelcomeScreen.this).getUserName();

            if(guestName!=null||!guestName.isEmpty()){
                mGuestName.setText("Hi "+guestName);
            }else{
                mGuestName.setText("Dear Guest");
            }

            getTimeFromAndroid();


            weatherIcon = (TextView) findViewById(R.id.weather_icon);
            weatherFont = Typeface.createFromAsset(getAssets(), "font/weathericons-regular-webfont.ttf");
            weatherIcon.setTypeface(weatherFont);

            mWeatherLayout = (RelativeLayout)findViewById(R.id.weather_layout);

            gps = new TrackGps(WelcomeScreen.this);

            if(checkPermissions()){

                if(locationCheck()){

                    if(gps.canGetLocation())
                    {
                        System.out.println("Long and lat Rev"+gps.getLatitude()+" = "+gps.getLongitude());
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        getWeather(""+latitude,""+longitude);



                    }
                    else
                    {
                        mWeatherLayout.setVisibility(View.GONE);
                    }

                }

            }else{
                mWeatherLayout.setVisibility(View.GONE);
            }


            SimpleDateFormat sdf = new SimpleDateFormat("MMM,yyyy");
            SimpleDateFormat sdfs = new SimpleDateFormat("dd");
            SimpleDateFormat sdfd = new SimpleDateFormat("EEEE");

            currentMonth.setText(""+sdf.format(new Date()));
            currentDate.setText(""+sdfs.format(new Date()));
            currentDay.setText(""+sdfd.format(new Date()));

            getBookings("Active");

            mViewBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(activeBooking!=null){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Bookings",activeBooking);
                        bundle.putString("Hotel",mHotelName.getText().toString());
                        bundle.putString("Locality",mHotelLocation.getText().toString());
                        Intent bill = new Intent(WelcomeScreen.this, TripDetailsScreen.class);
                        bill.putExtras(bundle);
                        startActivity(bill);
                    }


                }
            });

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
                        detailsField.setText(s.getWeather().get(0).getDescription());
                        currentTemperatureField.setText( s.getMain().getTemp()+ "°");

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


    //GPS Permission
    public boolean locationCheck() throws Exception{

        final boolean status = false;
        LocationManager lm = (LocationManager)WelcomeScreen.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(WelcomeScreen.this);
            dialog.setMessage("Gps Not enable");
            dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    WelcomeScreen.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    mWeatherLayout.setVisibility(View.GONE);


                }
            });
            dialog.show();
            return false;
        }else{
            return true;
        }
    }

    public boolean checkPermissions() {
        if ((ContextCompat.checkSelfPermission(WelcomeScreen.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(WelcomeScreen.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(WelcomeScreen.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(WelcomeScreen.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(WelcomeScreen.this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(WelcomeScreen.this, android.Manifest.permission.CALL_PHONE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(WelcomeScreen.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(WelcomeScreen.this, android.Manifest.permission.CAMERA))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(WelcomeScreen.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(WelcomeScreen.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length > 0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                gps = new TrackGps(WelcomeScreen.this);
            }
        }
    }

    private void getTimeFromAndroid() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        try{
            if(hours>=1 && hours<12){
                mGreeting.setText("Good Morning");
            }else if(hours>=12 && hours<16){
                mGreeting.setText("Good Afternoon");
            }else if(hours>=16 && hours<21){
                mGreeting.setText("Good Evening");
            }else if(hours>=21 && hours<24){
                mGreeting.setText("Good Night");
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void getBookings(final String status)
    {
        final ProgressDialog dialog = new ProgressDialog(WelcomeScreen.this);
        dialog.setMessage("Getting Bookings..");
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                BookingsApi bookingsApi = Util.getClient().create(BookingsApi.class);
                Call<ArrayList<Bookings>> getBookingresponse = bookingsApi.getBookingsByTravellerIdStatus(Constants.auth_string, PreferenceHandler.getInstance(WelcomeScreen.this).getUserId(),status);

                getBookingresponse.enqueue(new Callback<ArrayList<Bookings>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings>> call, Response<ArrayList<Bookings>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        int code = response.code();
                        if(code == 200 || code == 204)
                        {

                            activebookingsList = response.body();

                            if(activebookingsList!=null&&activebookingsList.size()!=0) {
                                Collections.sort(activebookingsList, new Comparator<Bookings>() {
                                    @Override
                                    public int compare(Bookings o1, Bookings o2) {
                                        String o1date = formatDate(o1.getCheckOutDate());
                                        String o2date = formatDate(o2.getCheckOutDate());

                                        return o1date.compareTo(o2date);
                                    }
                                });

                                    activeBooking = activebookingsList.get(0);

                                    if(activeBooking!=null){

                                        if(!status.equalsIgnoreCase("Quick")){

                                            mSelectRoom.setVisibility(View.GONE);

                                        }
                                        mCheckIn.setText(formatDate(activeBooking.getCheckInDate()));
                                        mCheckOut.setText(formatDate(activeBooking.getCheckOutDate()));
                                        mBookingId.setText("" + activeBooking.getBookingId());
                                        ///System.out.println(bookings1.getHotelId());
                                        getHotel(activeBooking.getHotelId());
                                        if (activeBooking.getRoomId() != 0) {
                                            getRoomDetails(activeBooking.getRoomId());
                                        } else {
                                            //Toast.makeText(WelcomeScreen.this, "You can request for room", Toast.LENGTH_SHORT).show();
                                            mRoomNum.setText("Request for Room");
                                            mRoomNumText.setVisibility(View.GONE);
                                        }
                                    }else{

                                        mBookingLayout.setVisibility(View.GONE);
                                        Toast.makeText(WelcomeScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }



                            }else
                            {

                                if(status!=null&&status.equalsIgnoreCase("Active")){

                                    getBookings("Quick");

                                }else if(status!=null&&status.equalsIgnoreCase("Quick")){

                                    getBookings("Delay");

                                }else{
                                    mBookingLayout.setVisibility(View.GONE);
                                    mRoomLayout.setVisibility(View.GONE);
                                    Toast.makeText(WelcomeScreen.this,"No Bookings found",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings>> call, Throwable t) {
                        System.out.println("Failed = "+t.getMessage());
                    }
                });
            }
        });
    }

    private void getRoomDetails(final int roomId) {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                RoomApi bookingsApi = Util.getClient().create(RoomApi.class);
                Call<Rooms> getBookingresponse = bookingsApi.getRoom(Constants.auth_string,
                        roomId);

                getBookingresponse.enqueue(new Callback<Rooms>() {
                    @Override
                    public void onResponse(Call<Rooms> call, Response<Rooms> response) {

                        int code = response.code();
                        if(code == 200 || code == 204)
                        {
                            Rooms rooms = response.body();

                            mRoomNum.setText(rooms.getRoomNo());

                        }
                    }

                    @Override
                    public void onFailure(Call<Rooms> call, Throwable t) {
                        System.out.println("Failed = "+t.getMessage());
                    }
                });
            }
        });
    }

    public void getHotel(final int id)
    {
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                HotelOperations hotelOperations = Util.getClient().create(HotelOperations.class);
                Call<HotelDetails> getHotelResponse = hotelOperations.getHotelByHotelId(Constants.auth_string,id);
                getHotelResponse.enqueue(new Callback<HotelDetails>() {
                    @Override
                    public void onResponse(Call<HotelDetails> call, Response<HotelDetails> response) {
                        if(response.code() == 200 || response.code() == 204)
                        {
                            HotelDetails hotelDetails = response.body();
                            if(hotelDetails != null)
                            {
                                mHotelName.setText(hotelDetails.getHotelName());
                                mHotelLocation.setText(hotelDetails.getLocalty());

                                if(hotelDetails.getHotelImage()!=null&&hotelDetails.getHotelImage().size()!=0){

                                    Picasso.with(WelcomeScreen.this).load(hotelDetails.getHotelImage().get(0).getImages()).placeholder(R.drawable.no_image).
                                            error(R.drawable.no_image).into(mHotelImage);

                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(WelcomeScreen.this,"Failed = "+response.code(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<HotelDetails> call, Throwable t) {
                        Toast.makeText(WelcomeScreen.this,"Failed = "+t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public String formatDate(String date)
    {
        String sDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yy");
        try {
            if(date.contains("-"))
            {
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                sDate = simpleDateFormat.format(date1);
            }
            else
            {
                Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(date);
                sDate = simpleDateFormat.format(date1);
            }
            //System.out.println("sDate = "+sDate);
            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
