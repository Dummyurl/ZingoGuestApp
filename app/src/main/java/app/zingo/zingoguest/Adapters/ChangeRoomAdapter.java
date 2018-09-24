package app.zingo.zingoguest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.zingoguest.CustomViews.CustomGridView;
import app.zingo.zingoguest.Model.RoomCategories;
import app.zingo.zingoguest.Model.SelectingRoomModel;
import app.zingo.zingoguest.R;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class ChangeRoomAdapter extends RecyclerView.Adapter<ChangeRoomAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ArrayList<SelectingRoomModel>> catWiseRoomsArraylist;
    private ArrayList<String> categoriesArrayList;

    public ChangeRoomAdapter(Context context,ArrayList<ArrayList<SelectingRoomModel>> catWiseRoomsArraylist,ArrayList<String> categoriesArrayList)
    {
        this.context = context;
        this.catWiseRoomsArraylist = catWiseRoomsArraylist;
        this.categoriesArrayList = categoriesArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.changeroom_recyclerview_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArrayList<SelectingRoomModel> rooms = catWiseRoomsArraylist.get(position);
        String roomCategories = categoriesArrayList.get(position);

        if(roomCategories != null)
        {
            holder.textView.setText(roomCategories);
            if(rooms != null && rooms.size() != 0)
            {
                SelectRoomGridViewAdapter adapter = new SelectRoomGridViewAdapter(context,rooms);
                holder.gridview.setAdapter(adapter);
            }
        }

    }

    @Override
    public int getItemCount() {
        return catWiseRoomsArraylist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CustomGridView gridview;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);

            gridview = itemView.findViewById(R.id.change_room_grid_view);
            textView = itemView.findViewById(R.id.change_room_room_type);
        }
    }

    class SelectRoomGridViewAdapter extends BaseAdapter {

        Context context;
        ArrayList<SelectingRoomModel> rooms;
        String floorNo = null;

        boolean roomSelected = true;
        public SelectRoomGridViewAdapter(Context context, ArrayList<SelectingRoomModel> rooms)
        {
            this.context = context;
            this.rooms = rooms;
        }
        @Override
        public int getCount() {


            return rooms.size();

        }

        @Override
        public Object getItem(int position) {
            return rooms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.pre_booking_gridview_layout,parent,false);
            }

            final ImageView preBookingRoomView = (ImageView) convertView.findViewById(R.id.pre_booking_selected_room);
            TextView preBookingRoomNumber = (TextView) convertView.findViewById(R.id.pre_booking_selected_room_number);
            LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.room_layout);

            System.out.println("room selected = "+rooms.get(position).getIsSelected());
            if(rooms.get(position).getIsSelected())
            {
                preBookingRoomView.setImageResource(R.drawable.opened_door);
                //linearLayout.setBackgroundColor(Color.parseColor("#757575"));
            }
            else
            {
                preBookingRoomView.setImageResource(R.drawable.closed_door);
                //linearLayout.setBackgroundColor(Color.parseColor("#4CAF50"));
            }

            if(position == (rooms.size() - 1))
            {
                floorNo = null;
            }


            preBookingRoomNumber.setText(rooms.get(position).getSelectingRoom().split("-")[0]);

            return convertView;
        }
    }
}
