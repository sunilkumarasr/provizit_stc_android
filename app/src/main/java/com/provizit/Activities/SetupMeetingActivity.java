package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.datatransport.cct.internal.LogEvent;
import com.google.gson.Gson;
import com.provizit.AdapterAndModel.ContactsList;
import com.provizit.AdapterAndModel.HostSlots.HostSlotsData;
import com.provizit.AdapterAndModel.MultipleEmailAddressAdapter;
import com.provizit.AdapterAndModel.MultiplePhoneNumberAdapter;
import com.provizit.Config.Customthree;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.DurationAdapter;
import com.provizit.FragmentDailouge.AllotParking.AllotParkingFragment;
import com.provizit.FragmentDailouge.ReccurenceFragment;
import com.provizit.FragmentDailouge.ReccuringNewFragment;
import com.provizit.FragmentDailouge.SetUpMeetingSelectedMailsMobileListFragment;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.Logins.ForgotActivity;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Services.AppointmentDetails.AppointmentDetailsModel;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model1;
import com.provizit.Utilities.Agenda;
import com.provizit.Utilities.CommonObject;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CustomAdapter;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.DepartmentAdapter;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.FileUtilsClass;
import com.provizit.Utilities.GlideChip;
import com.provizit.Utilities.Invited;
import com.provizit.Utilities.LocationData;
import com.provizit.Utilities.Pdfs;
import com.provizit.Utilities.People;
import com.provizit.Utilities.PeopleAdapter;
import com.provizit.Utilities.RangeTimePickerDialog;
import com.provizit.Utilities.RoleDetails;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.Utilities.daysview.DayModel;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.provizit.FilePath.isDownloadsDocument;
import static com.provizit.FilePath.isExternalStorageDocument;
import static com.provizit.FragmentDailouge.SetUpMeetingContactListFragment.isPhoneNumberValid;

public class SetupMeetingActivity extends AppCompatActivity {
    private static final String TAG = "SetupMeetingActivity";

    ActionBar actionBar;

    CardView card_view_progress;
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;

    LinearLayout linearSelfAssist;

    private static Locale locale;
    EditText subject, meeting_description, m_value;
    TextView date, meeting_st, meeting_et;
    Button setup_meeting;
    Integer count = 1;
    int status = 0;
    CheckBox pSlotCheckBox;
    String location, m_room, e_gate;
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    int mIndex;
    int date_status = 0, d_hour = 0, d_min = 0, s_hour, s_min;
    List<People> mlist2, mlist4;
    PeopleAdapter  adapter1;
    int update, M_SUGGETION;
    int actiontype;
    DurationAdapter alertad;
    ArrayList<CompanyData> meetingrooms, entrypoints, invitesData, invitesData1;
    CustomAdapter customAdapter1, customAdapter2, invitesadapter;
    AutoCompleteTextView meetingRoomSpinner, meeting_location, entrypoint, alert;
    public static long s_time, e_time, s_date;

    //self invitastion
    LinearLayout linearDepartment;
    CheckBox SelfMeetingSetupCheckBox;
    AutoCompleteTextView department_spinner,emp_spinner;
    String AssignID = "";
    ArrayList<CompanyData> employees;
    ArrayList<CompanyData> departments;
    DepartmentAdapter departmentAdapter;
    CustomAdapter employeeAdapter;

    private AlertDialog alertDialog_category;

    //reccurence
    ImageView img_reccurence;
    LinearLayout linear_recorring;
    TextView txtOccurs,txtOccurence;
    ImageView imgDelete;

    long RMS_Date, RMS_Stime, RMS_Etime;
    int RMS_STATUS, RMS_Mroom, MroomIndex;
    //appointments
    String m_id;
    AppointmentDetailsModel appointments_model;

    ArrayList<CommonObject> amenities;
    String current_date;
    String selected_date;
    String meeting;
    public static AutoCompleteTextView name;
    public static ImageButton img_gmails_add;
    public static LinearLayout relative_emails_list_open_eye;
    public static TextView txt_list_count;
    TextView m_room_info;
    LinearLayout m_roomLayout, LELayout, mtype_layout;
    public static ArrayList<Invited> invitedArrayList;
    public static ArrayList<Invited> AddGuestinvitedArrayList;

    CompanyData meeting_details;
    DatabaseHelper myDb;
    AlertDialog.Builder builder;
    public static EmpData empData;
    RoleDetails roledetails;
    public static SharedPreferences sharedPreferences1;
    ArrayList<LocationData> meetinglocations;
    Boolean GUEST_STATUS = true, pSlots = false;
    String emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";

    ChipGroup chipgroup;
    public static ArrayList<String> iEmail;
    int samecabin;
    SharedPreferences.Editor editor1;
    ArrayList<Agenda> agendaArrayList;
    //reshedule agenda
    ArrayList<Agenda>  addagendaList = new ArrayList<>();
    RecyclerView recyclerView;
    Adapter1 agendaAdapter;
    String SelectedDate, SelectedSTime, SelectedETime;
    int SelectedHour, SelectedMin;
    ArrayList<String> aString;
    RadioGroup radioGroup;
    TextView m_typeDrawable;
    RadioButton radioButton1, radioButton2, radioButton3;
    String m_type;
    ArrayList<Pdfs> pdfsArrayList;
    LinearLayout pdfUploadLayout;
    TextView pdfName;
    ImageView pdfClear;

    //mobile mobile list
    TextView img_phone_list;

    //category
    Spinner category_spinner;
    ImageView img_category_info;
    String category_item = "";

    String v_id = "";

    private AlertDialog alertDialog;

    //recurence
    String recurrence_status = "";
    String recurrence_type = "";
    String dtstart = "";
    String dtend = "";

    List<Long> dates_s;
    ArrayList<DayModel> weekday_s;
    int number_of_occurrences_count;
    String From_date = "";
    String To_date = "";

    ApiViewModel apiViewModel;

    //contact_list
    List<ContactsList> contactsLists;
    ContactsList contactsLi;
    //add new contact in edittext for contact list
    String add_contact_list_status = "";

    //contact list selection
    private static final int PICK_CONTACT_REQUEST = 1;
    private String selectedPhoneNumber = "";
    private String selectedEmailAddress = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setup_meeting);
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(SetupMeetingActivity.this, R.color.colorPrimary));
        card_view_progress = findViewById(R.id.card_view_progress);
        //card_view_progress.setVisibility(View.VISIBLE);
        ViewController.ShowProgressBar(SetupMeetingActivity.this);

        //contacts list permission
        if (ContextCompat.checkSelfPermission(SetupMeetingActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SetupMeetingActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 200);
        }

        apiViewModel = new ViewModelProvider(SetupMeetingActivity.this).get(ApiViewModel.class);

        //internet connection
        relative_internet = findViewById(R.id.relative_internet);
        relative_ui = findViewById(R.id.relative_ui);
        broadcastReceiver = new ConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)) {
                    relative_internet.setVisibility(GONE);
                    relative_ui.setVisibility(View.VISIBLE);
                } else {
                    relative_internet.setVisibility(View.VISIBLE);
                    relative_ui.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();

        myDb = new DatabaseHelper(SetupMeetingActivity.this);
        empData = new EmpData();
        meeting_details = new CompanyData();
        empData = myDb.getEmpdata();
        location = empData.getLocation() + "";
        amenities = new ArrayList<>();
        amenities = myDb.getCompanyAmenities();
        roledetails = new RoleDetails();
        roledetails = myDb.getRole();
        meetingrooms = new ArrayList<>();
        invitesData = new ArrayList<>();
        iEmail = new ArrayList<>();
        iEmail.add(empData.getEmail());
        meetinglocations = new ArrayList<>();
        pdfsArrayList = new ArrayList<>();
        entrypoints = new ArrayList<>();
        meetinglocations = myDb.getCompanyAddress();

        builder = new AlertDialog.Builder(SetupMeetingActivity.this);
        builder.setMessage("Do You Want to Delete").setTitle("Remove");
        date = findViewById(R.id.date);
        m_type = "1";
        linearSelfAssist = findViewById(R.id.linearSelfAssist);
        setup_meeting = findViewById(R.id.setup_meeting);
        m_value = findViewById(R.id.m_value);
        m_typeDrawable = findViewById(R.id.m_typeDrawable);
        mtype_layout = findViewById(R.id.mtype_layout);
        pdfUploadLayout = findViewById(R.id.pdfUpload);
        pdfName = findViewById(R.id.pdfName);
        pdfClear = findViewById(R.id.pdfClear);
        m_roomLayout = findViewById(R.id.mroom_layout);
        LELayout = findViewById(R.id.location_entryponintLayout);
        pSlotCheckBox = findViewById(R.id.pSlotCheckBox);
        name = findViewById(R.id.name1);
        img_gmails_add = findViewById(R.id.img_gmails_add);
        relative_emails_list_open_eye = findViewById(R.id.relative_emails_list_open_eye);
        txt_list_count = findViewById(R.id.txt_list_count);
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", 0);

        chipgroup = (ChipGroup) findViewById(R.id.chipgroup);
        linearDepartment = findViewById(R.id.linearDepartment);
        SelfMeetingSetupCheckBox = findViewById(R.id.SelfMeetingSetupCheckBox);
        department_spinner = findViewById(R.id.department_spinner);
        emp_spinner = findViewById(R.id.emp_spinner);
        emp_spinner.setInputType(InputType.TYPE_NULL);
        m_room_info = findViewById(R.id.m_room_info);
        radioGroup = findViewById(R.id.radioGroup);
        m_typeDrawable = findViewById(R.id.m_typeDrawable);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        meeting_et = findViewById(R.id.meeting_duration);
        meeting_st = findViewById(R.id.meeting_st);
        meeting_description = findViewById(R.id.meeting_description);
        //spinner
        category_spinner = findViewById(R.id.category_spinner);
        img_category_info = findViewById(R.id.img_category_info);
        getcategories();
        img_category_info.setOnClickListener(v -> {
            AnimationSet animationCategoryInfo = Conversions.animation();
            v.startAnimation(animationCategoryInfo);
            info_categories();
        });


        //reccurence
        img_reccurence = findViewById(R.id.img_reccurence);
        linear_recorring = findViewById(R.id.linear_recorring);
        txtOccurs = findViewById(R.id.txtOccurs);
        txtOccurence = findViewById(R.id.txtOccurence);
        imgDelete = findViewById(R.id.imgDelete);
        img_reccurence.setOnClickListener(v -> {
            AnimationSet animationReccurence = Conversions.animation();
            v.startAnimation(animationReccurence);


//            Bundle bundle = new Bundle();
//            bundle.putString("recurrence", recurrence_status);
//            bundle.putString("recurrence_type", recurrence_type);
//            bundle.putString("from_d", from_d);
//            bundle.putString("from_m", from_m);
//            bundle.putString("from_y", from_y);
//            bundle.putString("to_d", to_d);
//            bundle.putString("to_m", to_m);
//            bundle.putString("to_y", to_y);
//            bundle.putBoolean("t_mon", t_mon);
//            bundle.putBoolean("t_tue", t_tue);
//            bundle.putBoolean("t_web", t_web);
//            bundle.putBoolean("t_thu", t_thu);
//            bundle.putBoolean("t_fri", t_fri);
//            bundle.putBoolean("t_sat", t_sat);
//            bundle.putBoolean("t_sun", t_sun);
//            FragmentManager fm = getSupportFragmentManager();
//            ReccurenceFragment sFragment = new ReccurenceFragment();
//            // Show DialogFragment
//            sFragment.onItemsSelectedListner((recurrence, datess, re_type, dt_start, dt_end, starts, ends, weekdays, from__d, from__m, from__y, to__d, to__m, to__y, t__mon, t__tue, t__web, t__thu, t__fri, t__sat, t__sun, count, from__date, to__date) -> {
//                dates_s = new ArrayList<>();
//                weekday_s = new ArrayList();
//                recurrence_status = recurrence;
//                recurrence_type = re_type;
//                dtstart = dt_start;
//                dtend = dt_end;
//                from_d = from__d;
//                from_m = from__m;
//                from_y = from__y;
//                to_d = to__d;
//                to_m = to__m;
//                to_y = to__y;
//                t_mon = t__mon;
//                t_tue = t__tue;
//                t_web = t__web;
//                t_thu = t__thu;
//                t_fri = t__fri;
//                t_sat = t__sat;
//                t_sun = t__sun;
//                dates_s = datess;
//                weekday_s = weekdays;
//                number_of_occurrences_count = count;
//                From_date = from__date;
//                To_date = to__date;
//
//                if (recurrence.equalsIgnoreCase("true")) {
//                    img_reccurence.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
//                } else {
//                    img_reccurence.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hash));
//                }
//
//            });
//            sFragment.show(fm, "Dialog Fragment");
//            sFragment.setArguments(bundle);

            Bundle bundle = new Bundle();
            bundle.putString("recurrence_type", recurrence_type);
            FragmentManager fm = getSupportFragmentManager();
            ReccuringNewFragment sFragment = new ReccuringNewFragment();
            // Show DialogFragment
            sFragment.onItemsSelectedListner((recurrence, daysAndWeeks, daysAndWeeksList, selectionType,fromMillsSeconds,toMillsSeconds,occurrencesCount, fDate, tDate, dhour, dmin, stime, etime, sdate) -> {
                dates_s = new ArrayList<>();
                weekday_s = new ArrayList();

                recurrence_status  =recurrence;
                recurrence_type = selectionType;
                dtstart = fromMillsSeconds;
                dtend = toMillsSeconds;
                number_of_occurrences_count= occurrencesCount;
                From_date = fDate;
                To_date = tDate;
                //time and duration
                d_hour = dhour;
                d_min = dmin;
                s_time = stime;
                e_time = etime;
                s_date = sdate;

                //array list
                dates_s = daysAndWeeks;
                weekday_s = daysAndWeeksList;

                if (recurrence.equalsIgnoreCase("true")) {
                    img_reccurence.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    linear_recorring.setVisibility(View.VISIBLE);
                    txtOccurs.setText(getString(R.string.Occurs_every_day_until)+Conversions.dateToFormat(To_date));
                    txtOccurence.setText(getString(R.string.Number_of_Occurence)+number_of_occurrences_count);
                    date.setClickable(false);
                    meeting_st.setClickable(false);
                } else {
                    img_reccurence.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hash));
                    linear_recorring.setVisibility(GONE);
                }

            });
            sFragment.show(fm, "Dialog Fragment");
            sFragment.setArguments(bundle);

        });

        imgDelete.setOnClickListener(view -> {
            img_reccurence.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hash));
            linear_recorring.setVisibility(GONE);
            recurrence_status = "";
            currentDateSelection();
        });


        //mobile numbers add
        img_phone_list = findViewById(R.id.img_phone_list);

        subject = findViewById(R.id.subject);
        alert = findViewById(R.id.alert);
        entrypoint = findViewById(R.id.entrypoint_spinner);
        meetingRoomSpinner = findViewById(R.id.meetingRoomSpinner);
        meeting_location = findViewById(R.id.location_spinner);

        Intent intent = getIntent();

        RMS_Date = intent.getLongExtra("RMS_Date", 0);
        M_SUGGETION = intent.getIntExtra("M_SUGGETION", 0);
        RMS_STATUS = intent.getIntExtra("RMS_STATUS", 0);
        m_id = intent.getStringExtra("m_id");


        locale = new Locale(DataManger.appLanguage);
        actiontype = 0;
        Conversions.setAgenda(new ArrayList<>());
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        current_date = mDay + "-" + (mMonth + 1) + "-" + mYear;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        EditText agenda = findViewById(R.id.agenda);
        ImageButton agenda_btn = findViewById(R.id.a_btn);
        recyclerView = findViewById(R.id.list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SetupMeetingActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        agendaArrayList = new ArrayList<>();
        if (Conversions.agenda != null && Conversions.agenda.size() != 0) {
            agendaArrayList = Conversions.agenda;
            agendaAdapter = new Adapter1(SetupMeetingActivity.this, agendaArrayList);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(agendaAdapter);
        }
        radioButton1.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            m_type = "1";
            mtype_layout.setVisibility(View.GONE);
            m_roomLayout.setVisibility(View.VISIBLE);
            LELayout.setVisibility(View.VISIBLE);
            pSlotCheckBox.setVisibility(View.VISIBLE);

            Log.e("btn", "1");
        });
        radioButton2.setOnClickListener(v -> {
            AnimationSet abc = Conversions.animation();
            v.startAnimation(abc);
            m_type = "2";
            mtype_layout.setVisibility(View.VISIBLE);
            m_typeDrawable.setBackground(getDrawable(R.drawable.ic_meeting_room));
            m_roomLayout.setVisibility(View.GONE);
            LELayout.setVisibility(View.GONE);
            m_value.setText("");
            m_value.setHint(R.string.please_paste_the_meeting_link_here_);
            Log.e("btn", "2");
            //allotparking Remove
//            allotparkingRemove();
        });
        radioButton3.setOnClickListener(v -> {
            AnimationSet abc = Conversions.animation();
            v.startAnimation(abc);
            m_type = "3";
            mtype_layout.setVisibility(View.VISIBLE);
            m_roomLayout.setVisibility(View.GONE);
            m_typeDrawable.setBackground(getDrawable(R.drawable.ic_pin));
            LELayout.setVisibility(View.GONE);
            m_value.setText("");
            m_value.setHint(R.string.please_enter_the_meeting_location_here_);
            Log.e("btn", "3");
            //allotparking Remove
            //allotparkingRemove();
        });
        pdfUploadLayout.setOnClickListener(v -> {
            AnimationSet animationpdfUploadLayout = Conversions.animation();
            v.startAnimation(animationpdfUploadLayout);
            pickPdf();
        });

        agenda_btn.setOnClickListener(view -> {
            AnimationSet abc = Conversions.animation();
            view.startAnimation(abc);
            if (agenda.getText().length() != 0) {
                if (actiontype == 2){
                    Log.e("actiontype","2");
                    Agenda agenda1 = new Agenda();
                    agenda1.setDesc(agenda.getText().toString());
                    agenda1.setPoints(new ArrayList<>());
                    agenda.setText("");
                    agendaArrayList.add(agenda1);
                    addagendaList.add(agenda1);
                    agendaAdapter.notifyItemInserted(addagendaList.size() - 1);
                    Conversions.setAgenda(agendaArrayList);
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    Agenda agenda1 = new Agenda();
                    agenda1.setDesc(agenda.getText().toString());
                    agenda1.setPoints(new ArrayList<>());
                    agenda.setText("");
                    agendaArrayList.add(agenda1);
                    Conversions.setAgenda(agendaArrayList);
                    agendaAdapter = new Adapter1(SetupMeetingActivity.this, agendaArrayList);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(agendaAdapter);
                }
            }
        });

        m_room_info.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            AmenitiesPopup();
        });
        if (sharedPreferences1.getString("m_id", null) != null) {
            getmeetingdetails();
        } else {
           currentDateSelection();
        }

        List<String> categories = new ArrayList<String>();
        categories.add("30 mins");
        categories.add("1 hr");
        categories.add("2 hrs ");
        categories.add("3 hrs");

        ArrayList<String> MeetingRoom = new ArrayList<String>();
        MeetingRoom.add("MeetingRoom 1");
        MeetingRoom.add("MeetingRoom 2");
        MeetingRoom.add("MeetingRoom 3");
        MeetingRoom.add("MeetingRoom 4");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SetupMeetingActivity.this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        mlist2 = MeetingEndTime();
        mlist4 = AlertArray();
        meetingRoomSpinner.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String str = meetingRoomSpinner.getText().toString();

                for (int i = 0; i < customAdapter1.getCount(); i++) {
                    String temp = Objects.requireNonNull(customAdapter1.getItem(i)).getName();
                    if (str.compareTo(temp) == 0) {
                        return;
                    }
                }
                customAdapter1.getFilter().filter("");
                meetingRoomSpinner.setText("");
                builder.setMessage("Invalid Visitor type")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                ViewController.hideKeyboard(SetupMeetingActivity.this);
                meetingRoomSpinner.showDropDown();
            }
        });

        meetingRoomSpinner.setOnClickListener(arg0 -> {
            AnimationSet animationmeeting_room = Conversions.animation();
            arg0.startAnimation(animationmeeting_room);
            meetingRoomSpinner.showDropDown();
        });

        pSlotCheckBox.setOnClickListener(view -> {
            AnimationSet animationp = Conversions.animation();
            view.startAnimation(animationp);
            //pSlotCheckBox.setChecked(false);

            if (!pSlotCheckBox.isChecked()) {
                allotparkingRemove();
            }else {
                pSlotCheckBox.setChecked(false);
                pSlots = false;
                if (invitedArrayList.isEmpty()) {
                    pSlotCheckBox.setChecked(false);
                    warning_popup();
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    AllotParkingFragment sFragment = new AllotParkingFragment();
                    // Show DialogFragment
                    sFragment.onItemsSelectedListner((allotInvites , lastname , allowCheck) -> {

                        pSlots = allowCheck;
                        pSlotCheckBox.isChecked();
                        pSlotCheckBox.setChecked(true);

                    });
                    sFragment.show(fm, "Dialog Fragment");
                }
            }
        });

        meetingRoomSpinner.setOnItemClickListener((parent, view, index, id) -> {
            CompanyData contactModel = meetingrooms.get(index);
            String name1 = contactModel.getName();
            mIndex = index;
            if (index == 0) {
                samecabin = 1;
                m_room_info.setVisibility(View.GONE);
            } else {
                aString = new ArrayList<>();
                aString.add("Capacity (" + meetingrooms.get(index).getCapacity() + ")");
                ArrayList<Boolean> amenitiesB = new ArrayList<>();
                amenitiesB = meetingrooms.get(index).getAmenities();
                for (int i = 0; i < amenitiesB.size(); i++) {
                    if (amenitiesB.get(i) != null && amenitiesB.get(i)) {
                        aString.add(amenities.get(i).getName());
                    }
                }
                samecabin = 0;
                m_room_info.setVisibility(View.VISIBLE);
            }
            m_room = meetingrooms.get(index).get_id().get$oid();
            Log.e("m_room_:",meetingrooms.get(index).get_id().get$oid().toString());
        });

