package app.zingo.zingoguest.UI.RoomViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.zingo.zingoguest.Adapters.ChangeRoomAdapter;
import app.zingo.zingoguest.CustomViews.RecyclerTouchListener;
import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.HotelNotification;
import app.zingo.zingoguest.Model.RoomAvailablity;
import app.zingo.zingoguest.Model.RoomCategories;
import app.zingo.zingoguest.Model.RoomResponse;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Model.SelectingRoomModel;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.HotelOperations;
import app.zingo.zingoguest.WebAPI.NotificationAPI;
import app.zingo.zingoguest.WebAPI.RoomApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpgradeRoom extends AppCompatActivity {

    //View
    private Button mPreCheckInBtn;
    private LinearLayout mNoRoomsMatching;
    private TextView mPreBookHotelName,mPreHotelPlace;
    private RecyclerView mChangeRoomRecyclerview;


    private ProgressDialog progressDialog;
    private ArrayList<RoomResponse> rooms;

    public ArrayList<SelectingRoomModel> roomNumberWithFloorsWithFaciltyList;


    private TextView mSelectedRoomNumber,mNoOfRooms;

    //String selectedRoomNumber = "";
    public int count=0;
    public String preBookingSelectedRoomNumber="";
    ArrayList<SelectingRoomModel> filteredRooms;
    ArrayList<String> categoryNames;
    ArrayList<ArrayList<SelectingRoomModel>> arrayListArrayList;

    //Intent
    Bookings bookings;
    String hotelName,hotelPlace;
    int NoOfRooms;
    Rooms room;

    ChangeRoomAdapter.ViewHolder viewHolder;
    String upgradeselectedRoomId="",upgradeRoomNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_upgrade_room);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Upgrade Room");

            //mSelectFloors = (LinearLayout)findViewById(R.id.pre_book_select_floors);
            //mSelectFeatures= (RelativeLayout) findViewById(R.id.pre_book_select_featurs);
            mSelectedRoomNumber = (TextView) findViewById(R.id.pre_book_selected_room_numbers);
            mNoOfRooms = (TextView) findViewById(R.id.pre_book_total_room_number);
            mPreCheckInBtn = (Button) findViewById(R.id.request_for_pre_booking_btn);
            mNoRoomsMatching = (LinearLayout) findViewById(R.id.no_room_layout);
            // mPreBookSelectedRooms = (TextView) findViewById(R.id.pre_book_selected_room_numbers);
            mPreBookHotelName = (TextView) findViewById(R.id.pre_book_hotel_name);
            mPreHotelPlace = (TextView) findViewById(R.id.pre_book_hotel_city);
            mChangeRoomRecyclerview = (RecyclerView) findViewById(R.id.change_room_recycler_view);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                bookings = (Bookings)bundle.getSerializable("Bookings");
                hotelName = bundle.getString("Hotel");
                hotelPlace = bundle.getString("Locality");
            }

            if(bookings!=null){
                NoOfRooms = bookings.getNoOfRooms();
                mNoOfRooms.setText(""+NoOfRooms);
                getRooms();
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

            mChangeRoomRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(UpgradeRoom.this, mChangeRoomRecyclerview, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, final int position) {

                    viewHolder  = (ChangeRoomAdapter.ViewHolder) mChangeRoomRecyclerview.findViewHolderForAdapterPosition(position);
                    viewHolder.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            ArrayList<SelectingRoomModel> list = arrayListArrayList.get(position);
                            SelectingRoomModel s = list.get(pos);
                        /*System.out.println("selected room = "+s.getSelectingRoom());*/
                            if(!s.getIsSelected())
                            {
                                count++;
                                LinearLayout linearLayout = (LinearLayout) viewHolder.gridview.getChildAt(pos);

                                LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(0);
                                ImageView imageView = (ImageView) linearLayout1.getChildAt(0);
                                imageView.setImageResource(R.drawable.opened_door);
                                s.setIsSelected(true);
                                upgradeselectedRoomId = upgradeselectedRoomId+s.getRooms().getRoomId()+",";
                                upgradeRoomNo = upgradeRoomNo+s.getRooms().getRoomNo()+",";
                                mSelectedRoomNumber.setText(""+upgradeRoomNo);

                            }
                            else
                            {
                                count--;
                                LinearLayout linearLayout = (LinearLayout) viewHolder.gridview.getChildAt(pos);
                                LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(0);
                                ImageView imageView = (ImageView) linearLayout1.getChildAt(0);
                                imageView.setImageResource(R.drawable.closed_door);
                                s.setIsSelected(false);
                                upgradeselectedRoomId = upgradeselectedRoomId.replace(s.getRooms().getRoomId()+",","");
                                upgradeRoomNo = upgradeRoomNo.replace(s.getRooms().getRoomNo()+",","");
                                mSelectedRoomNumber.setText(""+upgradeRoomNo);
                            }
                        }
                    });
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


            mPreCheckInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(NoOfRooms ==0)
                    {
                        if(count < 1)
                        {
                            Toast.makeText(UpgradeRoom.this,"Please select room",Toast.LENGTH_SHORT).show();
                        }
                        else if(count == 1)
                        {
                            showAlertBox(upgradeRoomNo);
                            //System.out.println("upgradeRoomNo = "+upgradeRoomNo);
                        }
                        else
                        {
                            Toast.makeText(UpgradeRoom.this,"Please select only one room",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if(NoOfRooms != count)
                        {
                            Toast.makeText(UpgradeRoom.this,"Please select only "+NoOfRooms+" room",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            showAlertBox(upgradeRoomNo);
                        }
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    public void showCheckInAlertBox1()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpgradeRoom.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpgradeRoom.this);
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
                //UpgradeRoom.this.finish();
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

        final ProgressDialog dialog = new ProgressDialog(UpgradeRoom.this);
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
                                Toast.makeText(UpgradeRoom.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();
                                UpgradeRoom.this.finish();
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

    private void getRooms(){
        progressDialog = new ProgressDialog(UpgradeRoom.this);
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
                            ArrayList<RoomResponse> availabe = new ArrayList<>();


                            if(list != null && list.size() != 0)
                            {
                                rooms = list;
                                Collections.sort(rooms,new UpgradeRoom.SortRooms());
                                categoryNames = new ArrayList<>();

                                for (int i=0;i<rooms.size();i++)
                                {
                                    //System.out.println(availabe.get(i).getRoomCategoryId());
                                    if(!categoryNames.contains(rooms.get(i).getCategoryName()))
                                    {
                                        categoryNames.add(rooms.get(i).getCategoryName());
                                    }
                                }


                                arrayListArrayList = new ArrayList<>();
                                for (int i = 0;i<categoryNames.size();i++)
                                {
                                    ArrayList<SelectingRoomModel> roomsArrayList = new ArrayList<>();
                                    for (int j=0;j<rooms.size();j++)
                                    {
                                        if(categoryNames.get(i).equalsIgnoreCase(rooms.get(j).getCategoryName()))
                                        {
                                            roomsArrayList.add(new SelectingRoomModel(rooms.get(j).getRoomNo(),rooms.get(j)));
                                        }

                                    }

                                    arrayListArrayList.add(roomsArrayList);
                                }

                                if(arrayListArrayList!=null&&arrayListArrayList.size()!=0){

                                    ChangeRoomAdapter adapter = new ChangeRoomAdapter(UpgradeRoom.this,arrayListArrayList,categoryNames);
                                    mChangeRoomRecyclerview.setAdapter(adapter);


                                }else{

                                    mNoRoomsMatching.setVisibility(View.VISIBLE);
                                }




                            }else {
                                mNoRoomsMatching.setVisibility(View.VISIBLE);

                            }



                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(UpgradeRoom.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
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




    //gridview adapter

    class UpgradeRoomGridViewAdapter extends BaseAdapter {

        Context context;
        ArrayList<SelectingRoomModel> rooms;
        String floorNo = null;

        boolean roomSelected = true;
        public UpgradeRoomGridViewAdapter(Context context, ArrayList<SelectingRoomModel> rooms)
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





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                UpgradeRoom.this.finish();
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

    public void categoryList(final ArrayList<Rooms> roomList)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final RoomApi getRoomCategories = Util.getClient().create(RoomApi.class);
                Call<ArrayList<RoomCategories>> getRoomCategoriesResponse = getRoomCategories.
                        fetchRoomCategoriesByHotelId(Constants.auth_string, bookings.getHotelId());


                getRoomCategoriesResponse.enqueue(new Callback<ArrayList<RoomCategories>>() {
                    @Override
                    public void onResponse(Call<ArrayList<RoomCategories>> call, Response<ArrayList<RoomCategories>> response) {

                        try
                        {
                           /* if(dialog != null)
                            {
                                dialog.dismiss();
                            }*/
                            if(response.code() == 200)
                            {
                                if(response.body() != null && response.body().size() != 0)
                                {

                                }


                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<RoomCategories>> call, Throwable t) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        Toast.makeText(UpgradeRoom.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    private void showAlertBox(final String pos) {
        //final Rooms room = rooms.get(pos);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpgradeRoom.this);
        builder.setTitle("Do you want to Change the Room?");
        //builder.setCancelable(false);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //send request function here for notification after success full response show next alert box
                showAlertBoxConfirm(pos);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showAlertBoxConfirm(String roomNum)
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UpgradeRoom.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = (View) inflater.inflate(R.layout.pre_checkin_alert_box2,null);
        TextView selectedRoomsAlertTextView = (TextView) v.findViewById(R.id.pre_chckin_alert_message_with_room_number);
        selectedRoomsAlertTextView.setText(getResources().getString(R.string.pre_check_in_confirm_sub_title)+" "+roomNum);
        TextView changeConfirm = (TextView) v.findViewById(R.id.confirm_note);
        changeConfirm.setText(getResources().getString(R.string.change_room));
        builder.setView(v);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //System.out.println("selected rooms = "+ mPreBookSelectedRooms.getText().toString());

                //ChangeRoomActivity.this.finish();
                if(NoOfRooms != 0)
                {
                    HotelNotification notify = new HotelNotification();
                    notify.setHotelId(bookings.getHotelId());
                    //System.out.println("roomids = "+roomids);
                    notify.setMessage(upgradeselectedRoomId+" "+bookings.getBookingNumber()+" "+bookings.getBookingId());
                    notify.setTitle("Select Room Upgrade Request");
                    notify.setSenderId(Constants.senderId);
                    notify.setServerId(Constants.serverId);
                    sendfirebaseNotification(notify);
                }
                else
                {
                    HotelNotification notify = new HotelNotification();
                    notify.setHotelId(bookings.getHotelId());
                    //System.out.println("roomids = "+roomids);
                    notify.setMessage(upgradeselectedRoomId+" "+bookings.getBookingNumber()+" "+bookings.getBookingId());
                    notify.setTitle("Change Room Request");
                    notify.setSenderId(Constants.senderId);
                    notify.setServerId(Constants.serverId);
                    sendfirebaseNotification(notify);
                }

                dialog.dismiss();
            }
        });


        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}
