package com.provizit.Fragments;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.Activities.AppointmentDetailsNewActivity;
import com.provizit.Activities.CheckInDetailsActivity;
import com.provizit.Activities.MeetingDescriptionNewActivity;
import com.provizit.Activities.NavigationActivity;
import com.provizit.Activities.SelectedDateMeetingsActivity;
import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.Activities.SlotsActivity;
import com.provizit.Calendar.CalendarAdapter;
import com.provizit.Calendar.MyCalendar;
import com.provizit.Calendar.RecyclerTouchListener;
import com.provizit.Calendar.myCalendarData;
import com.provizit.Config.Preferences;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.provizit.Services.Model1;
import com.provizit.Utilities.AddToCalendar;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CustomAdapter;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.RoleDetails;
import com.provizit.databinding.TodayMeetingsListItemsBinding;

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingMeetingsNewFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "UpcomingMeetingsNewFrag";

    SwipeRefreshLayout refreshLayout;
    CardView card_view_progress;

    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;

    private AlertDialog ap_declineDialog;

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;

    CardView flot_bt;

    //calanderView Top
    private List<MyCalendar> calendarList = new ArrayList<>();
    private ArrayList<String> calendar1 = new ArrayList<>();
    ArrayList<CompanyData> meetings;
    ArrayList<CompanyData> meetings1;
    int defaultSelected;
    private SimpleDateFormat titledate = new SimpleDateFormat("EE, dd MMM yyyy");
    SimpleDateFormat simple1 = new SimpleDateFormat("dd MMM");
    RecyclerView.LayoutManager mLayoutManager;
    int position;
    Long stamp;
    Long SelectedDate;
    Long toDay;

    RelativeLayout relative_appointments;
    ImageView img_arrow_appointment;
    String appoinntment_open_type = "0";
    RelativeLayout relative_today;
    ImageView img_arrow_today;
    LinearLayout linear_today;
    String today_open_type = "0";
    RelativeLayout relative_upcoming;
    ImageView img_arrow_upc;
    LinearLayout linear_upcoming;
    String upcoming_open_type = "0";

    RecyclerView recyclerView1;
    TextView date, calender, upcoming_text, dateUp, today;
    Calendar day;
    private FragmentActivity ac;
    private CalendarAdapter mAdapter;

    //Department
    ArrayList<CompanyData> departments;
    CustomAdapter departmentAdapter;
    AutoCompleteTextView department_spinner;

    //Employees;
    ArrayList<CompanyData> employees;
    AutoCompleteTextView emp_spinner;
    CustomAdapter employeeAdapter;

    //Appointments
    TextView appointment_count_text;
    LinearLayout linear_appointment;
    RecyclerView recyclerview_appointment;
    ArrayList<CompanyData> toDayMeetings_Appointments;
    ArrayList<CompanyData> toDayMeetings_Appointments_short;
    AppointmentAdapter appointmentAdapter;

    //today Meetings and Upcoming
    Calendar end;
    TextView today_count_text;
    CardView today_emptycard;
    public static RecyclerView recyclerview_today;
    ArrayList<CompanyData> toDayMeetings;
    TodayMeetingAdapter todayMeetingAdapter;
    LinearLayout upcoming_l;
    TextView upcoming_count_text;
    CardView upcoming_emptycard;
    RecyclerView recyclerview_upcoming;
    UpComingMeetingAdapter upComingMeetingAdapter;

    Dialog dialog;
    String hierarchy_indexid, hierarchy_id, host;
    EditText reason;

    private boolean isPanelShown;

    ApiViewModel apiViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_meetings_new, container, false);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        card_view_progress = view.findViewById(R.id.card_view_progress);


        Log.e("resultUrukundu", "123");


        //localdata
        myDb = new DatabaseHelper(getActivity());
        sharedPreferences1 = getActivity().getSharedPreferences("EGEMSS_DATA", 0);
        empData = new EmpData();
        empData = myDb.getEmpdata();
        Log.e(TAG, "onCreateView:em " + empData.getEmp_id());


        flot_bt = view.findViewById(R.id.flot_bt);
        relative_appointments = view.findViewById(R.id.relative_appointments);
        img_arrow_appointment = view.findViewById(R.id.img_arrow_appointment);
        relative_today = view.findViewById(R.id.relative_today);
        img_arrow_today = view.findViewById(R.id.img_arrow_today);
        linear_today = view.findViewById(R.id.linear_today);
        relative_upcoming = view.findViewById(R.id.relative_upcoming);
        img_arrow_upc = view.findViewById(R.id.img_arrow_upc);
        linear_upcoming = view.findViewById(R.id.linear_upcoming);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_view);
        date = view.findViewById(R.id.date);
        calender = view.findViewById(R.id.calender);
        upcoming_text = view.findViewById(R.id.upcoming_text);
        dateUp = view.findViewById(R.id.dateUp);
        today = view.findViewById(R.id.today);

        apiViewModel = new ViewModelProvider(getActivity()).get(ApiViewModel.class);


        //notifications
        NotificationManager manager = (NotificationManager) getActivity().getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
        String meetingsId = getActivity().getIntent().getStringExtra("m_id");
        if (meetingsId != null && meetingsId.equals("")) {
            getmeetingdetails(meetingsId);
        }

        //calender
        day = Calendar.getInstance();
        day.set(Calendar.MILLISECOND, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        ac = getActivity();
        mAdapter = new CalendarAdapter(calendarList, getActivity().getWindowManager(), defaultSelected, getActivity());
        recyclerView1.setHasFixedSize(true);

        if (sharedPreferences1.getString("language", "").equals("ar")) {
            Locale locale = new Locale("ar");
            titledate = new SimpleDateFormat("EE, dd MMM yyyy", locale);
            simple1 = new SimpleDateFormat("dd MMM", locale);
        }
        date.setText(titledate.format(Calendar.getInstance().getTime()));
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                Intent intent = new Intent(getActivity(), SelectedDateMeetingsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(mLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mLayoutManager.getChildCount();
                for (int i = 0; i < totalItemCount; i++) {
                    View childView = recyclerView.getChildAt(i);
                    TextView childTextView = (TextView) (childView.findViewById(R.id.stamp));
                    String childTextViewText = (String) (childTextView.getText());
                    if (i == 0) {
                        stamp = Long.parseLong(childTextViewText);
                    }
                }
            }
        });
        recyclerView1.setAdapter(mAdapter);
        recyclerView1.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView1, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                TextView childTextView = (TextView) (view.findViewById(R.id.day_1));
                TextView childTextView1 = (TextView) (view.findViewById(R.id.date_1));

                MyCalendar calendar = calendarList.get(position);
                System.out.println(calendarList.size() + "shfjhjkashfajk");

                if (CalendarAdapter.defaultSelected != position) {

                }

                int totalItemCount = mLayoutManager.getChildCount();
                for (int i = 0; i < totalItemCount; i++) {
                    View view1 = mLayoutManager.getChildAt(i);
                    if (view1 != view) {
                        TextView childTextView11 = (TextView) (view1.findViewById(R.id.day_1));
                        TextView childTextView12 = (TextView) (view1.findViewById(R.id.date_1));
                        childTextView12.setBackgroundResource(0);
                        childTextView11.setTextColor(Color.BLACK);
                        childTextView12.setTextColor(Color.BLACK);
                    }
                }
                Animation startRotateAnimation = AnimationUtils.makeInChildBottomAnimation(getActivity().getApplicationContext());
                childTextView.startAnimation(startRotateAnimation);
                childTextView1.startAnimation(startRotateAnimation);
                Calendar end = Calendar.getInstance();
                end.setTimeInMillis(calendar.getTimemilli() * 1000);
                Calendar day = Calendar.getInstance();
                day.set(Calendar.MILLISECOND, 0);
                day.set(Calendar.SECOND, 0);
                day.set(Calendar.MINUTE, 0);
                day.set(Calendar.HOUR_OF_DAY, 0);
                if (calendar.getTimemilli() < (day.getTimeInMillis() / 1000)) {
                    end.add(Calendar.DAY_OF_MONTH, 1);
                    upcoming_text.setText(getResources().getString(R.string.past));
                } else {
                    end.add(Calendar.MONTH, 1);
                    upcoming_text.setText(getResources().getString(R.string.upcoming));
                }

                dateUp.setText(simple1.format(calendar.getTimemilli() * 1000));
                date.setText(titledate.format(calendar.getTimemilli() * 1000));

                if (calendar.getTimemilli() - Conversions.timezone() == toDay) {
                    SelectedDate = toDay;
                    System.out.println("stafsghfhga");
                    //todays meetings and upcoming
                    getoappointments(calendar.getTimemilli() - Conversions.timezone() + "", (end.getTimeInMillis() / 1000) - Conversions.timezone() + "");
                    dateUp.setText("");
                } else {
                    SelectedDate = null;
                    Log.e(TAG, "onClick:dsada" + "other");
                    System.out.println("gasgds");
                    getmeetings(calendar.getTimemilli() - Conversions.timezone() + "", (end.getTimeInMillis() / 1000) - Conversions.timezone() + "");
                }
                CalendarAdapter.defaultSelected = position;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        prepareCalendarData();
        end = Calendar.getInstance();
        end.add(Calendar.MONTH, 1);
        toDay = (new myCalendarData(0).getTimeMilli()) - Conversions.timezone();
        SelectedDate = toDay;
        RoleDetails roledata = myDb.getRole();
        if (roledata.getSmeeting().equals("false")) {
            flot_bt.setVisibility(GONE);
        }

        //department
        getsubhierarchys(empData.getLocation());

        //Appointments
        appointment_count_text = view.findViewById(R.id.appointment_count_text);
        linear_appointment = view.findViewById(R.id.linear_appointment);
        recyclerview_appointment = view.findViewById(R.id.recyclerview_appointment);
        getoappointments(toDay + "", (end.getTimeInMillis() / 1000) - Conversions.timezone() + "");

        //today Meetings and Upcoming
        toDayMeetings = new ArrayList<>();
        meetings = new ArrayList<>();
        meetings1 = new ArrayList<>();
        today_count_text = view.findViewById(R.id.today_count_text);
        today_emptycard = view.findViewById(R.id.today_emptycard);
        recyclerview_today = view.findViewById(R.id.recyclerview_today);
        upcoming_l = view.findViewById(R.id.upcoming_l);
        upcoming_count_text = view.findViewById(R.id.upcoming_count_text);
        upcoming_emptycard = view.findViewById(R.id.upcoming_emptycard);
        recyclerview_upcoming = view.findViewById(R.id.recyclerview_upcoming);

        isPanelShown = false;

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upcoming_text.setText(getResources().getString(R.string.upcoming));

                SelectedDate = toDay;
                if (meetingsId != null && meetingsId.equals("")) {
                    getmeetingdetails(meetingsId);
                }

                prepareCalendarData();
                //department
                getsubhierarchys(empData.getLocation());

                //appointment
                getoappointments(toDay + "", (end.getTimeInMillis() / 1000) - Conversions.timezone() + "");
                date.setText(titledate.format(Calendar.getInstance().getTime()));
                refreshLayout.setRefreshing(false);
            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation3 = Conversions.animation();
                v.startAnimation(animation3);
                upcoming_text.setText(getResources().getString(R.string.upcoming));

                SelectedDate = toDay;
                prepareCalendarData();
                //department
                getsubhierarchys(empData.getLocation());

                //appointment
                getoappointments(toDay + "", (end.getTimeInMillis() / 1000) - Conversions.timezone() + "");
                date.setText(titledate.format(Calendar.getInstance().getTime()));
            }
        });

        //fab.setOnClickListener(this);
        relative_appointments.setOnClickListener(this);
        relative_today.setOnClickListener(this);
        relative_upcoming.setOnClickListener(this);
        flot_bt.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_appointments:
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                if (appoinntment_open_type.equalsIgnoreCase("0")) {
                    img_arrow_appointment.animate().rotation(180).start();
                    appoinntment_open_type = "1";
//                 set animation
                    Animation bottomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
                    recyclerview_appointment.startAnimation(bottomUp);
                    recyclerview_appointment.setVisibility(View.VISIBLE);
                    //other
                    linear_today.setVisibility(GONE);
                    linear_upcoming.setVisibility(GONE);
                    img_arrow_today.animate().rotation(0).start();
                    img_arrow_upc.animate().rotation(0).start();
                    today_open_type = "0";
                    upcoming_open_type = "0";
                } else {
                    img_arrow_appointment.animate().rotation(0).start();
                    appoinntment_open_type = "0";
                    // Hide animation
                    Animation bottomDown = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
                    recyclerview_appointment.startAnimation(bottomDown);
                    recyclerview_appointment.setVisibility(GONE);
                }
                break;
            case R.id.relative_today:
                AnimationSet animation1 = Conversions.animation();
                v.startAnimation(animation1);
                if (today_open_type.equalsIgnoreCase("0")) {
                    img_arrow_today.animate().rotation(180).start();
                    today_open_type = "1";
                    linear_today.setVisibility(View.VISIBLE);
                    // set animation
                    Animation bottomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
                    linear_today.startAnimation(bottomUp);
                    linear_today.setVisibility(View.VISIBLE);
                    //other
                    recyclerview_appointment.setVisibility(GONE);
                    linear_upcoming.setVisibility(GONE);
                    img_arrow_appointment.animate().rotation(0).start();
                    img_arrow_upc.animate().rotation(0).start();
                    appoinntment_open_type = "0";
                    upcoming_open_type = "0";
                } else {
                    img_arrow_today.animate().rotation(0).start();
                    today_open_type = "0";
                    // Hide animation
                    Animation bottomDown = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
                    linear_today.startAnimation(bottomDown);
                    linear_today.setVisibility(GONE);
                }
                break;
            case R.id.relative_upcoming:
                AnimationSet animation2 = Conversions.animation();
                v.startAnimation(animation2);
                if (upcoming_open_type.equalsIgnoreCase("0")) {
                    img_arrow_upc.animate().rotation(180).start();
                    upcoming_open_type = "1";
                    // set animation
                    Animation bottomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
                    linear_upcoming.startAnimation(bottomUp);
                    linear_upcoming.setVisibility(View.VISIBLE);
                    //other
                    linear_today.setVisibility(GONE);
                    recyclerview_appointment.setVisibility(GONE);
                    img_arrow_appointment.animate().rotation(0).start();
                    img_arrow_today.animate().rotation(0).start();
                    today_open_type = "0";
                    appoinntment_open_type = "0";
                } else {
                    img_arrow_upc.animate().rotation(0).start();
                    upcoming_open_type = "0";
                    // Hide animation
                    Animation bottomDown = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
                    linear_upcoming.startAnimation(bottomDown);
                    linear_upcoming.setVisibility(GONE);
                }
                break;
            case R.id.flot_bt:
                AnimationSet animation4 = Conversions.animation();
                v.startAnimation(animation4);
                Intent intent = new Intent(getActivity(), SetupMeetingActivity.class);
                intent.putExtra("RMS_STATUS", 2);
                intent.putExtra("RMS_Date", SelectedDate);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
        }
    }

    private void prepareCalendarData() {
        myCalendarData m_calendar = new myCalendarData(-1);
        for (int i = 0; i <= 730; i++) {
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), m_calendar.getDay(), String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i, m_calendar.getTimeMilli());
            calendar1.add(m_calendar.calendar());
            m_calendar.getNextWeekDay(1);
            calendarList.add(calendar);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        position = calendar1.indexOf(formatter.format(calendar.getTime()));
        recyclerView1.scrollToPosition(position - 3);
        CalendarAdapter.defaultSelected = position;
        mAdapter.notifyDataSetChanged();
    }

    private void getmeetingdetails(String id) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmeetingdetails(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Model model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        CompanyData Popupdata = model.getItems();

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.checkin_visitor_popup);
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        final TextView submit;
                        final TextView v_name, purpose, email, mobile, cancel, department, company, tomeet, badge, time;
                        final CircleImageView pic;
                        cancel = dialog.findViewById(R.id.cancel);
                        pic = dialog.findViewById(R.id.pic);
                        purpose = dialog.findViewById(R.id.purpose);
                        company = dialog.findViewById(R.id.company);
                        email = dialog.findViewById(R.id.email);
                        mobile = dialog.findViewById(R.id.mobile);
                        v_name = dialog.findViewById(R.id.name);
                        badge = dialog.findViewById(R.id.badge);
                        time = dialog.findViewById(R.id.c_time);
                        purpose.setText(Popupdata.getHistory().get(0).getPurpose());
                        badge.setText(Popupdata.getHistory().get(0).getBadge());
                        v_name.setText(Popupdata.getName());
                        company.setText(Popupdata.getCompany());
                        mobile.setText(Popupdata.getMobile());
                        email.setText(Popupdata.getEmail());
                        if (Popupdata.getHistory().get(0).getLivepic() != null && Popupdata.getHistory().get(0).getLivepic().size() != 0) {
                            Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + Popupdata.getHistory().get(0).getLivepic().get(0))
                                    .into(pic);
                        }
                        String s_time1 = Conversions.millitotime((Long.parseLong(Popupdata.getHistory().get(0).getRequest().get$numberLong())) * 1000, false);
                        time.setText(s_time1);
                        System.out.println("subfshfb" + s_time1);
                        if (Popupdata.getHistory().get(0).getHstatus() != 0 && Popupdata.getHistory().get(0).getCheckin() != 0 && Popupdata.getHistory().get(0).getStatus() == 0) {
                            s_time1 = Conversions.millitotime((Long.parseLong(Popupdata.getHistory().get(0).getCheckin().toString())) * 1000, false);
                            time.setText(s_time1);
                            System.out.println("subfshfb" + s_time1);
                        }

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                System.out.println("asdf1" + t);
                Toast.makeText(getActivity(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
            }
        }, getActivity(), id);
    }

    //Department
    private void getsubhierarchys(String l_id) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getsubhierarchys(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                final Model1 model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        departments = new ArrayList<>();
                        departments = model.getItems();
                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
            }
        }, getActivity(), l_id);
    }

    //Employees
    private void getsearchemployees(String l_id, String h_id) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getsearchemployees(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                final Model1 model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        employees = new ArrayList<>();
                        employees = model.getItems();
                        for (int i = 0; i < employees.size(); i++) {
                            if (employees.get(i).getEmail().equals(empData.getEmail())) {
                                employees.remove(employees.get(i));
                                break;
                            }
                        }
                        if (employees.size() == 0) {
                            department_spinner.setText("");
                        } else {
                            employeeAdapter = new CustomAdapter(getActivity(), R.layout.row, R.id.lbl_name, employees, 0, "");
                            emp_spinner.setThreshold(0);//will start working from first character
                            emp_spinner.setAdapter(employeeAdapter);//setting the adapter data into the AutoCompleteTextView
                            emp_spinner.setEnabled(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                System.out.println("Subahsabd " + t);
                Toast.makeText(getActivity(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
            }
        }, getActivity(), l_id, h_id);
    }

    //Appointment List
    private void getoappointments(String s_time, String e_time) {
        card_view_progress.setVisibility(VISIBLE);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getoappointments(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                Model1 model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {

                        toDayMeetings.clear();
                        meetings.clear();
                        meetings1.clear();
                        toDayMeetings_Appointments = new ArrayList<>();
                        toDayMeetings_Appointments.clear();
                        toDayMeetings_Appointments_short = new ArrayList<>();
                        toDayMeetings_Appointments_short.clear();

                        for (int j = 0; j < model.getItems().size(); j++) {
                            if (SelectedDate == toDay) {
                                //Toast.makeText(getActivity(),SelectedDate+" - "+toDay,Toast.LENGTH_SHORT).show();
                                if (model.getItems().get(j).getStatus() <= 1) {
                                    String soort_stamp = model.getItems().get(j).getA_time().get$numberLong();
                                    model.getItems().get(j).setSort_stamp(soort_stamp);
                                    toDayMeetings_Appointments.add(model.getItems().get(j));
                                    Log.e("ppointments_getItems" , model.getItems().get(j).getSupertype());
                                }
                            } else {
//                                toDayMeetings_Appointments = model.getItems();
//                                String soort_stamp = toDayMeetings_Appointments.get(j).getA_time().get$numberLong();
//                                toDayMeetings_Appointments.get(j).setSort_stamp(soort_stamp);
                            }
                        }

                        getvisitorslist(s_time, e_time);
                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                card_view_progress.setVisibility(GONE);
                System.out.println("asdf2" + t);
            }
        }, getActivity(), "", "host", "", empData.getEmp_id());
    }

    //Today Meetings and Upcoming CheckIn
    private void getvisitorslist(String s_time, String e_time) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getvisitorslist(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                Model1 model = response.body();

                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        if (model.getItems() != null) {
                            for (int i = 0; i < model.getItems().size(); i++) {
                                if (model.getItems().get(i).getCreated_time() != null) {
                                    String soort_stamp = model.getItems().get(i).getCreated_time().get$numberLong();

                                    if (toDayMeetings_Appointments.size() > model.getItems().size()) {
                                        toDayMeetings_Appointments.get(i).setSort_stamp(soort_stamp);
                                        Log.e("ppointments_getItems1" , model.getItems().get(i).getSupertype());
                                    }

                                }

                                if (model.getItems().get(i).getHistory().get(0).getHstatus() == 0 || model.getItems().get(i).getHistory().get(0).getHstatus() == 2) {
                                    toDayMeetings_Appointments.add(model.getItems().get(i));
                                    Log.e("ppointments_getItems2" , model.getItems().get(i).getSupertype());
                                } else {
                                    toDayMeetings.add(model.getItems().get(i));
                                }

                            }
                        }
                        getmeetings(s_time, e_time);
                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                System.out.println("asdf2" + t);
                card_view_progress.setVisibility(GONE);
            }
        }, getActivity(), "", empData.getEmp_id(), "today", "checkinn", "checkin");
    }

    private void getmeetings(String s_time, String e_time) {
        card_view_progress.setVisibility(VISIBLE);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmeetings(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                Model1 model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        meetings = model.getItems();
//                        if (model.getItems()!=null){
//                            for (int i = 0; i < model.getItems().size(); i++) {
//                                if (model.getItems().get(i).getCreated_time()!=null){
//                                    String soort_stamp = model.getItems().get(i).getCreated_time().get$numberLong();
//                                    toDayMeetings_Appointments.get(i).setSort_stamp(soort_stamp);
//                                }
//                            }
//                        }
                        getmeetingrequests(s_time, e_time);
                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                System.out.println("asdf2" + t);
                card_view_progress.setVisibility(GONE);
            }
        }, getActivity(), "custom", empData.getEmp_id(), s_time, e_time);
    }

    private void getmeetingrequests(String s_time, String e_time) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmeetingrequests(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                Model1 model = response.body();

                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        meetings1 = model.getItems();

//                        if (model.getItems()!=null){
//                            for (int i = 0; i < model.getItems().size(); i++) {
//                                if (model.getItems().get(i).getCreated_time()!=null){
//                                    String soort_stamp = model.getItems().get(i).getCreated_time().get$numberLong();
//                                    toDayMeetings_Appointments.get(i).setSort_stamp(soort_stamp);
//                                }
//                            }
//                        }

                        meetings.addAll(meetings1);
                        getemployeeslots(s_time, e_time);

                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                System.out.println("asdf1" + t);
                card_view_progress.setVisibility(GONE);
            }
        }, getActivity(), "custom", empData.getEmail(), s_time, e_time);
    }

    private void getemployeeslots(String s_time, String e_time) {
        long end = 0;
        long date = 0;
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getemployeeslots(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                Model1 model = response.body();

                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        toDayMeetings.addAll(model.getItems());
//                        for (int i = 0; i < model.getItems().size(); i++) {
//                            toDayMeetings.add(model.getItems().get(i));
//                        }
                    }
                }
                getmeetingroomapprovals(s_time, e_time);
                // getmeetingapprovals(s_time, e_time);
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                System.out.println("asdf1" + t);
                card_view_progress.setVisibility(GONE);
            }
        }, getActivity(), "", empData.getLocation(), "", empData.getEmp_id(), "date", date, end, toDay);
    }

    private void getmeetingroomapprovals(String s_time, String e_time) {
        DataManger dataManager = DataManger.getDataManager();

        dataManager.getmeetingroomapprovals(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                Model1 model = response.body();

                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        toDayMeetings_Appointments.addAll(model.getItems());
                        Log.e("ppointments_getItems3" , model.getItems()+"");
                    }
                }
                getmeetingapprovals(s_time, e_time);
                String AdOnline = Preferences.loadStringValue(getActivity(), Preferences.AdOnline, "");

                if (AdOnline.equalsIgnoreCase("true")){
                    getoutlookappointments(s_time, e_time);
                }

            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.d("", t + "");
                card_view_progress.setVisibility(GONE);
            }
        }, getActivity(), "upcoming", empData.getEmp_id(), empData.getLocation(), empData.getHierarchy_id(), s_time, e_time);
    }


    private void getoutlookappointments(String s_time, String e_time) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getoutlookappointments(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                Model1 model = response.body();
                Log.e("meetings_model_",model+"");
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    Log.e("meetings_statuscode_",statuscode+"");
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        meetings.addAll(model.getItems());
                        Log.e("meetings_size_",meetings.size()+"");
                    }
                }

                //getmeetingapprovals(s_time, e_time);
            }
            @Override
            public void onFailure(Call<Model1> call,  Throwable t) {
                System.out.println("getoutlookappointments_" + t);
                Log.e("meetings_model_","12345");
            }
        }, getActivity(), "upcoming","zaldawsari@saudiexports.gov.sa", s_time, e_time, sharedPreferences1.getString("company_id", null));
    }

    private void getmeetingapprovals(String s_time, String e_time) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.getmeetingapprovals(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                card_view_progress.setVisibility(GONE);
                Model1 model = response.body();

                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {

                        meetings1 = model.getItems();

//                        if (model.getItems()!=null){
//                            for (int i = 0; i < model.getItems().size(); i++) {
//                                if (model.getItems().get(i).getCreated_time()!=null){
//                                    String soort_stamp = model.getItems().get(i).getCreated_time().get$numberLong();
//                                    toDayMeetings_Appointments.get(i).setSort_stamp(soort_stamp);
//                                }
//                            }
//                        }

                        meetings.addAll(meetings1);
                        if (getActivity() != null) {
                            new AddToCalendar(getActivity()).execute(meetings);
                        }
                        meetings = insertionSort(meetings);
                        meetings1.clear();
                        meetings1.addAll(meetings);

                        meetings1 = insertionSort(meetings1);

                        Calendar cal = Calendar.getInstance();
                        if (s_time.equals(toDay + "")) {
                            //today
                            System.out.println("size123 " + meetings.size());
                            for (int i = 0; i < meetings.size(); i++) {
                                System.out.println("Subhashbhasgf" + meetings.get(i).getDate());
                                if ((meetings.get(i).getDate() >= toDay && (meetings.get(i).getDate() < (toDay + (60 * 60 * 24 * 1))))) {
                                    if (meetings.get(i).getEnd() >= cal.getTimeInMillis() / 1000 - Conversions.timezone()) {
                                        if (empData.getRoleid().equalsIgnoreCase(meetings.get(i).getApprover_roleid())) {
                                            if (meetings1.size() != 0){
                                                toDayMeetings_Appointments.add(meetings.get(i));
                                                Log.e(TAG, "toDayMeetings_Appointments:asda" + "1");
                                            }
                                        } else {
                                            toDayMeetings.add(meetings.get(i));
                                            Log.e(TAG, "onResponse:asda" + "124");
                                        }
                                    }
                                    Log.e(TAG, "onResponse:asda" + "125");
                                    meetings1.remove(meetings.get(i));
                                }else {
                                    Log.e(TAG, "onResponse:asda" + "126");
                                    if (empData.getRoleid().equalsIgnoreCase(meetings.get(i).getApprover_roleid())) {
                                        if (meetings.size() != 0){
                                            toDayMeetings_Appointments.add(meetings.get(i));
                                            Log.e(TAG, "toDayMeetings_Appointments:asda" + "2");
                                            meetings1.remove(meetings.get(i));
                                        }
                                    }
                                }
                            }

                            //today
                            todayMeetingAdapter = new TodayMeetingAdapter(getActivity(), toDayMeetings);
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                            recyclerview_today.setLayoutManager(manager);
                            //set adapter
                            recyclerview_today.setAdapter(todayMeetingAdapter);
                            //count
                            today_count_text.setText("(" + toDayMeetings.size() + ")");
                            if (toDayMeetings.size() == 0) {
//                                 DateFormat simple = new SimpleDateFormat("dd MMM");
//                                 date1.setText(simple.format(Calendar.getInstance().getTime()));
                                recyclerview_today.setVisibility(GONE);
                                today_emptycard.setVisibility(View.VISIBLE);
                            } else {
                                recyclerview_today.setVisibility(View.VISIBLE);
                                today_emptycard.setVisibility(GONE);
                            }
                        }


                        Log.e("meetings1_size_",meetings1.size()+"");
                        //upcoming
                        upComingMeetingAdapter = new UpComingMeetingAdapter(getActivity(), meetings1);
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        recyclerview_upcoming.setLayoutManager(manager);
                        //set adapter
                        recyclerview_upcoming.setAdapter(upComingMeetingAdapter);


                        //count
                        upcoming_count_text.setText("(" + meetings1.size() + ")");
                        if (meetings1.size() == 0) {
                            recyclerview_upcoming.setVisibility(GONE);
                            upcoming_emptycard.setVisibility(View.VISIBLE);
                        } else {
                            recyclerview_upcoming.setVisibility(View.VISIBLE);
                            upcoming_emptycard.setVisibility(GONE);
                        }

                        //appointments list
                        toDayMeetings_Appointments_short.clear();
                        toDayMeetings_Appointments_short.addAll(toDayMeetings_Appointments);
                        toDayMeetings_Appointments_short = appoinment_insertionSort(toDayMeetings_Appointments_short);

                        if (toDayMeetings_Appointments.size() == 0) {
                            appoinntment_open_type = "0";
                            linear_appointment.setVisibility(GONE);
                            //icon rotation
                            img_arrow_appointment.animate().rotation(0).start();
                            img_arrow_upc.animate().rotation(0).start();
                            img_arrow_today.animate().rotation(180).start();
                            //other View
                            today_open_type = "1";
                            upcoming_open_type = "0";
                            linear_today.setVisibility(View.VISIBLE);
                            linear_upcoming.setVisibility(GONE);
                        } else {
                            linear_appointment.setVisibility(View.VISIBLE);
                            //icon rotation
                            img_arrow_appointment.animate().rotation(180).start();
                            appoinntment_open_type = "1";
                            for (int i = 0; i < toDayMeetings_Appointments.size(); i++) {
                                Log.e(TAG, "onResponse:toDayMeetings_Appointments__ " + toDayMeetings_Appointments);
                                appointmentAdapter = new AppointmentAdapter(getActivity(), toDayMeetings_Appointments);
                                LinearLayoutManager managera = new LinearLayoutManager(getActivity());
                                managera.setReverseLayout(true);
                                managera.setStackFromEnd(true);
                                recyclerview_appointment.setLayoutManager(managera);
                                //set adapter
                                recyclerview_appointment.setAdapter(appointmentAdapter);
                                Log.e(TAG, "onResponse:getStatus " + toDayMeetings_Appointments.get(i).getStatus());
                                Log.e(TAG, "onResponse:getStatus " + toDayMeetings_Appointments.get(i).getStatus());
                            }

                        }



                    }
                }

            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.d("", t + "");
                card_view_progress.setVisibility(GONE);
            }
        }, getActivity(), "custom", empData.getEmp_id(), empData.getRoleid(), empData.getLocation(), empData.getHierarchy_id(), s_time, e_time);
    }

    public ArrayList<CompanyData> insertionSort(ArrayList<CompanyData> meetingsA) {
        ArrayList<CompanyData> meetingsSort = new ArrayList<>();
        meetingsSort = meetingsA;
        int n = meetingsSort.size();
        int i, j;
        CompanyData key;
        for (i = 1; i < n; i++) {
            key = meetingsSort.get(i);
            Log.e(TAG, "insertionSort: " + meetingsSort.get(i).getStart());
            Log.e(TAG, "insertionSort:key " + key.getStart());
            j = i - 1;
            while (j >= 0 && meetingsSort.get(j).getStart() > key.getStart()) {
                meetingsSort.set(j + 1, meetingsSort.get(j));
                j = j - 1;
            }
            meetingsSort.set(j + 1, key);
        }
        return meetingsSort;
    }

    public class TodayMeetingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<CompanyData> todayMeetings;
        private Context context;

        public TodayMeetingAdapter(Context context, ArrayList<CompanyData> todayMeetings) {
            this.todayMeetings = todayMeetings;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TodayMeetingsListItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.today_meetings_list_items, parent, false);
            return new ViewHolderToday(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            CompanyData itemData = todayMeetings.get(position);
            ViewHolderToday Holder = (ViewHolderToday) holder;
            Holder.bindItem(itemData);
        }

        @Override
        public int getItemCount() {
            //count
            today_count_text.setText("(" + todayMeetings.size() + ")");
            if (todayMeetings.size() > 3) {
                //3 above
                ViewGroup.LayoutParams params = recyclerview_today.getLayoutParams();
                params.height = 600;
                recyclerview_today.setLayoutParams(params);
            } else {
                //3 below
            }
            return todayMeetings.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolderToday extends RecyclerView.ViewHolder {

            TodayMeetingsListItemsBinding binding;

            public ViewHolderToday(TodayMeetingsListItemsBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bindItem(CompanyData itemData) {

                Log.e(TAG, "onBindViewHolder:history:stype " + itemData.getSupertype());

                try {

                    if (itemData.getSupertype().equalsIgnoreCase("meeting")) {

                        binding.cardViewMeeting.setVisibility(View.VISIBLE);
                        binding.cardviewCheckin.setVisibility(GONE);
                        binding.cardviewVisitor.setVisibility(GONE);

                        if (itemData.getRecurrence()) {
                            binding.imgReccurence.setVisibility(VISIBLE);
                        }

                        if (empData.getEmp_id().equalsIgnoreCase(itemData.getEmp_id())) {
                        } else {
                            for (int i = 0; i < itemData.getInvites().size(); i++) {
                                if (itemData.getInvites().get(i).getView_status() == false) {
                                    binding.host.setTypeface(null, Typeface.BOLD);
                                    binding.subject.setTypeface(null, Typeface.BOLD);
                                    binding.viziter.setTypeface(null, Typeface.BOLD);
                                } else {
                                }
                            }
                        }

                        if (itemData.getV_id() == null || itemData.getV_id().equalsIgnoreCase("")) {
                            binding.titleType.setText(R.string.meeting);
                        } else {
                            binding.titleType.setText(R.string.appointment_p);
                        }

                        binding.mType.setBackgroundColor(getResources().getColor(R.color.white));
                        if (itemData.getEmployee().getPic().equals("null")) {

                        } else {
                            if (itemData.getEmployee().getPic() != null && itemData.getEmployee().getPic().size() != 0) {
                                String url = DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + itemData.getEmployee().getPic().get(itemData.getEmployee().getPic().size() - 1);

                                Glide.with(context)
                                        .load(url)
                                        .placeholder(R.drawable.ic_user)  // Placeholder image while loading
                                        .error(R.drawable.ic_user)       // Image to display on error
                                        .into(binding.pic);

                                Log.e("img_1", url );
                            }

                        }

                        if (itemData.getInvites().get(0).getName() != null) {
                            binding.viziter.setText(itemData.getInvites().get(0).getName() + "");
                        } else {
                            binding.viziter.setText(itemData.getInvites().get(0).getEmail() + "");
                        }

                        binding.subject.setText(Conversions.Capitalize(itemData.getSubject()));
                        binding.host.setText(" " + itemData.getEmployee().getName());

                        //meeting time
                        String s_time = Conversions.millitotime((itemData.getStart() + Conversions.timezone()) * 1000, false);
                        binding.date.setText(s_time);
                        String e_time = Conversions.millitotime((itemData.getEnd() + 1 + Conversions.timezone()) * 1000, false);
                        binding.sTime.setVisibility(GONE);
                        Long date_ = (itemData.getStart() + Conversions.timezone()) * 1000;
                        binding.meetingTime.setText(ViewController.Date_month_year(date_) + ", " + s_time + " To " + e_time);


                        //create date and time
                        long longtime = (Long.parseLong(String.valueOf(Long.parseLong(itemData.getCreated_time().get$numberLong()) * 1000)));
                        binding.createTime.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime) + "");


                        if (empData.getEmp_id().equals(itemData.getEmp_id())) {
                            binding.host.setText(" Me");
                            binding.mType.setBackgroundColor(getResources().getColor(R.color.white));
                            if (itemData.getInvites().get(0).getPic().size() != 0) {
                                Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + itemData.getInvites().get(0).getPic().get(itemData.getInvites().get(0).getPic().size() - 1))
                                        .into(binding.pic);
                            }
                        } else {
                            if (itemData.getEmployee().getPics().size() != 0) {
                                Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + itemData.getEmployee().getPics().get(itemData.getEmployee().getPics().size() - 1))
                                        .into(binding.pic);
                            }
                        }
                        if (itemData.getInvites().size() > 1) {
                            binding.txtCount.setVisibility(View.VISIBLE);
                            binding.txtCount.setText("+" + (itemData.getInvites().size() - 1));
                        }
                        if (itemData.getStatus() == 1) {
                            // Cancel
                            binding.mStatus.setVisibility(View.VISIBLE);
                            binding.mStatusColor.setVisibility(View.VISIBLE);
                            binding.mStatus.setText(R.string.Cancelled);
                            GradientDrawable drawable = (GradientDrawable) binding.mStatusColor.getBackground();
                            drawable.setStroke(3, getResources().getColor(R.color.Cancel)); // set stroke width and stroke color
                            binding.mStatus.setBackgroundColor(getResources().getColor(R.color.Cancel));
                        } else {
                            for (int i = 0; i < itemData.getInvites().size(); i++) {
                                GradientDrawable drawable = (GradientDrawable) binding.mStatusColor.getBackground();
                                if (empData.getEmail().equals(itemData.getInvites().get(i).getEmail())) {

                                    // Pending
                                    binding.mStatus.setVisibility(View.VISIBLE);
                                    binding.mStatusColor.setVisibility(View.VISIBLE);
                                    binding.mStatus.setBackgroundColor(getResources().getColor(R.color.Pending));
                                    drawable.setStroke(3, getResources().getColor(R.color.Pending)); // set stroke width and stroke color
                                    binding.mStatus.setText(R.string.pending);
                                    if (itemData.getInvites().get(i).getStatus() == 3) {
                                        //Accepted
                                        drawable.setStroke(3, getResources().getColor(R.color.Accept)); // set stroke width and stroke color
                                        binding.mStatus.setBackgroundColor(getResources().getColor(R.color.Accept));
                                        binding.mStatus.setText(R.string.accepted);

                                    } else if (itemData.getInvites().get(i).getStatus() == 1) {
                                        //declined
                                        drawable.setStroke(3, getResources().getColor(R.color.Decline)); // set stroke width and stroke color
                                        binding.mStatus.setBackgroundColor(getResources().getColor(R.color.Decline));
                                        binding.mStatus.setText(R.string.declined);
                                    }
                                    break;
                                } else {
                                    binding.mStatus.setVisibility(GONE);
                                    binding.mStatusColor.setVisibility(GONE);
                                }
                            }
                        }

                        binding.cardViewMeeting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AnimationSet animation = Conversions.animation();
                                v.startAnimation(animation);
                                Log.e(TAG, "onClick:meetingsd " + itemData);

                                Intent intent = new Intent(context, MeetingDescriptionNewActivity.class);
                                intent.putExtra("m_id", itemData.get_id().get$oid());
                                startActivityForResult(intent, REQUEST_CODE);
                                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                            }
                        });
                        if (empData.getRoleid().equals(itemData.getApprover_roleid())) {
                            binding.mType.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    } else if (itemData.getSupertype().equalsIgnoreCase("userslots")) {
                        binding.cardviewVisitor.setVisibility(VISIBLE);
                        binding.cardViewMeeting.setVisibility(GONE);
                        binding.cardviewCheckin.setVisibility(GONE);


                        binding.visitorNamehost.setText(itemData.getUserDetails().getName());
                        binding.vSubject.setText(itemData.getPurposeDetails().getName());


                        Long date_ = (itemData.getStart() + Conversions.timezone()) * 1000;
                        String s_time = Conversions.millitotime((itemData.getStart() + Conversions.timezone()) * 1000, false);
                        String e_time = Conversions.millitotime((itemData.getEnd() + 1 + Conversions.timezone()) * 1000, false);
                        binding.vTime.setText(ViewController.Date_month_year(date_) + ", " + s_time + " To " + e_time);


                        //create date and time
                        long longtime = (Long.parseLong(String.valueOf(Long.parseLong(itemData.getCreated_time().get$numberLong()) * 1000)));
                        binding.vCreateTime.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime));


                        binding.cardviewVisitor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AnimationSet animation = Conversions.animation();
                                v.startAnimation(animation);
                                Intent intent = new Intent(getActivity(), SlotsActivity.class);
                                intent.putExtra("id", itemData.get_id().get$oid());
                                startActivity(intent);
                            }
                        });
                    }else if (itemData.getSupertype().equalsIgnoreCase("exchange")) {
                        Log.e(TAG, "onBindViewHolder:super: " + itemData.getSupertype());
                    }else {

                        //checkin
                        Log.e(TAG, "onBindViewHolder:history: " + itemData.getHistory().get(0));

                        binding.cardviewCheckin.setVisibility(View.VISIBLE);
                        binding.cardViewMeeting.setVisibility(GONE);
                        binding.cardviewVisitor.setVisibility(GONE);

                        binding.checkout.setVisibility(GONE);
                        binding.decline.setVisibility(GONE);
                        binding.accept.setVisibility(GONE);
                        binding.assign.setVisibility(GONE);
                        if (itemData.getHistory().get(0).getCheckin() != 0 && itemData.getHistory().get(0).getStatus() == 0) {
                            binding.checkout.setVisibility(View.GONE);
                            String s_time1 = Conversions.millitotime((Long.parseLong(itemData.getHistory().get(0).getCheckin().toString())) * 1000, false);
                            binding.sTimeCheckin.setText(s_time1);
                        }


                        if (itemData.getHistory().get(0).getStart().equals("") && itemData.getHistory().get(0).getEnd().equals("")){
                            binding.checkinTime.setVisibility(VISIBLE);
                            //CheckIn date and time
                            long checkinTime = (Long.parseLong(itemData.getHistory().get(0).getDatetime().get$numberLong()) * 1000);
                            Log.e("check", itemData.getHistory().get(0).getStart() + "");
                            String s_time = Conversions.millitotime((itemData.getHistory().get(0).getStart() + Conversions.timezone()) * 1000, false);
                            String e_time = Conversions.millitotime((itemData.getHistory().get(0).getEnd() + 1 + Conversions.timezone()) * 1000, false);
                            binding.checkinTime.setText(ViewController.Date_month_year(checkinTime) + ", " + s_time + " to " + e_time);
                        }


                        //create date and time
                        long longtime = (Long.parseLong(String.valueOf(Long.parseLong(itemData.getCreated_time().get$numberLong()) * 1000)));
                        binding.checkinCreateTime.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime) + "");

                        //set data
                        binding.subjectCheckin.setText(Conversions.Capitalize(itemData.getName()));
                        binding.hostCheckin.setText(" " + itemData.getHistory().get(0).getPurpose());
                        String s_time1 = Conversions.millitotime((Long.parseLong(itemData.getHistory().get(0).getRequest().get$numberLong())) * 1000, false);
                        binding.sTimeCheckin.setText(s_time1);

                    }

                } catch (IndexOutOfBoundsException e) {
                    // Catching the exception and handling it
                    System.out.println("IndexOutOfBoundsException: " + e.getMessage());
                }


                if (itemData.getHistory() != null) {
                    if (itemData.getHistory().get(0).getLivepic() != null && itemData.getHistory().get(0).getLivepic().size() != 0) {
                        Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + itemData.getHistory().get(0).getLivepic().get(0))
                                .into(binding.picCheckin);
                    }
                }


                binding.accept.setOnClickListener(v -> {
                    AnimationSet animation = Conversions.animation();
                    v.startAnimation(animation);

                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "accept");
                        jsonObj_.put("hstatus", 1);
                        jsonObj_.put("id", itemData.get_id().get$oid());
                        jsonObj_.put("email", itemData.getEmail());
                        jsonObj_.put("name", itemData.getName());
                        jsonObj_.put("request", itemData.getHistory().get(0).getRequest().getCreated_time());
                        jsonObj_.put("checkin", itemData.getHistory().get(0).getCheckin());
                        jsonObj_.put("host", itemData.getHistory().get(0).getHost());
                        jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                        toDayMeetings.get(position).getHistory().get(0).setHstatus(Long.parseLong("1"));
//                        MeetingViewpager.setAdapter(meetingPager);
//                        meetingPager.notifyDataSetChanged();
                    actioncheckinout(gsonObject);
                });
                binding.decline.setOnClickListener(v -> {
                    AnimationSet animation = Conversions.animation();
                    v.startAnimation(animation);
                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "decline");
                        jsonObj_.put("hstatus", 2);
                        jsonObj_.put("id", itemData.get_id().get$oid());
                        jsonObj_.put("email", itemData.getEmail());
                        jsonObj_.put("name", itemData.getName());
                        jsonObj_.put("request", itemData.getHistory().get(0).getRequest());
                        jsonObj_.put("checkin", itemData.getHistory().get(0).getCheckin());
                        jsonObj_.put("host", itemData.getHistory().get(0).getHost());
                        jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                        toDayMeetings.get(position).getHistory().get(0).setHstatus(Long.parseLong("2"));
//                        MeetingViewpager.setAdapter(meetingPager);
//                        meetingPager.notifyDataSetChanged();
                    actioncheckinout(gsonObject);
                });
                binding.checkout.setOnClickListener(v -> {
                    AnimationSet animation = Conversions.animation();
                    v.startAnimation(animation);

                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("formtype", "checkout");
                        jsonObj_.put("status", 2);
                        jsonObj_.put("id", itemData.get_id().get$oid());
                        jsonObj_.put("checkin", itemData.getHistory().get(0).getCheckin());
                        jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                        toDayMeetings.remove(position);
//                        MeetingViewpager.setAdapter(meetingPager);
//                        meetingPager.notifyDataSetChanged();
                    actioncheckinout(gsonObject);
                });
                binding.assign.setOnClickListener(v -> {
                    AnimationSet animation = Conversions.animation();
                    v.startAnimation(animation);
                    Assignpopup(position, "");
                });
                binding.cardviewCheckin.setOnClickListener(v -> {
                    AnimationSet animation = Conversions.animation();
                    v.startAnimation(animation);
                    Intent intent = new Intent(getActivity(), CheckInDetailsActivity.class);
                    intent.putExtra("_id", itemData.getHistory().get(0).get_id().get$oid());
                    intent.putExtra("type", "today");
                    startActivityForResult(intent, REQUEST_CODE);
                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                });

                binding.executePendingBindings();

            }

        }

    }

    //Upcoming
    public class UpComingMeetingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<CompanyData> meetings1s;
        private Context context;

        public UpComingMeetingAdapter(Context context, ArrayList<CompanyData> meetings1s) {
            this.meetings1s = meetings1s;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcominng_meetings_list_items, parent, false);
            return new ViewHolderUpcoming(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ViewHolderUpcoming Holder = (ViewHolderUpcoming) holder;


            Log.e(TAG, "onBindViewHolder:getSupertype " + meetings1s.get(position).getName());


            if (meetings1s.get(position).getSupertype().equalsIgnoreCase("exchange")) {
                Log.e(TAG, "onBindViewHolder:super_123: " + meetings1s.get(position).getSupertype());
            }

            if (empData.getEmp_id().equalsIgnoreCase(meetings1s.get(position).getEmp_id())) {
            } else {
                for (int i = 0; i < meetings1s.get(position).getInvites().size(); i++) {
                    if (!meetings1s.get(position).getInvites().get(i).getView_status()) {
                        Holder.host.setTypeface(null, Typeface.BOLD);
                        Holder.subject.setTypeface(null, Typeface.BOLD);
                        Holder.viziter.setTypeface(null, Typeface.BOLD);
                    } else {
                    }
                }
            }

            if (meetings1s.get(position).getV_id() == null || meetings1s.get(position).getV_id().equalsIgnoreCase("")) {
                Holder.title_type.setText(R.string.meeting);
            } else {
                Holder.title_type.setText(R.string.appointment_p);
            }

            if (meetings1s.get(position).getRecurrence()) {
                Holder.img_reccurence.setVisibility(VISIBLE);
            }

            Holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MeetingDescriptionNewActivity.class);
                    intent.putExtra("m_id", meetings1s.get(position).get_id().get$oid());
                    startActivityForResult(intent, REQUEST_CODE);
                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });

            String s_time1 = Conversions.millitotime((meetings1s.get(position).getStart() + Conversions.timezone()) * 1000, false);
            String e_time = Conversions.millitotime((meetings1s.get(position).getEnd() + 1 + Conversions.timezone()) * 1000, false);
            Holder.s_time.setText(s_time1);

            DateFormat simple = new SimpleDateFormat("dd MMM yyyy");
            if (sharedPreferences1.getString("language", "").equals("ar")) {
                Locale locale = new Locale("ar");
                simple = new SimpleDateFormat("dd MMM yyyy", locale);
            }

            Date result = new Date((meetings1s.get(position).getStart() + Conversions.timezone()) * 1000);
            String time = simple.format(result) + "";
            Holder.date.setText(time);


            //meeting time
            Holder.meeting_time.setText(time + " " + s_time1 + " To " + e_time);

            if (meetings1s.get(position).getInvites().get(0).getName() != null) {
                Holder.viziter.setText(meetings1s.get(position).getInvites().get(0).getName() + "");
            } else {
                Holder.viziter.setText(meetings1s.get(position).getInvites().get(0).getEmail() + "");
            }

            //create date and time
            long longtime = (Long.parseLong(String.valueOf(Long.parseLong(meetings1s.get(position).getCreated_time().get$numberLong()) * 1000)));
            Holder.create_time.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime) + "");


            Holder.host_title.setText(getResources().getString(R.string.host));
            if (meetings1s.get(position).getEmployee().getPic() != null && meetings1s.get(position).getEmployee().getPic().size() != 0) {
                Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetings1s.get(position).getEmployee().getPic().get(meetings1s.get(position).getEmployee().getPic().size() - 1))
                        .into(Holder.pic);
            }
            Holder.subject.setText(Conversions.Capitalize(meetings1s.get(position).getSubject()));
            Holder.host.setText(" " + meetings1s.get(position).getEmployee().getName());
            if (empData.getEmp_id().equals(meetings1s.get(position).getEmp_id())) {
                Holder.host.setText(" Me");
                if (meetings1s.get(position).getInvites().get(0).getPic().size() != 0) {
                    System.out.println(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetings1s.get(position).getInvites().get(0).getPic().get(meetings1s.get(position).getInvites().get(0).getPic().size() - 1));
                    Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetings1s.get(position).getInvites().get(0).getPic().get(meetings1s.get(position).getInvites().get(0).getPic().size() - 1))
                            .into(Holder.pic);
                }
            } else {
                if (meetings1s.get(position).getEmployee().getPics().size() != 0) {
                    Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetings1s.get(position).getEmployee().getPics().get(meetings1s.get(position).getEmployee().getPics().size() - 1))
                            .into(Holder.pic);
                }
            }
            if (meetings1s.get(position).getInvites().size() > 1) {
                Holder.count.setVisibility(View.VISIBLE);
                Holder.count.setText("+" + (meetings1s.get(position).getInvites().size() - 1));
            }
            if (meetings1s.get(position).getStatus() == 1) {
                // Cancel
                Holder.mStatus.setVisibility(View.VISIBLE);
                Holder.mStatusColor.setVisibility(View.VISIBLE);
                Holder.mStatus.setText(R.string.Cancelled);
                GradientDrawable drawable = (GradientDrawable) Holder.mStatusColor.getBackground();
                drawable.setStroke(3, getResources().getColor(R.color.Cancel)); // set stroke width and stroke color
                Holder.mStatus.setBackgroundColor(getResources().getColor(R.color.Cancel));
            } else {
                for (int i = 0; i < meetings1s.get(position).getInvites().size(); i++) {

                    GradientDrawable drawable = (GradientDrawable) Holder.mStatusColor.getBackground();
                    if (empData.getEmail().equals(meetings1s.get(position).getInvites().get(i).getEmail())) {
                        // Pending
                        Holder.mStatus.setVisibility(View.VISIBLE);
                        Holder.mStatusColor.setVisibility(View.VISIBLE);
                        Holder.mStatus.setBackgroundColor(getResources().getColor(R.color.Pending));
                        drawable.setStroke(3, getResources().getColor(R.color.Pending)); // set stroke width and stroke color
                        Holder.mStatus.setText(R.string.pending);
                        if (meetings1s.get(position).getInvites().get(i).getStatus() == 3) {
                            //Accepted
                            drawable.setStroke(3, getResources().getColor(R.color.Accept)); // set stroke width and stroke color
                            Holder.mStatus.setBackgroundColor(getResources().getColor(R.color.Accept));
                            Holder.mStatus.setText(R.string.accepted);

                        } else if (meetings1s.get(position).getInvites().get(i).getStatus() == 1) {
                            //declined
                            drawable.setStroke(3, getResources().getColor(R.color.Decline)); // set stroke width and stroke color
                            Holder.mStatus.setBackgroundColor(getResources().getColor(R.color.Decline));
                            Holder.mStatus.setText(R.string.declined);
                        }
                        break;
                    } else {
                        Holder.mStatus.setVisibility(GONE);
                        Holder.mStatusColor.setVisibility(GONE);
                    }
                }
            }


        }

        @Override
        public int getItemCount() {
            //count
            upcoming_count_text.setText("(" + meetings1s.size() + ")");
            if (meetings1s.size() > 3) {
                //3 above
            } else {
                //3 below
            }
            return meetings1s.size();
        }

        public class ViewHolderUpcoming extends RecyclerView.ViewHolder {
            TextView title_type, subject, host, count, s_time, viziter, date, host_title, meeting_time, create_time, mStatus;
            CircleImageView pic;
            ImageView img_reccurence;
            RelativeLayout mStatusColor;

            public ViewHolderUpcoming(@NonNull View view) {
                super(view);
                date = view.findViewById(R.id.date);
                mStatusColor = view.findViewById(R.id.mStatusColor);
                meeting_time = view.findViewById(R.id.meeting_time);
                create_time = view.findViewById(R.id.create_time);
                mStatus = view.findViewById(R.id.mStatus);
                s_time = view.findViewById(R.id.s_time);
                viziter = view.findViewById(R.id.viziter);
                pic = view.findViewById(R.id.pic);
                img_reccurence = view.findViewById(R.id.img_reccurence);
                title_type = view.findViewById(R.id.title_type);
                subject = view.findViewById(R.id.subject);
                host = view.findViewById(R.id.host);
                count = view.findViewById(R.id.count);
                host_title = view.findViewById(R.id.host_title);
            }
        }
    }


    //Appointments
    public ArrayList<CompanyData> appoinment_insertionSort(ArrayList<CompanyData> meetingsA) {
        ArrayList<CompanyData> meetingsSort = new ArrayList<>();
        meetingsSort = meetingsA;
        int n = meetingsSort.size();
        int i, j;
        CompanyData key;
        for (i = 1; i < n; i++) {
            key = meetingsSort.get(i);
            j = i - 1;
            if (meetingsSort.get(j).getSort_stamp() != null && key.getSort_stamp() != null) {
                long v = Long.parseLong(meetingsSort.get(j).getSort_stamp());
                long key_v = Long.parseLong(key.getSort_stamp());
                while (j >= 0 && v < key_v) {
                    meetingsSort.set(j + 1, meetingsSort.get(j));
                    j = j - 1;
                }
                meetingsSort.set(j + 1, key);
            }

        }
        return meetingsSort;
    }

    private class AppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<CompanyData> appointment_ArrayList;
        ;
        private Context context;

        public AppointmentAdapter(Context context, ArrayList<CompanyData> appointment_ArrayList) {
            this.appointment_ArrayList = appointment_ArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointmrnt_list_items, parent, false);
            return new ViewHolder1(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ViewHolder1 Holder = (ViewHolder1) holder;

            Log.d("abc_", appointment_ArrayList.get(position).getSupertype());

            try {
                if (appointment_ArrayList.get(position).getSupertype().equalsIgnoreCase("meeting"))  {
                    Holder.cardview_meeting.setVisibility(View.VISIBLE);
                    Holder.cardview_appointment.setVisibility(View.GONE);
                    Holder.cardview_checkin.setVisibility(GONE);
                    Log.d("abc_getName", appointment_ArrayList.get(position).getEmployee().getName());

                    if (appointment_ArrayList.get(position).getApprover_statistics().size() != 0){
                        if (appointment_ArrayList.get(position).getApprover_statistics().get(0).getView_status() != null && appointment_ArrayList.get(position).getApprover_statistics().get(0).getView_status() == false) {
                            Holder.host_today.setTypeface(null, Typeface.BOLD);
                            Holder.viziter_today.setTypeface(null, Typeface.BOLD);
                        }
                    }

                    if (appointment_ArrayList.get(position).getRecurrence()) {
                        Holder.img_reccurence.setVisibility(VISIBLE);
                    }

                    Log.e(TAG, "onBindViewHolder:Mr_approversize " + appointment_ArrayList.get(position).getMr_approvers().size() + "");

                    if (appointment_ArrayList.get(position).getMr_approvers().size() != 0) {
                        Holder.title_type_m.setText(R.string.room_Approval);
                    }

                    Holder.host_today.setText(appointment_ArrayList.get(position).getEmployee().getName());

                    if (appointment_ArrayList.get(position).getV_id() == null || appointment_ArrayList.get(position).getV_id().equalsIgnoreCase("")) {
                        Holder.title_type.setText(R.string.appointment_p);
                    } else {
                        Holder.title_type.setText(R.string.appointment_p);
                    }

                    Holder.m_type_today.setBackgroundColor(getResources().getColor(R.color.white));
                    if (appointment_ArrayList.get(position).getEmployee().getPic() != null && appointment_ArrayList.get(position).getEmployee().getPic().size() != 0) {
                        Glide.with(context).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + appointment_ArrayList.get(position).getEmployee().getPic().get(appointment_ArrayList.get(position).getEmployee().getPic().size() - 1))
                                .into(Holder.pic);
                    }
                    String s_time = Conversions.millitotime((appointment_ArrayList.get(position).getStart() + Conversions.timezone()) * 1000, false);
                    String e_time = Conversions.millitotime((appointment_ArrayList.get(position).getEnd() + 1 + Conversions.timezone()) * 1000, false);
                    Holder.s_time.setVisibility(GONE);
                    DateFormat simple = new SimpleDateFormat("dd MMM");
                    Date result = new Date((appointment_ArrayList.get(position).getStart() + Conversions.timezone()) * 1000);
                    String time = simple.format(result) + "";
                    Holder.date_today.setText(s_time);

                    Long date_ = (appointment_ArrayList.get(position).getStart() + Conversions.timezone()) * 1000;
                    Holder.viziter_ap_time.setText(ViewController.Date_month_year(date_) + ", " + s_time + " To " + e_time);


                    if (appointment_ArrayList.get(position).getInvites().get(0).getName() != null) {
                        Holder.viziter_today.setText(appointment_ArrayList.get(position).getSubject());
                        //Holder.viziter_today.setText(appointment_ArrayList.get(position).getInvites().get(0).getName()+"");
                    } else {
                        Holder.viziter_today.setText(appointment_ArrayList.get(position).getInvites().get(0).getEmail() + "");
                    }

                    Holder.a_name.setText(Conversions.Capitalize(appointment_ArrayList.get(position).getSubject()));
                    Holder.host.setText(" " + appointment_ArrayList.get(position).getEmployee().getName());

                    DateFormat meet_simple = new SimpleDateFormat("dd MMM yyyy");
                    if (sharedPreferences1.getString("language", "").equals("ar")) {
                        Locale locale = new Locale("ar");
                        meet_simple = new SimpleDateFormat("dd MMM yyyy", locale);
                    }

                    //create date and time
                    long longtime = (Long.parseLong(String.valueOf(Long.parseLong(appointment_ArrayList.get(position).getCreated_time().get$numberLong()) * 1000)));
                    Holder.create_meeting_time.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime) + "");


                    if (empData.getEmp_id().equals(appointment_ArrayList.get(position).getEmp_id())) {
                        Holder.host.setText(" Me");
                        Holder.m_type_today.setBackgroundColor(getResources().getColor(R.color.white));
                        if (appointment_ArrayList.get(position).getInvites().get(0).getPic().size() != 0) {
                            Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + appointment_ArrayList.get(position).getInvites().get(0).getPic().get(appointment_ArrayList.get(position).getInvites().get(0).getPic().size() - 1))
                                    .into(Holder.pic);
                        }
                    } else {
                        if (appointment_ArrayList.get(position).getEmployee().getPics().size() != 0) {
                            Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + appointment_ArrayList.get(position).getEmployee().getPics().get(appointment_ArrayList.get(position).getEmployee().getPics().size() - 1))
                                    .into(Holder.pic);
                        }
                    }
                    if (appointment_ArrayList.get(position).getInvites().size() > 1) {
                        Holder.count_today.setVisibility(View.VISIBLE);
                        Holder.count_today.setText("+" + (appointment_ArrayList.get(position).getInvites().size() - 1));
                    }
                    if (appointment_ArrayList.get(position).getStatus() == 1) {
                        //Cancel
                        Holder.mStatus_today.setVisibility(View.VISIBLE);
                        Holder.mStatusColor_today.setVisibility(View.VISIBLE);
                        Holder.mStatus_today.setText(R.string.Cancelled);
                        GradientDrawable drawable = (GradientDrawable) Holder.mStatusColor_today.getBackground();
                        drawable.setStroke(3, getResources().getColor(R.color.Cancel)); // set stroke width and stroke color
                        Holder.mStatus_today.setBackgroundColor(getResources().getColor(R.color.Cancel));
                    } else {
                        for (int i = 0; i < appointment_ArrayList.get(position).getInvites().size(); i++) {
                            GradientDrawable drawable = (GradientDrawable) Holder.mStatusColor_today.getBackground();
                            if (empData.getEmail().equals(appointment_ArrayList.get(position).getInvites().get(i).getEmail())) {
                                //Pending
                                Holder.mStatus_today.setVisibility(View.VISIBLE);
                                Holder.mStatusColor_today.setVisibility(View.VISIBLE);
                                Holder.mStatus_today.setBackgroundColor(getResources().getColor(R.color.Pending));
                                drawable.setStroke(3, getResources().getColor(R.color.Pending)); // set stroke width and stroke color
                                Holder.mStatus_today.setText(R.string.pending);
                                if (appointment_ArrayList.get(position).getInvites().get(i).getStatus() == 3) {
                                    //Accepted
                                    drawable.setStroke(3, getResources().getColor(R.color.Accept)); // set stroke width and stroke color
                                    Holder.mStatus_today.setBackgroundColor(getResources().getColor(R.color.Accept));
                                    Holder.mStatus_today.setText(R.string.accepted);
                                } else if (appointment_ArrayList.get(position).getInvites().get(i).getStatus() == 1) {
                                    //declined
                                    drawable.setStroke(3, getResources().getColor(R.color.Decline)); // set stroke width and stroke color
                                    Holder.mStatus_today.setBackgroundColor(getResources().getColor(R.color.Decline));
                                    Holder.mStatus_today.setText(R.string.declined);
                                }
                                break;
                            } else {
                                Holder.mStatus_today.setVisibility(GONE);
                                Holder.mStatusColor_today.setVisibility(GONE);
                            }
                        }
                    }

                    Holder.cardview_meeting.setOnClickListener(v -> {
                        AnimationSet animation = Conversions.animation();
                        v.startAnimation(animation);
                        Intent intent = new Intent(context, MeetingDescriptionNewActivity.class);
//                        intent.putExtra("m_id", appointment_ArrayList.get(position).getMid());
                        intent.putExtra("m_id", appointment_ArrayList.get(position).get_id().get$oid());
                        startActivityForResult(intent, REQUEST_CODE);
                        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                        Log.e("m_id_a",appointment_ArrayList.get(position).get_id().get$oid());
                    });

                    if (empData.getRoleid().equals(appointment_ArrayList.get(position).getApprover_roleid())) {
                        Holder.m_type_today.setBackgroundColor(getResources().getColor(R.color.white));
                    }

                } else if (appointment_ArrayList.get(position).getSupertype().equalsIgnoreCase("outside")) {
                    Holder.cardview_appointment.setVisibility(View.VISIBLE);
                    Holder.cardview_meeting.setVisibility(GONE);
                    Holder.cardview_checkin.setVisibility(GONE);


                    Holder.a_name.setText(appointment_ArrayList.get(position).getvData().getName());
                    Holder.host.setText(appointment_ArrayList.get(position).getPurpose() + "");
                    Holder.a_company.setText(appointment_ArrayList.get(position).getvData().getCompany() + "");

                    String s_time1 = Conversions.millitotime((Long.parseLong(appointment_ArrayList.get(position).getA_time().get$numberLong())) * 1000, false);
                    Holder.s_time.setText(s_time1);


                    DateFormat appoint_simple = new SimpleDateFormat("dd MMM yyyy");
                    if (sharedPreferences1.getString("language", "").equals("ar")) {
                        Locale locale = new Locale("ar");
                        appoint_simple = new SimpleDateFormat("dd MMM yyyy", locale);
                    }


                    //time date
                    long tlongtime = (Long.parseLong(String.valueOf(Long.parseLong(appointment_ArrayList.get(position).getA_time().get$numberLong()) * 1000)));
                    Date td = new Date(tlongtime);
                    String tdate = DateFormat.getDateTimeInstance().format(td);
                    Holder.appointment_time.setText(tdate + "");


                    //create date and time
                    long longtime = (Long.parseLong(String.valueOf(Long.parseLong(appointment_ArrayList.get(position).getA_time().get$numberLong()) * 1000)));
                    Holder.create_appointment_time.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime) + "");


                    Holder.cardview_appointment.setOnClickListener(v -> {
                        AnimationSet animation = Conversions.animation();
                        v.startAnimation(animation);
                        Intent intent = new Intent(getActivity(), AppointmentDetailsNewActivity.class);
                        intent.putExtra("m_id", appointment_ArrayList.get(position).get_id().get$oid());
                        startActivityForResult(intent, REQUEST_CODE);
                        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                    });

                    //visible
                    Holder.assign.setVisibility(GONE);
                    Holder.decline.setVisibility(View.GONE);
                    Holder.accept.setVisibility(View.GONE);

                    Holder.assign.setOnClickListener(v -> Assignpopup(position, "outside"));

                    Holder.decline.setOnClickListener(v -> {
                        String id = appointment_ArrayList.get(position).get_id().get$oid();
                        String Emp_id = appointment_ArrayList.get(position).getEmp_id();
                        declineAppointmentpopup(id, Emp_id);
                    });

                    Holder.accept.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), SetupMeetingActivity.class);
                        intent.putExtra("RMS_STATUS", 3);
                        intent.putExtra("m_id", appointment_ArrayList.get(position).get_id().get$oid());
                        startActivity(intent);
                    });
                } else if (appointment_ArrayList.get(position).getSupertype().equalsIgnoreCase("visitor")) {

                    //checkin
                    Log.e(TAG, "onBindViewHolder:history: " + appointment_ArrayList.get(position).getHistory().get(0).getHstatus());
                    if (appointment_ArrayList.get(position).getHistory().get(0).getHstatus() == 0 || appointment_ArrayList.get(position).getHistory().get(0).getHstatus() == 2) {
                        Holder.cardview_checkin.setVisibility(VISIBLE);
                        Holder.cardview_meeting.setVisibility(View.GONE);
                        Holder.cardview_appointment.setVisibility(View.GONE);


                        System.out.println("testing 1");
                        Holder.checkout_checkin.setVisibility(GONE);
                        Holder.decline_checkin.setVisibility(View.GONE);
                        Holder.accept_checkin.setVisibility(View.GONE);
                        Holder.assign_checkin.setVisibility(GONE);
                        if (appointment_ArrayList.get(position).getHistory().get(0).getCheckin() != 0 && appointment_ArrayList.get(position).getHistory().get(0).getStatus() == 0) {
                            Holder.checkout_checkin.setVisibility(View.GONE);
                            String s_time1 = Conversions.millitotime((Long.parseLong(appointment_ArrayList.get(position).getHistory().get(0).getCheckin().toString())) * 1000, false);
                            Holder.s_time_checkin.setText(s_time1);
                        }
                        //set data
                        Holder.subject_checkin.setText(Conversions.Capitalize(appointment_ArrayList.get(position).getName()));
                        Holder.host_checkin.setText(" " + appointment_ArrayList.get(position).getHistory().get(0).getPurpose());
                        String s_time1 = Conversions.millitotime((Long.parseLong(appointment_ArrayList.get(position).getHistory().get(0).getRequest().get$numberLong())) * 1000, false);
                        Holder.s_time_checkin.setText(s_time1);
                    } else {
                        Holder.cardview_checkin.setVisibility(GONE);
                    }

                    if (appointment_ArrayList.get(position).getHistory().get(0).getLivepic() != null && appointment_ArrayList.get(position).getHistory().get(0).getLivepic().size() != 0) {
                        Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + appointment_ArrayList.get(position).getHistory().get(0).getLivepic().get(0))
                                .into(Holder.pic_checkin);
                    }

                    //CheckIn date and time
                    long checkinTime = (Long.parseLong(appointment_ArrayList.get(position).getHistory().get(0).getDatetime().get$numberLong()) * 1000);
                    Holder.checkin_time.setText(ViewController.Create_date_month_year_hr_mm_am_pm(checkinTime) + "");


                    //create date and time
                    long longtime = (Long.parseLong(String.valueOf(Long.parseLong(appointment_ArrayList.get(position).getCreated_time().get$numberLong()) * 1000)));
                    Holder.create_time.setText(ViewController.Create_date_month_year_hr_mm_am_pm(longtime) + "");

                    Holder.accept_checkin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AnimationSet animation = Conversions.animation();
                            v.startAnimation(animation);
                            JsonObject gsonObject = new JsonObject();
                            JSONObject jsonObj_ = new JSONObject();
                            try {
                                jsonObj_.put("formtype", "accept");
                                jsonObj_.put("hstatus", 1);
                                jsonObj_.put("id", appointment_ArrayList.get(position).get_id().get$oid());
                                jsonObj_.put("email", appointment_ArrayList.get(position).getEmail());
                                jsonObj_.put("name", appointment_ArrayList.get(position).getName());
                                jsonObj_.put("request", appointment_ArrayList.get(position).getHistory().get(0).getRequest().getCreated_time());
                                jsonObj_.put("checkin", appointment_ArrayList.get(position).getHistory().get(0).getCheckin());
                                jsonObj_.put("host", appointment_ArrayList.get(position).getHistory().get(0).getHost());
                                jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                                JsonParser jsonParser = new JsonParser();
                                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            actioncheckinout(gsonObject);
                        }
                    });
                    Holder.decline_checkin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AnimationSet animation = Conversions.animation();
                            v.startAnimation(animation);
                            JsonObject gsonObject = new JsonObject();
                            JSONObject jsonObj_ = new JSONObject();
                            try {
                                jsonObj_.put("formtype", "decline");
                                jsonObj_.put("hstatus", 2);
                                jsonObj_.put("id", appointment_ArrayList.get(position).get_id().get$oid());
                                jsonObj_.put("email", appointment_ArrayList.get(position).getEmail());
                                jsonObj_.put("name", appointment_ArrayList.get(position).getName());
                                jsonObj_.put("request", appointment_ArrayList.get(position).getHistory().get(0).getRequest());
                                jsonObj_.put("checkin", appointment_ArrayList.get(position).getHistory().get(0).getCheckin());
                                jsonObj_.put("host", appointment_ArrayList.get(position).getHistory().get(0).getHost());
                                jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                                JsonParser jsonParser = new JsonParser();
                                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //popup
                            decline_popup(gsonObject);
                        }
                    });
                    Holder.checkout_checkin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AnimationSet animation = Conversions.animation();
                            v.startAnimation(animation);

                            JsonObject gsonObject = new JsonObject();
                            JSONObject jsonObj_ = new JSONObject();
                            try {
                                jsonObj_.put("formtype", "checkout");
                                jsonObj_.put("status", 2);
                                jsonObj_.put("id", appointment_ArrayList.get(position).get_id().get$oid());
                                jsonObj_.put("checkin", appointment_ArrayList.get(position).getHistory().get(0).getCheckin());
                                jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                                JsonParser jsonParser = new JsonParser();
                                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            actioncheckinout(gsonObject);
                        }
                    });

                    Holder.cardview_checkin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AnimationSet animation = Conversions.animation();
                            v.startAnimation(animation);
                            Intent intent = new Intent(getActivity(), CheckInDetailsActivity.class);
                            intent.putExtra("_id", appointment_ArrayList.get(position).getHistory().get(0).get_id().get$oid());
                            intent.putExtra("type", "appointment");
                            startActivityForResult(intent, REQUEST_CODE);
                            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

                        }
                    });

