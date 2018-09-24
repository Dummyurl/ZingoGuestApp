package app.zingo.zingoguest.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Model.Traveller;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.UI.BookingDetails.BookingDetailsScreen;
import app.zingo.zingoguest.UI.LandingScreens.WelcomeScreen;
import app.zingo.zingoguest.UI.RoomViews.SelectRoom;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.RoomApi;
import app.zingo.zingoguest.WebAPI.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 21-09-2018.
 */

public class PagingBookingsAdapter extends RecyclerView.Adapter {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private Context context;
    private ArrayList<Bookings> list;
    Traveller travellers;

    private boolean isLoadingAdded = false;
    public PagingBookingsAdapter(Context context)
    {
        this.context = context;
        list = new ArrayList<>();

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType)
        {
            case ITEM:
                viewHolder = getViewHolder(parent,inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;
    }

    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent,LayoutInflater inflater)
    {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.fragment_booking_page_recyclerview_layout, parent, false);
        viewHolder = new BookingViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Bookings bookingAndTraveller = list.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                BookingViewHolder viewHolder = (BookingViewHolder) holder;
                if(bookingAndTraveller != null )
                {
                    //holder.mBookedPersonName.setText(traveller.getFirstName()+" "+traveller.getLastName());
                    final Bookings bookings1 = bookingAndTraveller;


                    if(bookings1 != null)
                    {



                        viewHolder.mBookingStatus.setVisibility(View.GONE);


                        viewHolder.mBookedId.setText("Booking Id: "+bookings1.getBookingId());
                        if(bookings1.getCheckInDate()!=null && !bookings1.getCheckInDate().isEmpty()&&bookings1.getCheckOutDate()!=null && !bookings1.getCheckOutDate().isEmpty()){
                            viewHolder.mBookingDates.setText(getBookingDateFormate(bookings1.getCheckInDate())
                                    +" To "+getBookingDateFormate(bookings1.getCheckOutDate()));
                        }
                        viewHolder.mNetAmount.setText("Gross Amount: ₹ "+bookings1.getTotalAmount());
                        if(bookings1.getBookingSource() != null)
                        {
                            viewHolder.mBookingSourceType.setText(bookings1.getBookingSource());
                            if(bookings1.getBookingSource().equalsIgnoreCase("GOIBIBO")){

                                viewHolder.mBookingSourceIcon.setImageResource(R.drawable.goibibo_icon);

                            }else if(bookings1.getBookingSource().equalsIgnoreCase("MAKEMY TRIP")||bookings1.getBookingSource().equalsIgnoreCase("MMT")){

                                viewHolder.mBookingSourceIcon.setImageResource(R.drawable.makemytrip_icon);

                            }else if(bookings1.getBookingSource().equalsIgnoreCase("BOOKING.COM")||bookings1.getBookingSource().equalsIgnoreCase("BOK")){

                                viewHolder.mBookingSourceIcon.setImageResource(R.drawable.bookingcom_icon);

                            }else if(bookings1.getBookingSource().equalsIgnoreCase("EXPEDIA")||bookings1.getBookingSource().equalsIgnoreCase("EXP")){

                                viewHolder.mBookingSourceIcon.setImageResource(R.drawable.expedia_icon);

                            }else if(bookings1.getBookingSource().equalsIgnoreCase("YATRA/TG")){

                                viewHolder.mBookingSourceIcon.setImageResource(R.drawable.yatra_icon);

                            }else if(bookings1.getBookingSource().equalsIgnoreCase("YATRA/TG")||bookings1.getBookingSource().equalsIgnoreCase("YAT")){

                                viewHolder.mBookingSourceIcon.setImageResource(R.drawable.yatra_icon);

                            }else if(bookings1.getBookingSource().equalsIgnoreCase("AGODA")){

                                viewHolder.mBookingSourceIcon.setImageResource(R.drawable.agoda_icon);

                            }else if(bookings1.getBookingSource().equalsIgnoreCase("VIA")){

                                viewHolder.mBookingSourceIcon.setImageResource(R.drawable.via_icon);

                            }
                        }


                        if(bookings1.getTravellerId() != 0)
                        {
                            getTraveller(bookings1,viewHolder.mBookedPersonName,viewHolder.mShortName);
                        }


                        viewHolder.mAllcateRoom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {



                            }
                        });

                        viewHolder.mViewDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Bookings",bookings1);
                                Intent bill = new Intent(context, BookingDetailsScreen.class);
                                bill.putExtras(bundle);
                                context.startActivity(bill);

                            }
                        });

                        viewHolder.mNoofRooms.setText(bookings1.getNoOfRooms()+" Room(s) - "+(long)getDays(bookings1.getCheckInDate(),bookings1.getCheckOutDate())+" Night(s)");

                        if(bookings1.getRoomId()==0){
                            viewHolder.mAllcateRoom.setVisibility(View.VISIBLE);
                        }else{
                            viewHolder.mAllcateRoom.setVisibility(View.GONE);
                        }


                    }

                }
                break;


            case LOADING:
