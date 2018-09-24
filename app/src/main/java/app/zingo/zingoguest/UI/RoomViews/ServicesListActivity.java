package app.zingo.zingoguest.UI.RoomViews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.zingoguest.Adapters.ServiceTakenAdapter;
import app.zingo.zingoguest.Model.HotelNotification;
import app.zingo.zingoguest.Model.Service;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.UI.LandingScreens.WelcomeScreen;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.NotificationAPI;
import app.zingo.zingoguest.WebAPI.ServiceApi;
import app.zingo.zingoguest.WebAPI.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesListActivity extends AppCompatActivity {


    RecyclerView mServiceRecyclerview;
    TextView mTotalServiceAmount;
    Button mSendRequestBtn;

    ArrayList<Service> list;
    String TAG = "ServicesListActivity";
    int BookingHotelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_services_list);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mServiceRecyclerview = (RecyclerView) findViewById(R.id.taken_services_list);
            mServiceRecyclerview.setLayoutManager(new LinearLayoutManager(ServicesListActivity.this));
            mServiceRecyclerview.setNestedScrollingEnabled(false);

            mTotalServiceAmount = (TextView) findViewById(R.id.services_total_amount);
            mSendRequestBtn = (Button) findViewById(R.id.send_servie_request_btn);

            Bundle bundle= getIntent().getExtras();

            setTitle("Services Taken");
            if(bundle != null)
            {
                list = (ArrayList<Service>) bundle.getSerializable("totalservices");
                BookingHotelId = getIntent().getIntExtra("BookingHotelId",0);

                if(list != null && list.size() != 0)
                {
                    ServiceTakenAdapter adapter = new ServiceTakenAdapter(ServicesListActivity.this,list);
                    mServiceRecyclerview.setAdapter(adapter);
                    int total = 0;
                    for (int i=0;i<list.size();i++)
                    {
                        total = total+list.get(i).getAmount();
                        //System.out.println(list.get(i).getBookingId()+" = "+list.get(i).getBookingNumber());
                    }

                    mTotalServiceAmount.setText("₹ "+total);
                }

            }


            mSendRequestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(list != null && list.size() != 0 && BookingHotelId != 0) {
                        addServices(list);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendfirebaseNotification(final HotelNotification notification) {

        final ProgressDialog dialog = new ProgressDialog(ServicesListActivity.this);
        dialog.setMessage("Sending Request");
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

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {
                                /*Toast.makeText(ServicesListActivity.this,"Thank you for upgrading/changing room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                Toast.makeText(ServicesListActivity.this,"Your request for services sent successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ServicesListActivity.this,WelcomeScreen.class);
                                intent.putExtra("BookingID",list.get(0).getBookingId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                startActivity(intent);
                                ServicesListActivity.this.finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void addServices(final ArrayList<Service> serviceArrayList)
    {
        final ProgressDialog dialog = new ProgressDialog(ServicesListActivity.this);
        dialog.setMessage("Sending Request");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ServiceApi api = Util.getClient().create(ServiceApi.class);
                Call<ArrayList<Service>> response = api.addMultipleServices(Constants.auth_string,serviceArrayList);

                response.enqueue(new Callback<ArrayList<Service>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {

                                HotelNotification notify = new HotelNotification();
                                notify.setHotelId(BookingHotelId);
                                //System.out.println("roomids = "+roomids);
                                notify.setMessage(serviceArrayList.get(0).getBookingId()+"");
                                notify.setTitle("Room Service Request");
                                notify.setSenderId(Constants.senderId);
                                notify.setServerId(Constants.serverId);
                                sendfirebaseNotification(notify);
                            }
                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
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
                ServicesListActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
