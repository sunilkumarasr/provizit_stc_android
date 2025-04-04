package com.provizit.Config;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.Window;

import com.provizit.R;

public class ProgressLoader {

   // private static Dialog dialog;
    private static ProgressDialog dialog;

    public static void show(Activity activity) {
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

    public static void hide(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

}