//                Do nothing
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println("inside getItemViewType = "+position);
        if(position == list.size() - 1 && isLoadingAdded)
        {
            //System.out.println("inside getItemViewType LOADING = "+position);
            return LOADING;
        }
        else
        {
            //System.out.println("inside getItemViewType ITEM = "+position);
            return ITEM;
        }
        //return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }



    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView mBookedPersonName,mBookingDates,mNoofRooms,mNetAmount,
                mShortName,mBookingSourceType,mBookedId,mAllcateRoom,mViewDetails,mBookingStatus;
        ImageView mBookingSourceIcon;

        public BookingViewHolder(View itemView) {
            super(itemView);

            mBookedPersonName = (TextView) itemView.findViewById(R.id.booked_person_name);
            mBookedId = (TextView) itemView.findViewById(R.id.booked_id);
            mBookingDates = (TextView) itemView.findViewById(R.id.booked_from_to_date);
            mNoofRooms = (TextView) itemView.findViewById(R.id.booked_no_rooms_night);
            mNetAmount = (TextView) itemView.findViewById(R.id.net_amount);
            mShortName = (TextView) itemView.findViewById(R.id.person_short_name);
            mBookingSourceType = (TextView) itemView.findViewById(R.id.booking_source_type);
            mAllcateRoom = (TextView) itemView.findViewById(R.id.allocate_category);
            mViewDetails = (TextView) itemView.findViewById(R.id.viewDetails);
            mBookingStatus = (TextView) itemView.findViewById(R.id.booking_status);
            mBookingSourceIcon = (ImageView) itemView.findViewById(R.id.booking_source_icon);


        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public String getBookedOnDateFormate(String sdate)
    {
        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(sdate);
            String sDate = new SimpleDateFormat("dd MMM yyyy").format(date);

            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }

    public String getBookingDateFormate(String bdate)
    {
        String sDate = null;
        try {
            if(bdate.contains("-"))
            {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(bdate);
                sDate = new SimpleDateFormat("dd MMM").format(date);
            }
            else
            {
                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(bdate);
                sDate = new SimpleDateFormat("dd MMM").format(date);
            }

            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long getDays(String checkInDate, String checkOutDate) {

        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat myFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        /*String inputString1 = "01/10/2018";
        String inputString2 = "01/19/2018";*/
        Date date1,date2;
        try {
            if(checkInDate.contains("-"))
            {
                date1 = myFormat1.parse(checkInDate);
                date2 = myFormat1.parse(checkOutDate);
            }
            else
            {
                date1 = myFormat.parse(checkInDate);
                date2 = myFormat.parse(checkOutDate);
            }
            long diff = date2.getTime() - date1.getTime();
            //System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }




    public void add(Bookings mc) {
        //System.out.println("BookingAndTraveller = "+mc.getRoomBooking().getTotalAmount());
        list.add(mc);
        //System.out.println("BookingAndTraveller = "+list.size());
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<Bookings> mcList) {
        for (Bookings mc : mcList) {
            add(mc);
        }
    }

    public void remove(Bookings city) {
        int position = list.indexOf(city);
        if (position > -1) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        //add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        if(list != null && list.size() !=0)
        {
            int position = list.size() - 1;
            Bookings item = getItem(position);
            notifyItemChanged(position);

        }
    }

    public Bookings getItem(int position) {
        return list.get(position);
    }

    public ArrayList<Bookings> getAllBookings()
    {
        return list;
    }



    public  void getRoomNo(final int i, final TextView t)
    {
     //   String auth_string = Util.getToken(context);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        RoomApi api = Util.getClient().create(RoomApi.class);
        Call<Rooms> getRoom = api.getRoom(Constants.auth_string,i);

        getRoom.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                if(response.code() == 200)
                {
                    if(response.body() != null)
                    {
                        t.setText("R No: "+response.body().getRoomNo());
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

    private void getTraveller(final Bookings bookings1, final TextView tv, final TextView tv1) {
        /*final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(context.getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();*/



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                TravellerApi travellerApi = Util.getClient().create(TravellerApi.class);


                Call<Traveller> getTraveller = travellerApi.fetchTravelerById(Constants.auth_string,bookings1.getTravellerId());
                getTraveller.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/

                        if(response.code() == 200 || response.code() == 201 || response.code() == 204)
                        {
                            Traveller traveller =  response.body();
                            travellers = response.body();
                            if(traveller != null)
                            {


                                tv.setText(traveller.getFirstName());
                                if(traveller.getFirstName() != null && !traveller.getFirstName().isEmpty())
                                {
               /* if(traveller.getLastName() != null && !traveller.getLastName().isEmpty())
                {
                    holder.mShortName.setText(traveller.getFirstName().charAt(0)+""+traveller.getLastName().charAt(0));
                }
                else
                {
                    holder.mShortName.setText(traveller.getFirstName().charAt(0));
                }*/

                                    String[] ab = traveller.getFirstName().trim().split(" ");
                                    if(ab.length > 1)
                                    {
                                        //if(ab[1].charAt(0) != "")
                                        tv1.setText(ab[0].charAt(0)+"");//+""+ab[1].charAt(0));
                                    }
                                    else
                                    {
                                        tv1.setText(traveller.getFirstName().trim().charAt(0)+"");
                                    }
                                }
                            }
                            //Toast.makeText(context,"Booking is updated successfully",Toast.LENGTH_SHORT).show();
                            //BookingDetailsActivity.this.finish();

                        }
                        else
                        {
                            //Toast.makeText(context,"Please try after some time",Toast.LENGTH_SHORT).show();
                            travellers = null;
                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                    }
                });
            }
        });


    }


}
