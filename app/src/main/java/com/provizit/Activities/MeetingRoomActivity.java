package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.provizit.Calendar.CalendarAdapter;
import com.provizit.Calendar.MyCalendar;
import com.provizit.Calendar.RecyclerTouchListener;
import com.provizit.Calendar.myCalendarData;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CustomAdapter;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.weekview.DateTimeInterpreter;
import com.provizit.Utilities.weekview.MonthLoader;
import com.provizit.Utilities.weekview.WeekView;
import com.provizit.Utilities.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.provizit.Calendar.CalendarAdapter.defaultSelected;

public class MeetingRoomActivity extends AppCompatActivity implements WeekView.EventClickListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    private WeekView mWeekView;
    private ArrayList<WeekViewEvent> mNewEvents;
    private CalendarAdapter mAdapter;
    private ArrayList<String> calendar1 = new ArrayList<>();
    private List<MyCalendar> calendarList = new ArrayList<>();
    RecyclerView recyclerView1;
    RecyclerView.LayoutManager mLayoutManager;
    Long stamp;
    int position;
    Long toDay;
    TextView date;
    SharedPreferences sharedPreferences1;
    CustomAdapter customAdapter1;
    private SimpleDateFormat titledate = new SimpleDateFormat("EE, dd MMM yyyy");
    ArrayList<CompanyData> meetingrooms;
    AutoCompleteTextView meeting_room;
    EmpData empData;
    DatabaseHelper myDb;
    String m_room;
    Long s_time, Selecteddate;
    Long e_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_room);

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

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.mWeekView);
        recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view);
        date = findViewById(R.id.date);
        date.setText(titledate.format(Calendar.getInstance().getTime()));
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("subdjgbgjdsbjhdsgs");
                return true;
            }
        };
        empData = new EmpData();
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", 0);
        myDb = new DatabaseHelper(MeetingRoomActivity.this);
        empData = myDb.getEmpdata();
        meeting_room = findViewById(R.id.meetingroom_spinner);
        meeting_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                AnimationSet animation = Conversions.animation();
                arg0.startAnimation(animation);
                meeting_room.showDropDown();
            }
        });
        meeting_room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                CompanyData contactModel = meetingrooms.get(index);
                m_room = meetingrooms.get(index).get_id().get$oid();
//                getrmslots(m_room,s_time,e_time);
            }
        });

        toDay = (new myCalendarData(0).getTimeMilli()) - Conversions.timezone();
        Selecteddate = toDay;
        s_time = toDay;
        e_time = toDay + (24 * 60 * 60) - 1;
        mWeekView.setOnLongClickListener(longClickListener);
        mWeekView.setXScrollingSpeed(0);
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                try {
                    SimpleDateFormat titledate = new SimpleDateFormat("EE, dd MMM yyyy", new Locale(DataManger.appLanguage));
                    return titledate.format(date.getTime());
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return "";
                }
            }

            @Override
            public String interpretTime(int hour) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(11, hour);
                    calendar.set(12, 0);
                    SimpleDateFormat titledate = new SimpleDateFormat("hh:mm a", new Locale(DataManger.appLanguage));
                    return titledate.format(calendar.getTime());
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return "";
                }
            }
        });

// Set an action when any event is clicked.
//        mWeekView.setOnEventClickListener(mEventClickListener);
//
//// The week view has infinite scrolling horizontally. We have to provide the events of a
//// month every time the month changes on the week view.
//        mWeekView.setMonthChangeListener(mMonthChangeListener);
//

//// Set long press listener for events.
//        mWeekView.setEventLongPressListener(mEventLongPressListener);
        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
                ArrayList<WeekViewEvent> newEvents = getNewEvents(newYear, newMonth);
                events.addAll(newEvents);
                System.out.println("fklsdgj" + events.size());
                return events;
            }
        };

        mAdapter = new CalendarAdapter(calendarList, getWindowManager(), defaultSelected, MeetingRoomActivity.this);
        recyclerView1.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MeetingRoomActivity.this.getApplicationContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(mLayoutManager);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL);
