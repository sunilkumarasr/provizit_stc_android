package com.provizit.AdapterAndModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.R;

import java.util.List;

public class MaterialPermitDetailsSetUpAdapter extends RecyclerView.Adapter<MaterialPermitDetailsSetUpAdapter.ViewHolder> {
    private List<MaterialDetailsSet> materialDetailsSetList;

    public MaterialPermitDetailsSetUpAdapter(List<MaterialDetailsSet> list) {
        this.materialDetailsSetList = list;
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
        MaterialDetailsSet materialDetailsModel = materialDetailsSetList.get(position);
        holder.txtDesignation.setText(materialDetailsModel.description);
        holder.txtSerialNumber.setText(materialDetailsModel.serial_num);
        holder.txtQuantity.setText(materialDetailsModel.quantity+"");

    }

    @Override
    public int getItemCount() {
        return materialDetailsSetList.size();
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

