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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hk.vehicleauth.adapters.VehicleDriverAdapter;
import com.hk.vehicleauth.databinding.ActivityVehicleDriverBinding;

public class VehicleDriverActivity extends AppCompatActivity {
    ActivityVehicleDriverBinding binding;
    private VehicleDriverAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_driver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        binding.addDriverFAB.setOnClickListener(l -> {
            //show insert alert dialog
            showAddDriver();
        });
    }

    private void showAddDriver() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.add_item_dialog, viewGroup, false);
        dialog.setView(view);

        AppCompatTextView titleTv = view.findViewById(R.id.titleTV);
        titleTv.setText("Add new driver");
        TextInputEditText vehicleET = view.findViewById(R.id.itemIdET);
        TextInputLayout vehicleLV = view.findViewById(R.id.itemInputLayout);
        vehicleLV.setHint("Driver no");
        AppCompatButton saveButton = view.findViewById(R.id.saveStBtn);
        AppCompatButton cancelButton = view.findViewById(R.id.cancelStBtn);
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }

    private void init() {
        getSupportActionBar().setTitle("Vehicle no: 12s7a4h5");
        adapter = new VehicleDriverAdapter(this);
        binding.vehicleDriverRV.setLayoutManager(new LinearLayoutManager(this));
        binding.vehicleDriverRV.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}