package app.zingo.zingoguest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.zingoguest.Model.Payment;
import app.zingo.zingoguest.R;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class PaymentsAdapter extends  RecyclerView.Adapter<PaymentsAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<Payment> paymentArrayList;

    public PaymentsAdapter(Context context,ArrayList<Payment> paymentArrayList)
    {
        this.context = context;
        this.paymentArrayList = paymentArrayList;
    }
    @Override
    public PaymentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_list_adapter_layout,parent,false);

        return new PaymentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentsAdapter.ViewHolder holder, int position) {

        holder.mPaymentName.setText(paymentArrayList.get(position).getPaymentName());
        holder.mPaymentPrice.setText("₹ "+paymentArrayList.get(position).getAmount()+"");
        holder.mPaymentStatus.setText(paymentArrayList.get(position).getPaymentType()+"");

    }

    @Override
    public int getItemCount() {
        return paymentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mPaymentName, mPaymentPrice, mPaymentStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            mPaymentName = (TextView) itemView.findViewById(R.id.services_detailed_payment_name);
            mPaymentPrice = (TextView) itemView.findViewById(R.id.services_detailed_payment_status);
            mPaymentStatus = (TextView) itemView.findViewById(R.id.services_detailed_payment_price);
        }
    }
}
