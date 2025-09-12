package com.provizit.AdapterAndModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.R;
import com.provizit.Utilities.Contractor;

import java.util.List;

public class ContractorDetailsAdapter extends RecyclerView.Adapter<ContractorDetailsAdapter.ViewHolder> {
    private List<ContractorData> subContractorList;

    public ContractorDetailsAdapter(List<ContractorData> list) {
        this.subContractorList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_details_contractor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContractorData contractor = subContractorList.get(position);
        holder.txtName.setText(contractor.getName());
        holder.txtEmail.setText(contractor.getEmail());
        holder.txtMobile.setText(contractor.getMobileData().internationalNumber);
        holder.txtSelectId.setText(contractor.getId_name());
        holder.txtNational.setText(contractor.getNationality());
        holder.txtIDNumber.setText(contractor.getId_number());
//        holder.txtAction.setText(contractor.get());
    }

    @Override
    public int getItemCount() {
        return subContractorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtMobile, txtSelectId, txtNational, txtIDNumber, txtAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtSelectId = itemView.findViewById(R.id.txtSelectId);
            txtNational = itemView.findViewById(R.id.txtNational);
            txtIDNumber = itemView.findViewById(R.id.txtIDNumber);
            txtAction = itemView.findViewById(R.id.txtAction);
        }
    }
}
