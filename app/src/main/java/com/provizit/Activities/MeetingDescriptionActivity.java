package com.provizit.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.provizit.AdapterAndModel.HostSlots.HostSlotsData;
import com.provizit.AdapterAndModel.HostSlots.HostSlotsModel;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.Logins.ForgotActivity;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.provizit.Services.Model1;
import com.provizit.Utilities.Agenda;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CustomAdapter;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.FileUtilsClass;
import com.provizit.Utilities.GlideChip;
import com.provizit.Utilities.Invited;
import com.provizit.Utilities.Inviteeitem;
import com.provizit.Utilities.Inviteemodelclass;
import com.provizit.Utilities.LocationData;
import com.provizit.Utilities.Pdfs;
import com.provizit.Utilities.RangeTimePickerDialog;
import com.provizit.Utilities.RoleDetails;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.view.View.GONE;

public class MeetingDescriptionActivity extends AppCompatActivity {
    private static final String TAG = "MeetingDescriptionActiv";

    CardView card_view_progress;

    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;

    public static final int RESULT_CODE = 12;

    ImageView img_back;
    TextView txt_title;
    TextView name, company, subject,txt_category, location, time,r_time,txt_time, invitee_count, description, cabin, date;
    LinearLayout linear_original_time,linear_suggested_time;
    CompanyData meetings;

    ArrayList<LocationData> meetinglocations;
    LinearLayout cancellation, reschedule, apporve, decline, accept, reject, assign, add_guest,resend;
    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;


    int indexid;
    LinearLayout visitorcountlinear,invitee_details;
    CircleImageView img;
    SharedPreferences.Editor editor1;
    AutoCompleteTextView department_spinner,emp_spinner;
    String Assignemail;
    ArrayList<CompanyData> departments;
    ArrayList<CompanyData> employees,invitesData;
    CustomAdapter departmentAdapter,employeeAdapter;
    ArrayList<Invited> invitedArrayList;
    int status;
    ArrayList<String> iEmail;
    AlertDialog.Builder builder;
    Boolean GUEST_STATUS = true;
    AutoCompleteTextView email_spinner;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2,}+";
    CustomAdapter invitesadapter;
    int mYear;
    int mMonth;
    int mDay;
    int date_status = 0,d_hour,d_min;
    long s_time, e_time, s_date;
    String meeting,assignedId;
    int R_status;
    EditText datep,meeting_st,meeting_et,reason;
    LinearLayout action_btn;
    RecyclerView recyclerView,agendaRecyclerView;
    Adapter1 invitesAdapter;
    AgendaAdapter1 agendaAdapter;
    TextView v_name,E_desi,email,mobile,host,invitee_text,assignedto,title1,pdfFile;
    FloatingActionButton fab,uploadPdf;
    ImageView stamp;
    TextView  agendaText;
    ArrayList<Pdfs> pdfsArrayList;
    private Intent browserIntent;
    private boolean isHostPdfUpload = false;
    String form_type = "";

    ApiViewModel apiViewModel;

    @SuppressLint({"RestrictedApi", "UseCompatLoadingForDrawables"})
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         Window window = getWindow();
         window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
         window.setStatusBarColor(ContextCompat.getColor(MeetingDescriptionActivity.this,R.color.colorPrimary));
         setContentView(R.layout.activity_meetingdescription);

        card_view_progress = findViewById(R.id.card_view_progress);


        apiViewModel = new ViewModelProvider(MeetingDescriptionActivity.this).get(ApiViewModel.class);


        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();

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


        myDb = new DatabaseHelper(this);
        meetinglocations = new ArrayList<>();
        pdfsArrayList = new ArrayList<>();
        meetinglocations = myDb.getCompanyAddress();
        sharedPreferences1 = MeetingDescriptionActivity.this.getSharedPreferences("EGEMSS_DATA", 0);
        editor1 = sharedPreferences1.edit();

        empData = new EmpData();
        empData = myDb.getEmpdata();

        img_back = findViewById(R.id.img_back);
        txt_title = findViewById(R.id.txt_title);
        invitee_details = findViewById(R.id.invitee_details);
        v_name = findViewById(R.id.v_name);
        resend = findViewById(R.id.iResend);
        pdfFile = findViewById(R.id.pdfFile);
        stamp = findViewById(R.id.stamp);
        uploadPdf = findViewById(R.id.uploadPdf);
        E_desi = findViewById(R.id.E_desi);
        email = findViewById(R.id.email);
        assignedto = findViewById(R.id.assignedto);
        mobile = findViewById(R.id.mobile);
        reschedule = findViewById(R.id.reschedule);
        cancellation = findViewById(R.id.cancellation);
        apporve = findViewById(R.id.apporve);
        invitee_text = findViewById(R.id.invitee_text);
        date = findViewById(R.id.date1);
        img = findViewById(R.id.visitor_img);
        action_btn = findViewById(R.id.action_btn);
        reject = findViewById(R.id.reject);
        add_guest = findViewById(R.id.add_guest);
        assign = findViewById(R.id.assign);
        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);
        name = findViewById(R.id.visitor_name);
        fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingDescriptionActivity.this, SetupMeetingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        RoleDetails roledata = myDb.getRole();
        if (roledata.getSmeeting().equals("false")) {
            fab.setVisibility(GONE);
        }
        visitorcountlinear = findViewById(R.id.visitorcountlinear);
        cabin = findViewById(R.id.cabin);
        company = findViewById(R.id.visitor_comapany);
        subject = findViewById(R.id.subject1);
        txt_category = findViewById(R.id.txt_category);
        location = findViewById(R.id.location1);
        time = findViewById(R.id.s_time);
        r_time = findViewById(R.id.r_time);
        txt_time = findViewById(R.id.txt_time);
        linear_original_time = findViewById(R.id.linear_original_time);
        linear_suggested_time = findViewById(R.id.linear_suggested_time);
        invitee_count = findViewById(R.id.invitee_count);
        host = findViewById(R.id.host);
        description = findViewById(R.id.decription);
        title1 = findViewById(R.id.title1);
        invitedArrayList =new ArrayList<>();
        invitesData =new ArrayList<>();
        iEmail =new ArrayList<>();

        Log.e(TAG, "onCreate:e"+empData.getEmp_id() );

        getsubhierarchys(empData.getLocation());
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                Assignpopup();
            }
        });
        visitorcountlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                Intent intent = new Intent(MeetingDescriptionActivity.this, VisitorDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("invites", meetings.getInvites());
                startActivity(intent);
            }
        });
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                getEmployeeDetails(meetings.getEmp_id());
            }
        });
        assignedto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                getEmployeeDetails(assignedId);
            }
        });
        uploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                pickPdf();
            }
        });
        add_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                if(status == 3){
                    AddGuest();
                }
                else{
                    editor1.putString("m_id",meetings.get_id().get$oid());
                    editor1.putInt("m_action",1);
                    editor1.commit();
                    editor1.apply();
                    Intent intent = new Intent(MeetingDescriptionActivity.this, SetupMeetingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                if(R_status == 1){
                    ReschedulePopup();
                }
                else{
                    editor1.putString("m_id",meetings.get_id().get$oid());
                    editor1.putInt("m_action",2);
                    editor1.commit();
                    editor1.apply();
                    Intent intent = new Intent(MeetingDescriptionActivity.this, SetupMeetingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
        pdfFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                if (empData.getEmp_id().equals(meetings.getEmp_id())) {
                    PdfClick();
                }
                else{
                    int pdfSize = meetings.getPdfs().size();
                    String pdf_url = DataManger.IMAGE_URL + "/uploads/"+sharedPreferences1.getString("company_id", null) + "/pdfs/"+ meetings.getPdfs().get(pdfSize-1).getValue();
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                    startActivity(browserIntent);
                }
            }
        });
        cancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);

                final Dialog dialog_c = new Dialog(MeetingDescriptionActivity.this);
                dialog_c.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog_c.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_c.setCancelable(false);
                dialog_c.setContentView(R.layout.otp_popup);
                Objects.requireNonNull(dialog_c.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                final TextView submit;
                final TextView cancel;
                final EditText reason;
                reason = dialog_c.findViewById(R.id.reason);
                submit = dialog_c.findViewById(R.id.submit);
                cancel = dialog_c.findViewById(R.id.cancel);
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
                                jsonObj_.put("formtype", "delete");
                                jsonObj_.put("status", 1);
                                jsonObj_.put("id", meetings.get_id().get$oid());
                                jsonObj_.put("reason", reason.getText().toString());
                                jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                                JsonParser jsonParser = new JsonParser();
                                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            updatemeetings(gsonObject);
                            dialog_c.dismiss();
                            //loading
                            card_view_progress.setVisibility(View.VISIBLE);
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AnimationSet animation4 = Conversions.animation();
                        v.startAnimation(animation4);
                        dialog_c.dismiss();
                    }
                });
                dialog_c.show();


