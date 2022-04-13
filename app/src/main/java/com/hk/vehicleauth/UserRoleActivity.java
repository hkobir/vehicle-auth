package com.hk.vehicleauth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.hk.vehicleauth.databinding.ActivityUserRoleBinding;

public class UserRoleActivity extends AppCompatActivity {
    private ActivityUserRoleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_role);

        binding.driverRoleId.setOnClickListener(l -> {
            goMainActivity("driver");
        });
        binding.ownerRoleId.setOnClickListener(l -> {
            goMainActivity("owner");
        });
    }

    public void goMainActivity(String message) {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }
}