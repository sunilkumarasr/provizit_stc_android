package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AppointmentDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AppointmentDetailsActiv";

    BroadcastReceiver broadcastReceiver;

    RelativeLayout relative_internet;
    RelativeLayout relative_ui;

    Dialog dialog;

    ProgressDialog progress;

    ImageView img_back;
    LinearLayout linear_accept,linear_assign,linear_decline;
    TextView txt_purpose,txt_date_time,txt_name,txt_phone,txt_email,txt_origanization,txt_note,txt_pdf_name;
    LinearLayout linear_note,linear_pdf;


    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;


    ArrayList<AppointmentDetailsModel> mylist0;
    ArrayList<AppointmentDetailsModel> mylist1;
    ArrayList<AppointmentDetailsModel> myList;

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
        setContentView(R.layout.activity_appointment_details);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            m_id = b.getString("m_id");
        }
        //db
        sharedPreferences1 = getApplicationContext().getSharedPreferences("EGEMSS_DATA", 0);
        myDb = new DatabaseHelper(AppointmentDetailsActivity.this);
        empData = myDb.getEmpdata();

        apiViewModel = new ViewModelProvider(AppointmentDetailsActivity.this).get(ApiViewModel.class);

        progress = new ProgressDialog(AppointmentDetailsActivity.this);

        //internet connection
        relative_internet = findViewById(R.id.relative_internet);
        relative_ui = findViewById(R.id.relative_ui);
        broadcastReceiver = new ConnectionReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)){
                    relative_internet.setVisibility(GONE);
                    relative_ui.setVisibility(View.VISIBLE);
                }else {
                    relative_internet.setVisibility(View.VISIBLE);
                    relative_ui.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();

        img_back = findViewById(R.id.img_back);
        linear_accept = findViewById(R.id.linear_accept);
        linear_assign = findViewById(R.id.linear_assign);
        linear_decline = findViewById(R.id.linear_decline);
        txt_date_time = findViewById(R.id.txt_date_time);
        txt_purpose = findViewById(R.id.txt_purpose);
        txt_name = findViewById(R.id.txt_name);
        txt_phone = findViewById(R.id.txt_phone);
        txt_email = findViewById(R.id.txt_email);
        txt_origanization = findViewById(R.id.txt_origanization);
        txt_note = findViewById(R.id.txt_note);
        linear_note = findViewById(R.id.linear_note);
        linear_pdf = findViewById(R.id.linear_pdf);
        txt_pdf_name = findViewById(R.id.txt_pdf_name);


        apiViewModel.getappointmentsdetails(getApplicationContext(),m_id);
        progress.show();
        //department and employ
        apiViewModel.getsubhierarchys(getApplicationContext(),empData.getLocation());

        myList = new ArrayList<>();

        apiViewModel.getappointmentsdetails_response().observe(this, new Observer<AppointmentDetailsModel>() {
            @Override
            public void onChanged(AppointmentDetailsModel response) {
                progress.dismiss();
                if (response != null) {
                    model = response;
                    set_Data(response);
                }
            }
        });
        apiViewModel.getsubhierarchys_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                if (response != null) {
                    departments = new ArrayList<>();
                    departments = response.getItems();
                }
            }
        });
        apiViewModel.getsearchemployees_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                progress.dismiss();
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
                }
            }
        });

        apiViewModel.updateappointment_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                progress.dismiss();
                if (response != null) {
                    Intent intent = new Intent(AppointmentDetailsActivity.this, NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });

        img_back.setOnClickListener(this);
        linear_accept.setOnClickListener(this);
        linear_assign.setOnClickListener(this);
        linear_decline.setOnClickListener(this);
        linear_pdf.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
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
                int pdfSize = myList.get(0).items.getPdfs().size();
                String pdf_url = DataManger.IMAGE_URL + "/uploads/"+sharedPreferences1.getString("company_id", null) + "/pdfs/"+ myList.get(0).items.getPdfs().get(pdfSize-1).getValue();
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
        txt_date_time.setText(date+"");

        txt_purpose.setText(body.items.getPurpose()+"");
        txt_name.setText(body.items.getvData().getName()+"");
        txt_phone.setText(body.items.getvData().getMobile()+"");
        txt_email.setText(body.items.getvData().getEmail()+"");
        txt_origanization.setText(body.items.getvData().getCompany()+"");
        if (body.items.getNote()==null||body.items.getNote().equalsIgnoreCase("")){
            linear_note.setVisibility(View.GONE);
        }else {
            linear_note.setVisibility(View.VISIBLE);
            txt_note.setText(body.items.getNote()+"");
        }

        if (body.items.getPdfs() !=null){
            int pdfSize = body.items.getPdfs().size();
            if (pdfSize == 0 ){
                linear_pdf.setVisibility(View.GONE);
            }else {
                linear_pdf.setVisibility(View.VISIBLE);
                txt_pdf_name.setText(body.items.getPdfs().get(pdfSize-1).getName()+"");
            }
        }

    }

    //Assign
    private void Assignpopup() {
        dialog = new Dialog(AppointmentDetailsActivity.this);
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
        final Dialog dialog = new Dialog(AppointmentDetailsActivity.this);
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

}