//        itemDecoration.setDrawable(new ColorDrawable(R.color.hash));
////       recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
//        recyclerView1.addItemDecoration(itemDecoration);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int totalItemCount = mLayoutManager.getChildCount();

                        for (int i = 0; i < totalItemCount; i++) {

                            View childView = recyclerView.getChildAt(i);
                            TextView childTextView = (TextView) (childView.findViewById(R.id.stamp));
                            String childTextViewText = (String) (childTextView.getText());

                            if (i == 0) {
                                stamp = Long.parseLong(childTextViewText);
                            }
//                        if (childTextViewText.equals("Sun"))
//                            childTextView.setTextColor(Color.RED);
//                        else
//                            childTextView.setTextColor(Color.BLACK);
                        }
                    }
                });
        recyclerView1.setAdapter(mAdapter);
//        setCurrentItem(0,1);
        // row click listener

        recyclerView1.addOnItemTouchListener(new RecyclerTouchListener(MeetingRoomActivity.this.getApplicationContext(), recyclerView1, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TextView childTextView = (TextView) (view.findViewById(R.id.day_1));
                TextView childTextView1 = (TextView) (view.findViewById(R.id.date_1));

                MyCalendar calendar = calendarList.get(position);
                System.out.println(calendarList.size() + "shfjhjkashfajk");

                if (defaultSelected != position) {

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
                Animation startRotateAnimation = AnimationUtils.makeInChildBottomAnimation(MeetingRoomActivity.this.getApplicationContext().getApplicationContext());
                childTextView.startAnimation(startRotateAnimation);
                childTextView1.startAnimation(startRotateAnimation);
//                childTextView1.setBackgroundResource(R.drawable.ic_circle_white_8dp);
//             holder.tb_date.setBackgroundResource(R.drawable.ic_circle_white_8dp);
//                CalendarAdapter.defaultSelected = position;
                Calendar end = Calendar.getInstance();
                end.setTimeInMillis(calendar.getTimemilli() * 1000);
                Selecteddate = end.getTimeInMillis();
                Calendar day = Calendar.getInstance();
                day.set(Calendar.MILLISECOND, 0);
                day.set(Calendar.SECOND, 0);
                day.set(Calendar.MINUTE, 0);
                day.set(Calendar.HOUR_OF_DAY, 0);

                mWeekView.goToDate(end);


                s_time = end.getTimeInMillis() / 1000;
                e_time = toDay + (24 * 60 * 60) - 1;
//                getrmslots(m_room,s_time,e_time);

                System.out.println("Subhash " + end.getTime());


                if (calendar.getTimemilli() < (day.getTimeInMillis() / 1000)) {
                    end.add(Calendar.DAY_OF_MONTH, 1);
//                    up_label.setText(getResources().getString(R.string.past));
                } else {
                    end.add(Calendar.MONTH, 1);
//                    up_label.setText(getResources().getString(R.string.upcoming));
                }
                date.setText(titledate.format(calendar.getTimemilli() * 1000));

                if (calendar.getTimemilli() - Conversions.timezone() == toDay) {
                    System.out.println("stafsghfhga");
//                    getvisitorslist(calendar.getTimemilli()-Conversions.timezone()+"",(end.getTimeInMillis() / 1000)-Conversions.timezone()+"");
                } else {
                    System.out.println("gasgds");
//                    getmeetings(calendar.getTimemilli()-Conversions.timezone()+"",(end.getTimeInMillis() / 1000)-Conversions.timezone()+"");
                }
                defaultSelected = position;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {
//                MyCalendar calendar = calendarList.get(position);
//                TextView childTextView = (TextView) (view.findViewById(R.id.day_1));
//                childTextView.setTextColor(Color.GREEN);
//                Toast.makeText(getApplicationContext(), calendar.getDate()+"/" + calendar.getDay()+"/" +calendar.getMonth()+"   selected!", Toast.LENGTH_SHORT).show();

            }
        }));
        prepareCalendarData();
