package com.provizit.Config;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewController {


    // private static Dialog dialog;
    private static ProgressDialog dialog;

    public static void barPrimaryColor(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
    }
    public static void barPrimaryColorWhite(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.white));
    }
    @SuppressLint("ResourceAsColor")
    public static void snackbar(Activity activity, String result, int color) {
        Snackbar customSnackbar = Snackbar.make(activity.findViewById(android.R.id.content), result, Snackbar.LENGTH_SHORT);
        customSnackbar.getView().setBackgroundColor(color);
        customSnackbar.show();
    }

    public static void internetConnection(BroadcastReceiver broadcastReceiver, Activity activity) {
        //internet connection
        broadcastReceiver = new ConnectionReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!isConnecteds(context)){
                    int colorInt = ContextCompat.getColor(activity, R.color.red);
                    ViewController.snackbar(activity,activity.getString(R.string.net_connection),colorInt);
                }
            }
        };
        registoreNetWorkBroadcast(broadcastReceiver,activity);
    }
    public static void registoreNetWorkBroadcast(BroadcastReceiver broadcastReceiver,Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            activity.registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public static void ShowProgressBar(Activity activity){

        //        dialog  = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.custom_progress_loader);
//        dialog.show();


        if (dialog == null || !dialog.isShowing()) {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false); // Prevent dialog from being canceled by back button
            dialog.setCanceledOnTouchOutside(false); // Prevent dialog from being canceled by touch outside
            dialog.show();
        }
    }
    public static void DismissProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public static String Create_date_month_year_hr_mm_am_pm(Long date){
        DateFormat createsimple = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        String Cdatetime = createsimple.format(date) + "";
        return Cdatetime;
    }

    public static String Date_month_year(Long date){
        DateFormat createsimple = new SimpleDateFormat("dd MMM yyyy");
        String date_M_Y = createsimple.format(date) + "";
        return date_M_Y;
    }
    public static String getSdate(Long s_date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date();
        now.setTime(s_date);
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static void clearCache(Context context) {
        try {
            context.getCacheDir().delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(Constant.PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isEmailValid(String email) {
        String expression = Constant.EMAIL_PATTERN;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void getCurrentLatLongs(Activity activity) {
        Location lastKnownLocation = getLastKnownLocation(activity);
        if (lastKnownLocation != null) {
            saveLocation(activity, lastKnownLocation);
        } else {
            // Handle case where last known location is null
        }
    }
    private static Location getLastKnownLocation(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        // Get last known location from GPS provider
        Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocationGPS != null) {
            return lastKnownLocationGPS;
        }
        // If GPS provider didn't return a location, try with network provider
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }
    private static void saveLocation(Activity activity, Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Preferences.saveStringValue(activity, Preferences.latitude, String.valueOf(latitude));
        Preferences.saveStringValue(activity, Preferences.longitude, String.valueOf(longitude));
        Toast.makeText(activity,latitude+"-"+longitude ,Toast.LENGTH_SHORT).show();
    }



}
