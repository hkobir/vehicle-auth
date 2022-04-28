package com.hk.vehicleauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.vehicleauth.adapters.VehicleDriverAdapter;
import com.hk.vehicleauth.databinding.ActivityVehicleDriverBinding;
import com.hk.vehicleauth.models.Driver;
import com.hk.vehicleauth.models.Vehicle;
import com.hk.vehicleauth.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class VehicleDriverActivity extends AppCompatActivity {
    ActivityVehicleDriverBinding binding;
    private VehicleDriverAdapter adapter;
    private String vehicleId = "";
    private DatabaseReference databaseReference;
    List<Driver> driverList;
    private AlertDialog dialog;
    private boolean existDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_driver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        loadDrivers();
        binding.addDriverFAB.setOnClickListener(l -> {
            //show insert alert dialog
            showAddDriver();
        });
    }

    private void showAddDriver() {
        dialog = new AlertDialog.Builder(this).create();
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.add_item_dialog, viewGroup, false);
        dialog.setView(view);

        AppCompatTextView titleTv = view.findViewById(R.id.titleTV);
        titleTv.setText("Add new driver");
        ProgressBar progressBar = view.findViewById(R.id.progressB);
        TextInputEditText driverET = view.findViewById(R.id.itemIdET);
        TextInputLayout vehicleLV = view.findViewById(R.id.itemInputLayout);
        vehicleLV.setHint("Driver no");
        AppCompatButton saveButton = view.findViewById(R.id.saveStBtn);
        AppCompatButton cancelButton = view.findViewById(R.id.cancelStBtn);
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        saveButton.setOnClickListener(v -> {
            if (driverET.getText().toString().equals("")) {
                driverET.setError("Driver id is required");
                return;
            }
            checkDriver(
                    driverET.getText().toString(),
                    progressBar
            );
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void init() {
        if (getIntent() != null)
            vehicleId = getIntent().getStringExtra("vId");
        getSupportActionBar().setTitle("Vehicle No: " + vehicleId);


        driverList = new ArrayList<>();
        adapter = new VehicleDriverAdapter(this, vehicleId);
        adapter.setDriverList(driverList);
        binding.vehicleDriverRV.setLayoutManager(new LinearLayoutManager(this));
        binding.vehicleDriverRV.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void loadDrivers() {
        DatabaseReference driverRef = databaseReference.child("vehicleDrivers").child(vehicleId);
        binding.progressLoad.setVisibility(View.VISIBLE);
        driverRef.child("currentDriver").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String currentDriver = snapshot.getValue().toString();
                    driverRef.child("drivers")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        binding.emptyItemView.setVisibility(View.GONE);
                                        driverList.clear();
                                        for (DataSnapshot pSnap : snapshot.getChildren()) {
                                            Driver driver = pSnap.getValue(Driver.class);
                                            if (currentDriver == driver.getDriverId()) {
                                                driver.setStatus("active");
                                            } else {
                                                driver.setStatus("null");
                                            }
                                            driverList.add(driver);
                                        }
                                        adapter.setDriverList(driverList);
                                        adapter.notifyDataSetChanged();
                                        binding.progressLoad.setVisibility(View.GONE);
                                    } else {
                                        binding.progressLoad.setVisibility(View.GONE);
                                        binding.emptyItemView.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                } else {
                    binding.progressLoad.setVisibility(View.GONE);
                    binding.emptyItemView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkDriver(String dId, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        existDriver = false;
        databaseReference.child("drivers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot pSnapshot : snapshot.getChildren()) {
                        Driver driver = pSnapshot.getValue(Driver.class);
                        if (dId.equals(driver.getDriverId())) {
                            existDriver = true;
                            addDriver(driver, progressBar);
                            break;
                        }
                    }
                    if (!existDriver) {
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toast.makeText(VehicleDriverActivity.this,
                                "Not a valid driver", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addDriver(Driver driver, ProgressBar progressBar) {
        databaseReference.child("vehicleDrivers")
                .child(vehicleId)
                .child("drivers")
                .push()
                .setValue(driver);
        dialog.dismiss();
        progressBar.setVisibility(View.GONE);
        Toast.makeText(VehicleDriverActivity.this,
                "driver added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}