//        // Handle the item click event
//        meetingRoomSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CompanyData contactModel = meetingrooms.get(position);
//                String name1 = contactModel.getName();
//                // Change the text in the AutoCompleteTextView
//                meetingRoomSpinner.setText(name1 + " selected");
//
//                m_room = meetingrooms.get(position).get_id().get$oid();
//                Log.e("m_room_:",meetingrooms.get(position).get_id().get$oid().toString());
//            }
//        });

        //Reschedule status
        if (sharedPreferences1.getInt("m_action", 0) == 2) {
            name.setEnabled(false);
            img_phone_list.setVisibility(GONE);
            actionBar.setTitle(getResources().getString(R.string.reschedule));
        }

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textValue = name.getText().toString();
                if (textValue.length() == 1) {
                    GUEST_STATUS = true;
                }
                if (GUEST_STATUS) {
                    if (textValue.length() >= 2) {

                        getPhoneNumbersByName(name.getText().toString());

                        String lastLetter = textValue.substring(textValue.length() - 1);
                        if (lastLetter.equals(" ") || lastLetter.equals(",")) {
                            textValue = textValue.trim();
                            textValue = textValue.replace(",", "");
                            if (textValue.matches(emailPattern)) {
                                if (iEmail.contains(textValue)) {
                                } else {
                                    CompanyData contactModel = invitesData.get(count);
                                    long currentDate = Conversions.getstamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
                                    Invited invited = new Invited();
                                    invited.setName(contactModel.getName() + "");
                                    invited.setEmail(contactModel.getEmail());
                                    invited.setMobile(contactModel.getMobile());
                                    invited.setCompany("");
                                    invited.setLink(currentDate + "");
                                    invitedArrayList.add(invited);
                                    AddGuestinvitedArrayList.add(invited);
                                    txt_list_count.setText(invitedArrayList.size() + "");
                                    name.setText("");

                                    Chip newChip = getChip(chipgroup, invited, 1);
                                    iEmail.add(textValue);
                                    chipgroup.addView(newChip);
                                }
                            }
                        }
                    }
                } else {
                    invitesData.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        name.setOnItemClickListener((av, arg1, index, arg3) -> {
            GUEST_STATUS = false;
            long currentDate = Conversions.getstamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
            name.dismissDropDown();
            Boolean aInserted = false;

            CompanyData contactModel = invitesData.get(index);

            if (iEmail.contains(contactModel.getEmail())) {
                aInserted = true;
            }
            if (!aInserted) {
                Invited invited = new Invited();
                invited.setName(contactModel.getName() + "");
                invited.setMobile(contactModel.getMobile() + "");
                invited.setEmail(contactModel.getEmail() + "");
                invited.setCompany("");
                invited.setLink(currentDate + "");
                invitedArrayList.add(invited);
                AddGuestinvitedArrayList.add(invited);
                txt_list_count.setText(invitedArrayList.size() + "");
//                            Adapter1 meetingListAdapter = new Adapter1(SetupMeetingActivity.this,invitedArrayList);
//                            recyclerView.setAdapter(meetingListAdapter);
//                            recyclerView.setVisibility(View.VISIBLE);
                name.setText("");

//                  invite_layout.setVisibility(View.GONE);
                status = 0;
//                  invite.setVisibility(View.VISIBLE);
                Chip newChip = getChip(chipgroup, invited, 1);
                chipgroup.addView(newChip);
                iEmail.add(contactModel.getEmail());
                img_gmails_add.setVisibility(View.GONE);
                relative_emails_list_open_eye.setVisibility(View.VISIBLE);
            } else {

            }
        });

        meeting_et.setOnClickListener(v -> {
            AnimationSet animationmeeting_et = Conversions.animation();
            v.startAnimation(animationmeeting_et);
            ViewController.hideKeyboard(SetupMeetingActivity.this);
            if (date.getText().length() == 0) {
                new AlertDialog.Builder(SetupMeetingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage("Select date")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else if (meeting_st.getText().length() == 0) {
                new AlertDialog.Builder(SetupMeetingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage("Select start time")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                final int hour = 1;
                final int minute = 0;
                final RangeTimePickerDialog mTimePicker;

                mTimePicker = new RangeTimePickerDialog(SetupMeetingActivity.this, (timePicker, selectedHour, selectedMinute) -> {
//                            e_time = Conversions.gettimestamp(SelectedDate,selectedHour + ":" + selectedMinute);
                    d_hour = selectedHour;
                    d_min = selectedMinute;
                    if (DataManger.appLanguage.equals("ar")) {
                        if (d_hour == 1) {
                            meeting_et.setText(Conversions.convertToArabic(selectedHour + "") + " ساعة" + " : " + Conversions.convertToArabic(selectedMinute + "") + " دقيقة");
                        } else {
                            System.out.println("safhjk " + Conversions.convertToArabic(selectedHour + "") + " ساعات" + " : " + Conversions.convertToArabic(selectedMinute + "") + " الدقائق");
                            meeting_et.setText(Conversions.convertToArabic(selectedHour + "") + " ساعات" + " : " + Conversions.convertToArabic(selectedMinute + "") + " الدقائق");
                        }
                    } else {
                        if (d_hour == 1) {
                            meeting_et.setText(selectedHour + " hour" + " : " + selectedMinute + " min");
                        } else {
                            meeting_et.setText(selectedHour + " hours" + " : " + selectedMinute + " min");
                        }
                    }
                    String myTime = SelectedSTime;
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
                    e_time = Conversions.gettimestamp(SelectedDate, newTime);
                }, d_hour, d_min, true);//true = 24 hour time
                mTimePicker.setMin(0, 15);
                mTimePicker.show();
            }
        });

        alert.setText("1 hour");
        alertad = new DurationAdapter(SetupMeetingActivity.this, R.layout.row, R.id.lbl_name, mlist4);
        alert.setThreshold(0);
        alert.setAdapter(alertad);
        alert.setOnClickListener(v -> alert.showDropDown());
        adapter1 = new PeopleAdapter(SetupMeetingActivity.this, R.layout.row, R.id.lbl_name, meetinglocations);
        meeting_location.setThreshold(0);//will start working from first character
        meeting_location.setAdapter(adapter1);//setting the adapter data into the AutoCompleteTextView
        meeting_location.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String str = meeting_location.getText().toString();


                for (int i = 0; i < adapter1.getCount(); i++) {
                    String temp = Objects.requireNonNull(adapter1.getItem(i)).getName();
                    if (str.compareTo(temp) == 0) {
                        return;
                    }
                }
                customAdapter1.getFilter().filter("");
                meeting_location.setText("");

                builder.setMessage("Invalid Visitor type")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                ViewController.hideKeyboard(SetupMeetingActivity.this);
                meeting_location.showDropDown();
            }
        });
        meeting_location.setOnClickListener(arg0 -> {
            AnimationSet animationmeeting_location = Conversions.animation();
            arg0.startAnimation(animationmeeting_location);
            System.out.println("subhasdh" + meetinglocations.size());
            meeting_location.showDropDown();
        });
        meeting_location.setOnItemClickListener((parent, view, index, id) -> {
            AnimationSet animationmeeting_location = Conversions.animation();
            view.startAnimation(animationmeeting_location);
            LocationData contactModel = meetinglocations.get(index);
            String name1 = contactModel.getName();
            System.out.println("subashhh" + "0" + index);
            location = index + "";
//                getmeetingrooms(index + "");
        });
        entrypoint.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String str = entrypoint.getText().toString();
                for (int i = 0; i < customAdapter2.getCount(); i++) {
                    String temp = Objects.requireNonNull(customAdapter2.getItem(i)).getName();
                    if (str.compareTo(temp) == 0) {
                        return;
                    }
                }
                customAdapter2.getFilter().filter("");
                entrypoint.setText("");
                builder.setMessage("Invalid Visitor type")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                ViewController.hideKeyboard(SetupMeetingActivity.this);
                entrypoint.showDropDown();
            }
        });
        entrypoint.setOnClickListener(arg0 -> {
            AnimationSet animationentrypoint = Conversions.animation();
            arg0.startAnimation(animationentrypoint);
            System.out.println("subhasdh" + entrypoints.size());
            entrypoint.showDropDown();
        });
        entrypoint.setOnItemClickListener((parent, view, index, id) -> {
            AnimationSet animationentrypoint = Conversions.animation();
            view.startAnimation(animationentrypoint);
            CompanyData contactModel = entrypoints.get(index);
            String name1 = contactModel.getName();
            e_gate = entrypoints.get(index).get_id().get$oid();
//                tvisitor = contactModel.getName();
        });
        invitedArrayList = new ArrayList<>();
        AddGuestinvitedArrayList = new ArrayList<>();

        date.setOnClickListener(v -> {
            AnimationSet animationdate = Conversions.animation();
            v.startAnimation(animationdate);
            ViewController.hideKeyboard(SetupMeetingActivity.this);
            datePicker();
        });
        meeting_st.setOnClickListener(v -> {
            AnimationSet animationmeeting_st = Conversions.animation();
            v.startAnimation(animationmeeting_st);
            ViewController.hideKeyboard(SetupMeetingActivity.this);
            meeting = "st";
            if (date.getText().length() == 0) {
                new AlertDialog.Builder(SetupMeetingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage("Select date")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                timePicker();
            }
        });

        setup_meeting.setOnClickListener(v -> {
            String textValue = name.getText().toString();
            if (textValue.matches(emailPattern)) {
                if (iEmail.contains(textValue)) {
                } else {
                    System.out.println("suhjadgfsghas");
                    long currentDate = Conversions.getstamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
                    Invited invited = new Invited();
                    invited.setName("x");
                    invited.setEmail(textValue);
                    invited.setMobile("");
                    invited.setCompany("x");
                    invited.setLink(currentDate + "");
                    invitedArrayList.add(invited);
                    AddGuestinvitedArrayList.add(invited);
                    txt_list_count.setText(invitedArrayList.size() + "");
                    name.setText("");
//                           Adapter1 meetingListAdapter = new Adapter1(SetupMeetingActivity.this,invitedArrayList);
//                           recyclerView.setAdapter(meetingListAdapter);
//                           recyclerView.setVisibility(View.VISIBLE);
                    Chip newChip = getChip(chipgroup, invited, 1);
//                  ge
                    iEmail.add(textValue);
                    chipgroup.addView(newChip);
                }
            }
            if (!agenda.getText().toString().equals("")) {
                Agenda agenda1 = new Agenda();
                agenda1.setDesc(agenda.getText().toString());
                agenda1.setPoints(new ArrayList<>());
                agenda.setText("");
                System.out.println("agenda  1" + agenda.getText().toString());
                agendaArrayList.add(agenda1);
                agendaAdapter = new Adapter1(SetupMeetingActivity.this, agendaArrayList);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(agendaAdapter);
            }
            setup_meeting.setEnabled(false);
            if (actiontype == 0 && invitedArrayList.size() == 0) {
                new AlertDialog.Builder(SetupMeetingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.add_at_least_one_invitee_to_proceed)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                setup_meeting.setEnabled(true);
            } else if (subject.getText().toString().length() == 0) {
                new AlertDialog.Builder(SetupMeetingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.enter_subject)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                setup_meeting.setEnabled(true);
            } else if (date.getText().length() == 0) {
                new AlertDialog.Builder(SetupMeetingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage("Select date")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                setup_meeting.setEnabled(true);
            } else if (meeting_st.getText().length() == 0 || meeting_st.getText().toString().equals("Select Time")) {
                new AlertDialog.Builder(SetupMeetingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage("Select start time")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                setup_meeting.setEnabled(true);
            } else if (meeting_et.getText().length() == 0) {
                new AlertDialog.Builder(SetupMeetingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage("Select End time")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                setup_meeting.setEnabled(true);
            } else if (!m_type.equals("1") && m_value.getText().length() == 0) {
                if (m_type.equals("2")) {
                    new AlertDialog.Builder(SetupMeetingActivity.this)
                            .setTitle(R.string.warning)
                            .setMessage(R.string.enter_meeting_link)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                    setup_meeting.setEnabled(true);
                } else {
                    new AlertDialog.Builder(SetupMeetingActivity.this)
                            .setTitle(R.string.warning)
                            .setMessage(R.string.enter_meeting_location)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                    setup_meeting.setEnabled(true);
                }
            } else if (actiontype == 1) {
                JsonObject json = addCovisitorjson();
                System.out.println("json object" + json);
                confirmationPopup(json, 3);
                setup_meeting.setEnabled(true);
            }else {
                if (actiontype == 0){
                    if (roledetails.getBehalfof().equalsIgnoreCase("true")){
                        if (SelfMeetingSetupCheckBox.isChecked() || !AssignID.equalsIgnoreCase("")){
                            setup_meeting.setEnabled(true);
                            gethostslots();
                           // card_view_progress.setVisibility(View.VISIBLE);
                            ViewController.ShowProgressBar(SetupMeetingActivity.this);
                        }else {
                            new AlertDialog.Builder(SetupMeetingActivity.this)
                                    .setTitle(R.string.warning)
                                    .setMessage(R.string.Selfmeetingsetup)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                            setup_meeting.setEnabled(true);
                        }
                    }else {
                        setup_meeting.setEnabled(true);
                        gethostslots();
                        //card_view_progress.setVisibility(View.VISIBLE);
                        ViewController.ShowProgressBar(SetupMeetingActivity.this);
                    }
                }else {
                    setup_meeting.setEnabled(true);
                    gethostslots();
                    //card_view_progress.setVisibility(View.VISIBLE);
                    ViewController.ShowProgressBar(SetupMeetingActivity.this);
                }

            }
        });

        if (RMS_STATUS == 1) {
            RMS_Stime = intent.getLongExtra("RMS_Stime", 0);
            RMS_Etime = intent.getLongExtra("RMS_Etime", 0);
            RMS_Mroom = intent.getIntExtra("RMS_Mroom", 0);
            RMS_Date = (RMS_Date / 1000) - Conversions.timezone();
            System.out.println("sfhasjh" + s_date);
            System.out.println("sfhasjh" + RMS_Date);
            if (s_date <= RMS_Date) {
                s_time = RMS_Stime;
                System.out.println("sdklsdh" + s_time);
                s_date = RMS_Date;
                date.setText(getSdateLabel((RMS_Date + Conversions.timezone()) * 1000));
                meeting_st.setText(Conversions.millitotime((RMS_Stime + Conversions.timezone()) * 1000, false));
            }
        } else if (RMS_STATUS == 2) {
            System.out.println("suhkjsfkjsagf" + s_time);
            System.out.println("suhkjsfkjsagf" + RMS_Date);
            RMS_Stime = RMS_Date + (8 * 60 * 60);
            if (s_date < RMS_Date) {
                s_time = RMS_Stime;
                s_date = RMS_Date;
                date.setText(getSdateLabel((RMS_Date + Conversions.timezone()) * 1000));
                meeting_st.setText(Conversions.millitotime((RMS_Stime + Conversions.timezone()) * 1000, false));
                System.out.println("suhkjsfkjsagf");
            }
        } else if (RMS_STATUS == 3) {

            name.setEnabled(false);
            img_phone_list.setEnabled(false);
            //relative_emails_list_open_eye.setEnabled(false);
            img_reccurence.setEnabled(false);
            relative_emails_list_open_eye.setVisibility(View.VISIBLE);
            apiViewModel.getappointmentsdetails(getApplicationContext(), m_id);

        }

        if (M_SUGGETION != 0) {
            String json = sharedPreferences1.getString("SetupMeetingJson", "");
            JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
            location = convertedObject.get("location").getAsString();
            m_room = sharedPreferences1.getString("SetupMeetingMRoomID", "");

            samecabin = convertedObject.get("samecabin").getAsInt();

            e_gate = convertedObject.get("entrypoint").getAsString();
            try {
                Invited[] arrayList = new Gson().fromJson(convertedObject.get("invites").getAsJsonArray(), Invited[].class);
                invitedArrayList = new ArrayList<Invited>(Arrays.asList(arrayList));
                System.out.println("InvitedSize" + invitedArrayList.size());
                for (int i = 0; i < invitedArrayList.size(); i++) {
                    Chip newChip = getChip(chipgroup, invitedArrayList.get(i), 1);
                    iEmail.add(invitedArrayList.get(i).getEmail());
                    chipgroup.addView(newChip);
                }
            } catch (Exception e) {
            }

            Agenda[] arraylist2 = new Gson().fromJson(convertedObject.get("agenda").getAsJsonArray(), Agenda[].class);
            agendaArrayList = new ArrayList<Agenda>(Arrays.asList(arraylist2));
            subject.setText(convertedObject.get("subject").getAsString());
            meeting_description.setText(convertedObject.get("desc").getAsString());
            meeting_location.setText(sharedPreferences1.getString("SetupMeetingLocation", ""));
            meetingRoomSpinner.setText(sharedPreferences1.getString("SetupMeetingMRoom", ""));
            entrypoint.setText(sharedPreferences1.getString("SetupMeetingEntryPoint", ""));

            if (M_SUGGETION == 1) {
                s_time = convertedObject.get("start").getAsLong();
                e_time = convertedObject.get("end").getAsLong() + 1;
                s_date = convertedObject.get("date").getAsLong();
            } else if (M_SUGGETION == 2) {
                s_time = intent.getLongExtra("RMS_Stime", 0);
                e_time = intent.getLongExtra("RMS_Etime", 0);
                s_date = intent.getLongExtra("RMS_Date", 0);
                s_date = (s_date / 1000) - Conversions.timezone();
            }
            Long d = e_time - s_time;
            d_hour = (int) Math.floor(d / 3600);
            d_min = (int) Math.floor((d % 3600) / 60);
            SelectedDate = ViewController.getSdate((s_time + Conversions.timezone()) * 1000);
            date.setText(getSdateLabel((s_time + Conversions.timezone()) * 1000));
            if (ViewController.getCurrentTimeStamp().equals(ViewController.getSdate((s_date + Conversions.timezone()) * 1000))) {
                date_status = 0;
            } else {
                date_status = 1;
            }

            SelectedSTime = Conversions.millitotime((s_time + Conversions.timezone()) * 1000, true);
            SelectedETime = Conversions.millitotime((e_time + Conversions.timezone()) * 1000, true);

            SelectedHour = Integer.parseInt(Conversions.millisTotime((s_time + Conversions.timezone()) * 1000, true));
            SelectedMin = Integer.parseInt(Conversions.millisTotime((e_time + Conversions.timezone()) * 1000, false));
            meeting_st.setText(Conversions.millitotime((s_time + Conversions.timezone()) * 1000, false));
            meeting_et.setText(d_hour + "hour : " + d_min + " min");
            if (d_hour > 0) {
                meeting_et.setText(d_hour + "hours : " + d_min + " min");
            }
            if (agendaArrayList.size() != 0) {
                agendaAdapter = new Adapter1(SetupMeetingActivity.this, agendaArrayList);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(agendaAdapter);
            }
        }

        img_gmails_add.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            String str_emails = name.getText().toString().replace(" ", "");
            ;
            if (str_emails.equalsIgnoreCase("")) {
            } else {
                String[] arrOfStr = str_emails.split(",");
                for (int i = 0; i < arrOfStr.length; i++) {
                    getInviteData(arrOfStr[i], "list_type");
                    Log.e(TAG, "onClick:count" + arrOfStr[i]);
                }
            }
        });
        relative_emails_list_open_eye.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            Bundle bundle = new Bundle();
            bundle.putInt("RMS_STATUS", RMS_STATUS);

            FragmentManager fm = getSupportFragmentManager();
            SetUpMeetingSelectedMailsMobileListFragment sFragment = new SetUpMeetingSelectedMailsMobileListFragment();
            // Show DialogFragment
            sFragment.onItemsSelectedListner((comId, DepName, CompName) -> {
            });
            sFragment.show(fm, "Dialog Fragment");
            sFragment.setArguments(bundle);
        });

        img_phone_list.setOnClickListener(v -> {
            AnimationSet animationimg_phone_list = Conversions.animation();
            v.startAnimation(animationimg_phone_list);

            pickContact();

        });


        //key board enter key
        name.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        //hide keyboard
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        popup_add_contact();

                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

        apiViewModel.getappointmentsdetails_response().observe(this, response -> {
            if (response != null) {
                appointments_model = response;
                Log.e(TAG, "onResponse:ssp: " + appointments_model.items.getPurpose());
                if (appointments_model != null) {
                    v_id = appointments_model.items.get_id().get$oid();
                    subject.setText(appointments_model.items.getPurpose());
                    if (appointments_model.items.getNote() != null) {
                        meeting_description.setText(appointments_model.items.getNote());
                    }
                    //chip group
                    Invited invited = new Invited();
                    invited.setName(appointments_model.items.getvData().getName());
                    invited.setEmail(appointments_model.items.getvData().getEmail());
                    invited.setMobile(appointments_model.items.getvData().getMobile());
                    invited.setCompany("");
                    invited.setLink("");
                    invitedArrayList.add(invited);
                    txt_list_count.setText(invitedArrayList.size() + "");
                    status = 0;
                    Chip newChip = getChip(chipgroup, invited, 1);
                    chipgroup.addView(newChip);
                    iEmail.add(appointments_model.items.getvData().getEmail());
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "getappointmentsdetails");
            }
        });

        apiViewModel.updateappointment_response().observe(this, response -> {
            if (response != null) {
                Intent intent1 = new Intent(SetupMeetingActivity.this, NavigationActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent1);
            }
        });

        apiViewModel.getentrypoints_response().observe(this, response -> {
            //card_view_progress.setVisibility(GONE);
            ViewController.DismissProgressBar();
            if (response != null) {
                Integer statusCode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statusCode.equals(failurecode)) {

                } else if (statusCode.equals(not_verified)) {

                } else if (statusCode.equals(successcode)) {
                    entrypoints = response.getItems();

                    if (actiontype == 0 && M_SUGGETION == 0) {
                        entrypoint.setText(entrypoints.get(0).getName());
                        e_gate = entrypoints.get(0).get_id().get$oid();
                    }
                    customAdapter2 = new CustomAdapter(SetupMeetingActivity.this, R.layout.row, R.id.lbl_name, entrypoints, 0, "");
                    entrypoint.setThreshold(1);//will start working from first character
                    entrypoint.setAdapter(customAdapter2);//setting the adapter data into the AutoCompleteTextView
                    entrypoint.setEnabled(true);
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "getentrypoints");
            }
        });

        apiViewModel.gethostslots_response().observe(this, response -> {
            //card_view_progress.setVisibility(GONE);
            ViewController.DismissProgressBar();
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)) {

                } else if (statuscode.equals(successcode)) {
                    ArrayList<HostSlotsData> hostSlotsData = new ArrayList<>();
                    hostSlotsData = response.getItems();
                    if (hostSlotsData.size() == 0) {
                        if (meetingRoomSpinner.getText().toString().equals(empData.getName() + " - Cabin") || !m_type.equals("1")) {
                            if (actiontype == 0) {
                                JsonObject json = createjson();
                                System.out.println("json object" + json);
                                confirmationPopup(json, 1);
                            } else {
                                JsonObject json = reschedulejson();
                                System.out.println("json object" + json);
                                confirmationPopup(json, 2);
                            }
                        } else {
                            getrmslots(m_room);
                        }
                    } else if (hostSlotsData.size() == 1 && hostSlotsData.get(0).get_id().get$oid().equals(sharedPreferences1.getString("m_id", ""))) {
                        JsonObject json = reschedulejson();
                        confirmationPopup(json, 2);
                    } else {
                        new AlertDialog.Builder(SetupMeetingActivity.this)
//                                   .setTitle(R.string.warning)
                                .setMessage(R.string.you_have_another_meeting_at_this_time_)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                        setup_meeting.setEnabled(true);
                    }
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "gethostslots");
            }
        });

        apiViewModel.getrmslots_response().observe(this, response -> {
            //card_view_progress.setVisibility(GONE);
            ViewController.DismissProgressBar();
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)) {

                } else if (statuscode.equals(successcode)) {
                    ArrayList<CompanyData> companyData = new ArrayList<>();
                    JsonObject json;
                    if (actiontype == 0) {
                        json = createjson();
                    } else {
                        json = reschedulejson();
                    }
                    companyData = response.getItems();
                    if (companyData.size() == 0) {
                        if (actiontype == 0) {
                            confirmationPopup(json, 1);
                        } else {
                            confirmationPopup(json, 2);
                        }
                    } else {
                        editor1 = sharedPreferences1.edit();
                        editor1.putString("SetupMeetingJson", json.toString());
                        editor1.putString("SetupMeetingLocation", meeting_location.getText().toString());
                        editor1.putString("SetupMeetingMRoom", meetingRoomSpinner.getText().toString());
                        editor1.putString("SetupMeetingMRoomID", m_room);
                        editor1.putString("SetupMeetingEntryPoint", entrypoint.getText().toString());
                        editor1.apply();
                        setup_meeting.setEnabled(true);
                        Intent intent12 = new Intent(SetupMeetingActivity.this, MeetingRoomEngagedActivity.class);
                        startActivity(intent12);
                    }
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "getrmslots");
            }
        });

        apiViewModel.getmeetingdetails_response().observe(this, response -> {
            //card_view_progress.setVisibility(GONE);
            ViewController.DismissProgressBar();
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)) {

                } else if (statuscode.equals(successcode)) {
                    if (sharedPreferences1.getInt("m_action", 0) == 1) {
                        actionBar.setTitle(getResources().getString(R.string.add_guest));
                        actiontype = 1;
                        setup_meeting.setText(getResources().getString(R.string.add_guest));
                        subject.setEnabled(false);
                        agenda.setEnabled(false);
                        date.setEnabled(false);
                        pSlotCheckBox.setEnabled(true);
                        img_category_info.setEnabled(false);
                        meeting_st.setEnabled(false);
                        meeting_et.setEnabled(false);
                        meetingRoomSpinner.setEnabled(false);
                        meeting_location.setEnabled(false);
                        entrypoint.setEnabled(false);
                        radioButton1.setEnabled(false);
                        radioButton2.setEnabled(false);
                        radioButton3.setEnabled(false);
                        meeting_description.setEnabled(false);
                        category_spinner.setEnabled(false);
                        alert.setEnabled(false);
                        pdfUploadLayout.setEnabled(false);
                    } else {
                        actiontype = 2;
                        actionBar.setTitle(getResources().getString(R.string.reschedule));
                        setup_meeting.setText(getResources().getString(R.string.reschedule));
                        name.setEnabled(false);
                        meeting_st.setFocusable(false);
                        meeting_et.setFocusable(false);
                        alert.setFocusable(false);
                        getmeetingrooms(empData.getLocation());
                    }

                    meeting_details = response.getItems();



                    if (meeting_details.getAgenda().size() != 0) {

                        for (int i = 0; i < meeting_details.getAgenda().size(); i++) {
                            // Add all items to agendaArrayList
                            agendaArrayList.add(meeting_details.getAgenda().get(i));

                            // Only add items with status "0.0" to addagendaList
                            if (meeting_details.getAgenda().get(i).getStatus().equals("0.0")) {
                                addagendaList.add(meeting_details.getAgenda().get(i));
                                recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView if there are items to display
                            }
                        }

                        // Set up the RecyclerView adapter with addagendaList
                        agendaAdapter = new Adapter1(SetupMeetingActivity.this, addagendaList);
                        recyclerView.setAdapter(agendaAdapter);
                    }

//                        progress.dismiss()
//                        date.setText(meeting_details.);
//                        meeting_st.setEnabled(false);
//                        meeting_et.setEnabled(false);
//                        alert.setEnabled(false);

                    samecabin = meeting_details.getSamecabin();
                    s_date = meeting_details.getDate();
                    s_time = meeting_details.getStart();
                    e_time = meeting_details.getEnd();
                    m_room = meeting_details.getMeetingroom();
                    location = meeting_details.getLocation();
                    e_gate = meeting_details.getEntrypoint();

                    if (M_SUGGETION == 0) {
                        Long d = meeting_details.getEnd() - meeting_details.getStart() + 1;
                        d_hour = (int) Math.floor(d / 3600);
                        d_min = (int) Math.floor((d % 3600) / 60);
                        SelectedDate = ViewController.getSdate((meeting_details.getStart() + Conversions.timezone()) * 1000);
                        date.setText(getSdateLabel((meeting_details.getStart() + Conversions.timezone()) * 1000));
//                        System.out.println("date123 "+ getCurrentTimeStamp() +" "+);
//                        Long m_date = (meeting_details.getDate()+Conversions.timezone() * 1000);
//                        String c_date = getCurrentTimeStamp();
//                        System.out.println("m_date "+ m_date + " c_date "+c_date );
                        if (ViewController.getCurrentTimeStamp().equals(ViewController.getSdate((meeting_details.getDate() + Conversions.timezone()) * 1000))) {
                            date_status = 0;
                        } else {
                            date_status = 1;
                        }
                        SelectedSTime = Conversions.millitotime((meeting_details.getStart() + Conversions.timezone()) * 1000, true);
                        SelectedETime = Conversions.millitotime((meeting_details.getEnd() + Conversions.timezone()) * 1000, true);
                        meeting_st.setText(Conversions.millitotime((meeting_details.getStart() + Conversions.timezone()) * 1000, false));
                        meeting_et.setText(d_hour + "hour : " + d_min + " min");
                        if (d_hour > 0) {
                            meeting_et.setText(d_hour + "hours : " + d_min + " min");
                        }
                    }
                    if (meeting_details.getMtype().equals("2")) {
                        radioButton2.setChecked(true);
                        pSlotCheckBox.setVisibility(GONE);
                        m_type = "2";
                        mtype_layout.setVisibility(View.VISIBLE);
                        m_typeDrawable.setBackground(getDrawable(R.drawable.ic_meeting_room));
                        m_roomLayout.setVisibility(View.GONE);
                        LELayout.setVisibility(View.GONE);
                        m_value.setHint(R.string.please_paste_the_meeting_link_here_);
                    } else if (meeting_details.getMtype().equals("3")) {
                        radioButton3.setChecked(true);
                        pSlotCheckBox.setVisibility(GONE);
                        m_type = "3";
                        mtype_layout.setVisibility(View.VISIBLE);
                        m_typeDrawable.setBackground(getDrawable(R.drawable.ic_pin));
                        m_roomLayout.setVisibility(View.GONE);
                        LELayout.setVisibility(View.GONE);
                        m_value.setHint(R.string.please_enter_the_meeting_location_here_);
                    } else {
                        m_type = "1";
                    }

                    Log.e(TAG, "onResponse:subject" + meeting_details.getSubject());
                    //reccurence hide in reschedule and assign conditions
                    img_reccurence.setVisibility(View.GONE);
                    subject.setText(meeting_details.getSubject());
                    pSlots = meeting_details.getPslots();
                    pSlotCheckBox.setChecked(pSlots);
                    m_value.setText(meeting_details.getMtype_val());
                    meeting_description.setText(meeting_details.getDesc());

                   //category
                    Log.e("category_itemm_value_",meeting_details.getMtype_val());
                    category_item = meeting_details.getCategoryData().get_id().get$oid();

                    meetingRoomSpinner.setText(meeting_details.getMeetingrooms().getName());
                    meeting_location.setText(meeting_details.getLocations().getName());
                    entrypoint.setText(meeting_details.getEntrypoints().getName());
                    Conversions.setAgenda(meeting_details.getAgenda());
                    alert.setEnabled(false);
                    for (int i = 0; i < meeting_details.getInvites().size(); i++) {
                        invitedArrayList.add(meeting_details.getInvites().get(i));

                        txt_list_count.setText(invitedArrayList.size() + "");
                        img_gmails_add.setVisibility(View.GONE);
                        relative_emails_list_open_eye.setVisibility(View.VISIBLE);

                        Chip newChip = getChip(chipgroup, meeting_details.getInvites().get(i), 2);

                        if (meeting_details.getInvites().get(i).getEmail().equalsIgnoreCase("")) {

                        } else if (meeting_details.getInvites().get(i).getEmail().matches(emailPattern)) {
                            iEmail.add(meeting_details.getInvites().get(i).getEmail());
                        } else {
                            iEmail.add(meeting_details.getInvites().get(i).getMobile());
                        }
                        chipgroup.addView(newChip);
                    }
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "getmeetingdetails");
            }
        });


        apiViewModel.actionmeetings_total_count_response().observe(this, response -> {
            //card_view_progress.setVisibility(GONE);
            ViewController.DismissProgressBar();
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)) {

                } else if (statuscode.equals(successcode)) {
                    String Total_counts = response.getTotal_counts();
                    Log.e(TAG, "onResponse:total_counts " + Total_counts);

                    if (Total_counts != null) {
                        acceptappointment(Total_counts);
                    }
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "actionmeetings_total_count");
            }
        });

        apiViewModel.actionmeetings_response().observe(this, response -> {
            //card_view_progress.setVisibility(GONE);
            ViewController.DismissProgressBar();
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)) {

                } else if (statuscode.equals(successcode)) {
                    Intent intent13 = new Intent(SetupMeetingActivity.this, NavigationActivity.class);
                    intent13.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent13.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent13);
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "actionmeetings");
            }
        });

        apiViewModel.updateappointment_response().observe(SetupMeetingActivity.this, response -> {
            if (response != null) {
                Intent intent14 = new Intent(SetupMeetingActivity.this, NavigationActivity.class);
                intent14.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent14.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent14);
            }
        });

        apiViewModel.addcovisitor_response().observe(SetupMeetingActivity.this, response -> {
            //card_view_progress.setVisibility(GONE);
            ViewController.DismissProgressBar();
            if (response != null) {
                Intent intent15 = new Intent(SetupMeetingActivity.this, NavigationActivity.class);
                intent15.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent15.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                editor1 = sharedPreferences1.edit();
                editor1.remove("m_id");
                editor1.remove("m_action");
                editor1.apply();
                startActivity(intent15);
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "addcovisitor");
            }
        });

        apiViewModel.updatemeetings_response().observe(SetupMeetingActivity.this, response -> {
            //card_view_progress.setVisibility(GONE);
            ViewController.DismissProgressBar();
//            if (response != null) {
//
//            }

            Intent intent16 = new Intent(SetupMeetingActivity.this, NavigationActivity.class);
            intent16.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent16.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            editor1 = sharedPreferences1.edit();
            editor1.remove("m_id");
            editor1.remove("m_action");
            editor1.apply();
            startActivity(intent16);

        });

        apiViewModel.pdfupload_response().observe(SetupMeetingActivity.this, response -> {
            if (response != null) {
            }
        });

        apiViewModel.getcategories_response().observe(SetupMeetingActivity.this, response -> {
            if (response != null) {
                ArrayList<CompanyData> CategoryList = new ArrayList<>();

                CategoryList = response.getItems();

                if (CategoryList != null && CategoryList.size() > 0) {
                    String[] Categorys = new String[CategoryList.size()];

                    for (int i = 0; i < CategoryList.size(); i++) {
                        Categorys[i] = CategoryList.get(i).get_id().get$oid();
                        if (CategoryList.get(i).getCat_type().equalsIgnoreCase("true")) {
                            Categorys[i] = CategoryList.get(i).getName() + " (Confidential)";
                        } else {
                            Categorys[i] = CategoryList.get(i).getName() + " (Non-Confidential)";
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SetupMeetingActivity.this, android.R.layout.simple_spinner_item, Categorys);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        category_spinner.setAdapter(spinnerArrayAdapter);
                        category_spinner.setSelection(1);
                    }

                    ArrayList<CompanyData> finalCategoryList = CategoryList;
                    category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            AnimationSet animationcategory_spinner = Conversions.animation();
                            view.startAnimation(animationcategory_spinner);
                            Log.e(TAG, "onItemSelected:ss " + finalCategoryList.get(position).get_id().get$oid());
                            category_item = finalCategoryList.get(position).get_id().get$oid();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "getcategories");
            }
        });


        //remove PDF
        pdfClear.setOnClickListener(view -> {
            if (pdfsArrayList != null && pdfsArrayList.size() != 0) {
                try {
                    pdfsArrayList.clear();
                    pdfClear.setVisibility(GONE);
                    pdfName.setText(R.string.UploadPDF);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //selfinvitation
        selfInvitaion();


    }

    protected void registoreNetWorkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void selfInvitaion() {

        SelfMeetingSetupCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (SelfMeetingSetupCheckBox.isChecked()){
                linearDepartment.setVisibility(GONE);
            }else {
                linearDepartment.setVisibility(View.VISIBLE);
            }
        });

        //department
        apiViewModel.getsubhierarchys(getApplicationContext(),empData.getLocation());
        apiViewModel.getsubhierarchys_response().observe(this, response -> {
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {
                } else if (statuscode.equals(not_verified)) {
                } else if (statuscode.equals(successcode)) {
                    departments = new ArrayList<>();


                    if (response.getItems().size()>0){
                        if (empData.getMeeting_assist().size()>0){
                            for (int i = 0; i < response.getItems().size(); i++) {
                                for (int j = 0; j < empData.getMeeting_assist().size(); j++) {
                                    if (response.getItems().get(i).get_id().get$oid().equalsIgnoreCase(empData.getMeeting_assist().get(j))){
                                        departments.add(response.getItems().get(i));
                                    }
                                }
                            }
                        }else {
                            departments = response.getItems();
                        }
                    }

                    departmentAdapter = new DepartmentAdapter(SetupMeetingActivity.this, R.layout.row, R.id.lbl_name, departments,0,"");
//                    department_spinner.setInputType(InputType.TYPE_NULL);
                    department_spinner.setThreshold(0);
                    department_spinner.setAdapter(departmentAdapter);
                    department_spinner.clearFocus();

                    department_spinner.setText("");
                    AssignID = "";
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "getsubhierarchys");
            }
        });

        department_spinner.setOnClickListener(v -> department_spinner.showDropDown());
        department_spinner.setOnItemClickListener((parent, view, position, id) -> getsearchemployees(empData.getLocation(), departments.get(position).get_id().get$oid()));

        //host
        apiViewModel.getsearchemployees_response().observe(this, response -> {
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {
                } else if (statuscode.equals(not_verified)) {
                } else if (statuscode.equals(successcode)) {
                    employees = new ArrayList<>();
                    employees = response.getItems();

                    if(employees.isEmpty()){
                        department_spinner.setText("");
                    }
                    else{
                        employeeAdapter = new CustomAdapter(SetupMeetingActivity.this, R.layout.row, R.id.lbl_name, employees,0,"");
                        emp_spinner.setThreshold(0);
                        emp_spinner.setAdapter(employeeAdapter);
                    }
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "getsearchemployees");
            }
        });

        emp_spinner.setOnClickListener(v -> emp_spinner.showDropDown());

        emp_spinner.setOnItemClickListener((parent, view, position, id) -> {

            AssignID = employees.get(position).get_id().get$oid();

//            Toast.makeText(getApplicationContext(), Assignemail, Toast.LENGTH_LONG).show();
//            Invited invited = new Invited();
//            invited.setId("1");
//            invited.setName(employees.get(position).getEmail());
//            invited.setEmail(Assignemail);
//            invited.setMobile(employees.get(position).getMobile());
//            invited.setAssign(true);
//            invitedArrayList.add(invited);
//            //add host
//            AddGuestinvitedArrayList.add(invited);
//            iEmail.add(employees.get(position).getMobile());
//            img_gmails_add.setVisibility(View.GONE);
//            relative_emails_list_open_eye.setVisibility(View.VISIBLE);
//            txt_list_count.setText(invitedArrayList.size() + "");
        });


    }

    private void getsearchemployees(String l_id, String h_id) {
        apiViewModel.getsearchemployees(getApplicationContext(),l_id,h_id);
    }

    private void allotparkingRemove() {
        pSlots = false;
        pSlotCheckBox.setChecked(false);

        if (invitedArrayList.size()>0){
            for (int i = 0; i < invitedArrayList.size(); i++) {
                Invited update_position = invitedArrayList.get(i);
                update_position.setCat_id("");
                update_position.setLot_id("");
                update_position.setPslot(0);
                update_position.setSlot("");
                update_position.setAuto_allot(false);
            }
        }
    }


    private void currentDateSelection() {

        Log.e(TAG, "onCreate:ss" + "helo");

        if (roledetails.getBehalfof().equalsIgnoreCase("true")) {
            linearSelfAssist.setVisibility(View.VISIBLE);
            if (actionBar != null) {
                actionBar.setTitle("Self Meeting Coordinator");
            }
        }

        date.setClickable(true);
        date.setFocusable(false);
        date.setText(getCurrentTimeStampLabel());
        SelectedDate = ViewController.getCurrentTimeStamp();
        meeting_st.setClickable(true);
        meeting_st.setFocusable(false);
        meeting_et.setFocusable(false);
        alert.setFocusable(false);
        String location1 = meetinglocations.get(Integer.parseInt(empData.getLocation())).getName();
        meeting_location.setText(location1);
        getmeetingrooms(empData.getLocation());
        s_date = Conversions.getdatestamp(SelectedDate);


        SelectedSTime = getTimeStamp(false);
        s_time = Conversions.gettimestamp(SelectedDate, SelectedSTime);
        meeting_st.setText(getTimeStamp(true));

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(SelectedSTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        d_hour = 1;
        d_min = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR_OF_DAY, d_hour);
        cal.add(Calendar.MINUTE, d_min);
        String newTime = df.format(cal.getTime());

        s_hour = cal.get(Calendar.HOUR_OF_DAY);
        s_min = cal.get(Calendar.MINUTE);
        e_time = Conversions.gettimestamp(SelectedDate, newTime);
        meeting_et.setText("1 hour : 00 min");
        if (DataManger.appLanguage.equals("ar")) {
            meeting_et.setText(Conversions.convertToArabic("1") + " ساعة" + " : " + Conversions.convertToArabic("00") + " دقيقة");
        }
        System.out.println("s_time" + s_time);
        System.out.println("e_time" + e_time);
    }

    //device contacts selection
    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    private void getContactDetails(Uri contactUri) {

        String contactId = "";
        String phonecontactName = "";
        String phoneNumber = "";
        String emailAddress = "";
        LinkedList<String> listOfPhoneNumbers = new LinkedList<>();
        LinkedList<String> listOfEmailAddress = new LinkedList<>();

        Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            phonecontactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // phone numbers
            Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{contactId},
                    null
            );
            listOfPhoneNumbers = new LinkedList<>();
            if (phoneCursor != null && phoneCursor.getCount() > 0) {
                while (phoneCursor.moveToNext()) {
                    phonecontactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String phoneN = phoneNumber.replaceAll(" ", "");
                    listOfPhoneNumbers.add(phoneN);
                }
            }
            phoneCursor.close();

            cursor.close();

            //email
            Cursor cursorEmail = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{contactId}, null);
            listOfEmailAddress = new LinkedList<>();
            if (cursorEmail.getCount() > 0) {
                while (cursorEmail.moveToNext()) {
                    emailAddress = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    String emailA = emailAddress.replaceAll(" ", "");
                    listOfEmailAddress.add(emailA);
                }
            }
            cursorEmail.close();

            Contact_addtolist(contactId, phonecontactName, phoneNumber, emailAddress, listOfPhoneNumbers, listOfEmailAddress);

        }

    }

    private void Contact_addtolist(String contactId, String phonecontactName, String phoneNumber, String emailAddress, LinkedList<String> listOfPhone, LinkedList<String> listOfEmail) {


        LinkedList<String> listOfPhoneNumbers = listOfPhone;
        LinkedList<String> EmailAddresslist = listOfEmail;

        //duplicates remove
        duplicatenumbersandemailsremoved(listOfPhoneNumbers, EmailAddresslist);

        String self_result = SetupMeetingActivity.empData.getMobile().replaceAll("[\\s-.^:,]", "");
        String replace_number = phoneNumber.replaceAll("[\\s-.^:,]", "");


        //only one number condition
        if (listOfPhoneNumbers.size() <= 1) {

            //self meeting check
            if (SetupMeetingActivity.empData.getEmail().equalsIgnoreCase(emailAddress) || self_result.equalsIgnoreCase(replace_number)) {
                int colorInt = ContextCompat.getColor(getApplicationContext(), R.color.Accept);
                ViewController.snackbar(SetupMeetingActivity.this,getApplicationContext().getString(R.string.self_account_not_acceptable), colorInt);
            } else {

                String re = "";

                //already in existing list
                if (invitedArrayList.size() != 0) {

                    for (int i = 0; i < invitedArrayList.size(); i++) {
                        if (invitedArrayList.get(i).getId().equalsIgnoreCase(contactId)) {
                            re = invitedArrayList.get(i).getId();
                        }
                    }

                    if (!re.equalsIgnoreCase(contactId)) {
                        //single number add
                        single_number_adding(contactId, phonecontactName, emailAddress, replace_number);
                    }

                } else {
                    //single number add
                    single_number_adding(contactId, phonecontactName, emailAddress, replace_number);
                }
            }

        } else {
            //Multiple numbers list
            showMultipleContactsDialog(contactId, replace_number, phonecontactName, listOfPhoneNumbers, EmailAddresslist);
        }



    }

    //duplicates remove
    private void duplicatenumbersandemailsremoved(LinkedList<String> listOfPhoneN, LinkedList<String> emailAddressl) {

        LinkedList<String> listOfPhoneNumbers = listOfPhoneN;
        LinkedList<String> EmailAddresslist = emailAddressl;

        //duplicate numbers and emails removed
        //numbers
        HashSet<String> setnumbers = new HashSet<>();
        for (String ss : listOfPhoneNumbers) {
            setnumbers.add(ss);
        }
        listOfPhoneNumbers.clear();
        listOfPhoneNumbers.addAll(setnumbers);
        //remove duplicate numbers items in list
        for (int j = 0; j <= listOfPhoneNumbers.size(); j++) {
            HashSet<String> uniqueNumbers = new HashSet<>(listOfPhoneNumbers);
            listOfPhoneNumbers.clear();
            listOfPhoneNumbers.addAll(uniqueNumbers);
        }

        //emails
        HashSet<String> setemails = new HashSet<>();
        for (String emails : EmailAddresslist) {
            setemails.add(emails);
        }
        EmailAddresslist.clear();
        EmailAddresslist.addAll(setemails);
        //remove duplicate Emails items in list
        for (int j = 0; j <= EmailAddresslist.size(); j++) {
            HashSet<String> uniqueemail = new HashSet<>(EmailAddresslist);
            EmailAddresslist.clear();
            EmailAddresslist.addAll(uniqueemail);
        }
    }

    //SingleContact
    private void single_number_adding(String contactId, String phonecontactName, String emailAddress, String phoneNumber) {

        if (!phoneNumber.equalsIgnoreCase("") || !emailAddress.equalsIgnoreCase("")){

            Invited invited = new Invited();
            invited.setId(contactId);
            invited.setName(phonecontactName);
            invited.setEmail(emailAddress);
            invited.setCompany("");
            invited.setLink("");
            invited.setMobile("");

            if (!phoneNumber.equalsIgnoreCase("")) {
                String[] phonenumbers = {phoneNumber};
                for (String phone : phonenumbers) {
                    if (isPhoneNumberValid(phone)) {
                        if (phoneNumber.startsWith("+91") || phoneNumber.startsWith("0091") || phoneNumber.startsWith("00966") || phoneNumber.startsWith("+966")) {
                            invited.setMobile(phoneNumber);
                        } else {
                            invited.setMobile("+966" + phoneNumber);
                        }
                    } else {
                        phoneNumber = "";
                        invited.setMobile(phoneNumber);
                        int colorInt = ContextCompat.getColor(getApplicationContext(), R.color.red);
                        ViewController.snackbar(SetupMeetingActivity.this,getApplicationContext().getString(R.string.Mobile_number_not_valid), colorInt);
                    }
                }
            }


            if (phoneNumber.equalsIgnoreCase("") && emailAddress.equalsIgnoreCase("")){

            }else {
                invitedArrayList.add(invited);
                //addguest
                AddGuestinvitedArrayList.add(invited);
                iEmail.add(phoneNumber);
                img_gmails_add.setVisibility(View.GONE);
                relative_emails_list_open_eye.setVisibility(View.VISIBLE);
                txt_list_count.setText(invitedArrayList.size() + "");
            }

        }else {
            int colorInt = ContextCompat.getColor(getApplicationContext(), R.color.red);
            ViewController.snackbar(SetupMeetingActivity.this,getApplicationContext().getString(R.string.Wrong_address),colorInt);
        }

    }

    //MultipleContacts
    private void showMultipleContactsDialog(String contactid, String contactphonenumber, String contactName, LinkedList<String> listOfPhoneNumbers, LinkedList<String> emailAddresslist) {

        final Dialog dialog = new Dialog(SetupMeetingActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_multiple_contacts);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvDone = dialog.findViewById(R.id.tvDone);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        LinearLayout llEmail = dialog.findViewById(R.id.llEmail);
        TextView tvContactName = dialog.findViewById(R.id.tvContactName);
        RecyclerView rvPhoneNumbers = dialog.findViewById(R.id.rvPhoneNumbers);
        RecyclerView rvEmailAddress = dialog.findViewById(R.id.rvEmailAddress);


        tvDone.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);

            String self_result = SetupMeetingActivity.empData.getMobile().replaceAll("[-.^:,]", "");
            String replace_number = selectedPhoneNumber.replaceAll("[-.^:,]", "");

            //self meeting check
            if (SetupMeetingActivity.empData.getEmail().equalsIgnoreCase(selectedEmailAddress) || self_result.equalsIgnoreCase(replace_number)) {
                int colorInt = ContextCompat.getColor(getApplicationContext(), R.color.red);
                ViewController.snackbar(SetupMeetingActivity.this,getApplicationContext().getString(R.string.self_account_not_acceptable),colorInt);
            }else {

                String res = "";
                Invited update_position = null;

                //already in existing list
                if (invitedArrayList.size() != 0) {

                    for (int i = 0; i < invitedArrayList.size(); i++) {
                        if (invitedArrayList.get(i).getId().equalsIgnoreCase(contactid)) {
                            res = invitedArrayList.get(i).getId();
                            update_position = invitedArrayList.get(i);

                            //update item
                            update_position.setId(contactid);
                            update_position.setName(contactName);
                            update_position.setChecked(true);
                            update_position.setCompany("");
                            update_position.setLink("");

                            if (!selectedPhoneNumber.equalsIgnoreCase("")) {
                                String[] phonenumbers = {selectedPhoneNumber};
                                for (String phone : phonenumbers) {
                                    if (isPhoneNumberValid(phone)) {
                                        if (selectedPhoneNumber.startsWith("+91") || selectedPhoneNumber.startsWith("0091") || selectedPhoneNumber.startsWith("00966") || selectedPhoneNumber.startsWith("+966")) {
                                            update_position.setMobile(selectedPhoneNumber);
                                        } else {
                                            update_position.setMobile("+966" + selectedPhoneNumber);
                                        }
                                    } else {
                                        int colorInt = ContextCompat.getColor(getApplicationContext(), R.color.red);
                                        ViewController.snackbar(SetupMeetingActivity.this,getApplicationContext().getString(R.string.Mobile_number_not_valid),colorInt);
                                    }
                                }
                            }

                            if (!selectedEmailAddress.equalsIgnoreCase("")){
                                update_position.setEmail(selectedEmailAddress);
                            }

                        }


                        if (!res.equalsIgnoreCase(contactid)) {
                            //self meeting check
                            Invited invited = new Invited();
                            invited.setId(contactid);
                            invited.setName(contactName);
                            invited.setChecked(true);
                            invited.setEmail(selectedEmailAddress);
                            invited.setCompany("");
                            invited.setLink("");

                            if (!selectedPhoneNumber.equalsIgnoreCase("")) {
                                String[] phonenumbers = {selectedPhoneNumber};
                                for (String phone : phonenumbers) {
                                    if (isPhoneNumberValid(phone)) {
                                        if (selectedPhoneNumber.startsWith("+91") || selectedPhoneNumber.startsWith("0091") || selectedPhoneNumber.startsWith("00966") || selectedPhoneNumber.startsWith("+966")) {
                                            invited.setMobile(selectedPhoneNumber);
                                        } else {
                                            invited.setMobile("+966" + selectedPhoneNumber);
                                        }
                                    } else {
                                        selectedPhoneNumber = "";
                                        invited.setMobile(selectedPhoneNumber);
                                        int colorInt = ContextCompat.getColor(getApplicationContext(), R.color.red);
                                        ViewController.snackbar(SetupMeetingActivity.this,getApplicationContext().getString(R.string.Mobile_number_not_valid),colorInt);
                                    }
                                }
                            }

                            if (selectedEmailAddress.equalsIgnoreCase("") && selectedPhoneNumber.equalsIgnoreCase("")) {

                            } else {
                                invitedArrayList.add(invited);
                                //addguest
                                AddGuestinvitedArrayList.add(invited);
                                iEmail.add(contactphonenumber);
                                img_gmails_add.setVisibility(View.GONE);
                                relative_emails_list_open_eye.setVisibility(View.VISIBLE);
                                txt_list_count.setText(invitedArrayList.size() + "");
                            }

                        }

                    }

                } else {

                    //self meeting check
                    Invited invited = new Invited();
                    invited.setId(contactid);
                    invited.setName(contactName);
                    invited.setChecked(true);
                    invited.setEmail(selectedEmailAddress);
                    invited.setCompany("");
                    invited.setLink("");

                    if (!selectedPhoneNumber.equalsIgnoreCase("")) {
                        String[] phonenumbers = {selectedPhoneNumber};
                        for (String phone : phonenumbers) {
                            if (isPhoneNumberValid(phone)) {
                                if (selectedPhoneNumber.startsWith("+91") || selectedPhoneNumber.startsWith("0091") || selectedPhoneNumber.startsWith("00966") || selectedPhoneNumber.startsWith("+966")) {
                                    invited.setMobile(selectedPhoneNumber);
                                } else {
                                    invited.setMobile("+966" + selectedPhoneNumber);
                                }
                            } else {
                                selectedPhoneNumber = "";
                                invited.setMobile(selectedPhoneNumber);
                                int colorInt = ContextCompat.getColor(getApplicationContext(), R.color.Accept);
                                ViewController.snackbar(SetupMeetingActivity.this,getApplicationContext().getString(R.string.Mobile_number_not_valid),colorInt);
                            }
                        }
                    }


                    if (selectedEmailAddress.equalsIgnoreCase("") && selectedPhoneNumber.equalsIgnoreCase("")) {

                    }else {
                        invitedArrayList.add(invited);
                        //addGuest
                        AddGuestinvitedArrayList.add(invited);
                        iEmail.add(contactphonenumber);
                        img_gmails_add.setVisibility(View.GONE);
                        relative_emails_list_open_eye.setVisibility(View.VISIBLE);
                        txt_list_count.setText(invitedArrayList.size() + "");
                    }
                }


            }

            
            dialog.dismiss();
        });

        tvCancel.setOnClickListener(v -> dialog.dismiss());

        tvContactName.setText(contactName);

        if (listOfPhoneNumbers != null) {
            if (!listOfPhoneNumbers.isEmpty()) {
                initializePhoneNumberAdapter(rvPhoneNumbers, listOfPhoneNumbers);
            }
        }

        if (emailAddresslist != null) {
            if (!emailAddresslist.isEmpty()) {
                llEmail.setVisibility(View.VISIBLE);
                initializeEmailAddressAdapter(rvEmailAddress, emailAddresslist);
            } else {
                llEmail.setVisibility(View.GONE);
            }
        } else {
            llEmail.setVisibility(View.GONE);
        }

        dialog.show();
    }

    private void initializePhoneNumberAdapter(RecyclerView rvPhoneNumbers, LinkedList<String> listOfPhoneNumbers) {

        //valid multiple numbers
        LinkedList<String> PhoneNumbersList = new LinkedList<>();
        PhoneNumbersList.clear();
        for (int i = 0; i < listOfPhoneNumbers.size(); i++) {
            if (!listOfPhoneNumbers.get(i).equalsIgnoreCase("")) {
                String[] phonenumbers = {listOfPhoneNumbers.get(i)};
                for (String phone : phonenumbers) {
                    if (isPhoneNumberValid(phone)) {
                        PhoneNumbersList.add(phone);
                    }
                }
            }
        }

        rvPhoneNumbers.setHasFixedSize(true);
        LinearLayoutManager layoutManagerrending = new LinearLayoutManager(getApplicationContext());
        rvPhoneNumbers.setLayoutManager(layoutManagerrending);
        MultiplePhoneNumberAdapter multiplePhoneNumberAdapter = new MultiplePhoneNumberAdapter(getApplicationContext(),PhoneNumbersList, new Customthree() {
            @Override
            public void onItemClick(String selectedphoneNumber, String cNAme, String cUuid) {
                selectedPhoneNumber = selectedphoneNumber;
            }
        });
        rvPhoneNumbers.setAdapter(multiplePhoneNumberAdapter);
    }

    private void initializeEmailAddressAdapter(RecyclerView rvEmailAddress, LinkedList<String> listOfEmailAddress) {
        rvEmailAddress.setHasFixedSize(true);
        LinearLayoutManager layoutManagertrending = new LinearLayoutManager(getApplicationContext());
        rvEmailAddress.setLayoutManager(layoutManagertrending);
        MultipleEmailAddressAdapter multipleEmailAddressAdapter = new MultipleEmailAddressAdapter(getApplicationContext(), listOfEmailAddress, new Customthree() {
            @Override
            public void onItemClick(String selectedEmail, String cNAme, String CUUId) {
                selectedEmailAddress = selectedEmail;
            }
        });
        rvEmailAddress.setAdapter(multipleEmailAddressAdapter);
    }

    private void datePicker() {
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(SetupMeetingActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    SelectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
//                        date.setText(new StringBuilder().append(dayOfMonth)
//                                .append("-").append(monthOfYear + 1).append("-").append(year)
//                                .append(" "));
                    s_date = Conversions.getdatestamp(date_time);
                    date.setText(Conversions.millitodateLabel((s_date + Conversions.timezone()) * 1000));
                    selected_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    SelectedHour = 0;
                    if (comparing(selected_date)) {
                        date_status = 0;
                    } else {
                        meeting_st.setText(getString(R.string.Select_Time));
                        date_status = 1;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        datePickerDialog.show();
    }

    private void timePicker() {
        if (date_status == 1) {
            int hour = 8;
            int minute = 0;
            if (SelectedHour != 0) {
                hour = SelectedHour;
                minute = SelectedMin;
            }
            final RangeTimePickerDialog mTimePicker;
            mTimePicker = new RangeTimePickerDialog(SetupMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    String time = "";
                    SelectedHour = selectedHour;
                    SelectedMin = selectedMinute;
                    if (selectedHour > 12 && selectedHour != 24) {
                        time = String.format("%02d", selectedHour - 12) + ":" + String.format("%02d", selectedMinute) + " PM";
                    } else if (selectedHour == 12) {
                        time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + " PM";
                    } else if (selectedHour == 24) {
                        time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + " AM";
                    } else {
                        time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + " AM";
                    }
//                    meeting_st.setText(time);
                    s_time = Conversions.gettimestamp(SelectedDate, selectedHour + ":" + selectedMinute);
                    s_hour = selectedHour;
                    s_min = selectedMinute;
                    SelectedSTime = selectedHour + ":" + selectedMinute;
                    String myTime = SelectedSTime;
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    Date d = null;
                    try {
                        d = df.parse(myTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    String Stime = df.format(cal.getTime());
                    meeting_st.setText(Conversions.millitotime((Conversions.gettimestamp(SelectedDate, Stime) + Conversions.timezone()) * 1000, false));

                    cal.add(Calendar.DATE, 1);
                    cal.add(Calendar.HOUR_OF_DAY, d_hour);
                    cal.add(Calendar.MINUTE, d_min);

                    String newTime = df.format(cal.getTime());
                    e_time = Conversions.gettimestamp(SelectedDate, newTime);

                    System.out.println("s_time" + s_time);
                    System.out.println("e_time" + e_time);

                    Log.e("d_hour_",d_hour+"");
                    Log.e("d_min_",d_min+"");
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
            int Shour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int Sminute = mcurrentTime.get(Calendar.MINUTE);

            final RangeTimePickerDialog mTimePicker;
            if (minute < 15) {
                minute = 15;
                Sminute = 15;
            } else if (minute < 30) {
                minute = 30;
                Sminute = 30;
            } else if (minute < 45) {
                minute = 45;
                Sminute = 45;
            } else {
                hour = hour + 1;
                Shour = Shour + 1;
                minute = 0;
                Sminute = 0;
            }
            if (SelectedHour != 0) {
                Shour = SelectedHour;
                Sminute = SelectedMin;
            }
            mTimePicker = new RangeTimePickerDialog(SetupMeetingActivity.this, (timePicker, selectedHour, selectedMinute) -> {
                String time = "";
                SelectedHour = selectedHour;
                SelectedMin = selectedMinute;
                if (selectedHour > 12 && selectedHour != 24) {
                    time = String.format("%02d", selectedHour - 12) + ":" + String.format("%02d", selectedMinute) + " PM";
                } else if (selectedHour == 12) {
                    time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + " PM";
                } else if (selectedHour == 24) {
                    time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + " AM";
                } else {
                    time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + " AM";
                }
//                    meeting_st.setText(selectedHour + ":" + selectedMinute);
                s_time = Conversions.gettimestamp(SelectedDate, selectedHour + ":" + selectedMinute);
                SelectedSTime = selectedHour + ":" + selectedMinute;
                String myTime = SelectedSTime;
                s_hour = selectedHour;
                s_min = selectedMinute;
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                Date d = null;
                try {
                    d = df.parse(myTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                String Stime = df.format(cal.getTime());
                System.out.println("Shfgdhsj " + Conversions.millitotime((Conversions.gettimestamp(SelectedDate, Stime) + Conversions.timezone()) * 1000, false));
                meeting_st.setText(Conversions.millitotime((Conversions.gettimestamp(SelectedDate, Stime) + Conversions.timezone()) * 1000, false));
                cal.add(Calendar.HOUR_OF_DAY, d_hour);
                cal.add(Calendar.MINUTE, d_min);
                String newTime = df.format(cal.getTime());
                e_time = Conversions.gettimestamp(SelectedDate, newTime);
                Log.e("d_hour_",d_hour+"");
                Log.e("d_min_",d_min+"");
            }, Shour, Sminute, true);//true = 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.setMin(hour, minute);
            mTimePicker.show();
        }
    }

    private void getentrypoints(String locationId) {

        apiViewModel.getentrypoints(SetupMeetingActivity.this, locationId);

    }

    private void getmeetingrooms(String locationId) {

        apiViewModel.getmeetingrooms(SetupMeetingActivity.this, locationId);

        apiViewModel.getmeetingrooms_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                //card_view_progress.setVisibility(GONE);
                ViewController.DismissProgressBar();
                meetingrooms.clear();
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        ArrayList<CompanyData> FilterMeetingrooms = new ArrayList<>();
                        FilterMeetingrooms = response.getItems();
                        CompanyData companyData = new CompanyData();
                        companyData.setName(empData.getName() + " Cabin");
                        CommonObject id = new CommonObject();
                        id.set$oid(empData.getEmp_id());
                        companyData.set_id(id);
                        companyData.setActive(true);

                        meetingrooms.add(0,companyData);
                        for (int j = 0; j < FilterMeetingrooms.size(); j++) {
                            if (FilterMeetingrooms.get(j).getActive().equals(true)){
                                meetingrooms.add(FilterMeetingrooms.get(j));
                                Log.e("FilterMeetingrooms_list", j + "");
                            }
                        }

                        if (actiontype == 0 && M_SUGGETION == 0) {
                            m_room = empData.getEmp_id();
                            meetingRoomSpinner.setText(empData.getName() +" - "+ getString(R.string.Cabin));
                            samecabin = 1;
                        } else if (actiontype == 2 && M_SUGGETION == 0) {
                            String mName = meeting_details.getMeetingrooms().getName();
                            for (int j = 0; j < meetingrooms.size(); j++) {
                                if (meetingrooms.get(j).getName().equals(mName)) {
                                    MroomIndex = j;
                                    break;
                                }
                            }
                            setMInformationData(MroomIndex);
                        } else if (M_SUGGETION == 1) {
                            String mName = sharedPreferences1.getString("SetupMeetingMRoom", "");
                            for (int j = 0; j < meetingrooms.size(); j++) {
                                if (meetingrooms.get(j).getActive() != null && meetingrooms.get(j).getActive().equals("true")) {
                                    if (meetingrooms.get(j).getName().equals(mName)) {
                                        MroomIndex = j;
                                        break;
                                    }
                                }
                            }
                            setMInformationData(MroomIndex);
                        }
                        if (RMS_STATUS == 1) {
                            for (int j = 0; j < meetingrooms.size(); j++) {
                                meetingRoomSpinner.setText(meetingrooms.get(RMS_Mroom).getName());
                                m_room = meetingrooms.get(RMS_Mroom).get_id().get$oid();
                                m_room_info.setVisibility(View.VISIBLE);
                                setMInformationData(RMS_Mroom);
                            }
                        }

                        customAdapter1 = new CustomAdapter(SetupMeetingActivity.this, R.layout.row, R.id.lbl_name, meetingrooms, 0, "", true);
                        meetingRoomSpinner.setThreshold(1);//will start working from first character
                        meetingRoomSpinner.setAdapter(customAdapter1);//setting the adapter data into the AutoCompleteTextView
                        meetingRoomSpinner.setEnabled(true);
                        getentrypoints(locationId);
                    }

                }else {
                    Conversions.errroScreen(SetupMeetingActivity.this, "getmeetingrooms");
                }
            }
        });

    }

    private void setMInformationData(int mr_id) {
        aString = new ArrayList<>();
        aString.add("Capacity (" + meetingrooms.get(mr_id).getCapacity() + ")");
        ArrayList<Boolean> amenitiesB = new ArrayList<>();
        amenitiesB = meetingrooms.get(mr_id).getAmenities();

        if (amenitiesB != null && amenitiesB.size() != 0) {
            for (int i = 0; i < amenitiesB.size(); i++) {
                if (amenitiesB.get(i) != null && amenitiesB.get(i)) {
                    aString.add(amenities.get(i).getName());
                }
            }
        }
        m_room_info.setVisibility(View.VISIBLE);
    }

    private JsonObject createjson() {
        Log.e(TAG, "createjson:createjson" + "success");
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray invites = new JSONArray();
        JSONArray agenda = new JSONArray();
        JSONArray pdfs = new JSONArray();
        JSONArray weeks_dates = new JSONArray();
        JSONArray days_s = new JSONArray();
        JSONArray starts_dates = new JSONArray();
        JSONArray ends_dates = new JSONArray();


        int hh_s = (d_hour * 60) * 60;
        int mm_s = d_min * 60;
        int duration_min = hh_s + mm_s;

        //reccuring
        //date stamps array
        JSONArray conflict_indexs = new JSONArray();

        //Weeks
        try {
            weeks_dates = new JSONArray();
            for (int i = 0; i < weekday_s.size(); i++) {
                weeks_dates.put(weekday_s.get(i).isSelected());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //days
        try {
            days_s = new JSONArray();
            for (int i = 0; i < dates_s.size(); i++) {
                days_s.put(dates_s.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //start days
        try {
            starts_dates = new JSONArray();
            ends_dates = new JSONArray();
            for (int i = 0; i < dates_s.size(); i++) {

                int hh_string = Integer.parseInt(Conversions.millisTotime(((s_time + Conversions.timezone()) * 1000), true));
                int mm_string = Integer.parseInt(Conversions.millisTotime(((s_time + Conversions.timezone()) * 1000), false));
                int hh_string_s = (hh_string * 60) * 60;
                int mm_string_s = mm_string * 60;
                int hh_mm_min = hh_string_s + mm_string_s;

                long ss = dates_s.get(i) + hh_mm_min;
                long end = ss + duration_min - 1;

                starts_dates.put(ss);
                ends_dates.put(end);
                Log.e(TAG, "createjson:dates_s" + dates_s);
                Log.e(TAG, "createjson:starts_dates" + starts_dates);
                Log.e(TAG, "createjson:ends_dates" + ends_dates);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (pSlots == true) {
//            invites = new JSONArray();
//            invites = AllotParkingFragment.allotinvites;
//            Log.e(TAG, "createjson:invites+allot" + invites);
//        } else {
//            try {
//                invites = new JSONArray();
//                for (int i = 0; i < invitedArrayList.size(); i++) {
//                    invites.put(invitedArrayList.get(i).getInvites());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Log.e(TAG, "createjson:invites+invites" + invites);
//        }


        try {
            invites = new JSONArray();
            for (int i = 0; i < invitedArrayList.size(); i++) {
                invites.put(invitedArrayList.get(i).getInvites());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (agendaArrayList != null && agendaArrayList.size() != 0) {
            try {
                agenda = new JSONArray();
                for (int i = 0; i < agendaArrayList.size(); i++) {
                    agenda.put(agendaArrayList.get(i).getAgenda());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pdfsArrayList != null && pdfsArrayList.size() != 0) {
            try {
                pdfs = new JSONArray();
                for (int i = 0; i < pdfsArrayList.size(); i++) {
                    pdfs.put(pdfsArrayList.get(i).getPdfs());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.e("pSlots",pSlots+"");
        Log.e("daysList",days_s+"");
        Log.e("weeksList",weeks_dates+"");

        try {
            jsonObj_.put("location", location);
            jsonObj_.put("mtype", m_type);
            jsonObj_.put("mtype_val", m_value.getText().toString());
            jsonObj_.put("pslots", pSlots);
            jsonObj_.put("meetingroom", m_room);
            jsonObj_.put("roleid", empData.getRoleid());
            jsonObj_.put("rolename", empData.getRolename());
            jsonObj_.put("samecabin", samecabin);
            jsonObj_.put("start", s_time);
            jsonObj_.put("end", e_time - 1);
            jsonObj_.put("subject", subject.getText().toString());
            jsonObj_.put("approver", false);
            if (roledetails.getApprover() != null && roledetails.getApprover().equalsIgnoreCase("true")) {
                jsonObj_.put("approver", true);
            }
            if (pdfsArrayList.size() > 0) {
                jsonObj_.put("pdfStatus", true);
            }
            jsonObj_.put("v_id", v_id);
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("date", s_date);
            jsonObj_.put("desc", meeting_description.getText().toString());

            if (roledetails.getBehalfof().equalsIgnoreCase("true")){
                if (SelfMeetingSetupCheckBox.isChecked()){
                    jsonObj_.put("emp_id", empData.getEmp_id());
                    jsonObj_.put("coordinator", empData.getEmp_id());
                }else {
                    jsonObj_.put("emp_id", AssignID);
                    jsonObj_.put("coordinator", empData.getEmp_id());
                }
            }else {
                jsonObj_.put("emp_id", empData.getEmp_id());
                jsonObj_.put("coordinator", empData.getEmp_id());
            }

            jsonObj_.put("entrypoint", e_gate);
            jsonObj_.put("formtype", "insert");
            jsonObj_.put("hierarchy_id", empData.getHierarchy_id());
            jsonObj_.put("hierarchy_indexid", empData.getHierarchy_indexid());
            jsonObj_.put("agenda", agenda);
            jsonObj_.put("invites", invites);
            jsonObj_.put("pdfs", pdfs);
            jsonObj_.put("category", category_item);


            if (recurrence_status.equalsIgnoreCase("true")) {
                jsonObj_.put("recurrence", true);
                jsonObj_.put("dates", days_s);
                jsonObj_.put("re_type", recurrence_type);
                jsonObj_.put("dtstart", dtstart);
                jsonObj_.put("dtend", dtend);
                jsonObj_.put("starts", starts_dates);
                jsonObj_.put("ends", ends_dates);
                jsonObj_.put("weekdays", weeks_dates);
                jsonObj_.put("conflict_indexs", conflict_indexs);
            }

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("createjsongsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;
    }

    private JsonObject addCovisitorjson() {
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray invites = new JSONArray();
        try {
            for (int i = 0; i < AddGuestinvitedArrayList.size(); i++) {
                invites.put(AddGuestinvitedArrayList.get(i).getInvites());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj_.put("id", sharedPreferences1.getString("m_id", null));
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("subject", subject.getText().toString());
            jsonObj_.put("desc", meeting_description.getText().toString());
            jsonObj_.put("formtype", "insert");
            jsonObj_.put("covisitors", invites);
            jsonObj_.put("emp_id", empData.getEmp_id());
            jsonObj_.put("location", location);
            jsonObj_.put("start", s_time);
            jsonObj_.put("end", e_time - 1);
            jsonObj_.put("agenda", new JSONArray());
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("gsonObject::addCovisitorjson" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;
    }

    private JsonObject reschedulejson() {

        if(m_type.equalsIgnoreCase("1")){

        }else {
            pSlots = false;
        }

        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray agenda = new JSONArray();
        JSONArray invites = new JSONArray();
        JSONArray pdfs = new JSONArray();
        JSONArray weeks_dates = new JSONArray();
        JSONArray days_s = new JSONArray();
        JSONArray starts_dates = new JSONArray();
        JSONArray ends_dates = new JSONArray();

        int hh_s = (d_hour * 60) * 60;
        int mm_s = d_min * 60;
        int duration_min = hh_s + mm_s;


        //reccuring
        //date stamps array
        JSONArray conflict_indexs = new JSONArray();

        //Weeks
        try {
            weeks_dates = new JSONArray();
            for (int i = 0; i < weekday_s.size(); i++) {
                weeks_dates.put(weekday_s.get(i).isSelected());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //days
        try {
            days_s = new JSONArray();
            for (int i = 0; i < dates_s.size(); i++) {
                days_s.put(dates_s.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //start days
        try {
            starts_dates = new JSONArray();
            ends_dates = new JSONArray();
            for (int i = 0; i < dates_s.size(); i++) {

                int hh_string = Integer.parseInt(Conversions.millisTotime(((s_time + Conversions.timezone()) * 1000), true));
                int mm_string = Integer.parseInt(Conversions.millisTotime(((s_time + Conversions.timezone()) * 1000), false));
                int hh_string_s = (hh_string * 60) * 60;
                int mm_string_s = mm_string * 60;
                int hh_mm_min = hh_string_s + mm_string_s;

                long ss = dates_s.get(i) + hh_mm_min;
                long end = ss + duration_min - 1;

                starts_dates.put(ss);
                ends_dates.put(end);
                Log.e(TAG, "createjson:dates_s" + dates_s);
                Log.e(TAG, "createjson:starts_dates" + starts_dates);
                Log.e(TAG, "createjson:ends_dates" + ends_dates);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            invites = new JSONArray();
            for (int i = 0; i < invitedArrayList.size(); i++) {
                invites.put(invitedArrayList.get(i).getInvites());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reschedule agenda
        if (agendaArrayList != null && agendaArrayList.size() != 0) {
            try {
                agenda = new JSONArray();

                for (int i = 0; i < agendaArrayList.size(); i++) {
                    boolean foundInAddAgendaList = false;
                    if (addagendaList != null && !addagendaList.isEmpty()) {
                        for (int j = 0; j < addagendaList.size(); j++) {
                            if (agendaArrayList.get(i).get_id().get$oid().equalsIgnoreCase(addagendaList.get(j).get_id().get$oid())) {
                                agendaArrayList.get(i).setStatus("0.0");
                                foundInAddAgendaList = true;
                                break;
                            }
                        }
                    }

                    // If not found in addagendaList, mark it as "1.0"
                    if (!foundInAddAgendaList) {
                        agendaArrayList.get(i).setStatus("1.0");
                    }

                    // Add the agenda item to the JSONArray
                    agenda.put(agendaArrayList.get(i).getAgenda());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (pdfsArrayList != null && !pdfsArrayList.isEmpty()) {
            try {
                pdfs = new JSONArray();
                for (int i = 0; i < pdfsArrayList.size(); i++) {
                    pdfs.put(pdfsArrayList.get(i).getPdfs());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            jsonObj_.put("formtype", "reschedule");
            jsonObj_.put("location", location);
            jsonObj_.put("mtype", m_type);
            jsonObj_.put("mtype_val", m_value.getText().toString());
            jsonObj_.put("pslots", pSlots);
            jsonObj_.put("meetingroom", m_room);
            jsonObj_.put("roleid", empData.getRoleid());
            jsonObj_.put("rolename", empData.getRolename());
            jsonObj_.put("samecabin", samecabin);
            jsonObj_.put("start", s_time);
            jsonObj_.put("end", e_time - 1);
            jsonObj_.put("subject", subject.getText().toString());
            jsonObj_.put("entrypoint", e_gate);
            jsonObj_.put("hierarchy_id", empData.getHierarchy_id());
            jsonObj_.put("hierarchy_indexid", empData.getHierarchy_indexid());
            jsonObj_.put("agenda", agenda);
            jsonObj_.put("invites", invites);
            jsonObj_.put("pdfs", pdfs);
            jsonObj_.put("category", category_item);
            jsonObj_.put("v_id", v_id);
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("date", s_date);
            jsonObj_.put("desc", meeting_description.getText().toString());
            jsonObj_.put("id", sharedPreferences1.getString("m_id", null));
            jsonObj_.put("status", 2);

            if (roledetails.getApprover() != null && roledetails.getApprover().equalsIgnoreCase("true")) {
                jsonObj_.put("approver", true);
            }else {
                jsonObj_.put("approver", false);
            }
            if (!pdfsArrayList.isEmpty()) {
                jsonObj_.put("pdfStatus", true);
            }
            if (roledetails.getBehalfof().equalsIgnoreCase("true")){
                if (SelfMeetingSetupCheckBox.isChecked()){
                    jsonObj_.put("emp_id", empData.getEmp_id());
                    jsonObj_.put("coordinator", empData.getEmp_id());
                }else {
                    jsonObj_.put("emp_id", AssignID);
                    jsonObj_.put("coordinator", empData.getEmp_id());
                }
            }else {
                jsonObj_.put("emp_id", empData.getEmp_id());
                jsonObj_.put("coordinator", empData.getEmp_id());
            }
            if (recurrence_status.equalsIgnoreCase("true")) {
                jsonObj_.put("recurrence", true);
                jsonObj_.put("dates", days_s);
                jsonObj_.put("re_type", recurrence_type);
                jsonObj_.put("dtstart", dtstart);
                jsonObj_.put("dtend", dtend);
                jsonObj_.put("starts", starts_dates);
                jsonObj_.put("ends", ends_dates);
                jsonObj_.put("weekdays", weeks_dates);
                jsonObj_.put("conflict_indexs", conflict_indexs);
            }

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("gsonObject::" + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;
    }

    private List<People> MeetingEndTime() {
        List<People> list = new ArrayList<People>();
        list.add(new People("30 mins"));
        list.add(new People("1 hour"));
        list.add(new People("2 hours"));
        list.add(new People("3 hours"));
        list.add(new People("4 hours"));
        list.add(new People("5 hours"));
        list.add(new People("6 hours"));
        list.add(new People("7 hours"));
        list.add(new People("8 hours"));
        return list;
    }

    private List<People> AlertArray() {
        List<People> list = new ArrayList<People>();
        list.add(new People("30 mins"));
        list.add(new People("1 hour"));
        list.add(new People("2 hours"));
        list.add(new People("3 hours"));
        return list;
    }

    private void gethostslots() {
        apiViewModel.gethostslots(SetupMeetingActivity.this, "end", empData.getEmp_id(), empData.getEmail(), s_time, e_time - 1);
    }

    private void getrmslots(String rm_id) {
        apiViewModel.getrmslots(SetupMeetingActivity.this, "end", s_time, e_time - 1, rm_id);
    }

    private void getmeetingdetails() {
        apiViewModel.getmeetingdetails(SetupMeetingActivity.this, sharedPreferences1.getString("m_id", null));
    }

    private void confirmationPopup(JsonObject jsonObject, int type) {
        final Dialog dialog = new Dialog(SetupMeetingActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.confirmationpopup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final TextView txt_reccuring_type, txt_weeks, txt_occurences;
        final TextView submit;
        final TextView datetime, visitor_list;
        final TextView cancel;
        txt_reccuring_type = dialog.findViewById(R.id.txt_reccuring_type);
        txt_weeks = dialog.findViewById(R.id.txt_weeks);
        txt_occurences = dialog.findViewById(R.id.txt_occurences);
        datetime = dialog.findViewById(R.id.time_date);
        visitor_list = dialog.findViewById(R.id.visitor_list);
        submit = dialog.findViewById(R.id.submit);
        cancel = dialog.findViewById(R.id.cancel);
        System.out.println("s_time " + s_time);
        System.out.println("s_time " + e_time);
        String value = getSdateLabel((s_time + Conversions.timezone()) * 1000) + " \n" + Conversions.millitotime(((s_time + Conversions.timezone()) * 1000), false) + "  to " + Conversions.millitotime(((e_time + Conversions.timezone()) * 1000), false);

//        date.setText(getSdateLabel((meeting_details.getStart() + Conversions.timezone()) * 1000));

//        if(meeting_details.getInvites() != null && meeting_details.getInvites().size() != 0){
//            for (int i=0;i<meeting_details.getInvites().size();i++) {
//                if (meeting_details.getInvites().get(i).getName().equals("x") || meeting_details.getInvites().get(i).getName().equals("")) {
//                    visitor_list.setText(visitor_list.getText() + meeting_details.getInvites().get(i).getEmail() + "\n");
//                } else {
//                    visitor_list.setText(visitor_list.getText() + meeting_details.getInvites().get(i).getName() + "\n");
//                }
//            }
//        }

        if (invitedArrayList != null && !invitedArrayList.isEmpty()) {
            for (int i = 0; i < invitedArrayList.size(); i++) {
                if (invitedArrayList.get(i).getName().equals("x") || invitedArrayList.get(i).getName().equals("")) {
                    if (invitedArrayList.get(i).getMobile().equals("")) {
                        visitor_list.setText(visitor_list.getText() + invitedArrayList.get(i).getEmail() + "\n");
                    } else {
                        visitor_list.setText(visitor_list.getText() + invitedArrayList.get(i).getMobile() + "\n");
                    }
                } else {
                    visitor_list.setText(visitor_list.getText() + invitedArrayList.get(i).getName() + "\n");
                }
            }
        }


        if (recurrence_status.equalsIgnoreCase("true")) {

            String source_date = "From " + "<b>" + Conversions.dateToFormat(From_date) + "</b>" + " to " + "<b>" + Conversions.dateToFormat(To_date) + "</b>" + "<br/>" + Conversions.millitotime(((s_time + Conversions.timezone()) * 1000), false) + "  to " + Conversions.millitotime(((e_time + Conversions.timezone()) * 1000), false) + "";
            txt_reccuring_type.setVisibility(View.VISIBLE);
            txt_occurences.setVisibility(View.VISIBLE);

            String recurrenceText;
            if (recurrence_type.equalsIgnoreCase("1")) {
                recurrenceText = "Daily";
                txt_occurences.setText(getString(R.string.Occurrences) + number_of_occurrences_count+" "+getString(R.string.days));
                txt_weeks.setVisibility(View.VISIBLE);
            } else if (recurrence_type.equalsIgnoreCase("2")) {
                recurrenceText = "Weekly";
                txt_occurences.setText(getString(R.string.Occurrences) + number_of_occurrences_count+" "+getString(R.string.Weeks));
                txt_weeks.setVisibility(View.VISIBLE);
            } else if (recurrence_type.equalsIgnoreCase("3")) {
                recurrenceText = "Monthly";
                txt_occurences.setText(getString(R.string.Occurrences) + number_of_occurrences_count+" "+getString(R.string.Months));
                txt_weeks.setVisibility(GONE);
            } else {
                recurrenceText = "";
            }
            txt_reccuring_type.setText(recurrenceText);
            datetime.setText(Html.fromHtml(source_date));

            String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < weekday_s.size(); i++) {
                boolean isSelected = weekday_s.get(i).isSelected();
                if (isSelected) {
                    sb.append(dayNames[i]).append(" ");
                }
            }
            txt_weeks.setText(sb.toString().trim());

        } else {
            datetime.setText(value);
        }

        submit.setOnClickListener(v -> {
            //card_view_progress.setVisibility(View.VISIBLE);
            ViewController.ShowProgressBar(SetupMeetingActivity.this);
            if (type == 1) {
                actionmeetings(jsonObject);
            } else if (type == 2) {
                updatemeetings(jsonObject);
            } else if (type == 3) {
                addcovisitor(jsonObject);
            }
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    private void AmenitiesPopup() {
        final Dialog dialog = new Dialog(SetupMeetingActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.amenities_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final RecyclerView AmenitiesrecyclerView;

        final TextView done;
        AmenitiesrecyclerView = dialog.findViewById(R.id.recycler);
        done = dialog.findViewById(R.id.done);
        AmenitiesrecyclerView.setLayoutManager(new GridLayoutManager(SetupMeetingActivity.this, 2));
        AmenitiesAdapter amenitiesAdapter = new AmenitiesAdapter(SetupMeetingActivity.this, aString);
        AmenitiesrecyclerView.setAdapter(amenitiesAdapter);
        done.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void actionmeetings(JsonObject jsonObject) {
        //outside meeting
        if (RMS_STATUS == 3) {

            apiViewModel.actionmeetings_total_count(SetupMeetingActivity.this, jsonObject);

        } else {

            apiViewModel.actionmeetings(getApplicationContext(), jsonObject);

        }
    }

    private void acceptappointment(String Total_counts) {

//        //update appointment
//        JsonObject gsonObject = new JsonObject();
//        JSONObject jsonObj_ = new JSONObject();
//        try {
//            jsonObj_.put("formtype", "accept");
//            jsonObj_.put("id",appointments_model.items.get_id().get$oid());
//            jsonObj_.put("emp_id", appointments_model.items.getEmp_id());
//            jsonObj_.put("mid",Total_counts);
//            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
//            JsonParser jsonParser = new JsonParser();
//            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        apiViewModel.updateappointment(getApplicationContext(),gsonObject);

        Intent intent = new Intent(SetupMeetingActivity.this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

    }

    private void addcovisitor(JsonObject jsonObject) {

        apiViewModel.addcovisitor(getApplicationContext(), jsonObject);

    }

    private void getInviteData(String S_value, String type) {

        apiViewModel.getInviteData(SetupMeetingActivity.this, "type", "usertype", S_value);

        apiViewModel.getInviteData_response().observe(SetupMeetingActivity.this, response -> {
            if (response != null) {

                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)) {

                } else if (statuscode.equals(successcode)) {
                    invitesData.addAll(response.getItems());
                    if (response.getItems().isEmpty()) {
                        Log.e(TAG, "onResponse:te: " + S_value);
                    } else {
                        Log.e(TAG, "onResponse:te: " + "2");
                        if (type.equalsIgnoreCase("list_type")) {
                            Log.e(TAG, "onResponse:getName: " + invitesData.get(0).getName());
                        } else {
                            Log.e(TAG, "onResponse:sa" + "2");
                        }
                    }
                }

                if (invitesData != null) {
                    invitesadapter = new CustomAdapter(SetupMeetingActivity.this, R.layout.row, R.id.lbl_name, invitesData, 1, sharedPreferences1.getString("company_id", null));
                    name.setThreshold(2);
                    name.setAdapter(invitesadapter);
                    if (GUEST_STATUS) {
                        name.showDropDown();
                    }
                }
            }else {
                Conversions.errroScreen(SetupMeetingActivity.this, "getInviteData");
            }
        });

    }

    public static String getCurrentTimeStampLabel() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("E MMM dd", locale);
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String getSdateLabel(Long s_date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("E MMM dd", locale);//dd/MM/yyyy
        Date now = new Date();
        now.setTime(s_date);
        String strDate = sdfDate.format(now);
        return strDate;
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
        if (is12hour) {
            sdfDate = new SimpleDateFormat("hh:mm a", locale);//dd/MM/yyyy
        }
        Date now = new Date();
        now = mcurrentTime.getTime();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    private GlideChip getChip(ChipGroup chipGroup, Invited invited, Integer type) {
        GlideChip chip = new GlideChip(SetupMeetingActivity.this);
        ChipDrawable chipDrawable = ChipDrawable.createFromResource(SetupMeetingActivity.this, R.xml.chip);
        chip.setChipDrawable(chipDrawable);
        if (type == 2) {
            chip.setCloseIconVisible(false);
        }
        chip.setCheckable(false);
        chip.setText(invited.getName());

        if (invited.getPic() != null && !invited.getPic().isEmpty()) {
            if (type == 2) {
                chip.setIconUrl1(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + invited.getPic().get(invited.getPic().size() - 1), getResources().getDrawable(R.drawable.ic_user));
            } else {
                chip.setIconUrl(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + invited.getPic().get(invited.getPic().size() - 1), getResources().getDrawable(R.drawable.ic_user));
            }
        }

        if (invited.getName() == null || invited.getName().equals("x") || invited.getName().equals("")) {
            chip.setText(invited.getEmail());
        }
        chip.setOnCloseIconClickListener(v -> {
            builder.setMessage("Are you sure you want to delete this Invitee ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        System.out.println("chip_id" + invited.getName());
                        System.out.println("chip_id" + invited.getEmail());
                        update = 0;
                        invitedArrayList.remove(invited);
                        iEmail.remove(invited.getEmail());
                        chipGroup.removeView(chip);
                        dialog.cancel();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("");
            alert.show();
        });
        return chip;
    }

    private GlideChip getChip1(ChipGroup chipGroup) {
        GlideChip chip = new GlideChip(SetupMeetingActivity.this);
        ChipDrawable chipDrawable = ChipDrawable.createFromResource(SetupMeetingActivity.this, R.xml.chip);
        chip.setChipDrawable(chipDrawable);

        chip.setCheckable(false);
        chip.setText("");
        chip.setOnCloseIconClickListener(v -> {
        });
        return chip;
    }

    private void updatemeetings(JsonObject jsonObject) {

        apiViewModel.updatemeetings(getApplicationContext(), jsonObject);

    }

    public Boolean comparing(String date) {
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = null;
        try {
            d1 = sdformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d2 = null;
        try {
            d2 = sdformat.parse(current_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (d1.compareTo(d2) == 0) {
            return true;
        } else {
            return false;
        }

    }

    public void getPhoneNumbersByName(String name_s) {
        invitesData = new ArrayList<>();
        invitesData.clear();
        String SearchedString = "%" + name_s + "%";
        ContentResolver cr = SetupMeetingActivity.this.getContentResolver();
        List<String> phones = new ArrayList<>();
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DATA};
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ? ", new String[]{SearchedString}, null);
        //Cursor cur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null);

        System.out.println("asfjkhjksfjagsfj" + cur.getCount());
        while (cur != null && cur.moveToNext()) {
            //to get the contact names
            String name1 = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String email = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
            CompanyData contact = new CompanyData();
            contact.setName(name1);
            contact.setMobile(phone);
            contact.setEmail(email);
            invitesData.add(contact);

            Log.e(TAG, "getPhoneNumbersByName: " + name1 + "123");

            invitesadapter = new CustomAdapter(SetupMeetingActivity.this, R.layout.row, R.id.lbl_name, invitesData, 1, sharedPreferences1.getString("company_id", null));
            name.setThreshold(0);
            name.setAdapter(invitesadapter);
            if (GUEST_STATUS) {
                name.showDropDown();
            }

            Log.e(TAG, "getPhoneNumbersByName:phone " + phone);
        }

        //getInviteData(name_s,"");
        if (cur != null) {
            cur.close();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            editor1 = sharedPreferences1.edit();
            editor1.remove("m_id");
            editor1.remove("m_action");
            editor1.apply();
            finish();
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void pickPdf() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, 9811);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 9811:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    String path = uri.getPath();
                    String displayName = null;

                    Uri image = data.getData();

                    Context context = getApplicationContext();

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
                                pdfName.setText(displayName);
                                pdfClear.setVisibility(View.VISIBLE);
                                Log.e("Filename", displayName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            // Handle the selected contact
            Uri contactUri = data.getData();
            getContactDetails(contactUri);

        }
    }

    public class Adapter1 extends RecyclerView.Adapter<Adapter1.MyviewHolder> {
        Context context;
        ArrayList<Agenda> agendaList;

        public class MyviewHolder extends RecyclerView.ViewHolder {
            LinearLayout linear;
            TextView listItemText;
            ImageButton remove;

            public MyviewHolder(View view) {
                super(view);
                linear = (LinearLayout) view.findViewById(R.id.linear);
                listItemText = (TextView) view.findViewById(R.id.lbl_name);
                remove = (ImageButton) view.findViewById(R.id.remove);
            }
        }

        public Adapter1(Context context, ArrayList<Agenda> agendaArrayList) {
            this.context = context;
            this.agendaList = agendaArrayList;
        }

        @NonNull
        @Override
        public Adapter1.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.row_list_view, parent, false);
            return new Adapter1.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Adapter1.MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.listItemText.setText(agendaList.get(position).getDesc());

            holder.remove.setOnClickListener(v -> {
                // Update the status to "1.0" when an item is removed
                agendaList.get(position).setStatus("1.0");

                // Find and remove the item from agendaArrayList as well
                for (int i = 0; i < agendaArrayList.size(); i++) {
                    if (agendaArrayList.get(i).get_id().get$oid().equals(agendaList.get(position).get_id().get$oid())) {
                        agendaArrayList.get(i).setStatus("1.0");
                        break;
                    }
                }

                // Remove the item from the current list and update RecyclerView
                agendaList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, agendaList.size());
            });

        }

        @Override
        public int getItemCount() {
            return agendaList.size();
        }
    }

    public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.MyviewHolder> {
        Context context;
        ArrayList<String> Amenities;

        public class MyviewHolder extends RecyclerView.ViewHolder {
            TextView amenities;

            public MyviewHolder(View view) {
                super(view);
                amenities = view.findViewById(R.id.amenities);
            }
        }

        public AmenitiesAdapter(Context mContext, ArrayList<String> Amenities) {
            this.context = mContext;
            this.Amenities = Amenities;
        }

        @Override
        public AmenitiesAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.amenities, parent, false);
            return new AmenitiesAdapter.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AmenitiesAdapter.MyviewHolder holder, final int position) {
            holder.amenities.setText(Amenities.get(position));
        }

        @Override
        public int getItemCount() {
            return Amenities.size();
        }
    }


    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else {
                if (Build.VERSION.SDK_INT > 20) {
                    //getExternalMediaDirs() added in API 21
                    File extenal[] = context.getExternalMediaDirs();
                    for (File f : extenal) {
                        filePath = f.getAbsolutePath();
                        if (filePath.contains(type)) {
                            int endIndex = filePath.indexOf("Android");
                            filePath = filePath.substring(0, endIndex) + split[1];
                        }
                    }
                } else {
                    filePath = "/storage/" + type + "/" + split[1];
                }
                return filePath;
            }
        } else if (isDownloadsDocument(uri)) {
            final String id = DocumentsContract.getDocumentId(uri);
            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {column};
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    String result = cursor.getString(index);
                    cursor.close();
                    return result;
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if (DocumentsContract.isDocumentUri(context, uri)) {
            // MediaProvider
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String[] ids = wholeID.split(":");
            String id;
            String type;
            if (ids.length > 1) {
                id = ids[1];
                type = ids[0];
            } else {
                id = ids[0];
                type = ids[0];
            }
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{id};
            final String column = "_data";
            final String[] projection = {column};
            Cursor cursor = context.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex(column);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
            return filePath;
        } else {
            String[] proj = {MediaStore.Audio.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                if (cursor.moveToFirst())
                    filePath = cursor.getString(column_index);
                cursor.close();
            }
            return filePath;
        }
        return null;
    }


    private void uploadFile(Uri fileUri) throws FileNotFoundException, RemoteException {
        String PDFPATH = FileUtilsClass.getRealPath(SetupMeetingActivity.this, fileUri);
        if (PDFPATH == null || PDFPATH.equals("")) {
            PDFPATH = FileUtilsClass.getFilePathFromURI(SetupMeetingActivity.this, fileUri);
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
        String timestamp = Calendar.getInstance().getTimeInMillis() / 1000 + ".pdf";
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
        Pdfs pdfs = new Pdfs();
        pdfs.setName(file.getName());
        pdfs.setStatus(true);
        pdfs.setValue(file.getName());
        pdfsArrayList.add(pdfs);

        RequestBody fname = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody c_id = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences1.getString("company_id", null));

        apiViewModel.pdfupload(SetupMeetingActivity.this, fileToUpload, fname, c_id);

    }


    private void getcategories() {

        apiViewModel.getcategories(SetupMeetingActivity.this, "");

    }


    private void info_categories() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(SetupMeetingActivity.this);
        View mview = LayoutInflater.from(SetupMeetingActivity.this).inflate(R.layout.popup_category_info, null);
        TextView title_note = mview.findViewById(R.id.title_note);
        TextView bt_ok = mview.findViewById(R.id.bt_ok);

        mbuilder.setView(mview);
        alertDialog_category = mbuilder.create();
        alertDialog_category.show();
        alertDialog_category.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog_category.setCancelable(false);
        alertDialog_category.setCanceledOnTouchOutside(false);

        title_note.setText("If Confidential category is selected, the Meeting Room Display will not show participants' names. It will display the meeting as Confidential.");

        bt_ok.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            if (alertDialog_category != null) {
                alertDialog_category.dismiss();
            }
        });
    }


    private void popup_add_contact() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(SetupMeetingActivity.this);
        View mview = LayoutInflater.from(SetupMeetingActivity.this).inflate(R.layout.popup_add_contact, null);
        ImageView img_back = mview.findViewById(R.id.img_back);
        TextView txt_create_new_contact = mview.findViewById(R.id.txt_create_new_contact);
        TextView txt_add_existing_contact = mview.findViewById(R.id.txt_add_existing_contact);

        mbuilder.setView(mview);
        alertDialog = mbuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        img_back.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        });

        txt_create_new_contact.setOnClickListener(v -> {
            add_contact_list_status = "clicked";
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            if (ViewController.isEmailValid(name.getText().toString())) {
                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                contactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, name.getText().toString())
                        .putExtra(ContactsContract.Intents.Insert.PHONE, "");
                startActivityForResult(contactIntent, 1);
            } else {
                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, name.getText().toString())
                        .putExtra(ContactsContract.Intents.Insert.EMAIL, "");
                startActivityForResult(contactIntent, 1);
            }
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        });

        txt_add_existing_contact.setOnClickListener(v -> {
            add_contact_list_status = "clicked";
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            if (ViewController.isEmailValid(name.getText().toString())) {
                Intent intent = new Intent(getApplicationContext(), AddtoexistingcontactsActivity.class);
                intent.putExtra("type", "email");
                intent.putExtra("number", name.getText().toString());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), AddtoexistingcontactsActivity.class);
                intent.putExtra("type", "mobile");
                intent.putExtra("number", name.getText().toString());
                startActivity(intent);
            }
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        });

    }

    private void warning_popup() {
        final Dialog dialog = new Dialog(SetupMeetingActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.warnging_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txt_yes = dialog.findViewById(R.id.txt_yes);

        txt_yes.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });
        dialog.show();
    }


    private void back_popup() {
        final Dialog dialog = new Dialog(SetupMeetingActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_back_press);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.title);
        TextView title_note = dialog.findViewById(R.id.title_note);
        TextView txt_yes = dialog.findViewById(R.id.txt_yes);
        TextView txt_no = dialog.findViewById(R.id.txt_no);

        title.setText(getResources().getString(R.string.New_Meeting));
        title_note.setText(getResources().getString(R.string.Do_you_want_to_exit_the_meeting_setup));
        txt_yes.setText(getResources().getString(R.string.Yes));
        txt_no.setText(getResources().getString(R.string.No));

        txt_yes.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            editor1 = sharedPreferences1.edit();
            editor1.remove("m_id");
            editor1.remove("m_action");
            editor1.apply();
            finish();
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

            dialog.dismiss();
        });
        txt_no.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, continue accessing the contacts list
                } else {
                    // Permission denied, disable the functionality that requires the permission
                }
                return;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        back_popup();
    }

}