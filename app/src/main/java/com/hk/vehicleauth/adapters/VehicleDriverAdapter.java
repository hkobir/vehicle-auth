package com.hk.vehicleauth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.vehicleauth.R;
import com.hk.vehicleauth.models.Driver;

import java.util.List;

public class VehicleDriverAdapter extends RecyclerView.Adapter<VehicleDriverAdapter.ViewHolder> {
    private Context context;
    List<Driver> driverList;
    private String vehicleId;
    private DatabaseReference databaseReference;

    public VehicleDriverAdapter(Context context,String vehicleId) {
        this.context = context;
        this.vehicleId = vehicleId;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
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
        Driver cDriver = driverList.get(position);
        holder.driverNoTv.setText(cDriver.getDriverId());
        holder.driverNameTv.setText(cDriver.getName());
        if (cDriver.getStatus().equals("active"))
            holder.serialTv.setBackgroundResource(R.drawable.circle_green_view);
        else
            holder.serialTv.setBackgroundResource(R.drawable.circle_serial_view);

        holder.driverSetting.setOnClickListener(l -> {
            PopupMenu popupMenu = new PopupMenu(context, l);
            popupMenu.getMenuInflater().inflate(R.menu.menu_vehicle_setting, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.deleteItem:
                        removeDriver(cDriver);
                        break;
                    default:
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private void removeDriver(Driver cDriver) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference driverRef = databaseReference
                .child("vehicleDrivers")
                .child(vehicleId)
                .child("drivers");

                driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                Driver driver = dataSnapshot.getValue(Driver.class);
                                if(driver.equals(cDriver)){
                                    driverRef.child(snapshot.getKey()).removeValue();
                                    Toast.makeText(context, "Removed!", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return driverList.size();
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
