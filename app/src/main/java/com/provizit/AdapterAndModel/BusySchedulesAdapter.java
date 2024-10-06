package com.provizit.AdapterAndModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.provizit.AdapterAndModel.BusySchedules.BusySchedulesData;
import com.provizit.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BusySchedulesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<BusySchedulesData> busyArrayList;
    private Context context;
    private OnItemClickListener onItemClickListener;


    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(BusySchedulesData item,String type);
    }


    public BusySchedulesAdapter(Context context, ArrayList<BusySchedulesData> busyArrayList, OnItemClickListener onItemClickListener) {
        this.busyArrayList = busyArrayList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.busyschedules_list_items, parent, false);
        return new ViewHolder1(view, onItemClickListener, busyArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ViewHolder1 Holder = (ViewHolder1) holder;

        // from date time
        double fromdate = Double.parseDouble(busyArrayList.get(position).getFrom_date());
        long datestampMillis = (long) (fromdate * 1000L);
        Date date = new Date(datestampMillis);
        double fromTime = Double.parseDouble(busyArrayList.get(position).getFrom_time());
        long timestampMillis = (long) (fromTime * 1000L);
        Date time = new Date(timestampMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy, hh:mm a", Locale.getDefault());
        String formattedDateTime = sdf.format(date);
        Holder.txtFrom.setText(formattedDateTime);


        // to date time
        double todate = Double.parseDouble(busyArrayList.get(position).getFrom_date());
        long todatestampMillis = (long) (todate * 1000L);
        Date dateto = new Date(todatestampMillis);
        double toTime = Double.parseDouble(busyArrayList.get(position).getFrom_time());
        long totimestampMillis = (long) (toTime * 1000L);
        Date totime = new Date(totimestampMillis);
        SimpleDateFormat tosdf = new SimpleDateFormat("EEE dd MMM yyyy, hh:mm a", Locale.getDefault());
        String toformattedDateTime = tosdf.format(dateto);
        Holder.txtTo.setText(toformattedDateTime);

    }

    @Override
    public int getItemCount() {
        return busyArrayList.size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {

        ImageView imgEdit,imgDelete;
        TextView txtFrom, txtTo;

        public ViewHolder1(@NonNull View itemView, final OnItemClickListener listener, final ArrayList<BusySchedulesData> items) {
            super(itemView);

            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            txtFrom = itemView.findViewById(R.id.txtFrom);
            txtTo = itemView.findViewById(R.id.txtTo);

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(busyArrayList.get(position), "delete"); // Trigger the click
                    }
                }
            });
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(busyArrayList.get(position), "edit"); // Trigger the click
                    }
                }
            });

        }
    }


}
