package com.hk.vehicleauth.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.hk.vehicleauth.R;
import com.hk.vehicleauth.VehicleDriverActivity;
import com.hk.vehicleauth.models.Vehicle;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {
    private Context context;
    private List<Vehicle> vehicles;

    public VehicleAdapter(Context context) {
        this.context = context;
    }
    public void setVehicleList(List<Vehicle> vehicles){
        this.vehicles = vehicles;
    }

    @NonNull
    @Override
    public VehicleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehicle_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleAdapter.ViewHolder holder, int position) {
        holder.serialTv.setText(String.valueOf(position + 1));
       Vehicle vehicle = vehicles.get(position);
        holder.carNoTv.setText(vehicle.getCarNo());

            holder.carNameTv.setText(vehicle.getName());

        holder.carSetting.setOnClickListener(l -> {
            PopupMenu popupMenu = new PopupMenu(context, l);
            popupMenu.getMenuInflater().inflate(R.menu.menu_vehicle_setting, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.deleteItem:
                        removeVehicle(vehicle);
                        break;
                    default:
                }
                return false;
            });
            popupMenu.show();
        });
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, VehicleDriverActivity.class);
            intent.putExtra("vId",vehicle.getCarNo());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView serialTv, carNameTv, carNoTv;
        LinearLayout carSetting;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serialTv = itemView.findViewById(R.id.serialTv);
            carNameTv = itemView.findViewById(R.id.carNameTV);
            carNoTv = itemView.findViewById(R.id.carNoTV);
            carSetting = itemView.findViewById(R.id.carIV);
        }
    }

    private void removeVehicle(Vehicle vehicle) {

    }
}
