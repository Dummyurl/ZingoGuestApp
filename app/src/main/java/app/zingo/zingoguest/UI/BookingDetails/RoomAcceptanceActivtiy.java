package app.zingo.zingoguest.UI.BookingDetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.HotelNotification;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.UI.LandingScreens.WelcomeScreen;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.NotificationAPI;
import app.zingo.zingoguest.WebAPI.RoomApi;
import app.zingo.zingoguest.WebAPI.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomAcceptanceActivtiy extends AppCompatActivity {

    TextView title,mSubTitle,mSuggestedRooms;
    GridView roomsgrid;
    LinearLayout mRoomSuggestParent;
    Button mAccept,mReject;


    String reponseString,responseMsg;
    String[] rooms;
    String suggestedRoomNumbers = "";
    String[] roomsAndBookingNumber;
    int HOTELID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_room_acceptance_activtiy);

            title = (TextView)findViewById(R.id.response_title);
            mSubTitle = (TextView)findViewById(R.id.response_sub_title);
            mSuggestedRooms = (TextView)findViewById(R.id.suggested_room_numbers);
            mRoomSuggestParent = (LinearLayout)findViewById(R.id.room_sugestion_parent_layout);
            mAccept = (Button)findViewById(R.id.room_accept_btn);
            mReject = (Button)findViewById(R.id.room_reject_btn);
            //roomsgrid = (GridView)findViewById(R.id.suggested_rooms);

            reponseString = getIntent().getStringExtra("Title");
            responseMsg = getIntent().getStringExtra("Message");


            if(reponseString != null && reponseString.equalsIgnoreCase("Room Rejected"))
            {
                title.setText(reponseString);
                mSubTitle.setText("Sorry, Your request is rejected. Please do pre checkin with other room(s)");
                setTitle("Rejection");
                mRoomSuggestParent.setVisibility(View.GONE);
            }
            else  if(reponseString != null && reponseString.equalsIgnoreCase("Room Upgrade Rejected")) {
                title.setText(reponseString);
                mSubTitle.setText("Sorry, Your request for room upgade is rejected.");
                setTitle("Upgrade Rejection");
                mRoomSuggestParent.setVisibility(View.GONE);
            }
            else if(reponseString != null && reponseString.equalsIgnoreCase("Room Upgrade Suggested"))
            {
                setTitle("Room Upgrade Suggestion");
                mSubTitle.setText("Hotelier has suggested some room(s). Please go through it revert back.");
                title.setText(reponseString);
                mRoomSuggestParent.setVisibility(View.VISIBLE);
                try
                {
                    if(responseMsg != null && !responseMsg.isEmpty())
                    {
                        //System.out.println("responseMsg = "+responseMsg);
                        roomsAndBookingNumber = responseMsg.split(" ");
                        if(roomsAndBookingNumber != null && roomsAndBookingNumber.length > 0)
                        {

                            if(roomsAndBookingNumber[3] != null && !roomsAndBookingNumber[3].isEmpty())
                            {
                                HOTELID = Integer.parseInt(roomsAndBookingNumber[3]);
                                System.out.println("roomsAndBookingNumber[2] = "+HOTELID);
                            }
                            System.out.println(roomsAndBookingNumber[0]);
                            rooms = roomsAndBookingNumber[0].split(",");

                            System.out.println(rooms.length);
                            for (int i=0;i<rooms.length;i++)
                            {
                                if(rooms[i] != null && !rooms[i].isEmpty())
                                {
                                    getRoomDetails(Integer.parseInt(rooms[i]));
                                }
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }



            }
            else if(reponseString != null && reponseString.equalsIgnoreCase("Room Suggested"))
            {
                setTitle("Suggestion");
                mSubTitle.setText("Hotelier has suggested some room(s). Please go through it revert back.");
                title.setText(reponseString);
                mRoomSuggestParent.setVisibility(View.VISIBLE);
                try
                {
                    if(responseMsg != null && !responseMsg.isEmpty())
                    {
                        //System.out.println("responseMsg = "+responseMsg);
                        roomsAndBookingNumber = responseMsg.split(" ");
                        if(roomsAndBookingNumber != null && roomsAndBookingNumber.length > 0)
                        {

                            if(roomsAndBookingNumber[2] != null && !roomsAndBookingNumber[2].isEmpty())
                            {
                                HOTELID = Integer.parseInt(roomsAndBookingNumber[2]);
                                System.out.println("roomsAndBookingNumber[2] = "+HOTELID);
                            }
                            rooms = roomsAndBookingNumber[0].split(",");

                            //System.out.println(rooms.length);
                            if(rooms != null && rooms.length != 0)
                            {
                                for (int i=0;i<rooms.length;i++)
                                {
                                    if(rooms[i] != null && !rooms[i].isEmpty())
                                    {
                                        getRoomDetails(Integer.parseInt(rooms[i]));
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }



            }
            else if(reponseString.equalsIgnoreCase("Checkout Rejected"))
            {

            }
        /*else if(reponseString != null && reponseString.equalsIgnoreCase("Room Acceptance"))
        {
            title.setText(reponseString);
        }*/

            mAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(reponseString != null && reponseString.equalsIgnoreCase("Room Upgrade Suggested"))
                    {
                        HotelNotification notify = new HotelNotification();
                        notify.setHotelId(HOTELID);
                        //System.out.println("roomids = "+roomids);
                        notify.setMessage(responseMsg);
                        notify.setTitle("Room Upgrade Suggestion Accepted");
                        notify.setSenderId(Constants.senderId);
                        notify.setServerId(Constants.serverId);
                        sendfirebaseNotificationAfterAccept(notify);
                    }
                    else
                    {
                        HotelNotification notify = new HotelNotification();
                        notify.setHotelId(HOTELID);
                        //System.out.println("roomids = "+roomids);
                        notify.setMessage(responseMsg);
                        notify.setTitle("Room Request");
                        notify.setSenderId(Constants.senderId);
                        notify.setServerId(Constants.serverId);
                        sendfirebaseNotificationAfterAccept(notify);
                    }

                }
            });

            mReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HotelNotification notify = new HotelNotification();
                    notify.setHotelId(HOTELID);
                    //System.out.println("roomids = "+roomids);
                    notify.setMessage(responseMsg);
                    notify.setTitle("Room Rejected");
                    notify.setSenderId(Constants.senderId);
                    notify.setServerId(Constants.serverId);
                    sendfirebaseNotificationAfterReject(notify);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getRoomDetails(final int i)
    {
        //String auth_string = Util.getToken(RoomAcceptanceActivtiy.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        RoomApi api = Util.getClient().create(RoomApi.class);
        Call<Rooms> getRoom = api.getRoom(Constants.auth_string,i);

        getRoom.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {


                if(response.code() == 200)
                {
                    if(response.body() != null)
                    {

                        //rooms.add(response.body());
                        //System.suggestedRoomNumbersout.println("Size=="+rooms.size());
                        suggestedRoomNumbers =  suggestedRoomNumbers+response.body().getRoomNo() + ",";
                        mSuggestedRooms.setText(suggestedRoomNumbers);
                        System.out.println(suggestedRoomNumbers);
                        /*mSelectedRoomNumber.setText(preBookingSelectedRoomNumber);
                        SelectingRoomModel selectingRoomModel = new SelectingRoomModel(response.body().getDisplayName(),response.body());
                        selectingRoomModel.setIsSelected(true);
                        roomNumberWithFloorsWithFaciltyList.add(selectingRoomModel);
                        System.out.println("Size=="+roomNumberWithFloorsWithFaciltyList.size());*/

                    }



                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void sendfirebaseNotificationAfterAccept(final HotelNotification notification) {

        final ProgressDialog dialog = new ProgressDialog(RoomAcceptanceActivtiy.this);
        dialog.setMessage("Loading...");
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
                                Toast.makeText(RoomAcceptanceActivtiy.this,"Thank you for accepting request. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();
                                intent();
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

    private void sendfirebaseNotificationAfterReject(final HotelNotification notification) {

        final ProgressDialog dialog = new ProgressDialog(RoomAcceptanceActivtiy.this);
        dialog.setMessage("Loading..");
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
                                Toast.makeText(RoomAcceptanceActivtiy.this,"Thank you for accepting request. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();
                                intent();
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

    public void intent()
    {
        Intent mainActivityIntent = new Intent(RoomAcceptanceActivtiy.this, WelcomeScreen.class);
//        mainActivityIntent.putExtra("BookingID",bookingId);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(mainActivityIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.notification_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.close_activity:
                intent();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
