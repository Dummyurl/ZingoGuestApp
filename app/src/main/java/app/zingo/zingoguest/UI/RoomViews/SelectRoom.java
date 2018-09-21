package app.zingo.zingoguest.UI.RoomViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.TreeSet;

import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.HotelDetails;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Model.SelectingRoomModel;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectRoom extends AppCompatActivity {

    //View
    private ScrollView mScrollView;
    private Button mPreCheckInBtn;
    private LinearLayout mNoRoomsMatching;
    private TextView mSelectedFloorLevels,mPreBookHotelName,mPreHotelPlace;

    private ListView itemsList;

    private ProgressDialog progressDialog;
    private ArrayList<Rooms> rooms;

    public ArrayList<SelectingRoomModel> roomNumberWithFloorsWithFaciltyList;
    private ListView selectedItemsListView;


    private LinearLayout mSelectFloors;
    private RelativeLayout mSelectFeatures;
    private GridView roomsGridView;
    private TextView mSelectedRoomNumber,mNoOfRooms;
    String localty;

    //String selectedRoomNumber = "";
    public int count=0,totalAvailableRooms = 0,totalBookedRooms=0,expectedCheckIns=0,
            expectedCheckOuts=0,totalBlockedRooms=0;
    public String selecteditem = "",selectedRoomNumber = "",preBookingSelectedRoomNumber="";
    ArrayList<SelectingRoomModel> filteredRooms;

    //Intent
    Bookings bookings;
    String hotelName,hotelPlace;
    int NoOfRooms;


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
                if(HOTELID != 0)
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
                    notify.setHotelId(HOTELID);
                    System.out.println("roomids = "+roomids);
                    notify.setMessage(roomids+" "+bookingnumber);
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
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                TravellerApi travellerApi = Util.getClient().create(TravellerApi.class);
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
    private void selectRoomAdapter(ArrayList<Rooms> hotelRooms){

        roomNumberWithFloorsWithFaciltyList = new ArrayList<>();
        for(Rooms room:hotelRooms/*getResources().getStringArray(R.array.hotel_rooms_with_floors_features)*/)
        {
            String s = room.getDisplayName();
            roomNumberWithFloorsWithFaciltyList.add(new SelectingRoomModel(s,room));
        }

        floorsList = new ArrayList();


        featuresList = new ArrayList<>();


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
                //linearLayout.setBackgroundColor(Color.parseColor("#757575"));
            }
            else
            {
                preBookingRoomView.setImageResource(R.drawable.closed_door);
                //linearLayout.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            //System.out.println("room selected = "+rooms.get(position).getIsSelected());

            if(floorNo == null && floorsList.size() == 0)
            {

                floorsList.add(new DataModel(rooms.get(position).getRooms().getFloor(),false));
                floorNo = rooms.get(position).getRooms().getFloor();
            }
            else
            {
                if(floorNo != null && !floorNo.equals(rooms.get(position).getRooms().getFloor()) &&!floorsList.contains(new DataModel(rooms.get(position).getRooms().getFloor(),false)) )//!floorsList.contains(rooms.get(position).getRooms().getFloor()))
                {
                    floorsList.add(new DataModel(rooms.get(position).getRooms().getFloor(),false));
                    floorNo = rooms.get(position).getRooms().getFloor();
                    //System.out.println("Floor Num=="+floorNo);
                }
            }
            //System.out.println("Position = "+position);
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
}
