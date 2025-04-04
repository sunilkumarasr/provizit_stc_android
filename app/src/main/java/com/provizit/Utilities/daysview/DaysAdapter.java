package com.provizit.Utilities.daysview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.provizit.R;

import java.util.ArrayList;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {
    private final ArrayList<DayModel> daysList;
    private final Context context;
    private String selectionType;

    public DaysAdapter(ArrayList<DayModel> daysList, Context context, String selectionType){
        this.daysList = daysList;
        this.context = context;
        this.selectionType = selectionType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayModel day = daysList.get(position);

        if (day.isSelected()){
            holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }else {
            holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        }


        if (position==0){
            holder.name_text_view.setText("M");
        } else if (position==1) {
            holder.name_text_view.setText("T");
        } else if (position==2) {
            holder.name_text_view.setText("W");
        } else if (position==3) {
            holder.name_text_view.setText("T");
        } else if (position==4) {
            holder.name_text_view.setText("F");
        } else if (position==5) {
            holder.name_text_view.setText("S");
        } else if (position==6) {
            holder.name_text_view.setText("S");
        }


        holder.itemView.setOnClickListener(view -> {

            DayModel update_position = null;
            update_position = daysList.get(position);
            if (day.isSelected()){
                update_position.setSelected(false);
                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.gray));
            }else {
                update_position.setSelected(true);
                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
            }

            // Notify adapter of data changes
            notifyDataSetChanged();
        });


    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        TextView name_text_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.cardview);
            name_text_view = itemView.findViewById(R.id.name_text_view);
        }
    }
}