package com.provizit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.provizit.Config.ViewController;
import com.provizit.Conversions;
import com.provizit.FragmentDailouge.UpdatePopUpFragment;
import com.provizit.Fragments.MeetingRoomFragment;
import com.provizit.Fragments.UpcomingMeetingsNewFragment;
import com.provizit.Logins.InitialActivity;
import com.provizit.MVVM.ApiViewModel;
import com.provizit.Config.Preferences;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Services.Notifications.NotificationsComon;
import com.provizit.UserInterationListener;
import com.provizit.Utilities.CommonObject;
import com.provizit.Utilities.CompanyData;
import com.provizit.Utilities.DatabaseHelper;
import com.provizit.Utilities.EmpData;
import com.provizit.Utilities.Inviteeitem;
import com.provizit.Utilities.RoleDetails;
import com.provizit.databinding.ActivityNavigationBinding;
import java.util.ArrayList;
import java.util.Objects;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityNavigationBinding binding;

    BroadcastReceiver broadcastReceiver;

    private FragmentTransaction fragmentTransaction;

    private boolean isMainActivityShown = true;

    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;
    SharedPreferences.Editor editor1;
    RoleDetails roleDetails;
    private UserInterationListener userInteractionListener;

    AnimationSet animation;

    //vestion
    ArrayList<CompanyData> versionData;

    //notification count
    ArrayList<NotificationsComon> notificationslist1;
    ArrayList<NotificationsComon> notificationslist;
    ArrayList<NotificationsComon> notificationslist_final;
    ApiViewModel apiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();


    }

    private void initView() {
        ViewController.barPrimaryColorWhite(NavigationActivity.this);
        ViewController.internetConnection(broadcastReceiver, NavigationActivity.this);
        Preferences.saveStringValue(getApplicationContext(), Preferences.LOGINCHECK, "Login");
        sharedPreferences1 = getSharedPreferences("EGEMSS_DATA", 0);
        editor1 = sharedPreferences1.edit();
        myDb = new DatabaseHelper(this);
        empData = new EmpData();
        roleDetails = new RoleDetails();
        empData = myDb.getEmpdata();
        roleDetails = myDb.getRole();
        animation = Conversions.animation();
        apiViewModel = new ViewModelProvider(NavigationActivity.this).get(ApiViewModel.class);
        apiViewModel.getVersions(getApplicationContext());
        Intent intent = getIntent();
        binding.empName.setText(empData.getName());
        binding.empRole.setText(empData.getDesignation());
        if (!sharedPreferences1.getString("company_id", "").equals("")) {
            apiViewModel.getazureaddetails(getApplicationContext(), sharedPreferences1.getString("company_id", null));
            apiViewModel.getamenities(getApplicationContext());
            apiViewModel.getnotifications(getApplicationContext(), sharedPreferences1.getString("company_id", null), empData.getEmail());
        }
        notificationslist1 = new ArrayList<>();
        notificationslist = new ArrayList<>();
        notificationslist_final = new ArrayList<>();
        if (empData.getEmp_image1().equals("")) {
            Resources res = getResources();
        } else {
            String Profile_Url = Preferences.loadStringValue(getApplicationContext(), Preferences.Profile_Url, "");
            if (Profile_Url.equalsIgnoreCase("") || Profile_Url == null) {
                Glide.with(NavigationActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + empData.getEmp_image1())
                        .into(binding.empImg);
            } else {
                Glide.with(NavigationActivity.this).load(Profile_Url)
                        .into(binding.empImg);
            }
        }
        int M_SUGGETION = intent.getIntExtra("M_SUGGETION", 0);

        if (M_SUGGETION == 1) {
            MeetingRoomFragment meetingRoomFragment = new MeetingRoomFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, meetingRoomFragment, "meetingRoomFragment");
            fragmentTransaction.commit();
            binding.imgMeeting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            binding.txtMeeting.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.imgHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), PorterDuff.Mode.SRC_ATOP);
            binding.txtHome.setTextColor(getResources().getColor(R.color.gray));
        } else {
            UpcomingMeetingsNewFragment home1 = new UpcomingMeetingsNewFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, home1, "Home");
            fragmentTransaction.commit();
            binding.imgMeeting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), PorterDuff.Mode.SRC_ATOP);
            binding.txtMeeting.setTextColor(getResources().getColor(R.color.gray));
            binding.imgHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            binding.txtHome.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        clickEvents();
        apiInitView();


    }

    private void clickEvents() {
        binding.linearlayoutMeetingroom.setOnClickListener(this);
        binding.linearlayoutHome.setOnClickListener(this);
        binding.frameLayoutNotifications.setOnClickListener(this);
        binding.imgReports.setOnClickListener(this);
        binding.imgBusy.setOnClickListener(this);
        binding.empImg.setOnClickListener(this);
        binding.empName.setOnClickListener(this);
    }

    private void apiInitView() {
        apiViewModel.getVersions_response().observe(this, response -> {
            if (response != null) {
                Integer statuscode = response.getResult();
                Integer successcode = 200, failurecode = 201, not_verified = 404;
                if (statuscode.equals(failurecode)) {

                } else if (statuscode.equals(not_verified)) {

                } else if (statuscode.equals(successcode)) {
                    versionData = new ArrayList<>();
                    versionData = response.getItems();
                    if (versionData != null || versionData.size() != 0) {
                        if (versionData.get(0).getAndroid().equalsIgnoreCase("1.8")) {

                        } else {
                            // update_popup();
                        }
                    }

                }
            }else {
                Conversions.errroScreen(NavigationActivity.this,"apiInitView");
            }
        });

        apiViewModel.getazureaddetailsResponse().observe(this, response -> {
            if (response != null) {
                //Toast.makeText(getApplicationContext(),response.getItems().getOnline()+"",Toast.LENGTH_SHORT).show();
                Preferences.saveStringValue(getApplicationContext(), Preferences.AdOnline, response.getItems().getOnline()+"");
            }else {
                Conversions.errroScreen(NavigationActivity.this, "getazureaddetails");
            }
        });

        apiViewModel.getamenitiesResponse().observe(this, response -> {
            Inviteeitem companyData = new Inviteeitem();
            companyData = response.getItems();
            ArrayList<CommonObject> commonObjectArrayList = companyData.getAmenities();
            myDb.insertAmenities(commonObjectArrayList);
        });

        //notifications count
        apiViewModel.getnotificationsListResponse().observe(this, response -> {
            if (response != null) {
                notificationslist1 = response.getItems();
                notificationslist.addAll(notificationslist1);
                binding.txtNotificationsCount.setText(String.valueOf(notificationslist.size()));
            }else {
                Conversions.errroScreen(NavigationActivity.this, "getnotificationsList");
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearlayout_meetingroom:
                view.startAnimation(animation);
                MeetingRoomFragment meetingRoomFragment = new MeetingRoomFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, meetingRoomFragment, "meetingRoomFragment");
                fragmentTransaction.commit();
                binding.imgMeeting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                binding.txtMeeting.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.imgHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), PorterDuff.Mode.SRC_ATOP);
                binding.txtHome.setTextColor(getResources().getColor(R.color.gray));
                break;
            case R.id.linearlayout_home:
                view.startAnimation(animation);
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.frame_layout_notifications:
                view.startAnimation(animation);
                Intent intents = new Intent(getApplicationContext(), NotificationsActivity.class);
                startActivity(intents);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.img_reports:
                view.startAnimation(animation);
                Intent intent1 = new Intent(getApplicationContext(), ReportsNewActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.imgBusy:
                view.startAnimation(animation);
                Intent inten = new Intent(getApplicationContext(), BusyScheduleActivity.class);
                startActivity(inten);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.emp_img:
                view.startAnimation(animation);
                profileClick();
                break;
            case R.id.emp_name:
                view.startAnimation(animation);
                profileClick();
                break;
            case R.id.logout:
                view.startAnimation(animation);
                myDb.logout();
                editor1.putString("language", "en");
                editor1.apply();
                Intent intent2 = new Intent(NavigationActivity.this, InitialActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent2);
                finish();
                break;
        }
    }

    public void profileClick() {
        Intent intent = new Intent(NavigationActivity.this, ProfileDetailsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void setUserInteractionListener(UserInterationListener userInteractionListener) {
        this.userInteractionListener = userInteractionListener;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (userInteractionListener != null)
            userInteractionListener.onUserInteraction();
    }

    private void update_popup() {
        FragmentManager fm = getSupportFragmentManager();
        UpdatePopUpFragment sFragment = new UpdatePopUpFragment();
        // Show DialogFragment
        sFragment.onItemsSelectedListner((comId, DepName, CompName) -> {
        });
        sFragment.show(fm, "Dialog Fragment");
        sFragment.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("BackOnclick");
        String Profile_Url = Preferences.loadStringValue(getApplicationContext(), Preferences.Profile_Url, "");
        if (Profile_Url.equalsIgnoreCase("") || Profile_Url == null) {
            myDb.getEmpdata();
            if (!empData.getEmp_image1().equals("")) {
                Glide.with(NavigationActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("company_id", null) + "/" + myDb.getEmpdata().getEmp_image1())
                        .into(binding.empImg);
            }
        } else {
            Glide.with(NavigationActivity.this).load(Profile_Url).into(binding.empImg);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back_popup();
    }
    private void back_popup() {
        final Dialog dialog = new Dialog(NavigationActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_back_press);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.title);
        TextView title_note = dialog.findViewById(R.id.title_note);
        TextView txt_yes = dialog.findViewById(R.id.txt_yes);
        TextView txt_no = dialog.findViewById(R.id.txt_no);

        title.setText(getResources().getString(R.string.app_name));
        title_note.setText(getResources().getString(R.string.Do_you_want_to_exit_the_application));
        txt_yes.setText(getResources().getString(R.string.Yes));
        txt_no.setText(getResources().getString(R.string.No));

        txt_yes.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            editor1.remove("m_id");
            editor1.remove("m_action");
            editor1.apply();
            if (isMainActivityShown) {
                finishAffinity();
            }
            dialog.dismiss();
        });
        txt_no.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });
        dialog.show();

    }


}
