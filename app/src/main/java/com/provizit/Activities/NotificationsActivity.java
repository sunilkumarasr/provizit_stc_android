package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.provizit.Config.CommonClass;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.Logins.ForgotActivity;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.MVVM.RequestModels.NotificationsStatusChangeModelRequest;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Model;
import com.provizit.Services.Notifications.NotificationsComon;
import com.provizit.Services.Notifications.Notifications_model;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NotificationsActivity";

    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;

    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    LinearLayout relative_ui;

    private CommonClass cc;

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;

    ImageView img_back;
    RelativeLayout relative_no_data;

    //invitation reports
    ShimmerFrameLayout shimmer_view_container;
    RecyclerView recycler_view;
    ArrayList<NotificationsComon> notificationslist1;
    ArrayList<NotificationsComon> notificationslist;
    ArrayList<NotificationsComon> notificationslist_final;
    NotificationsAdapter notificationsAdapter;

    ApiViewModel apiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ViewController.barPrimaryColor(NotificationsActivity.this);

        cc = new CommonClass(NotificationsActivity.this);

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

        sharedPreferences1 = getApplicationContext().getSharedPreferences("EGEMSS_DATA", 0);
        myDb = new DatabaseHelper(NotificationsActivity.this);
        empData = new EmpData();
        empData = myDb.getEmpdata();

        img_back= findViewById(R.id.img_back);
        relative_no_data= findViewById(R.id.relative_no_data);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        recycler_view = findViewById(R.id.recycler_view);

        apiViewModel = new ViewModelProvider(NotificationsActivity.this).get(ApiViewModel.class);

        apiViewModel.getnotifications(getApplicationContext(),sharedPreferences1.getString("company_id", null),empData.getEmail());
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();
        notificationslist1 = new ArrayList<>();
        notificationslist = new ArrayList<>();
        notificationslist_final = new ArrayList<>();

        apiViewModel.getnotificationsListResponse().observe(this, response -> {
            shimmer_view_container.stopShimmerAnimation();
            shimmer_view_container.setVisibility(View.GONE);

            notificationslist1.clear();
            notificationslist.clear();
            notificationslist_final.clear();

            if (response!=null){
                notificationslist1 = response.getItems();
                notificationslist.addAll(notificationslist1);

                if (notificationslist.size()==0){
                    relative_no_data.setVisibility(View.VISIBLE);
                }else {
                    for (int i = 0; i < notificationslist.size(); i++) {
                        notificationslist_final.add(notificationslist.get(i));
                    }
                }

                notificationsAdapter = new NotificationsAdapter(getApplicationContext(), notificationslist_final);
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                manager.setReverseLayout(true);
                manager.setStackFromEnd(true);
                recycler_view.setLayoutManager(manager);
                //set adapter
                recycler_view.setAdapter(notificationsAdapter);
                cc.AnimationLeft(recycler_view);
            }else {
                Conversions.errroScreen(NotificationsActivity.this, "getnotificationsList");
            }

        });

        apiViewModel.getnotificationsstatuschangeResponse().observe(this, new Observer<Model>() {
            @Override
            public void onChanged(Model response) {
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmerAnimation();
                //redirect to Navigation page
            }
        });


        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
//            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
            finish();
        }
    }

    protected void registoreNetWorkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        ArrayList<NotificationsComon> notificationsa;
        private final Context context;

        public NotificationsAdapter(Context context, ArrayList<NotificationsComon> notificationsa) {
            this.notificationsa = notificationsa;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_list_items, parent, false);
            return new ViewHolderToday(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            NotificationsAdapter.ViewHolderToday Holder = (NotificationsAdapter.ViewHolderToday) holder;

            Holder.txt_name.setText(" " + notificationsa.get(position).getEmployeeData().getName());
            Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());


