package app.zingo.zingoguest.UI.BookingDetails.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import app.zingo.zingoguest.Adapters.PagingBookingsAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CancelBooking extends Fragment {

    private static RecyclerView recyclerView;
    private static View empty;
    ProgressBar progressBar;


    ArrayList<Traveller> travellerArrayList;

    LinearLayoutManager linearLayoutManager;
    PagingBookingsAdapter adapter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES ;
    private int currentPage = PAGE_START;


    public CancelBooking() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{

            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_cancel_booking, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.active_bookings_list);
            empty = (View)view.findViewById(R.id.empty);
            progressBar = (ProgressBar) view.findViewById(R.id.active_booking_main_progress);

            linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter = new PagingBookingsAdapter(getActivity());
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

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public void loadFirstSetOfItems() {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingsApi bookingApi = Util.getClient().create(BookingsApi.class);

                Call<ArrayList<Bookings>> getAllBookings = bookingApi.
                        getBookingsPagingByStatus(PreferenceHandler.getInstance(getActivity()).getUserId(),"Cancelled",currentPage,10,10);

                getAllBookings.enqueue(new Callback<ArrayList<Bookings>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings>> call, Response<ArrayList<Bookings>> response) {


                        try{
                            if(response.code() == 200 && response.body()!= null)
                            {
                                if(response.body().size() != 0) {
                                    loadFirstPage(response.body());

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
                        getBookingsPagingByStatus(PreferenceHandler.getInstance(getActivity()).getUserId(),"Cancelled",currentPage,10,10);

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

}
