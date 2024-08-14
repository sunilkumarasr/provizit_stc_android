package com.provizit.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model1;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.RoleDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.provizit.compactcalendarview.CompactCalendarView;
import com.provizit.compactcalendarview.domain.Event;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.view.View.GONE;

public class SelectedDateMeetingsActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    CompactCalendarView compactCalendarView;
    String TAG = "Future";
    RelativeLayout calender;
    int status = 0;
    ShimmerFrameLayout shimmer_view_container;
    RecyclerView recyclerView;
    TextView month;
    SimpleDateFormat dateFormatForMonth, dateFormatForMonth1;
    ArrayList<CompanyData> meetings, meetings1, SelectedDateMeetings;
    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;
    Long SDate, EDate;
    int type = 1;
    long SDate1;
    long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
    SelectedDateAdapter meetingListAdapter;
    LinearLayout empty;
    FloatingActionButton fab;
    Long SelectedDate;

    ApiViewModel apiViewModel;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(SelectedDateMeetingsActivity.this,R.color.colorPrimary));
        setContentView(R.layout.activity_selected_date_meetings);


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


        apiViewModel = new ViewModelProvider(SelectedDateMeetingsActivity.this).get(ApiViewModel.class);

        myDb = new DatabaseHelper(this);
        empData = new EmpData();
        empData = myDb.getEmpdata();
        meetings = new ArrayList<>();
        meetings1 = new ArrayList<>();
        SelectedDateMeetings = new ArrayList<>();
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.recyclerView);
        empty = findViewById(R.id.empty);
        fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                Intent intent = new Intent(SelectedDateMeetingsActivity.this, SetupMeetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("RMS_STATUS",2);
                intent.putExtra("RMS_Date",SelectedDate);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        RoleDetails roledata = myDb.getRole();
        if (roledata.getSmeeting().equals("false")) {
            fab.setVisibility(GONE);
        }
        Calendar cal123 = Calendar.getInstance();
        TimeZone tz = cal123.getTimeZone();
//        Locale myLocale = new Locale("ar");
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", 0);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
//        compactCalendarView.setLocale(tz,myLocale);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectedDateMeetingsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class

        compactCalendarView.setUseThreeLetterAbbreviation(true);
        if(DataManger.appLanguage.equals("ar")){
            compactCalendarView.setIsRtl(true);
            String[]names = new String[7];
            names[0]= "الاثنين" ;
            names[1]= "الثلاثاء";
            names[2]= "الاربعاء";
            names[3]= "الخمیس";
            names[4]= "الجمعہ";
            names[5]= "السبت";
            names[6]= "الأحد";
            compactCalendarView.setDayColumnNames(names);
        }
        compactCalendarView.shouldSelectFirstDayOfMonthOnScroll(false);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);


        dateFormatForMonth = new SimpleDateFormat("E, dd MMM yyyy", new Locale(DataManger.appLanguage));
        dateFormatForMonth1 = new SimpleDateFormat("MMM yyyy", new Locale(DataManger.appLanguage));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        month = findViewById(R.id.month);
        month.setText(dateFormatForMonth.format(new Date().getTime()));

        Calendar cal = Calendar.getInstance();
        int year1 = cal.get(Calendar.YEAR);
        int month1 = cal.get(Calendar.MONTH);
        int date1 = cal.get(Calendar.DATE);
        cal.clear();
        cal.clear();
        cal.set(year1, month1, date1);
        SDate1 = cal.getTimeInMillis() / 1000;

        Calendar c = Calendar.getInstance();
        c.setTime(compactCalendarView.getFirstDayOfCurrentMonth());
        SDate = c.getTimeInMillis() / 1000;
        try {
            EDate = lastDayOfMonth(compactCalendarView.getFirstDayOfCurrentMonth());
            type = 1;
            getmeetings(SDate, (EDate/1000) + (86400 * 2));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        type = 2;
        Date E = new Date(compactCalendarView.getFirstDayOfCurrentMonth().getTime() + MILLIS_IN_A_DAY);
        c.setTime(E);
        Long EDate1 = c.getTimeInMillis() / 1000;
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                status = 0;
                type = 2;
                System.out.println("subahsdbj");
                Calendar c = Calendar.getInstance();
                c.setTime(dateClicked);
                Long SDate1 = c.getTimeInMillis() / 1000;
                Long Edate = SDate1 + (60 * 60 * 24 * 1);
                SelectedDate = SDate1 - Conversions.timezone();

                SelectedDateMeetings = new ArrayList<>();
                for (int i = 0; i < meetings.size(); i++) {
                    System.out.println("SDate " + (SDate1 - Conversions.timezone()));
                    System.out.println("EDate " + (Edate - Conversions.timezone()));
                    System.out.println("MDate " + meetings.get(i).getDate());

                    if ((meetings.get(i).getDate() >= (SDate1 - Conversions.timezone())) && (meetings.get(i).getDate() < (Edate - Conversions.timezone()))) {
                        SelectedDateMeetings.add(meetings.get(i));
                    }
                }
                meetingListAdapter = new SelectedDateAdapter(SelectedDateMeetingsActivity.this, SelectedDateMeetings);
                recyclerView.setAdapter(meetingListAdapter);
                if (SelectedDateMeetings.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    empty.setVisibility(View.GONE);
                }
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                int selecte_date = dateClicked.getDate();
                month.setText(dateFormatForMonth.format(dateClicked));
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                month.setText(dateFormatForMonth1.format(firstDayOfNewMonth));
                type = 1;
                compactCalendarView.removeAllEvents();
                Calendar c = Calendar.getInstance();
                c.setTime(firstDayOfNewMonth);
                SDate = c.getTimeInMillis() / 1000;
                try {
                    EDate = lastDayOfMonth(firstDayOfNewMonth);
                    getmeetings(SDate, (EDate/1000) + 86400 );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        apiViewModel.getmeetingapprovals_response().observe(SelectedDateMeetingsActivity.this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                shimmer_view_container.stopShimmerAnimation();
                shimmer_view_container.setVisibility(View.GONE);
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)){

                    }else if(statuscode.equals(not_verified)){

                    }else if (statuscode.equals(successcode)){
                        meetings1 = response.getItems();
                        meetings.addAll(meetings1);

                        for (int i = 0; i < meetings1.size(); i++) {
                            Event ev1 = new Event(getResources().getColor(R.color.a_color), (meetings1.get(i).getStart() + Conversions.timezone()) * 1000);
                            compactCalendarView.addEvent(ev1);
                        }
                        meetings = insertionSort(meetings);

                        System.out.println("Subhasdh" + SDate1);
                        System.out.println("Subhasdh" + meetings.size());
                        Long Edate = SDate1 + (60 * 60 * 24 * 1);

                        SelectedDateMeetings = new ArrayList<>();
                        for (int i = 0; i < meetings.size(); i++) {
                            if ((meetings.get(i).getDate() >= (SDate1 - Conversions.timezone())) && (meetings.get(i).getDate() < (Edate - Conversions.timezone()))) {
                                SelectedDateMeetings.add(meetings.get(i));
                                System.out.println("Subhasdh" + meetings.get(i).get_id());
                            }
                        }
                        meetingListAdapter = new SelectedDateAdapter(SelectedDateMeetingsActivity.this, SelectedDateMeetings);
                        recyclerView.setAdapter(meetingListAdapter);
                        System.out.println("Subhasdh" + SelectedDateMeetings.size());
                        if (SelectedDateMeetings.size() == 0) {
                            empty.setVisibility(View.VISIBLE);
                        }else {
                            empty.setVisibility(View.GONE);
                        }
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

    public class SelectedDateAdapter extends RecyclerView.Adapter<SelectedDateAdapter.MyviewHolder> {
        Context context;
        ArrayList<CompanyData> meetigsArrayList;


        public class MyviewHolder extends RecyclerView.ViewHolder {
            //            TextView Name, location, time;
            TextView title_type,subject, host, count,s_time,date,mStatus;
            CircleImageView pic;
            RelativeLayout mStatusColor;
//            View change_color;

            public MyviewHolder(View view) {
                super(view);

                date = view.findViewById(R.id.date);
                s_time = view.findViewById(R.id.s_time);
                pic = view.findViewById(R.id.pic);
                title_type = view.findViewById(R.id.title_type);
                subject = view.findViewById(R.id.subject);
                host = view.findViewById(R.id.host);
                count = view.findViewById(R.id.count);
                mStatus = itemView.findViewById(R.id.mStatus);
                mStatusColor = itemView.findViewById(R.id.mStatusColor);
            }
        }

        public SelectedDateAdapter(Context mContext, ArrayList<CompanyData> meetigsArrayList) {
            this.context = mContext;
            this.meetigsArrayList = meetigsArrayList;
        }


        @Override
        public SelectedDateAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.selected_date_meetings_item, parent, false);
            return new SelectedDateAdapter.MyviewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(SelectedDateAdapter.MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {
//            holder.Name.setText("Host: " + meetigsArrayList.get(position).getEmployee().getName());

            if (meetigsArrayList.get(position).getV_id() == null || meetigsArrayList.get(position).getV_id().equalsIgnoreCase("")){
                holder.title_type.setText("Meeting");
            }else {
                holder.title_type.setText("Appointment");
            }


            if (meetigsArrayList.get(position).getEmployee().getPic() != null && meetigsArrayList.get(position).getEmployee().getPic().size() != 0) {
                Glide.with(SelectedDateMeetingsActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetigsArrayList.get(position).getEmployee().getPic().get(meetigsArrayList.get(position).getEmployee().getPic().size() - 1))
                        .into(holder.pic);
            }
            String s_time = Conversions.millitotime((meetigsArrayList.get(position).getStart()+Conversions.timezone()) * 1000,false);
            holder.s_time.setText(s_time);
            holder.subject.setText(Conversions.Capitalize(meetigsArrayList.get(position).getSubject()));
            holder.host.setText(" "+meetigsArrayList.get(position).getEmployee().getName());
            DateFormat simple = new SimpleDateFormat("dd MMM",new Locale(DataManger.appLanguage));
            Date result = new Date((meetigsArrayList.get(position).getStart()+Conversions.timezone()) * 1000);
            String time = simple.format(result) + "";
            holder.date.setText(time);
//            holder.time.setText(s_time);
//            holder.location.setText(meetigsArrayList.get(position).getRoom().getName());
            if (empData.getEmp_id().equals(meetigsArrayList.get(position).getEmp_id())) {
                holder.host.setText(" Me");
                if (meetigsArrayList.get(position).getInvites().get(0).getPic().size() != 0) {
                    Glide.with(SelectedDateMeetingsActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetigsArrayList.get(position).getInvites().get(0).getPic().get(meetigsArrayList.get(position).getInvites().get(0).getPic().size() - 1))
                            .into(holder.pic);
                }
            }
            else {
                if (meetigsArrayList.get(position).getEmployee().getPics().size() != 0) {
                    Glide.with(SelectedDateMeetingsActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetigsArrayList.get(position).getEmployee().getPics().get(meetigsArrayList.get(position).getEmployee().getPics().size() - 1))
                            .into(holder.pic);
                }
            }
            if(meetigsArrayList.get(position).getInvites().size() > 1){
                holder.count.setVisibility(View.VISIBLE);
                holder.count.setText("+"+ (meetigsArrayList.get(position).getInvites().size()-1));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SelectedDateMeetingsActivity.this, MeetingDescriptionNewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("m_id", meetigsArrayList.get(position).get_id().get$oid());
                    startActivity(intent);
                }
            });

            if (meetigsArrayList.get(position).getStatus() == 1) {
                // Cancel
                holder.mStatus.setVisibility(View.VISIBLE);
                holder.mStatusColor.setVisibility(View.VISIBLE);
                holder.mStatus.setText("Cancelled");
                GradientDrawable drawable = (GradientDrawable) holder.mStatusColor.getBackground();
                drawable.setStroke(3, getResources().getColor(R.color.Cancel)); // set stroke width and stroke color
                holder.mStatus.setBackgroundColor(getResources().getColor(R.color.Cancel));
            } else {
                for (int i = 0; i < meetigsArrayList.get(position).getInvites().size(); i++) {
                    GradientDrawable drawable = (GradientDrawable) holder.mStatusColor.getBackground();
                    if (empData.getEmail().equals(meetigsArrayList.get(position).getInvites().get(i).getEmail())) {
                        // Pending
                        holder.mStatus.setVisibility(View.VISIBLE);
                        holder.mStatusColor.setVisibility(View.VISIBLE);
                        holder.mStatus.setBackgroundColor(getResources().getColor(R.color.Pending));
                        drawable.setStroke(3, getResources().getColor(R.color.Pending)); // set stroke width and stroke color
                        holder.mStatus.setText(R.string.pending);
                        if (meetigsArrayList.get(position).getInvites().get(i).getStatus() == 3) {
                            //Accepted
                            drawable.setStroke(3, getResources().getColor(R.color.Accept)); // set stroke width and stroke color
                            holder.mStatus.setBackgroundColor(getResources().getColor(R.color.Accept));
                            holder.mStatus.setText(R.string.accepted);

                        } else if (meetigsArrayList.get(position).getInvites().get(i).getStatus() == 1) {
                            //declined
                            drawable.setStroke(3, getResources().getColor(R.color.Decline)); // set stroke width and stroke color
                            holder.mStatus.setBackgroundColor(getResources().getColor(R.color.Decline));
                            holder.mStatus.setText(R.string.declined);
                        }
                        break;
                    } else {
                        holder.mStatus.setVisibility(GONE);
                        holder.mStatusColor.setVisibility(GONE);
                    }
                }
            }

//            holder.change_color.setBackgroundColor(getResources().getColor(R.color.r_color));
//            if (empData.getEmp_id().equals(meetigsArrayList.get(position).getEmp_id())) {
//                if (meetigsArrayList.get(position).getInvites().get(0).getName().equals("")) {
//                    holder.Name.setText(meetigsArrayList.get(position).getInvites().get(0).getEmail());
//                }
//                holder.Name.setText(meetigsArrayList.get(position).getInvites().get(0).getName());
//                holder.change_color.setBackgroundColor(getResources().getColor(R.color.m_color));
//                if (meetigsArrayList.get(position).getInvites().get(0).getPic().size() != 0) {
//                    Glide.with(SelectedDateMeetings.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + meetigsArrayList.get(position).getInvites().get(0).getPic().get(meetigsArrayList.get(position).getInvites().get(0).getPic().size() - 1))
//                            .into(holder.pic);
//                }
//
//            }
//            if (empData.getRoleid().equals(meetigsArrayList.get(position).getApprover_roleid())) {
//                holder.change_color.setBackgroundColor(getResources().getColor(R.color.a_color));
//            }

        }

        @Override
        public int getItemCount() {
            return meetigsArrayList.size();
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

    private void getmeetings(final Long SDate, final Long EDate) {

        apiViewModel.getmeetings(getApplicationContext(), "custom", empData.getEmp_id(), SDate + "", EDate + "");
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();

        apiViewModel.getmeetings_response().observe(SelectedDateMeetingsActivity.this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                shimmer_view_container.stopShimmerAnimation();
                shimmer_view_container.setVisibility(View.GONE);

                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)){

                    }else if(statuscode.equals(not_verified)){

                    }else if (statuscode.equals(successcode)){
                        meetings = response.getItems();
                        getmeetingrequests(SDate, EDate);
                        for (int i = 0; i < meetings.size(); i++) {
                            Event ev1 = new Event(getResources().getColor(R.color.m_color), (meetings.get(i).getStart() + Conversions.timezone()) * 1000);
                            compactCalendarView.addEvent(ev1);
                        }
                    }
                }
            }
        });

    }

    private void getmeetingrequests(Long SDate, Long EDate) {

        apiViewModel.getmeetingrequests(getApplicationContext(), "custom", empData.getEmail(), SDate + "", EDate + "");
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();

        apiViewModel.getmeetingrequests_response().observe(SelectedDateMeetingsActivity.this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                shimmer_view_container.stopShimmerAnimation();
                shimmer_view_container.setVisibility(View.GONE);

                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)){

                    }else if(statuscode.equals(not_verified)){

                    }else if (statuscode.equals(successcode)){
                        meetings1 = response.getItems();
                        meetings.addAll(meetings1);
                        getmeetingapprovals(SDate, EDate);
                        for (int i = 0; i < meetings1.size(); i++) {
                            Event ev1 = new Event(getResources().getColor(R.color.r_color), (meetings1.get(i).getStart() + Conversions.timezone()) * 1000);
                            compactCalendarView.addEvent(ev1);
                        }
                    }
                }
            }
        });

    }

    private void getmeetingapprovals(Long SDate, Long EDate) {

        apiViewModel.getmeetingapprovals(getApplicationContext(),"custom", empData.getEmp_id(), empData.getRoleid(), empData.getLocation(), empData.getHierarchy_id(), SDate + "", EDate + "");
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();

    }

    private Long lastDayOfMonth(Date date) throws ParseException {
//         String date = "01/07/2020";
//         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//         Date convertedDate = dateFormat.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

        System.out.println("date00" + c.getTimeInMillis());
        return c.getTimeInMillis();
    }
    public ArrayList<CompanyData> insertionSort(ArrayList<CompanyData> meetingsA){
        ArrayList<CompanyData> meetingsSort = new ArrayList<>();
        meetingsSort = meetingsA;
        int  n = meetingsSort.size();
        int i, j;
        CompanyData key;
        for (i = 1; i < n; i++)
        {
            key = meetingsSort.get(i);
            j = i - 1;

        /* Move elements of arr[0..i-1], that are
        greater than key, to one position ahead
        of their current position */
            while (j >= 0 && meetingsSort.get(j).getStart() > key.getStart())
            {
                meetingsSort.set(j+1,meetingsSort.get(j)) ;
                j = j - 1;
            }
            meetingsSort.set(j+1,key);
        }
        return meetingsSort;
    }


}
