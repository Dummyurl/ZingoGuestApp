package app.zingo.zingoguest.UI.BookingDetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import app.zingo.zingoguest.Adapters.MoreBookingAdapter;
import app.zingo.zingoguest.CustomViews.PageScrollListener;
import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.Traveller;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.BookingsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreBookingsScreen extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static View empty;
    ProgressBar progressBar;


    ArrayList<Bookings> activebookingsList;
    ArrayList<Traveller> travellerArrayList;

    LinearLayoutManager linearLayoutManager;
    MoreBookingAdapter adapter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES ;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_more_bookings_screen);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Bookings");

            recyclerView = (RecyclerView) findViewById(R.id.active_bookings_list);
            empty = (View)findViewById(R.id.empty);
            progressBar = (ProgressBar) findViewById(R.id.active_booking_main_progress);

            linearLayoutManager = new LinearLayoutManager(MoreBookingsScreen.this,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter = new MoreBookingAdapter(MoreBookingsScreen.this);
            recyclerView.setAdapter(adapter);

            recyclerView.setOnScrollListener(new PageScrollListener(linearLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;

                    currentPage = currentPage+1;
                    loadNextSetOfItems();
                }

                @Override
                public int getTotalPageCount() {
                    return currentPage;
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });

            loadFirstSetOfItems();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void loadFirstSetOfItems() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingsApi bookingApi = Util.getClient().create(BookingsApi.class);

                Call<ArrayList<Bookings>> getAllBookings = bookingApi.
                        getBookingsPagingById(PreferenceHandler.getInstance(MoreBookingsScreen.this).getUserId(),currentPage,10,10);

                getAllBookings.enqueue(new Callback<ArrayList<Bookings>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings>> call, Response<ArrayList<Bookings>> response) {


                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {
                                    activebookingsList = new ArrayList<>();
                                    //System.out.println("size = "+response.body().size());
                                    for (Bookings bookings1:response.body())
                                    {
                                        if(bookings1.getBookingStatus().equalsIgnoreCase("Active") ||
                                                bookings1.getBookingStatus().equalsIgnoreCase("Delay")||
                                                bookings1.getBookingStatus().equalsIgnoreCase("Quick"))
                                        {
                                            //System.out.println(bookings1.getHotelId());
                                            activebookingsList.add(bookings1);
                                        }
                                    }
                                    Collections.sort(activebookingsList, new Comparator<Bookings>() {
                                        @Override
                                        public int compare(Bookings o1, Bookings o2) {
                                            String o1date = formatDate(o1.getCheckInDate());
                                            String o2date = formatDate(o2.getCheckInDate());

                                            return o1date.compareTo(o2date);
                                        }
                                    });

                                    loadFirstPage(activebookingsList);

                                }
                                else
                                {
                                    adapter.removeLoadingFooter();
                                    isLastPage = true;
                                    isLoading = true;
                                    progressBar.setVisibility(View.GONE);
                                    empty.setVisibility(View.VISIBLE);
                                }

                            }
                            else
                            {
                                //Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                                empty.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings>> call, Throwable t) {
                    }
                });

                //WebService.getAllBookings(PreferenceHandler.getInstance(getActivity()).getHotelID());
            }
        });

    }

    public void loadNextSetOfItems() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingsApi bookingApi = Util.getClient().create(BookingsApi.class);
                Call<ArrayList<Bookings>> getAllBookings = bookingApi.
                        getBookingsPagingById(PreferenceHandler.getInstance(MoreBookingsScreen.this).getUserId(),currentPage,10,10);

                getAllBookings.enqueue(new Callback<ArrayList<Bookings>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings>> call, Response<ArrayList<Bookings>> response) {


                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {
                                    loadNextPage(response.body());
                                }
                                else
                                {
                                    adapter.removeLoadingFooter();
                                    isLastPage = true;
                                    empty.setVisibility(View.GONE);

                                }

                            }
                            else
                            {
                                //Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings>> call, Throwable t) {

                    }
                });

                //WebService.getAllBookings(PreferenceHandler.getInstance(getActivity()).getHotelID());
            }
        });

    }

    private void loadFirstPage(ArrayList<Bookings> list) {
        //Collections.reverse(list);
        progressBar.setVisibility(View.GONE);
        adapter.addAll(list);

        if (list != null && list.size() !=0)
            adapter.addLoadingFooter();
        else
            isLastPage = true;

    }

    private void loadNextPage(ArrayList<Bookings> list) {
        //Collections.reverse(list);

        adapter.removeLoadingFooter();
        isLoading = false;

        adapter.addAll(list);

        if (list != null && list.size() !=0)
        {
            adapter.addLoadingFooter();
        }
        else
        {
            isLastPage = true;
        }
    }

    public String formatDate(String date)
    {
        String sDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                MoreBookingsScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
