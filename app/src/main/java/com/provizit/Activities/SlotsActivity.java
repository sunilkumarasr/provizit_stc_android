package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.databinding.ActivitySlotsBinding;
import com.provizit.databinding.ActivitySplashBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SlotsActivity extends AppCompatActivity {
    private static final String TAG = "SlotsActivity";

    ActivitySlotsBinding activitySlotsBinding;

    BroadcastReceiver broadcastReceiver;

    //db
    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;

    ApiViewModel apiViewModel;

    String id = "";

    CompanyData companyData_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(SlotsActivity.this);
        activitySlotsBinding = DataBindingUtil.setContentView(this, R.layout.activity_slots);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = b.getString("id");
        }

        activitySlotsBinding.imgBack.setOnClickListener(view -> finish());

        //db
        sharedPreferences1 = getApplicationContext().getSharedPreferences("EGEMSS_DATA", 0);
        myDb = new DatabaseHelper(SlotsActivity.this);
        empData = myDb.getEmpdata();

        apiViewModel = new ViewModelProvider(SlotsActivity.this).get(ApiViewModel.class);

        apiViewModel.getuserslotdetails(getApplicationContext(), id, sharedPreferences1.getString("company_id", null));
        ViewController.ShowProgressBar(SlotsActivity.this);


        broadcastReceiver = new ConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)) {
                    activitySlotsBinding.relativeInternet.getRoot().setVisibility(GONE);
                    activitySlotsBinding.relativeUi.setVisibility(View.VISIBLE);
                } else {
                    activitySlotsBinding.relativeInternet.getRoot().setVisibility(View.VISIBLE);
                    activitySlotsBinding.relativeUi.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();


        apiViewModel.getuserslotdetails_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                ViewController.DismissProgressBar();;
                companyData_model = response.getItems();

                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {


                        //create date and time
                        long longtime = (Long.parseLong(String.valueOf(Long.parseLong(companyData_model.getCreated_time().get$numberLong())  * 1000)));
                        activitySlotsBinding.txtCDateTime.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime));


                        activitySlotsBinding.txtName.setText(companyData_model.getUserDetails().getName());
                        activitySlotsBinding.txtDesignation.setText(companyData_model.getUserDetails().getDesignation());
                        activitySlotsBinding.txtEmail.setText(companyData_model.getUserDetails().getEmail());
                        activitySlotsBinding.txtPhone.setText(companyData_model.getUserDetails().getMobile());
                        activitySlotsBinding.txtOriganization.setText(companyData_model.getUserDetails().getCompany());
                        activitySlotsBinding.txtCategory.setText(companyData_model.getCategoryDetails().getName());
                        activitySlotsBinding.txtPurpose.setText(companyData_model.getPurposeDetails().getName());


                        DateFormat simple = new SimpleDateFormat("E, dd MMM");
                        Date result = new Date((companyData_model.getStart() + Conversions.timezone()) * 1000);
                        String time = simple.format(result) + "";
                        activitySlotsBinding.txtDate.setText(time);


                        String s_time = Conversions.millitotime((companyData_model.getStart() + Conversions.timezone()) * 1000, false);
                        String e_time = Conversions.millitotime((companyData_model.getEnd()+1 + Conversions.timezone()) * 1000, false);
                        activitySlotsBinding.txtTime.setText(s_time +" - "+e_time);


//                        //set data
//                        if (response.getItems().getHistory().get(0).getLivepic() != null && response.getItems().getHistory().get(0).getLivepic().size() != 0) {
//                            Glide.with(getApplicationContext()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + response.getItems().getHistory().get(0).getLivepic().get(0))
//                                    .into(activitySlotsBinding.pic);
//                        }


                    }
                }

            }
        });


    }

    protected void registoreNetWorkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }


}