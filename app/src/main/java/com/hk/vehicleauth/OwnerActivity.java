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
import com.hk.vehicleauth.adapters.VehicleAdapter;
import com.hk.vehicleauth.databinding.ActivityOwnerBinding;


public class OwnerActivity extends AppCompatActivity {
    ActivityOwnerBinding binding;
    private VehicleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        binding.addCarFAB.setOnClickListener(l -> {
            //show insert alert dialog
            showAddVehicle();
        });
    }

    private void showAddVehicle() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.add_item_dialog, viewGroup, false);
        dialog.setView(view);

        TextInputEditText vehicleET = view.findViewById(R.id.itemIdET);
        AppCompatTextView titleTv = view.findViewById(R.id.titleTV);
        titleTv.setText("Add new vehicle");
        TextInputLayout vehicleLV = view.findViewById(R.id.itemInputLayout);
        vehicleLV.setHint("Vehicle no");
        AppCompatButton saveButton = view.findViewById(R.id.saveStBtn);
        AppCompatButton cancelButton = view.findViewById(R.id.cancelStBtn);
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }

    private void init() {
        getSupportActionBar().setTitle("Vehicle list");
        adapter = new VehicleAdapter(this);
        binding.vehiclesRV.setLayoutManager(new LinearLayoutManager(this));
        binding.vehiclesRV.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}