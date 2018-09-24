package app.zingo.zingoguest.UI.BookingDetails;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.zingoguest.Adapters.ActiveServiceListAdapter;
import app.zingo.zingoguest.CustomViews.RecyclerTouchListener;
import app.zingo.zingoguest.Model.BookingAndTraveller;
import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.Traveller;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.BookingsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomServicePendingListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView mNoServices;
    ArrayList<Traveller> travellerArrayList;
    ArrayList<Bookings> bookings1ArrayList;
    ArrayList<Bookings> bookingAndTravellerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_room_service_pending_list);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            setTitle("Pending Services Booing");

            recyclerView = (RecyclerView) findViewById(R.id.active_booking_service_list);
            mNoServices = (TextView) findViewById(R.id.no_services);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(RoomServicePendingListActivity.this,recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, final int position) {

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            activeService();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void activeService() {

        final ProgressDialog dialog = new ProgressDialog(RoomServicePendingListActivity.this);
        dialog.setTitle("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingsApi bookingApi = Util.getClient().create(BookingsApi.class);
                Call<ArrayList<Bookings>> getAllBookings = bookingApi.
                        getBookingsByTravellerIdStatus(Constants.auth_string,
                                PreferenceHandler.getInstance(RoomServicePendingListActivity.this).getUserId(),"Active");

                getAllBookings.enqueue(new Callback<ArrayList<Bookings>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings>> call, Response<ArrayList<Bookings>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }


                        if(response.code() == 200 && response.body()!= null)
                        {

                            try{
                                if(response.body().size() != 0)
                                {
                                    bookingAndTravellerArrayList = response.body();
                                    travellerArrayList = new ArrayList<>();
                                    bookings1ArrayList = new ArrayList<>();
                                    for(int i=0;i<response.body().size();i++)
                                    {
                                        if(bookingAndTravellerArrayList.get(i).getServicesList().size()!=0){
                                            for(int j=0;j<bookingAndTravellerArrayList.get(i).getServicesList().size();j++){
                                                if(bookingAndTravellerArrayList.get(i).getServicesList().get(j).getServiceStatus()!=null
                                                        && bookingAndTravellerArrayList.get(i).getServicesList().get(j)
                                                        .getServiceStatus().equalsIgnoreCase("Pending")){
                                                    bookings1ArrayList.add(response.body().get(i));
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                    if(bookings1ArrayList.size()!=0){
                                        ActiveServiceListAdapter bookingRecyclerViewAdapter =
                                                new ActiveServiceListAdapter(RoomServicePendingListActivity.this,bookings1ArrayList);
                                        recyclerView.setAdapter(bookingRecyclerViewAdapter);
                                        mNoServices.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(RoomServicePendingListActivity.this, "There is no pending Services for any booking", Toast.LENGTH_SHORT).show();
                                        mNoServices.setVisibility(View.VISIBLE);

                                    }


                                }
                                else
                                {
                                    Toast.makeText(RoomServicePendingListActivity.this,"No Bookings Found",Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Toast.makeText(RoomServicePendingListActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings>> call, Throwable t) {
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
        int i = item.getItemId();
        switch (i)
        {
            case android.R.id.home:
                RoomServicePendingListActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
