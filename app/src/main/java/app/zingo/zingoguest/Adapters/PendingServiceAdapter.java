package app.zingo.zingoguest.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.Service;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.UI.BookingDetails.PendingServices;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.ServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class PendingServiceAdapter extends RecyclerView.Adapter<PendingServiceAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Service> serviceArrayList;
    int travellerID;

    public PendingServiceAdapter(Context context, ArrayList<Service> serviceArrayList,int travellerID)
    {
        this.context = context;
        this.serviceArrayList = serviceArrayList;
        this.travellerID = travellerID;
        setHasStableIds(true);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pending_service,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        final Service service = serviceArrayList.get(position);


        if(service != null )
        {
            holder.mServiceName.setText(service.getDescription());
            holder.mServiceQuantity.setText(""+service.getQuantity());

        }

        holder.mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                service.setServiceStatus("Accepted");
                updateService(service);
            }
        });


        holder.mReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                service.setServiceStatus("Cancel");
                updateService(service);
            }
        });

    }



    @Override
    public int getItemCount() {
        return serviceArrayList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView mServiceName,mServiceQuantity;
        Button mAccept,mReject;

        public ViewHolder(View itemView) {
            super(itemView);

            mServiceName = (TextView)itemView.findViewById(R.id.service_name);
            mServiceQuantity = (TextView)itemView.findViewById(R.id.service_quantity);
            mAccept = (Button)itemView.findViewById(R.id.service_accept);
            mReject = (Button)itemView.findViewById(R.id.service_reject);
        }
    }

    private void updateService(final Service service) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                ServiceApi serviceApi = Util.getClient().create(ServiceApi.class);

                Call<Service> response = serviceApi.updateService(Constants.auth_string,service.getServicesId(),service);
                response.enqueue(new Callback<Service>() {
                    @Override
                    public void onResponse(Call<Service> call, Response<Service> response) {
                        Service serviceResponse = response.body();
                        if(response.code() == 200 && serviceResponse != null)
                        {

                            Toast.makeText(context,"Service updated successfully",Toast.LENGTH_LONG).show();

                            if(dialog != null && dialog.isShowing())
                            {
                                dialog.dismiss();
                            }

                            Intent intent = new Intent(context,PendingServices.class);
                            intent.putExtra("PendingServiceBookingId",service.getBookingId());
                            context.startActivity(intent);
                            ((Activity)context).finish();

                        }
                        else {

                            Toast.makeText(context,"Please try after some time",Toast.LENGTH_SHORT).show();
                            if(dialog != null && dialog.isShowing())
                            {
                                dialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Service> call, Throwable t) {
                        System.out.println("onFailure");
                        Toast.makeText(context,"Please try after some time",Toast.LENGTH_SHORT).show();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    }
                });

            }
        });
    }


}
