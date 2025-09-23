package com.provizit.AdapterAndModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.R;
import com.provizit.Utilities.SubContractor;

import java.util.List;

public class MaterialPermitDetailsAdapter extends RecyclerView.Adapter<MaterialPermitDetailsAdapter.ViewHolder> {
    private List<MaterialDetailsModel> materialDetailsModelList;

    public MaterialPermitDetailsAdapter(List<MaterialDetailsModel> list) {
        this.materialDetailsModelList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MaterialDetailsModel materialDetailsModel = materialDetailsModelList.get(position);
        holder.txtDesignation.setText(materialDetailsModel.getDescription());
        holder.txtSerialNumber.setText(materialDetailsModel.getSerial_num());
        holder.txtQuantity.setText(materialDetailsModel.getQuantity()+"");

    }

    @Override
    public int getItemCount() {
        return materialDetailsModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDesignation, txtSerialNumber, txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDesignation = itemView.findViewById(R.id.txtDesignation);
            txtSerialNumber = itemView.findViewById(R.id.txtSerialNumber);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
        }
    }
}

