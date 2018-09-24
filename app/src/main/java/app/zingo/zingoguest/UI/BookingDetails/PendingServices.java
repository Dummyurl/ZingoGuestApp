package app.zingo.zingoguest.UI.BookingDetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.zingoguest.Adapters.PendingServiceAdapter;
import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Model.Service;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.BookingsApi;
import app.zingo.zingoguest.WebAPI.RoomApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingServices extends AppCompatActivity {

    RecyclerView mPendingServiceList;
    TextView mRoomNum,mBookingId;
    ArrayList<Service> serviceList;

    int BookingId,hotelId;
    String message;
    Bookings bookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_pending_services);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            setTitle("Pending Service List");

            mPendingServiceList = (RecyclerView) findViewById(R.id.service_pending_list);
            mRoomNum = (TextView) findViewById(R.id.room_number);
            mBookingId = (TextView) findViewById(R.id.booking_id);



            BookingId = getIntent().getIntExtra("PendingServiceBookingId",0);
            message = getIntent().getStringExtra("Message");

            if(BookingId != 0&&message==null)
            {

                getBookings(BookingId);
            }else if(BookingId == 0 && message!=null){
                int bookingID = Integer.parseInt(message);
                System.out.println("Booking id=="+bookingID);
                getBookings(bookingID);
            }

            

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public  void getBookings(final int id)
    {
        BookingsApi api = Util.getClient().create(BookingsApi.class);
        Call<Bookings> getBooking = api.getBookingById(Constants.auth_string,id);

        getBooking.enqueue(new Callback<Bookings>() {
            @Override
            public void onResponse(Call<Bookings> call, Response<Bookings> response) {
                if(response.code() == 200)
                {
                    try{
                        bookings = response.body();
                        if(bookings != null)
                        {

                            mBookingId.setText("Booking Id: "+bookings.getBookingId());

                            hotelId = bookings.getHotelId();


                            serviceList = new ArrayList<>();
                            getRoomNo(bookings.getRoomId(),mRoomNum);

                            if(bookings.getServicesList().size()!=0){
                                for(int i =0;i<bookings.getServicesList().size();i++){
                                    if(bookings.getServicesList().get(i).getServiceStatus()!=null
                                            && bookings.getServicesList().get(i).getServiceStatus().equalsIgnoreCase("Pending")){
                                        serviceList.add(bookings.getServicesList().get(i));
                                    }
                                }

                                if(serviceList.size()!=0){
                                    PendingServiceAdapter adapter =
                                            new PendingServiceAdapter(PendingServices.this,serviceList,bookings.getTravellerId());
                                    mPendingServiceList.setAdapter(adapter);
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Bookings> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i)
        {
            case android.R.id.home:

               PendingServices.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void getRoomNo(final int i, final TextView t)
    {
        RoomApi api = Util.getClient().create(RoomApi.class);
        Call<Rooms> getRoom = api.getRoom(Constants.auth_string,i);

        getRoom.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                if(response.code() == 200)
                {
                    try{
                        if(response.body() != null)
                        {
                            t.setText("R No: "+response.body().getRoomNo());
                        }

                    }catch (Exception e){
                        e.printStackTrace();
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

}