//        mWeekView.setOnEventClickListener(mEventClickListener);

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(mMonthChangeListener);
        mWeekView.setEmptyViewLongPressListener(this);

//        getmeetingrooms(empData.getLocation());
        // Initially, there will be no events on the week view because the user has not tapped on
        // it yet.
        mNewEvents = new ArrayList<WeekViewEvent>();
    }

    protected void registoreNetWorkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
    }

    private ArrayList<WeekViewEvent> getNewEvents(int year, int month) {
        System.out.println("sfbjfgahhfbmasbf");
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(Calendar.YEAR, year);
        startOfMonth.set(Calendar.MONTH, month - 1);
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        startOfMonth.set(Calendar.MINUTE, 0);
        startOfMonth.set(Calendar.SECOND, 0);
        startOfMonth.set(Calendar.MILLISECOND, 0);
        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth.set(Calendar.MINUTE, 59);
        endOfMonth.set(Calendar.SECOND, 59);

        ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : mNewEvents) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                events.add(event);
            }
        }
        return events;
    }

    private void prepareCalendarData() {

        myCalendarData m_calendar = new myCalendarData(-1);

        for (int i = 0; i <= 730; i++) {
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), m_calendar.getDay(), String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i, m_calendar.getTimeMilli());
            calendar1.add(m_calendar.calendar());
            m_calendar.getNextWeekDay(1);
            calendarList.add(calendar);
        }


        // notify adapter about data set changes
        // so that it will render the list with new data
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        position = calendar1.indexOf(formatter.format(calendar.getTime()));
        recyclerView1.scrollToPosition(position - 3);
        defaultSelected = position;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Calendar endTime = (Calendar) time.clone();
        endTime.add(Calendar.HOUR, 1);
        Boolean status = false;
        if (mNewEvents.size() != 0) {


            for (int i = 0; i < mNewEvents.size(); i++) {
                Long s1 = mNewEvents.get(i).getStartTime().getTimeInMillis();
                Long s2 = time.getTimeInMillis();
                Long e1 = mNewEvents.get(i).getEndTime().getTimeInMillis();
                Long e2 = endTime.getTimeInMillis();
                if (s1 != s2 && s2 != e1) {
                    if (s1 < s2 && s2 < e1) {
                        status = false;
                        break;
                    } else if (s1 > s2 && s1 < e2) {
                        status = false;
                        break;
                    } else {
                        status = true;
                    }
                }
            }
        } else {
            status = true;
        }
        int min = time.get(Calendar.MINUTE);
        if (min < 15) {
            time.set(Calendar.MINUTE, 15);
        } else if (min < 30) {
            time.set(Calendar.MINUTE, 30);
        } else if (min < 45) {
            time.set(Calendar.MINUTE, 45);
        } else {
            time.add(Calendar.HOUR, 1);
            time.set(Calendar.MINUTE, 0);
        }
        endTime = time;
        endTime.add(Calendar.HOUR, 1);
        System.out.println("sakhkjgh" + status);
        if (status) {
            WeekViewEvent event = new WeekViewEvent(20, "New event", time, endTime);
            mNewEvents.add(event);

            // Refresh the week view. onMonthChange will be called again.
            mWeekView.notifyDatasetChanged();
            long st = (time.getTimeInMillis() / 1000) - Conversions.timezone();
            long et = (endTime.getTimeInMillis() / 1000) - Conversions.timezone();
            System.out.println("asnmnbfas" + st);
            Intent intent = new Intent(MeetingRoomActivity.this, SetupMeetingActivity.class);
            intent.putExtra("RMS_STATUS", 1);
            intent.putExtra("RMS_Date", Selecteddate);
            intent.putExtra("RMS_Stime", st);
            intent.putExtra("RMS_Etime", et);
            intent.putExtra("RMS_Mroom", m_room);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
        // Create a new event.
    }

}