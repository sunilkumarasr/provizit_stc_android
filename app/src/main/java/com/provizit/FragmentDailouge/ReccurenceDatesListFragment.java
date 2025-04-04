package com.provizit.FragmentDailouge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.Activities.MeetingDescriptionNewActivity;
import com.provizit.Activities.NavigationActivity;
import com.provizit.Config.CustomItemFiveListener;
import com.provizit.Conversions;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.RangeTimePickerDialog;
import com.provizit.Utilities.ReMeetings;
import com.provizit.databinding.FragmentReccurenceDatesListBinding;
import com.provizit.databinding.FragmentSetUpMeetingMailsListBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ReccurenceDatesListFragment extends BottomSheetDialogFragment {

    FragmentReccurenceDatesListBinding binding;
    ReccuringAdapter reccuringAdapter;
    SharedPreferences sharedPreferences1;
    ApiViewModel apiViewModel;

    int Dates_ = 0;
    int Months_ = 0;
    int Years_ = 0;

    Long s_date = null;
    Long s_time = null, e_time = null;
    long timeInMillis;

    private FragmentActivity myContext;
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

    ItemSelected listner;
    public void onItemsSelectedListner(ItemSelected listner){
        this.listner = listner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReccurenceDatesListBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();


        sharedPreferences1 = getActivity().getSharedPreferences("EGEMSS_DATA", 0);

        apiViewModel = new ViewModelProvider(getActivity()).get(ApiViewModel.class);


        binding.recyclerview.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerview.setLayoutManager(manager);
        reccuringAdapter = new ReccuringAdapter(getActivity(), MeetingDescriptionNewActivity.meetings.getRe_meetings(), (v, meetingid, date, start, end, type) -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);

            long currentTimeMillis = System.currentTimeMillis();
            long selectedDateMillis = Long.parseLong(date);

            Log.e("currentTimeMillis",currentTimeMillis+"");
            Log.e("selectedDateMillis",selectedDateMillis+"");


            if (currentTimeMillis < selectedDateMillis) {

            }else {

            }

            //listner.onSelcetd(meetingid, date,start,end, type);
            if (type.equalsIgnoreCase("Reschedule")){
                reschedulePopup(meetingid);
            }else {
                cancelPopup(meetingid);
            }

        });
        binding.recyclerview.setAdapter(reccuringAdapter);

        //reschedule meeting
        apiViewModel.reschedulemeeting_response().observe(getActivity(), response -> {
            binding.progressbar.setVisibility(GONE);
            dismiss();
            Intent intent = new Intent(getActivity(), NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        //cancel meeting
        apiViewModel.updatemeetings_response().observe(getActivity(), response -> {
            binding.progressbar.setVisibility(GONE);
            dismiss();
            Intent intent = new Intent(getActivity(), NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        return rootView;
    }




    private void reschedulePopup(String meetingId) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_recuring_date_time);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btDate = dialog.findViewById(R.id.btDate);
        Button btStartTime = dialog.findViewById(R.id.btStartTime);
        Button btEndTime = dialog.findViewById(R.id.btEndTime);
        TextView txt_yes = dialog.findViewById(R.id.txt_yes);
        TextView txt_no = dialog.findViewById(R.id.txt_no);

        btDate.setOnClickListener(view -> {
            AnimationSet animationSet = Conversions.animation();
            view.startAnimation(animationSet);
            DatePicker(btDate);
        });

        btStartTime.setOnClickListener(view -> {
            AnimationSet animationSet = Conversions.animation();
            view.startAnimation(animationSet);

            if (btDate.getText().length() == 0) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.warning)
                        .setMessage("Select date")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                StartTimePicker(btStartTime,"start");
            }

        });

        btEndTime.setOnClickListener(view -> {
            AnimationSet animationSet = Conversions.animation();
            view.startAnimation(animationSet);

            if (btStartTime.getText().length() == 0) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.warning)
                        .setMessage("Select Start Time")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                StartTimePicker(btEndTime,"end");
            }

        });

        txt_yes.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);

            if (s_date != null && s_time != null && e_time != null){
                JsonObject gsonObject = new JsonObject();
                JSONObject jsonObj_ = new JSONObject();
                try {
                    jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                    jsonObj_.put("id", meetingId);
                    jsonObj_.put("status", 2);
                    jsonObj_.put("date", s_date);
                    jsonObj_.put("start", s_time);
                    jsonObj_.put("end", e_time - 1);
                    System.out.println("reschedulemeeting_asfasf" + jsonObj_);
                    JsonParser jsonParser = new JsonParser();
                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiViewModel.reschedulemeeting(getActivity(), gsonObject);
                binding.progressbar.setVisibility(VISIBLE);
                dialog.dismiss();
            }

        });
        txt_no.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });
        dialog.show();
    }


    private void DatePicker(Button btDate) {
        Calendar from_c = Calendar.getInstance();
        int day = from_c.get(Calendar.DAY_OF_MONTH);
        int month = from_c.get(Calendar.MONTH);
        int year = from_c.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year1, monthOfYear, dayOfMonth) -> {

                    //from date set
                    String fromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    s_date = Conversions.getdatestamp(fromDate);
                    Dates_ = dayOfMonth;
                    Months_ = monthOfYear;
                    Years_ = year1;
                    Long fromDateMillis = from_c.getTimeInMillis();

                    //to date
                    btDate.setText(fromDate);

                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        datePickerDialog.show();
    }

    private void StartTimePicker(Button btTime, String type) {

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
        mTimePicker = new RangeTimePickerDialog(getActivity(), (timePicker, selectedHour, selectedMinute) -> {
            String time = "";
            if (selectedHour > 12 && selectedHour != 24) {
                time = String.format("%02d", selectedHour - 12) + ":" + String.format("%02d", selectedMinute) + " PM";
            } else if (selectedHour == 12) {
                time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + " PM";
            } else if (selectedHour == 24) {
                time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + " AM";
            } else {
                time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + " AM";
            }
            if (type.equalsIgnoreCase("start")){
                btTime.setText(selectedHour + ":" + selectedMinute);
                s_time = Conversions.gettimestamp(Dates_ + "-" + (Months_ + 1) + "-" + Years_, selectedHour + ":" + selectedMinute);

                timeInMillis = getTimeInMillis(selectedHour, selectedMinute);
            }else {

                if (timeInMillis < getTimeInMillis(selectedHour, selectedMinute)){
                    btTime.setText(selectedHour + ":" + selectedMinute);
                    e_time = Conversions.gettimestamp(Dates_ + "-" + (Months_ + 1) + "-" + Years_, selectedHour + ":" + selectedMinute);
                }


            }

        }, Shour, Sminute, true);//true = 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.setMin(hour, minute);
        mTimePicker.show();
    }

    private long getTimeInMillis(int hour, int minute) {
        // Create a Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Set the hour and minute
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Get the time in milliseconds
        return calendar.getTimeInMillis();
    }

    private void cancelPopup(String meetingId) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.otp_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText reason = dialog.findViewById(R.id.reason);
        TextView submit = dialog.findViewById(R.id.submit);
        TextView cancel = dialog.findViewById(R.id.cancel);

        submit.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);

            if (reason.getText().toString().equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please enter Reason",Toast.LENGTH_SHORT).show();
            }else {
                JsonObject gsonObject = new JsonObject();
                JSONObject jsonObj_ = new JSONObject();
                try {
                    jsonObj_.put("formtype", "delete");
                    jsonObj_.put("status", 1);
                    jsonObj_.put("action", "Cancel");
                    jsonObj_.put("reason", reason.getText().toString());
                    jsonObj_.put("id", meetingId);
                    jsonObj_.put("comp_id", sharedPreferences1.getString("company_id", null));
                    JsonParser jsonParser = new JsonParser();
                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                apiViewModel.updatemeetings(getActivity(), gsonObject);
                binding.progressbar.setVisibility(VISIBLE);
                dialog.dismiss();
            }


        });
        cancel.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });
        dialog.show();
    }

    public class ReccuringAdapter extends RecyclerView.Adapter<ReccuringAdapter.MyviewHolder> {
        CustomItemFiveListener customItemListener;
        Context context;
        ArrayList<ReMeetings> reccuring;

        DatabaseHelper myDb;
        EmpData empData;

        public ReccuringAdapter(Context context, ArrayList<ReMeetings> reccuring, CustomItemFiveListener customItemListener) {
            this.context = context;
            this.reccuring = reccuring;
            this.customItemListener = customItemListener;
        }

        @Override
        public  MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.recuuring_dates_items_list, parent, false);
            return new MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {

            //date and time
            String startTime = Conversions.millitotime((reccuring.get(position).getStart()+Conversions.timezone()) * 1000,false);
            String EndTime = Conversions.millitotime((reccuring.get(position).getEnd()+1+Conversions.timezone() + 1) * 1000,false);
            String date = Conversions.millitodateD((reccuring.get(position).getDate()+Conversions.timezone()) * 1000);
            String dateTime = date+", "+startTime + " to "+EndTime;

            long dateMillis = (long) (reccuring.get(position).getDate() * 1000);

            // Get the current time in milliseconds
            long currentDateMillis = System.currentTimeMillis();

            holder.tvDate.setText(dateTime);

            if (reccuring.get(position).getStatus()==1){
                holder.linearOptions.setVisibility(GONE);
                holder.tvStatus.setVisibility(VISIBLE);
                Spannable spannable = new SpannableString(date+", "+startTime + " to "+EndTime);
                spannable.setSpan(new StrikethroughSpan(), 0, dateTime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvDate.setText(spannable);
                holder.tvDate.setTextColor(getResources().getColor(R.color.red));
                holder.tvStatus.setTextColor(getResources().getColor(R.color.red));
                holder.tvStatus.setText("(" + holder.itemView.getContext().getString(R.string.cancel) + ")");
            }else {

                if (dateMillis < currentDateMillis) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        //api list date
                        LocalDate listDate = Instant.ofEpochMilli(dateMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        ZonedDateTime zonedDateTime = listDate.atStartOfDay(ZoneId.systemDefault());
                        long ListTimeMillis = zonedDateTime.toInstant().toEpochMilli();

                        //current date
                        LocalDate currentDate = Instant.ofEpochMilli(currentDateMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        String dateString = currentDate+"";
                        String timeString = startTime;
                        String dateTimeString = dateString + " " + timeString;
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
                        LocalDateTime cDateTime = LocalDateTime.parse(dateTimeString, formatter);
                        long CurrentTimeMillis = cDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

                        if (listDate.equals(currentDate)){

                            if (ListTimeMillis < CurrentTimeMillis) {
                                if (MeetingDescriptionNewActivity.meetings.getEmp_id().equalsIgnoreCase(empData.getEmp_id())){
                                    holder.linearOptions.setVisibility(VISIBLE);
                                    holder.tvStatus.setVisibility(GONE);
                                    holder.tvDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    holder.tvStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                                }else {
                                    holder.linearOptions.setVisibility(GONE);
                                    holder.tvStatus.setVisibility(VISIBLE);
                                    holder.tvDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    holder.tvStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    holder.tvStatus.setText("(" + holder.itemView.getContext().getString(R.string.upcoming) + ")");
                                }
                            }else {
                                holder.linearOptions.setVisibility(VISIBLE);
                                holder.tvStatus.setVisibility(GONE);
                                holder.tvDate.setTextColor(getResources().getColor(R.color.hash_color));
                                holder.imgResedule.setEnabled(false);
                                holder.imgResedule.setColorFilter(ContextCompat.getColor(getActivity(), R.color.hash_color), PorterDuff.Mode.SRC_IN);
                                holder.imgCancel.setEnabled(false);
                                holder.imgCancel.setColorFilter(ContextCompat.getColor(getActivity(), R.color.hash_color), PorterDuff.Mode.SRC_IN);
                            }

                        }else {
                            holder.linearOptions.setVisibility(VISIBLE);
                            holder.tvStatus.setVisibility(GONE);
                            holder.tvDate.setTextColor(getResources().getColor(R.color.hash_color));
                            holder.imgResedule.setEnabled(false);
                            holder.imgResedule.setColorFilter(ContextCompat.getColor(getActivity(), R.color.hash_color), PorterDuff.Mode.SRC_IN);
                            holder.imgCancel.setEnabled(false);
                            holder.imgCancel.setColorFilter(ContextCompat.getColor(getActivity(), R.color.hash_color), PorterDuff.Mode.SRC_IN);
                        }

                    }

                } else if (dateMillis > currentDateMillis) {
                    if (MeetingDescriptionNewActivity.meetings.getEmp_id().equalsIgnoreCase(empData.getEmp_id())){
                        holder.linearOptions.setVisibility(VISIBLE);
                        holder.tvStatus.setVisibility(GONE);
                        holder.tvDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                        holder.tvStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }else {
                        holder.linearOptions.setVisibility(GONE);
                        holder.tvStatus.setVisibility(VISIBLE);
                        holder.tvDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                        holder.tvStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                        holder.tvStatus.setText("(" + holder.itemView.getContext().getString(R.string.upcoming) + ")");
                    }
                } else {
                    if (MeetingDescriptionNewActivity.meetings.getEmp_id().equalsIgnoreCase(empData.getEmp_id())){
                        holder.linearOptions.setVisibility(VISIBLE);
                        holder.tvStatus.setVisibility(GONE);
                        holder.tvDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                        holder.tvStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }else {
                        holder.linearOptions.setVisibility(GONE);
                        holder.tvStatus.setVisibility(VISIBLE);
                        holder.tvDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                        holder.tvStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                        holder.tvStatus.setText("(" + holder.itemView.getContext().getString(R.string.upcoming) + ")");
                    }
                }

            }


            holder.imgResedule.setOnClickListener(view -> {
                customItemListener.onItemClick(view, reccuring.get(position).get_id().get$oid(), reccuring.get(position).getDate()+"",reccuring.get(position).getStart()+"",reccuring.get(position).getEnd()+"","Reschedule");
            });

            holder.imgCancel.setOnClickListener(view -> {
                customItemListener.onItemClick(view, reccuring.get(position).get_id().get$oid(), reccuring.get(position).getDate()+"",reccuring.get(position).getStart()+"",reccuring.get(position).getEnd()+"","Cancel");
            });


        }

        @Override
        public int getItemCount() {
            return reccuring.size();
        }

        public class MyviewHolder extends RecyclerView.ViewHolder {

            CardView cardView;
            TextView tvDate,tvStatus;
            ImageView imgResedule,imgCancel;
            LinearLayout linearOptions;
            public MyviewHolder(View view) {
                super(view);
                cardView = view.findViewById(R.id.cardView);
                tvDate = view.findViewById(R.id.tvDate);
                tvStatus = view.findViewById(R.id.tvStatus);
                linearOptions = view.findViewById(R.id.linearOptions);
                imgResedule = view.findViewById(R.id.imgResedule);
                imgCancel = view.findViewById(R.id.imgCancel);

                myDb = new DatabaseHelper(context);
                empData = new EmpData();
                empData = myDb.getEmpdata();

            }
        }

    }

    public interface ItemSelected {
        void onSelcetd(String type);
    }

}