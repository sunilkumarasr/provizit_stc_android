package com.provizit.AdapterAndModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.Activities.SetupMeetingActivity;
import com.provizit.Config.CustomItemCityListener;
import com.provizit.Config.Customthree;
import com.provizit.R;
import com.provizit.interfaces.MultipleContactsSelectionCallBack;
import java.util.ArrayList;
import java.util.LinkedList;

public class MultiplePhoneNumberAdapter extends RecyclerView.Adapter {

    private Context context;
    private LinkedList<String> listOfPhoneNumbers;
    private Customthree customItemClick;

    private int selectedPosition = 0;

    public MultiplePhoneNumberAdapter(Context context, LinkedList<String> listOfPhoneNumbers, Customthree customItemClick) {
        this.context = context;
        this.listOfPhoneNumbers = listOfPhoneNumbers;
        this.customItemClick = customItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_multiple_phone_number_items, parent, false);
        return new MenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        MenuViewHolder holder = (MenuViewHolder) viewHolder;

        String phoneNumber = listOfPhoneNumbers.get(position).replaceAll(" ", "");

        holder.tvPhoneNumber.setText(phoneNumber);
        holder.checkBox.setChecked(position == selectedPosition);

        if (position == selectedPosition){
            customItemClick.onItemClick(phoneNumber, "","");
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            customItemClick.onItemClick(phoneNumber, "","");
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return listOfPhoneNumbers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class MenuViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView tvPhoneNumber;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
        }

    }


}