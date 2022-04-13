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

import com.hk.vehicleauth.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null)
            userRole = getIntent().getStringExtra("message");

        initView();
        binding.loginBtn.setOnClickListener(l -> {
            if(userRole.equals("driver")) {
                checkDriver();
            }
            else{
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
    public void checkOwner(){
        if(binding.emailET.getText().toString().trim().equals("")){
            binding.emailET.setError("email is required");
            return;
        }
        if(binding.passET.getText().toString().trim().equals("")){
            binding.passET.setError("password is required");
            return;
        }
        authOwner(
                binding.emailET.getText().toString(),
                binding.passET.getText().toString()
        );
    }
    public void checkDriver(){
        if(binding.vehicleIdET.getText().toString().trim().equals("")){
            binding.vehicleIdET.setError("vehicle id is required");
            return;
        }
        if(binding.driverIdET.getText().toString().trim().equals("")){
            binding.vehicleIdET.setError("driver id is required");
            return;
        }
        if(binding.passET.getText().toString().trim().equals("")){
            binding.passET.setError("password is required");
            return;
        }
        authDriver(
                binding.vehicleIdET.getText().toString(),
                binding.driverIdET.getText().toString()
        );
    }

    public void authOwner(String email, String pass){
        hideKeyBoard();
        startActivity(new Intent(this,OwnerActivity.class));
        finish();
    }

    private void authDriver(String vehicleId, String driverId) {
        hideKeyBoard();
        startActivity(new Intent(this,DriverActivity.class));
        finish();
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
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}