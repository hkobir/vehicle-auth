package com.hk.vehicleauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hk.vehicleauth.adapters.DriverVehicleAdapter;
import com.hk.vehicleauth.databinding.ActivityDriverBinding;
import com.hk.vehicleauth.models.Vehicle;

import java.util.ArrayList;

public class DriverActivity extends AppCompatActivity {
    ActivityDriverBinding binding;
    private ArrayList<Vehicle> vehicles;
    private DriverVehicleAdapter adapter;
    private ListView listView;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        showVehicleOptions();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (vehicles.get(position).getStatus().equals("null")) {
                    binding.vehicleIdTv.setText(
                            vehicles.get(position).getId()
                    );
                    Toast.makeText(DriverActivity.this,
                            "vehicle selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DriverActivity.this,
                            "vehicle is not free", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    private void showVehicleOptions() {
        AlertDialog.Builder alertDialog = new
                AlertDialog.Builder(this);
        View rowList = getLayoutInflater().inflate(R.layout.vehicle_list_dialog, null);
        listView = rowList.findViewById(R.id.vehicleLV);
        adapter = new DriverVehicleAdapter(this, vehicles);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void initView() {
        //set profile data
        vehicles = generateVehicleList();
        binding.driverIdTV.setText("a52276ht");
        binding.nameTV.setText("Humayun Kobir");
    }

    private ArrayList<Vehicle> generateVehicleList() {
        ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();
        Vehicle v1 = new Vehicle("1g2u55i8", "Tesla boom", "1ht56jj", "M Habib");
        vehicleArrayList.add(v1);

        Vehicle v2 = new Vehicle("1g0u57i8", "Tesla boom", "null", "k mahbub");
        vehicleArrayList.add(v2);

        Vehicle v3 = new Vehicle("122u95i8", "ToyoTa", "1ht56jj", "Mr. John");
        vehicleArrayList.add(v3);

        Vehicle v4 = new Vehicle("1g1u35i8", "Marcedes", "null", "Rafa kerry");
        vehicleArrayList.add(v4);

        Vehicle v5 = new Vehicle("1g2u15i8", "BMW", "1ht56jj", "M Jaman");
        vehicleArrayList.add(v5);
        return vehicleArrayList;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}