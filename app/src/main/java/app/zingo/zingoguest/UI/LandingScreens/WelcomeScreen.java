package app.zingo.zingoguest.UI.LandingScreens;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import app.zingo.zingoguest.Adapters.NavigationListAdapter;
import app.zingo.zingoguest.Google.TrackGps;
import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.HotelDetails;
import app.zingo.zingoguest.Model.NavBarItems;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Model.Traveller;
import app.zingo.zingoguest.Model.WeatherData;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.UI.BookingDetails.BookingHistoryActivity;
import app.zingo.zingoguest.UI.BookingDetails.MoreBookingsScreen;
import app.zingo.zingoguest.UI.BookingDetails.TripDetailsScreen;
import app.zingo.zingoguest.UI.ProfileScreen.ProfileActivity;
import app.zingo.zingoguest.UI.RoomViews.SelectRoom;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.BookingsApi;
import app.zingo.zingoguest.WebAPI.HotelOperations;
import app.zingo.zingoguest.WebAPI.RoomApi;
import app.zingo.zingoguest.WebAPI.TravellerApi;
import app.zingo.zingoguest.WebAPI.UploadApi;
import app.zingo.zingoguest.WebAPI.WeatherAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.squareup.picasso.Picasso;

import static android.support.v4.view.GravityCompat.START;

public class WelcomeScreen extends AppCompatActivity {

    //Ui declare
    private static  String TAG = "WelcomeScreen";
    private static DrawerLayout drawer;

    private static TextView cityField, detailsField, currentTemperatureField,weatherIcon,
                currentMonth,currentDate,currentDay,mGuestName,mGreeting;
    private static Typeface weatherFont;
    private static RelativeLayout mWeatherLayout;

    private static CardView mBookingLayout,mSelectRoom,mUpgradeRoom,
                               mViewBill,mRoomService,mAmenity,mFeedBack,mViewBookings;
    private static ImageView mHotelImage;
    private static AppCompatTextView mBookingId,mHotelName,mHotelLocation,mCheckIn,mCheckOut,
                                      mRoomNumText,mRoomNum,mViewDetails;

    private static LinearLayout mRoomLayout;

    private static CircleImageView mProfileImage;
    private static TextView mUserName;
    private static ListView navbar;

    //GPS
    int MY_PERMISSIONS_REQUEST_RESULT =1;
    TrackGps gps;
    double latitude,longitude;

    //Bookings
    ArrayList<Bookings> activebookingsList;
    Bookings activeBooking;

    //Traveller
    Traveller profile;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String status,selectedImage;

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
            navbar = (ListView) findViewById(R.id.navbar_list);
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

            mProfileImage = (CircleImageView) findViewById(R.id.user_image);
            mUserName = (TextView) findViewById(R.id.user_name);
            mViewBookings = (CardView) findViewById(R.id.view_more_booking);

            setUpNavigationDrawer();

            String guestName = PreferenceHandler.getInstance(WelcomeScreen.this).getUserName();

            getProfile(PreferenceHandler.getInstance(WelcomeScreen.this).getUserId(),"Normal");

            if(guestName!=null||!guestName.isEmpty()){
                mGuestName.setText("Hi "+guestName);
                mUserName.setText(""+guestName);

            }else{
                mGuestName.setText("Dear Guest");
                mUserName.setText("Dear Guest");
            }

            getTimeFromAndroid();


            weatherIcon = (TextView) findViewById(R.id.weather_icon);
            weatherFont = Typeface.createFromAsset(getAssets(), "font/weathericons-regular-webfont.ttf");
            weatherIcon.setTypeface(weatherFont);