//                Holder.cardview_checkin.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final Dialog dialog = new Dialog(getActivity());
//                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setCancelable(false);
//                        dialog.setContentView(R.layout.checkin_visitor_popup);
//                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                        final TextView submit;
//                        final TextView v_name, purpose, email, mobile, department, company, tomeet, badge, time;
//                        final TextView cancel;
//                        final CircleImageView pic;
//                        final LinearLayout linear_bottom_Selection;
//                        LinearLayout linear_accept;
//                        LinearLayout linear_decline;
//                        cancel = dialog.findViewById(R.id.cancel);
////                      tomeet = dialog.findViewById(R.id.tomeet);
//                        pic = dialog.findViewById(R.id.pic);
////                      department = dialog.findViewById(R.id.department);
//                        purpose = dialog.findViewById(R.id.purpose);
//                        company = dialog.findViewById(R.id.company);
//                        email = dialog.findViewById(R.id.email);
//                        mobile = dialog.findViewById(R.id.mobile);
//                        v_name = dialog.findViewById(R.id.name);
//                        badge = dialog.findViewById(R.id.badge);
//                        time = dialog.findViewById(R.id.c_time);
//                        linear_accept = dialog.findViewById(R.id.linear_accept);
//                        linear_decline = dialog.findViewById(R.id.linear_decline);
//                        linear_bottom_Selection = dialog.findViewById(R.id.linear_bottom_Selection);
//                        purpose.setText(appointment_ArrayList.get(position).getHistory().get(0).getPurpose());
//                        badge.setText(appointment_ArrayList.get(position).getHistory().get(0).getBadge());
//                        v_name.setText(appointment_ArrayList.get(position).getName());
//                        company.setText(appointment_ArrayList.get(position).getCompany());
//                        mobile.setText(appointment_ArrayList.get(position).getMobile());
//                        email.setText(appointment_ArrayList.get(position).getEmail());
////                      tomeet.setText(meeting.get(position).getEmployee().getName());
////                      department.setText(meeting.get(position).getHierarchys().getName());
//                        if (appointment_ArrayList.get(position).getHistory().get(0).getLivepic() != null && appointment_ArrayList.get(position).getHistory().get(0).getLivepic().size() != 0) {
//                            Glide.with(getActivity()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + appointment_ArrayList.get(position).getHistory().get(0).getLivepic().get(0))
//                                    .into(pic);
//                        }
//                        String s_time1 = Conversions.millitotime((Long.parseLong(appointment_ArrayList.get(position).getHistory().get(0).getRequest().get$numberLong())) * 1000, false);
//                        time.setText(s_time1);
//                        System.out.println("subfshfb" + s_time1);
//                        if (appointment_ArrayList.get(position).getHistory().get(0).getHstatus() != 0 && appointment_ArrayList.get(position).getHistory().get(0).getCheckin() != 0 && appointment_ArrayList.get(position).getHistory().get(0).getStatus() == 0) {
//                            Holder.checkout_checkin.setVisibility(View.VISIBLE);
//                            s_time1 = Conversions.millitotime((Long.parseLong(appointment_ArrayList.get(position).getHistory().get(0).getCheckin().toString())) * 1000, false);
//                            time.setText(s_time1);
//                            System.out.println("subfshfb" + s_time1);
//
//                        }
//                        linear_bottom_Selection.setVisibility(View.VISIBLE);
//                        linear_accept.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                                AnimationSet animation = Conversions.animation();
//                                v.startAnimation(animation);
//                                JsonObject gsonObject = new JsonObject();
//                                JSONObject jsonObj_ = new JSONObject();
//                                try {
//                                    jsonObj_.put("formtype", "accept");
//                                    jsonObj_.put("hstatus", 1);
//                                    jsonObj_.put("id", appointment_ArrayList.get(position).get_id().get$oid());
//                                    jsonObj_.put("email", appointment_ArrayList.get(position).getEmail());
//                                    jsonObj_.put("name", appointment_ArrayList.get(position).getName());
//                                    jsonObj_.put("request", appointment_ArrayList.get(position).getHistory().get(0).getRequest().getCreated_time());
//                                    jsonObj_.put("checkin", appointment_ArrayList.get(position).getHistory().get(0).getCheckin());
//                                    jsonObj_.put("host", appointment_ArrayList.get(position).getHistory().get(0).getHost());
//                                    jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
//                                    JsonParser jsonParser = new JsonParser();
//                                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                actioncheckinout(gsonObject);
//                            }
//                        });
//                        linear_decline.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                                AnimationSet animation = Conversions.animation();
//                                v.startAnimation(animation);
//                                JsonObject gsonObject = new JsonObject();
//                                JSONObject jsonObj_ = new JSONObject();
//                                try {
//                                    jsonObj_.put("formtype", "decline");
//                                    jsonObj_.put("hstatus", 2);
//                                    jsonObj_.put("id", appointment_ArrayList.get(position).get_id().get$oid());
//                                    jsonObj_.put("email", appointment_ArrayList.get(position).getEmail());
//                                    jsonObj_.put("name", appointment_ArrayList.get(position).getName());
//                                    jsonObj_.put("request", appointment_ArrayList.get(position).getHistory().get(0).getRequest());
//                                    jsonObj_.put("checkin", appointment_ArrayList.get(position).getHistory().get(0).getCheckin());
//                                    jsonObj_.put("host", appointment_ArrayList.get(position).getHistory().get(0).getHost());
//                                    jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
//                                    JsonParser jsonParser = new JsonParser();
//                                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                actioncheckinout(gsonObject);
//                            }
//                        });
//                        cancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                        dialog.show();
////
//
//                    }
//                });

                    Holder.assign_checkin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AnimationSet animation = Conversions.animation();
                            v.startAnimation(animation);
                            Assignpopup(position, "");
                        }
                    });

                } else {

                }
            } catch (IndexOutOfBoundsException e) {
                // Catching the exception and handling it
                System.out.println("IndexOutOfBoundsException: " + e.getMessage());
            }

        }

        @Override
        public int getItemCount() {
            //count
            appointment_count_text.setText("(" + appointment_ArrayList.size() + ")");
            if (appointment_ArrayList.size() > 3) {
                //3 above
                ViewGroup.LayoutParams params = recyclerview_appointment.getLayoutParams();
                params.height = 600;
                recyclerview_appointment.setLayoutParams(params);
            } else {
                //3 below
            }
            return appointment_ArrayList.size();
        }

        public class ViewHolder1 extends RecyclerView.ViewHolder {
            //_today
            CardView cardview_meeting;
            TextView title_type_m, viziter_today, host_today, s_time_today, count_today, date_today, mStatus_today, viziter_ap_time, create_meeting_time;
            ImageView img_reccurence;
            CircleImageView pic_today;
            RelativeLayout m_type_today, mStatusColor_today;

            //appointment
            CardView cardview_appointment;
            TextView title_type;
            CircleImageView pic;
            LinearLayout line_appointment;
            TextView s_time;
            TextView appointment_time;
            TextView create_appointment_time;
            TextView a_name;
            TextView host;
            TextView a_company;
            ImageView assign;
            ImageView decline;
            ImageView accept;


            //checkin
            CardView cardview_checkin;
            CircleImageView pic_checkin;
            TextView subject_checkin, product_checkin, host_title_checkin, host_checkin, s_time_checkin, checkin_time, create_time;
            ImageView assign_checkin, checkout_checkin, decline_checkin, accept_checkin;
            RelativeLayout m_type_checkin;

            public ViewHolder1(@NonNull View itemView) {
                super(itemView);
                //today
                cardview_meeting = itemView.findViewById(R.id.cardview_meeting);
                title_type_m = itemView.findViewById(R.id.title_type_m);
                viziter_today = itemView.findViewById(R.id.viziter_today);
                host_today = itemView.findViewById(R.id.host_today);
                s_time_today = itemView.findViewById(R.id.s_time_today);
                count_today = itemView.findViewById(R.id.count_today);
                date_today = itemView.findViewById(R.id.date_today);
                mStatus_today = itemView.findViewById(R.id.mStatus_today);
                pic_today = itemView.findViewById(R.id.pic_today);
                m_type_today = itemView.findViewById(R.id.m_type_today);
                mStatusColor_today = itemView.findViewById(R.id.mStatusColor_today);
                viziter_ap_time = itemView.findViewById(R.id.viziter_ap_time);
                create_meeting_time = itemView.findViewById(R.id.create_meeting_time);
                img_reccurence = itemView.findViewById(R.id.img_reccurence);


                //appointment
                cardview_appointment = itemView.findViewById(R.id.cardview_appointment);
                title_type = itemView.findViewById(R.id.title_type);
                pic = itemView.findViewById(R.id.pic);
                line_appointment = itemView.findViewById(R.id.line_appointment);
                s_time = itemView.findViewById(R.id.s_time);
                appointment_time = itemView.findViewById(R.id.appointment_time);
                create_appointment_time = itemView.findViewById(R.id.create_appointment_time);
                a_name = itemView.findViewById(R.id.a_name);
                host = itemView.findViewById(R.id.host);
                a_company = itemView.findViewById(R.id.a_company);
                assign = itemView.findViewById(R.id.assign);
                decline = itemView.findViewById(R.id.decline);
                accept = itemView.findViewById(R.id.accept);


                //checkIn
                cardview_checkin = itemView.findViewById(R.id.cardview_checkin);
                pic_checkin = itemView.findViewById(R.id.pic_checkin);
                subject_checkin = itemView.findViewById(R.id.subject_checkin);
                product_checkin = itemView.findViewById(R.id.product_checkin);
                host_title_checkin = itemView.findViewById(R.id.host_title_checkin);
                host_checkin = itemView.findViewById(R.id.host_checkin);
                s_time_checkin = itemView.findViewById(R.id.s_time_checkin);
                checkin_time = itemView.findViewById(R.id.checkin_time);
                create_time = itemView.findViewById(R.id.create_time);
                m_type_checkin = itemView.findViewById(R.id.m_type_checkin);
                assign_checkin = itemView.findViewById(R.id.assign_checkin);
                checkout_checkin = itemView.findViewById(R.id.checkout_checkin);
                decline_checkin = itemView.findViewById(R.id.decline_checkin);
                accept_checkin = itemView.findViewById(R.id.accept_checkin);

            }
        }

    }

    private void decline_popup(JsonObject gsonObject_j) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getActivity());
        View mview = LayoutInflater.from(getActivity()).inflate(R.layout.appointment_decline_popup, null);
        TextView submit = mview.findViewById(R.id.submit);
        TextView cancel = mview.findViewById(R.id.cancel);

        mbuilder.setView(mview);
        ap_declineDialog = mbuilder.create();
        ap_declineDialog.show();
        ap_declineDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ap_declineDialog.setCancelable(false);
        ap_declineDialog.setCanceledOnTouchOutside(false);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actioncheckinout(gsonObject_j);
                ap_declineDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ap_declineDialog.dismiss();
            }
        });
    }

    //Assign
    private void Assignpopup(Integer Position, String super_type) {
        dialog = new Dialog(getActivity());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.assign_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView assign_btn, cancel;

        department_spinner = dialog.findViewById(R.id.department_spinner);
        emp_spinner = dialog.findViewById(R.id.emp_spinner);
        cancel = dialog.findViewById(R.id.cancel);
        emp_spinner.setInputType(InputType.TYPE_NULL);
        assign_btn = dialog.findViewById(R.id.assign);

        departmentAdapter = new CustomAdapter(getActivity(), R.layout.row, R.id.lbl_name, departments, 0, "");
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
                hierarchy_id = "";
                hierarchy_indexid = "";
                host = "";
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
                dialog.dismiss();
            }
        });
        assign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!host.equals("")) {
                    if (super_type.equalsIgnoreCase("outside")) {
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("formtype", "assign");
                            jsonObj_.put("id", toDayMeetings_Appointments.get(Position).get_id().get$oid());
                            jsonObj_.put("emp_id", host);
                            jsonObj_.put("coordinator", empData.getEmp_id());
                            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updateappointment(gsonObject);
                    } else {
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("formtype", "assign");
                            jsonObj_.put("id", toDayMeetings.get(Position).get_id().get$oid());
                            jsonObj_.put("request", toDayMeetings.get(Position).getHistory().get(0).getRequest().getCreated_time());
                            jsonObj_.put("host", host);
                            jsonObj_.put("hierarchy_indexid", hierarchy_indexid);
                            jsonObj_.put("hierarchy_id", hierarchy_id);
                            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        actioncheckinout(gsonObject);
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateappointment(JsonObject jsonObject) {
        card_view_progress.setVisibility(VISIBLE);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.updateappointment(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                card_view_progress.setVisibility(GONE);
                Model model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        getoappointments(toDay + "", (end.getTimeInMillis() / 1000) - Conversions.timezone() + "");
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                System.out.println("asdf2" + t);
                card_view_progress.setVisibility(GONE);
            }
        }, getActivity(), jsonObject);
    }

    private void actioncheckinout(JsonObject jsonObject) {
        card_view_progress.setVisibility(VISIBLE);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.actioncheckinout(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                card_view_progress.setVisibility(GONE);
                final Model model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        System.out.println("asfasf" + model.result);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        getoappointments(toDay + "", (end.getTimeInMillis() / 1000) - Conversions.timezone() + "");
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                System.out.println(t + "subhash");
                card_view_progress.setVisibility(GONE);
            }
        }, getActivity(), jsonObject);
    }


    //decline
    private void declineAppointmentpopup(String id, String Emp_id) {
        final Dialog dialog = new Dialog(getActivity());
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
                if (reason.getText().toString().equalsIgnoreCase("")) {
                    reason.setError("Please Enter Your Reason");
                } else {
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
                    declineappointment(gsonObject);
                    dialog.dismiss();
                }
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

    private void declineappointment(JsonObject jsonObject) {
        card_view_progress.setVisibility(VISIBLE);
        DataManger dataManager = DataManger.getDataManager();
        dataManager.updateappointment(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                card_view_progress.setVisibility(GONE);
                Model model = response.body();
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        getoappointments(toDay + "", (end.getTimeInMillis() / 1000) - Conversions.timezone() + "");
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                System.out.println("asdf2" + t);
                card_view_progress.setVisibility(GONE);
            }
        }, getActivity(), jsonObject);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {

//            //notification page to back activity refresh
//            Intent intent = new Intent(getActivity(), NavigationActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            getContext().startActivity(intent);

        }
    }


}