package com.provizit.Logins;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.provizit.Activities.NavigationActivity;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.provizit.Services.Model1;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;


import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    Button login;
    DatabaseHelper myDb;
    EditText userid, password;
    SharedPreferences sharedPreferences1;
    String usertype, regexStr, emailPattern, company_id;
    int status;
    ProgressDialog progress;
    CountryCodePicker ccp;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
        progress = new ProgressDialog(Login.this);
        company_id = sharedPreferences1.getString("company_id", null);
        myDb = new DatabaseHelper(this);
        login = findViewById(R.id.login);
        userid = findViewById(R.id.userid);
        password = findViewById(R.id.password);
        logo  = findViewById(R.id.logo);
        regexStr = "^[0-9]*$";
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
//        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!DatabaseHelper.companyImg.equals("")){
            Glide.with(Login.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + DatabaseHelper.companyImg)
                    .into(logo);
        }

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (userid.getText().toString().equals("")) {
                    userid.setError("required");
                    userid.requestFocus();
                } else {
                    if (userid.getText().toString().trim().matches(regexStr)) {
                        usertype = "mobile";
                        checkCustomer(ccp.getSelectedCountryCode()+ userid.getText().toString().trim());
                    } else {
                        usertype = "email";
                        checkCustomer(userid.getText().toString().trim());
                    }
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (company_id == null) {

                } else if (userid.getText().toString().equals("")) {

                } else if (password.getText().toString().equals("")) {

                } else {
                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("id", company_id);
                        jsonObj_.put("type", usertype);
                        jsonObj_.put("val", userid.getText().toString().trim());
                        if(usertype.equals("mobile")){
                            jsonObj_.put("val", ccp.getSelectedCountryCodeWithPlus()+userid.getText().toString());
                        }
                        jsonObj_.put("password", password.getText().toString());
                        jsonObj_.put("mverify", "0");
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Login(gsonObject);
                    progress.show();
                }
            }
        });
    }

    private void checkCustomer(String value) {
        System.out.println("asfjhjkashfjkhasjkhf "+value);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.checkuser(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                final Model1 model = response.body();
                progress.dismiss();

                if (model != null) {
//                   approve_btn.setEnabled(true);
                    Integer statuscode = model.getResult();
                    Integer successcode = 400, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                        userid.setError("Username not registered");
                        userid.requestFocus();
//                        progressBar.setVisibility(View.GONE);

                    } else if (statuscode.equals(not_verified)) {
//                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(Login.this, " username exit", Toast.LENGTH_SHORT).show();
                        userid.setError("username not Verified");
                        userid.requestFocus();


                    } else if (statuscode.equals(successcode)) {


                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                progress.dismiss();
//                                            progressDialog.dismiss();
                System.out.println(t + "subhash");
                Toast.makeText(Login.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();

            }
        },Login.this, sharedPreferences1.getString("company_id", null), usertype,value);


    }
    private void Login(JsonObject jsonObject) {

        DataManger dataManager = DataManger.getDataManager();
        dataManager.userLogin(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                final Model model = response.body();
//                                            progressDialog.dismiss();

                progress.dismiss();
                if (model != null) {
//                                                approve_btn.setEnabled(true);
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {
//                        progressBar.setVisibility(View.GONE);


                    } else if (statuscode.equals(successcode)) {
                        System.out.println("asfasf" + model.result);

                        CompanyData items = new CompanyData();
                        items = model.getItems();
                        if(items.getRoleDetails().getMeeting().equals("true") ||items.getRoleDetails().getApprover().equals("true")){
                        EmpData empData = new EmpData();
                        items = model.getItems();
                        String id = items.getEmp_id();
                        empData = items.getEmpData();
                        boolean b1 = myDb.insertEmp(id, empData);
                        boolean b3 = myDb.insertRole(items.getRoleDetails());
                        System.out.println("location" + empData.getLocation());
                        boolean b2 = myDb.insertTokenDetails(usertype, userid.getText().toString(), password.getText().toString(), 1);
                        System.out.println("subhash" + b1 + b2);
                        System.out.println("empdata" + myDb.getEmpdata().getEmp_id());
                        System.out.println("Roles" + myDb.getRole().getName());
                        Intent intent = new Intent(Login.this, NavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.enter, R.anim.exit);

                    }
                    else {
                      AlertDialog alertDialog =  new AlertDialog.Builder(Login.this)
                                .setTitle("ACCESS DENIED")
                                .setMessage("You do not have access")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
//                                            progressDialog.dismiss();
                System.out.println(t + "subhash");
                Toast.makeText(Login.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();

            }
        }, Login.this,jsonObject);

//
    }

}
