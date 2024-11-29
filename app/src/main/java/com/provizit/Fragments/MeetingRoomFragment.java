package com.provizit.Fragments;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.provizit.Activities.NavigationActivity;
import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.AdapterAndModel.MeetingRoom.MeetingRoomCalendarAdapter;
import com.provizit.Calendar.CalendarAdapter;
import com.provizit.Calendar.MyCalendar;
import com.provizit.Calendar.RecyclerTouchListener;
import com.provizit.Calendar.myCalendarData;
import com.provizit.Config.Preferences;
import com.provizit.Conversions;
import com.provizit.Logins.ForgotActivity;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model1;
import com.provizit.UserInterationListener;
import com.provizit.Utilities.CommonObject;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.CustomAdapter;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.MeetingRoomFragmentCustomAdapter;
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

public class MeetingRoomFragment extends Fragment implements  WeekView.EventClickListener, WeekView.EventLongPressListener,WeekView.EmptyViewLongPressListener, UserInterationListener {

    SwipeRefreshLayout refreshLayout;
    CardView card_view_progress;
    private WeekView mWeekView;
    private ArrayList<WeekViewEvent> mNewEvents;
    private MeetingRoomCalendarAdapter mAdapter;
    private ArrayList<String> calendar1= new ArrayList<>();
    private List<MyCalendar> calendarList= new ArrayList<>();
    RecyclerView recycler_view_calender;
    RecyclerView recycler_amenitiesView;
    RecyclerView.LayoutManager mLayoutManager;
    Long stamp;
    TextView m_room_info;
    LinearLayout linear_today;
    int position;
    Long toDay;
    SharedPreferences sharedPreferences1;
    MeetingRoomFragmentCustomAdapter customAdapter1;
    private SimpleDateFormat titledate = new SimpleDateFormat("EE, dd MMM yyyy",new Locale(DataManger.appLanguage));
    ArrayList<CompanyData> meetingrooms;
    AutoCompleteTextView meeting_room;
    EmpData empData;
    DatabaseHelper myDb;
    String m_room;
    int m_room1;
    Long s_time,Selecteddate;
    Long e_time;
    Calendar day;
    ArrayList<String> aString;
    ArrayList<CommonObject> amenities;
    private boolean isAmenitiesVisible = false;
    LinearLayout amenitiesView;
    int M_SUGGETION;
    private Rect rect;

