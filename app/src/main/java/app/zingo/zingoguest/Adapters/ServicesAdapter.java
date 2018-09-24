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

public class ServicesAdapter extends  RecyclerView.Adapter<ServicesAdapter.ViewHolder>
{

    private Context context;
    private ArrayList<Service> paymentArrayList;

    public ServicesAdapter(Context context,ArrayList<Service> paymentArrayList)
    {
        this.context = context;
        this.paymentArrayList = paymentArrayList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_list_adapter_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mPaymentName.setText(paymentArrayList.get(position).getDescription());
        holder.mPaymentPrice.setText("₹ "+paymentArrayList.get(position).getAmount()+"");
        holder.mPaymentStatus.setText(paymentArrayList.get(position).getPaidStatus()+"/"+paymentArrayList.get(position).getServiceStatus());

    }

    @Override
    public int getItemCount() {
        return paymentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mPaymentName,mPaymentPrice,mPaymentStatus;
        public ViewHolder(View itemView) {
            super(itemView);

            mPaymentName = (TextView) itemView.findViewById(R.id.services_detailed_payment_name);
            mPaymentPrice = (TextView) itemView.findViewById(R.id.services_detailed_payment_status);
            mPaymentStatus = (TextView) itemView.findViewById(R.id.services_detailed_payment_price);
        }
    }
}
