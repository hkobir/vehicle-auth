package com.hk.vehicleauth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.hk.vehicleauth.R;
import com.hk.vehicleauth.models.Vehicle;

import java.util.ArrayList;

public class DriverVehicleAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Vehicle> vehicles;

    public DriverVehicleAdapter(Context context, ArrayList<Vehicle> vehicles) {
        this.context = context;
        this.vehicles = vehicles;
    }

    @Override
    public int getCount() {
        return vehicles.size();
    }

    @Override
    public Object getItem(int position) {
        return vehicles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_driver_vehicle, parent, false);
        }
        Vehicle vehicle = vehicles.get(position);
        AppCompatTextView status = convertView.findViewById(R.id.statusVTv);
        AppCompatTextView vehicleId = convertView.findViewById(R.id.vehicleNoTV);
        AppCompatTextView vehicleName = convertView.findViewById(R.id.vehicleNameTV);
        AppCompatTextView ownerName = convertView.findViewById(R.id.menuTV);

        status.setBackgroundResource(
                vehicle.getStatus().equals("null") ?
                        R.drawable.circle_green_view : R.drawable.circle_serial_view
        );
        vehicleId.setText(vehicle.getId());
        vehicleName.setText(vehicle.getName());
        ownerName.setText(vehicle.getOwnerName());
        return convertView;
    }
}
