package com.provizit.FragmentDailouge;

import static com.provizit.FragmentDailouge.ReccurenceFragment.getListMonths;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.provizit.Conversions;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Utilities.RangeTimePickerDialog;
import com.provizit.Utilities.daysview.DayModel;
import com.provizit.Utilities.daysview.DaysAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReccuringNewFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetSearchDialogTheme);
    }

    ItemSelected listner;

    public void onItemsSelectedListner(ItemSelected listner) {
        this.listner = listner;
    }

    private static Locale locale;

    String selectionType = "1";

    RecyclerView recyclerView;

    //group radio
    RadioGroup radio_group;
    RadioButton radio_daily;
    RadioButton radio_weekly;
    RadioButton radio_monthly;

    //date
    Button bt_from_date;
    Button bt_to_date;
    //time
    Button bt_time;
    Button bt_duration;

    //daily and weekly, mothly
    RelativeLayout relative_daily;
    LinearLayout linear_monthly;

    TextView txt_occurse;
    LocalDate Local_startDate = null;
    LocalDate Local_endDate = null;
    Calendar from_c;
    Calendar to_c;
    long from_date_mills = 0;
    long to_date_mills = 0;

    //time picker
    int SelectedHour, SelectedMin;
    String SelectedDate, SelectedSTime;
    public static long s_time, e_time, s_date;
    int d_hour = 0, d_min = 0, s_hour, s_min;


    //days
    private ArrayList<DayModel> daysAndWeeksList;
    String todayDay = "";
    //month
    RadioButton radio_on_day, radio_on_the_first;

    Button bt_update;

    //date stamps adrray
    List<Long> dates_stamps;
    int number_of_occurrences_count;
    //from
    String from_day = "";
    String from_month = "";
    String from_year = "";
    //to
    String to_day = "";
    String to_month = "";
    String to_year = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reccuring_new, container, false);

        radio_group = view.findViewById(R.id.radio_group);
        radio_daily = view.findViewById(R.id.radio_daily);
        radio_weekly = view.findViewById(R.id.radio_weekly);
        radio_monthly = view.findViewById(R.id.radio_monthly);
        radio_on_day = view.findViewById(R.id.radio_on_day);
        radio_on_the_first = view.findViewById(R.id.radio_on_the_first);

        //date time
        bt_from_date = view.findViewById(R.id.bt_from_date);
        bt_to_date = view.findViewById(R.id.bt_to_date);
        bt_time = view.findViewById(R.id.bt_time);
        bt_duration = view.findViewById(R.id.bt_duration);

        recyclerView = view.findViewById(R.id.recycler_view);
        relative_daily = view.findViewById(R.id.relative_daily);
        linear_monthly = view.findViewById(R.id.linear_monthly);
        txt_occurse = view.findViewById(R.id.txt_occurse);
        bt_update = view.findViewById(R.id.bt_update);

        radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            if (radio_daily.isChecked()) {
                selectionType = "1";
                relative_daily.setVisibility(View.VISIBLE);
                linear_monthly.setVisibility(View.GONE);
                days();
                set_datestamp();
            } else if (radio_weekly.isChecked()) {
                selectionType = "2";
                relative_daily.setVisibility(View.VISIBLE);
                linear_monthly.setVisibility(View.GONE);
                days();
                set_datestamp();
            } else if (radio_monthly.isChecked()) {
                selectionType = "3";
                relative_daily.setVisibility(View.GONE);
                linear_monthly.setVisibility(View.VISIBLE);
                set_datestamp();
            } else {
            }
        });


        locale = new Locale(DataManger.appLanguage);
        bt_duration.setText("1 hour : 00 min");
        if (DataManger.appLanguage.equals("ar")) {
            bt_duration.setText(Conversions.convertToArabic("1") + " ساعة" + " : " + Conversions.convertToArabic("00") + " دقيقة");
        }

        currentDate();
        days();

        bt_from_date.setOnClickListener(this);
        bt_to_date.setOnClickListener(this);
        bt_time.setOnClickListener(this);
        bt_duration.setOnClickListener(this);
        bt_update.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_from_date:
                AnimationSet animationSet = Conversions.animation();
                view.startAnimation(animationSet);
                fromDatePicker();
                break;
            case R.id.bt_to_date:
                AnimationSet animation = Conversions.animation();
                view.startAnimation(animation);
                toDatePicker();
                break;
            case R.id.bt_time:
                AnimationSet animations = Conversions.animation();
                view.startAnimation(animations);
                timePicker();
                break;
            case R.id.bt_duration:
                AnimationSet animationSet1 = Conversions.animation();
                view.startAnimation(animationSet1);
                duration();
                break;
            case R.id.bt_update:
                AnimationSet animationSet2 = Conversions.animation();
                view.startAnimation(animationSet2);

                if (bt_from_date.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Please Select From Date",Toast.LENGTH_SHORT).show();
                }else if (bt_to_date.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Please Select To Date",Toast.LENGTH_SHORT).show();
                }else {

                    if (number_of_occurrences_count >= 2){
                        if (selectionType.equalsIgnoreCase("1") || selectionType.equalsIgnoreCase("2")){

                            //daily or week
                            boolean atLeastOneTrue = false;
                            for (DayModel day : daysAndWeeksList) {
                                if (day.isSelected()) {
                                    atLeastOneTrue = true;
                                    break;
                                }
                            }

                            if (atLeastOneTrue){
                                ReccuringFunction();
                            }else {
                                Toast.makeText(getActivity(),"Select At least one week",Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            //month
                            ReccuringFunction();
                        }
                    }else {
                        Toast.makeText(getActivity(),"Select at least 2 occurrences",Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }
    }


    //auto get current date and time
    private void currentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //from
            Local_startDate = LocalDate.now();
            Date from_date = Date.from(Local_startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            from_c = Calendar.getInstance();
            from_c.setTime(from_date);
            int day = from_c.get(Calendar.DAY_OF_MONTH);
            int month = from_c.get(Calendar.MONTH);
            int year = from_c.get(Calendar.YEAR);


            //from date set
            String from_d = day + "-" + (month + 1) + "-" + year;
            from_date_mills = from_c.getTimeInMillis();
            SelectedDate = from_d;
            bt_from_date.setText(from_d);
            from_day = day+"";
            from_month = (month+1)+"";
            from_year = year+"";



            //to date
            Local_endDate = Local_startDate.plusMonths(1);
            //convert millseconds
            ZonedDateTime endDateTime = Local_endDate.atStartOfDay(ZoneId.systemDefault());
            long endMillis = endDateTime.toInstant().toEpochMilli();
            to_date_mills = endMillis;
            // Format end date as a string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
            String to_d = Local_endDate.format(formatter);
            from_c.set(year, month, day);
            // Separate date, month, and year
            String[] parts = to_d.split("-");
            to_day = parts[0];
            to_month = parts[1];
            to_year = parts[2];
            bt_to_date.setText(to_day+"-"+to_month+"-"+to_year);


            //day
            SimpleDateFormat sdf = new SimpleDateFormat("EEE");
            todayDay = sdf.format(from_c.getTime());


            //monthly
            SimpleDateFormat month_format = new SimpleDateFormat("MMM");
            radio_on_day.setText("On day: " + day);
            radio_on_the_first.setText("On the first: " + month_format.format(from_c.getTime()));

            //time
            bt_time.setText(getTimeStamp(true));


            //duration
             durationSet();


            //list of dates
            set_datestamp();
        }

    }



    //from date
    private void fromDatePicker() {
        from_c = Calendar.getInstance();
        int day = from_c.get(Calendar.DAY_OF_MONTH);
        int month = from_c.get(Calendar.MONTH);
        int year = from_c.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year1, monthOfYear, dayOfMonth) -> {

                    //from date set
                    String from_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    from_date_mills = from_c.getTimeInMillis();
                    bt_from_date.setText(from_date);
                    SelectedDate = from_date;
                    from_day = dayOfMonth+"";
                    from_month = (monthOfYear+1)+"";
                    from_year = year1+"";


                    //to date
                    bt_to_date.setText("");
                    //picker start date based on from date selection
                    from_c.set(year1, monthOfYear, dayOfMonth);
//                    from_date_mills = from_c.getTimeInMillis();

                    //day
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE");
                    todayDay = sdf.format(from_c.getTime());
                    days();

                    //monthly
                    SimpleDateFormat month_format = new SimpleDateFormat("MMM");
                    radio_on_day.setText("On day: " + day);
                    radio_on_the_first.setText("On the first: " + month_format.format(from_c.getTime()));

                    //duration
                    durationSet();

                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        datePickerDialog.show();
    }


    //to date
    private void toDatePicker() {

        if (bt_to_date.getText().toString().equalsIgnoreCase("")){
            // Set minimum date based on provided year, month, and day
            Calendar minDateCalendar = Calendar.getInstance();
            minDateCalendar.set(Integer.parseInt(from_year), Integer.parseInt(from_month) - 1, Integer.parseInt(from_day)); // Month is 0-indexed

// Set up the date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (view, year1, monthOfYear, dayOfMonth) -> {

                        String to_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                        to_date_mills = minDateCalendar.getTimeInMillis();
                        bt_to_date.setText(to_date);
                        to_day = String.valueOf(dayOfMonth);
                        to_month = String.valueOf(monthOfYear + 1);
                        to_year = String.valueOf(year1);

                        // list of dates
                        set_datestamp();

                    }, Integer.parseInt(from_year), Integer.parseInt(from_month) - 1, Integer.parseInt(from_day));

// Set minimum date in date picker
            datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTimeInMillis());

// Show the date picker dialog
            datePickerDialog.show();

        }else {
            Calendar from_c = Calendar.getInstance();
            from_c.set(Integer.parseInt(from_year), Integer.parseInt(from_month), Integer.parseInt(from_day));
            Calendar to_c = Calendar.getInstance();
            to_c.setTimeInMillis(from_c.getTimeInMillis());
            to_c.add(Calendar.MONTH, 1);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String to_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                        to_date_mills = to_c.getTimeInMillis();
                        bt_to_date.setText(to_date);
                        to_day = dayOfMonth + "";
                        to_month = (monthOfYear + 1) + "";
                        to_year = year1 + "";

                        // List of dates
                        set_datestamp();

                    }, Integer.parseInt(from_year), Integer.parseInt(from_month), Integer.parseInt(from_day)); // Use the selected year, month, and day

            datePickerDialog.getDatePicker().setMinDate(from_date_mills - 1000);
            datePickerDialog.show();
        }

    }

    private void set_datestamp() {
        dates_stamps = new ArrayList<Long>();
        days_stamps();
        if (selectionType.equalsIgnoreCase("1")){
            //daily
            List<Date> dates = new ArrayList<Date>();
            String str_date =from_day+"/"+from_month+"/"+from_year;
            String end_date =to_day+"/"+to_month+"/"+to_year;

            //setDaily_days();

            try {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date  startDate = (Date)formatter.parse(str_date);
                Date  endDate = (Date)formatter.parse(end_date);
                long interval = 24*1000 * 60 * 60; // 1 hour in millis
                long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
                long curTime = startDate.getTime();
                while (curTime <= endTime) {
                    dates.add(new Date(curTime));
                    curTime += interval;
                }
                number_of_occurrences_count = 0;
                for(int i=0;i<dates.size();i++){
                    number_of_occurrences_count= i + 1;
                }
                txt_occurse.setText(getResources().getString(R.string.Number_of_occurrences)+ number_of_occurrences_count);
            }catch (ParseException e) {
                e.printStackTrace();
            }
        }else if (selectionType.equalsIgnoreCase("2")){
            //weekly
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.set(Integer.parseInt(from_year), Integer.parseInt(from_month), Integer.parseInt(from_day));
            calendar2.set(Integer.parseInt(to_year), Integer.parseInt(to_month), Integer.parseInt(to_day));

            long weeks = calendar2.get(Calendar.WEEK_OF_YEAR) - calendar1.get(Calendar.WEEK_OF_YEAR);

            // Create a list to store the weeks and stamps
            List<Long> weeksList = new ArrayList<>();

            // Add the weeks and stamps to the lists
            number_of_occurrences_count = 0;
            for (int i = 0; i <= weeks; i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.WEEK_OF_YEAR, calendar1.get(Calendar.WEEK_OF_YEAR) + i);
                weeksList.add((long) calendar.get(Calendar.WEEK_OF_YEAR));
                Log.e("dates_stamps",dates_stamps+"" );
                number_of_occurrences_count= i + 1;
            }
            txt_occurse.setText(getResources().getString(R.string.Number_of_occurrences)+ number_of_occurrences_count);

        }else if (selectionType.equalsIgnoreCase("3")){
            //from_year+"-"+from_month+"-"+from_day
            //to_year+"-"+to_month+"-"+to_day

            //monthly
            try {
                DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date dateFrom = df1.parse(from_month+"/"+from_day+"/"+from_year);
                Date dateTo = df1.parse(to_month+"/"+to_day+"/"+to_year);
                final Locale locale = Locale.US;

                DateFormat df2 = new SimpleDateFormat("MMM-yyyy", Locale.US);
                List<String> months = getListMonths(dateFrom, dateTo, locale, df2);
                number_of_occurrences_count = 0;
                for(int i=0;i<months.size();i++){
                    number_of_occurrences_count= i + 1;
                }
                txt_occurse.setText(getResources().getString(R.string.Number_of_occurrences)+ number_of_occurrences_count);

                for (String month : months){
                    Log.e( "months",month.toUpperCase(locale) );
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
        }
    }

    private void days_stamps() {
        List<Date> dates = new ArrayList<Date>();
        String str_date =from_day+"/"+from_month+"/"+from_year;
        String end_date =to_day+"/"+to_month+"/"+to_year;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = (Date) formatter.parse(str_date);
            Date endDate = (Date) formatter.parse(end_date);
            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                dates.add(new Date(curTime));
                curTime += interval;
            }
            number_of_occurrences_count = 0;
            for (int i = 0; i < dates.size(); i++) {
                Date lDate = (Date) dates.get(i);
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(lDate);
                long s = Conversions.getdatestamp(formattedDate);
                dates_stamps.add(s);
                Log.e( "dates_stamps" , dates_stamps+"");


                String ds = formatter.format(lDate);
                Log.e( "set_days:dsadas" , ds);
                number_of_occurrences_count = i + 1;
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }


    //get time
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

    //duration
    private void durationSet() {
        s_date = Conversions.getdatestamp(SelectedDate);
        SelectedSTime = getTimeStamp(false);
        s_time = Conversions.gettimestamp(SelectedDate, SelectedSTime);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(SelectedSTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        d_hour = 1;
        d_min = 0;
        from_c.setTime(d);
        from_c.add(Calendar.HOUR_OF_DAY, d_hour);
        from_c.add(Calendar.MINUTE, d_min);
        String newTime = df.format(from_c.getTime());
        s_hour = from_c.get(Calendar.HOUR_OF_DAY);
        s_min = from_c.get(Calendar.MINUTE);
        e_time = Conversions.gettimestamp(SelectedDate, newTime);
    }
    private void duration() {
        final int hour = 1;
        final int minute = 0;
        final RangeTimePickerDialog mTimePicker;

        mTimePicker = new RangeTimePickerDialog(getActivity(), (timePicker, selectedHour, selectedMinute) -> {
//                            e_time = Conversions.gettimestamp(SelectedDate,selectedHour + ":" + selectedMinute);
            d_hour = selectedHour;
            d_min = selectedMinute;
            if (DataManger.appLanguage.equals("ar")) {
                if (d_hour == 1) {
                    bt_duration.setText(Conversions.convertToArabic(selectedHour + "") + " ساعة" + " : " + Conversions.convertToArabic(selectedMinute + "") + " دقيقة");
                } else {
                    System.out.println("safhjk " + Conversions.convertToArabic(selectedHour + "") + " ساعات" + " : " + Conversions.convertToArabic(selectedMinute + "") + " الدقائق");
                    bt_duration.setText(Conversions.convertToArabic(selectedHour + "") + " ساعات" + " : " + Conversions.convertToArabic(selectedMinute + "") + " الدقائق");
                }
            } else {
                if (d_hour == 1) {
                    bt_duration.setText(selectedHour + " hour" + " : " + selectedMinute + " min");
                } else {
                    bt_duration.setText(selectedHour + " hours" + " : " + selectedMinute + " min");
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


    //time picker
    private void timePicker() {
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
        mTimePicker = new RangeTimePickerDialog(getActivity(), (timePicker, selectedHour, selectedMinute) -> {
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
            bt_time.setText(Conversions.millitotime((Conversions.gettimestamp(SelectedDate, Stime) + Conversions.timezone()) * 1000, false));
            cal.add(Calendar.HOUR_OF_DAY, d_hour);
            cal.add(Calendar.MINUTE, d_min);
            String newTime = df.format(cal.getTime());
            e_time = Conversions.gettimestamp(SelectedDate, newTime);
        }, Shour, Sminute, true);//true = 24 hour time
        mTimePicker.setTitle("Select Time");
//            mTimePicker.setMin(hour, minute);
        mTimePicker.show();
    }


    private void days() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        daysAndWeeksList = new ArrayList<>();

        if (selectionType.equalsIgnoreCase("1")) {
            // all values 'true'
            for (int i = 0; i < 7; i++) {
                daysAndWeeksList.add(new DayModel(true));
            }
        } else if (selectionType.equalsIgnoreCase("2")) {

            // all values 'false'
            for (int i = 0; i < 7; i++) {
                daysAndWeeksList.add(new DayModel(false));
            }

            // Update the day according to todayDay
            int index = -1;
            switch (todayDay) {
                case "Mon":
                    index = 0;
                    break;
                case "Tue":
                    index = 1;
                    break;
                case "Wed":
                    index = 2;
                    break;
                case "Thu":
                    index = 3;
                    break;
                case "Fri":
                    index = 4;
                    break;
                case "Sat":
                    index = 5;
                    break;
                case "Sun":
                    index = 6;
                    break;
            }
            if (index != -1) {
                // Update the value of the DayModel object at the corresponding index
                daysAndWeeksList.get(index).setSelected(true);
            }
        }

        //set adapter
        DaysAdapter adapter = new DaysAdapter(daysAndWeeksList, getActivity(), selectionType);
        recyclerView.setAdapter(adapter);

    }

    private void ReccuringFunction() {
        listner.onSelected("true",dates_stamps,daysAndWeeksList,selectionType, from_date_mills+"", to_date_mills+"", number_of_occurrences_count, bt_from_date.getText().toString(), bt_to_date.getText().toString(), d_hour, d_min, s_time, e_time, s_date);
        dismiss();
    }

    public interface ItemSelected {
        void onSelected(String recurrence, List<Long> dates_stamps, ArrayList<DayModel> daysAndWeeksList, String selectionType, String fromMillsSeconds, String toMillsSeconds, int occurrencesCount, String startDate, String endDate, int d_hour, int d_min, Long s_time, Long e_time, Long s_date);
    }


}