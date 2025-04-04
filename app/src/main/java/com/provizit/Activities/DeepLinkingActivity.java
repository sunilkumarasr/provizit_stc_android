package com.provizit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.provizit.Config.Preferences;
import com.provizit.R;
import com.provizit.databinding.ActivityDeepLinkingBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepLinkingActivity extends AppCompatActivity {

    public static final int RESULT_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);

//        Intent intess = new Intent(Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
//        Uri.parse("package:" + getPackageName()));
//        startActivity(intess);

        handleDeepLink(getIntent());

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
                String LOGINCHECK = Preferences.loadStringValue(getApplicationContext(), Preferences.LOGINCHECK, "");
                if (LOGINCHECK != null && !LOGINCHECK.equals("")) {
                    Intent inte = new Intent(DeepLinkingActivity.this, MeetingDescriptionNewActivity.class);
                    inte.putExtra("m_id", commonIdentifier);
                    startActivityForResult(inte, 11); // Use inte instead of intent
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }else {
                    Intent intent1 = new Intent(DeepLinkingActivity.this,SplashActivity.class);
                    startActivity(intent1);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE) {
            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }


}