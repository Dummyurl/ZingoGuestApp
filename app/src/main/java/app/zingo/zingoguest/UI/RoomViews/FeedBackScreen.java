package app.zingo.zingoguest.UI.RoomViews;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.zingoguest.Model.FeedBack;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.BookingsApi;
import app.zingo.zingoguest.WebAPI.FeedbackApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackScreen extends AppCompatActivity {

    RatingBar ratingBar;
    EditText mCustomerFeedBack;
    TextView mCustomerFeedbackButton;


    private int bookingId,HotelID;
    private String pending,BookingNumber;
    FeedBack feedBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_feed_back_screen);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Customer Feedback");

            ratingBar=(RatingBar)findViewById(R.id.customer_rating_bar);
            mCustomerFeedBack = (EditText) findViewById(R.id.customer_comments);
            mCustomerFeedbackButton=(TextView)findViewById(R.id.submit_customer_feedback);

            Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                bookingId = bundle.getInt("BookingBookingId");
                HotelID = bundle.getInt("BookingIdCustomerFeedBack");
                BookingNumber = bundle.getString("BookingNumber");
                //pending = bundle.getString(Constants.Pending);
                if(bookingId != 0)
                {
                    getFeedBack(bookingId);
                }
                System.out.println("bookingId = "+bookingId +" = "+HotelID+" = "+BookingNumber);

            }

            mCustomerFeedbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.hideKeyboard(FeedBackScreen.this);
                    String rating = String.valueOf(ratingBar.getRating());
                    System.out.println("Rating == "+rating);
                    if(ratingBar.getRating() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Please give star rating atleast one", Toast.LENGTH_LONG).show();
                    }
                    else if(bookingId == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Please try after some time", Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                            FeedBack customerFeedback = new FeedBack();
                            customerFeedback.setStarRating(rating);
                            customerFeedback.setBookingId(bookingId);
                            customerFeedback.setFeedbackDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                            if(mCustomerFeedBack.getText().toString() != null && mCustomerFeedBack.getText().toString().isEmpty())
                            {
                                customerFeedback.setComment("");
                            }
                            else
                            {
                                customerFeedback.setComment(mCustomerFeedBack.getText().toString());
                            }
                            if(bookingId != 0)
                            {
                                customerFeedback.setBookingId(bookingId);
                            }
                            postFeedBack(customerFeedback);


                    }

                }


            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getFeedBack(final int bookingId) {

        try
        {
            final ProgressDialog dialog = new ProgressDialog(FeedBackScreen.this);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();
            new ThreadExecuter().execute(new Runnable() {
                @Override
                public void run() {
                    FeedbackApi bookingsApi = Util.getClient().create(FeedbackApi.class);
                    Call<ArrayList<FeedBack>> response = bookingsApi.getCustomerFeedBack(Constants.auth_string,bookingId);
                    response.enqueue(new Callback<ArrayList<FeedBack>>() {
                        @Override
                        public void onResponse(Call<ArrayList<FeedBack>> call, Response<ArrayList<FeedBack>> response) {
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }

                            if(response.code() == 200)
                            {
                                if(response.body() != null && response.body().size() != 0)
                                {
                                    feedBack = response.body().get(response.body().size() -1);
                                    System.out.println("Float Rating == "+Float.parseFloat(feedBack.getStarRating()));
                                    ratingBar.setRating(Float.parseFloat(feedBack.getStarRating()));
                                   // ratingBar.setEnabled(false);
                                    mCustomerFeedBack.setText(feedBack.getComment());
                                  //  mCustomerFeedBack.setEnabled(false);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<FeedBack>> call, Throwable t) {
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            System.out.println(t.getMessage());
                        }
                    });
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    private void postFeedBack(final FeedBack feedback) {

        final ProgressDialog dialog = new ProgressDialog(FeedBackScreen.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                FeedbackApi feedbackApi = Util.getClient().create(FeedbackApi.class);
                //String authenticationString = Util.getToken(CustomerFeedbackActivity.this);
                Call<FeedBack> response = feedbackApi.postCustomerFeedback(Constants.auth_string,feedback);
                response.enqueue(new Callback<FeedBack>() {
                    @Override
                    public void onResponse(Call<FeedBack> call, Response<FeedBack> response) {
                        //System.out.println("addhotel = "+response.code());
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        FeedBack feedBackResponse = response.body();
                        //System.out.println("addhotel = "+feedBackResponse.getFeedbackId());
                        if(response.code() == 200 && feedBackResponse != null)
                        {

                            Toast.makeText(FeedBackScreen.this,"Feedback posted successfully",Toast.LENGTH_LONG).show();

                            FeedBackScreen.this.finish();


                        }
                        else {

                            Toast.makeText(FeedBackScreen.this,"Please try after some time",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<FeedBack> call, Throwable t) {
                        System.out.println("onFailure");
                        Toast.makeText(FeedBackScreen.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                        if(dialog != null && dialog.isShowing())
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

                FeedBackScreen.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