            mWeatherLayout = (RelativeLayout)findViewById(R.id.weather_layout);

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                activeBooking =(Bookings)bundle.getSerializable("ActiveBooking");
            }

            if(activeBooking!=null){

                if(!activeBooking.getBookingStatus().equalsIgnoreCase("Quick")){

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

                getBookings("Active");
            }


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

            mViewBookings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent bookings = new Intent(WelcomeScreen.this,MoreBookingsScreen.class);
                    startActivity(bookings);
                }
            });

            SimpleDateFormat sdf = new SimpleDateFormat("MMM,yyyy");
            SimpleDateFormat sdfs = new SimpleDateFormat("dd");
            SimpleDateFormat sdfd = new SimpleDateFormat("EEEE");

            currentMonth.setText(""+sdf.format(new Date()));
            currentDate.setText(""+sdfs.format(new Date()));
            currentDay.setText(""+sdfd.format(new Date()));

            //getBookings("Active");

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

            final int userId = PreferenceHandler.getInstance(WelcomeScreen.this).getUserId();


            mUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (profile != null) {

                        Intent edit = new Intent(WelcomeScreen.this, ProfileActivity.class);
                        edit.putExtra("Profile",profile);
                        startActivity(edit);

                    }else if(userId!=0){

                        getProfile(userId,"Edit");

                    }

                }
            });
            mProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (profile != null) {

                        selectImage();

                    }else if(userId!=0){

                        getProfile(userId,"Image");

                    }

                }
            });

            mSelectRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Bookings",activeBooking);
                    bundle.putString("Hotel",mHotelName.getText().toString());
                    bundle.putString("Locality",mHotelLocation.getText().toString());
                    Intent bill = new Intent(WelcomeScreen.this, SelectRoom.class);
                    bill.putExtras(bundle);
                    startActivity(bill);
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
                                        mViewBookings.setVisibility(View.GONE);
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
                                    mViewBookings.setVisibility(View.GONE);
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


    public void getProfile(final int id,final String image){

      /*  final ProgressDialog dialog = new ProgressDialog(ActivityDetail.this);
        dialog.setMessage("Loading Packages");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final TravellerApi subCategoryAPI = Util.getClient().create(TravellerApi.class);
                Call<Traveller> getProf = subCategoryAPI.fetchTravelerById(Constants.auth_string,id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<Traveller>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            profile = response.body();

                            if(image!=null&&!image.isEmpty()){

                                if(image.equalsIgnoreCase("Image")){

                                    selectImage();
                                }else if(image.equalsIgnoreCase("Edit")){
                                    Intent edit = new Intent(WelcomeScreen.this, ProfileActivity.class);
                                    edit.putExtra("Profile",profile);
                                    startActivity(edit);
                                }else{


                                        String base=profile.getImages();
                                        if(base != null && !base.isEmpty()){
                                            Picasso.with(WelcomeScreen.this).load(base).placeholder(R.drawable.profle_image).
                                                    error(R.drawable.profle_image).into(mProfileImage);
                                            //mImage.setImageBitmap(
                                            //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                                        }

                                }
                            }else{

                                if(profile.getImages()!=null){

                                    String base=profile.getImages();
                                    if(base != null && !base.isEmpty()){
                                        Picasso.with(WelcomeScreen.this).load(base).placeholder(R.drawable.profle_image).
                                                error(R.drawable.profle_image).into(mProfileImage);
                                        //mImage.setImageBitmap(
                                        //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                                    }
                                }
                            }



                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        //Toast.makeText(.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeScreen.this);
        builder.setTitle("Add Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Choose from Library")) {

                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        try{


            Uri selectedImageUri = data.getData( );
            String picturePath = getPath( WelcomeScreen.this, selectedImageUri );
            Log.d("Picture Path", picturePath);
            String[] all_path = {picturePath};
            selectedImage = all_path[0];
            System.out.println("allpath === "+data.getPackage());
            for (String s:all_path)
            {
                //System.out.println(s);
                File imgFile = new  File(s);
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //addView(null,Util.getResizedBitmap(myBitmap,400));
                    addImage(null,Util.getResizedBitmap(myBitmap,700));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    public void addImage(String uri,Bitmap bitmap)
    {
        try{


            if(uri != null)
            {

            }
            else if(bitmap != null)
            {
                mProfileImage.setImageBitmap(bitmap);

                if(selectedImage != null && !selectedImage.isEmpty())
                {
                    File file = new File(selectedImage);

                    if(file.length() <= 1*1024*1024)
                    {
                        FileOutputStream out = null;
                        String[] filearray = selectedImage.split("/");
                        final String filename = getFilename(filearray[filearray.length-1]);

                        out = new FileOutputStream(filename);
                        Bitmap myBitmap = BitmapFactory.decodeFile(selectedImage);

//          write the compressed bitmap at the destination specified by filename.
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                        uploadImage(filename,profile);



                    }
                    else
                    {
                        compressImage(selectedImage,profile);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void uploadImage(final String filePath,final Traveller userProfile)
    {
        //String filePath = getRealPathFromURIPath(uri, ImageUploadActivity.this);

        final File file = new File(filePath);
        int size = 1*1024*1024;

        if(file != null)
        {
            if(file.length() > size)
            {
                System.out.println(file.length());
                compressImage(filePath,userProfile);
            }
            else
            {
                final ProgressDialog dialog = new ProgressDialog(WelcomeScreen.this);
                dialog.setCancelable(false);
                dialog.setTitle("Uploading Image..");
                dialog.show();
                Log.d("Image Upload", "Filename " + file.getName());

                RequestBody mFile = RequestBody.create(MediaType.parse("image"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                UploadApi uploadImage = Util.getClient().create(UploadApi.class);

                Call<String> fileUpload = uploadImage.uploadProfileImage(fileToUpload, filename);
                fileUpload.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        userProfile.setImages(Constants.IMAGE_URL+response.body().toString());

                        System.out.println("User Image = "+userProfile.getImages());

                        updateProfile(userProfile);



                        if(filePath.contains("MyFolder/Images"))
                        {
                            file.delete();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("UpdateCate", "Error " + t.getMessage());
                    }
                });
            }
        }
    }

    public String compressImage(String filePath,final  Traveller userProfile) {

        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = actualHeight/2;//2033.0f;
        float maxWidth = actualWidth/2;//1011.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String[] filearray = filePath.split("/");
        final String filename = getFilename(filearray[filearray.length-1]);
        try {
            out = new FileOutputStream(filename);


//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            uploadImage(filename,userProfile);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename(String filePath) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println("getFilePath = "+filePath);
        String uriSting;
        if(filePath.contains(".jpg"))
        {
            uriSting = (file.getAbsolutePath() + "/" + filePath);
        }
        else
        {
            uriSting = (file.getAbsolutePath() + "/" + filePath+".jpg" );
        }
        return uriSting;

    }

    private void updateProfile(final Traveller userProfile) {


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Updating Image..");
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                TravellerApi auditApi = Util.getClient().create(TravellerApi.class);
                Call<Traveller> response = auditApi.updatetarveller(Constants.auth_string,userProfile,userProfile.getTravellerId());
                response.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            Toast.makeText(WelcomeScreen.this,"Profile Image Updated",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(WelcomeScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(WelcomeScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void setUpNavigationDrawer() {

        TypedArray icons = getResources().obtainTypedArray(R.array.navnar_item_images);
        String[] title  = getResources().getStringArray(R.array.navbar_items);

        final ArrayList<NavBarItems> navBarItemsList = new ArrayList<>();

        for (int i=0;i<title.length;i++)
        {
            NavBarItems navbarItem = new NavBarItems(title[i],icons.getResourceId(i, -1));
            navBarItemsList.add(navbarItem);
        }
        //final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(title));
        NavigationListAdapter adapter = new NavigationListAdapter(getApplicationContext(),navBarItemsList);
        navbar.setAdapter(adapter);
        navbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayView(navBarItemsList.get(position).getTitle());
            }
        });




    }

    public void displayView(String i)
    {
        if(drawer != null)
            drawer.closeDrawer(START);


        switch (i)
        {
            case "Profile":
                Intent edit = new Intent(WelcomeScreen.this, ProfileActivity.class);
                edit.putExtra("Profile",profile);
                startActivity(edit);
                break;

            case "Bookings":
                Intent booking = new Intent(WelcomeScreen.this, BookingHistoryActivity.class);
                startActivity(booking);
                break;

            case "Pending Services":
                break;

            case "Logout":
                PreferenceHandler.getInstance(WelcomeScreen.this).clear();
                Intent logout = new Intent(WelcomeScreen.this,GuestLoginScreen.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logout);
                break;

        }
    }

}
