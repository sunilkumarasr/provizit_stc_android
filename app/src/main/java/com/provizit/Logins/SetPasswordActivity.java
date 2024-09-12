package com.provizit.Logins;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.provizit.AESUtil;
import com.provizit.Activities.NavigationActivity;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.MVVM.RequestModels.ActionNotificationModelRequest;
import com.provizit.MVVM.RequestModels.SetUpLoginModelRequest;
import com.provizit.MVVM.RequestModels.UpdatePwdModelRequest;
import com.provizit.R;
import com.provizit.Services.Model;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.Utilities.RoleDetails;
import com.provizit.databinding.ActivityForgotPasswordSetBinding;
import com.provizit.databinding.ActivitySetPasswordBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "SetPasswordActivity";


    ActivitySetPasswordBinding binding;
    BroadcastReceiver broadcastReceiver;

    SharedPreferences sharedPreferences1;
    Boolean eyeStatus = false;
    DatabaseHelper myDb;
    AESUtil aesUtil;

    ApiViewModel apiViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);

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


        aesUtil = new AESUtil(SetPasswordActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        myDb = new DatabaseHelper(this);
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        apiViewModel = new ViewModelProvider(SetPasswordActivity.this).get(ApiViewModel.class);
        binding.ePassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding.cPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding.cPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.cPtl.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.ePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.cPtl.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
        binding.email.setText(sharedPreferences1.getString("ProvizitEmail", null));
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);

                if (binding.ePassword.getText().toString().length() >= 8){
                    binding.pPtl.setErrorEnabled(false);

                    if(!ViewController.isValidPassword(binding.ePassword.getText().toString())){
                        binding.pPtl.setErrorEnabled(true);
                        binding.pPtl.setError("Password is too weak");
                    }else{
                        binding.pPtl.setErrorEnabled(false);
                        if(binding.ePassword.getText().toString().equals(binding.cPassword.getText().toString())){
                            JsonObject gsonObject = new JsonObject();
                            JSONObject jsonObj_ = new JSONObject();
                            try {
                                jsonObj_.put("comp_id",sharedPreferences1.getString("company_id", null));
                                jsonObj_.put("pwd",aesUtil.encrypt(binding.cPassword.getText().toString(),binding.email.getText().toString().trim()));
                                jsonObj_.put("link", sharedPreferences1.getString("link", null));
                                JsonParser jsonParser = new JsonParser();
                                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            UpdatePwdModelRequest updatePwdModelRequest = new UpdatePwdModelRequest(sharedPreferences1.getString("company_id", null),aesUtil.encrypt(binding.cPassword.getText().toString(),binding.email.getText().toString().trim()),sharedPreferences1.getString("link", null));
                            apiViewModel.updatepwd(getApplicationContext(),updatePwdModelRequest);
                            ViewController.ShowProgressBar(SetPasswordActivity.this);
                        }
                        else{
                            binding.cPtl.setErrorEnabled(true);
                            binding.cPtl.setError("Password not matched");
                        }
                    }
                }else {
                    binding.pPtl.setErrorEnabled(true);
                    binding.pPtl.setError("Password minimum length 8 characters");
                }

            }
        });

        binding.cPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.cPassword.getRight() - binding.cPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(!eyeStatus){
                            binding.cPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                            binding.cPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_awesome_unlock, 0, R.drawable.ic_invisible, 0);
                            eyeStatus = true;
                        }
                        else{
                            eyeStatus = false;
                            binding.cPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_awesome_unlock, 0, R.drawable.ic_visibility_button, 0);
                            binding.cPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                        return true;
                    }
                }
                return false;
            }
        });



        apiViewModel.getupdatepwdResponse().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                ViewController.DismissProgressBar();
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("id", sharedPreferences1.getString("company_id", null));
                            jsonObj_.put("type", "email");
                            jsonObj_.put("val",binding.email.getText().toString());
                            jsonObj_.put("password",sharedPreferences1.getString("link", null));
                            jsonObj_.put("mverify", sharedPreferences1.getInt("mverify", 0));
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SetUpLoginModelRequest setUpLoginModelRequest = new SetUpLoginModelRequest(sharedPreferences1.getString("company_id", null),"email",binding.email.getText().toString(),sharedPreferences1.getString("link", null),sharedPreferences1.getInt("mverify", 0)+"");
                        apiViewModel.setuplogin(getApplicationContext(),setUpLoginModelRequest);
                    }
                }else {
                    Conversions.errroScreen(SetPasswordActivity.this, "getupdatepwd");
                }
            }
        });

        apiViewModel.getsetuploginResponse().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                ViewController.DismissProgressBar();
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        CompanyData items = new CompanyData();
                        items = response.getItems();
                        if(items.getRoleDetails().getMeeting().equals("true") ||items.getRoleDetails().getApprover().equals("true") || items.getRoleDetails().getRmeeting().equals("true")){
                            EmpData empData = new EmpData();
                            items = response.getItems();
                            String id = items.getEmp_id();
                            empData = items.getEmpData();
                            boolean b1 = myDb.insertEmp(id, empData);
                            boolean b3 = myDb.insertRole(items.getRoleDetails());
                            System.out.println("location" + empData.getLocation());
                            boolean b2 = myDb.insertTokenDetails("email", binding.email.getText().toString(), items.getLink(), 1);
                            System.out.println("empdata" + myDb.getEmpdata().getEmp_id());
                            System.out.println("Roles" + myDb.getRole().getName());

                            //firebase token send to server
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                        return;
                                    } else {
                                        String token = task.getResult();
                                        ActionNotificationModelRequest actionNotificationModelRequest = new ActionNotificationModelRequest(binding.email.getText().toString().trim(),token);
                                        apiViewModel.actionnotification(getApplicationContext(),actionNotificationModelRequest);
                                    }
                                }
                            });
                        }
                        else {
                            AlertDialog alertDialog =  new AlertDialog.Builder(SetPasswordActivity.this)
                                    .setTitle("ACCESS DENIED")
                                    .setMessage("You don't have access!")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(SetPasswordActivity.this, InitialActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.enter, R.anim.exit);
                                            dialog.cancel();
                                        }
                            }).show();
                        }
                    }
                }else {
                    Conversions.errroScreen(SetPasswordActivity.this, "getsetuplogin");
                }
            }
        });

        apiViewModel.getactionnotificationResponse().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                ViewController.DismissProgressBar();
                Intent intent = new Intent(SetPasswordActivity.this, NavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        });
    }

    protected void registoreNetWorkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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