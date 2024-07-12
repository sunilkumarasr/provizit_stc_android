package com.provizit.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.provizit.R;

public class ReportsNewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ReportsNewActivity";

    LinearLayout linear_my_reports,linear_invitation,linear_location,linear_rooms,linear_category,linear_columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_new);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Reports");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        linear_my_reports = findViewById(R.id.linear_my_reports);
        linear_invitation = findViewById(R.id.linear_invitation);
        linear_location = findViewById(R.id.linear_location);
        linear_rooms = findViewById(R.id.linear_rooms);
        linear_category = findViewById(R.id.linear_category);
        linear_columns = findViewById(R.id.linear_columns);



        linear_my_reports.setOnClickListener(this);
        linear_invitation.setOnClickListener(this);
        linear_location.setOnClickListener(this);
        linear_rooms.setOnClickListener(this);
        linear_category.setOnClickListener(this);
        linear_columns.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_my_reports:
                break;
            case R.id.linear_invitation:
                break;
            case R.id.linear_location:
                break;
            case R.id.linear_rooms:
                break;
            case R.id.linear_category:
                break;
            case R.id.linear_columns:
                break;
        }
    }


}