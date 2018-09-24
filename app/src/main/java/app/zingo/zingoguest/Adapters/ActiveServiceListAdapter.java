package app.zingo.zingoguest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.zingoguest.Model.Bookings;
import app.zingo.zingoguest.Model.Rooms;
import app.zingo.zingoguest.Model.Traveller;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.UI.BookingDetails.PendingServices;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.RoomApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class ActiveServiceListAdapter extends RecyclerView.Adapter<ActiveServiceListAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Bookings> bookingArrayList;

    public ActiveServiceListAdapter(Context context, ArrayList<Bookings> bookingArrayList)
    {
        this.context = context;
        this.bookingArrayList = bookingArrayList;
        setHasStableIds(true);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_active_service_list,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        final Bookings bookings1 = bookingArrayList.get(position);

        if(bookings1 != null )
        {
            //holder.mBookedPersonName.setText(traveller.getFirstName()+" "+traveller.getLastName());
            holder.mBookingNumber.setText("Booking Id: "+bookings1.getBookingId());
            if(bookings1.getRoomId() != 0 && bookings1.getBookingStatus() != null)
            {
                getRoomNo(bookings1.getRoomId(),holder.mRoomNumber);
                holder.mRoomNumber.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.mRoomNumber.setTextColor(Color.parseColor("#706f6f"));
            }





            System.out.println("Booking status = "+bookings1.getBookingStatus());



            holder.mparent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,PendingServices.class);
                    intent.putExtra("PendingServiceBookingId",bookings1.getBookingId());
                    context.startActivity(intent);
                }
            });



        }
    }



    @Override
    public int getItemCount() {
        return bookingArrayList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mServiceStatus,mBookingNumber,mRoomNumber;
        LinearLayout mparent;

        public ViewHolder(View itemView) {
            super(itemView);

            mRoomNumber = (TextView) itemView.findViewById(R.id.call_booked_room_no);
            mServiceStatus = (TextView) itemView.findViewById(R.id.service_status);
            mBookingNumber = (TextView) itemView.findViewById(R.id.booking_number);
            mparent = (LinearLayout) itemView.findViewById(R.id.parent_layout_for_user_details);

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

    public  void getRoomNo(final int i, final TextView t)
    {
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

}
