package app.zingo.zingoguest.Adapters;

import android.content.Context;
import android.graphics.Color;
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

public class RoomServiceAdapters extends RecyclerView.Adapter<RoomServiceAdapters.ViewHolder> {

    private Context context;
    private ArrayList<Service> list;

    public RoomServiceAdapters(Context context,ArrayList<Service> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Service service = list.get(position);

        if(service != null)
        {

            if(service.getServiceStatus() != null)
            {
                System.out.println("Status = "+service.getServiceStatus());
                if(service.getServiceStatus().equalsIgnoreCase("Accepted"))
                {
                    holder.mServiceName.setText(service.getDescription());
                    holder.mServiceStatus.setText(service.getServiceStatus());
                    holder.mServiceStatus.setTextColor(Color.parseColor("#388E3C"));
                }
                else if(service.getServiceStatus().equalsIgnoreCase("Rejected"))
                {
                    holder.mServiceName.setText(service.getDescription());
                    holder.mServiceStatus.setText(service.getServiceStatus());
                    holder.mServiceStatus.setTextColor(Color.parseColor("#f44336"));

                }
                else if(service.getServiceStatus().equalsIgnoreCase("Pending"))
                {
                    holder.mServiceName.setText(service.getDescription());
                    holder.mServiceStatus.setText(service.getServiceStatus());
                    holder.mServiceStatus.setTextColor(Color.parseColor("#FFA000"));
                }
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

        TextView mServiceName,mServiceStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            mServiceName = itemView.findViewById(R.id.accepted_service_name);
            mServiceStatus = itemView.findViewById(R.id.accepted_service_status);
        }
    }
}