    ApiViewModel apiViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.activity_meeting_room, container, false);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        inits(view);

        refreshLayout.setOnRefreshListener(() -> {
            inits(view);
            refreshLayout.setRefreshing(false);
        });

        return view;
    }

    private void inits(View view) {
        card_view_progress =  view.findViewById(R.id.card_view_progress);

        apiViewModel = new ViewModelProvider(getActivity()).get(ApiViewModel.class);

        ((NavigationActivity) getActivity()).setUserInteractionListener(this);
        mWeekView = (WeekView) view.findViewById(R.id.mWeekView);
        recycler_view_calender = (RecyclerView) view.findViewById(R.id.recycler_view_calender);
        amenitiesView =  view.findViewById(R.id.amenitiesView);
        m_room_info =  view.findViewById(R.id.m_room_info);
//        date.setText(titledate.format(Calendar.getInstance().getTime()));
        linear_today =  view.findViewById(R.id.linear_today);
        day = Calendar.getInstance();
        day.set(Calendar.MILLISECOND, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        recycler_amenitiesView = view.findViewById(R.id.recycler_amenitiesView);
        recycler_amenitiesView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("subdjgbgjdsbjhdsgs");
                return true;
            }
        };
        Intent intent = getActivity().getIntent();
        M_SUGGETION = intent.getIntExtra("M_SUGGETION",0);

        empData = new EmpData();
        sharedPreferences1 =  getActivity().getSharedPreferences("EGEMSS_DATA", 0);
        myDb = new DatabaseHelper(getActivity());
        empData = myDb.getEmpdata();
        amenities = new ArrayList<>();
        amenities = myDb.getCompanyAmenities();
        System.out.println("subhaadg "+ amenities.get(0).getName());
        meeting_room =  view.findViewById(R.id.meetingroom_spinner);
        meeting_room.setOnClickListener(arg0 -> {
            AnimationSet animationp = Conversions.animation();
            arg0.startAnimation(animationp);
            meeting_room.showDropDown();
        });
        meeting_room.setOnItemClickListener((parent, view12, index, id) -> {
            isAmenitiesVisible = false;
            AnimationSet animationp = Conversions.animation();
            view12.startAnimation(animationp);
            CompanyData contactModel = meetingrooms.get(index);
            m_room = meetingrooms.get(index).get_id().get$oid();
            m_room1 = index;
            getrmslots(m_room,s_time,e_time);
            aString = new ArrayList<>();
            aString.add("Capacity ("+meetingrooms.get(index).getCapacity()+")");
            ArrayList<Boolean> amenitiesB = new ArrayList<>();

            amenitiesB =  meetingrooms.get(index).getAmenities();
            for(int i = 0;i<amenitiesB.size();i++){
                System.out.println("subhash12345 "+ amenitiesB.get(i) + amenities.get(i).getName());
                if(amenitiesB.get(i) != null && amenitiesB.get(i)){
                    aString.add(amenities.get(i).getName());
                }
            }

            AmenitiesAdapter amenitiesAdapter = new AmenitiesAdapter(getActivity(), aString);
            recycler_amenitiesView.setAdapter(amenitiesAdapter);
            //amenitiesView.setVisibility(View.VISIBLE);
        });

        toDay = (new myCalendarData(0).getTimeMilli())-Conversions.timezone();

        Selecteddate = (toDay + Conversions.timezone()) * 1000;
        s_time = toDay;
        e_time = toDay + (24 * 60 * 60) - 1 ;

        mWeekView.setOnLongClickListener(longClickListener);
        mWeekView.setXScrollingSpeed(0);
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                try {
                    SimpleDateFormat titledate = new SimpleDateFormat("EE, dd MMM yyyy",new Locale(DataManger.appLanguage));
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
                    SimpleDateFormat titledate = new SimpleDateFormat("hh:mm a",new Locale(DataManger.appLanguage));
                    return titledate.format(calendar.getTime());
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return "";
                }
            }
        });

        MonthLoader.MonthChangeListener mMonthChangeListener = (newYear, newMonth) -> {
            // Populate the week view with some events.
            List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
            ArrayList<WeekViewEvent> newEvents = getNewEvents(newYear, newMonth);
            events.addAll(newEvents);
            System.out.println("fklsdgj"+ events.size());
            return events;
        };
        m_room_info.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);

            if (isAmenitiesVisible) {
                isAmenitiesVisible = false;
                amenitiesView.setVisibility(View.GONE);
            } else {
                isAmenitiesVisible = true;
                amenitiesView.setVisibility(View.VISIBLE);
            }
            //amenitiesView.setVisibility(View.VISIBLE);
            //m_room_info.setVisibility(View.GONE);
        });
        mAdapter = new MeetingRoomCalendarAdapter(calendarList,getActivity().getWindowManager(),defaultSelected,getActivity());
        recycler_view_calender.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_calender.setLayoutManager(mLayoutManager);

        recycler_view_calender.setItemAnimator(new DefaultItemAnimator());
        recycler_view_calender.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mLayoutManager.getChildCount();

                for (int i = 0; i < totalItemCount; i++){

                    View childView = recyclerView.getChildAt(i);
                    TextView childTextView = (TextView) (childView.findViewById(R.id.stamp));
                    String childTextViewText = (String) (childTextView.getText());

                    if(i==0){
                        stamp = Long.parseLong(childTextViewText);
                    }
                }
            }
        });
        recycler_view_calender.setAdapter(mAdapter);

        recycler_view_calender.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recycler_view_calender, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TextView childTextView = (TextView) (view.findViewById(R.id.day_1));
                TextView childTextView1 = (TextView) (view.findViewById(R.id.date_1));

                MyCalendar calendar = calendarList.get(position);
                System.out.println(calendarList.size()+"shfjhjkashfajk");

                if(defaultSelected != position){
                }
                int totalItemCount = mLayoutManager.getChildCount();
                for (int i = 0; i < totalItemCount; i++){
                    View view1 = mLayoutManager.getChildAt(i);
                    if (view1 != view){
                        TextView childTextView11 = (TextView) (view1.findViewById(R.id.day_1));
                        TextView childTextView12 = (TextView) (view1.findViewById(R.id.date_1));
                        childTextView12.setBackgroundResource(0);
                        childTextView11.setTextColor(Color.BLACK);
                        childTextView12.setTextColor(Color.BLACK);
                    }
                }
                Animation startRotateAnimation = AnimationUtils.makeInChildBottomAnimation(getActivity().getApplicationContext().getApplicationContext());
                childTextView.startAnimation(startRotateAnimation);
                childTextView1.startAnimation(startRotateAnimation);
