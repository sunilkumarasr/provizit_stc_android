package com.provizit.AdapterAndModel.AllotParking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.Config.CustomItemCityListener;
import com.provizit.R;
import com.provizit.Utilities.CompanyData;

import java.util.ArrayList;

public class AllotParkingSlotsAdapter extends RecyclerView.Adapter<AllotParkingSlotsAdapter.ViewHolder>{


    private static final String TAG = "PackagesAdapter";
    private CustomItemCityListener customItemClick;
    Context context;
    ArrayList<CompanyData> my_data;
    CompanyData stateModel;


    public AllotParkingSlotsAdapter(ArrayList<CompanyData> my_data,  Context context, CustomItemCityListener customItemClick) {
        this.my_data = my_data;
        this.context = context;
        this.customItemClick = customItemClick;

    }

    @Override
    public AllotParkingSlotsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_items, parent, false);
        return new AllotParkingSlotsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AllotParkingSlotsAdapter.ViewHolder holder, int position) {
        stateModel = my_data.get(position);
        holder.txt_service.setText(my_data.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_service;
        public CardView card1;


        public ViewHolder(View itemView) {
            super(itemView);

            txt_service = itemView.findViewById(R.id.txt_service);
            card1 = itemView.findViewById(R.id.card1);


            card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = my_data.get(getAdapterPosition()).get_id().get$oid();
                    String Name = my_data.get(getAdapterPosition()).getName();
                    customItemClick.onItemClick(view, id, Name,"");
                }
            });


        }
    }


}

