package com.provizit.AdapterAndModel;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.Config.CustomItemCityListener;
import com.provizit.Config.Customthree;
import com.provizit.R;
import com.provizit.interfaces.MultipleContactsSelectionCallBack;
import com.provizit.interfaces.MultipleEmailAddressSelectionCallBack;

import java.util.ArrayList;
import java.util.LinkedList;

public class MultipleEmailAddressAdapter extends RecyclerView.Adapter{

    private Context context;
    private LinkedList<String> listOfEmailAddress;
    private Customthree customItemClick;
    private int selectedPosition = 0;

    public MultipleEmailAddressAdapter(Context context, LinkedList<String> listOfEmailAddress, Customthree customItemClick) {
        this.context = context;
        this.listOfEmailAddress = listOfEmailAddress;
        this.customItemClick = customItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_multiple_email_address_items, parent, false);

        return new MultipleEmailAddressAdapter.MenuViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MultipleEmailAddressAdapter.MenuViewHolder holder = (MultipleEmailAddressAdapter.MenuViewHolder) viewHolder;

        String emailAddress = listOfEmailAddress.get(position);
        holder.tvEmailAddress.setText(emailAddress);

        holder.checkBox.setChecked(position == selectedPosition);

        if (position == selectedPosition){
            customItemClick.onItemClick(listOfEmailAddress.get(position), "","");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                customItemClick.onItemClick(listOfEmailAddress.get(position), "","");
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listOfEmailAddress.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class MenuViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView tvEmailAddress;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox);
            tvEmailAddress = itemView.findViewById(R.id.tvEmailAddress);

        }


    }

}
