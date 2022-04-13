package com.hk.vehicleauth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.hk.vehicleauth.R;

public class VehicleDriverAdapter extends RecyclerView.Adapter<VehicleDriverAdapter.ViewHolder> {
    private Context context;

    public VehicleDriverAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public VehicleDriverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehicle_driver_layout, parent, false);
        return new VehicleDriverAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleDriverAdapter.ViewHolder holder, int position) {
        String uId = java.util.UUID.randomUUID().toString();
        holder.driverNoTv.setText(uId.substring(0, 7));
        holder.serialTv.setBackgroundResource(R.drawable.circle_serial_view);
        holder.driverNameTv.setText("Driver " + (position + 1));
        if (position == 1)
            holder.serialTv.setBackgroundResource(R.drawable.circle_green_view);

        holder.driverSetting.setOnClickListener(l->{
            PopupMenu popupMenu = new PopupMenu(context, l);
            popupMenu.getMenuInflater().inflate(R.menu.menu_vehicle_setting, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.deleteItem:

                        break;
                    default:
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView serialTv, driverNameTv, driverNoTv;
        LinearLayout driverSetting;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serialTv = itemView.findViewById(R.id.serialDriverTv);
            driverNameTv = itemView.findViewById(R.id.driverNameTV);
            driverNoTv = itemView.findViewById(R.id.driverNoTV);
            driverSetting = itemView.findViewById(R.id.driverIV);
        }
    }
}
