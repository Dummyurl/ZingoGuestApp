package app.zingo.zingoguest.UI.RoomViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.zingoguest.CustomViews.CustomFontTextView;
import app.zingo.zingoguest.Model.PaidAmenities;
import app.zingo.zingoguest.Model.PaidAmenitiesWithTitle;
import app.zingo.zingoguest.Model.Service;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.PaidAmenitiesOperation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomServicesScreen extends AppCompatActivity {

    RecyclerView mFoodList;
    private RoomServicesListAdapter adapter;
    TextView mTotal,mType;
    double totalValue=0;
    int total=0;
    RoomServicesListAdapter.ViewHolder viewHolder1;
    SubServiceAdapter.ViewHolder viewHolder;

    int HotelID,BookingId;
    String BookingNumber;
    ArrayList<PaidAmenitiesWithTitle> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_room_services_screen);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Room Services");

            mFoodList = (RecyclerView)findViewById(R.id.all_food_list);
            mTotal = (TextView) findViewById(R.id.tv_total);


            HotelID = getIntent().getIntExtra("BookingHotelId",0);
            BookingId = getIntent().getIntExtra("BookingBookingId",0);
            BookingNumber = getIntent().getStringExtra("BookingNumber");
            if(HotelID != 0)
            {
                getAmenities(HotelID);
            }
          
            mTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(list != null && list.size() != 0)
                    {
                        System.out.println("list = "+list.size());
                        ArrayList<Service> servicesList = new ArrayList<>();
                        for (int i=0;i<list.size();i++)
                        {
                            RoomServicesListAdapter.ViewHolder viewHolder = (RoomServicesListAdapter.ViewHolder)
                                    mFoodList.findViewHolderForAdapterPosition(i);
                            if(viewHolder != null)
                            {

                                for (int j=0;j<viewHolder.mItemsGridview.getChildCount();j++)
                                {
                                    LinearLayout linearLayout = (LinearLayout) viewHolder.mItemsGridview.getChildAt(j);
                                    LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(0);
                                    LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(0);
                                    LinearLayout linearLayout3 = (LinearLayout) linearLayout1.getChildAt(1);
                                    TextView foodname = (TextView) linearLayout2.getChildAt(0);
                                    TextView foodDescription = (TextView) linearLayout2.getChildAt(1);
                                    TextView foodPrice = (TextView) linearLayout3.getChildAt(0);
                                    LinearLayout linearLayout4 = (LinearLayout) linearLayout3.getChildAt(2);
                                    TextView quantitytext = (TextView) linearLayout4.getChildAt(1);
                                    if(Integer.parseInt(quantitytext.getText().toString()) != 0)
                                    {

                                        String name = foodname.getText().toString();
                                        String desc = foodDescription.getText().toString();
                                        String foodamount = foodPrice.getText().toString();
                                        int  quantity = Integer.parseInt(quantitytext.getText().toString());
                                        int price = 0;
                                        if(foodamount.contains("₹ "))
                                        {
                                            price = Integer.parseInt(foodamount.replace("₹ ",""));
                                        }

                                        Service  service = new Service();
                                        service.setDescription(name);
                                        service.setAmount(quantity*price);
                                        service.setPaidStatus("Unpaid");
                                        service.setPaymentMode("None");
                                        service.setBookingId(BookingId);
                                        service.setBookingNumber(BookingNumber);
                                        service.setQuantity(quantity);
                                        service.setServiceStatus("Pending");
                                        service.setPaymentDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

                                        servicesList.add(service);

                                    }
                                }

                            }
                        }

                        Intent intent = new Intent(RoomServicesScreen.this,ServicesListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("totalservices",servicesList);
                        intent.putExtra("BookingHotelId",HotelID);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public int setNumber(String number, int value,int amount){
        int count= Integer.parseInt(number);
        count = count+value;
        if(count<0){

        }else{
            calculation(amount,value);
        }


        return count;
    }

    public  void calculation(int value, int values){


        if(values == 1){

            totalValue=totalValue+value;
            mTotal.setText("Total Rs: "+(totalValue));
        }else{
            totalValue = totalValue - value;
            mTotal.setText("Total Rs: "+(totalValue));
        }


    }

    public void getAmenities(final int id)
    {
        final ProgressDialog dialog = new ProgressDialog(RoomServicesScreen.this);
        dialog.setMessage("Loading Services");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                PaidAmenitiesOperation api = Util.getClient().create(PaidAmenitiesOperation.class);
                Call<ArrayList<PaidAmenities>> paidamenitiesresponse = api.getAmenitiesByHotelId(Constants.auth_string,id);

                paidamenitiesresponse.enqueue(new Callback<ArrayList<PaidAmenities>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PaidAmenities>> call, Response<ArrayList<PaidAmenities>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            if(response.body() != null && response.body().size() != 0)
                            {
                                ArrayList<PaidAmenities> breakfastlist = new ArrayList<>();
                                ArrayList<PaidAmenities> lunchList = new ArrayList<>();
                                ArrayList<PaidAmenities> dinnerList = new ArrayList<>();

                                for (int i=0;i<response.body().size();i++)
                                {
                                    if(response.body().get(i).getCategory().equalsIgnoreCase("Breakfast"))
                                    {
                                        breakfastlist.add(response.body().get(i));
                                    }
                                    else if(response.body().get(i).getCategory().equalsIgnoreCase("Lunch"))
                                    {
                                        lunchList.add(response.body().get(i));
                                    }
                                    else if(response.body().get(i).getCategory().equalsIgnoreCase("Dinner"))
                                    {
                                        dinnerList.add(response.body().get(i));
                                    }
                                }

                                list = new ArrayList<>();
                                list.add(new PaidAmenitiesWithTitle("Breakfast",breakfastlist));
                                list.add(new PaidAmenitiesWithTitle("Lunch",lunchList));
                                list.add(new PaidAmenitiesWithTitle("Dinner",dinnerList));

                                RoomServicesListAdapter roomServicesListAdapter = new RoomServicesListAdapter(
                                        RoomServicesScreen.this,list);
                                mFoodList.setAdapter(roomServicesListAdapter);

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<PaidAmenities>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                    }
                });
            }
        });
    }

    public class RoomServicesListAdapter extends RecyclerView.Adapter<RoomServicesListAdapter.ViewHolder> {

        private Context context;
        private ArrayList<PaidAmenitiesWithTitle> list;

        public RoomServicesListAdapter(Context context, ArrayList<PaidAmenitiesWithTitle> list)
        {
            this.context = context;
            this.list = list;
        }

        @Override
        public RoomServicesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_list_item_layout,parent,false);
            return new RoomServicesListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RoomServicesListAdapter.ViewHolder holder, int position) {

            PaidAmenitiesWithTitle paidAmenitiesWithTitle = list.get(position);

            if(paidAmenitiesWithTitle != null)
            {
                holder.mItemTitle.setText(paidAmenitiesWithTitle.getTitle());

                ArrayList<PaidAmenities> paidAmenities = paidAmenitiesWithTitle.getList();

                if(paidAmenities != null && paidAmenities.size() != 0)
                {
                    SubServiceAdapter sadapter = new SubServiceAdapter(RoomServicesScreen.this,paidAmenities);
                    holder.mItemsGridview.setAdapter(sadapter);
                }
                else
                {
                    holder.mItemTitle.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            CustomFontTextView mItemTitle;
            RecyclerView mItemsGridview;

            public ViewHolder(View itemView) {
                super(itemView);

                mItemTitle = (CustomFontTextView)itemView.findViewById(R.id.service_heading);
                mItemsGridview = (RecyclerView)itemView.findViewById(R.id.services_gridview);
            }
        }
    }

    public class SubServiceAdapter  extends RecyclerView.Adapter<SubServiceAdapter.ViewHolder> {
        private Context context;
        int count = 0;
        int pos=-1;
        ArrayList<PaidAmenities> paidAmenitiesArrayList;
        public SubServiceAdapter(Context context,ArrayList<PaidAmenities> paidAmenitiesArrayList) {

            this.context = context;
            this.paidAmenitiesArrayList = paidAmenitiesArrayList;


        }

        @Override
        public SubServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_food_list, parent, false);
            return new ViewHolder(v);


        }

        @Override
        public void onBindViewHolder(final SubServiceAdapter.ViewHolder holder, final int position) {


            PaidAmenities paidAmenities = paidAmenitiesArrayList.get(position);

            if(paidAmenities != null)
            {
                holder.mFoodName.setText(paidAmenities.getText());
                holder.mFoodDesc.setText(paidAmenities.getDescription());
                holder.mFoodSR.setText("₹ "+paidAmenities.getAmenityRate());
                holder.mFoodAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        holder.mFoodAdd.setVisibility(View.GONE);
                        System.out.println("Total Before=="+total);
                        holder.mFoodCountNum.setText("0");
                        count=0;
                        holder.mFoodCount.setVisibility(View.VISIBLE);
                        mTotal.setVisibility(View.VISIBLE);
                        total++;
                        System.out.println("Total=="+total);
                    }
                });

                holder.mAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ++count;

                        int quantity = setNumber(holder.mFoodCountNum.getText().toString(),1,paidAmenitiesArrayList.get(position).getAmenityRate());

                        holder.mFoodCountNum.setText(""+quantity);


                    }
                });

                holder.mRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        int quantity = setNumber(holder.mFoodCountNum.getText().toString(),-1,paidAmenitiesArrayList.get(position).getAmenityRate());

                        if(quantity<0){
                            --total;
                            System.out.println("Total==="+total);
                            holder.mFoodCount.setVisibility(View.GONE);
                            holder.mFoodAdd.setVisibility(View.VISIBLE);
                            if(total<=0){
                                mTotal.setVisibility(View.GONE);
                            }
                        }else{
                            holder.mFoodCountNum.setText(""+quantity);

                        }




                    }
                });
            }



        }

        @Override
        public int getItemCount() {
            return paidAmenitiesArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        class ViewHolder extends RecyclerView.ViewHolder {


            TextView mFoodName,mFoodDesc,mFoodSR,mFoodDR,mFoodDis,mFoodAdd,mFoodCountNum;
            ImageView mAdd,mRemove;
            LinearLayout mFoodCount;
            public ViewHolder(View itemView) {
                super(itemView);
                mFoodName = (TextView) itemView.findViewById(R.id.food_name);
                mFoodDesc = (TextView) itemView.findViewById(R.id.food_description);
                mFoodSR = (TextView) itemView.findViewById(R.id.food_sell_rate);
                mFoodAdd = (TextView) itemView.findViewById(R.id.food_add_btn);
                mFoodCountNum = (TextView) itemView.findViewById(R.id.food_count_add);
                mAdd = (ImageView) itemView.findViewById(R.id.food_add);
                mRemove = (ImageView) itemView.findViewById(R.id.food_minus);
                mFoodCount = (LinearLayout) itemView.findViewById(R.id.food_count);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                RoomServicesScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}



