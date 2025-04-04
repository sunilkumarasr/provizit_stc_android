package com.provizit.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.AdapterAndModel.BusySchedules.BusySchedulesData;
import com.provizit.AdapterAndModel.BusySchedulesAdapter;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.RangeTimePickerDialog;
import com.provizit.Utilities.daysview.DayModel;
import com.provizit.databinding.ActivityBusyScheduleBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BusyScheduleActivity extends AppCompatActivity  implements View.OnClickListener {

    ActivityBusyScheduleBinding binding;
    DatabaseHelper myDb;
    SharedPreferences.Editor editor1;
    EmpData empData;

    ApiViewModel apiViewModel;

    Button bt_from_date;
    Button bt_to_date;
    Button bt_from_time;
    Button bt_to_time;

    private static Locale locale;
    TextView txt_occurse;
    LocalDate Local_startDate = null;
    LocalDate Local_endDate = null;
    Calendar from_c;
    Calendar to_c;
    long from_date_mills = 0;
    long to_date_mills = 0;

    //from time picker
    int SelectedFromHour, SelectedFromMin;
    String SelectedFromDate, SelectedFromSTime;
    public static long Froms_time, Frome_time, s_date;
    int Fromd_hour = 0, Fromd_min = 0, Froms_hour, Froms_min;
    int Fhour = 0, Fminute = 0, FShour = 0,  FSminute = 0;

    //to time picker
    int SelectedToHour, SelectedToMin;
    String SelectedToDate, SelectedToSTime;
    public static long Tos_time, Toe_time, Tos_date;
    int Tod_hour = 0, Tod_min = 0, Tos_hour, Tos_min;

    //days
    private ArrayList<DayModel> daysAndWeeksList;
    String todayDay = "";
    //month
    RadioButton radio_on_day, radio_on_the_first;

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

    String comp_id = "";

    private int selectedFromHour, selectedFromMinute;
    private int selectedToHour, selectedToMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewController.barPrimaryColor(BusyScheduleActivity.this);
        binding = ActivityBusyScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

    }

    private void initView() {

        SharedPreferences sharedPreferences1 = BusyScheduleActivity.this.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
        editor1 = sharedPreferences1.edit();
        myDb = new DatabaseHelper(this);
        empData = new EmpData();
        empData = myDb.getEmpdata();

        apiViewModel = new ViewModelProvider(BusyScheduleActivity.this).get(ApiViewModel.class);

        //list
        comp_id = sharedPreferences1.getString("company_id", null);
        String Emp_id = empData.getEmp_id();
        Log.e("comp_id_",comp_id);
        Log.e("Emp_id_",Emp_id);
        apiViewModel.getbusyScheduledetails(getApplicationContext(), comp_id,Emp_id,"busy");
        apiViewModel.getbusySchedulesdetails_response().observe(BusyScheduleActivity.this, response -> {
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)){

                } else if (statuscode.equals(successcode)){
                    if (response.getItems()!=null){
                        ArrayList<BusySchedulesData> busySchedules = new ArrayList<>();
                        busySchedules.clear();

                        if (!response.getItems().isEmpty()){

                            for (int i = 0; i < response.getItems().size(); i++) {

                                // API date
                                double toDate = Double.parseDouble(response.getItems().get(i).getTo_date());
                                long toDateMillis = (long) (toDate * 1000L);

                                // Current date
                                long currentDateMillis = System.currentTimeMillis();

                                long millisInDay = 24 * 60 * 60 * 1000L;
                                long normalizedToDateMillis = toDateMillis / millisInDay * millisInDay;
                                long normalizedCurrentDateMillis = currentDateMillis / millisInDay * millisInDay;

                                Log.e("toDateMillis_",toDateMillis+"");
                                Log.e("currentDateMillis_",currentDateMillis+"");

                                if (normalizedToDateMillis >= normalizedCurrentDateMillis) {
                                    busySchedules.add(response.getItems().get(i)); // Add to the list if the condition is met
                                }

                            }

                            if (busySchedules.isEmpty()){
                                binding.relativeNoData.setVisibility(View.VISIBLE);
                            }

                            BusySchedulesAdapter busySchedulesAdapter = new BusySchedulesAdapter(BusyScheduleActivity.this, busySchedules, new BusySchedulesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BusySchedulesData item, String type) {
                                    // Handle the click event
                                    if (type.equals("delete")){
                                        deleteConformationPopUp(item.get_id().get$oid());
                                    }

                                }
                            });

                            LinearLayoutManager managera = new LinearLayoutManager(BusyScheduleActivity.this);
                            binding.recycler.setLayoutManager(managera);
                            binding.recycler.setAdapter(busySchedulesAdapter);

                        }else {
                            binding.relativeNoData.setVisibility(View.VISIBLE);
                        }

                    }else {
                        binding.relativeNoData.setVisibility(View.VISIBLE);
                    }
                }
            }else {
                Conversions.errroScreen(BusyScheduleActivity.this, "getbusySchedules");
            }
        });


        apiViewModel.actionbusySchedule_response().observe(BusyScheduleActivity.this, response -> {
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)){

                } else if (statuscode.equals(successcode)) {
                    if (response.getItems() != null) {
                        apiViewModel.getbusyScheduledetails(getApplicationContext(), comp_id, Emp_id, "busy");
                    }
                }
            }else {
                Conversions.errroScreen(BusyScheduleActivity.this, response.getItems().toString());
            }
        });


        binding.imgBack.setOnClickListener(this);
        binding.cardAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.cardAdd:
                addPopUp();
                break;
        }
    }

    //delete meeting
    private void deleteConformationPopUp(String meetingID) {
        final Dialog dialog = new Dialog(BusyScheduleActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtYes = dialog.findViewById(R.id.txtYes);
        TextView txtNo = dialog.findViewById(R.id.txtNo);

        txtYes.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);

            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("cid", comp_id);
                jsonObj_.put("id", meetingID);
                jsonObj_.put("type", "delete");
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiViewModel.actionbusySchedule(getApplicationContext(), gsonObject);
            dialog.dismiss();
        });
        txtNo.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void addPopUp() {
        final Dialog dialog = new Dialog(BusyScheduleActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_busy_schedule);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        bt_from_date = dialog.findViewById(R.id.bt_from_date);
        bt_to_date = dialog.findViewById(R.id.bt_to_date);
        bt_from_time = dialog.findViewById(R.id.bt_from_time);
        bt_to_time = dialog.findViewById(R.id.bt_to_time);
        TextView txtSubmit = dialog.findViewById(R.id.txtSubmit);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);

        currentDate();
        currentfromTimePicker();

        bt_from_date.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            fromDatePicker();
        });

        bt_to_date.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            toDatePicker();
        });

        bt_from_time.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            //fromTimePicker();
            FromTimePickerDialog();
        });
        bt_to_time.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
           // toTimePicker();
            ToTimePickerDialog();
        });

        txtSubmit.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });
        txtCancel.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });
        dialog.show();
    }

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

            //from and to date set
            String from_dateSet = day + "-" + (month + 1) + "-" + year;
            from_date_mills = from_c.getTimeInMillis();
            to_date_mills = from_c.getTimeInMillis();
            SelectedFromDate = from_dateSet;
            bt_from_date.setText(from_dateSet);
            bt_to_date.setText(from_dateSet);
            from_day = day+"";
            from_month = (month+1)+"";
            from_year = year+"";
            to_day = day+"";
            to_month = (month+1)+"";
            to_year = year+"";

            //day
            SimpleDateFormat sdf = new SimpleDateFormat("EEE");
            todayDay = sdf.format(from_c.getTime());

        }

    }

    //from date
    private void fromDatePicker() {
        from_c = Calendar.getInstance();
        int day = from_c.get(Calendar.DAY_OF_MONTH);
        int month = from_c.get(Calendar.MONTH);
        int year = from_c.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(BusyScheduleActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {

                    //from date set
                    String from_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    from_date_mills = from_c.getTimeInMillis();
                    bt_from_date.setText(from_date);
                    SelectedFromDate = from_date;
                    from_day = dayOfMonth+"";
                    from_month = (monthOfYear+1)+"";
                    from_year = year1+"";

                    //to date
                    bt_to_date.setText("");
                    //picker start date based on from date selection
                    from_c.set(year1, monthOfYear, dayOfMonth);
//                    from_date_mills = from_c.getTimeInMillis();

                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        datePickerDialog.show();
    }

    //to date
    private void toDatePicker() {

        //from date to one month
        to_c = Calendar.getInstance();
        to_c.setTimeInMillis(from_c.getTimeInMillis());
        to_c.add(Calendar.MONTH, 1);
        int day = to_c.get(Calendar.DAY_OF_MONTH);
        int month = to_c.get(Calendar.MONTH);
        int year = to_c.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(BusyScheduleActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {

                    String to_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    to_date_mills = to_c.getTimeInMillis();
                    bt_to_date.setText(to_date);
                    to_day = dayOfMonth+"";
                    to_month = (monthOfYear+1)+"";
                    to_year = year1+"";

                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(from_date_mills - 1000);
        datePickerDialog.show();

    }

    //current from time picker
    private void currentfromTimePicker() {
            final Calendar mcurrentTime = Calendar.getInstance();
            Fhour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            Fminute = mcurrentTime.get(Calendar.MINUTE);
            FShour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            FSminute = mcurrentTime.get(Calendar.MINUTE);

            if (Fhour < 15) {
                Fminute = 15;
                FSminute = 15;
            } else if (Fminute < 30) {
                Fminute = 30;
                FSminute = 30;
            } else if (Fminute < 45) {
                Fminute = 45;
                FSminute = 45;
            } else {
                Fhour = Fhour + 1;
                FShour = FShour + 1;
                Fminute = 0;
                FSminute = 0;
            }

            // If a time has been selected before, use it
            if (SelectedFromHour != 0) {
                FShour = SelectedFromHour;
                FSminute = SelectedFromMin;
            }

            // Automatically set the calculated time
            String time = "";
            if (FShour > 12 && FShour != 24) {
                time = String.format("%02d", FShour - 12) + ":" + String.format("%02d", FSminute) + " PM";
            } else if (FShour == 12) {
                time = String.format("%02d", FShour) + ":" + String.format("%02d", FSminute) + " PM";
            } else if (FShour == 24) {
                time = String.format("%02d", FShour) + ":" + String.format("%02d", FSminute) + " AM";
            } else {
                time = String.format("%02d", FShour) + ":" + String.format("%02d", FSminute) + " AM";
            }

            // Set the values directly without showing the dialog
            Froms_time = Conversions.gettimestamp(SelectedFromDate, FShour + ":" + FSminute);
            SelectedFromSTime = FShour + ":" + FSminute;
            String myTime = SelectedFromSTime;
            Froms_hour = FShour;
            Froms_min = FSminute;

            SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date d = null;
            try {
                d = df.parse(myTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (d != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                String Stime = df.format(cal.getTime());
                bt_from_time.setText(Conversions.millitotime((Conversions.gettimestamp(SelectedFromDate, Stime) + Conversions.timezone()) * 1000, false));

                // Add hours and minutes to get the ending time
                cal.add(Calendar.HOUR_OF_DAY, Fromd_hour);
                cal.add(Calendar.MINUTE, Fromd_min);
                String newTime = df.format(cal.getTime());
                Frome_time = Conversions.gettimestamp(SelectedFromDate, newTime);
                //currenttoTimePicker();
                Log.e("d_hour_", Fromd_hour + "");
                Log.e("d_min_", Fromd_min + "");
            }
    }

    // From Time Picker
    private void fromTimePicker() {
        final Calendar mcurrentTime = Calendar.getInstance();
        Fhour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        Fminute = mcurrentTime.get(Calendar.MINUTE);
        FShour = Fhour;
        FSminute = Fminute;

        final RangeTimePickerDialog mTimePicker;
        // Set the minute to the nearest 15-minute interval
        if (Fminute < 15) {
            Fminute = 15;
            FSminute = 15;
        } else if (Fminute < 30) {
            Fminute = 30;
            FSminute = 30;
        } else if (Fminute < 45) {
            Fminute = 45;
            FSminute = 45;
        } else {
            Fhour += 1;
            FShour += 1;
            Fminute = 0;
            FSminute = 0;
        }

        if (SelectedFromHour != 0) {
            FShour = SelectedFromHour;
            FSminute = SelectedFromMin;
        }

        mTimePicker = new RangeTimePickerDialog(BusyScheduleActivity.this, (timePicker, selectedHour, selectedMinute) -> {
            SelectedFromHour = selectedHour;
            SelectedFromMin = selectedMinute;
            String time = formatTime(selectedHour, selectedMinute);
            Froms_time = Conversions.gettimestamp(SelectedFromDate, selectedHour + ":" + selectedMinute);
            bt_from_time.setText(Conversions.millitotime(Froms_time * 1000, false));
        }, FShour, FSminute, true);

        mTimePicker.setTitle("Select Time");
        mTimePicker.setMin(Fhour, Fminute);
        mTimePicker.show();
    }

    // To Time Picker
    private void toTimePicker() {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        // Ensure "To Time" starts from the selected "From Time"
        int Shour = SelectedFromHour;
        int Sminute = SelectedFromMin;

        final RangeTimePickerDialog mTimePicker;
        // Set the minute to the nearest 15-minute interval
        if (Sminute < 15) {
            Sminute = 15;
        } else if (Sminute < 30) {
            Sminute = 30;
        } else if (Sminute < 45) {
            Sminute = 45;
        } else {
            Shour += 1;
            Sminute = 0;
        }

        if (SelectedToHour != 0) {
            Shour = SelectedToHour;
            Sminute = SelectedToMin;
        }

        mTimePicker = new RangeTimePickerDialog(BusyScheduleActivity.this, (timePicker, selectedHour, selectedMinute) -> {
            // Ensure "To Time" is after "From Time"
            if (selectedHour < SelectedFromHour || (selectedHour == SelectedFromHour && selectedMinute <= SelectedFromMin)) {
                Toast.makeText(BusyScheduleActivity.this, "To Time must be after From Time", Toast.LENGTH_SHORT).show();
                return;
            }

            SelectedToHour = selectedHour;
            SelectedToMin = selectedMinute;
            String time = formatTime(selectedHour, selectedMinute);
            Tos_time = Conversions.gettimestamp(SelectedToDate, selectedHour + ":" + selectedMinute);
            bt_to_time.setText(Conversions.millitotime(Tos_time * 1000, false));
        }, Shour, Sminute, true);

        mTimePicker.setTitle("Select Time");
        mTimePicker.setMin(Shour, Sminute);  // Set the minimum time to "From Time"
        mTimePicker.show();
    }

    // Helper function to format time
    private String formatTime(int hour, int minute) {
        if (hour > 12 && hour != 24) {
            return String.format("%02d", hour - 12) + ":" + String.format("%02d", minute) + " PM";
        } else if (hour == 12) {
            return String.format("%02d", hour) + ":" + String.format("%02d", minute) + " PM";
        } else if (hour == 24) {
            return String.format("%02d", hour - 12) + ":" + String.format("%02d", minute) + " AM";
        } else {
            return String.format("%02d", hour) + ":" + String.format("%02d", minute) + " AM";
        }
    }

    //new
    // From Time Picker
    private void FromTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Round the current minute to the nearest 15-minute interval
        currentMinute = (currentMinute + 7) / 15 * 15;

        // If rounded up past the hour, increase the hour by 1 and reset minute to 0
        if (currentMinute == 60) {
            currentMinute = 0;
            currentHour = (currentHour + 1) % 24;
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(BusyScheduleActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Round the selected minute to the nearest 15-minute interval
                        int roundedMinute = (minute / 15) * 15;

                        selectedFromHour = hourOfDay;  // Store the selected "From Hour"
                        selectedFromMinute = roundedMinute;  // Store the selected "From Minute"

                        String time = String.format("%02d:%02d", selectedFromHour, selectedFromMinute);
                        bt_from_time.setText(time);  // Update the button text with the selected time
                        Toast.makeText(BusyScheduleActivity.this, "Selected time: " + time, Toast.LENGTH_SHORT).show();
                    }
                }, currentHour, currentMinute, true);

        timePickerDialog.show();
    }

    // To Time Picker
    private void ToTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int currentHour = selectedFromHour;  // Use the previously selected "From Time" as the starting hour
        int currentMinute = selectedFromMinute;  // Use the previously selected "From Time" as the starting minute

        // Round the current minute to the nearest 15-minute interval
        currentMinute = (currentMinute + 7) / 15 * 15;

        // If rounded up past the hour, increase the hour by 1 and reset minute to 0
        if (currentMinute == 60) {
            currentMinute = 0;
            currentHour = (currentHour + 1) % 24;
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(BusyScheduleActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Round the selected minute to the nearest 15-minute interval
                        int roundedMinute = (minute / 15) * 15;

                        // Ensure that the "To Time" is after the "From Time"
                        if (hourOfDay < selectedFromHour || (hourOfDay == selectedFromHour && roundedMinute <= selectedFromMinute)) {
                            Toast.makeText(BusyScheduleActivity.this, "To Time must be after From Time", Toast.LENGTH_SHORT).show();
                            return;  // Do not set the time if invalid
                        }

                        selectedToHour = hourOfDay;  // Store the selected "To Hour"
                        selectedToMinute = roundedMinute;  // Store the selected "To Minute"

                        String time = String.format("%02d:%02d", selectedToHour, selectedToMinute);
                        bt_to_time.setText(time);  // Update the button text with the selected time
                        Toast.makeText(BusyScheduleActivity.this, "Selected time: " + time, Toast.LENGTH_SHORT).show();
                    }
                }, currentHour, currentMinute, true);

        timePickerDialog.show();
    }


}