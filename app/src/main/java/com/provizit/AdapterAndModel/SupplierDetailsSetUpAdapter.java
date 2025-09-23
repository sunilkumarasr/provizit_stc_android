package com.provizit.AdapterAndModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.R;

import java.util.List;

public class SupplierDetailsSetUpAdapter extends RecyclerView.Adapter<SupplierDetailsSetUpAdapter.ViewHolder> {

    private final List<SupplierDetails> subContractorList;

    public SupplierDetailsSetUpAdapter(List<SupplierDetails> list) {
        this.subContractorList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suppliers_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupplierDetails supplierDetails = subContractorList.get(position);
        holder.txtName.setText(supplierDetails.contact_person);
        holder.txtEmail.setText(supplierDetails.supplier_email);
        holder.txtMobile.setText(supplierDetails.supplier_mobile.internationalNumber);
        holder.txtIdNumber.setText(supplierDetails.id_number);
        holder.txtNationality.setText(supplierDetails.nationality);
        holder.txtVehicleNumber.setText(supplierDetails.vehicle_no);
        holder.txtVehicleType.setText(supplierDetails.vehicle_type);
    }

    @Override
    public int getItemCount() {
        return subContractorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtMobile, txtIdNumber, txtNationality, txtVehicleNumber, txtVehicleType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtIdNumber = itemView.findViewById(R.id.txtIdNumber);
            txtNationality = itemView.findViewById(R.id.txtNationality);
            txtVehicleNumber = itemView.findViewById(R.id.txtVehicleNumber);
            txtVehicleType = itemView.findViewById(R.id.txtVehicleType);
        }
    }
}


