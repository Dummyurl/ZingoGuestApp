package app.zingo.zingoguest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.Service;
import app.zingo.zingoguest.R;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class ServiceTakenAdapter extends RecyclerView.Adapter<ServiceTakenAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Service> serviceArrayList;

    public ServiceTakenAdapter(Context context, ArrayList<Service> serviceArrayList)
    {
        this.context = context;
        this.serviceArrayList = serviceArrayList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Service service = serviceArrayList.get(position);
        if(service != null)
        {
            holder.mServiceName.setText(service.getDescription());
            holder.mServiceQuantity.setText(service.getQuantity()+"");
            holder.mServicesPrice.setText("₹ "+service.getAmount());
        }
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

        TextView mServiceName,mServiceQuantity,mServicesPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            mServiceName = (TextView)itemView.findViewById(R.id.list_service_name);
            mServiceQuantity = (TextView)itemView.findViewById(R.id.list_service_quantity);
            mServicesPrice = (TextView)itemView.findViewById(R.id.list_service_price);
        }
    }
}
