package com.provizit.Activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.provizit.FragmentDailouge.SetUpMeetingContactListFragment.isPhoneNumberValid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.AdapterAndModel.HostSlots.HostSlotsData;
import com.provizit.AdapterAndModel.HostSlots.HostSlotsModel;
import com.provizit.AdapterAndModel.MeetingDetailsAgendas.MeetingDetailsAgendaAdapter;
import com.provizit.AdapterAndModel.MultipleEmailAddressAdapter;
import com.provizit.AdapterAndModel.MultiplePhoneNumberAdapter;
import com.provizit.Config.Customthree;
import com.provizit.Config.Preferences;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.FragmentDailouge.ReccurenceDatesListFragment;
import com.provizit.FragmentDailouge.ViewMoreInvitesFragment;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MeetingDescriptionNewActivity extends AppCompatActivity {

    private static final String TAG = "MeetingDescriptionActiv";

    CardView card_view_progress;

    BroadcastReceiver broadcastReceiver;
    public static final int RESULT_CODE = 12;
    ImageView img_back;
    TextView txtTitle, txt_date_time;
    TextView name, company, subject, txt_category, location, time, o_time, r_time, invitee_count, description, cabin, date;
    LinearLayout main_time_date, linear_original_suggested_time;
    public static CompanyData meetings;

    ArrayList<LocationData> meetinglocations;
    LinearLayout cancellation, reschedule, apporve, decline, accept, reject, assign, add_guest, resend;
    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;

    int indexid;
    LinearLayout visitorcountlinear, invitee_details;
    CircleImageView img;
    SharedPreferences.Editor editor1;
    AutoCompleteTextView department_spinner, emp_spinner;
    String Assignemail;
    ArrayList<CompanyData> departments;
    ArrayList<CompanyData> employees, invitesData;
    CustomAdapter departmentAdapter, employeeAdapter;
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
    int date_status = 0, d_hour, d_min;
    long s_time, e_time, s_date;
    String meeting, assignedId;
    int R_status;
    EditText datep, meeting_st, meeting_et, reason;
    LinearLayout action_btn;
    RecyclerView recyclerViewinvites, agendaRecyclerView;
    TextView txt_view_more;
    InvitesAdapter1 invitesAdapter;
    MeetingDetailsAgendaAdapter agendaAdapter;
    LinearLayout leanear_stamp;
    CardView cardview_note;
    TextView v_name, E_desi, email, mobile, host,coordinatorName, invitee_text, txt_stamp, assignedto, title1, pdfName;
    LinearLayout pdf;
    FloatingActionButton fab, uploadPdf;
    TextView agendaText;
    ArrayList<Pdfs> pdfsArrayList;
    private Intent browserIntent;
    private boolean isHostPdfUpload = false;

    ApiViewModel apiViewModel;

    //recurrenc
    ImageView img_reccurence_list;

    //contact list selection
    private static final int PICK_CONTACT_REQUEST = 1;
    private String selectedPhoneNumber = "";
    private String selectedEmailAddress = "";
    ChipGroup chipgroup;

    String meetingsId = "";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(MeetingDescriptionNewActivity.this);
        setContentView(R.layout.activity_meeting_description_new);
        meetingsId = getIntent().getStringExtra("m_id");

        card_view_progress = findViewById(R.id.card_view_progress);

        apiViewModel = new ViewModelProvider(MeetingDescriptionNewActivity.this).get(ApiViewModel.class);

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();

        ViewController.internetConnection(broadcastReceiver, MeetingDescriptionNewActivity.this);

        myDb = new DatabaseHelper(this);
        meetinglocations = new ArrayList<>();
        pdfsArrayList = new ArrayList<>();
        meetinglocations = myDb.getCompanyAddress();
        sharedPreferences1 = MeetingDescriptionNewActivity.this.getSharedPreferences("EGEMSS_DATA", 0);
        editor1 = sharedPreferences1.edit();

        empData = new EmpData();
        empData = myDb.getEmpdata();

        img_back = findViewById(R.id.img_back);
        txtTitle = findViewById(R.id.txtTitle);
        txt_date_time = findViewById(R.id.txt_date_time);
        invitee_details = findViewById(R.id.invitee_details);
        v_name = findViewById(R.id.v_name);
        resend = findViewById(R.id.iResend);
        pdf = findViewById(R.id.pdf);
        pdfName = findViewById(R.id.pdfName);
        uploadPdf = findViewById(R.id.uploadPdf);
        E_desi = findViewById(R.id.E_desi);
        email = findViewById(R.id.email);
        leanear_stamp = findViewById(R.id.leanear_stamp);
        txt_stamp = findViewById(R.id.txt_stamp);
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
        img_reccurence_list = findViewById(R.id.img_reccurence_list);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingDescriptionNewActivity.this, SetupMeetingActivity.class);
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
        o_time = findViewById(R.id.o_time);
        r_time = findViewById(R.id.r_time);
        main_time_date = findViewById(R.id.main_time_date);
        linear_original_suggested_time = findViewById(R.id.linear_original_suggested_time);
        invitee_count = findViewById(R.id.invitee_count);
        host = findViewById(R.id.host);
        coordinatorName = findViewById(R.id.coordinatorName);
        cardview_note = findViewById(R.id.cardview_note);
        description = findViewById(R.id.decription);
        title1 = findViewById(R.id.title1);
        invitedArrayList = new ArrayList<>();
        invitesData = new ArrayList<>();
        iEmail = new ArrayList<>();

        Log.e(TAG, "onCreate:e" + empData.getEmp_id());

        getsubhierarchys(empData.getLocation());
        assign.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            Assignpopup();
        });
        visitorcountlinear.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            Intent intent = new Intent(MeetingDescriptionNewActivity.this, VisitorDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("invites", meetings.getInvites());
            startActivity(intent);
        });
        host.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            getEmployeeDetails(meetings.getEmp_id());
        });
        assignedto.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            getEmployeeDetails(assignedId);
        });
        uploadPdf.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            pickPdf();
        });
        add_guest.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            if (status == 3) {
                AddGuest();
            } else {
                editor1.putString("m_id", meetings.get_id().get$oid());
                editor1.putInt("m_action", 1);
                editor1.commit();
                editor1.apply();
                Intent intent = new Intent(MeetingDescriptionNewActivity.this, SetupMeetingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        reschedule.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            if (R_status == 1) {
                ReschedulePopup();
            } else {
                editor1.putString("m_id", meetings.get_id().get$oid());
                editor1.putInt("m_action", 2);
                editor1.commit();
                editor1.apply();
                Intent intent = new Intent(MeetingDescriptionNewActivity.this, SetupMeetingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        pdf.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            if (empData.getEmp_id().equals(meetings.getEmp_id())) {
                PdfClick();
            } else {
                int pdfSize = meetings.getPdfs().size();
                String pdf_url = DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/pdfs/" + meetings.getPdfs().get(pdfSize - 1).getValue();
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                startActivity(browserIntent);
            }
        });
        cancellation.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);

            final Dialog dialog_c = new Dialog(MeetingDescriptionNewActivity.this);
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
                    if (reason.getText().toString().equalsIgnoreCase("")) {
                        reason.setError("Please Enter Your Reason");
                    } else {
                        //update appointment
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("formtype", "delete");
                            jsonObj_.put("c_emp_id", empData.getEmp_id());
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
                        //card_view_progress.setVisibility(View.VISIBLE);
                        ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);
                    }

                }
            });
            cancel.setOnClickListener(v1 -> {
                AnimationSet animation4 = Conversions.animation();
                v1.startAnimation(animation4);
                dialog_c.dismiss();
            });
            dialog_c.show();
        });
        accept.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            //card_view_progress.setVisibility(View.VISIBLE);
            ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);

            apiViewModel.gethostslots(MeetingDescriptionNewActivity.this, "end", empData.getEmp_id(), empData.getEmail(), s_time, e_time - 1);

        });
        resend.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.Resend)
                    .setMessage(R.string.are_you_sure_resend_this_meeting)
                    .setCancelable(false)
                    .setNegativeButton(R.string.no, (dialog, id) -> {
                        dialog.cancel();
                    })
                    .setPositiveButton(R.string.yes, (dialog, id) -> {
                        dialog.cancel();
                        //card_view_progress.setVisibility(View.VISIBLE);
                        ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);
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
                    });
            AlertDialog alert = builder.create();
            alert.show();

        });
        decline.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            popup(false);
        });
        apporve.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            JsonObject json = ApproverActionjson(true);
            System.out.println("json object" + json);
            updatemeetings(json);
        });
        reject.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            popup(true);
        });
        recyclerViewinvites = findViewById(R.id.recyclerViewinvites);
        txt_view_more = findViewById(R.id.txt_view_more);
        agendaText = findViewById(R.id.agendaText);
        agendaRecyclerView = findViewById(R.id.agendaRecyclerView);


        //recurrenc
        img_reccurence_list.setOnClickListener(view -> {
            AnimationSet animationp = Conversions.animation();
            view.startAnimation(animationp);

            Bundle bundle = new Bundle();
            bundle.putString("meetingslist", meetings.getRe_meetings() + "");
            FragmentManager fm = getSupportFragmentManager();
            ReccurenceDatesListFragment sFragment = new ReccurenceDatesListFragment();
            // Show DialogFragment
            sFragment.onItemsSelectedListner((type) -> {
                if (type.equalsIgnoreCase("cancel")) {
                    getmeetingdetails(meetingsId);
                }
            });
            sFragment.show(fm, "Dialog Fragment");
            sFragment.setArguments(bundle);
        });


        //add underline
        txt_view_more.setPaintFlags(txt_view_more.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_view_more.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            Bundle bundle = new Bundle();
            bundle.putString("meetingslist", meetings.getInvites() + "");
            FragmentManager fm = getSupportFragmentManager();
            ViewMoreInvitesFragment sFragment = new ViewMoreInvitesFragment();
            // Show DialogFragment
            sFragment.onItemsSelectedListner(new ViewMoreInvitesFragment.ItemSelected() {
                @Override
                public void onSelcetd(String id, String name, String email) {
                    if (id.equals("")) {
                        Log.e(TAG, "onSelcetd:id " + id);
                        builder = new AlertDialog.Builder(MeetingDescriptionNewActivity.this);
                        builder.setTitle("Visitor details not found")
                                .setMessage(email)
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
                        Log.e(TAG, "onSelcetd:id2 " + id);
                        getEmployeeDetails(id);
                    }
                }
            });
            sFragment.show(fm, "Dialog Fragment");
            sFragment.setArguments(bundle);
        });

        img_back.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            Intent intent2 = new Intent();
            setResult(RESULT_CODE, intent2);
            finish();
        });


        //meeting details
        getmeetingdetails(meetingsId);

        apiViewModel.gethostslots_response().observe(this, new Observer<HostSlotsModel>() {
            @Override
            public void onChanged(HostSlotsModel response) {
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

                        } else {
                            new AlertDialog.Builder(MeetingDescriptionNewActivity.this)
//                                   .setTitle(R.string.warning)
                                    .setMessage(R.string.you_have_another_meeting_at_this_time_)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        }
                    }
                }else {
                    Conversions.errroScreen(MeetingDescriptionNewActivity.this, "gethostslots");
                }
            }
        });

        apiViewModel.updatemeetings_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                //card_view_progress.setVisibility(GONE);
                ViewController.DismissProgressBar();
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        Intent intent = new Intent(MeetingDescriptionNewActivity.this, NavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else {
                    Conversions.errroScreen(MeetingDescriptionNewActivity.this, "updatemeetings");
                }
            }
        });

        apiViewModel.iresend_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                //card_view_progress.setVisibility(GONE);
                ViewController.DismissProgressBar();
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        System.out.println("asfasf" + response.result);
                        int colorInt = ContextCompat.getColor(getApplicationContext(), R.color.Accept);
                        ViewController.snackbar(MeetingDescriptionNewActivity.this, getApplicationContext().getString(R.string.success), colorInt);
                    }
                }else {
                    Conversions.errroScreen(MeetingDescriptionNewActivity.this, "iresend");                }
            }
        });

        apiViewModel.addcovisitor_response().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                //card_view_progress.setVisibility(GONE);
                ViewController.DismissProgressBar();
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(not_verified)) {
                    } else if (statuscode.equals(successcode)) {
                        Intent intent = new Intent(MeetingDescriptionNewActivity.this, NavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else {
                    Conversions.errroScreen(MeetingDescriptionNewActivity.this, "addcovisitor");
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
                }else {
                    Conversions.errroScreen(MeetingDescriptionNewActivity.this, "getsubhierarchys");
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
                        for (int j = 0; j < meetings.getInvites().size(); j++) {

                            for (int i = 0; i < employees.size(); i++) {

                                if (employees.get(i).getEmail().equals(empData.getEmail())) {
                                    employees.remove(employees.get(i));
                                }

                                if (meetings.getInvites().get(j).getId().equalsIgnoreCase(employees.get(i).get_id().get$oid())) {
                                    if (meetings.getInvites().get(j).isAssign() == true) {
                                        employees.remove(employees.get(i));
                                    }
                                }

                            }

                        }

                        if (employees.size() == 0) {
                            department_spinner.setText("");
                        } else {
                            employeeAdapter = new CustomAdapter(MeetingDescriptionNewActivity.this, R.layout.row, R.id.lbl_name, employees, 0, "");
                            emp_spinner.setThreshold(0);//will start working from first character
                            emp_spinner.setAdapter(employeeAdapter);//setting the adapter data into the AutoCompleteTextView
                            emp_spinner.setEnabled(true);
                        }
                    }
                }else {
                    Conversions.errroScreen(MeetingDescriptionNewActivity.this, "getsearchemployees");
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
                        ArrayList<CompanyData> DinvitesData = new ArrayList<>();


                        invitesadapter = new CustomAdapter(MeetingDescriptionNewActivity.this, R.layout.row, R.id.lbl_name, invitesData, 1, sharedPreferences1.getString("company_id", null));
                        email_spinner.setThreshold(2);
                        email_spinner.setAdapter(invitesadapter);
                        if (GUEST_STATUS) {
                            email_spinner.showDropDown();
                        }
                    }
                }else {
                    Conversions.errroScreen(MeetingDescriptionNewActivity.this, "getInviteData");
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

                        } else if (statuscode.equals(successcode)) {
                            Inviteeitem inviteDetails = model.getItems();
                            final Dialog dialog = new Dialog(MeetingDescriptionNewActivity.this);
                            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.invitee_popup);
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            final TextView submit;
                            final TextView v_name, E_desi, email, mobile, cancel, company1, title;
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
                                Glide.with(MeetingDescriptionNewActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + inviteDetails.getPic().get(inviteDetails.getPic().size() - 1))
                                        .into(emp_img);
                            }

                            if (inviteDetails.getSupertype().equals("employee")) {
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
                }
                else {
                    builder = new AlertDialog.Builder(MeetingDescriptionNewActivity.this);
                    builder.setMessage("Visitor details not found")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
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

        apiViewModel.getmeetingdetails_response().observe(this, response -> {
            //card_view_progress.setVisibility(GONE);
            ViewController.DismissProgressBar();
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {
                } else if (statuscode.equals(not_verified)) {
                } else if (statuscode.equals(successcode)) {
                    meetings = response.getItems();
                    setupView();
                }
            }else {
                Conversions.errroScreen(MeetingDescriptionNewActivity.this, "getmeetingdetails");
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

    //readorunread
    private void readorunread(JsonObject jsonObject) {
        apiViewModel.readorunread(getApplicationContext(), jsonObject);
    }

    private void updatemeetings(JsonObject jsonObject) {
        apiViewModel.updatemeetings(getApplicationContext(), jsonObject);
        //card_view_progress.setVisibility(View.VISIBLE);
        ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);
    }

    private void iResend(JsonObject jsonObject) {
        apiViewModel.iresend(getApplicationContext(), jsonObject);
        //card_view_progress.setVisibility(View.VISIBLE);
        ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);
    }

    private void popup(final Boolean ApproverAction) {
        final Dialog dialog = new Dialog(MeetingDescriptionNewActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.otp_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView submit;
        final TextView cancel;
        reason = dialog.findViewById(R.id.reason);
        TextInputLayout reason_textinput = dialog.findViewById(R.id.reason_textinput);
        submit = dialog.findViewById(R.id.submit);
        cancel = dialog.findViewById(R.id.cancel);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //card_view_progress.setVisibility(View.VISIBLE);
                ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);
                if (reason.getText().toString().equalsIgnoreCase("")) {
                    reason_textinput.setErrorEnabled(true);
                    reason_textinput.setError("Enter Reason");
                } else {

                    if (meetings.getMr_approvers().size() != 0) {
                        //room approver
                        JsonObject gsonObject = new JsonObject();
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("formtype", "mr_reject");
                            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                            jsonObj_.put("reason", reason.getText().toString());
                            jsonObj_.put("emp_id", empData.getEmp_id());
                            jsonObj_.put("id", meetings.get_id().get$oid());
                            JsonParser jsonParser = new JsonParser();
                            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updatemeetings(gsonObject);
                    } else {
                        if (ApproverAction) {
                            JsonObject json = ApproverActionjson(false);
                            updatemeetings(json);
                        } else {
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
                    }

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

    private void ReschedulePopup() {
        final Dialog dialog = new Dialog(MeetingDescriptionNewActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.reschedule_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView request, cancel;

        reason = dialog.findViewById(R.id.reason);
        cancel = dialog.findViewById(R.id.cancel);
        request = dialog.findViewById(R.id.request);

        datep = dialog.findViewById(R.id.date);
        meeting_st = dialog.findViewById(R.id.meeting_st);
        meeting_et = dialog.findViewById(R.id.meeting_duration);
        Long d = meetings.getEnd() - meetings.getStart() + 1;
        d_hour = (int) Math.floor(d / 3600);
        d_min = (int) Math.floor((d % 3600) / 60);
        datep.setText(ViewController.getSdate((meetings.getDate() + Conversions.timezone()) * 1000));
        meeting_st.setText(Conversions.millitotime((meetings.getStart() + Conversions.timezone()) * 1000, false));
        meeting_et.setText(d_hour + ":" + d_min);


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
                    new AlertDialog.Builder(MeetingDescriptionNewActivity.this)
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
                    new AlertDialog.Builder(MeetingDescriptionNewActivity.this)
                            .setTitle("Warning")
                            .setMessage("Select date")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else if (meeting_st.getText().length() == 0) {
                    new AlertDialog.Builder(MeetingDescriptionNewActivity.this)
                            .setTitle("Warning")
                            .setMessage("Select start time")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    final int hour = 1;
                    final int minute = 0;
                    final RangeTimePickerDialog mTimePicker;

                    mTimePicker = new RangeTimePickerDialog(MeetingDescriptionNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                            e_time = Conversions.gettimestamp(date.getText().toString(), newTime);
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
                //card_view_progress.setVisibility(View.VISIBLE);
                ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);
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
        final Dialog dialog = new Dialog(MeetingDescriptionNewActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_guest_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView img_phone_list;
        TextView add_guest, cancel;

        invitedArrayList = new ArrayList<>();
//        meetings.getApprover_roleid()
        img_phone_list = dialog.findViewById(R.id.img_phone_list);
        chipgroup = dialog.findViewById(R.id.chipgroup);
        add_guest = dialog.findViewById(R.id.add_guest);
        cancel = dialog.findViewById(R.id.cancel);
        email_spinner = dialog.findViewById(R.id.email_spinner);
        for (int i = 0; i < meetings.getInvites().size(); i++) {
            Chip newChip = getChip(chipgroup, meetings.getInvites().get(i), 2);
            iEmail.add(meetings.getInvites().get(i).getEmail());
            chipgroup.addView(newChip);
        }

        img_phone_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationimg_phone_list = Conversions.animation();
                v.startAnimation(animationimg_phone_list);
                pickContact();
            }
        });

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
            public void onItemClick(AdapterView<?> av, View arg1, int index, long arg3) {
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
                    invited.setName(contactModel.getName() + "");
                    invited.setEmail(contactModel.getEmail() + "");
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
                if (invitedArrayList.size() != 0) {
                    dialog.dismiss();
                    //card_view_progress.setVisibility(View.VISIBLE);
                    ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);
                    JsonObject json = addCovisitorjson();
                    System.out.println("json object" + json);
                    addcovisitor(json);
                }
            }
        });
        dialog.show();
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
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
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
                    Log.d("Contact", "Phone Number: " + phoneNumber);
                    listOfPhoneNumbers.add(phoneNumber.replace(" ", ""));
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
                    listOfEmailAddress.add(emailAddress.replace(" ", ""));
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


        if (listOfPhoneNumbers != null) {

            String self_result = empData.getMobile().replaceAll("[-+.^:,]", "");
            String replace_number = phoneNumber.replaceAll("[-+.^:,]", "");

            //only one number condition
            if (listOfPhoneNumbers.size() <= 1) {
                if (phoneNumber.length() >= 10) {

                    //self meeting check
                    if (empData.getEmail().equalsIgnoreCase(emailAddress) || self_result.equalsIgnoreCase(replace_number)) {
                        Toast.makeText(getApplicationContext(), "Self Account Not Acceptable", Toast.LENGTH_SHORT).show();
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
                }
            } else {
                //Multiple numbers list
                showMultipleContactsDialog(contactId, replace_number, phonecontactName, listOfPhoneNumbers, EmailAddresslist);
            }
        } else {

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
        Invited invited = new Invited();
        invited.setId(contactId);
        invited.setName(phonecontactName);
        invited.setEmail(emailAddress);

        //number
        if (phoneNumber.equalsIgnoreCase("")) {
        } else {
            String[] phonenumbers = {phoneNumber};
            for (String phone : phonenumbers) {

                //old_code
                if (isPhoneNumberValid(phone)) {
                    String l_mobile = phoneNumber.replace(" ", "");
                    if (l_mobile.length() == 10) {
                        invited.setMobile("+91" + phoneNumber);
                    } else if (l_mobile.length() == 14) {
                        String s = "";
                        if (l_mobile.length() > 2) {
                            s = l_mobile.substring(0, 2);
                        } else {
                            s = l_mobile;
                        }
                        if (s.equalsIgnoreCase("00")) {
                            invited.setMobile(l_mobile.replaceFirst("00", "+"));
                        } else {
                            invited.setMobile(phoneNumber);
                        }
                    } else {
                        invited.setMobile(phoneNumber);
                    }
                } else {
                    String l_mobile = phoneNumber.replace(" ", "");
                    if (l_mobile.length() == 15) {
                        String s = "";
                        if (l_mobile.length() > 1) {
                            s = l_mobile.substring(0, 1);
                        } else {
                            s = l_mobile;
                        }
                        if (s.equalsIgnoreCase("+")) {
                            String[] arrOfStr = phoneNumber.split("[+]");
                            for (String a : arrOfStr) {
                                invited.setMobile(a.replaceFirst("00", "+"));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                        }
                    } else if (l_mobile.length() == 12) {
                        String s = "";
                        if (l_mobile.length() > 2) {
                            s = l_mobile.substring(0, 2);
                        } else {
                            s = l_mobile;
                        }
                        if (s.equalsIgnoreCase("00")) {
                            invited.setMobile(l_mobile.replaceFirst("00", "+91"));
                        } else {
                            invited.setMobile(phoneNumber);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

        invited.setCompany("");
        invited.setLink("");
        invitedArrayList.add(invited);
        status = 0;
        Chip newChip = getChip(chipgroup, invited, 1);
        chipgroup.addView(newChip);
        iEmail.add(emailAddress);
        GUEST_STATUS = false;
    }

    //MultipleContacts
    private void showMultipleContactsDialog(String contactid, String contactphonenumber, String contactName, LinkedList<String> listOfPhoneNumbers, LinkedList<String> emailAddresslist) {

        final Dialog dialog = new Dialog(MeetingDescriptionNewActivity.this);
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


        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);

                String res = "";
                Invited update_position = null;

                //already in existing list
                if (invitedArrayList.size() != 0) {

                    for (int i = 0; i < invitedArrayList.size(); i++) {
                        if (invitedArrayList.get(i).getId().equalsIgnoreCase(contactid)) {
                            res = invitedArrayList.get(i).getId();
                            update_position = invitedArrayList.get(i);

                            //update item
                            if (selectedEmailAddress.equalsIgnoreCase("") && selectedPhoneNumber.equalsIgnoreCase("")) {
                                dialog.dismiss();
                            } else {
                                //self meeting check
                                if (empData.getEmail().equalsIgnoreCase(selectedEmailAddress)) {
                                    Toast.makeText(MeetingDescriptionNewActivity.this, "Self Account Not Acceptable", Toast.LENGTH_SHORT).show();
                                } else {

                                    update_position.setId(contactid);
                                    update_position.setName(contactName);
                                    update_position.setChecked(true);
                                    update_position.setEmail(selectedEmailAddress);

                                    //number
                                    if (selectedPhoneNumber.equalsIgnoreCase("")) {
                                        update_position.setMobile("");
                                    } else {
                                        String[] phonenumbers = {selectedPhoneNumber};
                                        for (String phone : phonenumbers) {
                                            if (isPhoneNumberValid(phone)) {
                                                String l_mobile = selectedPhoneNumber.replace(" ", "");
                                                if (l_mobile.length() == 10) {
                                                    update_position.setMobile("+91" + selectedPhoneNumber);
                                                } else if (l_mobile.length() == 14) {
                                                    String s = "";
                                                    if (l_mobile.length() > 2) {
                                                        s = l_mobile.substring(0, 2);
                                                    } else {
                                                        s = l_mobile;
                                                    }
                                                    if (s.equalsIgnoreCase("00")) {
                                                        update_position.setMobile(l_mobile.replaceFirst("00", "+"));
                                                    } else {
                                                        update_position.setMobile(selectedPhoneNumber);
                                                    }
                                                } else {
                                                    update_position.setMobile(selectedPhoneNumber);
                                                }
                                            } else {
                                                String l_mobile = selectedPhoneNumber.replace(" ", "");
                                                if (l_mobile.length() == 15) {
                                                    String s = "";
                                                    if (l_mobile.length() > 1) {
                                                        s = l_mobile.substring(0, 1);
                                                    } else {
                                                        s = l_mobile;
                                                    }
                                                    if (s.equalsIgnoreCase("+")) {
                                                        String[] arrOfStr = selectedPhoneNumber.split("[+]");
                                                        for (String a : arrOfStr) {
                                                            update_position.setMobile(a.replaceFirst("00", "+"));
                                                        }
                                                    } else {
                                                        Toast.makeText(MeetingDescriptionNewActivity.this, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else if (l_mobile.length() == 12) {
                                                    String s = "";
                                                    if (l_mobile.length() > 2) {
                                                        s = l_mobile.substring(0, 2);
                                                    } else {
                                                        s = l_mobile;
                                                    }
                                                    if (s.equalsIgnoreCase("00")) {
                                                        update_position.setMobile(l_mobile.replaceFirst("00", "+91"));
                                                    } else {
                                                        Toast.makeText(MeetingDescriptionNewActivity.this, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(MeetingDescriptionNewActivity.this, "is not valid" + phone, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }

                                    update_position.setCompany("");
                                    update_position.setLink("");

                                }
                            }

                        }
                    }

                    if (!res.equalsIgnoreCase(contactid)) {
                        Log.e(TAG, "onClick: " + "123");

                        if (selectedEmailAddress.equalsIgnoreCase("") && selectedPhoneNumber.equalsIgnoreCase("")) {
                            dialog.dismiss();
                        } else {
                            //self meeting check
                            if (empData.getEmail().equalsIgnoreCase(selectedEmailAddress)) {
                                Toast.makeText(MeetingDescriptionNewActivity.this, "Self Account Not Acceptable", Toast.LENGTH_SHORT).show();
                            } else {

                                Invited invited = new Invited();
                                invited.setId(contactid);
                                invited.setName(contactName);
                                invited.setChecked(true);
                                Log.e(TAG, "onClick: dsd" + "das");
                                invited.setEmail(selectedEmailAddress);

                                //number
                                if (selectedPhoneNumber.equalsIgnoreCase("")) {
                                    invited.setMobile("");
                                } else {
                                    String[] phonenumbers = {selectedPhoneNumber};
                                    for (String phone : phonenumbers) {
                                        if (isPhoneNumberValid(phone)) {
                                            String l_mobile = selectedPhoneNumber.replace(" ", "");
                                            if (l_mobile.length() == 10) {
                                                invited.setMobile("+91" + selectedPhoneNumber);
                                            } else if (l_mobile.length() == 14) {
                                                String s = "";
                                                if (l_mobile.length() > 2) {
                                                    s = l_mobile.substring(0, 2);
                                                } else {
                                                    s = l_mobile;
                                                }
                                                if (s.equalsIgnoreCase("00")) {
                                                    invited.setMobile(l_mobile.replaceFirst("00", "+"));
                                                } else {
                                                    invited.setMobile(selectedPhoneNumber);
                                                }
                                            } else {
                                                invited.setMobile(selectedPhoneNumber);
                                            }
                                        } else {
                                            String l_mobile = selectedPhoneNumber.replace(" ", "");
                                            if (l_mobile.length() == 15) {
                                                String s = "";
                                                if (l_mobile.length() > 1) {
                                                    s = l_mobile.substring(0, 1);
                                                } else {
                                                    s = l_mobile;
                                                }
                                                if (s.equalsIgnoreCase("+")) {
                                                    String[] arrOfStr = selectedPhoneNumber.split("[+]");
                                                    for (String a : arrOfStr) {
                                                        invited.setMobile(a.replaceFirst("00", "+"));
                                                    }
                                                } else {
                                                    Toast.makeText(MeetingDescriptionNewActivity.this, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                                                }
                                            } else if (l_mobile.length() == 12) {
                                                String s = "";
                                                if (l_mobile.length() > 2) {
                                                    s = l_mobile.substring(0, 2);
                                                } else {
                                                    s = l_mobile;
                                                }
                                                if (s.equalsIgnoreCase("00")) {
                                                    invited.setMobile(l_mobile.replaceFirst("00", "+91"));
                                                } else {
                                                    Toast.makeText(MeetingDescriptionNewActivity.this, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(MeetingDescriptionNewActivity.this, "is not valid" + phone, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }

                                invited.setCompany("");
                                invited.setLink("");
                                invitedArrayList.add(invited);
                                status = 0;
                                Chip newChip = getChip(chipgroup, invited, 1);
                                chipgroup.addView(newChip);
                                iEmail.add(selectedEmailAddress);

                            }
                        }

                    }
                } else {
                    if (selectedEmailAddress.equalsIgnoreCase("") && selectedPhoneNumber.equalsIgnoreCase("")) {
                        dialog.dismiss();
                    } else {
                        //self meeting check
                        if (empData.getEmail().equalsIgnoreCase(selectedEmailAddress)) {
                            Toast.makeText(MeetingDescriptionNewActivity.this, "Self Account Not Acceptable", Toast.LENGTH_SHORT).show();
                        } else {

                            Invited invited = new Invited();
                            invited.setId(contactid);
                            invited.setName(contactName);
                            invited.setChecked(true);
                            invited.setEmail(selectedEmailAddress);

                            //number
                            if (selectedPhoneNumber.equalsIgnoreCase("")) {
                                invited.setMobile("");
                            } else {
                                String[] phonenumbers = {selectedPhoneNumber};
                                for (String phone : phonenumbers) {
                                    if (isPhoneNumberValid(phone)) {
                                        String l_mobile = selectedPhoneNumber.replace(" ", "");
                                        if (l_mobile.length() == 10) {
                                            invited.setMobile("+91" + selectedPhoneNumber);
                                        } else if (l_mobile.length() == 14) {
                                            String s = "";
                                            if (l_mobile.length() > 2) {
                                                s = l_mobile.substring(0, 2);
                                            } else {
                                                s = l_mobile;
                                            }
                                            if (s.equalsIgnoreCase("00")) {
                                                invited.setMobile(l_mobile.replaceFirst("00", "+"));
                                            } else {
                                                invited.setMobile(selectedPhoneNumber);
                                            }
                                        } else {
                                            invited.setMobile(selectedPhoneNumber);
                                        }
                                    } else {
                                        String l_mobile = selectedPhoneNumber.replace(" ", "");
                                        if (l_mobile.length() == 15) {
                                            String s = "";
                                            if (l_mobile.length() > 1) {
                                                s = l_mobile.substring(0, 1);
                                            } else {
                                                s = l_mobile;
                                            }
                                            if (s.equalsIgnoreCase("+")) {
                                                String[] arrOfStr = selectedPhoneNumber.split("[+]");
                                                for (String a : arrOfStr) {
                                                    invited.setMobile(a.replaceFirst("00", "+"));
                                                }
                                            } else {
                                                Toast.makeText(MeetingDescriptionNewActivity.this, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                                            }
                                        } else if (l_mobile.length() == 12) {
                                            String s = "";
                                            if (l_mobile.length() > 2) {
                                                s = l_mobile.substring(0, 2);
                                            } else {
                                                s = l_mobile;
                                            }
                                            if (s.equalsIgnoreCase("00")) {
                                                invited.setMobile(l_mobile.replaceFirst("00", "+91"));
                                            } else {
                                                Toast.makeText(MeetingDescriptionNewActivity.this, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(MeetingDescriptionNewActivity.this, "is not valid" + phone, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            invited.setCompany("");
                            invited.setLink("");
                            invitedArrayList.add(invited);
                            status = 0;
                            Chip newChip = getChip(chipgroup, invited, 1);
                            chipgroup.addView(newChip);
                            iEmail.add(selectedEmailAddress);

                        }
                    }
                }

                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvContactName.setText(contactName);

        if (listOfPhoneNumbers != null) {
            if (listOfPhoneNumbers.size() != 0) {
                initializePhoneNumberAdapter(rvPhoneNumbers, listOfPhoneNumbers);
            }
        }

        if (emailAddresslist != null) {
            if (emailAddresslist.size() != 0) {
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
        rvPhoneNumbers.setHasFixedSize(true);
        LinearLayoutManager layoutManagertrending = new LinearLayoutManager(getApplicationContext());
        rvPhoneNumbers.setLayoutManager(layoutManagertrending);
        MultiplePhoneNumberAdapter multiplePhoneNumberAdapter = new MultiplePhoneNumberAdapter(getApplicationContext(), listOfPhoneNumbers, new Customthree() {
            @Override
            public void onItemClick(String selectedphoneNumber, String cNAme, String CUUId) {
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


    private void Assignpopup() {
        final Dialog dialog = new Dialog(MeetingDescriptionNewActivity.this);
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

        departmentAdapter = new CustomAdapter(MeetingDescriptionNewActivity.this, R.layout.row, R.id.lbl_name, departments, 0, "");
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
                //card_view_progress.setVisibility(GONE);
                ViewController.DismissProgressBar();
            }
        });
        assign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //card_view_progress.setVisibility(View.VISIBLE);
                ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);
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

        apiViewModel.addcovisitor(getApplicationContext(), jsonObject);
        //card_view_progress.setVisibility(View.VISIBLE);
        ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);
    }

    private void getsubhierarchys(String l_id) {

        apiViewModel.getsubhierarchys(getApplicationContext(), l_id);

    }

    private void getsearchemployees(String l_id, String h_id) {

        apiViewModel.getsearchemployees(getApplicationContext(), l_id, h_id);

    }

    private GlideChip getChip(final ChipGroup chipGroup, final Invited invited, Integer type) {
        final GlideChip chip = new GlideChip(MeetingDescriptionNewActivity.this);
        ChipDrawable chipDrawable = ChipDrawable.createFromResource(MeetingDescriptionNewActivity.this, R.xml.chip);
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
            chip.setText(invited.getName());
        }

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(MeetingDescriptionNewActivity.this);
                builder.setMessage("Are you sure you want to delete this Invitee ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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

        apiViewModel.getInviteData(getApplicationContext(), "type", "usertype", S_value);

    }

    private void datePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final String current_date = mYear + "- " + mMonth + "-" + mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(MeetingDescriptionNewActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        date.setText(new StringBuilder().append(dayOfMonth)
                                .append("-").append(monthOfYear + 1).append("-").append(year)
                                .append(" "));

                        s_date = Conversions.getdatestamp(datep.getText().toString());

                        System.out.println("s_date :-" + s_date + " s_time :-" + s_time + " e_time :-" + e_time);

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
            mTimePicker = new RangeTimePickerDialog(MeetingDescriptionNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    meeting_st.setText(selectedHour + ":" + selectedMinute);
                    s_time = Conversions.gettimestamp(datep.getText().toString(), selectedHour + ":" + selectedMinute);
                    System.out.println("s_date :-" + s_date + " s_time :-" + s_time + " e_time :-" + e_time);
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
                    e_time = Conversions.gettimestamp(datep.getText().toString(), newTime);
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
            mTimePicker = new RangeTimePickerDialog(MeetingDescriptionNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    meeting_st.setText(selectedHour + ":" + selectedMinute);
                    s_time = Conversions.gettimestamp(datep.getText().toString(), selectedHour + ":" + selectedMinute);
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
                    e_time = Conversions.gettimestamp(datep.getText().toString(), newTime);
                    System.out.println("s_date :-" + s_date + " s_time :-" + s_time + " e_time :-" + e_time);
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
            jsonObj_.put("location", meetings.getLocation());
            jsonObj_.put("start", meetings.getStart());
            jsonObj_.put("end", meetings.getEnd());
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

        if (meetings.getMr_approvers().size() != 0) {
            //room approver
            try {
                jsonObj_.put("formtype", "mr_approve");
                jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                jsonObj_.put("emp_id", empData.getEmp_id());
                jsonObj_.put("id", meetings.get_id().get$oid());
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //approver
            try {
                jsonObj_.put("formtype", "mbefore");
                jsonObj_.put("id", meetings.get_id().get$oid());
                jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                jsonObj_.put("emp_id", empData.getEmp_id());
                jsonObj_.put("approver_roleid", meetings.getApprover_roleid());
                jsonObj_.put("status", 2);
                if (!Apporve) {
                    jsonObj_.put("status", 1);
                    jsonObj_.put("reason", reason.getText().toString());
                }
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                System.out.println("gsonObject::" + gsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    public JSONObject getRdata() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("status", 0);
            jo.put("date", s_date);
            jo.put("start", s_time);
            jo.put("end", e_time - 1);
            jo.put("emp_id", "");
            jo.put("roleid", "");
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

    public class InvitesAdapter1 extends RecyclerView.Adapter<MeetingDescriptionNewActivity.InvitesAdapter1.MyviewHolder> {
        Context context;
        ArrayList<Invited> invited;
        int SelectedIndex;

        public InvitesAdapter1(Context context, ArrayList<Invited> vehicles) {
            this.context = context;
            this.invited = vehicles;
        }

        @Override
        public InvitesAdapter1.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.invitess_items_list, parent, false);
            return new InvitesAdapter1.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final InvitesAdapter1.MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {
            System.out.println("Size@" + invited.size());
            System.out.println("Size1@" + invited.get(position).getName());

            //name
            if (invited.get(position).getName().equals("x") || invited.get(position).getName().equals("")) {
                holder.txt_name.setText("   -   ");
            } else {
                holder.txt_name.setText(invited.get(position).getName());
            }

            //email
            if (invited.get(position).getEmail() == null || invited.get(position).getEmail().equals("")) {
                holder.txt_email.setText("   -   ");
            } else {
                holder.txt_email.setText(invited.get(position).getEmail());
            }

            //mobile
            if (invited.get(position).getMobile() == null || invited.get(position).getMobile().equals("")) {
                holder.txt_phone.setText("   -   ");
            } else {
                holder.txt_phone.setText(invited.get(position).getMobile());
            }

            //slots
            if (invited.get(position).getParking_status()) {
                holder.txt_p_slot.setText(invited.get(position).getPcat_name() + " - " + invited.get(position).getLot_name() + " - " + invited.get(position).getSlot_name());
            } else if (!invited.get(position).getPcat_name().equalsIgnoreCase("") && !invited.get(position).getLot_name().equalsIgnoreCase("")) {
                holder.txt_p_slot.setText(invited.get(position).getPcat_name() + " - " + invited.get(position).getLot_name() + " - " + invited.get(position).getSlot_name());
            } else {
                holder.txt_p_slot.setText("   -   ");
            }


            //assign title showing
            if (invited.get(position).isAssign()) {
                holder.txtAssigned.setVisibility(View.VISIBLE);
            } else {
                holder.txtAssigned.setVisibility(GONE);
            }


            holder.card_view.setOnClickListener(v -> {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);

                if (invited.get(position).getId().equals("")) {
                    builder = new AlertDialog.Builder(MeetingDescriptionNewActivity.this);
                    builder.setTitle("Visitor details not found")
                            .setMessage(invited.get(position).getEmail())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> {
                                //do things
                                dialog.cancel();
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    getEmployeeDetails(invited.get(position).getId());
                }
            });


        }

        @Override
        public int getItemCount() {
            int limit = 2;
            return Math.min(invited.size(), limit);
        }

        public class MyviewHolder extends RecyclerView.ViewHolder {
            CardView card_view;
            TextView txtAssigned, txt_name, txt_email, txt_phone, txt_p_slot;

            public MyviewHolder(View view) {
                super(view);
                card_view = view.findViewById(R.id.card_view);
                txtAssigned = view.findViewById(R.id.txtAssigned);
                txt_name = view.findViewById(R.id.txt_name);
                txt_email = view.findViewById(R.id.txt_email);
                txt_phone = view.findViewById(R.id.txt_phone);
                txt_p_slot = view.findViewById(R.id.txt_p_slot);
            }
        }

    }


    private void getEmployeeDetails(String position) {
        apiViewModel.getEmployeeDetails(getApplicationContext(), position);
    }

    private void PdfClick() {
        final CharSequence[] options = {"Open", "Download", "Delete", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDescriptionNewActivity.this);
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Open")) {
                int pdfSize = meetings.getPdfs().size();
                String pdf_url = DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/pdfs/" + meetings.getPdfs().get(pdfSize - 1).getValue();
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                startActivity(browserIntent);
            } else if (options[item].equals("Download")) {
                downloadPDF();
            } else if (options[item].equals("Delete")) {
                HostPdfDelete();
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void downloadPDF() {
        int pdfSize = meetings.getPdfs().size();

        if (pdfSize > 0) {
            String pdfUrl = DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/pdfs/" + meetings.getPdfs().get(pdfSize - 1).getValue();
            startPdfDownload(pdfUrl, "latest_meeting.pdf");
        }
    }

    private void startPdfDownload(String pdfUrl, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
        request.setTitle("Downloading PDF");
        request.setDescription("Downloading meeting PDF");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

    private void InviteePdfClick(int index) {
        final CharSequence[] options = {"View", "Delete", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDescriptionNewActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("View")) {
                    int pdfSize = meetings.getInvites().get(index).getPdfs().size();
                    String pdf_url = DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/pdfs/" + meetings.getInvites().get(index).getPdfs().get(pdfSize - 1).getValue();
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                    startActivity(browserIntent);
                } else if (options[item].equals("Delete")) {
                    InviteePdfDelete();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void HostPdfDelete() {
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

    private void InviteePdfDelete() {
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

    protected void pickPdf() {
        if (permissionsGrantedPickPdf()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, 9811);
        } else {
            ActivityCompat.requestPermissions(MeetingDescriptionNewActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        }
    }

    private Boolean permissionsGrantedPickPdf() {
        return ContextCompat.checkSelfPermission(MeetingDescriptionNewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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
                                Log.e("Filename", displayName);
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

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            // Handle the selected contact
            Uri contactUri = data.getData();
            getContactDetails(contactUri);
        }

    }

    private void uploadFile(Uri fileUri) throws FileNotFoundException, RemoteException {
        String PDFPATH = FileUtilsClass.getRealPath(MeetingDescriptionNewActivity.this, fileUri);
        System.out.println("Floder " + this.getExternalCacheDir().getAbsolutePath());
        System.out.println("PDfPAth " + fileUri.getPath());
        System.out.println("PDfPAth " + fileUri);
        System.out.println("PDfPAth " + PDFPATH);
        if (PDFPATH == null || PDFPATH.equals("")) {
            PDFPATH = FileUtilsClass.getFilePathFromURI(MeetingDescriptionNewActivity.this, fileUri);
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
        RequestBody fname = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody c_id = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences1.getString("company_id", null));


        apiViewModel.pdfupload(MeetingDescriptionNewActivity.this, fileToUpload, fname, c_id);

        UploadPdf(file.getName());

    }

    private void UploadPdf(String filename) {
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
            if (meetings.getInvites().get(indexid).getPdfs() != null) {
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

        apiViewModel.getmeetingdetails(MeetingDescriptionNewActivity.this, id);
        //card_view_progress.setVisibility(View.VISIBLE);
        ViewController.ShowProgressBar(MeetingDescriptionNewActivity.this);

    }

    @SuppressLint("RestrictedApi")
    private void setupView() {
        assert meetings != null;
        if (meetings.getPdfStatus() != null && meetings.getPdfStatus()) {
            pdf.setVisibility(View.VISIBLE);
            pdfsArrayList = meetings.getPdfs();
            pdfName.setText(pdfsArrayList.get(0).getName());
        } else {
            pdf.setVisibility(GONE);
        }

//        //create date
//        long longtime = (Long.parseLong(meetings.getA_time().get$numberLong()) * 1000);
//        Date d = new Date(longtime);
//        String c_date = DateFormat.getDateTimeInstance().format(d);
//        txt_date_time.setText(c_date);

        subject.setText(meetings.getSubject());

        if (meetings.getCategoryData().getCat_type() == true) {
            txt_category.setText(meetings.getCategoryData().getName() + " (Confidential)");
        } else {
            txt_category.setText(meetings.getCategoryData().getName() + " (Non-Confidential)");
        }

        s_date = meetings.getDate();
        s_time = meetings.getStart();
        e_time = meetings.getEnd();

        if (meetings.getRecurrence()) {
            img_reccurence_list.setVisibility(VISIBLE);
        }

//        if (empData.getEmp_id() != (meetings.getCoordinator())){
//            coordinatorName.setVisibility(VISIBLE);
//            coordinatorName.setText("organized by ("+meetings.getCoordinatorData().getName()+ ")");
//        }


        if (empData.getEmp_id().equals(meetings.getEmp_id())) {
            uploadPdf.setVisibility(View.VISIBLE);
            isHostPdfUpload = true;
            resend.setVisibility(View.VISIBLE);
            reschedule.setVisibility(View.VISIBLE);
            add_guest.setVisibility(View.VISIBLE);
            assign.setVisibility(View.VISIBLE);
            String Cancel_access = Preferences.loadStringValue(getApplicationContext(), Preferences.Cancel_access, "");
            if (Cancel_access.equalsIgnoreCase("true")){
                cancellation.setVisibility(View.VISIBLE);
            }
            Log.e("status_check", "1");
        }else if (empData.getEmp_id().equals(meetings.getCoordinator())){
            uploadPdf.setVisibility(View.VISIBLE);
            isHostPdfUpload = true;
            resend.setVisibility(View.VISIBLE);
            reschedule.setVisibility(View.VISIBLE);
            add_guest.setVisibility(View.VISIBLE);
            assign.setVisibility(View.VISIBLE);
            String Cancel_access = Preferences.loadStringValue(getApplicationContext(), Preferences.Cancel_access, "");
            if (Cancel_access.equalsIgnoreCase("true")){
                cancellation.setVisibility(View.VISIBLE);
            }

            Log.e("status_check", "1.0");
        }
        else if (empData.getRoleid().equals(meetings.getApprover_roleid())) {
            Log.e("status_check", "2");

            uploadPdf.setVisibility(GONE);

            if (meetings.getMr_approvers().size() != 0) {
                txtTitle.setText(getString(R.string.Meetingroom_approval_request));
                //room approve status
                if (meetings.getMr_approver_statistics().get(0).getStatus() == 0.0) {
                    apporve.setVisibility(View.VISIBLE);
                    reject.setVisibility(View.VISIBLE);
                } else if (meetings.getMr_approver_statistics().get(0).getStatus() == 1.0) {
                    reject.setVisibility(VISIBLE);
                    apporve.setVisibility(GONE);
                    leanear_stamp.setBackgroundResource(R.color.gray);
                    leanear_stamp.setVisibility(View.VISIBLE);
                    txt_stamp.setText("The Meeting Room Approver has been Approved by you");
                } else if (meetings.getMr_approver_statistics().get(0).getStatus() == 2.0) {
                    apporve.setVisibility(VISIBLE);
                    reject.setVisibility(GONE);
                    leanear_stamp.setBackgroundResource(R.color.Cancel);
                    leanear_stamp.setVisibility(View.VISIBLE);
                    txt_stamp.setText("The Meeting Room Approver has been rejected by you");
                }
            } else {
                txtTitle.setText(getString(R.string.approval_request));
                //approve status
                if (meetings.getApprover_statistics().get(0).getStatus() == 0.0) {
                    apporve.setVisibility(View.VISIBLE);
                    reject.setVisibility(View.VISIBLE);
                } else if (meetings.getApprover_statistics().get(0).getStatus() == 1.0) {
                    apporve.setVisibility(VISIBLE);
                    reject.setVisibility(GONE);
                    leanear_stamp.setBackgroundResource(R.color.Cancel);
                    leanear_stamp.setVisibility(View.VISIBLE);
                    txt_stamp.setText("The Approver has been rejected by you");
                } else if (meetings.getApprover_statistics().get(0).getStatus() == 2.0) {
                    reject.setVisibility(VISIBLE);
                    apporve.setVisibility(GONE);
                    leanear_stamp.setBackgroundResource(R.color.gray);
                    leanear_stamp.setVisibility(View.VISIBLE);
                    txt_stamp.setText("The Approver has been Approved by you");
                }
            }


        } else {
            Log.e("status_check", "3");
            for (int i = 0; i < meetings.getInvites().size(); i++) {
                if (empData.getEmail().equals(meetings.getInvites().get(i).getEmail())) {
                    uploadPdf.setVisibility(View.VISIBLE);
                    isHostPdfUpload = false;
                    R_status = 1;
                    accept.setVisibility(View.VISIBLE);
                    decline.setVisibility(View.VISIBLE);
                    reschedule.setVisibility(View.VISIBLE);
                    System.out.println();
                    if (meetings.getInvites().get(i).getStatus() == 3) {
                        status = 3;
                        add_guest.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.GONE);
                        leanear_stamp.setBackgroundResource(R.color.Accept);
                        leanear_stamp.setVisibility(View.VISIBLE);
                        txt_stamp.setText("Meeting is Accepted");
                    } else if (meetings.getInvites().get(i).getStatus() == 1) {
                        decline.setVisibility(View.GONE);
                        leanear_stamp.setBackgroundResource(R.color.Cancel);
                        leanear_stamp.setVisibility(View.VISIBLE);
                        txt_stamp.setText("Meeting is Cancelled by you");
                    }
                    indexid = i;
                    break;
                }
            }
        }

        host.setText(" " + meetings.getEmployee().getName());

        if (meetings.getInvites().size() == 1) {
            invitee_text.setText("(" + meetings.getInvites().size() + ")");
        } else {
            invitee_text.setText("(" + meetings.getInvites().size() + ")");
        }
        company.setText(meetings.getEmployee().getDesignation());
        if (empData.getEmp_id().equals(meetings.getEmp_id())) {
            host.setText("Me");
            name.setText(meetings.getInvites().get(0).getName() + "\n" + meetings.getInvites().get(0).getEmail());
            company.setText(meetings.getInvites().get(0).getCompany());
            if (meetings.getInvites().get(0).getPic().size() != 0) {
                Glide.with(MeetingDescriptionNewActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetings.getInvites().get(0).getPic().get(meetings.getInvites().get(0).getPic().size() - 1))
                        .into(img);
            }
        }
        if (meetings.getStatus() == 1) {
            uploadPdf.setVisibility(GONE);
            action_btn.setVisibility(View.GONE);
            leanear_stamp.setBackgroundResource(R.color.Cancel);
            leanear_stamp.setVisibility(View.VISIBLE);
            txt_stamp.setText("Meeting is Cancelled by you");
        }

//        if(meetings.getStart()+Conversions.timezone() <= (Calendar.getInstance().getTimeInMillis()/1000)){
//            action_btn.setVisibility(View.GONE);
//            uploadPdf.setVisibility(GONE);
//        }

        //location
        if (meetings.getMtype().equals("1")) {
            location.setText(meetings.getEntrypoints().getName() + ", " + meetings.getMeetingrooms().getName() + ", " + meetinglocations.get(Integer.parseInt(meetings.getLocation())).getName());
        }
        //Virtual meeting link redirect to web
        if (meetings.getMtype().equals("2")) {
            Spannable spannable = new SpannableString(meetings.getMtype_val());
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    String urlString = meetings.getMtype_val();
                    try {
                        Uri uri = Uri.parse(urlString);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } catch (NullPointerException e) {
                        Log.e("URL Parsing Error", "URL string is null");
                        e.printStackTrace();
                    } catch (Exception e) {
                        Log.e("URL Parsing Error", "Error parsing URL: " + urlString);
                        e.printStackTrace();
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    // Set link color
                    ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.m_color));
                    ds.setUnderlineText(true);
                }
            };
            spannable.setSpan(clickableSpan, meetings.getMtype_val().indexOf(meetings.getMtype_val()), meetings.getMtype_val().indexOf(meetings.getMtype_val()) + meetings.getMtype_val().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            location.setText(spannable);
            location.setMaxLines(3);
            location.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if (meetings.getMtype().equals("3")) {
            location.setText(meetings.getMtype_val());
        }


        if (meetings.getDesc().equals("")) {
            title1.setVisibility(GONE);
            cardview_note.setVisibility(GONE);
        } else {
            title1.setVisibility(View.VISIBLE);
            cardview_note.setVisibility(View.VISIBLE);
            description.setText("   " + meetings.getDesc());
        }
        cabin.setText(meetings.getMeetingrooms().getName());

        //original time
        String s_time = Conversions.millitotime((meetings.getStart() + Conversions.timezone()) * 1000, false);
        String e_time = Conversions.millitotime((meetings.getEnd() + 1 + Conversions.timezone() + 1) * 1000, false);
        String date1 = Conversions.millitodateD((meetings.getStart() + Conversions.timezone()) * 1000);

        //rechedule time
        String r_s_time = Conversions.millitotime((meetings.getHistory().get(0).getStart() + Conversions.timezone()) * 1000, false);
        String r_e_time = Conversions.millitotime((meetings.getHistory().get(0).getEnd() + 1 + Conversions.timezone()) * 1000, false);
        String r_date1 = Conversions.millitodateD((meetings.getHistory().get(0).getDate() + Conversions.timezone()) * 1000);

        if (s_time.equalsIgnoreCase(r_s_time)) {
            main_time_date.setVisibility(VISIBLE);
            date.setText(r_date1 + " " + r_s_time + " - " + r_e_time);
            linear_original_suggested_time.setVisibility(GONE);
        } else {
            main_time_date.setVisibility(GONE);
            linear_original_suggested_time.setVisibility(View.VISIBLE);
            date.setVisibility(GONE);
            o_time.setText(r_date1 + ",  " + r_s_time + " to " + r_e_time);
            r_time.setText(date1 + ", " + s_time + " to " + e_time);
        }

        //invitees more
        invitee_count.setText("" + meetings.getInvites().size());
        if (meetings.getInvites().size() > 2) {
            txt_view_more.setVisibility(VISIBLE);
        } else {
            txt_view_more.setVisibility(GONE);
        }

        //invitees list
        //assign value showing
        for (int i = 0; i < meetings.getInvites().size(); i++) {
            if (meetings.getInvites().get(i).isAssign()) {
                assignedId = meetings.getInvites().get(i).getId();
                assignedto.setVisibility(View.VISIBLE);
                assignedto.setText(getString(R.string.assignedto) + ": " + meetings.getInvites().get(i).getName());
                break;
            }
        }
        recyclerViewinvites.setHasFixedSize(true);
        recyclerViewinvites.setLayoutManager(new GridLayoutManager(MeetingDescriptionNewActivity.this, 2));
        invitesAdapter = new InvitesAdapter1(MeetingDescriptionNewActivity.this, meetings.getInvites());
        recyclerViewinvites.setAdapter(invitesAdapter);
//        if(!meetings.getInvites().get(0).getId().equals("")){
//            getEmployeeDetails(meetings.getInvites().get(0).getId());
//        }
//        else{
//             email.setText(meetings.getInvites().get(0).getEmail());
//        }
        //agendas list
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MeetingDescriptionNewActivity.this);
        agendaRecyclerView.setLayoutManager(linearLayoutManager1);
        if (meetings.getAgenda() != null && meetings.getAgenda().size() != 0) {
            ArrayList<Agenda> agendaList =  new ArrayList<>();
            agendaList.clear();
            for (int i = 0; i < meetings.getAgenda().size(); i++) {
                if (meetings.getAgenda().get(i).getStatus().equals("0.0")){
                    agendaList.add(meetings.getAgenda().get(i));
                    agendaRecyclerView.setVisibility(VISIBLE);
                    agendaText.setVisibility(VISIBLE);
                }
            }
            agendaAdapter = new MeetingDetailsAgendaAdapter(MeetingDescriptionNewActivity.this, agendaList);
            agendaRecyclerView.setAdapter(agendaAdapter);
        }

        //read card
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        try {
            jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
            jsonObj_.put("formtype", meetings.getSupertype());
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


    }

    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent();
        setResult(RESULT_CODE, intent2);
        finish();
    }


}