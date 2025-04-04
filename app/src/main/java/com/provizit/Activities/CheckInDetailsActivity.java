package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.Logins.ForgotActivity;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.provizit.Services.Model1;
import com.provizit.Utilities.CommonObject;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CustomAdapter;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.RangeTimePickerDialog;
import com.provizit.databinding.ActivityCheckInDetailsNotificationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CheckInDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "CheckInDetailsNotificat";

    ActivityCheckInDetailsNotificationBinding activityCheckInDetailsNotificationBinding;

    public static final int RESULT_CODE = 12;
    
    //local sql
    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;

    BroadcastReceiver broadcastReceiver;

    CompanyData companyData_model;

    //Department
    ArrayList<CompanyData> departments;
    CustomAdapter departmentAdapter;
    AutoCompleteTextView department_spinner;

    //Employees;
    ArrayList<CompanyData> employees;
    AutoCompleteTextView emp_spinner;
    CustomAdapter employeeAdapter;
    String hierarchy_indexid,hierarchy_id ,host;


    //meeting room
    ArrayList<CompanyData> meetingrooms;
    CustomAdapter customAdapter1;
    AlertDialog.Builder builder;
    AutoCompleteTextView meetingroom_spinner;
    String m_room_ID = "";


    //time
    int s_hour,s_min,e_hour,e_min;


    ApiViewModel apiViewModel;

    String id = "";
    String vlocation = "";
    String Emp_id = "";
    String email = "";
    String name = "";
    String request;
    Long checkin;
    String history_host;
    String Supertype;


    int hour;
    int minute;
    Calendar currentTime;


    //time and date stamp
    private static Locale locale;
    String SelectedDate;
    public static long start_time_stamp,end_time_stamp;

    String type = "";
    String _id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(CheckInDetailsActivity.this);
        activityCheckInDetailsNotificationBinding = ActivityCheckInDetailsNotificationBinding.inflate(getLayoutInflater());
        setContentView(activityCheckInDetailsNotificationBinding.getRoot());
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            _id = b.getString("_id");
            type = b.getString("type");
        }


        Log.e(TAG, "onCreate:type "+type );
        Log.e(TAG, "onCreate:_id "+_id );


        //db
        sharedPreferences1 = getApplicationContext().getSharedPreferences("EGEMSS_DATA", 0);
        myDb = new DatabaseHelper(CheckInDetailsActivity.this);
        empData = myDb.getEmpdata();
        locale = new Locale(DataManger.appLanguage);


        apiViewModel = new ViewModelProvider(CheckInDetailsActivity.this).get(ApiViewModel.class);


        currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);
        currentTime.add(Calendar.DATE, 1);
        Log.e(TAG, "onCreate:hour "+hour );
        Log.e(TAG, "onCreate:minute "+minute );


        apiViewModel.gethistorydetails(getApplicationContext(), _id, sharedPreferences1.getString("company_id", null));

        ViewController.ShowProgressBar(CheckInDetailsActivity.this);


        //internet connection
        broadcastReceiver = new ConnectionReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)){
                    activityCheckInDetailsNotificationBinding.relativeInternet.getRoot().setVisibility(GONE);
                    activityCheckInDetailsNotificationBinding.relativeUi.setVisibility(View.VISIBLE);
                }else {
                    activityCheckInDetailsNotificationBinding.relativeInternet.getRoot().setVisibility(View.VISIBLE);
                    activityCheckInDetailsNotificationBinding.relativeUi.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();



        //checkin Details
        apiViewModel.gethistorydetails_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                ViewController.DismissProgressBar();
                companyData_model = response.getItems();

                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {

                        try {

                            //set data
                            if (response.getItems().getHistory().get(0).getHstatus() == 0) {
                                activityCheckInDetailsNotificationBinding.linearCheckout.setVisibility(GONE);
                                activityCheckInDetailsNotificationBinding.linearAccept.setVisibility(View.VISIBLE);
                                activityCheckInDetailsNotificationBinding.linearDecline.setVisibility(View.VISIBLE);
                                activityCheckInDetailsNotificationBinding.linearAssign.setVisibility(GONE);
                            } else if (response.getItems().getHistory().get(0).getHstatus() == 1) {
                                activityCheckInDetailsNotificationBinding.linearCheckout.setVisibility(GONE);
                                activityCheckInDetailsNotificationBinding.linearAccept.setVisibility(GONE);
                                activityCheckInDetailsNotificationBinding.linearDecline.setVisibility(GONE);
                                activityCheckInDetailsNotificationBinding.linearAssign.setVisibility(View.VISIBLE);

                                if (response.getItems().getHistory().get(0).getStart().equals("") && response.getItems().getHistory().get(0).getEnd().equals("")){
                                    //CheckIn date and time
                                    activityCheckInDetailsNotificationBinding.linaerCheckInDate.setVisibility(View.VISIBLE);
                                    long checkinTime = (Long.parseLong(response.getItems().getHistory().get(0).getDatetime().get$numberLong()) * 1000);
                                    String s_time = Conversions.millitotime((response.getItems().getHistory().get(0).getStart() + Conversions.timezone()) * 1000, false);
                                    String e_time = Conversions.millitotime((response.getItems().getHistory().get(0).getEnd() + 1 + Conversions.timezone()) * 1000, false);
                                    activityCheckInDetailsNotificationBinding.txtDateTime.setText(ViewController.Date_month_year(checkinTime) + ", " + s_time + " to " + e_time);
                                }

                                //location
                                activityCheckInDetailsNotificationBinding.linaerLocationWhere.setVisibility(View.VISIBLE);
                                activityCheckInDetailsNotificationBinding.txtLocation.setText(response.getItems().getMeetingrooms().getName()+", "+response.getItems().getLocations().getName());


                            } else if (response.getItems().getHistory().get(0).getHstatus() == 2) {
                                activityCheckInDetailsNotificationBinding.linearCheckout.setVisibility(GONE);
                                activityCheckInDetailsNotificationBinding.linearAccept.setVisibility(View.VISIBLE);
                                activityCheckInDetailsNotificationBinding.linearDecline.setVisibility(GONE);
                                activityCheckInDetailsNotificationBinding.linearAssign.setVisibility(GONE);
                            } else {
                                activityCheckInDetailsNotificationBinding.linearCheckout.setVisibility(GONE);
                                activityCheckInDetailsNotificationBinding.linearAccept.setVisibility(GONE);
                                activityCheckInDetailsNotificationBinding.linearDecline.setVisibility(GONE);
                                activityCheckInDetailsNotificationBinding.linearAssign.setVisibility(GONE);
                            }


                            id = response.getItems().getHistory().get(0).getUser_id();
                            vlocation = response.getItems().getHistory().get(0).getVlocation();
                            Emp_id = response.getItems().get_id().get$oid();
                            email = response.getItems().getEmail();
                            name = response.getItems().getName();
                            request = response.getItems().getHistory().get(0).getRequest().get$numberLong();
                            checkin = response.getItems().getHistory().get(0).getCheckin();
                            history_host = response.getItems().getHistory().get(0).getHost();
                            Supertype = response.getItems().getSupertype();


                            //stamp
                            if (response.getItems().getHistory().get(0).getHost().equalsIgnoreCase(empData.getEmp_id())){
                                if (response.getItems().getHistory().get(0).getHstatus()==1){
                                    //accept stamp
                                    activityCheckInDetailsNotificationBinding.leanearStamp.setVisibility(View.VISIBLE);
                                    activityCheckInDetailsNotificationBinding.leanearStamp.setBackgroundResource(R.color.Accept);
                                    activityCheckInDetailsNotificationBinding.txtStamp.setText("This Check-in Request has been accepted by you");
                                }else if (response.getItems().getHistory().get(0).getHstatus()==2){
                                    //Decline stamp
                                    activityCheckInDetailsNotificationBinding.leanearStamp.setVisibility(View.VISIBLE);
                                    activityCheckInDetailsNotificationBinding.leanearStamp.setBackgroundResource(R.color.Cancel);
                                    activityCheckInDetailsNotificationBinding.txtStamp.setText("This Check-in Request has been declined by you");
                                }
                            }


                            long longtime = (Long.parseLong(response.getItems().getHistory().get(0).getDatetime().get$numberLong()) * 1000);
                            activityCheckInDetailsNotificationBinding.txtCreateDateTime.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime) + "");


//                          activityCheckInDetailsNotificationBinding.txtBadgeNo.setText(" " + response.getItems().getHistory().get(0).getBadge());
                            activityCheckInDetailsNotificationBinding.txtPurpose.setText(" " + response.getItems().getHistory().get(0).getPurpose());
                            activityCheckInDetailsNotificationBinding.txtName.setText(response.getItems().getName());
                            if (!response.getItems().getEmail().equalsIgnoreCase("")) {
                                activityCheckInDetailsNotificationBinding.txtEmail.setText(response.getItems().getEmail());
                            } else {
                                activityCheckInDetailsNotificationBinding.txtEmail.setVisibility(GONE);
                            }
                            activityCheckInDetailsNotificationBinding.txtPhone.setText(response.getItems().getMobile());
                            activityCheckInDetailsNotificationBinding.txtOriganization.setText(response.getItems().getCompany());


                            if (response.getItems().getHistory().get(0).getLivepic() != null && response.getItems().getHistory().get(0).getLivepic().size() != 0) {
                                Glide.with(getApplicationContext()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + response.getItems().getHistory().get(0).getLivepic().get(0))
                                        .into(activityCheckInDetailsNotificationBinding.pic);
                            }


                        } catch (IndexOutOfBoundsException e) {
                            Log.e("Exception" , e.getMessage());
                        }
                    }
                }else {
                    Conversions.errroScreen(CheckInDetailsActivity.this, "gethistorydetails");
                }

            }
        });


        //accept decline
        apiViewModel.actioncheckinout_response().observe(this, response -> {
            ViewController.DismissProgressBar();
            if (response != null) {
                Intent intent = new Intent(CheckInDetailsActivity.this, NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }else {
                Conversions.errroScreen(CheckInDetailsActivity.this, "actioncheckinout");
            }
        });

        //decline
        apiViewModel.updateappointment_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                ViewController.DismissProgressBar();
                if (response != null) {
                    Intent intent = new Intent(CheckInDetailsActivity.this, NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }else {
                    Conversions.errroScreen(CheckInDetailsActivity.this, "updateappointment");
                }
            }
        });


        //assign
        apiViewModel.getsubhierarchys(getApplicationContext(),empData.getLocation());
        apiViewModel.getsubhierarchys_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                ViewController.DismissProgressBar();
                if (response != null) {
                    departments = new ArrayList<>();
                    departments = response.getItems();
                }else {
                    Conversions.errroScreen(CheckInDetailsActivity.this, "getsubhierarchys");
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
                    Conversions.errroScreen(CheckInDetailsActivity.this, "getsearchemployees");
                }
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


        activityCheckInDetailsNotificationBinding.imgBack.setOnClickListener(this);
        activityCheckInDetailsNotificationBinding.linearAccept.setOnClickListener(this);
        activityCheckInDetailsNotificationBinding.linearDecline.setOnClickListener(this);
        activityCheckInDetailsNotificationBinding.linearAssign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                Intent intent2 = new Intent();
                setResult(RESULT_CODE, intent2);
                finish();
                break;
            case R.id.linear_accept:
                AnimationSet animation3 = Conversions.animation();
                v.startAnimation(animation3);
                Acceptpopup();
                break;
            case R.id.linear_decline:
                AnimationSet animation4 = Conversions.animation();
                v.startAnimation(animation4);
                declinepopup();
                break;
            case R.id.linear_assign:
                AnimationSet animation5 = Conversions.animation();
                v.startAnimation(animation5);
                Assignpopup();
                break;
        }
    }

    private void registoreNetWorkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    //accept
    private void Acceptpopup() {
        final Dialog dialog = new Dialog(CheckInDetailsActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.checkin_accept_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        meetingroom_spinner = dialog.findViewById(R.id.meetingroom_spinner);
        TextView txt_start_time = dialog.findViewById(R.id.txt_start_time);
        TextView txt_end_time = dialog.findViewById(R.id.txt_end_time);
        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView confirm = dialog.findViewById(R.id.confirm);


        //meetingroom list
        meetingroom_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                AnimationSet animationp = Conversions.animation();
                arg0.startAnimation(animationp);
                meetingroom_spinner.showDropDown();
            }
        });
        meetingroom_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                AnimationSet animationp = Conversions.animation();
                view.startAnimation(animationp);
                CompanyData contactModel = meetingrooms.get(index);
                m_room_ID = meetingrooms.get(index).get_id().get$oid();
            }
        });
        getmeetingrooms(empData.getLocation());


        txt_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewController.hideKeyboard(CheckInDetailsActivity.this);
                final RangeTimePickerDialog mTimePicker;
                mTimePicker = new RangeTimePickerDialog(CheckInDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        s_hour = selectedHour;
                        s_min = selectedMinute;

                        txt_start_time.setText(s_hour+":"+s_min);

                        txt_end_time.setText("");
                    }
                }, hour, minute, true);//true = 24 hour time
                mTimePicker.setMin(hour,15);
                mTimePicker.show();
            }
        });

        txt_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewController.hideKeyboard(CheckInDetailsActivity.this);
                if (txt_start_time.getText().length() == 0) {
                    new AlertDialog.Builder(CheckInDetailsActivity.this)
                            .setTitle(R.string.warning)
                            .setMessage("Select Start Time")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }else {
                    final RangeTimePickerDialog mTimePicker;
                    mTimePicker = new RangeTimePickerDialog(CheckInDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            int start_milliseconds = (s_hour * 60 * 60 * 1000) + (s_min * 60 * 1000);
                            int end_milliseconds = (selectedHour * 60 * 60 * 1000) + (selectedMinute * 60 * 1000);

                            if (start_milliseconds < end_milliseconds){
                                e_hour =  selectedHour;
                                e_min = selectedMinute;

                                txt_end_time.setText(e_hour+":"+e_min);
                            }

                        }
                    }, s_hour, s_min, true);//true = 24 hour time
                    mTimePicker.setMin(hour,15);
                    mTimePicker.show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);

                SelectedDate = ViewController.getCurrentTimeStamp();

                if (meetingroom_spinner.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Select Meeting Room",Toast.LENGTH_SHORT).show();
                }else if (txt_start_time.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Select Start Time",Toast.LENGTH_SHORT).show();
                }else if (txt_end_time.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Select End Time",Toast.LENGTH_SHORT).show();
                }else {

                    start_time_stamp = Conversions.gettimestamp(SelectedDate,s_hour + ":" + s_min);
                    end_time_stamp = Conversions.gettimestamp(SelectedDate,e_hour + ":" + e_min);

                    try {
                        accept_api();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    private void getmeetingrooms(String locationId) {

        apiViewModel.getmeetingrooms(CheckInDetailsActivity.this, locationId);

        apiViewModel.getmeetingrooms_response().observe(CheckInDetailsActivity.this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    }else if (statuscode.equals(not_verified)) {

                    }else if (statuscode.equals(successcode)) {
                        meetingrooms = new ArrayList<>();
//                        meetingrooms = response.getItems();
//                        if (getApplicationContext()!=null) {
//                            customAdapter1 = new CustomAdapter(getApplicationContext(), R.layout.row, R.id.lbl_name, meetingrooms, 0, "",true);
//                            meetingroom_spinner.setText(meetingrooms.get(0).getName());
//                            m_room_ID = meetingrooms.get(0).get_id().get$oid();
//
//
//                            meetingroom_spinner.setThreshold(1);//will start working from first character
//                            meetingroom_spinner.setAdapter(customAdapter1);//setting the adapter data into the AutoCompleteTextView
//                            meetingroom_spinner.setEnabled(true);
//                        }


                        meetingrooms = response.getItems();
                        CompanyData companyData = new CompanyData();
                        companyData.setName(empData.getName() + " Cabin");
                        CommonObject id = new CommonObject();
                        id.set$oid(empData.getEmp_id());
                        companyData.set_id(id);
                        meetingrooms.add(0, companyData);

                        meetingroom_spinner.setText(meetingrooms.get(0).getName());
                        m_room_ID = meetingrooms.get(0).get_id().get$oid();



                        customAdapter1 = new CustomAdapter(CheckInDetailsActivity.this, R.layout.row, R.id.lbl_name, meetingrooms, 0, "",true);
                        meetingroom_spinner.setThreshold(1);//will start working from first character
                        meetingroom_spinner.setAdapter(customAdapter1);//setting the adapter data into the AutoCompleteTextView
                        meetingroom_spinner.setEnabled(true);

                    }

                }else {
                    Conversions.errroScreen(CheckInDetailsActivity.this, "getmeetingrooms");
                }
            }
        });

