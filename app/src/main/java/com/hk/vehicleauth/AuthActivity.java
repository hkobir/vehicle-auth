package com.hk.vehicleauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.vehicleauth.databinding.ActivityAuthBinding;
import com.hk.vehicleauth.models.Driver;
import com.hk.vehicleauth.models.Owner;
import com.hk.vehicleauth.utils.Common;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    String userRole;
    private DatabaseReference databaseReference;
    private boolean authOwner;
    private boolean authDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (getIntent() != null)
            userRole = getIntent().getStringExtra("message");

        initView();
        binding.loginBtn.setOnClickListener(l -> {
            if (userRole.equals("driver")) {
                checkDriver();
            } else {
                checkOwner();
            }
        });
    }

    private void initView() {
        switch (userRole) {
            case "driver":
                getSupportActionBar().setTitle("Driver login");
                binding.driverLayout.setVisibility(View.VISIBLE);
                binding.ownerLayout.setVisibility(View.GONE);
                break;
            case "owner":
                getSupportActionBar().setTitle("Owner login");
                binding.ownerLayout.setVisibility(View.VISIBLE);
                binding.driverLayout.setVisibility(View.GONE);
                break;
            default:
        }
    }

    public void checkOwner() {
        if (binding.emailET.getText().toString().trim().equals("")) {
            binding.emailET.setError("email is required");
            return;
        }
        if (binding.passET.getText().toString().trim().equals("")) {
            binding.passET.setError("password is required");
            return;
        }
        authOwner(
                binding.emailET.getText().toString(),
                binding.passET.getText().toString()
        );
    }

    public void checkDriver() {

        if (binding.driverIdET.getText().toString().trim().equals("")) {
            binding.driverIdET.setError("driver id is required");
            return;
        }
        if (binding.passET.getText().toString().trim().equals("")) {
            binding.passET.setError("password is required");
            return;
        }
        authDriver(
                binding.driverIdET.getText().toString(),
                binding.passET.getText().toString()
        );
    }

    public void authOwner(String email, String pass) {
        authOwner = false;
        hideKeyBoard();
        binding.progressView.setVisibility(View.VISIBLE);
        databaseReference.child("owners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot pSnapshot : snapshot.getChildren()) {
                        Owner owner = pSnapshot.getValue(Owner.class);
                        if (email.equals(owner.getEmail()) && pass.equals(owner.getPass())) {
                            authOwner = true;
                            Common.currentOwner = owner;
                            break;
                        }
                    }
                    if (authOwner) {
                        startActivity(new Intent(AuthActivity.this, OwnerActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AuthActivity.this, "Invalid credential",
                                Toast.LENGTH_SHORT).show();
                    }
                    binding.progressView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void authDriver(String driverId, String password) {
        authDriver = false;
        hideKeyBoard();
        binding.progressView.setVisibility(View.VISIBLE);
        databaseReference.child("drivers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot pSnapshot : snapshot.getChildren()) {
                        Driver driver = pSnapshot.getValue(Driver.class);
                        if (driverId.equals(driver.getDriverId()) &&
                                password.equals(driver.getPassword())) {
                            authDriver = true;
                            Common.currentDriver = driver;
                            break;
                        }
                    }
                    if (authDriver) {
                        startActivity(new Intent(AuthActivity.this,
                                DriverActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AuthActivity.this, "Invalid credential",
                                Toast.LENGTH_SHORT).show();
                    }
                    binding.progressView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hideKeyBoard() {
        View view = getCurrentFocus();
        InputMethodManager im = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        try {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0); // make keyboard hide
        } catch (NullPointerException e) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}