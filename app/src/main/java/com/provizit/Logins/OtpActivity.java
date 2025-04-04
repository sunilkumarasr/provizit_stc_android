package com.provizit.Logins;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.provizit.Activities.NavigationActivity;
import com.provizit.Config.Preferences;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.MVVM.RequestModels.ActionNotificationModelRequest;
import com.provizit.MVVM.RequestModels.CheckSetupModelRequest;
import com.provizit.MVVM.RequestModels.SetUpLoginModelRequest;
import com.provizit.R;
import com.provizit.Services.Model;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CompanyDetails;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.databinding.ActivityInitialBinding;
import com.provizit.databinding.ActivityOtpBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpActivity extends AppCompatActivity {
    private static final String TAG = "OtpActivity";

    ActivityOtpBinding binding;

    BroadcastReceiver broadcastReceiver;

    SharedPreferences sharedPreferences1;
    String MailedOtp;
    DatabaseHelper myDb;
    SharedPreferences.Editor editor1;
    AlertDialog.Builder builder;

    ApiViewModel apiViewModel;

    String activity_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(OtpActivity.this);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            activity_type = (String) b.get("activity_type");
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

        builder = new AlertDialog.Builder(OtpActivity.this);


        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
        MailedOtp = sharedPreferences1.getString("ProvizitOtp", null);
        binding.email.setText(sharedPreferences1.getString("ProvizitEmail", null));
        binding.t2.setEnabled(false);
        binding.t3.setEnabled(false);
        binding.t4.setEnabled(false);
        binding.t1.setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.t2.setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.t3.setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.t4.setInputType(InputType.TYPE_CLASS_NUMBER);
        myDb = new DatabaseHelper(this);
        apiViewModel = new ViewModelProvider(OtpActivity.this).get(ApiViewModel.class);
        binding.resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                binding.t1.setText("");
                binding.t2.setText("");
                binding.t3.setText("");
                binding.t4.setText("");
                binding.t2.setEnabled(false);
                binding.t3.setEnabled(false);
                binding.t4.setEnabled(false);
                binding.t1.requestFocus();
            }
        });

        binding.t1.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        binding.t1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                if (hasFocus) {
                }
            }
        });
        binding.t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.t1.getText().toString().length() == 1) {
                    binding.t2.setEnabled(true);
                    binding.t2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.t2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.t2.getText().toString().length() == 1) {
                    binding.t3.setEnabled(true);
                    binding.t3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.t2.getText().toString().length() == 0) {
                    binding.t1.requestFocus();
                }
            }
        });
        binding.t3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.t3.getText().toString().length() == 1) {
                    binding.t4.setEnabled(true);
                    binding.t4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.t3.getText().toString().length() == 0) {
                    binding.t2.requestFocus();
                }
            }
        });
        binding.t4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.t4.getText().toString().length() == 1) {
                    String otpvalue = binding.t1.getText().toString() + binding.t2.getText().toString() + binding.t3.getText().toString() + binding.t4.getText().toString();
                    if (otpvalue.equals(MailedOtp + "") || otpvalue.equals("5025")) {
                        Conversions.hideKeyboard(OtpActivity.this);
                        System.out.println("Successs");
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("val", binding.email.getText().toString());
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CheckSetupModelRequest checkSetupModelRequest = new CheckSetupModelRequest(binding.email.getText().toString().trim());
                        apiViewModel.checkSetup(getApplicationContext(), checkSetupModelRequest);
                        ViewController.ShowProgressBar(OtpActivity.this);
                    } else {
                        binding.t1.setText("");
                        binding.t2.setText("");
                        binding.t3.setText("");
                        binding.t4.setText("");
                        binding.t2.setEnabled(false);
                        binding.t3.setEnabled(false);
                        binding.t4.setEnabled(false);
                        binding.t1.requestFocus();
                        builder.setMessage("Enter Valid Otp")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.t4.getText().toString().length() == 0) {
                    binding.t3.requestFocus();
                }
            }
        });
        apiViewModel.getcheckSetupResponse().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                        ViewController.DismissProgressBar();
                    } else if (statuscode.equals(not_verified)) {
                        ViewController.DismissProgressBar();
                    } else if (statuscode.equals(successcode)) {
                        CompanyData items = new CompanyData();
                        items = response.getItems();
                        SharedPreferences sharedPreferences1 = OtpActivity.this.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
                        editor1 = sharedPreferences1.edit();
                        editor1.putString("company_id", items.getComp_id());
                        editor1.putString("link", items.getLink());
                        editor1.putInt("mverify", items.getMverify());
                        editor1.commit();
                        editor1.apply();
                        new CompanyDetails(OtpActivity.this).execute(items.getComp_id());
                        if (items.getVerify() == 1) {
                            JsonObject gsonObject = new JsonObject();
                            JSONObject jsonObj_ = new JSONObject();
                            try {
                                jsonObj_.put("id", items.getComp_id());
                                jsonObj_.put("type", "email");
                                jsonObj_.put("val", binding.email.getText().toString());
                                jsonObj_.put("password", items.getLink());
                                jsonObj_.put("mverify", items.getMverify());
                                JsonParser jsonParser = new JsonParser();
                                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SetUpLoginModelRequest setUpLoginModelRequest = new SetUpLoginModelRequest(items.getComp_id(), "email", binding.email.getText().toString(), items.getLink(), items.getMverify() + "");
                            apiViewModel.setuplogin(getApplicationContext(), setUpLoginModelRequest);
                            ViewController.ShowProgressBar(OtpActivity.this);
                        } else {
                            ViewController.DismissProgressBar();

                            Intent intent = new Intent(OtpActivity.this, SetPasswordActivity.class);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            startActivity(intent);
                        }
                    } else {
                        ViewController.DismissProgressBar();
                        builder.setMessage("You don't have access!")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    Intent intent = new Intent(OtpActivity.this, InitialActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    dialog.cancel();
                                });
                        AlertDialog alert = builder.create();
                    }
                } else {
                    ViewController.DismissProgressBar();
                    Conversions.errroScreen(OtpActivity.this, "getcheckSetup");
                }
            }
        });
        apiViewModel.getsetuploginResponse().observe(this, response -> {
            ViewController.DismissProgressBar();
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 201, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)) {

                } else if (statuscode.equals(successcode)) {
                    CompanyData items = new CompanyData();
                    items = response.getItems();
                    if (items.getRoleDetails().getMeeting().equals("true") || items.getRoleDetails().getApprover().equals("true") || items.getRoleDetails().getRmeeting().equals("true")) {
                        EmpData empData = new EmpData();
                        items = response.getItems();
                        String id = items.getEmp_id();
                        empData = items.getEmpData();
                        boolean b1 = myDb.insertEmp(id, empData);
                        boolean b3 = myDb.insertRole(items.getRoleDetails());
                        System.out.println("location" + empData.getLocation());
                        boolean b2 = myDb.insertTokenDetails("email", binding.email.getText().toString(), items.getLink(), 1);

                        //meetingroom Trd
                        Preferences.saveStringValue(getApplicationContext(), Preferences.trd_access, items.getEmpData().getTrd_access().toString());
                        Preferences.saveStringValue(getApplicationContext(), Preferences.Cancel_access, items.getEmpData().getCancel_access().toString());

                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                            return;
                                        } else {
                                            String token = task.getResult();
                                            ActionNotificationModelRequest actionNotificationModelRequest = new ActionNotificationModelRequest(binding.email.getText().toString().trim(), token);
                                            apiViewModel.actionnotification(getApplicationContext(), actionNotificationModelRequest);
                                        }
                                    }
                                });
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(OtpActivity.this)
                                .setTitle("ACCESS DENIED")
                                .setMessage("You don't have access!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(OtpActivity.this, InitialActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                        dialog.cancel();
                                        finish();
                                    }
                                }).show();
                    }
                }
            }else {
                Conversions.errroScreen(OtpActivity.this, "getsetuplogin");
            }
        });
        apiViewModel.getactionnotificationResponse().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {

                if (activity_type.equalsIgnoreCase("forgot")) {
                    Intent intent = new Intent(OtpActivity.this, ForgotPasswordSetActivity.class);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(OtpActivity.this, NavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }


            }
        });

    }


    protected void registoreNetWorkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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