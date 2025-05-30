package com.provizit;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.provizit.Activities.MeetingDescriptionNewActivity;
import com.provizit.Activities.NavigationActivity;
import com.provizit.Activities.SplashActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    private static int count = 0;
    NotificationManagerCompat notificationManager;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived: "+remoteMessage.getData() );

        System.out.println("NotificationData"+ remoteMessage.getData());
        System.out.println("NotificationData"+ remoteMessage.getData().get("mtype"));
        System.out.println("NotificationData"+ remoteMessage.getData().get("mid"));

        Log.e(TAG, "onMessageReceived:m "+remoteMessage.getData().get("mid") );


//        sendNotification(remoteMessage.getNotification().getTitle());
      sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getData());

    }

    private void sendNotification(String title, String messageBody, Map<String, String> data) {
        System.out.println("m_id_send"+data.get("mid")+"");
        Log.e(TAG, "onMessageReceived:mid "+data.get("mid") );

//        Bundle simple_bundle=new Bundle();
//        simple_bundle.putString("mid",data.get("mid"));
//        simple_bundle.putString("mtype",data.get("mtype"));
//
//        notificationManager = NotificationManagerCompat.from(this);
//
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtras(simple_bundle);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 ,intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification notification = new NotificationCompat.Builder(this, "Seasame")
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle("Just In")
//                .setContentText(messageBody)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .build();
//        notificationManager.notify(1, notification);

        PendingIntent pendingIntent;
        if(!Objects.equals(data.get("mid"), "")){
//            Intent intent = new Intent(getApplicationContext(), MeetingDescriptionActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("m_id", data.get("mid"));
//            intent.putExtra("meeting", "");
//            intent.putExtra("form_type", "notification");
//            pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);

            Intent intent = new Intent(getApplicationContext(), MeetingDescriptionNewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }else{
            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //For Android Version Orio and greater than orio.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel("Sesame", "Sesame", importance);
            mChannel.setDescription(messageBody);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotifyManager.createNotificationChannel(mChannel);
        }
//For Android Version lower than oreo.
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Seasame");
        mBuilder.setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.logo)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_sesame))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(Color.parseColor("#FFD600"))
                .setContentIntent(pendingIntent)
                .setChannelId("Sesame")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        mNotifyManager.notify(count, mBuilder.build());
        count++;
    }


}
