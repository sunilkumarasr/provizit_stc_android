package com.provizit.FragmentDailouge;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.provizit.Conversions;
import com.provizit.R;
import com.provizit.Utilities.DatabaseHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ReccurenceFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "ReccurenceFragment";

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;

    String recurrence = "";

    private FragmentActivity myContext;

    RadioGroup radio_group;
    RadioButton radio_daily;
    RadioButton radio_weekly;
    RadioButton radio_monthly;
    String Selection_type = "";

    //date stamps adrray
    List<Long> dates_stamps;

    //days
    LinearLayout linear_weekly;
    Button bt_mon;
    Button bt_tue;
    Button bt_wed;
    Button bt_thu;
    Button bt_fri;
    Button bt_sat;
    Button bt_sun;
    boolean t_mon =false;
    boolean t_tue =false;
    boolean t_web =false;
    boolean t_thu =false;
    boolean t_fri =false;
    boolean t_sat =false;
    boolean t_sun =false;
    ArrayList weekdays;

    int number_of_occurrences_count;


    TextView txt_number_of_occurrences;
    Button bt_clear;
    Button bt_update;


    //date pickers
    DatePickerDialog datePickerDialog;
    String date_selection = "";
    String to_date_selection = "";
    long from_date_mills = 0;
    long to_date_mills = 0;
    Button bt_from_date;
    Button bt_to_date;


    //date pickers
    //from
    String from_day = "";
    String from_month = "";
    String from_year = "";
    //to
    String to_day = "";
    String to_month = "";
    String to_year = "";

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style. BottomSheetSearchDialogTheme);
    }

    ReccurenceFragment.ItemSelected listner;
    public void onItemsSelectedListner(ReccurenceFragment.ItemSelected listner){
        this.listner = listner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recurrence = getArguments().getString("recurrence");
        Selection_type = getArguments().getString("recurrence_type");
        from_day = getArguments().getString("from_d");
        from_month = getArguments().getString("from_m");
        from_year = getArguments().getString("from_y");
        to_day = getArguments().getString("to_d");
        to_month = getArguments().getString("to_m");
        to_year = getArguments().getString("to_y");
        t_mon = getArguments().getBoolean("t_mon");
        t_tue = getArguments().getBoolean("t_tue");
        t_web = getArguments().getBoolean("t_web");
        t_thu = getArguments().getBoolean("t_thu");
        t_fri = getArguments().getBoolean("t_fri");
        t_sat = getArguments().getBoolean("t_sat");
        t_sun = getArguments().getBoolean("t_sun");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reccurence, container, false);
        myDb = new DatabaseHelper(getActivity());
        sharedPreferences1 = getActivity().getSharedPreferences("EGEMSS_DATA", 0);
        Log.e(TAG, "onCreateView:from_day "+t_mon );
        Log.e(TAG, "onCreateView:from_month "+from_month );


        radio_group = view.findViewById(R.id.radio_group);
        radio_daily = view.findViewById(R.id.radio_daily);
        radio_weekly = view.findViewById(R.id.radio_weekly);
        radio_monthly = view.findViewById(R.id.radio_monthly);
        linear_weekly = view.findViewById(R.id.linear_weekly);
        bt_from_date = view.findViewById(R.id.bt_from_date);
        bt_to_date = view.findViewById(R.id.bt_to_date);
        bt_mon = view.findViewById(R.id.bt_mon);
        bt_tue = view.findViewById(R.id.bt_tue);
        bt_wed = view.findViewById(R.id.bt_wed);
        bt_thu = view.findViewById(R.id.bt_thu);
        bt_fri = view.findViewById(R.id.bt_fri);
        bt_sat = view.findViewById(R.id.bt_sat);
        bt_sun = view.findViewById(R.id.bt_sun);
        txt_number_of_occurrences = view.findViewById(R.id.txt_number_of_occurrences);
        bt_clear = view.findViewById(R.id.bt_clear);
        bt_update = view.findViewById(R.id.bt_update);

        if (recurrence.equalsIgnoreCase("true")){
            set_data();
        }else {
            Selection_type = "1";
            bt_clear.setVisibility(View.GONE);
            current_date();
        }

        radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            if (radio_daily.isChecked()) {
                Selection_type = "1";
                linear_weekly.setVisibility(View.VISIBLE);
                set_datestamp();
            } else if (radio_weekly.isChecked()) {
                Selection_type = "2";
                linear_weekly.setVisibility(View.VISIBLE);
                set_datestamp();
            } else if (radio_monthly.isChecked()) {
                Selection_type = "3";
                linear_weekly.setVisibility(View.GONE);
                set_datestamp();
            }else {
            }
        });


        bt_from_date.setOnClickListener(this);
        bt_to_date.setOnClickListener(this);
        bt_mon.setOnClickListener(this);
        bt_tue.setOnClickListener(this);
        bt_wed.setOnClickListener(this);
        bt_thu.setOnClickListener(this);
        bt_fri.setOnClickListener(this);
        bt_sat.setOnClickListener(this);
        bt_sun.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_update.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_from_date:
                AnimationSet animationbt_from_date = Conversions.animation();
                v.startAnimation(animationbt_from_date);
