package com.provizit.AdapterAndModel.MeetingRoom;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.Calendar.CalendarAdapter;
import com.provizit.Calendar.MyCalendar;
import com.provizit.R;

import java.util.List;

public class MeetingRoomCalendarAdapter extends RecyclerView.Adapter<MeetingRoomCalendarAdapter.MyViewHolder> {

    public static int defaultSelected;
    private List<MyCalendar> mCalendar;
    WindowManager windowManager;
    FragmentActivity context;
    private int recyclecount=0;
    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView tb_day, tb_date,stamp;
        private RelativeLayout parentview;
        public MyViewHolder(View view) {
            super(view);
            tb_day = (TextView) view.findViewById(R.id.tb_day);
            parentview = (RelativeLayout) view.findViewById(R.id.parentview);
            tb_date = (TextView) view.findViewById(R.id.date_1);
            stamp = (TextView) view.findViewById(R.id.stamp);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displaymetrics);
            int devicewidth = displaymetrics.widthPixels / 7;
            view.getLayoutParams().width = devicewidth;
        }


        @Override
        public void onClick(View v) {
            System.out.println("dsgkdshgkhsdjkgh");
        }
    }
    public MeetingRoomCalendarAdapter(List<MyCalendar> mCalendar, WindowManager windowManager, Integer defaultSelected, FragmentActivity Context) {
        this.mCalendar = mCalendar;
        this.defaultSelected = defaultSelected;
        this.windowManager = windowManager;
        this.context = Context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_room_date_list_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tb_day.setTextColor(Color.BLACK);
        holder.tb_date.setTextColor(Color.BLACK);
        holder.tb_date.setBackgroundResource(0);
        if(defaultSelected == position) {
            System.out.println("Subhhshfghagsfhgafjs" + position);
//            holder.parentview.setBackgroundResource(R.color.colorPrimary);
//            holder.itemView.setBackgroundResource();
            holder.tb_date.setBackgroundResource(R.drawable.ic_circle_white_8dp);
//            holder.tb_date.setTextColor(Color.WHITE);
//            holder.tb_day.setTextColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("dgjdgjjsdklgj");

                Animation startRotateAnimation = AnimationUtils.makeInChildBottomAnimation(context.getApplicationContext());
                holder.tb_date.startAnimation(startRotateAnimation);
                holder.tb_day.startAnimation(startRotateAnimation);
            }
        });
        MyCalendar calendar = mCalendar.get(position);
        holder.tb_day.setText(calendar.getDay());
        holder.tb_date.setText(calendar.getDate());
        holder.stamp.setText(calendar.getTimemilli()+"");
    }
    @Override
    public int getItemCount() {
        return mCalendar.size();
    }

    @Override
    public void onViewRecycled (MyViewHolder holder){
        recyclecount++;
    }
}

