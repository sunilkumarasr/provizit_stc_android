package com.provizit.Logins;

import static android.view.View.GONE;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;
import com.provizit.AESUtil;
import com.provizit.Activities.NavigationActivity;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.MVVM.RequestModels.ActionNotificationModelRequest;
import com.provizit.MVVM.RequestModels.CheckSetupModelRequest;
import com.provizit.MVVM.RequestModels.OtpSendEmaiModelRequest;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CompanyDetails;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.RoleDetails;
import com.provizit.databinding.ActivityLoginMicrosoftAdactivityBinding;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginMicrosoftADActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginMicrosoftAdactivityBinding binding;
    BroadcastReceiver broadcastReceiver;
    AlertDialog.Builder builder;
    DatabaseHelper myDb;
    SharedPreferences.Editor editor1;
    int otp;
    AESUtil aesUtil;
    ApiViewModel apiViewModel;


    String AUTHORITY = "https://login.microsoftonline.com/c6416dc9-4961-4429-a1bc-1c1bfee7f846";
    String REDIRECT_URI = "msauth://com.provizit.ksa/UxP3IRhPTbETz7%2FC8mdQfs5RtS4%3D";
    String CLIENT_ID = "3e4a6142-7057-4e08-a278-114688ab51ef";
    String Company_ID = "";

    String username = "";
    String LoginType = "";

    String company_id= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(LoginMicrosoftADActivity.this);
        binding = ActivityLoginMicrosoftAdactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences1 = LoginMicrosoftADActivity.this.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
        editor1 = sharedPreferences1.edit();
        myDb = new DatabaseHelper(this);
        builder = new AlertDialog.Builder(LoginMicrosoftADActivity.this);
        aesUtil = new AESUtil(LoginMicrosoftADActivity.this);
        apiViewModel = new ViewModelProvider(LoginMicrosoftADActivity.this).get(ApiViewModel.class);


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

        apiViewModel.getcheckSetupResponse().observe(this, response -> {
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 201, not_verified = 404;

                if (statuscode.equals(failurecode)) {
                    ViewController.DismissProgressBar();
                    builder.setTitle("Warning")
                            .setMessage("Email not registered")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (statuscode.equals(not_verified)) {
                    ViewController.DismissProgressBar();
                    builder.setTitle("Warning")
                            .setMessage("Login Failed")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (statuscode.equals(successcode)) {

                    CompanyData items = new CompanyData();
                    items = response.getItems();

                    company_id = items.getCid();
                    editor1 = sharedPreferences1.edit();
                    editor1.putString("company_id", items.getComp_id());
                    editor1.putString("link", items.getLink());
                    editor1.putInt("mverify", items.getMverify());
                    editor1.putString("ProvizitEmail", binding.editEmail.getText().toString());
                    editor1.commit();
                    editor1.apply();

                     if(items.isAzure() == false && items.getVerify() == 0){
                         ViewController.DismissProgressBar();

                         Intent intent = new Intent(LoginMicrosoftADActivity.this, SetPasswordActivity.class);
                         overridePendingTransition(R.anim.enter, R.anim.exit);
                         startActivity(intent);

                    }
                    else if (items.isAzure()) {
                         ViewController.DismissProgressBar();
                        LoginType = "Azure";
                        microsoft_ad( items.getClientid(), items.getTenantid(), items.getClientsecret());
                    }else if (items.isTwofa()) {
                         ViewController.DismissProgressBar();
                        Intent intent = new Intent(LoginMicrosoftADActivity.this, InitialActivity.class);
                        intent.putExtra("email",binding.editEmail.getText().toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }else {
                        //otp screen
                        otp_send();
                        LoginType = "OTP";
                    }

                }else {
                    ViewController.DismissProgressBar();
                }
            } else {
                ViewController.DismissProgressBar();
                builder.setTitle("Warning")
                        .setMessage("Login Failed")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        apiViewModel.getuserADloginResponse().observe(this, response -> {
            if (response != null) {
                try {
                    if (LoginType.equalsIgnoreCase("Azure")){

                        CompanyData items = new CompanyData();
                        items = response.getItems();

                        if (items.getRoleDetails().getMeeting().equals("true") || items.getRoleDetails().getApprover().equals("true") || items.getRoleDetails().getRmeeting().equals("true")) {
                            SharedPreferences sharedPreferences11 = LoginMicrosoftADActivity.this.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
                            editor1 = sharedPreferences11.edit();
                            editor1.putString("company_id", Company_ID);
                            editor1.putString("link", items.getLink());
                            editor1.putInt("mverify", items.getMverify());
                            editor1.commit();
                            editor1.apply();
                            new CompanyDetails(LoginMicrosoftADActivity.this).execute(items.getComp_id());
                            if (items.getVerify() == 1) {

                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(task -> {
                                            if (!task.isSuccessful()) {
                                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                                return;
                                            } else {
                                                String token = task.getResult();
                                                ActionNotificationModelRequest actionNotificationModelRequest = new ActionNotificationModelRequest(username.trim(), token);
                                                apiViewModel.actionnotification(getApplicationContext(), actionNotificationModelRequest);
                                            }
                                        });

                                EmpData empData = new EmpData();
                                items = response.getItems();
                                String id = items.getEmp_id();
                                empData = items.getEmpData();
                                boolean b1 = myDb.insertEmp(id, empData);
                                boolean b3 = myDb.insertRole(items.getRoleDetails());
                                boolean b2 = myDb.insertTokenDetails("email", username.trim(), items.getLink(), 1);

                            }else {
                                ViewController.DismissProgressBar();
                            }
                        }else {
                            AlertDialog alertDialog = new AlertDialog.Builder(LoginMicrosoftADActivity.this)
                                    .setTitle("ACCESS DENIED")
                                    .setMessage("You don't have access!")
                                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                        Intent intent = new Intent(LoginMicrosoftADActivity.this, InitialActivity.class);
                                        intent.putExtra("email",binding.editEmail.getText().toString());
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                        dialog.cancel();
                                        finish();
                                    }).show();
                        }


                    }else if (LoginType.equalsIgnoreCase("OTP")){

                        otp = Conversions.getNDigitRandomNumber(4);
                        editor1.putString("ProvizitOtp", otp + "");
                        editor1.putString("ProvizitEmail", binding.editEmail.getText().toString().trim());
                        editor1.commit();
                        editor1.apply();
                        //otp send api
                        OtpSendEmaiModelRequest otpSendEmaiModelRequest = new OtpSendEmaiModelRequest(binding.editEmail.getText().toString().trim(), binding.editEmail.getText().toString().trim(), aesUtil.encrypt(otp + "", "egems_2013_grms_2017_provizit_2020"), aesUtil.encrypt(otp + "", binding.editEmail.getText().toString().trim()));
                        apiViewModel.otpsendemail(getApplicationContext(), otpSendEmaiModelRequest);

                    }else {
                        ViewController.DismissProgressBar();
                        builder.setTitle("Warning")
                                .setMessage("Login Failed")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }catch (IndexOutOfBoundsException e) {
                    Log.e("error",e.getMessage());
                    ViewController.DismissProgressBar();
                    builder.setTitle("Warning")
                            .setMessage("Login Failed")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } else {
                ViewController.DismissProgressBar();
                builder.setTitle("Warning")
                        .setMessage("Login Failed")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        apiViewModel.getactionnotificationResponse().observe(this, response -> {
            ViewController.DismissProgressBar();
            Intent intent = new Intent(LoginMicrosoftADActivity.this, NavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        });

        apiViewModel.getotpsendemailResponse().observe(this, response -> {
            ViewController.DismissProgressBar();
            if (response != null) {
                Intent intent = new Intent(LoginMicrosoftADActivity.this, OtpActivity.class);
                intent.putExtra("activity_type", "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
                startActivity(intent);
            }
        });

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(10000);
                } catch (Exception e) {
                } finally {
                    ViewController.DismissProgressBar();
                }
            }
        };
        t.start();


        binding.next.setOnClickListener(this);
    }

    protected void registoreNetWorkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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
                ViewController.ShowProgressBar(LoginMicrosoftADActivity.this);
            } else {
                binding.emailTextinput.setErrorEnabled(true);
                binding.emailTextinput.setError("Invalid Email");
            }
        }
    }


    private void microsoft_ad( String clientid, String tenantid, String clientsecret) {

        Company_ID = company_id;
        AUTHORITY = "https://login.microsoftonline.com/" + tenantid;
        REDIRECT_URI = "msauth://com.provizit.ksa/UxP3IRhPTbETz7%2FC8mdQfs5RtS4%3D";
        CLIENT_ID = clientid;

        // Create PublicClientApplication instance
        PublicClientApplication.create(
                getApplicationContext(),
                CLIENT_ID,
                AUTHORITY,
                REDIRECT_URI,
                new IPublicClientApplication.ApplicationCreatedListener() {
                    @Override
                    public void onCreated(IPublicClientApplication application) {

                        // Handle application creation success

                        String[] scopes = {"User.Read", "Mail.Read"};

                        application.acquireToken(LoginMicrosoftADActivity.this, scopes, new AuthenticationCallback() {
                            @Override
                            public void onSuccess(IAuthenticationResult authenticationResult) {
                                IAccount account = authenticationResult.getAccount();

                                if (account != null) {
                                    // Get the username from the account
                                    username = account.getUsername().toLowerCase();
                                    String id = account.getId();

                                    Log.e("username_",username);
                                    Log.e("id_",id);

                                    JsonObject gsonObject = new JsonObject();
                                    JSONObject jsonObj_ = new JSONObject();
                                    try {
                                        jsonObj_.put("id", company_id);
                                        jsonObj_.put("val", "provizit@burgerizzr.com");
                                        String encryptPWD = DataManger.Pwd_encrypt(getApplicationContext(), id, username.trim());
                                        jsonObj_.put("adval", encryptPWD);
                                        JsonParser jsonParser = new JsonParser();
                                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    apiViewModel.userADlogin(getApplicationContext(), gsonObject);
                                    ViewController.ShowProgressBar(LoginMicrosoftADActivity.this);
                                }
                            }

                            @Override
                            public void onError(MsalException exception) {
                                // Handle authentication error
                                exception.printStackTrace();
                                Log.e("errorAD_", exception+"");
                            }

                            @Override
                            public void onCancel() {
                                // Handle user cancellation
                            }

                        });
                    }

                    @Override
                    public void onError(MsalException exception) {
                        // Handle application creation error
                        exception.printStackTrace();
                        Log.e("errorAD_e", exception+"");
                    }
                }
        );



    }


    private void otp_send() {

        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        try {
            jsonObj_.put("id", company_id);
            jsonObj_.put("val", binding.editEmail.getText().toString().trim());
            String encryptPWD = DataManger.Pwd_encrypt(getApplicationContext(), otp + "", binding.editEmail.getText().toString().trim());
            jsonObj_.put("adval", encryptPWD);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiViewModel.userADlogin(getApplicationContext(), gsonObject);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}