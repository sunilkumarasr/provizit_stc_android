package com.provizit.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.provizit.Config.Preferences;
import com.provizit.Config.ViewController;
import com.provizit.Logins.LoginMicrosoftADActivity;
import com.provizit.R;
import com.provizit.Services.DataManger;
import com.provizit.Utilities.CompanyDetails;
import com.provizit.Utilities.DatabaseHelper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.provizit.Utilities.EmpData;
import com.provizit.databinding.ActivitySplashBinding;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    ActivitySplashBinding activitySplashBinding;

    Animation animationUp;
    SharedPreferences.Editor editor1;
    SharedPreferences sharedPreferences1;
    DatabaseHelper myDb;
    EmpData empData;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(activitySplashBinding.getRoot());
        ViewController.barPrimaryColorWhite(SplashActivity.this);

        handleDeepLink(getIntent());

        Location lastKnownLocation = getLastKnownLocation(this);
        if (lastKnownLocation != null) {
            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();
            Preferences.saveStringValue(getApplicationContext(), Preferences.latitude, latitude+"");
            Preferences.saveStringValue(getApplicationContext(), Preferences.longitude, longitude+"");
            //Toast.makeText(getApplicationContext(),latitude+"-"+longitude ,Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Last known location is null");
        }

        myDb = new DatabaseHelper(this);
        empData = new EmpData();
        empData = myDb.getEmpdata();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                return;
            }
            // Get new FCM registration token
            String token = task.getResult();
            // Log and toast
//                          String msg = getString(R.string.msg_token_fmt, token);
            Log.d("TAG", token);
        });

        sharedPreferences1 = SplashActivity.this.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);

        editor1 = sharedPreferences1.edit();
        if (sharedPreferences1.getString("language", "").equals("ar")) {
            Locale myLocale = new Locale("ar");
            DataManger.appLanguage = "ar";
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            conf.setLayoutDirection(myLocale);
            res.updateConfiguration(conf, dm);
        } else {
            Locale myLocale = new Locale("en");
            DataManger.appLanguage = "en";
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            conf.setLayoutDirection(myLocale);
            res.updateConfiguration(conf, dm);
        }

        Log.e(TAG, "onCreate:m " + getIntent().getStringExtra("m_id") + "");

//        if (getIntent().hasExtra("pushnotification") && getIntent().getStringExtra("m_id").equals("3")) {
//            Intent intent = new Intent(this, MeetingDescriptionActivity.class);
//            System.out.println("m_id"+getIntent().getStringExtra("m_id"));
//            intent.putExtra("m_id", getIntent().getStringExtra("m_id"));
//            startActivity(intent);
//            finish();
//        } else {
//            StoreContacts();
//        }

        Log.e(TAG, "onCreate:emp " + empData.getEmp_id());

        Intent notifyIntent = getIntent();
        if (notifyIntent != null) {
            Log.d(":asd", "asd");
            Bundle bundle = notifyIntent.getExtras();
            if (bundle != null) {
                Log.d("asd", "asd");
                if (bundle.containsKey("mid") && bundle.containsKey("mtype")) {
                    String mId = bundle.getString("mid");
                    String mtype = bundle.getString("mtype");
                    Set<String> keys = bundle.keySet();
                    Log.d("asd", keys.toString());
//                Intent intent = new Intent(this, MeetingDescriptionActivity.class);
//                System.out.println("m_id"+bundle.getString("mid")+"");
//                intent.putExtra("m_id", bundle.getString("mid")+"");
//                intent.putExtra("meeting", "");
//                intent.putExtra("form_type", "notification");
//                startActivity(intent);
//                finish();
                    Log.e(TAG, "onCreate:em" + "empty");
                    Intent intents = new Intent(this, NotificationsActivity.class);
                    startActivity(intents);
                } else {
                    storeContacts();
                }
            } else {
                storeContacts();
            }
        } else {
            storeContacts();
        }


    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Handle deep link data
        handleDeepLink(intent);
    }
    private void handleDeepLink(Intent intent) {

        Uri data = intent.getData();
        if (data != null) {
            String url = data.toString();
            String commonIdentifier = parseDeepLink(url);
            if (commonIdentifier != null) {
                // Check if the toast message has already been displayed
                boolean toastDisplayed = false;
                if (!toastDisplayed) {
                    Toast.makeText(getApplicationContext(), commonIdentifier, Toast.LENGTH_SHORT).show();
                    toastDisplayed = true; // Set the flag to true to indicate that the toast message has been displayed
                }

                String LOGINCHECK = Preferences.loadStringValue(getApplicationContext(), Preferences.LOGINCHECK, "");
                if (LOGINCHECK != null && !LOGINCHECK.equals("")) {
                    Intent inte = new Intent(SplashActivity.this, MeetingDescriptionNewActivity.class);
                    inte.putExtra("m_id", commonIdentifier);
                    startActivityForResult(inte, 11); // Use inte instead of intent
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        }
    }

    public static String parseDeepLink(String url) {
        String pattern = "(?:https?://)?(?:[^./]+\\.)*[^./]+/(?:linkdetails/)?(\\w+)/?";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);

        if (m.find()) {
            return m.group(1); // Extract the common identifier
        } else {
            return null;
        }
    }

    private Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = null;
        try {
            // Get last known location from the network provider
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException: " + e.getMessage());
        }
        return lastKnownLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SplashAnimation();
                }
                break;
            case 102:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
        }
    }

    private Boolean permissionsGranted() {
        return ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private Boolean permissionsGranted1() {
        return ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    private Boolean permissionsGranted2() {
        return ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    private Boolean permissionsGranted3() {
        return ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private Boolean permissionsGranted4() {
        return ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void storeContacts() {
        if (permissionsGranted() && permissionsGranted1() && permissionsGranted2() && permissionsGranted3() && permissionsGranted4()) {
            SplashAnimation();
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
    }

    public void SplashAnimation() {
        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        activitySplashBinding.upImage.setAnimation(animationUp);
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1200);
                } catch (Exception e) {
                } finally {
                    Cursor res = myDb.getAllData();
                    editor1.remove("m_id");
                    editor1.remove("m_action");
                    editor1.apply();
                    if (res.getCount() == 0) {
                        Intent intent = new Intent(SplashActivity.this, LoginMicrosoftADActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        while (res.moveToNext()) {
                            Integer Status = res.getInt(4);
                            if (Status == 1) {
                                String LOGINCHECK = Preferences.loadStringValue(getApplicationContext(), Preferences.LOGINCHECK, "");

                                if (LOGINCHECK != null && !LOGINCHECK.equals("")) {
                                    new CompanyDetails(SplashActivity.this).execute(sharedPreferences1.getString("company_id", null));
                                    Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }

                            }else {
                                Intent intent = new Intent(SplashActivity.this, LoginMicrosoftADActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            }
        };
        t.start();
    }


}


