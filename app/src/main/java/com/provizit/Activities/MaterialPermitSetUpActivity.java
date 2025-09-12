package com.provizit.Activities;

import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.RoleDetails;
import com.provizit.databinding.ActivityMaterialPermitSetUpBinding;
import com.provizit.databinding.ActivityWorkPermitSetUpBinding;

public class MaterialPermitSetUpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMaterialPermitSetUpBinding binding;

    BroadcastReceiver broadcastReceiver;

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;
    SharedPreferences.Editor editor1;
    RoleDetails roleDetails;
    AnimationSet animation;
    ApiViewModel apiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialPermitSetUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inits();

    }


    private void inits() {
        ViewController.barPrimaryColor(MaterialPermitSetUpActivity.this);
        ViewController.internetConnection(broadcastReceiver, MaterialPermitSetUpActivity.this);
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", 0);
        editor1 = sharedPreferences1.edit();
        myDb = new DatabaseHelper(this);
        empData = new EmpData();
        roleDetails = new RoleDetails();
        empData = myDb.getEmpdata();
        roleDetails = myDb.getRole();
        animation = Conversions.animation();
        apiViewModel = new ViewModelProvider(MaterialPermitSetUpActivity.this).get(ApiViewModel.class);


        binding.imgBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                view.startAnimation(animation);
                finish();
                break;
        }
    }

}