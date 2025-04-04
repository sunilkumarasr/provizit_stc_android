package com.provizit.Activities;

import static android.view.View.GONE;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.provizit.Conversions;
import com.provizit.Config.ConnectionReceiver;
import com.provizit.Logins.ForgotActivity;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.R;
import com.provizit.Services.Model1;
import com.provizit.Utilities.CompanyData;

import org.json.JSONObject;

import java.util.ArrayList;

public class MeetingRoomEngagedActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;

    TextView m_name,m_time,change;
    RecyclerView meetingRoomsrecycler;
    SharedPreferences sharedPreferences1;
    SharedPreferences.Editor editor1;
    String location;
    JSONObject obj;
    Long start,end;

    Adapter1 meetingroomAdapter;

    ApiViewModel apiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_room_engaged);
        ResourceIds();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MeetingRoomEngagedActivity.this,R.color.white));
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

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


        apiViewModel = new ViewModelProvider(MeetingRoomEngagedActivity.this).get(ApiViewModel.class);

        sharedPreferences1 = MeetingRoomEngagedActivity.this.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
        editor1 = sharedPreferences1.edit();
        String json = sharedPreferences1.getString("SetupMeetingJson", "");
        JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
        m_name.setText(sharedPreferences1.getString("SetupMeetingMRoom",""));
        location = convertedObject.get("location").getAsString();
        start = convertedObject.get("start").getAsLong();
        end = convertedObject.get("end").getAsLong() + 1;
//        Invited[] invitedArrayList = new Gson().fromJson(convertedObject.get("invites").getAsJsonArray(),Invited[].class);

        String time = Conversions.millitotime((start+Conversions.timezone()) * 1000,false) + " - " +Conversions.millitotime((end+Conversions.timezone()) * 1000,false);
        m_time.setText(time);


        apiViewModel.getmrmslots(getApplicationContext(),"end", start, end, location);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MeetingRoomEngagedActivity.this, LinearLayoutManager.VERTICAL, false);
        meetingRoomsrecycler.setLayoutManager(linearLayoutManager);
        change.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            Intent intent = new Intent(MeetingRoomEngagedActivity.this, NavigationActivity.class);
            intent.putExtra("M_SUGGETION",1);
            startActivity(intent);
        });


        apiViewModel.getmrmslots_response().observe(this, new Observer<Model1>() {
            @Override
            public void onChanged(Model1 response) {
                if (response != null) {
                    Integer statuscode = response.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;

                    if (statuscode.equals(successcode)){
                        ArrayList<CompanyData> meetingrooms = new ArrayList<>();

                        if (response.getItems().size() > 0) {
                            for (int i = 0; i < response.getItems().size(); i++) {
                                CompanyData item = response.getItems().get(i);
                                if (Boolean.TRUE.equals(item.getActive())) {
                                    meetingrooms.add(item);
                                }
                            }
                        }

                        meetingroomAdapter = new Adapter1(MeetingRoomEngagedActivity.this, meetingrooms);
                        meetingRoomsrecycler.setAdapter(meetingroomAdapter);
                    }

                }else {
                    Conversions.errroScreen(MeetingRoomEngagedActivity.this, "getmrmslots");
                }
            }
        });


    }

    protected void registoreNetWorkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void ResourceIds(){
        m_name = findViewById(R.id.m_name);
        change = findViewById(R.id.change);
        m_time = findViewById(R.id.m_time);
        meetingRoomsrecycler = findViewById(R.id.recycler);
    }
    public class Adapter1 extends RecyclerView.Adapter<Adapter1.MyviewHolder> {
        Context context;
        ArrayList<CompanyData> meetingrooms;

        public class MyviewHolder extends RecyclerView.ViewHolder {
            TextView name;
            RadioButton radioButton1;
            CardView cardview;

            public MyviewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.name);
                radioButton1 = view.findViewById(R.id.radioButton1);
                cardview = view.findViewById(R.id.cardview);

            }
        }


        public Adapter1(Context context, ArrayList<CompanyData> meetingrooms) {
            this.context = context;
            this.meetingrooms = meetingrooms;
        }

        @Override
        public Adapter1.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.meeting_romms, parent, false);
            return new Adapter1.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Adapter1.MyviewHolder holder, final int position) {

            String str1 = " ("+meetingrooms.get(position).getCapacity() + " Persons)";

            holder.name.setText((position+1) +"." + meetingrooms.get(position).getName()+str1 );



            holder.radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    editor1.putString("SetupMeetingMRoom",meetingrooms.get(position).getName());
                    editor1.putString("SetupMeetingMRoomID",meetingrooms.get(position).get_id().get$oid());
                    editor1.apply();
                    Intent intent = new Intent(MeetingRoomEngagedActivity.this, SetupMeetingActivity.class);
                    intent.putExtra("M_SUGGETION",1);
                    startActivity(intent);
                }
            });
            holder.cardview.setOnClickListener(view -> {
                holder.radioButton1.setChecked(true);
                editor1.putString("SetupMeetingMRoom",meetingrooms.get(position).getName());
                editor1.putString("SetupMeetingMRoomID",meetingrooms.get(position).get_id().get$oid());
                editor1.apply();
                Intent intent = new Intent(MeetingRoomEngagedActivity.this, SetupMeetingActivity.class);
                intent.putExtra("M_SUGGETION",1);
                startActivity(intent);
            });


        }
        @Override
        public int getItemCount() {
            return meetingrooms.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        }
        return super.onOptionsItemSelected(item);
    }

}