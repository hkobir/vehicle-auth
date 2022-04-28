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
import com.hk.vehicleauth.adapters.VehicleAdapter;
import com.hk.vehicleauth.databinding.ActivityOwnerBinding;
import com.hk.vehicleauth.models.Vehicle;
import com.hk.vehicleauth.utils.Common;

import java.util.ArrayList;
import java.util.List;


public class OwnerActivity extends AppCompatActivity {
    ActivityOwnerBinding binding;
    private VehicleAdapter adapter;
    private DatabaseReference databaseReference;
    private boolean existVehicle;
    private List<Vehicle> vehicles;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        loadVehicleList();

        binding.addCarFAB.setOnClickListener(l -> {
            //show insert alert dialog
            showAddVehicle();
        });
    }

    private void showAddVehicle() {
        dialog = new AlertDialog.Builder(this).create();
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.add_item_dialog, viewGroup, false);
        dialog.setView(view);

        TextInputEditText vehicleET = view.findViewById(R.id.itemIdET);
        AppCompatTextView titleTv = view.findViewById(R.id.titleTV);
        ProgressBar progressBar = view.findViewById(R.id.progressB);
        titleTv.setText("Add new vehicle");
        TextInputLayout vehicleLV = view.findViewById(R.id.itemInputLayout);
        vehicleLV.setHint("Vehicle no");
        AppCompatButton saveButton = view.findViewById(R.id.saveStBtn);
        AppCompatButton cancelButton = view.findViewById(R.id.cancelStBtn);
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        saveButton.setOnClickListener(v -> {
            if (vehicleET.getText().toString().equals("")) {
                vehicleET.setError("Vehicle id is required");
                return;
            }
            checkVehicle(
                    vehicleET.getText().toString(),
                    progressBar
            );
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void init() {
        vehicles = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setTitle("Vehicle list");
        adapter = new VehicleAdapter(this);
        binding.vehiclesRV.setLayoutManager(new LinearLayoutManager(this));
        adapter.setVehicleList(vehicles);
        binding.vehiclesRV.setAdapter(adapter);
    }

    public void loadVehicleList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("ownerVehicles")
                .child(Common.currentOwner.getNid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.emptyItemView.setVisibility(View.GONE);
                            vehicles.clear();
                            for (DataSnapshot pSnap : snapshot.getChildren()) {
                                Vehicle vehicle = pSnap.getValue(Vehicle.class);
                                vehicles.add(vehicle);
                            }
                            adapter.setVehicleList(vehicles);
                            adapter.notifyDataSetChanged();
                            binding.progressBar.setVisibility(View.GONE);
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.emptyItemView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void checkVehicle(String vId, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        existVehicle = false;
        databaseReference.child("vehicles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot pSnapshot : snapshot.getChildren()) {
                        Vehicle vehicle = pSnapshot.getValue(Vehicle.class);
                        if (vId.equals(vehicle.getCarNo())) {
                            existVehicle = true;
                            addVehicle(vehicle, progressBar);
                            break;
                        }
                    }
                    if (!existVehicle) {
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toast.makeText(OwnerActivity.this, "Not a valid vehicle", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addVehicle(Vehicle vehicle, ProgressBar progressBar) {
        databaseReference.child("ownerVehicles")
                .child(Common.currentOwner.getNid())
                .push()
                .setValue(vehicle);
        databaseReference.child("vehicleDrivers")
                .child(vehicle.getCarNo())
                .child("profile")
                .setValue(vehicle);
        databaseReference.child("vehicleDrivers")
                .child(vehicle.getCarNo())
                .child("currentDriver")
                .setValue("null");
        progressBar.setVisibility(View.GONE);
        dialog.dismiss();
        Toast.makeText(OwnerActivity.this, "vehicle added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}