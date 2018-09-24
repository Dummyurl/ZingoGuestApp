package app.zingo.zingoguest.UI.BookingDetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.zingoguest.Adapters.PaymentsAdapter;
import app.zingo.zingoguest.Adapters.ServicesAdapter;
import app.zingo.zingoguest.CustomViews.CustomFontTextView;
import app.zingo.zingoguest.Model.BookingAndTraveller;
import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.HotelNotification;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Model.Traveller;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.UI.LandingScreens.WelcomeScreen;
import app.zingo.zingoguest.UI.RoomViews.AmenityScreen;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.BookingsApi;
import app.zingo.zingoguest.WebAPI.NotificationAPI;
import app.zingo.zingoguest.WebAPI.RoomApi;
import app.zingo.zingoguest.WebAPI.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailsScreen extends AppCompatActivity {

    //View Declaration
    CustomFontTextView mName,mEmail,mPhoneNumber;

    Button mViewDetails;

    //Variable for data
    String mPhone,name,email,roomNumber,duration;
    int travellerId;
    ArrayList<Bookings> bookings1ArrayList;


    //Booking Details
    TextView mShortName,mPersonName,mBookingStatus,mBookingNo,mCheckInDate,mCheckOutDate,mRoomType,mGuestCount,mTotalAmounts,
            mDetailedBookingStatus,mBookedDate,mRoomBasePrice,mGSTAmount,mNoPaymentData,mNoServiceData;

    RecyclerView mDetailedPaymentLists,mDetailedServiceLists;
    Button mUpdateCheckout,mUpdateCancel;
    LinearLayout mUpdateButtonParent;




    Bookings bookingData;

    String activity;
    private long diffDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_booking_details_screen);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            setTitle("Booking Details");

            //activity = getIntent().getStringExtra("ActivityName");

            mName = (CustomFontTextView)findViewById(R.id.brief_detail_traveller_name);
            mEmail = (CustomFontTextView)findViewById(R.id.brief_detail_traveller_mail);
            mPhoneNumber = (CustomFontTextView)findViewById(R.id.brief_detail_traveller_mobile);
            mViewDetails = (Button)findViewById(R.id.brief_deails_view_btn) ;//total_bookings
            // mViewDetails.setVisibility(View.VISIBLE);

            //<-----------Booking Details--------------->
            mShortName = (TextView) findViewById(R.id.details_short_name);
            mPersonName = (TextView) findViewById(R.id.details_person_name);
            mBookingStatus = (TextView) findViewById(R.id.booking_details_booking_status);
            mBookingNo = (TextView) findViewById(R.id.details_booking_id);
            mCheckInDate = (TextView) findViewById(R.id.details_check_in);
            mCheckOutDate = (TextView) findViewById(R.id.details_check_out);
            mRoomType = (TextView) findViewById(R.id.details_room_type);
            mGuestCount = (TextView) findViewById(R.id.details_guest_count);
            mTotalAmounts = (TextView) findViewById(R.id.details_total_amount);
            mDetailedBookingStatus = (TextView) findViewById(R.id.details_booking_status);
            mBookedDate = (TextView) findViewById(R.id.details_booking_date);
            mRoomBasePrice = (TextView) findViewById(R.id.details_base_price);
            mGSTAmount = (TextView) findViewById(R.id.details_gst_price);
            mNoPaymentData = (TextView) findViewById(R.id.no_payment_data);
            mNoServiceData = (TextView) findViewById(R.id.no_service_data);
            mUpdateCheckout = (Button) findViewById(R.id.update_check_out);
            mUpdateCancel = (Button) findViewById(R.id.update_cancel);
            mUpdateButtonParent = (LinearLayout) findViewById(R.id.update_button_parent);
            //mSendPDF.setVisibility(View.GONE);
            mDetailedPaymentLists = (RecyclerView) findViewById(R.id.detailed_payments_lists);
            mDetailedServiceLists = (RecyclerView) findViewById(R.id.detailed_services_lists);




            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                travellerId = bundle.getInt("TravelelrId",0);
                mPhone = bundle.getString("PhoneNumber");
                email = bundle.getString("Email");
                name = bundle.getString("Name");



                Bookings bookings1 = (Bookings) bundle.getSerializable("Bookings");

                 if(bookings1 != null)
                {
                    try {
                        bookingData = bookings1;
                       setUpFields(bookingData);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            if(mPhone!=null&&!mPhone.isEmpty()){
                mPhoneNumber.setText(mPhone);

            }else{
                mPhoneNumber.setVisibility(View.GONE);
            }

            if(name!=null&&!name.isEmpty()){

                mName.setText(name);

            }else{

                mName.setVisibility(View.GONE);
            }

            if(email!=null&&!email.isEmpty()){

                mEmail.setText(email);

            }else{

                mEmail.setVisibility(View.GONE);
            }


                if(bookingData.getBookingStatus() != null &&(bookingData.getBookingStatus().equalsIgnoreCase("quick") ||
                        bookingData.getBookingStatus().equalsIgnoreCase("delay") ||
                        bookingData.getBookingStatus().equalsIgnoreCase("active")) )
                {
                    mUpdateButtonParent.setVisibility(View.VISIBLE);
                }
                else
                {
                    mUpdateButtonParent.setVisibility(View.GONE);
                }



            mUpdateCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bookingData != null)
                    {
                        if(bookingData.getBookingStatus().equalsIgnoreCase("Active"))
                        {
                           // updateBooking(bookingData,"Completed");
                            HotelNotification notify = new HotelNotification();
                            System.out.println(bookingData.getHotelId());
                            notify.setHotelId(bookingData.getHotelId());
                            //System.out.println("roomids = "+roomids);
                            notify.setMessage(bookingData.getBookingId()+"");
                            notify.setTitle("Checkout Request");
                            notify.setSenderId(Constants.senderId);
                            notify.setServerId(Constants.serverId);
                            sendcheckouteNotification(notify);
                        }
                        else
                        {
                            updateBooking(bookingData,"Active");
                        }
                    }
                }
            });



            mUpdateCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bookingData != null)
                    {

                        updateBooking(bookingData,"Cancelled");

                    }
                }
            });

            setTitle("Booking Details");





        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateBooking(final Bookings bookings1, final String status) {
        final ProgressDialog dialog = new ProgressDialog(BookingDetailsScreen.this);
        dialog.setTitle("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingsApi bookingApi = Util.getClient().create(BookingsApi.class);
                Bookings dBook = bookings1;
                dBook.setBookingStatus(status);

                Call<String> checkout = bookingApi.updateBookingStatus(Constants.auth_string,bookings1.getBookingId(),dBook);
                checkout.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 204)
                        {

                            Toast.makeText(BookingDetailsScreen.this,"Booking is updated successfully",Toast.LENGTH_SHORT).show();
                            //GuestBookingOverViewActivity.this.finish();
                            if(bookings1.getRoomId() != 0)
                            {
                                if(status.equalsIgnoreCase("Active"))
                                {
                                    getRoom(bookings1.getRoomId(),"Booked");
                                }
                                else
                                {
                                    getRoom(bookings1.getRoomId(),"Available");
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(BookingDetailsScreen.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }


    private void getRoom(final int id, final String roomStatus) {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                RoomApi checkOutApi = Util.getClient().create(RoomApi.class);
                //final Rooms updaterooms = null;

                Call<Rooms> checkout = checkOutApi.getRoom(Constants.auth_string,id);
                checkout.enqueue(new Callback<Rooms>() {
                    @Override
                    public void onResponse(Call<Rooms> call, Response<Rooms> response) {

                        if(response.code() == 200)
                        {

                            try{
                                if(response.body() != null)
                                {

                                    updateRoom(response.body(),roomStatus);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                        else
                        {

                            //Toast.makeText(getBaseContext(),"Please try after some time",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Rooms> call, Throwable t) {

                        //Toast.makeText(getBaseContext(),"Please try after some time",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void updateRoom(final Rooms responsestr, final String rstatus) {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                RoomApi checkOutApi = Util.getClient().create(RoomApi.class);
                Rooms updaterooms = responsestr;
                updaterooms.setStatus(rstatus);

                Call<Rooms> checkout = checkOutApi.updateRoom(Constants.auth_string,updaterooms.getRoomId(),updaterooms);
                checkout.enqueue(new Callback<Rooms>() {
                    @Override
                    public void onResponse(Call<Rooms> call, Response<Rooms> response) {

                        if(response.code() == 200)
                        {

                            try{
                                if(response.body() != null)
                                {


                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                        else
                        {

                            //Toast.makeText(getBaseContext(),"Please try after some time",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Rooms> call, Throwable t) {

                        //Toast.makeText(getBaseContext(),"Please try after some time",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    private void setUpFields(Bookings bookingAndTraveller) throws Exception{
        if(bookingAndTraveller != null )
        {


                    mShortName.setText(PreferenceHandler.getInstance(BookingDetailsScreen.this).getUserFullName().charAt(0)+"");
                    mPersonName.setText(PreferenceHandler.getInstance(BookingDetailsScreen.this).getUserFullName());



            if(bookingAndTraveller.getBookingStatus() != null &&
                    bookingAndTraveller.getBookingStatus().equalsIgnoreCase("Active"))
            {
                //mBookingStatus.setBackgroundColor(Color.parseColor(""));
                mBookingStatus.setText(bookingAndTraveller.getBookingStatus());
            }
            else if(bookingAndTraveller.getBookingStatus() != null &&
                    bookingAndTraveller.getBookingStatus().equalsIgnoreCase("Delay"))
            {
                //mBookingStatus.setBackgroundColor(Color.parseColor(""));
                mBookingStatus.setText("Upcoming");
            }
            else if(bookingAndTraveller.getBookingStatus() != null &&
                    bookingAndTraveller.getBookingStatus().equalsIgnoreCase("Quick"))
            {
                //mBookingStatus.setBackgroundColor(Color.parseColor(""));
                mBookingStatus.setText("Advance");
            }
            else if(bookingAndTraveller.getBookingStatus() != null &&
                    bookingAndTraveller.getBookingStatus().equalsIgnoreCase("Cancelled"))
            {
                //mBookingStatus.setBackgroundColor(Color.parseColor(""));
                mBookingStatus.setText(bookingAndTraveller.getBookingStatus());
            }
            else if(bookingAndTraveller.getBookingStatus() != null &&
                    bookingAndTraveller.getBookingStatus().equalsIgnoreCase("Abandoned"))
            {
                //mBookingStatus.setBackgroundColor(Color.parseColor(""));
                mBookingStatus.setText("No Show");
            }
            else if(bookingAndTraveller.getBookingStatus() != null &&
                    bookingAndTraveller.getBookingStatus().equalsIgnoreCase("Completed"))
            {
                //mBookingStatus.setBackgroundColor(Color.parseColor(""));
                mBookingStatus.setText(bookingAndTraveller.getBookingStatus());
            }

            duration(bookingAndTraveller.getCheckInDate(),bookingAndTraveller.getCheckOutDate());
            if(bookingAndTraveller.getRoomId() != 0)
            {
                getRooms(bookingAndTraveller.getRoomId());
            }
            mBookingNo.setText(""+bookingAndTraveller.getBookingId());
            mDetailedBookingStatus.setText(bookingAndTraveller.getBookingStatus());
            if(bookingAndTraveller.getCheckInDate().contains("/"))
            {
                mCheckInDate.setText(getBookingDateFormate(bookingAndTraveller.getCheckInDate()));

            }
            else if(bookingAndTraveller.getCheckInDate().contains("-"))
            {
                mCheckInDate.setText(getBookedFormate(bookingAndTraveller.getCheckInDate()));
            }

            if(bookingAndTraveller.getCheckOutDate().contains("/"))
            {
                mCheckOutDate.setText(getBookingDateFormate(bookingAndTraveller.getCheckOutDate()));

            }
            else if(bookingAndTraveller.getCheckOutDate().contains("-"))
            {
                mCheckOutDate.setText(getBookedFormate(bookingAndTraveller.getCheckOutDate()));
            }

            if(bookingAndTraveller.getBookingDate().contains("/"))
            {
                mBookedDate.setText(getBookingDateFormate(bookingAndTraveller.getBookingDate()));

            }
            else if(bookingAndTraveller.getBookingDate().contains("-"))
            {
                mBookedDate.setText(getBookedFormate(bookingAndTraveller.getBookingDate()));
            }
            mGuestCount.setText(bookingAndTraveller.getNoOfAdults()+"");
            mRoomType.setText(bookingAndTraveller.getRoomCategory());
            mTotalAmounts.setText("₹ "+bookingAndTraveller.getTotalAmount());
            System.out.println("Bae price = "+"₹ "+bookingAndTraveller.getSellRate());
            mRoomBasePrice.setText("₹ "+bookingAndTraveller.getSellRate());
            mGSTAmount.setText("₹ "+bookingAndTraveller.getGstAmount());
            if(bookingAndTraveller.getServicesList() != null &&
                    bookingAndTraveller.getServicesList().size() != 0)
            {
                mNoServiceData.setVisibility(View.GONE);
                mDetailedServiceLists.setVisibility(View.VISIBLE);
                ServicesAdapter sadapter = new ServicesAdapter(BookingDetailsScreen.this,bookingAndTraveller.getServicesList());
                mDetailedServiceLists.setAdapter(sadapter);
            }
            else
            {
                mNoServiceData.setVisibility(View.VISIBLE);
                mDetailedServiceLists.setVisibility(View.GONE);
            }

            if(bookingAndTraveller.getPaymentList() != null &&
                    bookingAndTraveller.getPaymentList().size() != 0)
            {
                mNoPaymentData.setVisibility(View.GONE);
                mDetailedPaymentLists.setVisibility(View.VISIBLE);
                PaymentsAdapter sadapter = new PaymentsAdapter(BookingDetailsScreen.this,
                        bookingAndTraveller.getPaymentList());
                mDetailedPaymentLists.setAdapter(sadapter);
            }
            else
            {
                mNoPaymentData.setVisibility(View.VISIBLE);
                mDetailedPaymentLists.setVisibility(View.GONE);
            }
        }
        else
        {
            Toast.makeText(BookingDetailsScreen.this,"Sorry, some data is missing",Toast.LENGTH_SHORT).show();
        }


    }

    public String getBookedFormate(String sdate) throws Exception
    {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(sdate);
            String sDate = new SimpleDateFormat("dd MMM yyyy").format(date);

            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }

    public String getBookingDateFormate(String bdate) throws Exception
    {
        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(bdate);
            String sDate = new SimpleDateFormat("dd MMM").format(date);

            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void duration(String fromm,String too) throws Exception{

        String from = fromm+" 00:00:00";
        String to = too+" 00:00:00";

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {

            if(from.contains("-"))
            {
                d1 = format1.parse(from);
                d2 = format1.parse(to);
            }
            else
            {
                d1 = format.parse(from);
                d2 = format.parse(to);
            }
            long diff = d2.getTime() - d1.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
            duration = String.valueOf(diffDays);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void getRooms(final int id){

        new ThreadExecuter().execute(new Runnable() {

            @Override
            public void run() {
                RoomApi apiService =
                        Util.getClient().create(RoomApi.class);
                Call<Rooms> call = apiService.getRoom(Constants.auth_string,id)/*getRooms()*/;

                call.enqueue(new Callback<Rooms>() {
                    @Override
                    public void onResponse(Call<Rooms> call, Response<Rooms> response) {

                        try
                        {
                            int statusCode = response.code();

                            if (statusCode == 200) {

                                Rooms list =  response.body();
                                roomNumber = list.getRoomNo();


                            }else {

                                Toast.makeText(BookingDetailsScreen.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Rooms> call, Throwable t) {
                        Log.e("TAG", t.toString());
                        Toast.makeText(BookingDetailsScreen.this, "Failed due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });


    }

    private void sendcheckouteNotification(final HotelNotification notification) {

        final ProgressDialog dialog = new ProgressDialog(BookingDetailsScreen.this);
        dialog.setMessage("Sending Request..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                NotificationAPI travellerApi = Util.getClient().create(NotificationAPI.class);
                Call<ArrayList<String>> response = travellerApi.sendnotificationToHotel(Constants.auth_string,notification);

                response.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {
                                Toast.makeText(BookingDetailsScreen.this,"Checkout request sent to hotel",Toast.LENGTH_SHORT).show();

                                //intent();
                                //SelectRoom.this.finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                BookingDetailsScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