//        apiViewModel.getmeetingrooms_response().observe(this, new Observer<Model1>() {
//            @Override
//            public void onChanged(Model1 response) {
//                meetingrooms = new ArrayList<>();
//                progress.dismiss();
//                if (response != null) {
//                    Integer statuscode = response.getResult();
//                    Integer successcode = 200, failurecode = 401, not_verified = 404;
//                    if (statuscode.equals(failurecode)) {
//
//                    } else if (statuscode.equals(not_verified)) {
//
//                    } else if (statuscode.equals(successcode)) {
//                        meetingrooms = response.getItems();
//                        CompanyData companyData = new CompanyData();
//                        companyData.setName(empData.getName() + " Cabin");
//                        CommonObject id = new CommonObject();
//                        id.set$oid(empData.getEmp_id());
//                        companyData.set_id(id);
//                        //meetingrooms.add(0, companyData);
//
//                        meetingroom_spinner.setText(meetingrooms.get(0).getName());
//                        m_room_ID = meetingrooms.get(0).get_id().get$oid();
//
//                        customAdapter1 = new CustomAdapter(CheckInDetailsActivity.this, R.layout.row, R.id.lbl_name, meetingrooms, 0, "",true);
//                        meetingroom_spinner.setThreshold(1);//will start working from first character
//                        meetingroom_spinner.setAdapter(customAdapter1);//setting the adapter data into the AutoCompleteTextView
//                        meetingroom_spinner.setEnabled(true);
//                    }
//
//                }
//            }
//        });

    }

    private void accept_api() throws JSONException {

        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONObject jsonObj_request_numberLong = new JSONObject();
        try {
            jsonObj_request_numberLong.put("$numberLong",request);
            jsonObj_.put("formtype", "accept");
            jsonObj_.put("hstatus", 1);
            jsonObj_.put("rstatus", "1");
            jsonObj_.put("id", id);
            jsonObj_.put("request", jsonObj_request_numberLong);
            jsonObj_.put("checkin", checkin);
            jsonObj_.put("host", history_host);
            jsonObj_.put("start", start_time_stamp);
            jsonObj_.put("end", end_time_stamp);
            jsonObj_.put("meetingroom", m_room_ID);
            jsonObj_.put("vlocation", vlocation);
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiViewModel.actioncheckinout(getApplicationContext(),gsonObject);
        ViewController.ShowProgressBar(CheckInDetailsActivity.this);
    }

    public static String getTimeStamp(Boolean is12hour) {
        Date date = new Date();
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
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
        mcurrentTime.setTime(date);
        mcurrentTime.set(Calendar.HOUR_OF_DAY, hour);// for 6 hour
        mcurrentTime.set(Calendar.MINUTE, minute);// for 6 hour

        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
        if(is12hour){
            sdfDate = new SimpleDateFormat("hh:mm a",locale);//dd/MM/yyyy
        }
        Date now = new Date();
        now = mcurrentTime.getTime();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    //decline
    private void declinepopup() {
        final Dialog dialog = new Dialog(CheckInDetailsActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.decline_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView submit = dialog.findViewById(R.id.submit);
        TextView cancel = dialog.findViewById(R.id.cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);

                JsonObject gsonObject = new JsonObject();
                JSONObject jsonObj_ = new JSONObject();
                JSONObject jsonObj_request_numberLong = new JSONObject();
                try {
                    jsonObj_request_numberLong.put("$numberLong",request);
                    jsonObj_.put("formtype", "decline");
                    jsonObj_.put("hstatus", 2);
                    jsonObj_.put("rstatus", "1");
                    jsonObj_.put("id", id);
                    jsonObj_.put("request", jsonObj_request_numberLong);
                    jsonObj_.put("checkin", checkin);
                    jsonObj_.put("host", history_host);
                    jsonObj_.put("vlocation", vlocation);
                    jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                    JsonParser jsonParser = new JsonParser();
                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiViewModel.actioncheckinout(getApplicationContext(),gsonObject);
                ViewController.ShowProgressBar(CheckInDetailsActivity.this);

                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //Assign
    private void Assignpopup() {
        Dialog dialog = new Dialog(CheckInDetailsActivity.this);
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
                ViewController.DismissProgressBar();
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

                if(!host.equals("")){
                    if (Supertype.equalsIgnoreCase("outside")){
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("formtype", "assign");
                            jsonObj_.put("id", _id);
                            jsonObj_.put("emp_id", host);
                            jsonObj_.put("coordinator", empData.getEmp_id());
                            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        apiViewModel.updateappointment(getApplicationContext(),gsonObject);
                    }else {
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        JSONObject jsonObj_request_numberLong = new JSONObject();
                        try {
                            jsonObj_request_numberLong.put("$numberLong",request);
                            jsonObj_.put("formtype", "assign");
                            jsonObj_.put("id",id);
                            jsonObj_.put("request", jsonObj_request_numberLong);
                            jsonObj_.put("checkin", checkin);
                            jsonObj_.put("host",host);
                            jsonObj_.put("hierarchy_indexid",hierarchy_indexid);
                            jsonObj_.put("hierarchy_id",hierarchy_id);
                            jsonObj_.put("vlocation", vlocation);
                            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        apiViewModel.actioncheckinout(getApplicationContext(),gsonObject);
                    }
                }
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