//                childTextView1.setBackgroundResource(R.drawable.ic_circle_white_8dp);
//                holder.tb_date.setBackgroundResource(R.drawable.ic_circle_white_8dp);
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

                s_time = end.getTimeInMillis()/1000;
                e_time = toDay + (24 * 60 * 60) - 1 ;
                getrmslots(m_room,s_time,e_time);

                System.out.println("Subhash "+end.getTime());

                if(calendar.getTimemilli() < (day.getTimeInMillis() / 1000)){
                    end.add(Calendar.DAY_OF_MONTH,1);
//                    up_label.setText(getResources().getString(R.string.past));
                }
                else{
                    end.add(Calendar.MONTH,1);
//                    up_label.setText(getResources().getString(R.string.upcoming));
                }
                if(calendar.getTimemilli()- Conversions.timezone() == toDay){
                    System.out.println("stafsghfhga");
//                    getvisitorslist(calendar.getTimemilli()-Conversions.timezone()+"",(end.getTimeInMillis() / 1000)-Conversions.timezone()+"");
                }
                else{
                    System.out.println("gasgds");
//                    getmeetings(calendar.getTimemilli()-Conversions.timezone()+"",(end.getTimeInMillis() / 1000)-Conversions.timezone()+"");
                }
                defaultSelected = position;
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onLongClick(View view, int position) {
//                MyCalendar calendar = calendarList.get(position);
//                TextView childTextView = (TextView) (view.view.findViewById(R.id.day_1));
//                childTextView.setTextColor(Color.GREEN);
//                Toast.makeText(getApplicationContext(), calendar.getDate()+"/" + calendar.getDay()+"/" +calendar.getMonth()+"   selected!", Toast.LENGTH_SHORT).show();
            }
        }));
        prepareCalendarData();

        mWeekView.setMonthChangeListener(mMonthChangeListener);
        mWeekView.setEmptyViewLongPressListener(this);

        getmeetingrooms(empData.getLocation());

        mNewEvents = new ArrayList<WeekViewEvent>();
        linear_today.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            if(stamp < (day.getTimeInMillis() / 1000)){
                recycler_view_calender.scrollToPosition(position + 3);
            }
            else{
                recycler_view_calender.scrollToPosition(position - 3);
            }
            CalendarAdapter.defaultSelected = position;
            mAdapter.notifyDataSetChanged();
        });


        //getrmslots
        apiViewModel.getrmslots_response().observe(getActivity(), new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                card_view_progress.setVisibility(View.GONE);
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    }else if (statuscode.equals(not_verified)) {

                    }else if (statuscode.equals(successcode)) {
                        ArrayList<CompanyData> companyData = new ArrayList<>();
                        companyData = response.getItems();
                        mNewEvents = new ArrayList<>();
                        if(companyData != null && companyData.size() != 0){
                            for (int i = 0;i<companyData.size();i++) {
                                Calendar s1 = Calendar.getInstance();
                                s1.setTimeInMillis((companyData.get(i).getStart() + Conversions.timezone()) * 1000);
                                Calendar e1 = Calendar.getInstance();
                                e1.setTimeInMillis((companyData.get(i).getEnd() + Conversions.timezone()) * 1000);
                                WeekViewEvent event = new WeekViewEvent(companyData.get(i).getStart(), companyData.get(i).getSubject(), s1, e1);
                                System.out.println("sflslksdjg" + s1.getTime());
                                mNewEvents.add(event);

                                // Refresh the week view. onMonthChange will be called again.
                                mWeekView.notifyDatasetChanged();
                            }
                        }
                        else{
                            mWeekView.notifyDatasetChanged();
                        }
                    }

                }else {
                    Conversions.errroScreen(getActivity(), "getrmslots");
                }
            }
        });
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
    }
    private ArrayList<WeekViewEvent> getNewEvents(int year, int month) {
        // Get the starting point and ending point of the given month. We need this to find the
        // events of the given month.
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

        // Find the events that were added by tapping on empty view and that occurs in the given
        // time frame.
        ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : mNewEvents) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Calendar endTime = (Calendar) time.clone();
        endTime.add(Calendar.HOUR, 1);
        Boolean status = false;
        if(mNewEvents.size() != 0) {
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
        }
        else{
            status = true;
        }
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int min = time.get(Calendar.MINUTE);
        System.out.println("askj"+min);
        System.out.println("askj"+hour);
        if (min < 15) {
            time.set(Calendar.MINUTE,15);
        } else if (min < 30) {
            time.set(Calendar.MINUTE,30);
        } else if (min < 45) {
            time.set(Calendar.MINUTE,45);
        } else {
            System.out.print("dsgklsdh");
            time.add(Calendar.HOUR, 1);
            time.set(Calendar.MINUTE,0);
        }
        endTime = (Calendar) time.clone();
        endTime.add(Calendar.HOUR,1);
        if(status){
            WeekViewEvent event = new WeekViewEvent(20, "New event", time, endTime);
            mNewEvents.add(event);
            // Refresh the week view. onMonthChange will be called again.
            mWeekView.notifyDatasetChanged();
            Intent intent = new Intent(getActivity(), SetupMeetingActivity.class);
            if(M_SUGGETION == 1 ){
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                editor1.putString("SetupMeetingMRoom",meeting_room.getText().toString());
                editor1.putString("SetupMeetingMRoomID",m_room);

                System.out.println("fragment "+m_room);
                editor1.apply();
                intent.putExtra("RMS_Date",Selecteddate);
                intent.putExtra("M_SUGGETION",2);
                intent.putExtra("RMS_Stime",(time.getTimeInMillis() / 1000) - Conversions.timezone());
                intent.putExtra("RMS_Etime",(endTime.getTimeInMillis() / 1000) - Conversions.timezone());
            }
            else {
                intent.putExtra("RMS_STATUS",1);
                intent.putExtra("RMS_Date",Selecteddate);
                intent.putExtra("RMS_Stime",(time.getTimeInMillis() / 1000) - Conversions.timezone());
                intent.putExtra("RMS_Etime",(endTime.getTimeInMillis() / 1000) - Conversions.timezone());
                intent.putExtra("RMS_Mroom",m_room1 + 1);
            }

            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

        }
    }
    private void prepareCalendarData() {

        // run a for loop for all the next 30 days of the current month starting today
        // initialize mycalendarData and get Instance
        // getnext to get next set of date etc.. which can be used to populate MyCalendarList in for loop

        myCalendarData m_calendar = new myCalendarData(-1);

        for ( int i=0; i <=730; i++) {
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(),  m_calendar.getDay(), String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()),i,m_calendar.getTimeMilli());
            calendar1.add(m_calendar.calendar());
            m_calendar.getNextWeekDay(1);
            calendarList.add(calendar);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        position = calendar1.indexOf(formatter.format(calendar.getTime()));
        recycler_view_calender.scrollToPosition(position-3);
        defaultSelected = position;
        mAdapter.notifyDataSetChanged();
    }

    private void getmeetingrooms(String locationId) {

        //getrmslots
        apiViewModel.getmeetingrooms(getActivity(), locationId);
        card_view_progress.setVisibility(VISIBLE);
        //getrmslots
        apiViewModel.getmeetingrooms_response().observe(getActivity(), new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                card_view_progress.setVisibility(View.GONE);
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 201, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    }else if (statuscode.equals(not_verified)) {

                    }else if (statuscode.equals(successcode)) {
                        meetingrooms = new ArrayList<>();
                        meetingrooms.clear();
                        if (getActivity()!=null) {

                            ArrayList<CompanyData> FilterMeetingrooms = new ArrayList<>();
                            FilterMeetingrooms = response.getItems();

                            for (int j = 0; j < FilterMeetingrooms.size(); j++) {
                                if (FilterMeetingrooms.get(j).getActive().equals(true)){

                                    String trd_access = Preferences.loadStringValue(getActivity(), Preferences.trd_access, "");
                                    if (trd_access.equals("true")){
                                        meetingrooms.add(FilterMeetingrooms.get(j));
                                    }else {
                                        if (FilterMeetingrooms.get(j).getActive().equals(true) && FilterMeetingrooms.get(j).getTrd_access().equals(false)){
                                           meetingrooms.add(FilterMeetingrooms.get(j));
                                        }
                                    }
                                }
                            }

                            if (meetingrooms.size() > 0) {
                                customAdapter1 = new MeetingRoomFragmentCustomAdapter(getActivity(), R.layout.row, R.id.lbl_name, meetingrooms, 0, "",true);
                                meeting_room.setText(meetingrooms.get(0).getName());
                                m_room = meetingrooms.get(0).get_id().get$oid();
                                m_room1 = 0;
                                if (M_SUGGETION == 1){
                                    m_room = sharedPreferences1.getString("SetupMeetingMRoomID","");
                                    meeting_room.setText(sharedPreferences1.getString("SetupMeetingMRoom",""));
                                }
                                aString = new ArrayList<>();
                                aString.add("Capacity ("+meetingrooms.get(0).getCapacity()+")");
                                ArrayList<Boolean> amenitiesB = new ArrayList<>();
                                amenitiesB =  meetingrooms.get(0).getAmenities();
                                for(int i = 0;i<amenitiesB.size();i++){
                                    if(amenitiesB.get(i) != null && amenitiesB.get(i)){
                                        aString.add(amenities.get(i).getName());
                                    }
                                }
                                AmenitiesAdapter amenitiesAdapter = new AmenitiesAdapter(getActivity(), aString);
                                recycler_amenitiesView.setAdapter(amenitiesAdapter);
                                meeting_room.setThreshold(1);//will start working from first character
                                meeting_room.setAdapter(customAdapter1);//setting the adapter data into the AutoCompleteTextView
                                meeting_room.setEnabled(true);
                                getrmslots(m_room,s_time,e_time);
                            } else {
                                meeting_room.setText("No rooms available");
                            }

                        }
                    }

                }else {
                    Conversions.errroScreen(getActivity(), "getmeetingrooms");
                }
            }
        });

    }

    private void getrmslots(String rm_id,Long s_time,Long e_time) {

        //getrmslots
        apiViewModel.getrmslots(getActivity(), "rms", s_time, e_time, rm_id);
        card_view_progress.setVisibility(VISIBLE);
    }

    @Override
    public void onUserInteraction() {
        amenitiesView.setVisibility(View.GONE);
        m_room_info.setVisibility(View.VISIBLE);
        System.out.println("sujksafhjhf");
    }

    public class AmenitiesAdapter extends RecyclerView.Adapter< AmenitiesAdapter.MyviewHolder> {
        Context context;
        ArrayList<String> Amenities;
        public class MyviewHolder extends RecyclerView.ViewHolder {

            //TextView Name, location, time;
            TextView amenities;
            //View change_color;

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
        public   AmenitiesAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.amenities, parent, false);
            return new AmenitiesAdapter.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder( AmenitiesAdapter.MyviewHolder holder, final int position) {
                holder.amenities.setText(Amenities.get(position));
        }

        @Override
        public int getItemCount() {
            return Amenities.size();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getrmslots(m_room,s_time,e_time);
    }

}