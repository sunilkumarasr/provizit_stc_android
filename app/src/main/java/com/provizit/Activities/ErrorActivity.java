package com.provizit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.provizit.Config.ViewController;
import com.provizit.R;
import com.provizit.databinding.ActivityErrorBinding;
import com.provizit.databinding.ActivityInitialBinding;

public class ErrorActivity extends AppCompatActivity {

    ActivityErrorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(ErrorActivity.this);
        binding = ActivityErrorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });

        binding.goHome.setOnClickListener(view -> {
            Intent intent = new Intent(ErrorActivity.this, NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });


    }
}