//            if (notificationsa.get(position).getMeetingData().getStart()!=null && notificationsa.get(position).getMeetingData().getEnd()!=null && notificationsa.get(position).getMeetingData().getStart()!=null){
//                String start_time = Conversions.millitotime((notificationsa.get(position).getMeetingData().getStart() + Conversions.timezone()) * 1000, false);
//                String end_time = Conversions.millitotime((notificationsa.get(position).getMeetingData().getEnd() + Conversions.timezone()) * 1000, false);
//                String date1 = Conversions.millitodateD((notificationsa.get(position).getMeetingData().getStart()+Conversions.timezone()) * 1000);
//
//                Holder.txt_date_time.setVisibility(View.VISIBLE);
//                Holder.txt_date_time.setText(date1+" "+start_time + " - "+ end_time);
//            }else {
//                Holder.txt_date_time.setVisibility(View.GONE);
//            }


            if (notificationsa.get(position).getCreated_time().get$numberLong()!=null){
//                String date = Conversions.millitodateD((notificationsa.get(position).getCreated_time().get$numberLong() + Conversions.timezone()) * 1000);
//                Holder.txt_date_time.setVisibility(View.VISIBLE);
//                Holder.txt_date_time.setText(date);

                //create date and time
                long longtime = (Long.parseLong(String.valueOf(Long.parseLong(String.valueOf(notificationsa.get(position).getCreated_time().get$numberLong()))  * 1000)));
                Date d = new Date(longtime);
                String date = DateFormat.getDateTimeInstance().format(d);
                Holder.txt_date_time.setVisibility(View.VISIBLE);
                Holder.txt_date_time.setText(date+"");


            }else {
                Holder.txt_date_time.setVisibility(View.GONE);
            }


            if (notificationsa.get(position).getEmployeeData().getPic()!=null){
                if (notificationsa.get(position).getEmployeeData().getPic().size() != 0) {
                    Glide.with(getApplicationContext()).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + notificationsa.get(position).getEmployeeData().getPic().get(notificationsa.get(position).getEmployeeData().getPic().size() - 1))
                            .into(Holder.emp_img);
                }
            }

            
            Log.e(TAG, "onBindViewHolder:oid"+notificationsa.get(position).get_id().get$oid() );


            if (notificationsa.get(position).getNtype().equalsIgnoreCase("1")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.meeting));
                Holder.txt_note.setText(getResources().getString(R.string.has_invited_you_for_meeting));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("2")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.meeting));
                Holder.txt_note.setText(getResources().getString(R.string.has_accepted_you_for_meeting_request));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("3")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.meeting));
                Holder.txt_note.setText(getResources().getString(R.string.inviitee_has_declined_your_meeting_request));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("4")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.meeting));
                Holder.txt_note.setText(getResources().getString(R.string.wants_to_reschedule_the_meeting));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("5")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.meeting));
                Holder.txt_note.setText(getResources().getString(R.string.has_rescheduled_the_meeting));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("6")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.meeting));
                Holder.txt_note.setText(getResources().getString(R.string.has_cancelled_the_meeting));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("7")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.meeting));
                Holder.txt_note.setText(getResources().getString(R.string.requests_for_meeting_approval));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("8")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.meeting));
                Holder.txt_note.setText(getResources().getString(R.string.has_approved_your_meeting_request));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("9")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.meeting));
                Holder.txt_note.setText(getResources().getString(R.string.has_rejected_your_meeting_request));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getSubject());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("10")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.appointment_p));
                Holder.txt_note.setText(getResources().getString(R.string.has_requested_Appointment_for));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getPurpose());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("11")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.appointment_p));
                Holder.txt_note.setText(getResources().getString(R.string.has_accepted_your_appointment_request_for));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getPurpose());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("12")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.appointment_p));
                Holder.txt_note.setText(getResources().getString(R.string.has_rejected_your_appointment_request_for));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getPurpose());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("13")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.checkIn));
                Holder.txt_note.setText(getResources().getString(R.string.has_checked_in_for));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getPurpose());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("14")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.checkout));
                Holder.txt_note.setText("");
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getPurpose());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("15")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.checkIn));
                Holder.txt_note.setText(getResources().getString(R.string.is_here_to_see_you_for));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getPurpose());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("16")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.checkIn));
                Holder.txt_note.setText(getResources().getString(R.string.has_accepted_you_check_In_request));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getPurpose());
            }else if (notificationsa.get(position).getNtype().equalsIgnoreCase("17")){
                Holder.txt_meeting_type.setText(getResources().getString(R.string.checkIn));
                Holder.txt_note.setText(getResources().getString(R.string.has_declined_you_check_In_request));
                Holder.txt_subject.setText(" " + notificationsa.get(position).getMeetingData().getPurpose());
            }else {

            }


            Holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimationSet animation = Conversions.animation();
                    v.startAnimation(animation);

                    //service
                    notificationsStatus_Change(notificationsa.get(position).getComp_id(),notificationsa.get(position).get_id().get$oid());

                    if (Holder.txt_meeting_type.getText().toString().equalsIgnoreCase(getResources().getString(R.string.appointment_p))){
                        Log.e(TAG, "onClick:txt_meeting_type "+"1" );
                        //meeting description activity
                        Intent intent = new Intent(context, AppointmentDetailsNewActivity.class);
                        intent.putExtra("m_id", notificationsa.get(position).getMid());
                        startActivityForResult(intent, REQUEST_CODE);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }else if (Holder.txt_meeting_type.getText().toString().equalsIgnoreCase(getResources().getString(R.string.meeting))){
                        Log.e(TAG, "onClick:txt_meeting_type "+"2" );
                        //meeting description activity
                        Intent intent = new Intent(context, MeetingDescriptionNewActivity.class);
                        intent.putExtra("m_id", notificationsa.get(position).getMid());
                        startActivity(intent);


                    }else if (Holder.txt_meeting_type.getText().toString().equalsIgnoreCase(getResources().getString(R.string.checkIn))){
                        Log.e(TAG, "onClick:txt_meeting_type "+"3" );

                        Intent intent = new Intent(getApplicationContext(), CheckInDetailsActivity.class);
                        intent.putExtra("_id",notificationsa.get(position).getMid());
                        intent.putExtra("type","appointment");
                        startActivityForResult(intent, REQUEST_CODE);
                        overridePendingTransition(R.anim.enter, R.anim.exit);

//                        final Dialog dialog = new Dialog(NotificationsActivity.this);
//                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setCancelable(false);
//                        dialog.setContentView(R.layout.slot_visitor_popup);
//                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                        final TextView s_name, s_purpose, s_email, s_mobile, s_cancel, s_company;
//                        final CircleImageView s_pic;
//                        s_cancel = dialog.findViewById(R.id.s_cancel);
//                        s_pic = dialog.findViewById(R.id.s_pic);
//                        s_purpose = dialog.findViewById(R.id.s_purpose);
//                        s_company = dialog.findViewById(R.id.s_company);
//                        s_email = dialog.findViewById(R.id.s_email);
//                        s_mobile = dialog.findViewById(R.id.s_mobile);
//                        s_name = dialog.findViewById(R.id.s_name);
//
//                        s_name.setText(notificationsa.get(position).getEmployeeData().getName());
//                        s_purpose.setText(notificationsa.get(position).getMeetingData().getPurpose());
//                        s_email.setText(notificationsa.get(position).getEmail());
////                        s_mobile.setText(notificationsa.get(position).getUserDetails().getMobile());
////                        s_company.setText(notificationsa.get(position).getCompany());
//                        s_cancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                        dialog.show();

                    }else {
                        Log.e(TAG, "onClick:txt_meeting_type "+"4" );
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return notificationsa.size();
        }


        public class ViewHolderToday extends RecyclerView.ViewHolder {

            CardView card_view;
            CircleImageView emp_img;
            TextView txt_meeting_type;
            TextView txt_name;
            TextView txt_note;
            TextView txt_subject;
            TextView txt_date_time;

            public ViewHolderToday(@NonNull View itemView) {
                super(itemView);
                card_view = itemView.findViewById(R.id.card_view);
                emp_img = itemView.findViewById(R.id.emp_img);
                txt_meeting_type = itemView.findViewById(R.id.txt_meeting_type);
                txt_name = itemView.findViewById(R.id.txt_name);
                txt_note = itemView.findViewById(R.id.txt_note);
                txt_subject = itemView.findViewById(R.id.txt_subject);
                txt_date_time = itemView.findViewById(R.id.txt_date_time);
            }
        }

    }

    private void notificationsStatus_Change(String comp_id, String $oid) {
        NotificationsStatusChangeModelRequest notificationsStatusChangeModelRequest = new NotificationsStatusChangeModelRequest(comp_id,$oid);
        apiViewModel.notificationsStatus_Change(getApplicationContext(),notificationsStatusChangeModelRequest);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiViewModel.getnotifications(getApplicationContext(),sharedPreferences1.getString("company_id", null),empData.getEmail());
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        shimmer_view_container.setVisibility(GONE);
        shimmer_view_container.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            // TODO: Do something with your extra data
            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(intent);
        finish();
    }

}
