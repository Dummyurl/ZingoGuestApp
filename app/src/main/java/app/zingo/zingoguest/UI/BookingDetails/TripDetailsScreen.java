package app.zingo.zingoguest.UI.BookingDetails;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.zingoguest.Adapters.PaymentsAdapter;
import app.zingo.zingoguest.Adapters.ServicesAdapter;
import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.HotelNotification;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.NotificationAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailsScreen extends AppCompatActivity {

    private static ImageView mBack;
    private static AppCompatButton  mCheckOutBtn,mSendInvoiceBtn;
    private static AppCompatTextView mHotelName,mBookingId,mCheckIn,mCheckOut,
                                 mRoomType,mRoomChargeDesc,mRoomChargeValue,mRoomCharge,
                                           mGstPercentage,mGstValue,mGST,mOther,mTotal;

    private static TextView mmTravellerName,mBookingNumber,mDurationOfStay,mDurationLeft,
            mCIT,mCOT, mPaxDetails,mPaidAmount,mRemainingAmount,mTotalAmount,mTotalBalance,
            mTotalPaid,mPreBookHotelName,mNoPaymentData,mNoServiceData,
            mPreHotelPlace,mTotalRoomBalance,mTotalRoom,mViewBreakUp;
    private static LinearLayout mPaymentBreakUp;
    RecyclerView mDetailedPaymentLists,mDetailedServiceLists;

    //Intent
    Bookings bookings;
    String hotelName,hotelPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_trip_details_screen);

            /*mBack = (ImageView)findViewById(R.id.back);

            mHotelName = (AppCompatTextView)findViewById(R.id.hotel_name_trip_details);
            mBookingId = (AppCompatTextView)findViewById(R.id.booking_number_trip_details);*/

          /*  mCheckOut = (AppCompatTextView)findViewById(R.id.check_out);
            mRoomType = (AppCompatTextView)findViewById(R.id.room_type);
            mRoomChargeDesc = (AppCompatTextView)findViewById(R.id.room_charge);
            mRoomChargeValue = (AppCompatTextView)findViewById(R.id.room_charge_value);
            mRoomCharge = (AppCompatTextView)findViewById(R.id.room_charge_value_text);
            mGstPercentage = (AppCompatTextView)findViewById(R.id.gst_value);
            mGstValue = (AppCompatTextView)findViewById(R.id.gst_amount);
            mGST = (AppCompatTextView)findViewById(R.id.gst_amount_text);
            mOther = (AppCompatTextView)findViewById(R.id.other_charge);
            mTotal = (AppCompatTextView)findViewById(R.id.total_charge);*/

            mRoomChargeDesc = (AppCompatTextView)findViewById(R.id.room_charge);
            mRoomChargeValue = (AppCompatTextView)findViewById(R.id.room_charge_value);
            mRoomCharge = (AppCompatTextView)findViewById(R.id.room_charge_value_text);
            mGstPercentage = (AppCompatTextView)findViewById(R.id.gst_value);
            mGstValue = (AppCompatTextView)findViewById(R.id.gst_amount);
            mGST = (AppCompatTextView)findViewById(R.id.gst_amount_text);
            mOther = (AppCompatTextView)findViewById(R.id.other_charge);

            mBack = (ImageView)findViewById(R.id.back);
            mCheckIn = (AppCompatTextView)findViewById(R.id.check_in_date);
            mCheckOut = (AppCompatTextView)findViewById(R.id.check_out);
            mCheckOutBtn = (AppCompatButton) findViewById(R.id.submit_bill);
            mSendInvoiceBtn = (AppCompatButton) findViewById(R.id.send_invoice);
            mPreBookHotelName = (TextView)findViewById(R.id.billing_hotel_name);
            mPreHotelPlace = (TextView)findViewById(R.id.bill_hotel_place);
            mCIT =(TextView)findViewById(R.id.brief_detail_check_in_time);
            mCOT =(TextView)findViewById(R.id.brief_detail_check_out_time);
            mmTravellerName=(TextView)findViewById(R.id.brief_detail_traveller_name);
            mmTravellerName.setText(PreferenceHandler.getInstance(TripDetailsScreen.this).getUserName());

            mDurationOfStay = (TextView) findViewById(R.id.brief_detail_duration_of_stay);
            mDurationLeft = (TextView) findViewById(R.id.brief_detail_duration_left);
            mPaxDetails = (TextView) findViewById(R.id.brief_detail_pax_details);
            mBookingNumber= (TextView) findViewById(R.id.booking_num);

            mTotalAmount = (TextView) findViewById(R.id.total_amount_info);
            mTotalRoomBalance = (TextView) findViewById(R.id.balance_amount_info);
            mTotalBalance = (TextView) findViewById(R.id.details_room_balance);
            mTotalPaid = (TextView) findViewById(R.id.paid_amount_info);

            mPaymentBreakUp = (LinearLayout) findViewById(R.id.payment_break_up);
            mNoPaymentData = (TextView) findViewById(R.id.no_payment_data);
            mNoServiceData = (TextView) findViewById(R.id.no_service_data);
            mViewBreakUp = (TextView) findViewById(R.id.view_break_up);
            mDetailedPaymentLists = (RecyclerView) findViewById(R.id.detailed_payments_lists);
            mDetailedServiceLists = (RecyclerView) findViewById(R.id.detailed_services_lists);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                bookings = (Bookings)bundle.getSerializable("Bookings");
                hotelName = bundle.getString("Hotel");
                hotelPlace = bundle.getString("Locality");
            }


            if(bookings!=null){
                setBookingsData(bookings);
            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            if(hotelName!=null&&!hotelName.isEmpty()){
                mPreBookHotelName.setText(""+hotelName);
            }else{
                mPreBookHotelName.setText("Zingo Hotels");
            }
            if(hotelPlace!=null&&!hotelPlace.isEmpty()){
                mPreHotelPlace.setText(""+hotelPlace);
            }else{
                mPreHotelPlace.setText("");
            }

            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TripDetailsScreen.this.finish();
                }
            });

            mCheckOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // updateBooking(bookingData,"Completed");
                    HotelNotification notify = new HotelNotification();
                    System.out.println(bookings.getHotelId());
                    notify.setHotelId(bookings.getHotelId());
                    //System.out.println("roomids = "+roomids);
                    notify.setMessage(bookings.getBookingId()+"");
                    notify.setTitle("Checkout Request");
                    notify.setSenderId(Constants.senderId);
                    notify.setServerId(Constants.serverId);
                    sendcheckouteNotification(notify);

                }
            });

            mViewBreakUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mPaymentBreakUp.getVisibility()==View.GONE){
                        mPaymentBreakUp.setVisibility(View.VISIBLE);
                        mViewBreakUp.setText("Hide Payment Breakup");
                    }else{
                        mPaymentBreakUp.setVisibility(View.GONE);
                        mViewBreakUp.setText("Show Payment Breakup");
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setBookingsData(final Bookings bookings){

        DecimalFormat df = new DecimalFormat("#,###.##");

        mCheckIn.setText(formatDate(bookings.getCheckInDate()));
        mCheckOut.setText(formatDate(bookings.getCheckOutDate()));
        mBookingNumber.setText("Booking Id: #"+bookings.getBookingId());
        mPaxDetails.setText(bookings.getNoOfAdults()+" Adults, "+bookings.getNoOfChild()+" Childs");

        if(bookings.getCheckInTime()==null){

            mCIT.setText("12:00 PM");
        }else{
            mCIT.setText(""+bookings.getCheckInTime());
        }

        if(bookings.getCheckOutTime()==null){

            mCOT.setText("11:00 AM");
        }else{
            mCOT.setText(""+bookings.getCheckOutTime());
        }

      //  int durationStay = duration(bookings.getCheckInTime(),bookings.getCheckInDate(),bookings.getCheckOutTime(),bookings.getCheckOutDate());
        mTotalAmount.setText("₹"+df.format(bookings.getTotalAmount()));

        int total = bookings.getTotalAmount();
        int balance = bookings.getBalanceAmount();

        mTotalBalance.setText("₹ "+df.format(bookings.getBalanceAmount()));
        mTotalPaid.setText("₹ "+df.format((total-balance)));

        if(bookings.getPaymentList() != null &&
                bookings.getPaymentList().size() != 0)
        {
            mNoPaymentData.setVisibility(View.GONE);
            mDetailedPaymentLists.setVisibility(View.VISIBLE);
            PaymentsAdapter sadapter = new PaymentsAdapter(TripDetailsScreen.this,
                    bookings.getPaymentList());
            mDetailedPaymentLists.setAdapter(sadapter);
        }
        else
        {
            mNoPaymentData.setVisibility(View.VISIBLE);
            mDetailedPaymentLists.setVisibility(View.GONE);
        }

        if(bookings.getServicesList() != null &&
                bookings.getServicesList().size() != 0)
        {
            mNoServiceData.setVisibility(View.GONE);
            mDetailedServiceLists.setVisibility(View.VISIBLE);
            ServicesAdapter sadapter = new ServicesAdapter(TripDetailsScreen.this,bookings.getServicesList());
            mDetailedServiceLists.setAdapter(sadapter);
        }
        else
        {
            mNoServiceData.setVisibility(View.VISIBLE);
            mDetailedServiceLists.setVisibility(View.GONE);
        }

        mRoomChargeDesc.setText("("+bookings.getNoOfRooms()+" room(s) × "+bookings.getDurationOfStay()+" night(s)");
        mRoomChargeValue.setText("₹ "+df.format(bookings.getSellRate()));

        int sellRate = bookings.getSellRate();
        int durationStay = duration(bookings.getCheckInTime(),bookings.getCheckInDate(),bookings.getCheckOutTime(),bookings.getCheckOutDate());

        int noOfRooms = bookings.getNoOfRooms();

        if(durationStay==0||noOfRooms==0){
            mRoomCharge.setText("( ₹"+df.format(sellRate)+"/per night)");
            if(noOfRooms==0){
                noOfRooms=1;
            }
        }else{
            int price = sellRate/(durationStay * noOfRooms);
            mRoomCharge.setText("( ₹"+df.format(price)+"/per night)");
        }

        mGstPercentage.setText("("+bookings.getGst()+"%)");
        mGstValue.setText("₹"+df.format(bookings.getGstAmount()));
        mGST.setText("(₹"+df.format(bookings.getGstAmount())+"×"+noOfRooms+"×"+durationStay+")");
        mOther.setText("₹"+df.format(bookings.getExtraCharges()));


       /* mBookingId.setText("Booking Id: " + bookings.getBookingId());
        mRoomType.setText(""+bookings.getRoomCategory());
        mRoomChargeDesc.setText("("+bookings.getNoOfRooms()+" room(s) × "+bookings.getDurationOfStay()+" night(s)");
        mRoomChargeValue.setText("₹ "+df.format(bookings.getSellRate()));

        int sellRate = bookings.getSellRate();
        int durationStay = duration(bookings.getCheckInTime(),bookings.getCheckInDate(),bookings.getCheckOutTime(),bookings.getCheckOutDate());

        int noOfRooms = bookings.getNoOfRooms();

        if(durationStay==0||noOfRooms==0){
            mRoomCharge.setText("( ₹"+df.format(sellRate)+"/per night)");
            if(noOfRooms==0){
                noOfRooms=1;
            }
        }else{
            int price = sellRate/(durationStay * noOfRooms);
            mRoomCharge.setText("( ₹"+df.format(price)+"/per night)");
        }

        mGstPercentage.setText("("+bookings.getGst()+"%)");
        mGstValue.setText("₹"+df.format(bookings.getGstAmount()));
        mGST.setText("(₹"+df.format(bookings.getGstAmount())+"×"+noOfRooms+"×"+durationStay+")");
        mOther.setText("₹"+df.format(bookings.getExtraCharges()));
        mTotalAmount.setText("₹"+df.format(bookings.getTotalAmount()));*/

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

    public int duration(String cit,String ciDate,String cot,String coDate){

        long diffDays,diffHours,diffMinutes,diffDays2,diffHours2,diffMinutes2;
        String duration = null,duration2 = null;
        String from = null,to = null;
        int durationStay=0;
//        System.out.println("Cit"+cit);
        //System.out.println("Cot"+cot);


        if(cit==null){
            cit = "12:00 PM";
        }
        if(cot==null){
            cot = "11:00 AM";
        }

        if(cit.indexOf(" ")!=-1)
        {
            String[] fArray = cit.split(" ");

            if(fArray[1].equalsIgnoreCase("am")){
                //from = ciDate+" "+fArray[0]+":00";
                String[] am = fArray[0].split(":");
                if(am[0].equalsIgnoreCase("12")){
                    from = ciDate+" "+"00:"+am[1]+":00";
                }else{
                    from = ciDate+" "+fArray[0]+":00";
                }
            }else if(fArray[1].equalsIgnoreCase("pm")){

                String[] pm = fArray[0].split(":");
                if(pm[0].equalsIgnoreCase("12")){

                    from = ciDate+" "+fArray[0]+":00";
                }else{
                    int value = Integer.parseInt(pm[0]);
                    from = ciDate+" "+(value+12)+":"+pm[1]+":00";
                }

            }

        }
        else
        {
            from = ciDate+" "+cit+":00";
        }

        if(cot.indexOf(" ")!=-1)
        {
            String[] fArray = cot.split(" ");

            if(fArray[1].equalsIgnoreCase("am")){

                String[] am = fArray[0].split(":");
                if(am[0].equalsIgnoreCase("12")){
                    to = coDate+" "+"00:"+am[1]+":00";
                }else{
                    to = coDate+" "+fArray[0]+":00";
                }
            }else if(fArray[1].equalsIgnoreCase("pm")){

                String[] pm = fArray[0].split(":");
                if(pm[0].equalsIgnoreCase("12")){

                    to = coDate+" "+fArray[0]+":00";
                }else{
                    int value = Integer.parseInt(pm[0]);
                    to = coDate+" "+(value+12)+":"+pm[1]+":00";
                }
            }

        }else{
            from = ciDate+" "+cit+":00";
            to = coDate+" "+cot+":00";
        }


        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {

            if(ciDate.contains("-"))
            {
                d1 = format2.parse(from);
                d2 = format2.parse(to);
            }
            else
            {
                d1 = format.parse(from);
                d2 = format.parse(to);
            }
            long diff = new Date().getTime() - d1.getTime();
            long diff2 = d2.getTime() - new Date().getTime();
            long diff3 = d2.getTime() - d1.getTime();

            //calculation for duration stay
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);
            duration =  diffDays+" Days "+diffHours+" Hours "+diffMinutes+" Minutes";

            mDurationOfStay.setText(duration);

            //calculation for duration to left
            diffMinutes2 = diff2 / (60 * 1000) % 60;
            diffHours2 = diff2 / (60 * 60 * 1000) % 24;
            diffDays2 = diff2 / (24 * 60 * 60 * 1000);
            duration2 =  diffDays2+" Days "+diffHours2+" Hours "+diffMinutes2+" Minutes";
            mDurationLeft.setText(duration2);

            long diffDay = diff3 / (24 * 60 * 60 * 1000);

            durationStay = (int)diffDay;
        }catch(Exception e){
            e.printStackTrace();
        }
        return durationStay;
    }

    private void sendcheckouteNotification(final HotelNotification notification) {

        final ProgressDialog dialog = new ProgressDialog(TripDetailsScreen.this);
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
                                Toast.makeText(TripDetailsScreen.this,"Checkout request sent to hotel",Toast.LENGTH_SHORT).show();

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


}
