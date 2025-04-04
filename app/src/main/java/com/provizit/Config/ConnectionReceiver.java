package com.provizit.Config;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.provizit.Activities.NavigationActivity;
import com.provizit.R;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public boolean isConnecteds(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

//            if (!networkInfo.isAvailable()) {
//                // do something
//
//                Toast.makeText(context,"no _",Toast.LENGTH_SHORT).show();
//            }else {
//
//                Toast.makeText(context,"yes _",Toast.LENGTH_SHORT).show();
//
//            }

            return (networkInfo != null && networkInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }


}