package com.provizit.Logins;

import static android.view.View.GONE;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;

import com.provizit.AESUtil;
import com.provizit.Activities.NavigationActivity;
import com.provizit.Config.Constant;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.MVVM.RequestModels.CheckSetupModelRequest;
import com.provizit.MVVM.RequestModels.OtpSendEmaiModelRequest;
import com.provizit.R;
import com.provizit.Services.Model;
import com.provizit.Utilities.CompanyData;
import com.provizit.databinding.ActivityForgotBinding;
import com.provizit.databinding.ActivityInitialBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ForgotActivity";

    ActivityForgotBinding binding;

    BroadcastReceiver broadcastReceiver;

    AlertDialog.Builder builder;

    SharedPreferences.Editor editor1;

    int otp;
    AESUtil aesUtil;

    ApiViewModel apiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(ForgotActivity.this);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences sharedPreferences1 = ForgotActivity.this.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
        editor1 = sharedPreferences1.edit();


        aesUtil = new AESUtil(ForgotActivity.this);

        apiViewModel = new ViewModelProvider(ForgotActivity.this).get(ApiViewModel.class);



        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }


        //internet connection
        broadcastReceiver = new ConnectionReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)){
                    binding.relativeInternet.getRoot().setVisibility(GONE);
                    binding.relativeUi.setVisibility(View.VISIBLE);
                }else {
                    binding.relativeInternet.getRoot().setVisibility(View.VISIBLE);
                    binding.relativeUi.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();



        apiViewModel.getcheckSetupResponse().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                binding.relativeUi.setEnabled(true);
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404, internet_verified = 500;
                    if (statuscode.equals(failurecode)) {
                        ViewController.DismissProgressBar();
                        builder.setMessage("Presently, this app is accessible by only the enterprise users of PROVIZIT.\n" +
                                        "\n" +
                                        "We couldn’t find you as an enterprise user.\n" +
                                        "\n" +
                                        "You may contact your organization or write to info@provizit.com for more information.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (statuscode.equals(internet_verified)) {
                        ViewController.DismissProgressBar();
                    } else if (statuscode.equals(not_verified)) {
                        ViewController.DismissProgressBar();
                    } else if (statuscode.equals(successcode)) {

                        otp = Conversions.getNDigitRandomNumber(4);
                        System.out.println("Otp" + otp);
                        editor1.putString("ProvizitOtp", otp + "");
                        editor1.putString("ProvizitEmail", binding.editEmail.getText().toString().trim());
                        editor1.commit();
                        editor1.apply();

                        OtpSendEmaiModelRequest otpSendEmaiModelRequest = new OtpSendEmaiModelRequest(binding.editEmail.getText().toString().trim(),binding.editEmail.getText().toString().trim(),aesUtil.encrypt(otp + "", "egems_2013_grms_2017_provizit_2020"),aesUtil.encrypt(otp + "", binding.editEmail.getText().toString().trim()));
                        apiViewModel.otpsendemail(getApplicationContext(),otpSendEmaiModelRequest);
                        binding.relativeUi.setEnabled(false);
                    }
                }else {
                    ViewController.DismissProgressBar();
                }
            }
        });



        apiViewModel.getotpsendemailResponse().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                ViewController.DismissProgressBar();
                binding.relativeUi.setEnabled(true);
                if (response != null) {
                    Intent intent = new Intent(ForgotActivity.this, OtpActivity.class);
                    intent.putExtra("activity_type","forgot");
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(intent);
                }
            }
        });


        binding.next.setOnClickListener(this);
    }

    protected void registoreNetWorkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next) {
            AnimationSet animations = Conversions.animation();
            v.startAnimation(animations);
            if (ViewController.isEmailValid(binding.editEmail.getText().toString())) {
                //api mvvm
                CheckSetupModelRequest checkSetupModelRequest = new CheckSetupModelRequest(binding.editEmail.getText().toString().trim());
                apiViewModel.checkSetup(getApplicationContext(), checkSetupModelRequest);
                ViewController.ShowProgressBar(ForgotActivity.this);
            } else {
                binding.emailTextinput.setErrorEnabled(true);
                binding.emailTextinput.setError("Invalid Email");
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}