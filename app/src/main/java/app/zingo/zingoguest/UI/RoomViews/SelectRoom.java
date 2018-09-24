package app.zingo.zingoguest.UI.RoomViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.HotelDetails;
import app.zingo.zingoguest.Model.HotelNotification;
import app.zingo.zingoguest.Model.RoomAvailablity;
import app.zingo.zingoguest.Model.RoomResponse;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Model.SelectingRoomModel;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.HotelOperations;
import app.zingo.zingoguest.WebAPI.NotificationAPI;
import app.zingo.zingoguest.WebAPI.RoomApi;
import app.zingo.zingoguest.WebAPI.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectRoom extends AppCompatActivity {

    //View
    private Button mPreCheckInBtn;
    private LinearLayout mNoRoomsMatching;
    private TextView mPreBookHotelName,mPreHotelPlace;


    private ProgressDialog progressDialog;
    private ArrayList<RoomResponse> rooms;

    public ArrayList<SelectingRoomModel> roomNumberWithFloorsWithFaciltyList;


    private GridView roomsGridView;
    private TextView mSelectedRoomNumber,mNoOfRooms;

    //String selectedRoomNumber = "";
    public int count=0;
    public String preBookingSelectedRoomNumber="";
    ArrayList<SelectingRoomModel> filteredRooms;

    //Intent
    Bookings bookings;
    String hotelName,hotelPlace;
    int NoOfRooms;
    Rooms room;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_select_room);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Select Room");

            //mSelectFloors = (LinearLayout)findViewById(R.id.pre_book_select_floors);
            //mSelectFeatures= (RelativeLayout) findViewById(R.id.pre_book_select_featurs);
            roomsGridView = (GridView) findViewById(R.id.pre_booking_room_grid_view);
            mSelectedRoomNumber = (TextView) findViewById(R.id.pre_book_selected_room_numbers);
            mNoOfRooms = (TextView) findViewById(R.id.pre_book_total_room_number);
            mPreCheckInBtn = (Button) findViewById(R.id.request_for_pre_booking_btn);
            mNoRoomsMatching = (LinearLayout) findViewById(R.id.no_room_layout);
            // mPreBookSelectedRooms = (TextView) findViewById(R.id.pre_book_selected_room_numbers);
            mPreBookHotelName = (TextView) findViewById(R.id.pre_book_hotel_name);
            mPreHotelPlace = (TextView) findViewById(R.id.pre_book_hotel_city);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                bookings = (Bookings)bundle.getSerializable("Bookings");
                hotelName = bundle.getString("Hotel");
                hotelPlace = bundle.getString("Locality");
            }

            if(bookings!=null){
               NoOfRooms = bookings.getNoOfRooms();
               mNoOfRooms.setText(""+NoOfRooms);
                getRooms(bookings.getHotelId());
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


            roomsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(NoOfRooms != 0)
                    {
                        if(filteredRooms != null && !filteredRooms.isEmpty())
                        {
                           setImage(filteredRooms,position);
                        }
                        else
                        {
                           setImage(roomNumberWithFloorsWithFaciltyList,position);
                        }
                    }
                }
            });

            mPreCheckInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(count == NoOfRooms)
                    {
                        showCheckInAlertBox1();
                    }
                    else if(NoOfRooms < count)
                    {
                        Toast.makeText(SelectRoom.this,"Sorry. You have selected more rooms",Toast.LENGTH_SHORT).show();
                    }
                    else if(count < NoOfRooms)
                    {
                        Toast.makeText(SelectRoom.this,"Sorry. You need to select "+(NoOfRooms-count)+" more rooms",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    public void showCheckInAlertBox1()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectRoom.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = (View) inflater.inflate(R.layout.pre_checkin_alert_box1,null);
        builder.setView(v);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showCheckInAlertBox2();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showCheckInAlertBox2()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectRoom.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = (View) inflater.inflate(R.layout.pre_checkin_alert_box2,null);
        TextView selectedRoomsAlertTextView = (TextView) v.findViewById(R.id.pre_chckin_alert_message_with_room_number);
        selectedRoomsAlertTextView.setText(getResources().getString(R.string.pre_check_in_confirm_sub_title)+" "+mSelectedRoomNumber.getText().toString());
        builder.setView(v);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //System.out.println("selected rooms = "+ mPreBookSelectedRooms.getText().toString());
                //SelectRoom.this.finish();
                dialog.dismiss();
                if(bookings.getHotelId() != 0)
                {

                    String roomids = "" ;
                    if(roomNumberWithFloorsWithFaciltyList != null && roomNumberWithFloorsWithFaciltyList.size() > 0)
                    {
                        for(int i=0;i<roomNumberWithFloorsWithFaciltyList.size();i++)
                        {
                            if(roomNumberWithFloorsWithFaciltyList.get(i).getIsSelected())
                            {
                                roomids = roomids+roomNumberWithFloorsWithFaciltyList.get(i).getRooms().getRoomId()+",";
                            }
                        }
                    }
                    else if(filteredRooms != null && filteredRooms.size() > 0)
                    {
                        for(int i=0;i<filteredRooms.size();i++)
                        {
                            if(filteredRooms.get(i).getIsSelected())
                            {
                                roomids = filteredRooms.get(i).getRooms().getRoomId()+",";
                            }
                        }
                    }
                    HotelNotification notify = new HotelNotification();
                    notify.setHotelId(bookings.getHotelId());
                    notify.setMessage(roomids+" "+bookings.getBookingNumber());
                    notify.setTitle("Room Request");
                    notify.setSenderId(Constants.senderId);
                    notify.setServerId(Constants.serverId);
                    sendfirebaseNotification(notify);

                }
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendfirebaseNotification(final HotelNotification notification) {

        final ProgressDialog dialog = new ProgressDialog(SelectRoom.this);
        dialog.setMessage("Sending Reques");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
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
                                Toast.makeText(SelectRoom.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();
                                SelectRoom.this.finish();
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

    private void getRooms(final int hotelid){
        progressDialog = new ProgressDialog(SelectRoom.this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final RoomAvailablity roomAvailablity = new RoomAvailablity();
        roomAvailablity.setHotelId(bookings.getHotelId());
        roomAvailablity.setFromDate(bookings.getCheckInDate());
        roomAvailablity.setToDate(bookings.getCheckOutDate());

        new ThreadExecuter().execute(new Runnable() {

            @Override
            public void run() {

                HotelOperations apiService =
                        Util.getClient().create(HotelOperations.class);

                Call<ArrayList<RoomResponse>> call = apiService.getRoomsStatusByHotelId(Constants.auth_string,roomAvailablity)/*getRooms()*/;

                call.enqueue(new Callback<ArrayList<RoomResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<RoomResponse>> call, Response<ArrayList<RoomResponse>> response) {

                        int statusCode = response.code();
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        if (statusCode == 200) {

                            ArrayList<RoomResponse> list =  response.body();
                            ArrayList<Rooms> availabe = new ArrayList<>();


                            if(list != null && list.size() != 0)
                            {
                                rooms =  list;


                                    Collections.sort(rooms,new SortRooms());

                                    selectRoomAdapter(rooms);



                            }



                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(SelectRoom.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<RoomResponse>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });


    }

    class SortRooms implements Comparator<RoomResponse>
    {

        @Override
        public int compare(RoomResponse o1, RoomResponse o2) {
            if(o1 != null && o2 != null)
            {
                return (o1.getRoomNo().compareTo(o2.getRoomNo()));
            }
            else
            {
                return -1;
            }
        }
    }
    private void selectRoomAdapter(ArrayList<RoomResponse> hotelRooms){

        roomNumberWithFloorsWithFaciltyList = new ArrayList<>();
        for(RoomResponse room:hotelRooms/*getResources().getStringArray(R.array.hotel_rooms_with_floors_features)*/)
        {
            String s = room.getRoomNo();
            roomNumberWithFloorsWithFaciltyList.add(new SelectingRoomModel(s,room));
        }


        setGridViewAdapter(roomNumberWithFloorsWithFaciltyList);
    }

    private void setGridViewAdapter(ArrayList<SelectingRoomModel> gridViewData)
    {

        if(gridViewData != null && gridViewData.size() != 0)
        {
            mNoRoomsMatching.setVisibility(View.GONE);
            roomsGridView.setVisibility(View.VISIBLE);
            SelectRoomGridViewAdapter adapter = new SelectRoomGridViewAdapter(SelectRoom.this,gridViewData);
            roomsGridView.setAdapter(adapter);
        }
        else
        {
            mNoRoomsMatching.setVisibility(View.VISIBLE);
            roomsGridView.setVisibility(View.GONE);
        }
    }

    //gridview adapter

    class SelectRoomGridViewAdapter extends BaseAdapter {

        Context context;
        ArrayList<SelectingRoomModel> rooms;
        String floorNo = null;

        boolean roomSelected = true;
        public SelectRoomGridViewAdapter(Context context, ArrayList<SelectingRoomModel> rooms)
        {
            this.context = context;
            this.rooms = rooms;
        }
        @Override
        public int getCount() {


            return rooms.size();

        }

        @Override
        public Object getItem(int position) {
            return rooms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.pre_booking_gridview_layout,parent,false);
            }

            final ImageView preBookingRoomView = (ImageView) convertView.findViewById(R.id.pre_booking_selected_room);
            TextView preBookingRoomNumber = (TextView) convertView.findViewById(R.id.pre_booking_selected_room_number);
            LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.room_layout);

            System.out.println("room selected = "+rooms.get(position).getIsSelected());
            if(rooms.get(position).getIsSelected())
            {
                preBookingRoomView.setImageResource(R.drawable.opened_door);
            }
            else
            {
                preBookingRoomView.setImageResource(R.drawable.closed_door);
            }

            if(position == (rooms.size() - 1))
            {
                floorNo = null;
            }


            preBookingRoomNumber.setText(rooms.get(position).getSelectingRoom().split("-")[0]);

            return convertView;
        }
    }


    public void setImage(ArrayList<SelectingRoomModel> data, int position)
    {

        String[] splitedString = data.get(position).getSelectingRoom().split("-");
        String splitedCombinedString = splitedString[0];
        boolean b = data.get(position).getIsSelected();
        if (!data.get(position).getIsSelected()) {

            count++;
            SelectingRoomModel model = data.get(position);
            LinearLayout linearLayout = (LinearLayout) roomsGridView.getChildAt(position);
            LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(0);
            ImageView imageView = (ImageView) linearLayout1.getChildAt(0);

            for (int i = 0; i < roomNumberWithFloorsWithFaciltyList.size(); i++) {

                if (roomNumberWithFloorsWithFaciltyList.get(i).getSelectingRoom().contains(splitedCombinedString) &&
                        !roomNumberWithFloorsWithFaciltyList.get(i).getIsSelected()) {
                    roomNumberWithFloorsWithFaciltyList.get(i).isSelected = true;

                    preBookingSelectedRoomNumber = preBookingSelectedRoomNumber + splitedString[0] + ",";
                    mSelectedRoomNumber.setText(preBookingSelectedRoomNumber);
                    imageView.setImageResource(R.drawable.opened_door);

                } else {
//
                }
            }


        } else {
            count--;
            LinearLayout linearLayout = (LinearLayout) roomsGridView.getChildAt(position);
            LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(0);
            ImageView imageView = (ImageView) linearLayout1.getChildAt(0);
            imageView.setImageResource(R.drawable.closed_door);

            for (int i = 0; i < roomNumberWithFloorsWithFaciltyList.size(); i++) {
                if (roomNumberWithFloorsWithFaciltyList.get(i).getSelectingRoom().contains(splitedCombinedString)) {
                    roomNumberWithFloorsWithFaciltyList.get(i).setIsSelected(false);
                }
            }

            preBookingSelectedRoomNumber = preBookingSelectedRoomNumber.replace(splitedString[0] + ",", "");
            mSelectedRoomNumber.setText(preBookingSelectedRoomNumber);

        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
               SelectRoom.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void getRoomNo(final int i)
    {
        room = new Rooms();
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
                    }

                }
                else
                {
                    System.out.println(response.message());
                    room = null;
                }
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });


    }
}
