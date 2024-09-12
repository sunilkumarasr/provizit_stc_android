package com.provizit.Logins;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.google.gson.Gson;
import com.provizit.AESUtil;
import com.provizit.Activities.PrivacyPolicyActivity;
import com.provizit.AdapterAndModel.ContactsList;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.MVVM.RequestModels.OtpSendEmaiModelRequest;
import com.provizit.Config.Preferences;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.databinding.ActivityInitialBinding;
import com.provizit.databinding.ActivitySplashBinding;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitialActivity extends AppCompatActivity {
    private static final String TAG = "InitialActivity";

    ActivityInitialBinding binding;
    BroadcastReceiver broadcastReceiver;

    int otp;
    SharedPreferences.Editor editor1;

    AlertDialog.Builder builder;
    AESUtil aesUtil;

    ApiViewModel apiViewModel;

    //contacts List
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    List<ContactsList> contactsLists;
    ContactsList contactsLi;

    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(InitialActivity.this);
        binding = ActivityInitialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            email = (String) b.get("email");
        }

        binding.editEmail.setText(email.trim());

        SharedPreferences sharedPreferences1 = InitialActivity.this.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
        builder = new AlertDialog.Builder(InitialActivity.this);
        aesUtil = new AESUtil(InitialActivity.this);

        // Request permission to read contacts
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        //internet connection
        broadcastReceiver = new ConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)) {
                    binding.relativeInternet.getRoot().setVisibility(GONE);
                    binding.relativeUi.setVisibility(View.VISIBLE);
                } else {
                    binding.relativeInternet.getRoot().setVisibility(View.VISIBLE);
                    binding.relativeUi.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();


        editor1 = sharedPreferences1.edit();

        apiViewModel = new ViewModelProvider(InitialActivity.this).get(ApiViewModel.class);

        binding.editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.emailTextinput.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkbox.setChecked(binding.checkbox.isChecked());
            } else {
                binding.checkbox.setChecked(false);
            }
        });

        binding.linearPrivacy.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);

            startActivity(new Intent(InitialActivity.this, PrivacyPolicyActivity.class));
            overridePendingTransition(R.anim.enter, R.anim.exit);

        });

        binding.forgotPassword.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);


            startActivity(new Intent(InitialActivity.this, ForgotActivity.class));
            overridePendingTransition(R.anim.enter, R.anim.exit);
        });

        binding.next.setOnClickListener(v -> {
            ViewController.hideKeyboard(InitialActivity.this);
//                STCPROAA02

            if (binding.editEmail.getText().toString().equalsIgnoreCase("")) {
                binding.emailTextinput.setErrorEnabled(true);
                binding.emailTextinput.setError("Enter email");
            } else if (binding.editPassword.getText().toString().equalsIgnoreCase("")) {
                binding.passwordSt.setErrorEnabled(true);
                binding.passwordSt.setError("Enter password");
            } else {
                if (binding.checkbox.isChecked()) {
                    if (ViewController.isEmailValid(binding.editEmail.getText().toString())) {
//                            JsonObject gsonObject = new JsonObject();
//                            JSONObject jsonObj_ = new JSONObject();
//                            try {
//                                jsonObj_.put("val", edit_email.getText().toString().trim());
//                                JsonParser jsonParser = new JsonParser();
//                                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            System.out.println("subhash1 "+gsonObject);
//                            //api mvvm
//                            CheckSetupModelRequest checkSetupModelRequest = new CheckSetupModelRequest(edit_email.getText().toString().trim());
//                            apiViewModel.checkSetup(getApplicationContext(),checkSetupModelRequest);

                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("id", "");
                            jsonObj_.put("mverify", 0);
                            jsonObj_.put("type", "email");
                            jsonObj_.put("val", binding.editEmail.getText().toString().trim());
                            String encryptPWD = DataManger.Pwd_encrypt(getApplicationContext(), binding.editPassword.getText().toString(), binding.editEmail.getText().toString().trim());
                            jsonObj_.put("password", encryptPWD);
                            Log.e(TAG, "onClick:password " + encryptPWD);
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        apiViewModel.appuserlogin(getApplicationContext(), gsonObject);

                        ViewController.ShowProgressBar(InitialActivity.this);
                        binding.relativeUi.setEnabled(false);
                    } else {
                        binding.emailTextinput.setErrorEnabled(true);
                        binding.emailTextinput.setError("Invalid Email");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Agree & Continue", Toast.LENGTH_SHORT).show();
                }
            }
        });

        apiViewModel.getappuserloginResponse().observe(this, response -> {
            binding.relativeUi.setEnabled(true);
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 201, not_verified = 404, internet_verified = 500;
                if (statuscode.equals(failurecode)) {
                    ViewController.DismissProgressBar();
                    builder.setTitle("Warning")
                            .setMessage("Wrong Password Entered")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
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
//                        JsonObject gsonObject = new JsonObject();
//                        JSONObject jsonObj_ = new JSONObject();
//                        try {
//                            jsonObj_.put("val", email.getText().toString().trim());
//                            jsonObj_.put("email", email.getText().toString().trim());
//                            jsonObj_.put("sotp", aesUtil.encrypt(otp + "", "egems_2013_grms_2017_provizit_2020"));
//                            jsonObj_.put("otp", aesUtil.encrypt(otp + "", email.getText().toString().trim()));
//                            JsonParser jsonParser = new JsonParser();
//                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    OtpSendEmaiModelRequest otpSendEmaiModelRequest = new OtpSendEmaiModelRequest(binding.editEmail.getText().toString().trim(), binding.editEmail.getText().toString().trim(), aesUtil.encrypt(otp + "", "egems_2013_grms_2017_provizit_2020"), aesUtil.encrypt(otp + "", binding.editEmail.getText().toString().trim()));
                    apiViewModel.otpsendemail(getApplicationContext(), otpSendEmaiModelRequest);
                    binding.relativeUi.setEnabled(false);
                }
            } else {
                ViewController.DismissProgressBar();
                Conversions.errroScreen(InitialActivity.this, "getappuserlogin");
            }
        });

        apiViewModel.getcheckSetupResponse().observe(this, response -> {
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
//                        JsonObject gsonObject = new JsonObject();
//                        JSONObject jsonObj_ = new JSONObject();
//                        try {
//                            jsonObj_.put("val", email.getText().toString().trim());
//                            jsonObj_.put("email", email.getText().toString().trim());
//                            jsonObj_.put("sotp", aesUtil.encrypt(otp + "", "egems_2013_grms_2017_provizit_2020"));
//                            jsonObj_.put("otp", aesUtil.encrypt(otp + "", email.getText().toString().trim()));
//                            JsonParser jsonParser = new JsonParser();
//                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    OtpSendEmaiModelRequest otpSendEmaiModelRequest = new OtpSendEmaiModelRequest(binding.editEmail.getText().toString().trim(), binding.editEmail.getText().toString().trim(), aesUtil.encrypt(otp + "", "egems_2013_grms_2017_provizit_2020"), aesUtil.encrypt(otp + "", binding.editEmail.getText().toString().trim()));
                    apiViewModel.otpsendemail(getApplicationContext(), otpSendEmaiModelRequest);
                    binding.relativeUi.setEnabled(false);
                }
            } else {
                ViewController.DismissProgressBar();
            }
        });

        apiViewModel.getotpsendemailResponse().observe(this, response -> {
            ViewController.DismissProgressBar();
            binding.relativeUi.setEnabled(true);
            if (response != null) {
                Intent intent = new Intent(InitialActivity.this, OtpActivity.class);
                intent.putExtra("activity_type", "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
                startActivity(intent);
            }else {
                Conversions.errroScreen(InitialActivity.this, "getotpsendemail");
            }
        });


    }

    protected void registoreNetWorkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }


}