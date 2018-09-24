package app.zingo.zingoguest.UI.BookingDetails;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.zingoguest.Adapters.RoomServiceAdapters;
import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.Service;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.BookingsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomServiceAcceptanceReportActivity extends AppCompatActivity {

    RecyclerView mList;


    String responseMsg;
    int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_room_service_acceptance_report);

            mList = (RecyclerView)findViewById(R.id.accepted_service_list);
            mList.setLayoutManager(new LinearLayoutManager(RoomServiceAcceptanceReportActivity.this));

            responseMsg = getIntent().getStringExtra("Message");
            setTitle("Service Response");

            if(responseMsg != null && !responseMsg.isEmpty())
            {
                bookingId = Integer.parseInt(responseMsg.trim());

                if(bookingId != 0)
                {
                    getServicesBasedOnBooking(bookingId);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getServicesBasedOnBooking(final int i) {
        try
        {
            final ProgressDialog dialog = new ProgressDialog(RoomServiceAcceptanceReportActivity.this);
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.show();

            new ThreadExecuter().execute(new Runnable() {
                @Override
                public void run() {
                    BookingsApi bookingsApi = Util.getClient().create(BookingsApi.class);
                    Call<Bookings> response = bookingsApi.getBookingById(Constants.auth_string,i);

                    response.enqueue(new Callback<Bookings>() {
                        @Override
                        public void onResponse(Call<Bookings> call, Response<Bookings> response) {
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            if(response.code() == 200)
                            {
                                if(response.body() != null)
                                {
                                    ArrayList<Service> serviceArrayList = response.body().getServicesList();
                                    if(serviceArrayList != null && serviceArrayList.size() != 0)
                                    {
                                        RoomServiceAdapters adapters = new RoomServiceAdapters(RoomServiceAcceptanceReportActivity.this,
                                                serviceArrayList);
                                        mList.setAdapter(adapters);
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(RoomServiceAcceptanceReportActivity.this,"Please check your data connection",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Bookings> call, Throwable t) {
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            Log.e("TAG",t.getMessage());
                        }
                    });
                }
            });
        }
        catch (Exception ex)
        {

        }
    }
}
