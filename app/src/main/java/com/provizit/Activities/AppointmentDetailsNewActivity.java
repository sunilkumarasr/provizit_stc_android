package com.provizit.Activities;

import static android.view.View.GONE;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.Logins.ForgotActivity;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Services.AppointmentDetails.AppointmentDetailsModel;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.provizit.Services.Model1;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CustomAdapter;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.databinding.ActivityAppointmentDetailsNewBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AppointmentDetailsNewActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "AppointmentDetailsNewAc";


    ActivityAppointmentDetailsNewBinding activityAppointmentDetailsNewBinding;

    public static final int RESULT_CODE = 12;

    BroadcastReceiver broadcastReceiver;

    Dialog dialog;

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;

    AppointmentDetailsModel model;

    //Department
    ArrayList<CompanyData> departments;
    CustomAdapter departmentAdapter;
    AutoCompleteTextView department_spinner;
    //Employees;
    ArrayList<CompanyData> employees;
    AutoCompleteTextView emp_spinner;
    CustomAdapter employeeAdapter;
    String hierarchy_indexid,hierarchy_id ,host;

    String m_id = "";

    ApiViewModel apiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(AppointmentDetailsNewActivity.this);
        activityAppointmentDetailsNewBinding = ActivityAppointmentDetailsNewBinding.inflate(getLayoutInflater());
        setContentView(activityAppointmentDetailsNewBinding.getRoot());
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            m_id = b.getString("m_id");
        }

        //db
        sharedPreferences1 = getApplicationContext().getSharedPreferences("EGEMSS_DATA", 0);
        myDb = new DatabaseHelper(AppointmentDetailsNewActivity.this);
        empData = myDb.getEmpdata();


        apiViewModel = new ViewModelProvider(AppointmentDetailsNewActivity.this).get(ApiViewModel.class);


        apiViewModel.getappointmentsdetails(getApplicationContext(),m_id);
        ViewController.ShowProgressBar(AppointmentDetailsNewActivity.this);
        //department and employ
        apiViewModel.getsubhierarchys(getApplicationContext(),empData.getLocation());


        //internet connection
        broadcastReceiver = new ConnectionReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)){
                    activityAppointmentDetailsNewBinding.relativeInternet.getRoot().setVisibility(GONE);
                    activityAppointmentDetailsNewBinding.relativeUi.setVisibility(View.VISIBLE);
                }else {
                    activityAppointmentDetailsNewBinding.relativeInternet.getRoot().setVisibility(View.VISIBLE);
                    activityAppointmentDetailsNewBinding.relativeUi.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();


        apiViewModel.getappointmentsdetails_response().observe(this, new Observer<AppointmentDetailsModel>() {
            @Override
            public void onChanged(AppointmentDetailsModel response) {
                ViewController.DismissProgressBar();
                if (response != null) {
                    model = response;
                    set_Data(response);
                }else {
                    Conversions.errroScreen(AppointmentDetailsNewActivity.this, "getappointmentsdetails");
                }
            }
        });
        apiViewModel.getsubhierarchys_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                if (response != null) {
                    departments = new ArrayList<>();
                    departments = response.getItems();
                }else {
                    Conversions.errroScreen(AppointmentDetailsNewActivity.this, "getsubhierarchys");
                }
            }
        });
        apiViewModel.getsearchemployees_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                ViewController.DismissProgressBar();
                if (response != null) {
                    employees = new ArrayList<>();
                    employees = response.getItems();
                    for(int i=0;i<employees.size();i++){
                        if(employees.get(i).getEmail().equals(empData.getEmail())){
                            employees.remove(employees.get(i));
                            break;
                        }
                    }
                    if(employees.size() == 0){
                        department_spinner.setText("");
                    }
                    else{
                        employeeAdapter = new CustomAdapter(getApplicationContext(), R.layout.row, R.id.lbl_name, employees,0,"");
                        emp_spinner.setThreshold(0);//will start working from first character
                        emp_spinner.setAdapter(employeeAdapter);//setting the adapter data into the AutoCompleteTextView
                        emp_spinner.setEnabled(true);
                    }
                }else {
                    Conversions.errroScreen(AppointmentDetailsNewActivity.this, "getsearchemployees");
                }
            }
        });

        apiViewModel.updateappointment_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                ViewController.DismissProgressBar();
                if (response != null) {
                    Intent intent = new Intent(AppointmentDetailsNewActivity.this, NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }else {
                    Conversions.errroScreen(AppointmentDetailsNewActivity.this, "updateappointment");
                }
            }
        });


        activityAppointmentDetailsNewBinding.imgBack.setOnClickListener(this);
        activityAppointmentDetailsNewBinding.linearAccept.setOnClickListener(this);
        activityAppointmentDetailsNewBinding.linearAssign.setOnClickListener(this);
        activityAppointmentDetailsNewBinding.linearDecline.setOnClickListener(this);
        activityAppointmentDetailsNewBinding.linearPdf.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                AnimationSet anima = Conversions.animation();
                v.startAnimation(anima);
                Intent intent2 = new Intent();
                setResult(RESULT_CODE, intent2);
                finish();
                break;
            case R.id.linear_accept:
                AnimationSet animation1 = Conversions.animation();
                v.startAnimation(animation1);
                Intent intent = new Intent(getApplicationContext(),SetupMeetingActivity.class);
                intent.putExtra("RMS_STATUS",3);
                intent.putExtra("m_id", model.items.get_id().get$oid());
                startActivity(intent);
                break;
            case R.id.linear_assign:
                AnimationSet animation3 = Conversions.animation();
                v.startAnimation(animation3);
                Assignpopup();
                break;
            case R.id.linear_decline:
                AnimationSet animation4 = Conversions.animation();
                v.startAnimation(animation4);
                String id = model.items.get_id().get$oid();
                String Emp_id = model.items.getEmp_id();
                declineAppointmentpopup(id,Emp_id);
                break;
            case R.id.linear_pdf:
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                int pdfSize = model.items.getPdfs().size();
                String pdf_url = DataManger.IMAGE_URL + "/uploads/"+sharedPreferences1.getString("company_id", null) + "/pdfs/"+ model.items.getPdfs().get(pdfSize-1).getValue();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                startActivity(browserIntent);
                break;
        }
    }

    protected void registoreNetWorkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void set_Data(AppointmentDetailsModel body) {
        long longtime = (Long.parseLong(body.items.getA_time().get$numberLong()) * 1000);
        Date d = new Date(longtime);
        String date = DateFormat.getDateTimeInstance().format(d);
        activityAppointmentDetailsNewBinding.txtDateTime.setText(date+"");

        activityAppointmentDetailsNewBinding.txtPurpose.setText(body.items.getPurpose()+"");
        activityAppointmentDetailsNewBinding.txtName.setText(body.items.getvData().getName()+"");
        activityAppointmentDetailsNewBinding.txtPhone.setText(body.items.getvData().getMobile()+"");
        activityAppointmentDetailsNewBinding.txtEmail.setText(body.items.getvData().getEmail()+"");

        if (body.items.getvData().getCompany().equalsIgnoreCase("")||body.items.getvData().getCompany()==null){
            activityAppointmentDetailsNewBinding.lineOriganization.setVisibility(GONE);
        }else {
            activityAppointmentDetailsNewBinding.txtOriganization.setText(body.items.getvData().getCompany()+"");
        }
        if (body.items.getNote()==null||body.items.getNote().equalsIgnoreCase("")){
            activityAppointmentDetailsNewBinding.linearNote.setVisibility(View.GONE);
        }else {
            activityAppointmentDetailsNewBinding.linearNote.setVisibility(View.VISIBLE);
            activityAppointmentDetailsNewBinding.txtNote.setText(body.items.getNote()+"");
        }

        if (body.items.getPdfs() !=null){
            int pdfSize = body.items.getPdfs().size();
            if (pdfSize == 0 ){
                activityAppointmentDetailsNewBinding.linearPdf.setVisibility(View.GONE);
            }else {
                activityAppointmentDetailsNewBinding.linearPdf.setVisibility(View.VISIBLE);
                activityAppointmentDetailsNewBinding.txtPdfName.setText(body.items.getPdfs().get(pdfSize-1).getName()+"");
            }
        }

    }

    //Assign
    private void Assignpopup() {
        dialog = new Dialog(AppointmentDetailsNewActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.assign_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView assign_btn,cancel;

        department_spinner = dialog.findViewById(R.id.department_spinner);
        emp_spinner = dialog.findViewById(R.id.emp_spinner);
        cancel = dialog.findViewById(R.id.cancel);
        emp_spinner.setInputType(InputType.TYPE_NULL);
        assign_btn = dialog.findViewById(R.id.assign);

        departmentAdapter = new CustomAdapter(getApplicationContext(), R.layout.row, R.id.lbl_name, departments,0,"");
        department_spinner.setInputType(InputType.TYPE_NULL);
        department_spinner.setThreshold(0);
        department_spinner.setAdapter(departmentAdapter);
        department_spinner.setEnabled(true);
        department_spinner.clearFocus();
        department_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                department_spinner.showDropDown();
            }
        });
        department_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //employ
                apiViewModel.getsearchemployees(getApplicationContext(),empData.getLocation(), departments.get(position).get_id().get$oid());
                dialog.show();
                hierarchy_id =  "";
                hierarchy_indexid =  "";
                host =  "";
            }
        });
        emp_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emp_spinner.showDropDown();
            }
        });
        emp_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hierarchy_id = employees.get(position).getHierarchy_id();
                hierarchy_indexid = employees.get(position).getHierarchy_indexid();
                host = employees.get(position).get_id().get$oid();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation4 = Conversions.animation();
                v.startAnimation(animation4);
                dialog.dismiss();
            }
        });
        assign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation4 = Conversions.animation();
                v.startAnimation(animation4);
                if(host!=null){
                    if(!host.equals("")){
                        //update appointment
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("formtype", "assign");
                            jsonObj_.put("id", model.items.get_id().get$oid());
                            jsonObj_.put("emp_id", host);
                            jsonObj_.put("coordinator", empData.getEmp_id());
                            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        apiViewModel.updateappointment(getApplicationContext(),gsonObject);
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //decline
    private void declineAppointmentpopup(String id,String Emp_id) {
        final Dialog dialog = new Dialog(AppointmentDetailsNewActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.otp_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView submit;
        final TextView cancel;
        final EditText reason;
        reason = dialog.findViewById(R.id.reason);
        submit = dialog.findViewById(R.id.submit);
        cancel = dialog.findViewById(R.id.cancel);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation4 = Conversions.animation();
                v.startAnimation(animation4);
                if (reason.getText().toString().equalsIgnoreCase("")){
                    reason.setError("Please Enter Your Reason");
                }else {
                    //update appointment
                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "decline");
                        jsonObj_.put("id", id);
                        jsonObj_.put("emp_id", Emp_id);
                        jsonObj_.put("reason", reason.getText().toString());
                        jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    apiViewModel.updateappointment(getApplicationContext(),gsonObject);
                    dialog.dismiss();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation4 = Conversions.animation();
                v.startAnimation(animation4);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent();
        setResult(RESULT_CODE, intent2);
        finish();
    }

}