//                builder = new AlertDialog.Builder(MeetingDescriptionActivity.this);
//                builder.setMessage("Do you want to Cancel this Meeting ?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                progress.show();
//                                JsonObject gsonObject = new JsonObject();
//                                JSONObject jsonObj_ = new JSONObject();
//                                try {
//                                    jsonObj_.put("formtype", "delete");
//                                    jsonObj_.put("status", 1);
//                                    jsonObj_.put("id", meetings.get_id().get$oid());
//                                    jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
//
//                                    JsonParser jsonParser = new JsonParser();
//                                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                updatemeetings(gsonObject);
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                //Creating dialog box
//                AlertDialog alert = builder.create();
//                //Setting the title manually
//                alert.setTitle("");
//                alert.show();


            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                card_view_progress.setVisibility(View.VISIBLE);

                apiViewModel.gethostslots(MeetingDescriptionActivity.this,"end",  empData.getEmp_id(), empData.getEmail(), s_time, e_time-1);


//                JsonObject gsonObject = new JsonObject();
//                JSONObject jsonObj_ = new JSONObject();
//                try {
//                    jsonObj_.put("formtype", "after");
//                    jsonObj_.put("indexid", indexid);
//                    jsonObj_.put("emp_id", empData.getEmp_id());
//                    jsonObj_.put("status", 3);
//                    jsonObj_.put("email", empData.getEmail());
//                    jsonObj_.put("id", meetings.get_id().get$oid());
//                    jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
//                    JsonParser jsonParser = new JsonParser();
//                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                updatemeetings(gsonObject);

            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                card_view_progress.setVisibility(View.VISIBLE);
                JsonObject gsonObject = new JsonObject();
                JSONObject jsonObj_ = new JSONObject();
                try {
                    jsonObj_.put("formtype", "insert");
                    jsonObj_.put("id", meetings.get_id().get$oid());
                    jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                    JsonParser jsonParser = new JsonParser();
                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                iResend(gsonObject);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                popup(false);
            }
        });
        apporve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                card_view_progress.setVisibility(View.VISIBLE);
                JsonObject json = ApproverActionjson(true);
                System.out.println("json object" + json);
                updatemeetings(json);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                popup(true);
            }
        });
        recyclerView = findViewById(R.id.invites);
        agendaText = findViewById(R.id.agendaText);
        agendaRecyclerView = findViewById(R.id.agenda);


        CompanyData intentMeetings = null;
        form_type = getIntent().getStringExtra("form_type");
        if (form_type.equalsIgnoreCase("notification")){
            txt_title.setText(getString(R.string.meeting));
        }else {
            intentMeetings = (CompanyData) getIntent().getSerializableExtra("meeting");

        }


        String meetingsId;


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                Intent intent2 = new Intent();
                setResult(RESULT_CODE, intent2);
                finish();
            }
        });


        if( intentMeetings != null){
            meetingsId = intentMeetings.get_id().get$oid();
            getmeetingdetails(meetingsId);
        }
        else{
            System.out.println("m_id"+getIntent().getStringExtra("m_id"));
            meetingsId = getIntent().getStringExtra("m_id");
            System.out.println();
            getmeetingdetails(meetingsId);
        }


        apiViewModel.gethostslots_response().observe(this, new Observer<HostSlotsModel>() {
            @Override
            public void onChanged(HostSlotsModel response) {
                card_view_progress.setVisibility(GONE);
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        ArrayList<HostSlotsData> hostSlotsData = new ArrayList<>();
                        hostSlotsData = response.getItems();
                         if (hostSlotsData.size() == 0) {

                            JsonObject gsonObject = new JsonObject();
                            JSONObject jsonObj_ = new JSONObject();
                            try {
                                jsonObj_.put("formtype", "after");
                                jsonObj_.put("indexid", indexid);
                                jsonObj_.put("emp_id", empData.getEmp_id());
                                jsonObj_.put("status", 3);
                                jsonObj_.put("email", empData.getEmail());
                                jsonObj_.put("id", meetings.get_id().get$oid());
                                jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                                JsonParser jsonParser = new JsonParser();
                                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            updatemeetings(gsonObject);

                        }else {
                            new AlertDialog.Builder(MeetingDescriptionActivity.this)
//                                   .setTitle(R.string.warning)
                                    .setMessage(R.string.you_have_another_meeting_at_this_time_)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        }
                    }
                }
            }
        });


        apiViewModel.updatemeetings_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                card_view_progress.setVisibility(GONE);
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        Intent intent = new Intent(MeetingDescriptionActivity.this, NavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });

        apiViewModel.iresend_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                card_view_progress.setVisibility(GONE);
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        System.out.println("asfasf" + response.result);
                    }
                }
            }
        });

        apiViewModel.addcovisitor_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                card_view_progress.setVisibility(GONE);
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        Intent intent = new Intent(MeetingDescriptionActivity.this, NavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });

        apiViewModel.getsubhierarchys_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        departments = new ArrayList<>();
                        departments = response.getItems();
                    }
                }
            }
        });

        apiViewModel.getsearchemployees_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        employees = new ArrayList<>();
                        employees = response.getItems();

                        //assign person remove for list and host
                        for(int j=0;j<meetings.getInvites().size();j++){

                            for(int i=0;i<employees.size();i++){

                                if(employees.get(i).getEmail().equals(empData.getEmail())){
                                    employees.remove(employees.get(i));
                                }

                                if (meetings.getInvites().get(j).getId().equalsIgnoreCase(employees.get(i).get_id().get$oid())){
                                    if(meetings.getInvites().get(j).isAssign() == true){
                                        employees.remove(employees.get(i));
                                    }
                                }

                            }

                        }


                        if(employees.size() == 0){
                            department_spinner.setText("");
                        }
                        else{
                            employeeAdapter = new CustomAdapter(MeetingDescriptionActivity.this, R.layout.row, R.id.lbl_name, employees,0,"");
                            emp_spinner.setThreshold(0);//will start working from first character
                            emp_spinner.setAdapter(employeeAdapter);//setting the adapter data into the AutoCompleteTextView
                            emp_spinner.setEnabled(true);
                        }
                    }
                }
            }
        });

        apiViewModel.getInviteData_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        invitesData = new ArrayList<>();
                        invitesData = response.getItems();
                        ArrayList<CompanyData>  DinvitesData = new ArrayList<>();

                        System.out.println("InviteSize" + invitesData.size());
                        invitesadapter = new CustomAdapter(MeetingDescriptionActivity.this, R.layout.row, R.id.lbl_name, invitesData, 1, sharedPreferences1.getString("company_id", null));
                        email_spinner.setThreshold(2);
                        email_spinner.setAdapter(invitesadapter);
                        if (GUEST_STATUS) {
                            email_spinner.showDropDown();
                        }
                    }
                }
            }
        });

        apiViewModel.getEmployeeDetailsResponse().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject response) {
                if (response != null) {
                    Gson gson = new Gson();
                    Inviteemodelclass model = gson.fromJson(String.valueOf(response), Inviteemodelclass.class);

                    if (model != null) {
                        Integer statuscode = model.getResult();
                        Integer successcode = 200, failurecode = 401, not_verified = 404;
                        if (statuscode.equals(failurecode)) {

                        } else if (statuscode.equals(not_verified)) {

                        } else if (statuscode.equals(successcode))  {
                            Inviteeitem inviteDetails = model.getItems();
                            final Dialog dialog = new Dialog(MeetingDescriptionActivity.this);
                            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.invitee_popup);
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            final TextView submit;
                            final TextView  v_name,E_desi,email,mobile,cancel,company1,title;
                            final CircleImageView emp_img;

                            cancel = dialog.findViewById(R.id.cancel);
                            emp_img = dialog.findViewById(R.id.emp_img);
                            E_desi = dialog.findViewById(R.id.E_desi);
                            title = dialog.findViewById(R.id.title);
                            email = dialog.findViewById(R.id.email);
                            mobile = dialog.findViewById(R.id.mobile);
                            company1 = dialog.findViewById(R.id.company);
                            v_name = dialog.findViewById(R.id.v_name);
                            v_name.setText(inviteDetails.getName());
                            mobile.setText(inviteDetails.getMobile());
                            email.setText(inviteDetails.getEmail());
                            company1.setText(inviteDetails.getCompany());

                            if (inviteDetails.getPic() != null && inviteDetails.getPic().size() != 0) {
                                Glide.with(MeetingDescriptionActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + inviteDetails.getPic().get(inviteDetails.getPic().size() - 1))
                                        .into(emp_img);
                            }

                            if(inviteDetails.getSupertype().equals("employee")){
                                title.setText("Employee Details");
                                company1.setText(DatabaseHelper.companyname);

                            }
                            E_desi.setText(inviteDetails.getDesignation());
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }
                }else {
                    Conversions.errroScreen(MeetingDescriptionActivity.this, "");
                }
            }
        });

        apiViewModel.pdfupload_response().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject response) {
                if (response != null) {

                }
            }
        });

        apiViewModel.getmeetingdetails_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                card_view_progress.setVisibility(GONE);
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        meetings = response.getItems();
                        setupView();
                    }
                }
            }
        });

        apiViewModel.readorunread_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                    }
                }
            }
        });

    }

    protected void registoreNetWorkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    //readorunread
    private void readorunread(JsonObject jsonObject) {

        apiViewModel.readorunread(getApplicationContext(),jsonObject);

    }


    private void updatemeetings(JsonObject jsonObject) {
        apiViewModel.updatemeetings(getApplicationContext(),jsonObject);
        card_view_progress.setVisibility(View.VISIBLE);
    }

    private void iResend(JsonObject jsonObject) {

        apiViewModel.iresend(getApplicationContext(),jsonObject);
        card_view_progress.setVisibility(View.VISIBLE);
    }

    private void popup(final Boolean ApproverAction) {
        final Dialog dialog = new Dialog(MeetingDescriptionActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.otp_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView submit;
        final TextView cancel;
        reason = dialog.findViewById(R.id.reason);
        submit = dialog.findViewById(R.id.submit);
        cancel = dialog.findViewById(R.id.cancel);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_view_progress.setVisibility(View.VISIBLE);
                System.out.println("staus 1"+ApproverAction);
                if(ApproverAction){
                    JsonObject json = ApproverActionjson(false);
                    System.out.println("json object" + json);
                    updatemeetings(json);
                }
                else {
                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "after");
                        jsonObj_.put("indexid", indexid);
                        jsonObj_.put("reason", reason.getText().toString());
                        jsonObj_.put("emp_id", empData.getEmp_id());
                        jsonObj_.put("status", 1);
                        jsonObj_.put("email", empData.getEmail());
                        jsonObj_.put("id", meetings.get_id().get$oid());
                        jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updatemeetings(gsonObject);
                }
                dialog.dismiss();
            }


        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void ReschedulePopup() {
        final Dialog dialog = new Dialog(MeetingDescriptionActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.reschedule_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView request,cancel;

        reason = dialog.findViewById(R.id.reason);
        cancel = dialog.findViewById(R.id.cancel);
        request = dialog.findViewById(R.id.request);

        datep = dialog.findViewById(R.id.date);
        meeting_st = dialog.findViewById(R.id.meeting_st);
        meeting_et = dialog.findViewById(R.id.meeting_duration);
        Long d= meetings.getEnd()-meetings.getStart()+1;
        d_hour = (int) Math.floor(d / 3600);
        d_min =  (int) Math.floor((d % 3600) / 60);
        datep.setText(getSdate((meetings.getDate()+Conversions.timezone()) * 1000));
        meeting_st.setText(Conversions.millitotime((meetings.getStart()+Conversions.timezone()) * 1000,false));
        meeting_et.setText(d_hour+":"+d_min);


        datep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
        meeting_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meeting = "st";
                if (date.getText().length() == 0) {
                    new AlertDialog.Builder(MeetingDescriptionActivity.this)
                            .setTitle("Warning")
                            .setMessage("Select date")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    timePicker();
                }
            }
        });
        meeting_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.getText().length() == 0) {
                    new AlertDialog.Builder(MeetingDescriptionActivity.this)
                            .setTitle("Warning")
                            .setMessage("Select date")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else if (meeting_st.getText().length() == 0) {
                    new AlertDialog.Builder(MeetingDescriptionActivity.this)
                            .setTitle("Warning")
                            .setMessage("Select start time")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    final int hour = 1;
                    final int minute = 0;
                    final RangeTimePickerDialog mTimePicker;

                    mTimePicker = new RangeTimePickerDialog(MeetingDescriptionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            meeting_et.setText(selectedHour + ":" + selectedMinute);
                            String myTime = meeting_et.getText().toString();
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                            Date d = null;
                            try {
                                d = df.parse(myTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            cal.add(Calendar.MINUTE, selectedMinute);
                            cal.add(Calendar.HOUR_OF_DAY, selectedHour);
                            String newTime = df.format(cal.getTime());
                            e_time = Conversions.gettimestamp(date.getText().toString(),newTime);
                        }
                    }, hour, minute, true);//true = 24 hour time
                    mTimePicker.show();

                }
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                card_view_progress.setVisibility(View.VISIBLE);
                JsonObject json = Reschedulejson();
                System.out.println("json object" + json);
                updatemeetings(json);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void AddGuest() {
        final Dialog dialog = new Dialog(MeetingDescriptionActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_guest_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final ChipGroup chipgroup;
        TextView add_guest,cancel;

        invitedArrayList = new ArrayList<>();
//        meetings.getApprover_roleid()
        chipgroup = dialog.findViewById(R.id.chipgroup);
        add_guest = dialog.findViewById(R.id.add_guest);
        cancel = dialog.findViewById(R.id.cancel);
        email_spinner = dialog.findViewById(R.id.email_spinner);
        for (int i = 0; i < meetings.getInvites().size(); i++) {
            Chip newChip = getChip(chipgroup, meetings.getInvites().get(i), 2);
            iEmail.add(meetings.getInvites().get(i).getEmail());
            chipgroup.addView(newChip);
        }
        email_spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textValue = email_spinner.getText().toString();
                if (textValue.length() == 1) {
                    GUEST_STATUS = true;
                }
                if (GUEST_STATUS) {
                    if (textValue.length() >= 2) {
                        getInviteData(email_spinner.getText().toString());
                        String lastLetter = textValue.substring(textValue.length() - 1);
                        if (lastLetter.equals(" ") || lastLetter.equals(",")) {
                            textValue = textValue.trim();
                            textValue = textValue.replace(",", "");
                            if (textValue.matches(emailPattern)) {
                                if (iEmail.contains(textValue)) {

                                } else {
                                    System.out.println("suhjadgfsghas");
                                    long currentDate = Conversions.getstamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
                                    Invited invited = new Invited();
//                           System.out.println("invited_name"+contactModel.getName());
                                    invited.setName("x");
                                    invited.setEmail(textValue);
                                    invited.setMobile("");
                                    invited.setCompany("x");
                                    invited.setCovisitor(true);
                                    invited.setLink(currentDate + "");
                                    invitedArrayList.add(invited);
                                    email_spinner.setText("");
//                           Adapter1 meetingListAdapter = new Adapter1(getActivity(),invitedArrayList);
//                           recyclerView.setAdapter(meetingListAdapter);
//                           recyclerView.setVisibility(View.VISIBLE);
                                    Chip newChip = getChip(chipgroup, invited, 1);
//                                            ge
                                    iEmail.add(textValue);
                                    chipgroup.addView(newChip);
                                }

                            }
                        }

                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View arg1, int index,long arg3) {
                GUEST_STATUS = false;
                long currentDate = Conversions.getstamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
                email_spinner.dismissDropDown();
                Boolean aInserted = false;
                CompanyData contactModel = invitesData.get(index);
                if (iEmail.contains(contactModel.getEmail())) {
                    aInserted = true;
                }
                if (!aInserted) {
                    Invited invited = new Invited();
                    System.out.println("invited_name" + contactModel.getName());
                    invited.setName(contactModel.getName()+"");
                    invited.setEmail(contactModel.getEmail()+"");
                    invited.setPic(contactModel.getPic());
                    invited.setMobile("");
                    invited.setCovisitor(true);
                    invited.setCompany("");
                    invited.setLink(currentDate + "");
                    invitedArrayList.add(invited);
                    email_spinner.setText("");
                    status = 0;
//                    invite.setVisibility(View.VISIBLE);
                    Chip newChip = getChip(chipgroup, invited, 1);
                    chipgroup.addView(newChip);
                    iEmail.add(contactModel.getEmail());
                } else {
                    System.out.println("subhahsdashf");
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        add_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(invitedArrayList.size() != 0){
                    card_view_progress.setVisibility(View.VISIBLE);
                    JsonObject json = addCovisitorjson();
                    System.out.println("json object" + json);
                    addcovisitor(json);
                }
            }
        });
        dialog.show();
    }

    private void Assignpopup() {
        final Dialog dialog = new Dialog(MeetingDescriptionActivity.this);
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

        departmentAdapter = new CustomAdapter(MeetingDescriptionActivity.this, R.layout.row, R.id.lbl_name, departments,0,"");
        department_spinner.setInputType(InputType.TYPE_NULL);
        department_spinner.setThreshold(0);//will start working from first character
        department_spinner.setAdapter(departmentAdapter);//setting the adapter data into the AutoCompleteTextView
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
                getsearchemployees(empData.getLocation(), departments.get(position).get_id().get$oid());
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
                Assignemail = employees.get(position).getEmail();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                card_view_progress.setVisibility(GONE);
            }
        });
        assign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_view_progress.setVisibility(View.VISIBLE);
                Invited invited = new Invited();
                invited.setEmail(Assignemail);
                invited.setAssign(true);
                invitedArrayList.add(invited);
                JsonObject json = addCovisitorjson();
                System.out.println("json object" + json);
                addcovisitor(json);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addcovisitor(JsonObject jsonObject) {

        apiViewModel.addcovisitor(getApplicationContext(),jsonObject);
        card_view_progress.setVisibility(View.VISIBLE);
    }

    private void getsubhierarchys(String l_id) {

        apiViewModel.getsubhierarchys(getApplicationContext(),l_id);

    }

    private void getsearchemployees(String l_id, String h_id) {

        apiViewModel.getsearchemployees(getApplicationContext(),l_id,h_id);

    }

    private GlideChip getChip(final ChipGroup chipGroup, final Invited invited, Integer type) {
        final GlideChip chip = new GlideChip(MeetingDescriptionActivity.this);
        ChipDrawable chipDrawable = ChipDrawable.createFromResource(MeetingDescriptionActivity.this, R.xml.chip);
        chip.setChipDrawable(chipDrawable);
        if (type == 2) {
            chip.setCloseIconVisible(false);
        }
        chip.setCheckable(false);
        chip.setText(invited.getName());

        if (invited.getPic() != null && invited.getPic().size() != 0) {
            chip.setIconUrl(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + invited.getPic().get(invited.getPic().size() - 1), getResources().getDrawable(R.drawable.ic_user));
        }

        if (invited.getName() == null || invited.getName().equals("x") || invited.getName().equals("")) {
            chip.setText(invited.getEmail());
        }

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(MeetingDescriptionActivity.this);
                builder.setMessage("Are you sure you want to delete this Invitee ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                System.out.println("chip_id" + invited.getName());
                                System.out.println("chip_id" + invited.getEmail());
                                invitedArrayList.remove(invited);
                                iEmail.remove(invited.getEmail());
                                chipGroup.removeView(chip);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("");
                alert.show();
            }
        });
        return chip;
    }


    private void getInviteData(String S_value) {

        apiViewModel.getInviteData(getApplicationContext(),"type", "usertype", S_value);

    }

    private void datePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final String current_date = mYear + "- " + mMonth + "-" + mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(MeetingDescriptionActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        date.setText(new StringBuilder().append(dayOfMonth)
                                .append("-").append(monthOfYear + 1).append("-").append(year)
                                .append(" "));

                        s_date = Conversions.getdatestamp(datep.getText().toString());

                        System.out.println("s_date :-"+s_date+" s_time :-"+s_time+" e_time :-"+e_time);

                        String selected_date = year + "-" + monthOfYear + "-" + dayOfMonth;
                        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d1 = null;
                        try {
                            d1 = sdformat.parse(selected_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date d2 = null;
                        try {
                            d2 = sdformat.parse(current_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        System.out.println("The date 1 is: " + sdformat.format(d1));
                        System.out.println("The date 2 is: " + sdformat.format(d2));

                        if (d1.compareTo(d2) == 0) {
                            date_status = 0;
                            System.out.println("Both dates are equal");
                        } else {
                            date_status = 1;
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        datePickerDialog.show();

    }

    private void timePicker() {
        if (date_status == 1) {
            final int hour = 8;
            final int minute = 0;
            final RangeTimePickerDialog mTimePicker;
            mTimePicker = new RangeTimePickerDialog(MeetingDescriptionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    meeting_st.setText(selectedHour + ":" + selectedMinute);
                    s_time = Conversions.gettimestamp(datep.getText().toString(),selectedHour + ":" + selectedMinute);
                    System.out.println("s_date :-"+s_date+" s_time :-"+s_time+" e_time :-"+e_time);
                    String myTime = selectedHour + ":" + selectedMinute;
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    Date d = null;
                    try {
                        d = df.parse(myTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.HOUR_OF_DAY, d_hour);
                    cal.add(Calendar.MINUTE, d_min);
                    String newTime = df.format(cal.getTime());
                    e_time = Conversions.gettimestamp(datep.getText().toString(),newTime);
                }
            }, hour, minute, true);//true = 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.setMin(8, 0);
            mTimePicker.setMax(21, 0);
            mTimePicker.show();
        } else {
            final Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            final RangeTimePickerDialog mTimePicker;
            if (minute < 15) {
                minute = 15;
            } else if (minute < 30) {
                minute = 30;
            } else if (minute < 45) {
                minute = 45;
            } else {
                hour = hour + 1;
                minute = 0;
            }
            mTimePicker = new RangeTimePickerDialog(MeetingDescriptionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    meeting_st.setText(selectedHour + ":" + selectedMinute);
                    s_time = Conversions.gettimestamp(datep.getText().toString(),selectedHour + ":" + selectedMinute);
                    String myTime = selectedHour + ":" + selectedMinute;
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    Date d = null;
                    try {
                        d = df.parse(myTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.HOUR_OF_DAY, 1);
                    String newTime = df.format(cal.getTime());
                    e_time = Conversions.gettimestamp(datep.getText().toString(),newTime);
                    System.out.println("s_date :-"+s_date+" s_time :-"+s_time+" e_time :-"+e_time);
                }
            }, hour, minute, true);//true = 24 hour time
            mTimePicker.setTitle("Select Time" + minute);

            mTimePicker.setMin(hour, minute);
            mTimePicker.show();
        }
    }

    private JsonObject addCovisitorjson() {
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray invites = new JSONArray();
        try {
            invites = new JSONArray();
            for (int i = 0; i < invitedArrayList.size(); i++) {
                invites.put(invitedArrayList.get(i).getInvites());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj_.put("id", meetings.get_id().get$oid());
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("subject", meetings.getSubject());
            jsonObj_.put("desc", meetings.getDesc());
            jsonObj_.put("formtype", "insert");
            jsonObj_.put("covisitors", invites);
            jsonObj_.put("emp_id", empData.getEmp_id());
            jsonObj_.put("agenda", new JSONArray());
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("gsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;
    }
    private JsonObject ApproverActionjson(Boolean Apporve) {
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        try {
            jsonObj_.put("formtype", "mbefore");
            jsonObj_.put("id", meetings.get_id().get$oid());
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("emp_id", empData.getEmp_id());
            jsonObj_.put("approver_roleid", meetings.getApprover_roleid());
            jsonObj_.put("status", 2);
            if(!Apporve){
                jsonObj_.put("status", 1);
                jsonObj_.put("reason", reason.getText().toString());
            }
//            jsonObj_.put("agenda", new JSONArray());
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("gsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;
    }

    private JsonObject Reschedulejson() {
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray reschedule = new JSONArray();
        try {
            reschedule = new JSONArray();
            reschedule.put(getRdata());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj_.put("reschedule", reschedule);
            jsonObj_.put("rdate", s_time);
            jsonObj_.put("formtype", "after");
            jsonObj_.put("id", meetings.get_id().get$oid());
            jsonObj_.put("emp_id", empData.getEmp_id());
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("email", empData.getEmail());
            jsonObj_.put("status", 2);
            jsonObj_.put("reason", reason.getText().toString());
//            jsonObj_.put("agenda", new JSONArray());
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("gsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;
    }

    public JSONObject getRdata(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("status", 0);
            jo.put("date",  s_date);
            jo.put("start", s_time);
            jo.put("end", e_time - 1);
            jo.put("emp_id", "");
            jo.put("roleid",  "");
            jo.put("reason", "");
            jo.put("update", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }
        return super.onOptionsItemSelected(item);
    }
    public class Adapter1 extends RecyclerView.Adapter<Adapter1.MyviewHolder> {
        Context context;
        ArrayList<Invited> invited;
        int SelectedIndex;

        public class MyviewHolder extends RecyclerView.ViewHolder {
            LinearLayout playout;
            TextView txt_assign,name,slot;
            ImageView invitee_status,PdfIcon,reschedule_request;
            public MyviewHolder(View view) {
                super(view);
                txt_assign = view.findViewById(R.id.txt_assign);
                name = view.findViewById(R.id.name);
                invitee_status = view.findViewById(R.id.invitee_status);
                slot = view.findViewById(R.id.p_slot);
                playout = view.findViewById(R.id.p_layout);
                PdfIcon = view.findViewById(R.id.PdfIcon);
                reschedule_request = view.findViewById(R.id.reschedule_request);
            }
        }

        public Adapter1(Context context, ArrayList<Invited> vehicles) {
            this.context = context;
            this.invited = vehicles;
        }

        @Override
        public  Adapter1.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.invitess, parent, false);
            return new Adapter1.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final  Adapter1.MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {
            System.out.println("Size@"+invited.size());
            System.out.println("Size1@"+invited.get(position).getName());

            if(invited.get(position).getName().equals("x") || invited.get(position).getName().equals("")){
                if (invited.get(position).getEmail().equals("")){
                    holder.name.setText(invited.get(position).getMobile());
                }else {
                    holder.name.setText(invited.get(position).getEmail());
                }
            }else if (invited.get(position).getEmail()==null || invited.get(position).getEmail().equals("")){
                if (invited.get(position).getName().equals("")){
                    holder.name.setText(invited.get(position).getMobile());
                }else {
                    holder.name.setText(invited.get(position).getName());
                }
            }else if (invited.get(position).getMobile()==null || invited.get(position).getMobile().equals("")){
                holder.name.setText(invited.get(position).getEmail());
            }
            else{
                holder.name.setText(invited.get(position).getName());
            }


            if (invited.get(position).isAssign()==true){
                holder.txt_assign.setVisibility(View.VISIBLE);
            }else {
                holder.txt_assign.setVisibility(GONE);
            }


            //old
//            if (invited.get(position).getPslot() != 0 ){
//                holder.playout.setVisibility(View.VISIBLE);
//                holder.slot.setText(invited.get(position).getPslot()+"");
//            }


            if (invited.get(position).getPslot() != 0 ){
                holder.playout.setVisibility(View.VISIBLE);
                holder.slot.setText(invited.get(position).getPcat_name()+","+invited.get(position).getLot_name()+","+invited.get(position).getSlot_name());
            }


            if(invited.get(position).isPdfStatus()){
                holder.PdfIcon.setVisibility(View.VISIBLE);
            }
            else{
                holder.PdfIcon.setVisibility(GONE);
            }
            if(empData.getEmp_id().equals(meetings.getEmp_id())){
                holder.invitee_status.setVisibility(View.VISIBLE);
                holder.invitee_status.setBackgroundResource(R.drawable.ic_group_84);
                if(invited.get(position).getStatus() == 1){
                    holder.invitee_status.setBackgroundResource(R.drawable.ic_cancellation);
                }
                else if(invited.get(position).getStatus() == 3){
                    holder.invitee_status.setBackgroundResource(R.drawable.ic_check);
                }
            }
            if(invited.get(position).isAssign()){
                assignedto.setVisibility(View.VISIBLE);
                assignedId = invited.get(position).getId();
                assignedto.setText(invited.get(position).getName());
            }

            holder.PdfIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(empData.getEmail().equals(meetings.getInvites().get(position).getEmail())) {
                        InviteePdfClick(position);
                    }
                    else{
                        int pdfSize = meetings.getInvites().get(position).getPdfs().size();
                        String pdf_url = DataManger.IMAGE_URL + "/uploads/"+sharedPreferences1.getString("company_id", null) + "/pdfs/"+ meetings.getInvites().get(position).getPdfs().get(pdfSize-1).getValue();
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                        startActivity(browserIntent);
                    }
                }
            });

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(invited.get(position).getId().equals("")){
                        builder = new AlertDialog.Builder(MeetingDescriptionActivity.this);
                        builder.setTitle("Visitor details not found")
                                .setMessage(invited.get(position).getEmail())
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        getEmployeeDetails(invited.get(position).getId());
                    }
                }
            });


            if (invited.get(position).getStatus()==2.0){
                holder.reschedule_request.setVisibility(View.VISIBLE);
            }else {
                holder.reschedule_request.setVisibility(GONE);
            }

            holder.reschedule_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "reschedule");
                        jsonObj_.put("start", invited.get(position).getReschedule().get(0).getStart());
                        jsonObj_.put("end", invited.get(position).getReschedule().get(0).getEnd());
                        jsonObj_.put("id", meetings.get_id().get$oid());
                        jsonObj_.put("date", invited.get(position).getReschedule().get(0).getDate());
                        jsonObj_.put("status", 2);
                        jsonObj_.put("emp_id", empData.getEmp_id());
                        jsonObj_.put("desc", meetings.getDesc());
                        jsonObj_.put("samecabin", meetings.getSamecabin());
                        jsonObj_.put("meetingroom", meetings.getMeetingroom());
                        jsonObj_.put("entrypoint", meetings.getEntrypoint());
                        jsonObj_.put("subject", meetings.getSubject());
                        jsonObj_.put("location", meetings.getLocation());
                        jsonObj_.put("mtype", meetings.getMtype());
                        jsonObj_.put("mtype_val", meetings.getMtype_val());
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        System.out.println("gsonObject:reschedule" + gsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    reschedule_Popup(gsonObject,invited.get(position).getName(),invited.get(position).getReschedule().get(0).getDate(),invited.get(position).getReschedule().get(0).getStart(),invited.get(position).getReschedule().get(0).getEnd());
                }
            });


        }

        @Override
        public int getItemCount() {
            return invited.size();
        }
    }

    private void reschedule_Popup(JsonObject jsonObject,String Name,Long Date,Long Start,Long End) {
        final Dialog dialog = new Dialog(MeetingDescriptionActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.reschedule_info_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView txt_date,txt_time,txt_cancel,txt_accept;
        txt_date = dialog.findViewById(R.id.txt_date);
        txt_time = dialog.findViewById(R.id.txt_time);
        txt_cancel = dialog.findViewById(R.id.txt_cancel);
        txt_accept = dialog.findViewById(R.id.txt_accept);

        String s_time = Conversions.millitotime((Start + Conversions.timezone()) * 1000, false);
        String e_time = Conversions.millitotime((End+1 + Conversions.timezone()) * 1000, false);

        DateFormat simple = new SimpleDateFormat("E, dd MMM yyyy");
        Date result = new Date((Date + Conversions.timezone()) * 1000);
        String dates = simple.format(result) + "";

        txt_date.setText(dates);
        txt_time.setText(s_time+" - "+e_time);



        String r_s_time = Conversions.millitotime((meetings.getHistory().get(0).getStart()+Conversions.timezone()) * 1000,false);
        //accept button showing only for host
        if (empData.getEmp_id().equals(meetings.getEmp_id())) {
            if (s_time.equalsIgnoreCase(r_s_time)){
                txt_accept.setVisibility(GONE);
            }else {
                txt_accept.setVisibility(View.VISIBLE);
            }
        }

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation4 = Conversions.animation();
                v.startAnimation(animation4);
                dialog.dismiss();
            }
        });

        txt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updatemeetings(jsonObject);
            }
        });

        dialog.show();

    }

    public class AgendaAdapter1 extends RecyclerView.Adapter<AgendaAdapter1.MyviewHolder> {
        Context context;
        ArrayList<Agenda> agenda;

        public class MyviewHolder extends RecyclerView.ViewHolder {
            TextView agneda;
            public MyviewHolder(View view) {
                super(view);
                agneda = view.findViewById(R.id.agneda);
            }
        }

        public AgendaAdapter1(Context context, ArrayList<Agenda> agenda) {
            this.context = context;
            this.agenda = agenda;
        }

        @Override
        public  AgendaAdapter1.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.agenda_item, parent, false);
            return new AgendaAdapter1.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final  AgendaAdapter1.MyviewHolder holder, final int position) {
            System.out.println("Size@"+agenda.size());
            holder.agneda.setText((position+1) + "." + agenda.get(position).getDesc());
        }

        @Override
        public int getItemCount() {
            return agenda.size();
        }
    }
    private void getEmployeeDetails(String position) {

        apiViewModel.getEmployeeDetails(getApplicationContext(),position);

    }

    public static String getSdate(Long s_date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");//dd/MM/yyyy
        Date now = new Date();
        now.setTime(s_date);
        String strDate = sdfDate.format(now);
        return strDate;
    }
    private void reload(){
        Intent i = new Intent(MeetingDescriptionActivity.this, MeetingDescriptionActivity.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(i);
        overridePendingTransition(0, 0);
    }
    private void PdfClick() {
        final CharSequence[] options = {"View", "Delete", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDescriptionActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("View")){
                    int pdfSize = meetings.getPdfs().size();
                    String pdf_url = DataManger.IMAGE_URL + "/uploads/"+sharedPreferences1.getString("company_id", null) + "/pdfs/"+ meetings.getPdfs().get(pdfSize-1).getValue();
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                    startActivity(browserIntent);
                }
                else if (options[item].equals("Delete")){
                    HostPdfDelete();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void InviteePdfClick(int index) {
        final CharSequence[] options = {"View", "Delete", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDescriptionActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("View")){
                    int pdfSize = meetings.getInvites().get(index).getPdfs().size();
                    String pdf_url = DataManger.IMAGE_URL + "/uploads/"+sharedPreferences1.getString("company_id", null) + "/pdfs/"+ meetings.getInvites().get(index).getPdfs().get(pdfSize-1).getValue();
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                    startActivity(browserIntent);
                }else if (options[item].equals("Delete")){
                    InviteePdfDelete();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void HostPdfDelete(){
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray pdfs = new JSONArray();
        try {
            pdfs = new JSONArray();
            for (int i = 0; i < pdfsArrayList.size(); i++) {
                pdfsArrayList.get(i).setStatus(false);
                pdfs.put(pdfsArrayList.get(i).getPdfs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj_.put("id", meetings.get_id().get$oid());
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("formtype", "pdfs");
            jsonObj_.put("pdfStatus", false);
            jsonObj_.put("pdfs", pdfs);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("gsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updatemeetings(gsonObject);
    }
    private void InviteePdfDelete(){
        ArrayList<Pdfs> inviteePdfArraylist = new ArrayList<>();
        inviteePdfArraylist = meetings.getInvites().get(indexid).getPdfs();
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray pdfs = new JSONArray();
        try {
            pdfs = new JSONArray();
            for (int i = 0; i < inviteePdfArraylist.size(); i++) {
                inviteePdfArraylist.get(i).setStatus(false);
                pdfs.put(inviteePdfArraylist.get(i).getPdfs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj_.put("id", meetings.get_id().get$oid());
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("indexid", indexid);
            jsonObj_.put("formtype", "pdfsinvite");
            jsonObj_.put("email", empData.getEmail());
            jsonObj_.put("pdfStatus", false);
            jsonObj_.put("pdfs", pdfs);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("gsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updatemeetings(gsonObject);
    }
    protected void pickPdf(){
        if (permissionsGrantedPickPdf()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent,9811);
        } else {
            ActivityCompat.requestPermissions(MeetingDescriptionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        }
    }
    private Boolean permissionsGrantedPickPdf() {
        return ContextCompat.checkSelfPermission(MeetingDescriptionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 9811:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
//
                    Uri uri = data.getData();
                    String uriString = uri.toString();
//
                    String path = uri.getPath();
                    String displayName = null;
//                    Log.e("FilePath",uri.getPath());
////                    String filePath = getRealPathFromURI_API19(SetupMeetingActivity.this,uri);
////                    String filePath = FilePath.getFilePath(SetupMeetingActivity.this, uri);;
//
//                    File file = new File(path);

                    Uri image = data.getData();
                    //    selectedImageUri = data.getData();
                    // String filePath = getRealPathFromURIPath(image, CreateNewLeaveRequestActivity.this);

                    Context context = getApplicationContext();
//                    String  path = RealPathUtil.getRealPath(context,image);


                    //    selectedImage = FileUtils2.getPath(CreateNewLeaveRequestActivity.this,image);
//mediaPath = "";
                    //calling the upload file method after choosing the file
                    try {
                        uploadFile(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Log.v("mediaPath", "test");
                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                Log.e("Filename",displayName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
//                        displayName = myFile.getName();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile(Uri fileUri) throws FileNotFoundException, RemoteException {
        String PDFPATH = FileUtilsClass.getRealPath(MeetingDescriptionActivity.this,fileUri);
        System.out.println("Floder "+this.getExternalCacheDir().getAbsolutePath());
        System.out.println("PDfPAth " + fileUri.getPath());
        System.out.println("PDfPAth " + fileUri);
        System.out.println("PDfPAth " + PDFPATH);
        if(PDFPATH == null || PDFPATH.equals("")){
            PDFPATH =  FileUtilsClass.getFilePathFromURI(MeetingDescriptionActivity.this,fileUri);
        }
        File src = new File(PDFPATH);
        ContentResolver resolver = this.getContentResolver();
        ContentProviderClient providerClient = resolver.acquireContentProviderClient(fileUri);
        ParcelFileDescriptor descriptor = providerClient.openFile(fileUri, "r");
        ParcelFileDescriptor parcelFileDescriptor =
                resolver.openFileDescriptor(fileUri, "r", null);
        FileInputStream inputStream = new FileInputStream(descriptor.getFileDescriptor());
        //File file = new File(PDFPATH);

        File file = new File(this.getExternalCacheDir().getAbsolutePath(), src.getName());
        try (InputStream in = new FileInputStream(descriptor.getFileDescriptor())) {
            file.createNewFile();
            try (OutputStream out = new FileOutputStream(file)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",
                file.getName(), requestBody);

//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("*/*"), file));

        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        try {
            jsonObj_.put("key", file.getName());
            jsonObj_.put("cid", sharedPreferences1.getString("company_id", null));
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody fname = RequestBody.create(MediaType.parse("text/plain"),file.getName());
        RequestBody c_id = RequestBody.create(MediaType.parse("text/plain"),sharedPreferences1.getString("company_id", null));


        apiViewModel.pdfupload(MeetingDescriptionActivity.this, fileToUpload, fname,c_id);

        UploadPdf(file.getName());

    }
    private void UploadPdf(String filename){
        System.out.println("Subhasdh123456789");
        if (isHostPdfUpload) {
            Pdfs pdfsItem = new Pdfs();
            pdfsItem.setName(filename);
            pdfsItem.setStatus(true);
            pdfsItem.setValue(filename);
            pdfsArrayList.add(pdfsItem);
            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            JSONArray pdfs = new JSONArray();
            try {
                pdfs = new JSONArray();
                for (int i = 0; i < pdfsArrayList.size(); i++) {
                    pdfs.put(pdfsArrayList.get(i).getPdfs());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj_.put("id", meetings.get_id().get$oid());
                jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                jsonObj_.put("formtype", "pdfs");
                jsonObj_.put("pdfStatus", true);
                jsonObj_.put("pdfs", pdfs);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                System.out.println("gsonObject::" + gsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updatemeetings(gsonObject);
        } else {
            ArrayList<Pdfs> inviteePdfArraylist = new ArrayList<>();
            if(meetings.getInvites().get(indexid).getPdfs() != null) {
                inviteePdfArraylist = meetings.getInvites().get(indexid).getPdfs();
            }
            Pdfs pdfsItem = new Pdfs();
            pdfsItem.setName(filename);
            pdfsItem.setStatus(true);
            pdfsItem.setValue(filename);
            inviteePdfArraylist.add(pdfsItem);
            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            JSONArray pdfs = new JSONArray();
            try {
                pdfs = new JSONArray();
                for (int i = 0; i < inviteePdfArraylist.size(); i++) {
                    pdfs.put(inviteePdfArraylist.get(i).getPdfs());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj_.put("id", meetings.get_id().get$oid());
                jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                jsonObj_.put("indexid", indexid);
                jsonObj_.put("formtype", "pdfsinvite");
                jsonObj_.put("email", empData.getEmail());
                jsonObj_.put("pdfStatus", true);
                jsonObj_.put("pdfs", pdfs);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                System.out.println("gsonObject::" + gsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updatemeetings(gsonObject);
        }
    }
    private void getmeetingdetails(String id) {

        apiViewModel.getmeetingdetails(MeetingDescriptionActivity.this, id);
        card_view_progress.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    private void setupView(){
        assert meetings != null;
        if (meetings.getPdfStatus()){
            pdfFile.setVisibility(View.VISIBLE);
            pdfsArrayList = meetings.getPdfs();
        }
        else{
            pdfFile.setVisibility(GONE);

        }
        subject.setText(meetings.getSubject());
        txt_category.setText(meetings.getCategoryData().getName());
        s_date = meetings.getDate();
        s_time = meetings.getStart();
        e_time = meetings.getEnd();
        System.out.println("s_date :-"+s_date+" s_time :-"+s_time+" e_time :-"+e_time);
        if (empData.getEmp_id().equals(meetings.getEmp_id())) {
            uploadPdf.setVisibility(View.VISIBLE);
            isHostPdfUpload = true;
            resend.setVisibility(View.VISIBLE);
            reschedule.setVisibility(View.VISIBLE);
            add_guest.setVisibility(View.VISIBLE);
            assign.setVisibility(View.VISIBLE);
            cancellation.setVisibility(View.VISIBLE);
        }
        else if (empData.getRoleid().equals(meetings.getApprover_roleid())) {
            uploadPdf.setVisibility(GONE);
            apporve.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
        }
        else{
            for (int i = 0; i < meetings.getInvites().size(); i++) {
                if (empData.getEmail().equals(meetings.getInvites().get(i).getEmail())) {
                    uploadPdf.setVisibility(View.VISIBLE);
                    isHostPdfUpload = false;
                    R_status = 1;
                    accept.setVisibility(View.VISIBLE);
                    decline.setVisibility(View.VISIBLE);
                    reschedule.setVisibility(View.VISIBLE);
                    System.out.println();
                    if(meetings.getInvites().get(i).getStatus() == 3){
                        status = 3;
                        add_guest.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.GONE);
                        stamp.setBackgroundResource(R.drawable.accepted);
                        stamp.setVisibility(View.VISIBLE);
                    }
                    else if(meetings.getInvites().get(i).getStatus() == 1){
                        decline.setVisibility(View.GONE);
                        stamp.setBackgroundResource(R.drawable.declined);
                        stamp.setVisibility(View.VISIBLE);
                    }
                    indexid = i;
                    break;
                }
            }
        }
        host.setText(" "+meetings.getEmployee().getName());

        if(meetings.getInvites().size() == 1){
            invitee_text.setText(meetings.getInvites().size() + " " + getResources().getString(R.string.Guest));
        }
        else{
            invitee_text.setText(meetings.getInvites().size() + " " + getResources().getString(R.string.Guests));
        }
        company.setText(meetings.getEmployee().getDesignation());
        if (empData.getEmp_id().equals(meetings.getEmp_id())) {
            txt_title.setText(getString(R.string.my_meeting));
            host.setText("Me");
            name.setText(meetings.getInvites().get(0).getName() + "\n" + meetings.getInvites().get(0).getEmail());
            company.setText(meetings.getInvites().get(0).getCompany());
            if (meetings.getInvites().get(0).getPic().size() != 0) {
                Glide.with(MeetingDescriptionActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetings.getInvites().get(0).getPic().get(meetings.getInvites().get(0).getPic().size() - 1))
                        .into(img);
            }
        }
        if(meetings.getStatus() == 1){
            uploadPdf.setVisibility(GONE);
            action_btn.setVisibility(View.GONE);
            stamp.setBackgroundResource(R.drawable.cancelled);
            stamp.setVisibility(View.VISIBLE);
        }
        if(meetings.getStart()+Conversions.timezone() <= (Calendar.getInstance().getTimeInMillis()/1000)){
            action_btn.setVisibility(View.GONE);
            uploadPdf.setVisibility(GONE);
        }
        if(!meetings.getMtype().equals("1")){
            if(meetings.getMtype().equals("2")){

                location.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_meeting_room,0,0,0);
            }
            location.setText(meetings.getMtype_val());
        }
        else{
            location.setText(meetings.getEntrypoints().getName()+", "+meetings.getMeetingrooms().getName()+", "+meetinglocations.get(Integer.parseInt(meetings.getLocation())).getName());
        }
        if(meetings.getDesc().equals("")){
            title1.setVisibility(GONE);
            description.setVisibility(GONE);
        }
        else {
            title1.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            description.setText("   "+meetings.getDesc());
        }
        cabin.setText(meetings.getMeetingrooms().getName());
        //original time
        String s_time = Conversions.millitotime((meetings.getStart()+Conversions.timezone()) * 1000,false);
        String e_time = Conversions.millitotime((meetings.getEnd()+1+Conversions.timezone() + 1) * 1000,false);
        String date1 = Conversions.millitodateD((meetings.getStart()+Conversions.timezone()) * 1000);

        //rechedule time
        String r_s_time = Conversions.millitotime((meetings.getHistory().get(0).getStart()+Conversions.timezone()) * 1000,false);
        String r_e_time = Conversions.millitotime((meetings.getHistory().get(0).getEnd()+1+Conversions.timezone()) * 1000,false);
        String r_date1 = Conversions.millitodateD((meetings.getHistory().get(0).getDate()+Conversions.timezone()) * 1000);



        if (s_time.equalsIgnoreCase(r_s_time)){
            txt_time.setText(r_s_time + " - "+r_e_time);
            date.setText(" "+r_date1+" ");
            linear_original_time.setVisibility(GONE);
            linear_suggested_time.setVisibility(GONE);
        }else {
            linear_original_time.setVisibility(View.VISIBLE);
            linear_suggested_time.setVisibility(View.VISIBLE);
            date.setVisibility(GONE);
            txt_time.setVisibility(GONE);
            time.setText(r_date1+",  "+ r_s_time + " to "+r_e_time);
            r_time.setText(date1+", "+s_time+" to "+e_time);
        }


        invitee_count.setText("" + meetings.getInvites().size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MeetingDescriptionActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        invitesAdapter = new Adapter1(MeetingDescriptionActivity.this, meetings.getInvites());
        recyclerView.setAdapter(invitesAdapter);
//        if(!meetings.getInvites().get(0).getId().equals("")){
//            getEmployeeDetails(meetings.getInvites().get(0).getId());
//        }
//        else{
//             email.setText(meetings.getInvites().get(0).getEmail());
//        }
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MeetingDescriptionActivity.this, LinearLayoutManager.VERTICAL, false);
        agendaRecyclerView.setLayoutManager(linearLayoutManager1);
        if(meetings.getAgenda() != null && meetings.getAgenda().size() != 0){
            agendaAdapter = new AgendaAdapter1(MeetingDescriptionActivity.this, meetings.getAgenda());
            agendaRecyclerView.setAdapter(agendaAdapter);
        }
        else {
            agendaText.setVisibility(View.GONE);
        }

        //read card
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        try {
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("formtype", form_type);
            jsonObj_.put("id", meetings.get_id().get$oid());
            jsonObj_.put("roleid", empData.getRoleid());
            jsonObj_.put("email", empData.getEmail());
            jsonObj_.put("emp_id", empData.getEmp_id());
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        readorunread(gsonObject);
        if (empData.getEmp_id().equalsIgnoreCase(meetings.getEmp_id())) {
            txt_title.setText(getString(R.string.my_meeting));
        }else {
            txt_title.setText(getString(R.string.meeting));
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent();
        setResult(RESULT_CODE, intent2);
        finish();
    }


}

