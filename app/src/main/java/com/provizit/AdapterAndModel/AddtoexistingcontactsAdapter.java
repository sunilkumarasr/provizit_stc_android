package com.provizit.AdapterAndModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.provizit.CustomItemListener;
import com.provizit.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddtoexistingcontactsAdapter extends RecyclerView.Adapter {

    private CustomItemListener customItemClick;
    Context context;
    List<ContactsList> data;
    List<ContactsList> contactsLists;
    ContactsList deliveryEarningListModel;

    public AddtoexistingcontactsAdapter(Context context, List<ContactsList> contactsLists, CustomItemListener customItemClick) {
        this.context = context;
        this.data = contactsLists;
        this.contactsLists = contactsLists;
        this.customItemClick = customItemClick;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contacts_list_items, parent, false);
        return new AddtoexistingcontactsAdapter.ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ContactViewHolder holders = (ContactViewHolder) holder;
        deliveryEarningListModel = contactsLists.get(position);
        holders.txt_name.setText("Name: "+deliveryEarningListModel.getName());
        holders.txt_phone.setText("Mobile: "+deliveryEarningListModel.getPhone());
        holders.txt_email.setText("Email: "+deliveryEarningListModel.getEmail());


        if (deliveryEarningListModel.getPhote() != null) {
            Glide.with(context).load(deliveryEarningListModel.getPhote()).into(holders.img_profile);
        } else {
            holders.img_profile.setImageResource(R.drawable.user);
        }

        holders.checkbox.setVisibility(View.GONE);

    }



    @Override
    public int getItemCount() {
        return contactsLists.size();
    }


    public void filter(String query) {
        data = new ArrayList<>();
        for (ContactsList item : contactsLists) {
            if (item.getName().contains(query)) {
                data.add(item);
            }
        }
        contactsLists = data;
        notifyDataSetChanged();
    }


    private class ContactViewHolder extends RecyclerView.ViewHolder {

        CardView card_v;
        CircleImageView img_profile;
        TextView txt_name;
        TextView txt_phone;
        TextView txt_email;
        CheckBox checkbox;



        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            card_v = itemView.findViewById(R.id.card_v);
            img_profile = itemView.findViewById(R.id.img_profile);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_phone = itemView.findViewById(R.id.txt_phone);
            txt_email = itemView.findViewById(R.id.txt_email);
            checkbox = itemView.findViewById(R.id.checkbox);

            card_v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Id = contactsLists.get(getAdapterPosition()).getId();
                    customItemClick.onItemClick(v, "", Id);
                }
            });


        }
    }

    public void updateList(ArrayList<ContactsList> list) {
        contactsLists = list;
        notifyDataSetChanged();
    }

}
