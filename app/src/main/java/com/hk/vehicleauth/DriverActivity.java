package com.hk.vehicleauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.vehicleauth.adapters.DriverVehicleAdapter;
import com.hk.vehicleauth.databinding.ActivityDriverBinding;
import com.hk.vehicleauth.models.Driver;
import com.hk.vehicleauth.models.Vehicle;
import com.hk.vehicleauth.models.VehicleDriver;
import com.hk.vehicleauth.utils.Common;
import com.hk.vehicleauth.utils.DataPreference;

import java.util.ArrayList;

public class DriverActivity extends AppCompatActivity {
    ActivityDriverBinding binding;
    private ArrayList<Vehicle> vehicles;
    private DriverVehicleAdapter adapter;
    private ListView listView;
    private AlertDialog dialog;
    private DatabaseReference databaseReference;
    private ProgressBar progressDialog;
    private boolean authDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        if (DataPreference.getString(this, Common.DRIVER_ID_KEY).equals(""))
            showVehicleOptions();
        else
            loadProfile();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vehicle cVehicle = vehicles.get(position);
                if (cVehicle.getStatus().equals("free")) {
                    validateDriver(cVehicle);
                } else {
                    Toast.makeText(DriverActivity.this,
                            "vehicle is not free", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("vehicleDrivers")
                        .child(DataPreference.getString(DriverActivity.this, Common.VEHICLE_KEY))
                        .child("currentDriver")
                        .setValue("null")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DriverActivity.this, "Logged out",
                                        Toast.LENGTH_SHORT).show();
                                DataPreference.clearAllData(DriverActivity.this);
                                startActivity(new Intent(DriverActivity.this, AuthActivity.class));
                            }
                        });

            }
        });
    }

    private void validateDriver(Vehicle vehicle) {
        authDriver = false;
        progressDialog.setVisibility(View.VISIBLE);
        databaseReference.child("vehicleDrivers")
                .child(vehicle.getCarNo())
                .child("drivers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot pSnap : snapshot.getChildren()) {
                                Driver driver = pSnap.getValue(Driver.class);
                                if (driver.getDriverId().equals(Common.currentDriver.getDriverId())) {
                                    authDriver = true;
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.setVisibility(View.GONE);
                    }
                });
        if (authDriver) {
            binding.vehicleIdTv.setText(
                    vehicle.getCarNo()
            );
            databaseReference.child("vehicleDrivers")
                    .child(vehicle.getCarNo())
                    .child("currentDriver")
                    .setValue(Common.currentDriver.getDriverId())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            DataPreference.saveString(
                                    DriverActivity.this,
                                    Common.DRIVER_ID_KEY,
                                    Common.currentDriver.getDriverId()
                            );
                            DataPreference.saveString(
                                    DriverActivity.this,
                                    Common.DRIVER_NAME_KEY,
                                    Common.currentDriver.getName()
                            );
                            DataPreference.saveString(
                                    DriverActivity.this,
                                    Common.VEHICLE_KEY,
                                    vehicle.getName()
                            );

                            progressDialog.setVisibility(View.GONE);
                            Toast.makeText(DriverActivity.this,
                                    "vehicle selected", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
        } else {
            Toast.makeText(DriverActivity.this,
                    "You are not permitted for this vehicle!", Toast.LENGTH_SHORT).show();
            progressDialog.setVisibility(View.GONE);
        }
    }

    private void loadProfile() {
        binding.vehicleIdTv.setText(
                DataPreference.getString(this, Common.VEHICLE_KEY)
        );
    }


    private void showVehicleOptions() {
        AlertDialog.Builder alertDialog = new
                AlertDialog.Builder(this);
        View rowList = getLayoutInflater().inflate(R.layout.vehicle_list_dialog, null);
        listView = rowList.findViewById(R.id.vehicleLV);
        progressDialog = rowList.findViewById(R.id.progressV);
        databaseReference.child("vehicleDrivers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.setVisibility(View.VISIBLE);
                if (snapshot.exists()) {
                    vehicles.clear();
                    for (DataSnapshot pSnap : snapshot.getChildren()) {
                        VehicleDriver vehicleDriver = pSnap.getValue(VehicleDriver.class);
                        Vehicle vehicle = vehicleDriver.getProfile();
                        if (vehicleDriver.getCurrentDriver().equals("null"))
                            vehicle.setStatus("free");
                        else
                            vehicle.setStatus("busy");
                        vehicles.add(vehicle);
                    }
                    adapter = new DriverVehicleAdapter(DriverActivity.this, vehicles);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    alertDialog.setView(rowList);
                    dialog = alertDialog.create();
                    dialog.setCancelable(false);
                    dialog.show();
                    progressDialog.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initView() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //set profile data
        vehicles = new ArrayList<>();
        binding.driverIdTV.setText(Common.currentDriver.getDriverId());
        binding.nameTV.setText(Common.currentDriver.getName());
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}