//                FromDateTimeField();
                current_date();
                break;
            case R.id.bt_to_date:
                AnimationSet animationbt_to_date = Conversions.animation();
                v.startAnimation(animationbt_to_date);
                if (date_selection.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Please Select From Date First",Toast.LENGTH_SHORT).show();
                }else {
                    ToDateTimeField();
                }
                break;
            case R.id.bt_mon:
                AnimationSet animationbt_mon = Conversions.animation();
                v.startAnimation(animationbt_mon);
                if (bt_mon.isSelected()) {
                    t_mon =false;
                    bt_mon.setSelected(false);
                    bt_mon.setBackgroundResource(R.drawable.round_gray_without_edge);
                } else {
                    t_mon =true;
                    bt_mon.setSelected(true);
                    bt_mon.setBackgroundResource(R.drawable.round_primary_color_without_edge);
                }
                break;
            case R.id.bt_tue:
                AnimationSet animationbt_tue = Conversions.animation();
                v.startAnimation(animationbt_tue);
                if (bt_tue.isSelected()) {
                    t_tue = false;
                    bt_tue.setSelected(false);
                    bt_tue.setBackgroundResource(R.drawable.round_gray_without_edge);
                } else {
                    t_tue = true;
                    bt_tue.setSelected(true);
                    bt_tue.setBackgroundResource(R.drawable.round_primary_color_without_edge);
                }
                break;
            case R.id.bt_wed:
                AnimationSet animationbt_wed = Conversions.animation();
                v.startAnimation(animationbt_wed);
                if (bt_wed.isSelected()) {
                    t_web = false;
                    bt_wed.setSelected(false);
                    bt_wed.setBackgroundResource(R.drawable.round_gray_without_edge);
                } else {
                    t_web = true;
                    bt_wed.setSelected(true);
                    bt_wed.setBackgroundResource(R.drawable.round_primary_color_without_edge);
                }
                break;
            case R.id.bt_thu:
                AnimationSet animationbt_thu = Conversions.animation();
                v.startAnimation(animationbt_thu);
                if (bt_thu.isSelected()) {
                    t_thu = false;
                    bt_thu.setSelected(false);
                    bt_thu.setBackgroundResource(R.drawable.round_gray_without_edge);
                } else {
                    t_thu = true;
                    bt_thu.setSelected(true);
                    bt_thu.setBackgroundResource(R.drawable.round_primary_color_without_edge);
                }
                break;
            case R.id.bt_fri:
                AnimationSet animationbt_fri = Conversions.animation();
                v.startAnimation(animationbt_fri);
                if (bt_fri.isSelected()) {
                    t_fri = false;
                    bt_fri.setSelected(false);
                    bt_fri.setBackgroundResource(R.drawable.round_gray_without_edge);
                } else {
                    t_fri = true;
                    bt_fri.setSelected(true);
                    bt_fri.setBackgroundResource(R.drawable.round_primary_color_without_edge);
                }
                break;
            case R.id.bt_sat:
                AnimationSet animationbt_sat = Conversions.animation();
                v.startAnimation(animationbt_sat);
                if (bt_sat.isSelected()) {
                    t_sat = false;
                    bt_sat.setSelected(false);
                    bt_sat.setBackgroundResource(R.drawable.round_gray_without_edge);
                } else {
                    t_sat = true;
                    bt_sat.setSelected(true);
                    bt_sat.setBackgroundResource(R.drawable.round_primary_color_without_edge);
                }
                break;
            case R.id.bt_sun:
                AnimationSet animationbt_sun = Conversions.animation();
                v.startAnimation(animationbt_sun);
                if (bt_sun.isSelected()) {
                    t_sun = false;
                    bt_sun.setSelected(false);
                    bt_sun.setBackgroundResource(R.drawable.round_gray_without_edge);
                } else {
                    t_sun = true;
                    bt_sun.setSelected(true);
                    bt_sun.setBackgroundResource(R.drawable.round_primary_color_without_edge);
                }
                break;
            case R.id.bt_clear:
                AnimationSet animationbt_clear = Conversions.animation();
                v.startAnimation(animationbt_clear);
                listner.onSelected("false",dates_stamps,Selection_type,from_date_mills+"",to_date_mills+"","","",weekdays,from_day,from_month,from_year,to_day,to_month,to_year,t_mon,t_tue,t_web,t_thu,t_fri,t_sat,t_sun,number_of_occurrences_count,bt_from_date.getText().toString(),bt_to_date.getText().toString());
                dismiss();
                break;
            case R.id.bt_update:

                if (date_selection.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Please Select From Date",Toast.LENGTH_SHORT).show();
                }else if (to_date_selection.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Please Select To Date",Toast.LENGTH_SHORT).show();
                }else {
                    weekdays = new ArrayList();
                    weekdays.add(t_mon);
                    weekdays.add(t_tue);
                    weekdays.add(t_web);
                    weekdays.add(t_thu);
                    weekdays.add(t_fri);
                    weekdays.add(t_sat);
                    weekdays.add(t_sun);
                    Log.e(TAG, "onClick:weekdays"+weekdays );

                    if (Selection_type.equalsIgnoreCase("2") || Selection_type.equalsIgnoreCase("1")){
                        if (t_mon==true || t_tue==true || t_web==true || t_thu==true || t_fri==true || t_sat==true || t_sun==true){
                            Reccuring_fun();
                        }else {
                            Toast.makeText(getActivity(),"Please Select any Day",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Reccuring_fun();
                    }

                }
                break;
        }
    }

    private void set_data() {
        bt_clear.setVisibility(View.VISIBLE);
        selected_date();
        if (Selection_type.equalsIgnoreCase("1")){
            linear_weekly.setVisibility(View.VISIBLE);
            radio_daily.setChecked(true);
            radio_weekly.setChecked(false);
            radio_monthly.setChecked(false);
            set_datestamp();
        }else if (Selection_type.equalsIgnoreCase("2")){
            linear_weekly.setVisibility(View.VISIBLE);
            radio_weekly.setChecked(true);
            radio_daily.setChecked(false);
            radio_monthly.setChecked(false);
            set_datestamp();
            set_weekly();
        }else if (Selection_type.equalsIgnoreCase("3")){
            linear_weekly.setVisibility(View.GONE);
            radio_monthly.setChecked(true);
            radio_daily.setChecked(false);
            radio_weekly.setChecked(false);
            set_datestamp();
        }else {
        }
    }

    private void selected_date() {
        //from date
        String date_time = from_day + "-" + from_month+ "-" + from_year;
        from_date_mills = Conversions.getdatestamp(date_time);
        bt_from_date.setText(Conversions.millitodateLabel((from_date_mills + Conversions.timezone())*1000));
        date_selection = from_date_mills+"";

        //todate
        String date_t_time = to_day + "-" + to_month + "-" + to_year;
        to_date_mills = Conversions.getdatestamp(date_t_time);
        to_date_selection = to_date_mills+"";
        bt_to_date.setText(Conversions.millitodateLabel((to_date_mills + Conversions.timezone())*1000));
        Log.e(TAG, "onDateSet:to_date_mills "+to_date_mills );

        set_datestamp();
    }

    private void set_weekly() {
        if (t_mon==true){
            bt_mon.setSelected(true);
            bt_mon.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        }
        if (t_tue==true){
            bt_tue.setSelected(true);
            bt_tue.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        }
        if (t_web==true){
            bt_wed.setSelected(true);
            bt_wed.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        }
        if (t_thu==true){
            bt_thu.setSelected(true);
            bt_thu.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        }
        if (t_fri==true){
            bt_fri.setSelected(true);
            bt_fri.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        }
        if (t_sat==true){
            bt_sat.setSelected(true);
            bt_sat.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        }
        if (t_sun==true){
            bt_sun.setSelected(true);
            bt_sun.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        }
    }

    private void current_date() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        Log.e(TAG, "onDateSet:currentdate "+day+"-"+(month+1)+"-"+ year);

        //from date
        String date_time = day + "-" + (month+1) + "-" + year;
        from_date_mills = Conversions.getdatestamp(date_time);
        bt_from_date.setText(Conversions.millitodateLabel((from_date_mills + Conversions.timezone())*1000));
        date_selection = from_date_mills+"";
        from_day = day+"";
        from_month = (month+1)+"";
        from_year = year+"";

        //todate
        String date_t_time = day + "-" + (month+1) + "-" + year;
        to_date_mills = Conversions.getdatestamp(date_t_time);
        to_date_selection = to_date_mills+"";
        bt_to_date.setText(Conversions.millitodateLabel((to_date_mills + Conversions.timezone())*1000));
        Log.e(TAG, "onDateSet:to_date_mills "+to_date_mills );
        to_day = day+"";
        to_month = (month + 1)+"";
        to_year = year+"";
        set_datestamp();
    }

    private void FromDateTimeField() {
        Calendar From_c = Calendar.getInstance();
        int mYear = From_c.get(Calendar.YEAR);
        int mMonth = From_c.get(Calendar.MONTH);
        int mDay = From_c.get(Calendar.DAY_OF_MONTH);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        from_day = dayOfMonth+"";
                        from_month = (monthOfYear + 1)+"";
                        from_year = year+"";

                        String date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        from_date_mills = Conversions.getdatestamp(date_time);
                        bt_from_date.setText(Conversions.millitodateLabel((from_date_mills + Conversions.timezone())*1000));
                        date_selection = from_date_mills+"";
                        Log.e(TAG, "onDateSet:from_date_mills "+from_date_mills );

                        //clear to date
                        bt_to_date.setText("");
                        to_date_selection = "";
                        number_of_occurrences_count = 0;
                        txt_number_of_occurrences.setText(getResources().getString(R.string.Number_of_occurrences)+ number_of_occurrences_count);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        datePickerDialog.show();
    }

    private void ToDateTimeField() {

        Calendar to_c = Calendar.getInstance();
        int mYear = to_c.get(Calendar.YEAR);
        int mMonth = to_c.get(Calendar.MONTH);
        int mDay = to_c.get(Calendar.DAY_OF_MONTH);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        to_day = dayOfMonth+"";
                        to_month = (monthOfYear + 1)+"";
                        to_year = year+"";

                        String date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        to_date_mills = Conversions.getdatestamp(date_time);
                        to_date_selection = to_date_mills+"";
                        bt_to_date.setText(Conversions.millitodateLabel((to_date_mills + Conversions.timezone())*1000));
                        Log.e(TAG, "onDateSet:to_date_mills "+to_date_mills );

                        set_datestamp();
                    }
                },  mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        datePickerDialog.show();
    }

    private void set_datestamp() {
        dates_stamps = new ArrayList<Long>();
        days_stamps();
        if (Selection_type.equalsIgnoreCase("1")){
            //daily
            List<Date> dates = new ArrayList<Date>();
            String str_date =from_day+"/"+from_month+"/"+from_year;
            String end_date =to_day+"/"+to_month+"/"+to_year;

            setDaily_days();

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
                txt_number_of_occurrences.setText(getResources().getString(R.string.Number_of_occurrences)+ number_of_occurrences_count);
            }catch (ParseException e) {
                e.printStackTrace();
            }
        }else if (Selection_type.equalsIgnoreCase("2")){
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
                Log.e(TAG, "dates_stamps"+dates_stamps );
                number_of_occurrences_count= i + 1;
            }
            txt_number_of_occurrences.setText(getResources().getString(R.string.Number_of_occurrences)+ number_of_occurrences_count);

        }else if (Selection_type.equalsIgnoreCase("3")){
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
                txt_number_of_occurrences.setText(getResources().getString(R.string.Number_of_occurrences)+ number_of_occurrences_count);

                for (String month : months){
                    Log.e(TAG, "set_datestamp:months"+month.toUpperCase(locale) );
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
        }

    }

    private void setDaily_days() {
        //monday
        t_mon =true;
        bt_mon.setSelected(true);
        bt_mon.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        //tue
        t_tue = true;
        bt_tue.setSelected(true);
        bt_tue.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        //web
        t_web = true;
        bt_wed.setSelected(true);
        bt_wed.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        //thu
        t_thu = true;
        bt_thu.setSelected(true);
        bt_thu.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        //fri
        t_fri = true;
        bt_fri.setSelected(true);
        bt_fri.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        //sat
        t_sat = true;
        bt_sat.setSelected(true);
        bt_sat.setBackgroundResource(R.drawable.round_primary_color_without_edge);
        //sun
        t_sun = true;
        bt_sun.setSelected(true);
        bt_sun.setBackgroundResource(R.drawable.round_primary_color_without_edge);
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
                Log.e(TAG, "dates_stamps" + dates_stamps);

                String ds = formatter.format(lDate);
                Log.e(TAG, "set_days:dsadas" + ds);
                number_of_occurrences_count = i + 1;
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getListMonths(Date dateFrom, Date dateTo, Locale locale, DateFormat df) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(dateFrom);

        List<String> months = new ArrayList<>();

        while (calendar.getTime().getTime() <= dateTo.getTime()) {
            months.add(df.format(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
        }
        return months;
    }

    private void Reccuring_fun() {
        listner.onSelected("true",dates_stamps,Selection_type,from_date_mills+"",to_date_mills+"","","",weekdays,from_day,from_month,from_year,to_day,to_month,to_year,t_mon,t_tue,t_web,t_thu,t_fri,t_sat,t_sun,number_of_occurrences_count,bt_from_date.getText().toString(),bt_to_date.getText().toString());
        dismiss();
    }

    public interface ItemSelected {
        void onSelected(String recurrence,List<Long>dates_s,String re_type,String dtstart,String dtend,String starts,String ends,ArrayList weekdays,String from_d,String from_m,String from_y,String to_d,String to_m,String to_y,boolean t_mon,boolean t_tue,boolean t_web,boolean t_thu,boolean t_fri,boolean t_sat,boolean t_sun,int number_of_occurrences_count,String from_date,String to_Date);
    }

}