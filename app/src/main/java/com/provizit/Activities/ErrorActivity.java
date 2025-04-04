package com.provizit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.provizit.Config.ViewController;
import com.provizit.databinding.ActivityErrorBinding;

public class ErrorActivity extends AppCompatActivity {

    ActivityErrorBinding binding;

    String error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(ErrorActivity.this);
        binding = ActivityErrorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            error = (String) b.get("error");
        }

        binding.txtError.setText(error);

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });

        binding.goHome.setOnClickListener(view -> {
            Intent intent = new Intent(ErrorActivity.this, NavigationActivity.class);
            startActivity(intent);
        });


    }
}